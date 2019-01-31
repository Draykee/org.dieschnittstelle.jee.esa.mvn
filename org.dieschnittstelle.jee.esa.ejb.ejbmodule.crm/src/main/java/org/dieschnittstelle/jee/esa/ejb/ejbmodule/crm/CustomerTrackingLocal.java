package org.dieschnittstelle.jee.esa.ejb.ejbmodule.crm;

import org.dieschnittstelle.jee.esa.entities.crm.CustomerTransaction;

import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Local
@Path("/tracking")
@Produces({MediaType.APPLICATION_JSON})
@Consumes({MediaType.APPLICATION_JSON})
public interface CustomerTrackingLocal {

	@POST
	public void createTransaction(CustomerTransaction transaction);

	@GET
	public List<CustomerTransaction> readAllTransactions();

}
