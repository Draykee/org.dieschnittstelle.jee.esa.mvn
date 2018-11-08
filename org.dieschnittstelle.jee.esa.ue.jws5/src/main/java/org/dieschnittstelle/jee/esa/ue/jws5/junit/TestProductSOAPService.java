package org.dieschnittstelle.jee.esa.ue.jws5.junit;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.dieschnittstelle.jee.esa.entities.erp.AbstractProduct;
import org.dieschnittstelle.jee.esa.entities.erp.IndividualisedProductItem;
import org.dieschnittstelle.jee.esa.entities.erp.ProductType;
import org.junit.Before;
import org.junit.Test;

/*
 * UE JWS5: rufen Sie hier den in JWS4 implementierten Web Service auf.
 */
public class TestProductSOAPService {

	private ProductCRUDSOAPClient client;

	private IndividualisedProductItem PRODUCT_1 = null;
	private IndividualisedProductItem PRODUCT_2 = null;

	@Before
	public void prepareContext() throws Exception {
		client = new ProductCRUDSOAPClient();
	}

	@Test
	public void crudWorks() {

		PRODUCT_1 = new IndividualisedProductItem();
		PRODUCT_1.setName("Schrippe");
		PRODUCT_1.setProductType(ProductType.ROLL);
		PRODUCT_1.setExpirationAfterStocked(480);

		PRODUCT_2 = new IndividualisedProductItem();
		PRODUCT_2.setName("Kirschplunder");
		PRODUCT_2.setProductType(ProductType.PASTRY);
		PRODUCT_2.setExpirationAfterStocked(720);

		List<?> prodlistBefore;
		// read all products
		assertNotNull("product list can be read",prodlistBefore = client.readAllProducts());

		/* CREATE + READ */
		// create two products
		client.createProduct(PRODUCT_1);
		client.createProduct(PRODUCT_2);

		assertEquals("product list is appended on create",2,client.readAllProducts().size()-prodlistBefore.size());

		// read the products and check whether they are equivalent
		AbstractProduct testProduct = client.readProduct(PRODUCT_1.getId());

		assertNotNull("new product can be read",testProduct);
		assertEquals("new product name is correct",PRODUCT_1.getName(),testProduct.getName());

		/* UPDATE */
		// change the local name
		PRODUCT_1.setName(PRODUCT_1.getName() + " " + PRODUCT_1.getName());
		// update the product on the server-side
		client.updateProduct(PRODUCT_1);

		// read out the product and compare the names
		testProduct = client.readProduct(PRODUCT_1.getId());
		assertEquals("product name is updated correctly",PRODUCT_1.getName(), testProduct.getName());

		/* DELETE */
		assertTrue("product can be deleted",  client.deleteProduct(PRODUCT_1.getId()));
		assertNull("deleted product does not exist anymore", client.readProduct(PRODUCT_1.getId()));
		assertEquals("product list is reduced on delete",prodlistBefore.size()+1,client.readAllProducts().size());

	}

}
