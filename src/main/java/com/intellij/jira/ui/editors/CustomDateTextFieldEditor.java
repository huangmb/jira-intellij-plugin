package com.intellij.jira.ui.editors;

import javax.swing.*;
import java.text.SimpleDateFormat;

public class CustomDateTextFieldEditor extends AbstractFieldEditor {

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
    private JFormattedTextField myFormattedTextField;

    public CustomDateTextFieldEditor(String fieldName) {
        super(fieldName);
        this.myFormattedTextField = new JFormattedTextField(DATE_FORMAT);
    }

    @Override
    public JComponent getInput() {
        return myFormattedTextField;
    }

    @Override
    public String getInputValue() {
        return myFormattedTextField.getText();
    }
}
