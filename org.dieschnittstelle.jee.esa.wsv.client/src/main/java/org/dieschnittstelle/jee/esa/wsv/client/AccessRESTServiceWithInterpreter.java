package org.dieschnittstelle.jee.esa.wsv.client;

import java.io.IOException;
import java.lang.reflect.Proxy;
import java.util.List;

import org.apache.logging.log4j.Logger;
import org.dieschnittstelle.jee.esa.entities.crm.Address;
import org.dieschnittstelle.jee.esa.entities.crm.StationaryTouchpoint;
import org.dieschnittstelle.jee.esa.wsv.client.service.ITouchpointCRUDService;

import org.dieschnittstelle.jee.esa.wsv.interpreter.JAXRSClientInterpreter;

import static org.dieschnittstelle.jee.esa.utils.Utils.*;

public class AccessRESTServiceWithInterpreter {

    protected static Logger logger = org.apache.logging.log4j.LogManager
            .getLogger(AccessRESTServiceWithInterpreter.class);

    /**
     * @param args
     */
    public static void main(String[] args) {

        /*
         * create a client for the web service using Proxy.newProxyInstance()
         *
         * create an instance of the invocation handler passing the service
         * interface and the base url
         */
        ITouchpointCRUDService serviceProxy = (ITouchpointCRUDService) Proxy.newProxyInstance(
                AccessRESTServiceWithInterpreter.class.getClassLoader(),
                new Class[]{ITouchpointCRUDService.class},
                new JAXRSClientInterpreter(ITouchpointCRUDService.class,
                        "http://localhost:8888/org.dieschnittstelle.jee.esa.jrs/api")
        );


        show("serviceProxy: " + serviceProxy);

        step();

        // 1) read out all touchpoints
        List<StationaryTouchpoint> tps = serviceProxy.readAllTouchpoints();
        show("read all: " + tps);


        //  comment-in the call to delete() once this is handled by the invocation handler
//		// 2) delete the touchpoint if there is one
		if (tps.size() > 0) {
          step();
			show("deleted: "
					+ serviceProxy.deleteTouchpoint(tps.get(0).getId()));
		}
//
//		// 3) create a new touchpoint
        step();

        Address addr = new Address("Luxemburger Strasse", "10", "13353",
                "Berlin");
        StationaryTouchpoint tp = new StationaryTouchpoint(-1,
                "BHT Verkaufsstand", addr);
        tp = (StationaryTouchpoint) serviceProxy.createTouchpoint(tp);
        show("created: " + tp);

        //  comment-in the call to read() once this is handled
//		/*
//		 * 4) read out the new touchpoint
//		 */
		show("read created: " + serviceProxy.readTouchpoint(tp.getId()));
//

        //  comment-in the call to update() once this is handled
//		/*
//		 * 5) update the touchpoint
//		 */
//		// change the name
		step();
		tp.setName("BHT Mensa");
		tp = serviceProxy.updateTouchpoint(tp.getId(), tp);
		show("updated: " + tp);

    }

    public static void step() {
        try {
            System.out.println("/>");
            System.in.read();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}

