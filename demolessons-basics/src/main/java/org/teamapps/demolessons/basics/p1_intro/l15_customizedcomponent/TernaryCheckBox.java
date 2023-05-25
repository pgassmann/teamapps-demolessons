package org.teamapps.demolessons.basics.p1_intro.l15_customizedcomponent;

import org.teamapps.dto.UiField;
import org.teamapps.icon.material.MaterialIcon;
import org.teamapps.ux.component.field.TemplateField;
import org.teamapps.ux.component.template.BaseTemplate;

import java.util.Map;

import static org.teamapps.demolessons.basics.p1_intro.l15_customizedcomponent.TernaryValue.*;
/*
* Custom Component to provide a three-state checkbox. by extending a TemplateField
* */
public class TernaryCheckBox extends TemplateField<TernaryValue> {

    private String caption;

    public TernaryCheckBox(String caption) {
        super(BaseTemplate.LIST_ITEM_MEDIUM_ICON_SINGLE_LINE, FALSE);
        this.caption = caption;
        setPropertyProvider((ternaryValue, propertyNames) -> extractProperties(ternaryValue));
        onClicked.addListener(() -> {
            TernaryValue currentValue = (TernaryValue) getValue();
            TernaryValue newValue = values()[(currentValue.ordinal() + 1) % values().length];
            setValue(newValue);
            onValueChanged.fire(newValue);
        });
    }

    private Map extractProperties(TernaryValue ternaryValue) {
        switch (ternaryValue) {
            case TRUE:
                return Map.of("icon", MaterialIcon.CHECK_BOX, "caption", this.caption);
            case FALSE:
                return Map.of("icon", MaterialIcon.CHECK_BOX_OUTLINE_BLANK, "caption", this.caption);
            default:
                return Map.of("icon", MaterialIcon.INDETERMINATE_CHECK_BOX, "caption", this.caption);
        }
    }


    public void setCaption(String caption) {
        this.caption = caption;
        // update component on client side when caption changes
        queueCommandIfRendered(() -> new UiField.SetValueCommand(getId(), convertUxValueToUiValue(getValue())));
    }
}
