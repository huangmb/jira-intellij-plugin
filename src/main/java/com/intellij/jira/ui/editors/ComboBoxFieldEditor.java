package com.intellij.jira.ui.editors;

import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.intellij.openapi.ui.ComboBox;
import com.intellij.openapi.ui.ValidationInfo;
import com.intellij.ui.CollectionComboBoxModel;
import com.intellij.util.ui.FormBuilder;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.util.List;

import static com.intellij.jira.util.JiraGsonUtil.createNameObject;
import static com.intellij.openapi.util.text.StringUtil.isEmpty;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

public class ComboBoxFieldEditor<T> extends AbstractFieldEditor {

    private ComboBox<T> myComboBox;
    private CollectionComboBoxModel<T> myComboBoxItems;
    private boolean isMultiSelect;

    public ComboBoxFieldEditor(String fieldName, List<T> items, String issueKey, boolean required, boolean isMultiSelect) {
        super(fieldName, issueKey, required);
        this.myComboBoxItems = new CollectionComboBoxModel<>(items);
        this.myComboBox = new ComboBox(myComboBoxItems, 300);
        this.isMultiSelect = isMultiSelect;

    }

    @Override
    public JComponent createPanel() {
        return FormBuilder.createFormBuilder()
                .addLabeledComponent(this.myLabel, this.myComboBox)
                .getPanel();
    }


    @Override
    public JsonElement getJsonValue() {
        if(isNull(myComboBox.getSelectedItem())){
            return JsonNull.INSTANCE;
        }

        return createNameObject(getSelectedValue(), isMultiSelect);
    }

    private String getSelectedValue(){
        return nonNull(this.myComboBox.getSelectedItem()) ? this.myComboBox.getSelectedItem().toString() : "";
    }


    @Nullable
    @Override
    public ValidationInfo validate() {
        if(isRequired() && isEmpty(getSelectedValue())){
            return new ValidationInfo(myLabel.getMyLabelText() + " is required.");
        }

        return null;
    }
}
