package com.intellij.jira.ui.editors;

import com.intellij.ui.components.JBTextField;

import javax.swing.*;

public class CustomTextFieldEditor extends AbstractFieldEditor {

    private JBTextField myTextField;

    public CustomTextFieldEditor(String fieldName) {
        super(fieldName);
        this.myTextField = new JBTextField();
        this.myFieldLabel.setLabelFor(this.myTextField);
    }


    @Override
    public JComponent getInput() {
        return myTextField;
    }

    @Override
    public String getInputValue() {
        return this.myTextField.getText();
    }
}
