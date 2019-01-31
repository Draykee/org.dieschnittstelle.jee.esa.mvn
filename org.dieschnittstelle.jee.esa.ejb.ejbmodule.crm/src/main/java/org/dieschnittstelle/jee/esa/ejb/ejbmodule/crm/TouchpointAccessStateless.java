package org.dieschnittstelle.jee.esa.ejb.ejbmodule.crm;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;

import org.dieschnittstelle.jee.esa.ejb.ejbmodule.crm.crud.TouchpointCRUDLocal;
import org.dieschnittstelle.jee.esa.entities.crm.AbstractTouchpoint;
import org.dieschnittstelle.jee.esa.entities.crm.ShoppingCartItem;
import org.dieschnittstelle.jee.esa.ejb.ejbmodule.erp.crud.PointOfSaleCRUDLocal;
import org.dieschnittstelle.jee.esa.entities.erp.PointOfSale;
import org.apache.logging.log4j.Logger;

@Stateless
@WebService(targetNamespace = "http://dieschnittstelle.org/jee/esa/jws",
		serviceName = "TouchpointAccessWebService",
		endpointInterface = "org.dieschnittstelle.jee.esa.ejb.ejbmodule.crm.TouchpointAccessRemote")
@SOAPBinding(parameterStyle = SOAPBinding.ParameterStyle.BARE)
public class TouchpointAccessStateless implements
		TouchpointAccessRemote, TouchpointAccessLocal {

	protected static Logger logger = org.apache.logging.log4j.LogManager
			.getLogger(TouchpointAccessStateless.class);

	@EJB
	private TouchpointCRUDLocal touchpointCRUD;

	@EJB
	private PointOfSaleCRUDLocal posCRUD;

	@Override
	public AbstractTouchpoint createTouchpointAndPointOfSale(
			AbstractTouchpoint touchpoint) throws ShoppingException {
//		logProductBundleKlass();

		// we first create the posCRUD
		PointOfSale pos = posCRUD.createPointOfSale(new PointOfSale());
		logger.info("createTouchpointAndPointOfSale(): created pointOfSale: "
				+ pos);

		// we pass the id to the touchpoint
		touchpoint.setErpPointOfSaleId(pos.getId());

		// then we persist the touchpoint
		touchpoint = touchpointCRUD.createTouchpoint(touchpoint);
		logger.info("createTouchpointAndPointOfSale(): created touchpoint: "
				+ touchpoint);

		// return it
		return touchpoint;
	}
	
	// for testing class loading
	private void logProductBundleKlass() {
		StringBuffer log = new StringBuffer();
		log.append(ShoppingCartItem.class + "\n");
		ClassLoader cl = ShoppingCartItem.class.getClassLoader();
		do {
			log.append("\t"+ cl + "\n");
			cl = cl.getParent();
		}
		while (cl != null);
		
		logger.info("class loader hierarchy of ShoppingCartItem is: \n" + log);
	}

	@Override
	public List<AbstractTouchpoint> readAllTouchpoints() {
		return touchpointCRUD.readAllTouchpoints();
	}

}
