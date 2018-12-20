package org.dieschnittstelle.jee.esa.ejb.ejbmodule.erp.crud;

import org.dieschnittstelle.jee.esa.entities.erp.IndividualisedProductItem;
import org.dieschnittstelle.jee.esa.entities.erp.PointOfSale;
import org.dieschnittstelle.jee.esa.entities.erp.ProductAtPosPK;
import org.dieschnittstelle.jee.esa.entities.erp.StockItem;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Stateless
public class StockItemCRUDStateless implements StockItemCRUDLocal {

    @PersistenceContext(unitName = "erp_PU") //dependency injection for PersistenceUnit
    private EntityManager em;

    @Override
    public StockItem createStockItem(StockItem item) {
        return em.merge(item); //Needs to be merged, in case that product is not known yet
    }

    @Override
    public StockItem readStockItem(IndividualisedProductItem prod, PointOfSale pos) {
        return em.find(StockItem.class,new ProductAtPosPK(prod,pos));
    }

    @Override
    public StockItem updateStockItem(StockItem item) {
        return em.merge(item);
    }

    @Override
    public List<StockItem> readAllStockItems() {
        return em.createQuery("select p from StockItem as p").getResultList();
    }

    @Override
    public List<StockItem> readStockItemsForProduct(IndividualisedProductItem prod) {
        return em.createQuery("SELECT si FROM StockItem si WHERE st.id.product="+prod.getId()).getResultList();
    }

    @Override
    public List<StockItem> readStockItemsForPointOfSale(PointOfSale pos) {
        return em.createQuery("SELECT si FROM StockItem si WHERE st.id.pos="+pos.getId()).getResultList();
    }
}
