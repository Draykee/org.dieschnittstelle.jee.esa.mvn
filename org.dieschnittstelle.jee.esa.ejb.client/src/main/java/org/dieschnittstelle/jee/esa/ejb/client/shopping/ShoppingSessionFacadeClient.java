package org.dieschnittstelle.jee.esa.ejb.client.shopping;

import org.apache.logging.log4j.Logger;
import org.dieschnittstelle.jee.esa.ejb.client.ejbclients.EJBProxyFactory;
import org.dieschnittstelle.jee.esa.ejb.ejbmodule.crm.CampaignTrackingRemote;
import org.dieschnittstelle.jee.esa.ejb.ejbmodule.crm.ShoppingException;
import org.dieschnittstelle.jee.esa.ejb.ejbmodule.crm.shopping.ShoppingSessionFacadeRemote;
import org.dieschnittstelle.jee.esa.entities.crm.AbstractTouchpoint;
import org.dieschnittstelle.jee.esa.entities.crm.Customer;
import org.dieschnittstelle.jee.esa.entities.erp.AbstractProduct;

public class ShoppingSessionFacadeClient implements ShoppingBusinessDelegate {

    protected static Logger logger = org.apache.logging.log4j.LogManager
            .getLogger(ShoppingSessionFacadeClient.class);

    /*
     * use a proxy for the ShoppingSessionFacadeRemote interface
     */
    private ShoppingSessionFacadeRemote ejbProxy;

    public ShoppingSessionFacadeClient() {
        /* instantiate the proxy using the EJBProxyFactory (see the other client classes) */
        this.ejbProxy = EJBProxyFactory.getInstance().getProxy(ShoppingSessionFacadeRemote.class,
                "ejb:org.dieschnittstelle.jee.esa.ejb/org.dieschnittstelle.jee.esa.ejb.ejbmodule.crm/ShoppingSessionFacadeRemoteStateful!org.dieschnittstelle.jee.esa.ejb.ejbmodule.crm.shopping.ShoppingSessionFacadeRemote?stateful");
    }

    @Override
    public void setTouchpoint(AbstractTouchpoint touchpoint) {
        this.ejbProxy.setTouchpoint(touchpoint);
    }

    @Override
    public void setCustomer(Customer customer) {
        this.ejbProxy.setCustomer(customer);
    }

    @Override
    public void addProduct(AbstractProduct product, int units) {
        this.ejbProxy.addProduct(product, units);
    }

    @Override
    public void purchase() throws ShoppingException {
        this.ejbProxy.purchase();
    }

}
