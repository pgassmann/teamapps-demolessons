package org.teamapps.demolessons.basics.p1_intro.l08_combobox;

import org.teamapps.data.extract.BeanPropertyExtractor;
import org.teamapps.demolessons.common.DemoLesson;
import org.teamapps.icon.material.MaterialIcon;
import org.teamapps.server.jetty.embedded.TeamAppsJettyEmbeddedServer;
import org.teamapps.ux.component.Component;
import org.teamapps.ux.component.field.Label;
import org.teamapps.ux.component.field.combobox.ComboBox;
import org.teamapps.ux.component.flexcontainer.VerticalLayout;
import org.teamapps.ux.component.panel.Panel;
import org.teamapps.ux.component.rootpanel.RootPanel;
import org.teamapps.ux.component.template.BaseTemplate;
import org.teamapps.ux.session.SessionContext;
import org.teamapps.webcontroller.WebController;

import java.util.Arrays;

public class ComboBoxDemo implements DemoLesson {

    private Component rootComponent;

    // Constructor, only set session context instance variable
    public ComboBoxDemo(SessionContext sessionContext) {

        Panel panel = new Panel(MaterialIcon.LIGHTBULB_OUTLINE, "ComboBox Demo");
        rootComponent = panel;

        VerticalLayout verticalLayout = new VerticalLayout();
        panel.setContent(verticalLayout);

        /* ComboBox of Strings*/
        verticalLayout.addComponent(new Label("ComboBox of Strings"));
        ComboBox<String> stringComboBox = ComboBox.createForList(Arrays.asList("Cake", "Spaghetti", "Fries", "Icecream", "Pizza", "Chocolate"));
        stringComboBox.onValueChanged.addListener(s -> sessionContext.showNotification(MaterialIcon.ARROW_DROP_DOWN, s));
        stringComboBox.setShowClearButton(true);
        verticalLayout.addComponent(stringComboBox);

        /* ComboBox of Objects*/
        verticalLayout.addComponent(new Label("ComboBox of Objects"));
        ComboBox<Meal> mealComboBox = ComboBox.createForList(Arrays.asList(
                new Meal(MaterialIcon.CAKE, "Cake", "100 kcal"),
                new Meal(MaterialIcon.GESTURE, "Spaghetti", "100 kcal"),
                new Meal(MaterialIcon.CHILD_CARE, "Fries", "100 kcal"),
                new Meal(MaterialIcon.AC_UNIT, "Icecream", "100 kcal"),
                new Meal(MaterialIcon.BLUR_CIRCULAR, "Pizza", "100 kcal"),
                new Meal(MaterialIcon.LOCAL_CAFE, "Coffee", "100 kcal"),
                new Meal(MaterialIcon.LANDSCAPE, "Chocolate", "100 kcal")
        ));
        verticalLayout.addComponent(mealComboBox);

        /* Display objects using a Template */
        mealComboBox.setTemplate(BaseTemplate.LIST_ITEM_MEDIUM_ICON_TWO_LINES);
        /* BaseTemplate.LIST_ITEM_MEDIUM_ICON_TWO_LINES uses icon, caption and description */

        /* Map Caption and Description to properties of Meal */
        /* Bean Property Extractor automatically maps Fields with the same Name.
        Meal already has an Icon Property.
        Other Properties can be mapped manually:  */
        BeanPropertyExtractor<Meal> extractor = new BeanPropertyExtractor<>();
        extractor.addProperty("caption", Meal::getName);
        extractor.addProperty("description", Meal::getCalories);
        mealComboBox.setPropertyExtractor(extractor);

        /* Define string representation of object for search and autocomplete */
        mealComboBox.setRecordToStringFunction(Meal::getName);
        mealComboBox.setAutoComplete(true);
    }

    public Component getRootComponent(){
        return rootComponent;
    }

    // This method is called every time the Demo is selected in the DemoLessonApp
    public void handleDemoSelected() { }


    // main method to launch the Demo standalone
    public static void main(String[] args) throws Exception {
        WebController controller = sessionContext -> {
            RootPanel rootPanel = new RootPanel();
            sessionContext.addRootPanel(null, rootPanel);

            // create new instance of the Demo Class
            DemoLesson demo = new ComboBoxDemo(sessionContext);

            // call the method defined in the DemoLesson Interface
            demo.handleDemoSelected();

            rootPanel.setContent(demo.getRootComponent());
        };
        new TeamAppsJettyEmbeddedServer(controller).start();
    }
}