package org.dieschnittstelle.jee.esa.ejb.client.ejbclients;

import org.dieschnittstelle.jee.esa.ejb.ejbmodule.erp.crud.ProductCRUDRemote;
import org.dieschnittstelle.jee.esa.ejb.ejbmodule.erp.crud.ProductCRUDStateless;
import org.dieschnittstelle.jee.esa.entities.erp.AbstractProduct;

import java.util.List;

import static org.dieschnittstelle.jee.esa.utils.Utils.show;

public class ProductCRUDClient implements ProductCRUDRemote {

    private ProductCRUDRemote ejbProxy;

    public ProductCRUDClient() throws Exception {
        // obtain a proxy specifying the ejb interface and uri. Let all subsequent methods use the proxy.
        this.ejbProxy = EJBProxyFactory.getInstance().getProxy(ProductCRUDStateless.class,
                "ejb:org.dieschnittstelle.jee.esa.ejb/org.dieschnittstelle.jee.esa.ejb.ejbmodule.erp/ProductCRUDStateless!org.dieschnittstelle.jee.esa.ejb.ejbmodule.erp.crud.ProductCRUDRemote");
        show("create ejb proxy %s of class %s", this.ejbProxy, this.ejbProxy.getClass());
    }

    public AbstractProduct createProduct(AbstractProduct prod) {
		AbstractProduct created = ejbProxy.createProduct(prod);
		// as a side-effect we set the id of the created product on the argument before returning
		//prod.setId(created.getId());
		return created;
    }

    public List<AbstractProduct> readAllProducts() {
        return ejbProxy.readAllProducts();
//        return null;
    }

    public AbstractProduct updateProduct(AbstractProduct update) {
        return ejbProxy.updateProduct(update);
//        return null;
    }

    public AbstractProduct readProduct(long productID) {
		return ejbProxy.readProduct(productID);
//        return null;
    }

    public boolean deleteProduct(long productID) {
		return ejbProxy.deleteProduct(productID);
//        return false;
    }

}
