package com.intellij.jira.ui.editors;

import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.intellij.jira.util.JiraLabelUtil;
import com.intellij.openapi.ui.ValidationInfo;
import com.intellij.ui.components.JBLabel;
import com.intellij.util.ui.FormBuilder;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public class LabelFieldEditor extends AbstractFieldEditor {

    private JBLabel myLabelText;
    private String labelText;

    public LabelFieldEditor(String fieldName, String issueKey) {
        this(fieldName, "None", issueKey);
    }

    public LabelFieldEditor(String fieldName, String labelText, String issueKey) {
        super(fieldName, issueKey);
        this.labelText = labelText;
    }

    @Override
    public JComponent createPanel() {
        this.myLabelText = JiraLabelUtil.createBoldLabel(labelText);

        return FormBuilder.createFormBuilder()
                .addLabeledComponent(this.myLabel, this.myLabelText)
                .getPanel();
    }


    @Override
    public JsonElement getJsonValue() {
        return JsonNull.INSTANCE;
    }

    @Nullable
    @Override
    public ValidationInfo validate() {
        return null;
    }
}
