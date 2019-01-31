package org.dieschnittstelle.jee.esa.ejb.ejbmodule.crm;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.dieschnittstelle.jee.esa.ejb.ejbmodule.crm.crud.CustomerTransactionCRUDLocal;
import org.dieschnittstelle.jee.esa.entities.crm.ShoppingCartItem;
import org.dieschnittstelle.jee.esa.entities.crm.CustomerTransaction;
import org.apache.logging.log4j.Logger;

/**
 * allows read/write access to a customer's shopping history
 */
@Stateless(name="customerTrackingSystem")
public class CustomerTrackingStateless implements CustomerTrackingRemote, CustomerTrackingLocal {

	protected static Logger logger = org.apache.logging.log4j.LogManager
			.getLogger(CustomerTrackingStateless.class);

	/**
	 * we use the local interface to the CustomerTransactionCRUD
	 */
	@EJB
	private CustomerTransactionCRUDLocal customerTransactionCRUD;
	
	
	public CustomerTrackingStateless() {
		logger.info("<constructor>: " + this);
	}

	public void createTransaction(CustomerTransaction transaction) {
		// in case of using the RESTful shopping cart implementation, product bundles will have been persisted and will
		// be passed with a non-default id. In order to allow a unified treatment, we will keep the respective OneToMany
		// relations to ShoppingCartItem and will reset their ids before creating the transaction
		for (ShoppingCartItem item : transaction.getItems()) {
			item.setId(0);
		}
		
		customerTransactionCRUD.createTransaction(transaction);
	}

	public List<CustomerTransaction> readAllTransactions() {
		//return transactions;
		return new ArrayList<CustomerTransaction>();
	}

	@PostConstruct
	public void initialise() {
		logger.info("@PostConstruct: customerTransactionCRUD is: " + customerTransactionCRUD);
	}

	@PreDestroy
	public void finalise() {
		logger.info("@PreDestroy");
	}

}
