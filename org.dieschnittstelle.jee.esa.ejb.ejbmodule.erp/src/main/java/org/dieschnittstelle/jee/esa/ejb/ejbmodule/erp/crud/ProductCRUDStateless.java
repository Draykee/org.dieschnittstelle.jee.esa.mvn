package org.dieschnittstelle.jee.esa.ejb.ejbmodule.erp.crud;

import org.dieschnittstelle.jee.esa.entities.erp.AbstractProduct;
import org.dieschnittstelle.jee.esa.entities.erp.IndividualisedProductItem;

import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

import static org.dieschnittstelle.jee.esa.utils.Utils.show;

@Stateless
//@Remote(ProductCRUDRemote.class) // when ProductCRUDRemote misses @Remote annotation
//@WebService(endpointInterface = "org.dieschnittstelle.jee.esa.ejb.ejbmodule.erp.crud.ProductCRUDRemote")
//@SOAPBinding
//@XmlSeeAlso(IndividualisedProductItem.class)
public class ProductCRUDStateless implements ProductCRUDRemote, ProductCRUDLocal {

    @PersistenceContext(unitName = "erp_PU") //dependency injection for PersistenceUnit
    private EntityManager em;

    @Override
    //@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public AbstractProduct createProduct(AbstractProduct prod) {
        show("AbstractProduct createProduct( %s )", prod);
        if (em.contains(prod))
            return em.merge(prod);
        else
            em.persist(prod);
        return prod;
    }

    @Override
    public List<AbstractProduct> readAllProducts() {
        return em.createQuery("select p from AbstractProduct as p").getResultList();
    }

    @Override
    public AbstractProduct updateProduct(AbstractProduct update) {
        return em.merge(update);
    }

    @Override
    public AbstractProduct readProduct(long productID) {
        return em.find(AbstractProduct.class, productID);
    }

    @Override
    public boolean deleteProduct(long productID) {
        AbstractProduct abstractProduct = em.find(AbstractProduct.class, productID);
        em.remove(abstractProduct);
        return !em.contains(abstractProduct);
    }
}
