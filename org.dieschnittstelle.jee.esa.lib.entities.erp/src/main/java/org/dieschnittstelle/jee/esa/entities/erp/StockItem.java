package org.dieschnittstelle.jee.esa.entities.erp;

import javax.persistence.*;

import org.apache.logging.log4j.Logger;

@Entity //For ORM in Task EJB
@Table(name = "stock")
@IdClass(ProductAtPosPK.class)
public class StockItem {

	protected static Logger logger = org.apache.logging.log4j.LogManager.getLogger(StockItem.class);

	@Id
	@ManyToOne
	private PointOfSale pos;

	@Id
	@ManyToOne
	private IndividualisedProductItem product;

	private int price;
	
	private int units;

	public StockItem() {
		logger.info("<constructor>");
	}

	public StockItem(IndividualisedProductItem product,
			PointOfSale pos, int units) {
		this.product = product;
		this.pos = pos;
		this.units = units;
	}

	public PointOfSale getPos() {
		return pos;
	}

	public void setPos(PointOfSale pos) {
		this.pos = pos;
	}

	public IndividualisedProductItem getProduct() {
		return product;
	}

	public void setProduct(IndividualisedProductItem product) {
		this.product = product;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	/*
	 * the lifecycle log messages
	 */

	@PostLoad
	public void onPostLoad() {
		logger.info("@PostLoad(): " + this);
	}

	@PostPersist
	public void onPostPersist() {
		logger.info("@PostPersist(): " + this);
	}

	@PostRemove
	public void onPostRemove() {
		logger.info("@PostRemove(): " + this);
	}

	@PostUpdate
	public void onPostUpdate() {
		logger.info("@PostUpdate(): " + this);
	}

	@PrePersist
	public void onPrePersist() {
		logger.info("@PrePersist(): " + this);
	}

	@PreRemove
	public void onPreRemove() {
		logger.info("@PreRemove(): " + this);
	}

	@PreUpdate
	public void onPreUpdate() {
		logger.info("@PreUpdate(): " + this);
	}

	public String toString() {
		return "<StockItem " + this.price + ", " + this.product + "@"
				+ this.pos + ":" + this.units + ">";
	}

	public int getUnits() {
		return units;
	}

	public void setUnits(int units) {
		this.units = units;
	}

}
