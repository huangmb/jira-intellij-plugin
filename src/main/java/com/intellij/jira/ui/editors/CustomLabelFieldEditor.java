package com.intellij.jira.ui.editors;

import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.intellij.jira.util.JiraLabelUtil;
import com.intellij.ui.components.JBLabel;
import com.intellij.util.ui.FormBuilder;

import javax.swing.*;
import java.util.Map;

public class CustomLabelFieldEditor extends AbstractFieldEditor {

    private JBLabel myLabelText;
    private String labelText;

    public CustomLabelFieldEditor(String fieldName, String issueKey) {
        this(fieldName, "None", issueKey);
    }

    public CustomLabelFieldEditor(String fieldName, String labelText, String issueKey) {
        super(fieldName, issueKey);
        this.labelText = labelText;
    }

    @Override
    public JComponent createPanel() {
        this.myLabelText = JiraLabelUtil.createBoldLabel(labelText);

        return FormBuilder.createFormBuilder()
                .addLabeledComponent(this.myFieldLabel, this.myLabelText)
                .getPanel();
    }

    @Override
    public Map<String, String> getInputValues() {
        return null;
    }

    @Override
    public JsonElement getJsonValue() {
        return JsonNull.INSTANCE;
    }
}
