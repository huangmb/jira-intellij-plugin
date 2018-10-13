package com.intellij.jira.ui.editors;

import com.intellij.openapi.ui.ComboBox;
import com.intellij.ui.CollectionComboBoxModel;

import javax.swing.*;
import java.util.List;

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
    public JComponent getInput() {
        return myComboBox;
    }

    @Override
    public String getInputValue() {
        return nonNull(this.myComboBox.getSelectedItem()) ? this.myComboBox.getSelectedItem().toString() : "";
    }
}
