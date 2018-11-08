package org.dieschnittstelle.jee.esa.jws.client;

import java.io.IOException;
import java.util.List;

import org.apache.logging.log4j.Logger;

import org.dieschnittstelle.jee.esa.entities.crm.ws.AbstractTouchpoint;
import org.dieschnittstelle.jee.esa.entities.crm.ws.StationaryTouchpoint;
import org.dieschnittstelle.jee.esa.jws.*;

import javax.xml.ws.Response;

public class ShowTouchpointSOAPService {

	protected static Logger logger = org.apache.logging.log4j.LogManager
			.getLogger(ShowTouchpointSOAPService.class);

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		// we may access the service synchronously or asynchronously
		boolean async = false;

		try {

			// create an instance of the client-side web service class
			TouchpointCRUDWebService service = new TouchpointCRUDWebService();
			// obtain an interface to the operations provided by the service
			ITouchpointCRUDService serviceProxy = service.getTouchpointCRUDPort();

			// 1) read out all touchpoints
			List<AbstractTouchpoint> touchpoints = null;
			if (async) {
				logger.info("read async...");
				Response<ReadAllTouchpointsResponse> readallResponse = serviceProxy.readAllTouchpointsAsync();
				// get() results in a synchronous access to the response content, i.e. here execution will be blocked
				touchpoints = readallResponse.get().getReturn();
			}
			else {
				touchpoints = serviceProxy
						.readAllTouchpoints();
			}
			logger.info("read touchpoints: " + touchpoints);

			// 2) delete the touchpoint after next console input
			if (touchpoints != null && touchpoints.size() > 0) {
				step();

				StationaryTouchpoint tp = (StationaryTouchpoint) touchpoints
						.get(0);
				if (async) {
					logger.info("delete async...");
					Response<DeleteTouchpointResponse> deleteResponse = serviceProxy.deleteTouchpointAsync(tp.getId());
					logger.info("delete async: received response future: " + deleteResponse);
					deleteResponse.get();
					logger.info("delete async: received response value");
				}
				else {
					serviceProxy.deleteTouchpoint(tp.getId());
				}
				logger.info("deleted touchpoint: " + tp);
			} else {
				logger.warn("no touchpoints available for deletion...");
			}

			// 3) create a new touchpoint
			step();

			Address addr = new Address();
			addr.setStreet("Luxemburger Strasse");
			addr.setHouseNr("10");
			addr.setZipCode("13353");
			addr.setCity("Berlin");
			StationaryTouchpoint tp = new StationaryTouchpoint();
			tp.setId(-1);
			tp.setName("BHT SOAP Store");
			tp.setAddress(addr);

			if (async) {
				logger.info("create async...");
				Response<CreateTouchpointResponse> createResponse = serviceProxy.createTouchpointAsync(tp);
				tp = (StationaryTouchpoint)createResponse.get().getReturn();
			}
			else {
				tp = (StationaryTouchpoint) serviceProxy.createTouchpoint(tp);
			}
			logger.info("created touchpoint: " + tp);

			/*
			 * 4) wait for input and...
			 */
			step();
			// change the name
			tp.setName("BHT Mensa");

			/*
			 * UE JWS3: add a call to the update method of the web service, passing tp
			 */

			logger.info("TestTouchpointSOAPService: done.\n");
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/*
	 * this is duplicated here as we do not want to introduce any dependencies to ESA-specific artifacts
	 */
	private static void step() {
		try {
			System.out.println("/>");
			System.in.read();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
