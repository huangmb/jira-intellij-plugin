package com.intellij.jira.ui.editors;

import com.google.gson.JsonElement;
import com.intellij.util.ui.FormBuilder;

import javax.swing.*;
import java.text.SimpleDateFormat;
import java.util.Map;

public class CustomDateTextFieldEditor extends AbstractFieldEditor {

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
    private JFormattedTextField myFormattedTextField;

    public CustomDateTextFieldEditor(String fieldName) {
        super(fieldName);
    }

    @Override
    public JComponent createPanel() {
        this.myFormattedTextField = new JFormattedTextField(DATE_FORMAT);

        return FormBuilder.createFormBuilder()
                .addLabeledComponent(this.myFieldLabel, this.myFormattedTextField)
                .getPanel();
    }

    @Override
    public Map<String, String> getInputValues() {
        return null;
    }

    @Override
    public JsonElement getJsonValue() {
        return null;
    }
}
