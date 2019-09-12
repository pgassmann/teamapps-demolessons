package org.teamapps.demo.lessons.l08_combobox;

import org.teamapps.icon.material.MaterialIcon;

public class Meal {

	private MaterialIcon icon;
	private String name;
	private String calories;

	public Meal(MaterialIcon icon, String name, String calories) {
		this.icon = icon;
		this.name = name;
		this.calories = calories;
	}

	public MaterialIcon getIcon() {
		return icon;
	}

	public void setIcon(MaterialIcon icon) {
		this.icon = icon;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCalories() {
		return calories;
	}

	public void setCalories(String calories) {
		this.calories = calories;
	}

}
