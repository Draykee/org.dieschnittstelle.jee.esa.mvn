package org.dieschnittstelle.jee.esa.ejb.ejbmodule.crm.shopping;

import org.apache.logging.log4j.Logger;
import org.dieschnittstelle.jee.esa.ejb.ejbmodule.crm.*;
import org.dieschnittstelle.jee.esa.ejb.ejbmodule.erp.StockSystemLocal;
import org.dieschnittstelle.jee.esa.ejb.ejbmodule.erp.StockSystemRemote;
import org.dieschnittstelle.jee.esa.ejb.ejbmodule.erp.crud.ProductCRUDLocal;
import org.dieschnittstelle.jee.esa.ejb.ejbmodule.erp.crud.ProductCRUDRemote;
import org.dieschnittstelle.jee.esa.entities.crm.AbstractTouchpoint;
import org.dieschnittstelle.jee.esa.entities.crm.Customer;
import org.dieschnittstelle.jee.esa.entities.crm.CustomerTransaction;
import org.dieschnittstelle.jee.esa.entities.crm.ShoppingCartItem;
import org.dieschnittstelle.jee.esa.entities.erp.AbstractProduct;
import org.dieschnittstelle.jee.esa.entities.erp.Campaign;
import org.dieschnittstelle.jee.esa.entities.erp.IndividualisedProductItem;

import javax.ejb.EJB;
import javax.ejb.Remote;
import javax.ejb.Stateful;
import java.util.List;


/**
 * <h1>${CLASS}</h1>
 * [Description]
 *
 * @author Kevin Mattutat
 * @version 10.01.2019
 * @since 10.01.2019
 */
@Remote(ShoppingSessionFacadeRemote.class)
@Stateful
public class ShoppingSessionFacadeRemoteStateful implements ShoppingSessionFacadeRemote {

    protected static Logger logger = org.apache.logging.log4j.LogManager.getLogger(ShoppingSessionFacadeRemoteStateful.class);

    /*
     * the three beans that are used
     * @EJB is used for dependency injection
     */
    @EJB
    private ShoppingCartLocal shoppingCart;

    @EJB
    private CustomerTrackingLocal customerTracking;

    @EJB
    private CampaignTrackingLocal campaignTracking;

    /*
     * ProductCRUD and StockSystem is required for the purchase
     */
    @EJB
    private ProductCRUDLocal productCRUD;

    @EJB
    private StockSystemLocal stockSystem;


    /**
     * the customer
     */
    private Customer customer;

    /**
     * the touchpoint
     */
    private AbstractTouchpoint touchpoint;

    public void setTouchpoint(AbstractTouchpoint touchpoint) {
        this.touchpoint = touchpoint;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public void addProduct(AbstractProduct product, int units) {
        this.shoppingCart.addItem(new ShoppingCartItem(product.getId(), units, product instanceof Campaign));
    }

    /*
     * verify whether campaigns are still valid
     */
    public void verifyCampaigns() throws ShoppingException {
        if (this.customer == null || this.touchpoint == null) {
            throw new RuntimeException("cannot verify campaigns! No touchpoint has been set!");
        }

        for (ShoppingCartItem item : this.shoppingCart.getItems()) {
            if (item.isCampaign()) {
                int availableCampaigns = this.campaignTracking.existsValidCampaignExecutionAtTouchpoint(
                        item.getErpProductId(), this.touchpoint);
                logger.info("got available campaigns for product " + item.getErpProductId() + ": "
                        + availableCampaigns);
                // we check whether we have sufficient campaign items available
                if (availableCampaigns < item.getUnits()) {
                    throw new ShoppingException("verifyCampaigns() failed for productBundle " + item
                            + " at touchpoint " + this.touchpoint + "! Need " + item.getUnits()
                            + " instances of campaign, but only got: " + availableCampaigns);
                }
            }
        }
    }

    public void purchase() throws ShoppingException {
        logger.info("purchase()");

        if (this.customer == null || this.touchpoint == null) {
            throw new RuntimeException(
                    "cannot commit shopping session! Either customer or touchpoint has not been set: " + this.customer
                            + "/" + this.touchpoint);
        }

        // verify the campaigns
        verifyCampaigns();

        // remove the products from stock
        checkAndRemoveProductsFromStock();

        // then we add a new customer transaction for the current purchase
        List<ShoppingCartItem> products = this.shoppingCart.getItems();
        CustomerTransaction transaction = new CustomerTransaction(this.customer, this.touchpoint, products);
        transaction.setCompleted(true);
        customerTracking.createTransaction(transaction);

        logger.info("purchase(): done.\n");
    }

    /*
     * to be implemented as server-side method for PAT2
     */
    private void checkAndRemoveProductsFromStock() {
        logger.info("checkAndRemoveProductsFromStock");

        for (ShoppingCartItem item : this.shoppingCart.getItems()) {

            // Ermitteln Sie das AbstractProduct für das gegebene ShoppingCartItem.
            // Nutzen Sie dafür dessen erpProductId und die ProductCRUD EJB

            AbstractProduct abstractProduct = this.productCRUD.readProduct(item.getErpProductId());//??


            if (item.isCampaign()) {
                logger.info("checkAndRemoveProductsFromStock: Found campaign item!");
                this.campaignTracking.purchaseCampaignAtTouchpoint(item.getErpProductId(), this.touchpoint,
                        item.getUnits());
                // TODO: wenn Sie eine Kampagne haben, muessen Sie hier
                // 1) ueber die ProductBundle Objekte auf dem Campaign Objekt iterieren, und
                Campaign campaign = (Campaign) abstractProduct;//Transient.. ??
                // 2) fuer jedes ProductBundle das betreffende Produkt in der auf dem Bundle angegebenen Anzahl,
                campaign.getBundles().forEach(productBundle -> {
                    // multipliziert mit dem Wert von item.getUnits() aus dem Warenkorb,
                    // - hinsichtlich Verfuegbarkeit ueberpruefen, und
                    // - falls verfuegbar, aus dem Warenlager entfernen - nutzen Sie dafür die StockSystem EJB
                    // (Anm.: item.getUnits() gibt Ihnen Auskunft darüber, wie oft ein Produkt, im vorliegenden Fall eine Kampagne, im
                    // Warenkorb liegt)
                    int amount = item.getUnits() * productBundle.getUnits();
                    int unitsOnStock = this.stockSystem.getUnitsOnStock(
                            productBundle.getProduct(),
                            this.touchpoint.getErpPointOfSaleId()
                    );
                    logger.info("checkAndRemoveProductsFromStock: Purchasing " + amount + "/" + unitsOnStock + " of " + productBundle);
                    if (amount <= unitsOnStock) {
                        this.stockSystem.removeFromStock(
                                productBundle.getProduct(),
                                this.touchpoint.getErpPointOfSaleId(),
                                amount
                        );
                    }

                });

            } else {
                // TODO: andernfalls (wenn keine Kampagne vorliegt) muessen Sie
                // 1) das Produkt in der in item.getUnits() angegebenen Anzahl hinsichtlich Verfuegbarkeit ueberpruefen und

                int unitsOnStock = this.stockSystem.getUnitsOnStock(
                        (IndividualisedProductItem) abstractProduct,
                        this.touchpoint.getErpPointOfSaleId()
                );
                logger.info("checkAndRemoveProductsFromStock: Purchasing " + item.getUnits() + "/" + unitsOnStock + " of " + item);
                // 2) das Produkt, falls verfuegbar, in der entsprechenden Anzahl aus dem Warenlager entfernen
                if (item.getUnits() <= unitsOnStock) {
                    this.stockSystem.removeFromStock(
                            (IndividualisedProductItem) abstractProduct,
                            this.touchpoint.getErpPointOfSaleId(),
                            item.getUnits()
                    );
                }
            }

        }
    }

}
/***********************************************************************************************
 *
 *                  All rights reserved, SpaceParrots UG (c) copyright 2019
 *
 ***********************************************************************************************/