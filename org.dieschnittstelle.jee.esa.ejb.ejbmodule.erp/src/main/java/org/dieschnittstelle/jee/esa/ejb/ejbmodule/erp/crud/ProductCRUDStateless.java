package org.dieschnittstelle.jee.esa.ejb.ejbmodule.erp.crud;

import org.dieschnittstelle.jee.esa.entities.erp.AbstractProduct;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

import static org.dieschnittstelle.jee.esa.utils.Utils.show;

@Stateless
//Alternative: @Remote(ProductCRUDRemote.class) when ProductCRUDRemote misses @Remote annotation
public class ProductCRUDStateless implements ProductCRUDRemote {

    @PersistenceContext(unitName = "erp_PU") //dependency injection for PersistenceUnit
    private EntityManager em;

    @Override
    public AbstractProduct createProduct(AbstractProduct prod) {
        show("em is: %s of class: %s", em, em.getClass());
        em.persist(prod);
        return prod;
    }

    @Override
    public List<AbstractProduct> readAllProducts() {
        Query query = em.createQuery("select p from AbstractProduct as p");
        return query.getResultList();
    }

    @Override
    public AbstractProduct updateProduct(AbstractProduct update) {
        return update;
    }

    @Override
    public AbstractProduct readProduct(long productID) {
        return em.find(AbstractProduct.class, productID);
    }

    @Override
    public boolean deleteProduct(long productID) {
        em.remove(productID);
        return em.contains(productID);
    }
}
