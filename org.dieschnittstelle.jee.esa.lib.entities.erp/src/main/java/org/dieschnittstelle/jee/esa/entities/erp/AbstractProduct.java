package org.dieschnittstelle.jee.esa.entities.erp;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import org.apache.logging.log4j.Logger;
import org.dieschnittstelle.jee.esa.entities.GenericCRUDEntity;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;

//@XmlAccessorType(XmlAccessType.FIELD)
//@XmlType(namespace = "http://dieschnittstelle.org/jee/esa/entities/erp")
/*
 * UE JRS3: entfernen Sie die Auskommentierung der Annotation
 */
//@JsonTypeInfo(use=JsonTypeInfo.Id.CLASS, include=JsonTypeInfo.As.PROPERTY, property="@class")

@Entity //For ORM in Task EJB
@SequenceGenerator(name = "product_sequence", sequenceName = "product_id_sequence")
public abstract class AbstractProduct implements Serializable, GenericCRUDEntity {

	protected static Logger logger = org.apache.logging.log4j.LogManager.getLogger(AbstractProduct.class);

	/**
	 * 
	 */
	private static final long serialVersionUID = 6940403029597060153L;

	@Id	//For ORM in Task EJB
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "product_sequence")
	private long id;

	private String name;
	
	private int price;

	public AbstractProduct() {
		logger.info("<constructor>");
	}

	public AbstractProduct(String name) {
		this.name = name;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

}
