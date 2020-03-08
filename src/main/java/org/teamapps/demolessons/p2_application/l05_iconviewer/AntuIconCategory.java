package org.teamapps.demolessons.p2_application.l05_iconviewer;

import org.teamapps.icon.antu.AntuIcon;
import org.teamapps.icons.api.Icon;

enum AntuIconCategory {

    ACTION(AntuIcon.ACTION.class, AntuIcon.ACTION.SEARCH_24),
    STATUS(AntuIcon.STATUS.class, AntuIcon.STATUS.SECURITY_HIGH_22),
    PLACES(AntuIcon.PLACES.class, AntuIcon.PLACES.FOLDER_BLACK_32),
    EMOTES(AntuIcon.EMOTES.class, AntuIcon.EMOTES.FACE_COOL_22),
    EMBLEMS(AntuIcon.EMBLEMS.class, AntuIcon.EMBLEMS.VCS_NORMAL_16),
    DEVICES(AntuIcon.DEVICES.class, AntuIcon.DEVICES.DRIVE_REMOVABLE_MEDIA_22),
    CATEGORIES(AntuIcon.CATEGORIES.class, AntuIcon.CATEGORIES.APPLICATIONS_EDUCATION_UNIVERSITY_32),
    APPS(AntuIcon.APPS.class, AntuIcon.APPS.APPLICATIONS_GRAPHICS_48),
    APPLETS(AntuIcon.APPLETS.class, AntuIcon.APPLETS.WEATHERWIDGET_256)
    ;

    private String categoryName;
    private Icon categoryIcon;

    AntuIconCategory(Class iconClass, Icon categoryIcon) {
        this.categoryIcon = categoryIcon;
        this.categoryName = this.name();
        this.iconClass = iconClass;
        this.categoryIcon = categoryIcon;
    }

    private Class iconClass;

    public String getCategoryName() {
        return categoryName;
    }
    public Class getIconClass() {
        return iconClass;
    }
    public Icon getCategoryIcon() { return categoryIcon;   }

}
