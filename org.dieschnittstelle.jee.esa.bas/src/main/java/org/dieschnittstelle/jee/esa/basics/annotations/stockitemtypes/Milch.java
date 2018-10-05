package org.dieschnittstelle.jee.esa.basics.annotations.stockitemtypes;

import org.dieschnittstelle.jee.esa.basics.annotations.*;

@StockItem
public class Milch {

	@Units
	@DisplayAs( value = "Flaschen" )
	private int menge;

	@Brandname
	private String markenname;

	public int getMenge() {
		return menge;
	}

	public void setMenge(int menge) {
		this.menge = menge;
	}

	public String getMarkenname() {
		return markenname;
	}

	public void setMarkenname(String markenname) {
		this.markenname = markenname;
	}

	@Purchase
	public void kaufen(int unitsToPurchase) {
		if (unitsToPurchase > this.menge) {
			throw new RuntimeException(
					"You cannot purchase more than what is available. Got: "
							+ unitsToPurchase
							+ " units to purchase, but have available only: "
							+ this.menge);
		}
		this.menge -= unitsToPurchase;
	}

	@Initialise
	public void lagern(int units, String brandname) {
		this.markenname = brandname;
		this.menge = units;
	}
	
	/**
	 * our own toString method
	 */
	public String toString() {
		return "<Milch " + this.markenname + " " + this.menge + ">";
	}

}
