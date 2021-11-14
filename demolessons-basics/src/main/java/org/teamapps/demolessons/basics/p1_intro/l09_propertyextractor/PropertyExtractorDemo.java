package org.teamapps.demolessons.basics.p1_intro.l09_propertyextractor;

import org.teamapps.data.extract.BeanPropertyExtractor;
import org.teamapps.demolessons.common.DemoLesson;
import org.teamapps.icon.material.MaterialIcon;
import org.teamapps.server.jetty.embedded.TeamAppsJettyEmbeddedServer;
import org.teamapps.ux.component.Component;
import org.teamapps.ux.component.field.Label;
import org.teamapps.ux.component.field.combobox.ComboBox;
import org.teamapps.ux.component.field.combobox.TagBoxWrappingMode;
import org.teamapps.ux.component.field.combobox.TagComboBox;
import org.teamapps.ux.component.flexcontainer.VerticalLayout;
import org.teamapps.ux.component.panel.Panel;
import org.teamapps.ux.component.rootpanel.RootPanel;
import org.teamapps.ux.component.template.BaseTemplate;
import org.teamapps.ux.session.SessionContext;
import org.teamapps.webcontroller.WebController;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PropertyExtractorDemo implements DemoLesson {

    private Component rootComponent;

    // Constructor, only set session context instance variable
    public PropertyExtractorDemo(SessionContext sessionContext) {

        Panel panel = new Panel(MaterialIcon.LIGHTBULB_OUTLINE, "Property Extractor and (Tag)ComboBox Demo");
        rootComponent = panel;
        VerticalLayout verticalLayout = new VerticalLayout();
        panel.setContent(verticalLayout);

        // List of Meals
        List<Meal> myMeals = Arrays.asList(
                new Meal(MaterialIcon.CAKE, "Cake", "100 kcal"),
                new Meal(MaterialIcon.GESTURE, "Spaghetti", "203 kcal"),
                new Meal(MaterialIcon.CHILD_CARE, "Fries", "300 kcal"),
                new Meal(MaterialIcon.AC_UNIT, "Icecream", "430 kcal"),
                new Meal(MaterialIcon.BLUR_CIRCULAR, "Pizza", "540 kcal"),
                new Meal(MaterialIcon.LOCAL_CAFE, "Coffee", "623 kcal"),
                new Meal(MaterialIcon.LANDSCAPE, "Chocolate", "700 kcal")
        );

        /* Dropdown with myMeals*/
        verticalLayout.addComponent(new Label("ComboBox with manual Property mapping"));
        ComboBox<Meal> mealComboBox = ComboBox.createForList(myMeals);
        verticalLayout.addComponent(mealComboBox);
        mealComboBox.setShowClearButton(true);
        mealComboBox.setTemplate(BaseTemplate.LIST_ITEM_MEDIUM_ICON_TWO_LINES);

        /* Manual Property Mapping */
        /* The BaseTemplate.LIST_ITEM_MEDIUM_ICON_TWO_LINES uses three properties
        * caption, description and Icon.
        * instead of using strings, the properties can be accessed by public variables of the BaseTemplate class */
        mealComboBox.setPropertyExtractor((meal, propertyName) -> switch (propertyName) {
            case BaseTemplate.PROPERTY_CAPTION -> meal.getName();
            case BaseTemplate.PROPERTY_DESCRIPTION -> meal.getCalories();
            case BaseTemplate.PROPERTY_ICON -> meal.getIcon();
            default -> null;
        });

        // Preselect first Meal
        mealComboBox.setValue(myMeals.get(1));

        /* Define string representation of object for search and autocomplete */
        mealComboBox.setRecordToStringFunction(meal -> meal.getName()+" ("+ meal.getCalories()+")");

        mealComboBox.onValueChanged.addListener(s -> sessionContext.showNotification(s.getIcon(), s.getName()));


        /* Second Combobox uses a BeanPropertyExtractor */
        /* Bean Property Extractor */
        verticalLayout.addComponent(new Label("ComboBox with BeanPropertyExtractor"));
        ComboBox<Meal> mealComboBox2 = ComboBox.createForList(myMeals);
        verticalLayout.addComponent(mealComboBox2);
        mealComboBox2.setShowClearButton(true);
        mealComboBox2.setTemplate(BaseTemplate.LIST_ITEM_MEDIUM_ICON_TWO_LINES);

        /* Map Caption and Description to properties of Meal */
        /* Bean Property Extractor automatically maps Fields with the same Name.
        Meal already has an Icon Property.
        Other Properties can be mapped manually:  */
        BeanPropertyExtractor<Meal> mealPropertyExtractor = new BeanPropertyExtractor<>();
        mealPropertyExtractor.addProperty("caption", Meal::getName);
        mealPropertyExtractor.addProperty( BaseTemplate.PROPERTY_DESCRIPTION, Meal::getCalories);
        mealComboBox2.setPropertyExtractor(mealPropertyExtractor);

        /* Define string representation of object for search and autocomplete */
        mealComboBox2.setRecordToStringFunction(meal -> meal.getName()+" ("+ meal.getCalories()+")");


        /* TagComboBox */
        // TagComboBox allows selection of multiple items and displays them as Tags
        verticalLayout.addComponent(new Label("TagComboBox with dynamic Content. Write your favourite Meals. Max 3."));
        TagComboBox<Meal> mealTagComboBox = new TagComboBox<>();
        verticalLayout.addComponent(mealTagComboBox);

        mealTagComboBox.setWrappingMode(TagBoxWrappingMode.MULTI_LINE);
        mealTagComboBox.setMaxEntries(3);
        mealTagComboBox.setTemplate(BaseTemplate.LIST_ITEM_MEDIUM_ICON_TWO_LINES);

        /* The Objects of the TagCombobox are dynamically created */
        /* In real world application, this could be any query to Database/Backend etc. */
        mealTagComboBox.setModel(queryString -> {
            if(!queryString.equals("")){
                return Arrays.asList(
                        new Meal(MaterialIcon.CAKE, "Free " + queryString, "100 kcal"),
                        new Meal(MaterialIcon.LANDSCAPE, "Premium " + queryString + " with Chocolate", "1234 kcal")
                );
            } else {
                return new ArrayList<>();
            }
        });

        mealTagComboBox.setPropertyExtractor(mealPropertyExtractor);
        mealTagComboBox.setRecordToStringFunction(Meal::getName);

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
            DemoLesson demo = new PropertyExtractorDemo(sessionContext);

            // call the method defined in the DemoLesson Interface
            demo.handleDemoSelected();

            rootPanel.setContent(demo.getRootComponent());
        };
        new TeamAppsJettyEmbeddedServer(controller).start();
    }
}