package org.teamapps.demolessons.p5_hibernate_sql.models;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "customers")
public class Customer implements Serializable {
	public final static String FIELD_ID = "id";
	public final static String FIELD_FORENAME = "forename";
	public final static String FIELD_SURNAME = "surname";
	public final static String FIELD_EMAIL = "email";

	@Column(name = "customer_id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Id
	private Integer id;

	@Column(name = "forename")
	private String forename;

	@Column(name = "surname")
	private String surname;

	@Column(name = "email")
	private String email;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getForename() {
		return forename;
	}

	public void setForename(String forename) {
		this.forename = forename;
	}

	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
}
