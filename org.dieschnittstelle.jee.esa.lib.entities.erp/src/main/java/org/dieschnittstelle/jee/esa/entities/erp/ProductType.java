package org.dieschnittstelle.jee.esa.entities.erp;

import com.fasterxml.jackson.annotation.JsonCreator;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(namespace = "http://dieschnittstelle.org/jee/esa/entities/erp")
public enum ProductType {

	BREAD, ROLL, PASTRY;
	
	@JsonCreator
	public static ProductType deserialise(String pt) {	
		return ProductType.valueOf(ProductType.class,pt);
	}
}
