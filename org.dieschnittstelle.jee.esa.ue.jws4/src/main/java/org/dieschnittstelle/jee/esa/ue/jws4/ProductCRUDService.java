package org.dieschnittstelle.jee.esa.ue.jws4;

import org.apache.logging.log4j.Logger;
import org.dieschnittstelle.jee.esa.entities.GenericCRUDExecutor;
import org.dieschnittstelle.jee.esa.entities.erp.AbstractProduct;
import org.dieschnittstelle.jee.esa.entities.erp.IndividualisedProductItem;

import javax.annotation.Resource;
import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.servlet.ServletContext;
import javax.xml.ws.WebServiceContext;
import javax.xml.ws.handler.MessageContext;
import java.util.List;

/*
 * UE JWS4: machen Sie die Funktionalitaet dieser Klasse als Web Service verfuegbar und verwenden Sie fuer
 * die Umetzung der Methoden die Instanz von GenericCRUDExecutor<AbstractProduct>,
 * die Sie aus dem ServletContext auslesen koennen
 */
@WebService(targetNamespace = "http://dieschnittstelle.org/jee/esa/jws",
        name = "IProductCRUDService",
        serviceName = "ProductCRUDWebService",
        portName = "ProductCRUDPort")
public class ProductCRUDService {

    protected static Logger logger = org.apache.logging.log4j.LogManager
            .getLogger(ProductCRUDService.class);

    @Resource
    private WebServiceContext wscontext;

    public ProductCRUDService() {
        logger.info("<constructor>");
    }

    @WebMethod
    public List<AbstractProduct> readAllProducts() {

        logger.info("readAllProducts()");

        // we obtain the servlet context from the wscontext
        ServletContext ctx = (ServletContext) wscontext.getMessageContext()
                .get(MessageContext.SERVLET_CONTEXT);
        logger.info("readAllProducts(): servlet context is: " + ctx);

        GenericCRUDExecutor<AbstractProduct> productCRUD = (GenericCRUDExecutor<AbstractProduct>) ctx
                .getAttribute("productCRUD");
        logger.info("readAllProducts(): read productCRUD from servletContext: "
                + productCRUD);

        return productCRUD.readAllObjects();

    }

    @WebMethod
    public AbstractProduct createProduct(AbstractProduct product) {

        // obtain the CRUD executor from the servlet context
        GenericCRUDExecutor<AbstractProduct> productCRUD = (GenericCRUDExecutor<AbstractProduct>) ((ServletContext) wscontext
                .getMessageContext().get(MessageContext.SERVLET_CONTEXT))
                .getAttribute("productCRUD");

        return productCRUD.createObject(product);
    }

    @WebMethod
    public AbstractProduct updateProduct(AbstractProduct update) {

        GenericCRUDExecutor<AbstractProduct> productCRUD = (GenericCRUDExecutor<AbstractProduct>) ((ServletContext) wscontext
                .getMessageContext().get(MessageContext.SERVLET_CONTEXT))
                .getAttribute("productCRUD");
        return productCRUD.updateObject(update);
    }

    @WebMethod
    public boolean deleteProduct(long id) {
        GenericCRUDExecutor<AbstractProduct> productCRUD = (GenericCRUDExecutor<AbstractProduct>) ((ServletContext) wscontext
                .getMessageContext().get(MessageContext.SERVLET_CONTEXT))
                .getAttribute("productCRUD");
        return productCRUD.deleteObject(id);
    }

    @WebMethod
    public IndividualisedProductItem readProduct(long id) {
        GenericCRUDExecutor<AbstractProduct> productCRUD = (GenericCRUDExecutor<AbstractProduct>) ((ServletContext) wscontext
                .getMessageContext().get(MessageContext.SERVLET_CONTEXT))
                .getAttribute("productCRUD");
        return (IndividualisedProductItem) productCRUD.readObject(id);
    }

}
