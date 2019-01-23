package org.dieschnittstelle.jee.esa.wsv.interpreter;


import java.io.ByteArrayOutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import org.apache.http.client.methods.*;
import org.apache.logging.log4j.Logger;

import javax.ws.rs.*;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;

import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.util.EntityUtils;

import org.dieschnittstelle.jee.esa.utils.Http;
import org.dieschnittstelle.jee.esa.wsv.interpreter.json.JSONObjectSerialiser;

import static org.dieschnittstelle.jee.esa.utils.Utils.*;


/*
 * TODO: implement this class such that the crud operations declared on ITouchpointCRUDService in .esa.wsv can be successfully called from the class AccessRESTServiceWithInterpreter in the .esa.wsv.client project
 */
public class JAXRSClientInterpreter implements InvocationHandler {

    // use a logger
    protected static Logger logger = org.apache.logging.log4j.LogManager.getLogger(JAXRSClientInterpreter.class);

    // declare a baseurl
    private String baseurl;

    // declare a common path segment
    private String commonPath;

    // use our own implementation JSONObjectSerialiser
    private JSONObjectSerialiser jsonSerialiser = new JSONObjectSerialiser();

    // use an attribute that holds the serviceInterface (useful, e.g. for providing a toString() method)
    private Class serviceInterface;

    // use a constructor that takes an annotated service interface and a baseurl. the implementation should read out the path annotation, we assume we produce and consume json, i.e. the @Produces and @Consumes annotations will not be considered here
    public JAXRSClientInterpreter(Class serviceInterface, String baseurl) {

        // implement the constructor!
        this.serviceInterface = serviceInterface;
        Path annotation = (Path) this.serviceInterface.getAnnotation(Path.class);
        if (annotation == null) {
            logger.info("Annotation Path not found on " + serviceInterface);
            return;
        }
        this.baseurl = baseurl;
        this.commonPath = annotation.value();

        logger.info("<constructor>: " + serviceInterface + " / " + baseurl + " / " + commonPath);
    }

    // implement this method interpreting jax-rs annotations on the meth argument
    @Override
    public Object invoke(Object proxy, Method meth, Object[] args)
            throws Throwable {

        // check whether we handle the toString method and give some appropriate return value
        if (meth.getName().equals("toString")) {
            return "Proxy for Service interface: " + serviceInterface;
        }

        // use a default http client
        HttpClient client = Http.createSyncClient();

        // create the url using baseurl and commonpath (further segments may be added if the method has an own @Path annotation)
        // check whether we have a path annotation and append the url (path params will be handled when looking at the method arguments)
        String url = baseurl + commonPath + (meth.isAnnotationPresent(Path.class) ? meth.getAnnotation(Path.class).value() : "");
        //Enthält /{touchpoint} als pfad -> dieser muss durch das argument ersetzt werden
        logger.info("Base: " + url);
        // a value that needs to be sent via the http request body
        Object bodyValue = null;

        // check whether we have method arguments - only consider pathparam annotations (if any) on the first argument here - if no args are passed, the value of args is null! if no pathparam annotation is present assume that the argument value is passed via the body of the http request
        if (args != null && args.length > 0) {
            if (meth.getParameterAnnotations()[0].length > 0 &&
                    meth.getParameterAnnotations()[0][0].annotationType() == PathParam.class) {
                // handle PathParam on the first argument - do not forget that in this case we might have a second
                // argument providing a bodyValue
                // String paramName = ((PathParam) meth.getParameterAnnotations()[0][0]).value();
                logger.info("Length:" + args.length);
                //Send entity in body (stored in second argument)
                bodyValue = args.length > 1 ? args[1] : null;
                // if we have a path param, we need to replace the corresponding pattern in the url with the parameter value
                url = url.replace(meth.getAnnotation(Path.class).value(), "/" + String.valueOf(args[0]));
                logger.info("Replaced: " + url);
            } else {
                // if we do not have a path param, we assume the argument value will be sent via the body of the request
                bodyValue = args[0];
            }
        }

        // declare a HttpUriRequest variable
        HttpUriRequest request = null;
        // check which of the http method annotation is present and instantiate request accordingly passing the url
        if (meth.isAnnotationPresent(POST.class)) {
            request = new HttpPost(url);
            logger.info("POST @ " + url);
        } else if (meth.isAnnotationPresent(GET.class)) {
            request = new HttpGet(url);
            logger.info("GET @ " + url);
        } else if (meth.isAnnotationPresent(DELETE.class)) {
            request = new HttpDelete(url);
            logger.info("DELETE @ " + url);
        } else if (meth.isAnnotationPresent(PUT.class)) {
            request = new HttpPut(url);
            logger.info("PUT @ " + url);
        } else {
            throw new UnsupportedOperationException("connot handle meethod call of: " + meth + ". Only POST and GET is supported.");
        }


        // add a header on the request declaring that we accept json (for header names, you can use the constants declared in javax.ws.rs.core.HttpHeaders, for content types use the constants from javax.ws.rs.core.MediaType;)
        request.setHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON);

        // if we need to send the method argument in the request body we need to declare an entity
        ByteArrayEntity bae = null;

        // if a body shall be sent, convert the bodyValue to json, create an entity from it and set it on the request
        if (bodyValue != null) {

            // use a ByteArrayOutputStream for writing json
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            // write the object to the stream using the jsonSerialiser
            jsonSerialiser.writeObject(bodyValue, bos);
            // create an ByteArrayEntity from the stream's content
            bae = new ByteArrayEntity(bos.toByteArray());
            // set the entity on the request, which must be cast to HttpEntityEnclosingRequest
            ((HttpEntityEnclosingRequest) request).setEntity(bae);
            // and add a content type header for the request
            request.setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON);
        }

        logger.info("invoke(): executing request: " + request);

        // then send the request to the server and get the response
        HttpResponse response = client.execute(request);

        logger.info("invoke(): received response: " + response);

        // check the response code
        if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {

            // declare a variable for the return value
            Object returnValue;

            // convert the resonse body to a java object of an appropriate type considering the return type of
            // the method and set the object as value of returnValue
            //
            // if the return type of the mis a generic type, getGenericReturnType() will return a non null result,
            // otherwise use getReturnType()
            Type returnType = meth.getGenericReturnType() != null ? meth.getGenericReturnType() : meth.getReturnType();

            returnValue = new JSONObjectSerialiser().readObject(response.getEntity().getContent(), returnType);

            // don't forget to cleanup the entity using EntityUtils.consume()
            if (bae != null) {
                EntityUtils.consume(bae);
            }

            // and return the return value
            logger.info("invoke(): returning value: " + returnValue);
            return returnValue;

        } else {
            throw new RuntimeException("Got unexpected status from server: " + response.getStatusLine());
        }
    }

}