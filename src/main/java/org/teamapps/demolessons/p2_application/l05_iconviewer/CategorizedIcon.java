package org.teamapps.demolessons.p2_application.l05_iconviewer;

import org.teamapps.icon.antu.AntuIconProvider;
import org.teamapps.icons.api.IconStyle;
import org.teamapps.icons.api.SimpleIcon;
import org.teamapps.icons.api.StyledIcon;

public class CategorizedIcon {
	private final String category;
	private final StyledIcon icon;

	public CategorizedIcon(String category, SimpleIcon icon, IconStyle style) {
		this.category = category;
		this.icon = new StyledIcon(AntuIconProvider.LIBRARY_ID, style.getStyleId(),icon.getIconName());
	}

	public String getCategory() {
		return category;
	}

	public StyledIcon getIcon() {
		return icon;
	}

	@Override
	public String toString() {
		return "AntuIcon." + getCategory() + "." + getIcon().getIconName();
	}
}