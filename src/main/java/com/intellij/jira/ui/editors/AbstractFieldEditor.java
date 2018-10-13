package com.intellij.jira.ui.editors;

import com.intellij.ui.components.JBLabel;

import javax.swing.*;

import static javax.swing.SwingConstants.RIGHT;

public abstract class AbstractFieldEditor implements FieldEditor {

    protected JBLabel myFieldLabel;


    public AbstractFieldEditor(String fieldName) {
        this.myFieldLabel = new JBLabel(fieldName, RIGHT);
    }


    @Override
    public JComponent getLabel() {
        return myFieldLabel;
    }

    @Override
    public String getLabelValue() {
        return this.myFieldLabel.getText();
    }


}
