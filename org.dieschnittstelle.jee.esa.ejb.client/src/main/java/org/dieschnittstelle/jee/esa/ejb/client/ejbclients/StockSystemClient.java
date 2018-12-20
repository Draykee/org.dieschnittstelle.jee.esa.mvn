package org.dieschnittstelle.jee.esa.ejb.client.ejbclients;

import org.dieschnittstelle.jee.esa.ejb.ejbmodule.erp.StockSystemSingleton;
import org.dieschnittstelle.jee.esa.ejb.ejbmodule.erp.StockSystemRemote;
import org.dieschnittstelle.jee.esa.entities.erp.IndividualisedProductItem;

import java.util.List;

import static org.dieschnittstelle.jee.esa.utils.Utils.show;

public class StockSystemClient implements StockSystemRemote {

	private StockSystemRemote ejbProxy;
	
	public StockSystemClient() throws Exception {
		this.ejbProxy = EJBProxyFactory.getInstance().getProxy(StockSystemSingleton.class,
				"ejb:org.dieschnittstelle.jee.esa.ejb/org.dieschnittstelle.jee.esa.ejb.ejbmodule.erp/StockSystemSingleton!org.dieschnittstelle.jee.esa.ejb.ejbmodule.erp.StockSystemRemote");
		show("create ejb proxy %s of class %s", this.ejbProxy, this.ejbProxy.getClass());
	}
	
	
	@Override
	public void addToStock(IndividualisedProductItem product, long pointOfSaleId, int units) {
		this.ejbProxy.addToStock(product, pointOfSaleId, units);
	}

	@Override
	public void removeFromStock(IndividualisedProductItem product, long pointOfSaleId,
			int units) {
		this.ejbProxy.removeFromStock(product, pointOfSaleId, units);
	}

	@Override
	public List<IndividualisedProductItem> getProductsOnStock(long pointOfSaleId) {
		return this.ejbProxy.getProductsOnStock(pointOfSaleId);
	}

	@Override
	public List<IndividualisedProductItem> getAllProductsOnStock() {
		return this.ejbProxy.getAllProductsOnStock();
	}

	@Override
	public int getUnitsOnStock(IndividualisedProductItem product, long pointOfSaleId) {
		return this.ejbProxy.getUnitsOnStock(product, pointOfSaleId);
	}

	@Override
	public int getTotalUnitsOnStock(IndividualisedProductItem product) {
		return this.ejbProxy.getTotalUnitsOnStock(product);
	}

	@Override
	public List<Long> getPointsOfSale(IndividualisedProductItem product) {
		return this.ejbProxy.getPointsOfSale(product);
	}


}
