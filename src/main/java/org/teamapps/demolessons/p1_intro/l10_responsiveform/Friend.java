package org.teamapps.demolessons.p1_intro.l10_responsiveform;

import java.time.Instant;
import java.time.LocalDate;

public class Friend {

	private String firstName;
	private String lastName;
	private String street;
	private boolean active;
	private LocalDate birthDate;
	private Instant createdAt;

	public Friend() {
		this.createdAt = Instant.now();
	}

	public Friend(String firstName, String lastName, String street, boolean active, LocalDate birthDate) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.street = street;
		this.active = active;
		this.birthDate = birthDate;
		this.createdAt = Instant.now();
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public LocalDate getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(LocalDate birthDate) {
		this.birthDate = birthDate;
	}

	@Override
	public String toString() {
		return "Friend{" +
				"firstName='" + firstName + '\'' +
				", lastName='" + lastName + '\'' +
				", street='" + street + '\'' +
				", isActive=" + active +
				", birthDate=" + birthDate +
				'}';
	}

	public Instant getCreatedAt() {
		return createdAt;
	}
}
