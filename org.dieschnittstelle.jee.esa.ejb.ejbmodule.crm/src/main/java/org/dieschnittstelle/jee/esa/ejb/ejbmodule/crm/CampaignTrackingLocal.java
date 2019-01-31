package org.dieschnittstelle.jee.esa.ejb.ejbmodule.crm;

import org.dieschnittstelle.jee.esa.entities.crm.AbstractTouchpoint;
import org.dieschnittstelle.jee.esa.entities.crm.CampaignExecution;

import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Local
@Path("/campaigns")
@Produces({MediaType.APPLICATION_JSON})
@Consumes({MediaType.APPLICATION_JSON})
public interface CampaignTrackingLocal {

	@POST
	public void addCampaignExecution(CampaignExecution campaign);

	// usage of PUT here is due to the fact that we will neither refactor the previous ejb interfaces nor their
	// implementations for Stateless and Singleton beans, hence we need to be able to pass the touchpoint object
	// via the body of the request
	@PUT
	@Path("/{campaignId}/check-is-valid")
	public int existsValidCampaignExecutionAtTouchpoint(@PathParam("campaignId") long erpProductId,
                                                        AbstractTouchpoint tp);

	// we specify the amount of units to be purchased passing a query parameter
	@PUT
	@Path("/{campaignId}/purchase")
	public void purchaseCampaignAtTouchpoint(@PathParam("campaignId") long erpProductId,
                                             AbstractTouchpoint tp, @QueryParam("units") int units);

	@GET
	public List<CampaignExecution> getAllCampaignExecutions();
}
