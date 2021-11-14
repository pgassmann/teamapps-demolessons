package org.teamapps.demolessons.basics.p1_intro.l11_table;

import java.time.Instant;
import java.time.LocalDate;

public class Friend {

	private String firstName;
	private String lastName;
	private String street;
	private boolean active;
	private LocalDate birthDate;
	private Instant createdAt;
	private Meal favouriteMeal;

	public Friend() {
		this.createdAt = Instant.now();
	}

	public Friend(String firstName, String lastName, String street, boolean active, LocalDate birthDate, Meal favouriteMeal) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.street = street;
		this.active = active;
		this.birthDate = birthDate;
		this.favouriteMeal = favouriteMeal;
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

	public Meal getFavouriteMeal() {
		return favouriteMeal;
	}

	public void setFavouriteMeal(Meal favouriteMeal) {
		this.favouriteMeal = favouriteMeal;
	}
}
