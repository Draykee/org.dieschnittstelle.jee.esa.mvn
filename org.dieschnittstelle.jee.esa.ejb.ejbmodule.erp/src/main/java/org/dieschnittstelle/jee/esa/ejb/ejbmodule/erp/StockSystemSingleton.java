package org.dieschnittstelle.jee.esa.ejb.ejbmodule.erp;

import org.apache.logging.log4j.Logger;
import org.dieschnittstelle.jee.esa.ejb.ejbmodule.erp.crud.*;
import org.dieschnittstelle.jee.esa.entities.erp.IndividualisedProductItem;
import org.dieschnittstelle.jee.esa.entities.erp.PointOfSale;
import org.dieschnittstelle.jee.esa.entities.erp.ProductAtPosPK;
import org.dieschnittstelle.jee.esa.entities.erp.StockItem;

import javax.ejb.EJB;
import javax.ejb.Remote;
import javax.ejb.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@Singleton
@Remote(StockSystemRemote.class)
public class StockSystemSingleton implements StockSystemRemote {

    protected static Logger logger = org.apache.logging.log4j.LogManager.getLogger(StockSystemSingleton.class);

    @EJB
    private StockItemCRUDLocal siCRUD;

    @EJB
    private ProductCRUDRemote prodCRUD;

    @EJB
    private PointOfSaleCRUDLocal posCRUD;

    @PersistenceContext(unitName = "erp_PU")
    private EntityManager em;


    @Override
    public void addToStock(IndividualisedProductItem product, long pointOfSaleId, int units) {
        PointOfSale pointOfSale = posCRUD.readPointOfSale(pointOfSaleId);
        StockItem stockItem = siCRUD.readStockItem(product, pointOfSale);
        if (stockItem == null) {
            siCRUD.createStockItem(new StockItem(product, pointOfSale, units));
        } else {
            stockItem.setUnits(stockItem.getUnits() + units);
            siCRUD.updateStockItem(stockItem);
        }
    }

    @Override
    public void removeFromStock(IndividualisedProductItem product, long pointOfSaleId, int units) {
        PointOfSale pointOfSale = posCRUD.readPointOfSale(pointOfSaleId);
        StockItem stockItem = siCRUD.readStockItem(product, pointOfSale);
        stockItem.setUnits(stockItem.getUnits() - units);
        siCRUD.updateStockItem(stockItem);
    }

    @Override
    public List<IndividualisedProductItem> getProductsOnStock(long pointOfSaleId) {
        return siCRUD.readAllStockItems()
                .stream()
                .filter(stockItem -> stockItem.getPos().getId() == pointOfSaleId)
                .map(stockItem -> stockItem.getProduct())
                .distinct()
                .collect(Collectors.toList());
    }

    @Override
    public List<IndividualisedProductItem> getAllProductsOnStock() {
        return siCRUD.readAllStockItems()
                .stream()
                .map(stockItem -> stockItem.getProduct())
                .distinct()
                .collect(Collectors.toList());
    }

    @Override
    public int getUnitsOnStock(IndividualisedProductItem product, long pointOfSaleId) {
        final PointOfSale pointOfSale = posCRUD.readPointOfSale(pointOfSaleId);
        StockItem stockItem = siCRUD.readStockItem(product, pointOfSale);
        return stockItem.getUnits();
    }

    @Override
    public int getTotalUnitsOnStock(IndividualisedProductItem product) {
        return siCRUD.readAllStockItems()
                .stream()
                .filter(stockItem -> stockItem.getProduct().equals(product))
                .mapToInt(value -> value.getUnits())
                .sum();
    }

    @Override
    public List<Long> getPointsOfSale(IndividualisedProductItem product) {
        return siCRUD.readAllStockItems()
                .stream()
                .filter(stockItem -> stockItem.getProduct().equals(product))
                .mapToLong(stockItem -> stockItem.getPos().getId())
                .boxed()
                .collect(Collectors.toList());
    }
}
