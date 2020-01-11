package org.teamapps.demolessons;

import org.teamapps.ux.component.Component;

public interface DemoLesson {

    Component getRootComponent();

    // This method is called every time the Demo is selected in the DemoLessonApp
    default void handleDemoSelected() {

    }
}
