package org.teamapps.demolessons.p1_intro.l11_table;

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


	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}

		Meal meal = (Meal) o;

		if (icon != null ? !icon.equals(meal.icon) : meal.icon != null) {
			return false;
		}
		if (name != null ? !name.equals(meal.name) : meal.name != null) {
			return false;
		}
		return calories != null ? calories.equals(meal.calories) : meal.calories == null;
	}

	@Override
	public int hashCode() {
		int result = icon != null ? icon.hashCode() : 0;
		result = 31 * result + (name != null ? name.hashCode() : 0);
		result = 31 * result + (calories != null ? calories.hashCode() : 0);
		return result;
	}
}
