package org.dieschnittstelle.jee.esa.ejb.client.demos;

import org.apache.logging.log4j.Logger;
import org.dieschnittstelle.jee.esa.ejb.client.ejbclients.EJBProxyFactory;
import org.dieschnittstelle.jee.esa.ejb.ejbmodule.crm.ShoppingException;
import org.dieschnittstelle.jee.esa.utils.Utils;
import org.dieschnittstelle.jee.esa.ejb.client.ejbclients.ProductCRUDClient;
import org.dieschnittstelle.jee.esa.ejb.client.ejbclients.StockSystemClient;
import org.dieschnittstelle.jee.esa.ejb.client.ejbclients.TouchpointAccessClient;

import static org.dieschnittstelle.jee.esa.ejb.client.Constants.*;
import static org.dieschnittstelle.jee.esa.utils.Utils.*;

public class ShowStockSystem {

	protected static Logger logger = org.apache.logging.log4j.LogManager.getLogger(ShowStockSystem.class);

	public static void main(String[] args) {
		EJBProxyFactory.initialise();

		try {
			(new ShowStockSystem()).runAll();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	// declare the attributes that will be instantiated with the ejb clients
	private ProductCRUDClient productCRUD;
	private StockSystemClient stockSystemClient;
	private TouchpointAccessClient touchpointCRUD;
	
	public ShowStockSystem() throws Exception {
		instantiateClients();
	}
	
	public void runAll() {

		try {
			createProducts();
			createTouchpoints();
			createStock();
			showStock();
		} catch (Exception e) {
			logger.error("got exception: " + e, e);
		}
	}

	private void showStock() {
		show("poss for prod1: " + stockSystemClient.getPointsOfSale(PRODUCT_1));
		show("poss for prod2: " + stockSystemClient.getPointsOfSale(PRODUCT_2));
		show("total units prod1: " + stockSystemClient.getTotalUnitsOnStock(PRODUCT_1));
		show("total units prod2: " + stockSystemClient.getTotalUnitsOnStock(PRODUCT_2));
	}

	public void instantiateClients() throws Exception {
		// instantiate the clients
		productCRUD = new ProductCRUDClient();
		stockSystemClient = new StockSystemClient();
		touchpointCRUD = new TouchpointAccessClient();
		
		System.out.println("\n***************** instantiated clients\n");
	}

	public void createProducts() {
		// create products
		productCRUD.createProduct(PRODUCT_1);
		Utils.step();
		productCRUD.createProduct(PRODUCT_2);
		productCRUD.createProduct(CAMPAIGN_1);
		productCRUD.createProduct(CAMPAIGN_2);
		
		show("created products: " + PRODUCT_1 + ", " + PRODUCT_2);
		show("all products: " + productCRUD.readAllProducts());
		
		Utils.step();

		System.out.println("\n***************** created products\n");
	}

	public void createTouchpoints() {
		// create touchpoints
		try {
			touchpointCRUD.createTouchpointAndPointOfSale(TOUCHPOINT_1);
			touchpointCRUD.createTouchpointAndPointOfSale(TOUCHPOINT_2);

			System.out.println("\n***************** created touchpoints\n");
		}
		catch (ShoppingException e) {
			throw new RuntimeException("createTouchpoints(): got exception: " + e,e);
		}
	}
	
	public void createStock() {
		// create stock
		stockSystemClient.addToStock(PRODUCT_1,
				TOUCHPOINT_1.getErpPointOfSaleId(), 100);
		stockSystemClient.addToStock(PRODUCT_1,
				TOUCHPOINT_2.getErpPointOfSaleId(), 90);
		stockSystemClient.addToStock(PRODUCT_2,
				TOUCHPOINT_1.getErpPointOfSaleId(), 80);
//		stockSystemClient.addToStock(PRODUCT_2,
//				TOUCHPOINT_2.getErpPointOfSaleId(), 100);

		System.out.println("\n***************** created stock\n");
	}

}
