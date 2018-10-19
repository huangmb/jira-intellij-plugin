package com.intellij.jira.ui.editors;

import com.intellij.jira.util.JiraLabelUtil;
import com.intellij.ui.components.JBLabel;

import javax.swing.*;

public class CustomLabelFieldEditor extends AbstractFieldEditor {

    private JBLabel myLabelText;

    public CustomLabelFieldEditor(String fieldName) {
        this(fieldName, "None");
    }

    public CustomLabelFieldEditor(String fieldName, String labelText) {
        super(fieldName);
        this.myLabelText = JiraLabelUtil.createBoldLabel(labelText);
    }


    @Override
    public JComponent getInput() {
        return myLabelText;
    }

    @Override
    public String getInputValue() {
        return myLabelText.getText();
    }
}
