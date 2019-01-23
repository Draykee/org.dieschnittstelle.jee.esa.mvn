package org.dieschnittstelle.jee.esa.entities.crm;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;

import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import org.apache.logging.log4j.Logger;
import org.dieschnittstelle.jee.esa.entities.GenericCRUDEntity;

/**
 * this is an abstraction over different touchpoints (with pos being the most
 * prominent one, others may be a service center, website, appsite, etc.)
 * 
 * @author kreutel
 * 
 */
// jaxb annotations
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(namespace = "http://dieschnittstelle.org/jee/esa/entities/crm/ws")
@XmlSeeAlso({StationaryTouchpoint.class,MobileTouchpoint.class})

// jpa annotations
@Entity
// inheritance
@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name="touchpointType", discriminatorType=DiscriminatorType.STRING)
//@Inheritance(strategy=InheritanceType.TABLE_PER_CLASS);
//@Inheritance(strategy=InheritanceType.JOINED);
@SequenceGenerator(name = "touchpoint_sequence", sequenceName = "touchpoint_id_sequence")

// jrs/jackson annotations
@JsonTypeInfo(use=JsonTypeInfo.Id.CLASS, include=JsonTypeInfo.As.PROPERTY, property="@class")
@JsonIgnoreProperties(ignoreUnknown = true)
public abstract class AbstractTouchpoint implements Serializable, GenericCRUDEntity {

	protected static Logger logger = org.apache.logging.log4j.LogManager.getLogger(AbstractTouchpoint.class);


	/**
	 * 
	 */
	private static final long serialVersionUID = 5207353251688141788L;

	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "touchpoint_sequence")
	protected long id;

	/**
	 * the id of the PointOfSale from the erp data
	 */
	protected long erpPointOfSaleId;

	/**
	 * the name of the touchpoint
	 */
	protected String name;

	/*
	 * UE JWS2: kommentieren Sie @XmlTransient aus
	 */
	@XmlTransient
	@ManyToMany
	private Collection<Customer> customers = new HashSet<Customer>();
	
	@XmlTransient
	@OneToMany(mappedBy="touchpoint")
	private Collection<CustomerTransaction> transactions;


	public AbstractTouchpoint() {
		logger.info("<constructor>");
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getErpPointOfSaleId() {
		return erpPointOfSaleId;
	}

	public void setErpPointOfSaleId(long erpPointOfSaleId) {
		this.erpPointOfSaleId = erpPointOfSaleId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	// this annotation must be set on accessor methods rather than on the attributes themselves
	@JsonIgnore
	public Collection<Customer> getCustomers() {
		return customers;
	}

	public void setCustomers(HashSet<Customer> customers) {
		this.customers = customers;
	}

	public void addCustomer(Customer customer) {
		this.customers.add(customer);
	}

	public boolean equals(Object obj) {

		if (obj == null || !(obj instanceof AbstractTouchpoint)) {
			return false;
		}

		return this.getId() == ((AbstractTouchpoint) obj).getId();
	}

	@JsonIgnore
	public Collection<CustomerTransaction> getTransactions() {
		return transactions;
	}

	public void setTransactions(Collection<CustomerTransaction> transactions) {
		this.transactions = transactions;
	}

}
