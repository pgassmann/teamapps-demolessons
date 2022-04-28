package org.teamapps.demolessons.basics.p4_issuetracker;

import org.teamapps.data.extract.BeanPropertyExtractor;
import org.teamapps.demolessons.issuetracker.model.issuetracker.Group;
import org.teamapps.demolessons.issuetracker.model.issuetracker.User;
import org.teamapps.icon.material.MaterialIcon;
import org.teamapps.ux.application.layout.StandardLayout;
import org.teamapps.ux.application.perspective.Perspective;
import org.teamapps.ux.application.view.View;
import org.teamapps.ux.component.field.AbstractField;
import org.teamapps.ux.component.field.ImageField;
import org.teamapps.ux.component.field.TextField;
import org.teamapps.ux.component.field.combobox.ComboBox;
import org.teamapps.ux.component.field.upload.FileField;
import org.teamapps.ux.component.form.ResponsiveForm;
import org.teamapps.ux.component.form.ResponsiveFormLayout;
import org.teamapps.ux.component.infiniteitemview.InfiniteItemView2;
import org.teamapps.ux.component.template.BaseTemplate;

import java.util.Arrays;
import java.util.List;

public class UserPerspective {

    private TextField nameField;
    private TextField emailField;
    private FileField<ImageField> avatarField;
    private ComboBox<Group> groupsField;
    private List<AbstractField<?>> formFields;

    public Perspective getPerspective(){
        Perspective usersPerspective = Perspective.createPerspective();
        InfiniteItemView2<User> userItemView = createUserItemView();

        View userListView = View.createView(StandardLayout.CENTER, MaterialIcon.PERSON, "Users", userItemView);
        usersPerspective.addView(userListView);
        View userEditView = View.createView(StandardLayout.RIGHT, MaterialIcon.PERSON, "Edit User", createUserEditForm());
        usersPerspective.addView(userEditView);
        return usersPerspective;
    }

    private ResponsiveForm<User> createUserEditForm() {

        ResponsiveForm<User> form = new ResponsiveForm<>(100, 150, 0);
        ResponsiveFormLayout formLayout = form.addResponsiveFormLayout(450);

        nameField = new TextField();
        emailField = new TextField();
        avatarField = new FileField<ImageField>(file -> {return null;});
        groupsField = new ComboBox<Group>();

        nameField.setRequired(true);
        emailField.setRequired(true);

        formFields = Arrays.asList(
                nameField,
                emailField,
                avatarField,
                groupsField);

        formLayout.addSection(MaterialIcon.INFO, "User");
        formLayout.addLabelAndField(null, "Name", nameField);
        formLayout.addLabelAndField(null, "Email", emailField);
        formLayout.addLabelAndField(null, "Avatar", avatarField);
        formLayout.addLabelAndField(null, "Groups", groupsField);
        return form;
    }

    private InfiniteItemView2<User> createUserItemView() {
        InfiniteItemView2<User> userItemView = new InfiniteItemView2<>();
        userItemView.setModel(new UserInfiniteItemViewModel());
        userItemView.setItemTemplate(BaseTemplate.ITEM_VIEW_ITEM);
        userItemView.setItemHeight(80);

        // Map Properties
        BeanPropertyExtractor<User> userPropertyExtractor = new BeanPropertyExtractor<>();
        userPropertyExtractor.addProperty( BaseTemplate.PROPERTY_CAPTION, user -> user.getName());
        userPropertyExtractor.addProperty( BaseTemplate.PROPERTY_DESCRIPTION, User::getEmail);
        userPropertyExtractor.addProperty( BaseTemplate.PROPERTY_IMAGE, User::getAvatar);
        userPropertyExtractor.addProperty( BaseTemplate.PROPERTY_ICON, user -> MaterialIcon.PERSON);
        userItemView.setItemPropertyExtractor(userPropertyExtractor);

        // Alternative
//        userItemView.setItemPropertyExtractor((user, propertyName) -> {
//            if (propertyName.equals(BaseTemplate.PROPERTY_CAPTION)) {
//                return user.getName();
//            } else if (propertyName.equals(BaseTemplate.PROPERTY_DESCRIPTION)) {
//                return user.getEmail();
//            } else if (propertyName.equals(BaseTemplate.PROPERTY_ICON)) {
//                return MaterialIcon.PERSON;
//            } else {
//                return null;
//            }
//        });

        return userItemView;
    }
}
