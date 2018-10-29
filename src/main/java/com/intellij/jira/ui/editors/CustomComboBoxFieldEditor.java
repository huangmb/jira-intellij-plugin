package com.intellij.jira.ui.editors;

import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.intellij.openapi.ui.ComboBox;
import com.intellij.ui.CollectionComboBoxModel;
import com.intellij.util.ui.FormBuilder;

import javax.swing.*;
import java.util.List;
import java.util.Map;

import static com.intellij.jira.util.JiraGsonUtil.createNameObject;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

public class CustomComboBoxFieldEditor<T> extends AbstractFieldEditor {

    private ComboBox<T> myComboBox;
    private CollectionComboBoxModel<T> myComboBoxItems;
    private boolean isMultiSelect;

    public CustomComboBoxFieldEditor(String fieldName, List<T> items, String issueKey, boolean isMultiSelect) {
        super(fieldName, issueKey);
        this.myComboBoxItems = new CollectionComboBoxModel<>(items);
        this.myComboBox = new ComboBox(myComboBoxItems, 300);
        this.isMultiSelect = isMultiSelect;

    }

    @Override
    public JComponent createPanel() {
        return FormBuilder.createFormBuilder()
                .addLabeledComponent(this.myFieldLabel, this.myComboBox)
                .getPanel();
    }

    @Override
    public Map<String, String> getInputValues() {
        myInputValues.put(myFieldLabel.getText(), getSelectedValue());
        return myInputValues;
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



}
