package org.dieschnittstelle.jee.esa.ejb.ejbmodule.erp;

import org.apache.logging.log4j.Logger;
import org.dieschnittstelle.jee.esa.entities.erp.IndividualisedProductItem;
import org.dieschnittstelle.jee.esa.entities.erp.PointOfSale;
import org.dieschnittstelle.jee.esa.entities.erp.ProductAtPosPK;
import org.dieschnittstelle.jee.esa.entities.erp.StockItem;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Stateless
public class StockSystemCRUDStateless implements StockSystemRemote
{

    protected static Logger logger = org.apache.logging.log4j.LogManager.getLogger( StockSystemCRUDStateless.class );

    @PersistenceContext(unitName = "erp_PU")
    private EntityManager em;

    @Override
    public void addToStock( IndividualisedProductItem product, long pointOfSaleId, int units )
    {
        PointOfSale pointOfSale = em.find( PointOfSale.class, pointOfSaleId );
        StockItem stockItem = new StockItem( product, pointOfSale, units );
        em.persist( stockItem );
    }

    @Override
    public void removeFromStock( IndividualisedProductItem product, long pointOfSaleId, int units )
    {
        PointOfSale pointOfSale = new PointOfSale();
        pointOfSale.setId( pointOfSaleId );
        int index = units;


        while ( index-- > 0 )
        {
            em.remove( em.find( StockItem.class, new ProductAtPosPK( product, pointOfSale ) ) );
        }
    }

    @Override
    public List<IndividualisedProductItem> getProductsOnStock( long pointOfSaleId )
    {
        //TODO: Join with AbstractProduct Table?
        return em.createQuery( "SELECT ap FROM AbstractProduct ap, StockItem st WHERE ap.id=" + pointOfSaleId + " AND st.id.product=ap.id" ).getResultList();
    }

    @Override
    public List<IndividualisedProductItem> getAllProductsOnStock()
    {
        return em.createQuery( "SELECT ap FROM AbstractProduct ap, StockItem st WHERE st.id.product=ap.id" ).getResultList();
    }

    @Override
    public int getUnitsOnStock( IndividualisedProductItem product, long pointOfSaleId )
    {
        PointOfSale pointOfSale = em.find( PointOfSale.class, pointOfSaleId );
        ProductAtPosPK pk = new ProductAtPosPK( product, pointOfSale );
        StockItem stockItem = em.find( StockItem.class, pk );
        return stockItem.getUnits();
    }

    @Override
    public int getTotalUnitsOnStock( IndividualisedProductItem product )
    {
        return em.createQuery( "SUM(st.units) FROM StockItem st WHERE st.product=" + product.getId() ).getFirstResult();
    }

    @Override
    public List<Long> getPointsOfSale( IndividualisedProductItem product )
    {
        return em.createQuery( "SELECT st.pos FROM StockItem st WHERE st.product=" + product.getId() ).getResultList();
    }
}
