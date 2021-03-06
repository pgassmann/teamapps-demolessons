package org.teamapps.demolessons.p5_hibernate_sql.models;

import org.teamapps.ux.component.field.multicurrency.CurrencyField;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "invoices")
public class Invoice implements Serializable {
	public final static String FIELD_ID = "id";
	public final static String FIELD_IDENTIFIER = "identifier";
	public final static String FIELD_CUSTOMER = "customer";
	public final static String FIELD_SUM = "sum";
	public final static String FIELD_CREATED = "created";

	@Column(name = "invoice_id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Id
	private Integer id;

	@Column(name = "identifier")
	private String identifier;

	@JoinColumn(name = "customer_id", referencedColumnName = "customer_id")
	@ManyToOne(fetch = FetchType.LAZY)
	private Customer customer;

	@Column(name = "sum", precision = 10, scale = 2)
	// TODO: Don't do this in production code, use CurrencyValue and custom mappers/extractors !
	private Double sum;

	@Column(name = "created")
	private LocalDateTime created;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getIdentifier() {
		return identifier;
	}

	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public Double getSum() {
		return sum;
	}

	public void setSum(Double sum) {
		this.sum = sum;
	}

	public LocalDateTime getCreated() {
		return created;
	}

	public void setCreated(LocalDateTime created) {
		this.created = created;
	}
}
