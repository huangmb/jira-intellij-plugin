package com.intellij.jira.ui.editors;

import javax.swing.*;

public class CustomTextAreaFieldEditor extends AbstractFieldEditor {

    private JTextArea myTextArea;

    public CustomTextAreaFieldEditor(String fieldName) {
        super(fieldName);
        this.myTextArea = new JTextArea(6, 60);
        this.myFieldLabel.setLabelFor(this.myTextArea);
    }


    @Override
    public JComponent getInput() {
        return myTextArea;
    }

    @Override
    public String getInputValue() {
        return this.myTextArea.getText();
    }

}
