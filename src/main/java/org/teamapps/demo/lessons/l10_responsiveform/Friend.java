package org.teamapps.demo.lessons.l10_responsiveform;

import java.time.Instant;
import java.time.LocalDate;

public class Friend {

	private String firstName;
	private String lastName;
	private String street;
	private boolean isActive;
	private LocalDate birthDate;
	private Instant createdAt;

	public Friend() {
		this.createdAt = Instant.now();
	}

	public Friend(String firstName, String lastName, String street, boolean isActive, LocalDate birthDate) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.street = street;
		this.isActive = isActive;
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
		return isActive;
	}

	public void setActive(boolean active) {
		this.isActive = active;
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
				", isActive=" + isActive +
				", birthDate=" + birthDate +
				'}';
	}

	public Instant getCreatedAt() {
		return createdAt;
	}
}
