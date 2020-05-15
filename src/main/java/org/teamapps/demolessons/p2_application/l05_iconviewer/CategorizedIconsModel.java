package org.teamapps.demolessons.p2_application.l05_iconviewer;

import org.teamapps.icon.antu.AntuIconProvider;
import org.teamapps.icons.api.IconStyle;
import org.teamapps.icons.api.SimpleIcon;
import org.teamapps.ux.component.infiniteitemview.AbstractInfiniteItemViewModel;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class CategorizedIconsModel extends AbstractInfiniteItemViewModel<CategorizedIcon> {

	private List<CategorizedIcon> categorizedIcons;
	private String filterString;
	private List<AntuIconCategory> iconCategories;
	private IconStyle iconStyle;

	public CategorizedIconsModel(List<AntuIconCategory> iconCategories) {
		this.iconCategories = iconCategories;
		updateIcons();
	}

	public void setIconCategories(List<AntuIconCategory> iconCategories) {
		this.iconCategories = iconCategories;
		updateIcons();
	}

	public void setFilterString(String filterString) {
		this.filterString = filterString;
		updateIcons();
	}

	public void setIconStyle(IconStyle iconStyle) {
		this.iconStyle = iconStyle;
		updateIcons();
	}

	private void updateIcons() {
		List<CategorizedIcon> categorizedIcons =
				iconCategories.stream()
				.flatMap(iconCategory -> Arrays.stream(iconCategory.getIconClass().getEnumConstants())
							.map(icon -> {
								if (iconStyle != null) {
									return new CategorizedIcon(iconCategory.getCategoryName(), icon.toString(),  (SimpleIcon) icon, iconStyle);
								} else {
									return new CategorizedIcon(iconCategory.getCategoryName(), icon.toString(), (SimpleIcon) icon, AntuIconProvider.STANDARD);
								}
							})
				).filter(categorizedIcon -> {
					return filterString == null || categorizedIcon.getIcon().toString().toLowerCase().contains(filterString.toLowerCase());
				})
				.collect(Collectors.toList());
		this.categorizedIcons = categorizedIcons;
		onAllDataChanged.fire();
	}

	@Override
	public int getCount() {
		return categorizedIcons.size();
	}

	@Override
	public List<CategorizedIcon> getRecords(int startIndex, int length) {
		return categorizedIcons.stream()
				.skip(startIndex)
				.limit(length)
				.collect(Collectors.toList());
	}
}