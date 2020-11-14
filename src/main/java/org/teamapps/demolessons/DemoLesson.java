package org.teamapps.demolessons;

import org.teamapps.ux.component.Component;

public interface DemoLesson {

    // Interface of a DemoLesson

    // a DemoLesson requires two methods.
    // getRootComponent and handleDemoSelected.

    // should return a Component with the Content of the DemoLesson
    Component getRootComponent();

    // This method is called every time the Demo is selected in the DemoLessonApp
    // default implementation in interface which does nothing.
    // with a default implementation you can omit this method in your DemoLesson class if you don't need it.
    default void handleDemoSelected() {

    }
}
