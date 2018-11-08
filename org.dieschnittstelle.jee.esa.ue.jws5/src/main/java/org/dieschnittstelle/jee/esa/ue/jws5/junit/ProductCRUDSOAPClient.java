package org.dieschnittstelle.jee.esa.ue.jws5.junit;

import org.apache.logging.log4j.Logger;
import org.dieschnittstelle.jee.esa.entities.erp.AbstractProduct;
import org.dieschnittstelle.jee.esa.entities.erp.IndividualisedProductItem;
import org.dieschnittstelle.jee.esa.jws.IProductCRUDService;
import org.dieschnittstelle.jee.esa.jws.ProductCRUDWebService;

import java.util.List;

public class ProductCRUDSOAPClient {

	private IProductCRUDService serviceProxy;

	protected static Logger logger = org.apache.logging.log4j.LogManager.getLogger(ProductCRUDSOAPClient.class);

	public ProductCRUDSOAPClient() throws Exception {

		// create an instance of the client-side web service class
		ProductCRUDWebService service = new ProductCRUDWebService();
		// obtain an interface to the operations provided by the service
		serviceProxy = service.getProductCRUDPort();
	}

	public AbstractProduct createProduct(IndividualisedProductItem prod) {
		AbstractProduct created = serviceProxy.createProduct(prod);
		// as a side-effect we set the id of the created product on the argument before returning
		prod.setId(created.getId());
		return created;
	}

	public List<?> readAllProducts() {
		return serviceProxy.readAllProducts();
	}

	public AbstractProduct updateProduct(AbstractProduct update) {
		return serviceProxy.updateProduct(update);
	}

	public boolean deleteProduct(long id) {
		return serviceProxy.deleteProduct(id);
	}

	public AbstractProduct readProduct(long id) {
		return serviceProxy.readProduct(id);
	}

}
