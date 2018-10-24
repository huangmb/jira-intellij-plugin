package com.intellij.jira.ui.editors;

import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.intellij.openapi.ui.ComboBox;
import com.intellij.ui.CollectionComboBoxModel;
import com.intellij.util.ui.FormBuilder;

import javax.swing.*;
import java.util.List;
import java.util.Map;

import static com.intellij.jira.util.JiraGsonUtil.createArrayNameObject;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

public class CustomComboBoxFieldEditor<T> extends AbstractFieldEditor {

    private ComboBox<T> myComboBox;
    private CollectionComboBoxModel<T> myComboBoxItems;

    public CustomComboBoxFieldEditor(String fieldName, List<T> items) {
        super(fieldName);
        this.myComboBoxItems = new CollectionComboBoxModel<>(items);
        this.myComboBox = new ComboBox(myComboBoxItems, 300);
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

        return createArrayNameObject(getSelectedValue());
    }

    private String getSelectedValue(){
        return nonNull(this.myComboBox.getSelectedItem()) ? this.myComboBox.getSelectedItem().toString() : "";
    }



}
