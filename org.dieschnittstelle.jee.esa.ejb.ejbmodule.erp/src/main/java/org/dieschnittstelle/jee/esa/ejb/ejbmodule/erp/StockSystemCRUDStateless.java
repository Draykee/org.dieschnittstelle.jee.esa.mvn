package org.dieschnittstelle.jee.esa.ejb.ejbmodule.erp;

import org.dieschnittstelle.jee.esa.entities.erp.IndividualisedProductItem;

import javax.ejb.Stateless;
import java.util.List;

@Stateless
public class StockSystemCRUDStateless implements StockSystemRemote{

    @Override
    public void addToStock(IndividualisedProductItem product, long pointOfSaleId, int units) {

    }

    @Override
    public void removeFromStock(IndividualisedProductItem product, long pointOfSaleId, int units) {

    }

    @Override
    public List<IndividualisedProductItem> getProductsOnStock(long pointOfSaleId) {
        return null;
    }

    @Override
    public List<IndividualisedProductItem> getAllProductsOnStock() {
        return null;
    }

    @Override
    public int getUnitsOnStock(IndividualisedProductItem product, long pointOfSaleId) {
        return 0;
    }

    @Override
    public int getTotalUnitsOnStock(IndividualisedProductItem product) {
        return 0;
    }

    @Override
    public List<Long> getPointsOfSale(IndividualisedProductItem product) {
        return null;
    }
}
