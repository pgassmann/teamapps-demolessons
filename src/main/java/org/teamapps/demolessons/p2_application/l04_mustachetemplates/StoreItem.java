package org.teamapps.demolessons.p2_application.l04_mustachetemplates;

import java.time.LocalDate;

public class StoreItem {

	private final String name;
	private final String imageUrl;
	private final double price;
	private final LocalDate releaseDate;

	public StoreItem(String name, String imageUrl, double price, LocalDate releaseDate) {
		this.name = name;
		this.imageUrl = imageUrl;
		this.price = price;
		this.releaseDate = releaseDate;
	}

	public String getName() {
		return name;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public double getPrice() {
		return price;
	}

	public LocalDate getReleaseDate() {
		return releaseDate;
	}
}
