package org.teamapps.demolessons.p2_application.l05_iconviewer;

import org.teamapps.icons.api.Icon;

public class CategorizedIcon {
	private final String category;
	private final Icon icon;

	public CategorizedIcon(String category, Icon icon) {
		this.category = category;
		this.icon = icon;
	}

	public String getCategory() {
		return category;
	}

	public Icon getIcon() {
		return icon;
	}

	@Override
	public String toString() {
		return "AntuIcon." + getCategory() + "." + getIcon().toString();
	}
}