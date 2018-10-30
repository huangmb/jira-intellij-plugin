package com.intellij.jira.ui.editors;

import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.intellij.openapi.ui.ValidationInfo;
import com.intellij.ui.JBColor;
import com.intellij.util.ui.FormBuilder;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

import static com.intellij.jira.util.JiraGsonUtil.createNameObject;
import static com.intellij.openapi.util.text.StringUtil.isEmpty;
import static com.intellij.openapi.util.text.StringUtil.trim;

public class CustomTextAreaFieldEditor extends AbstractFieldEditor {

    protected JTextArea myTextArea;

    public CustomTextAreaFieldEditor(String fieldName, String issueKey, boolean required) {
        super(fieldName, issueKey, required);
    }

    @Override
    public JComponent createPanel() {
        this.myTextArea = new JTextArea(6, 60);
        this.myTextArea.setBorder(BorderFactory.createLineBorder(JBColor.border()));


        return FormBuilder.createFormBuilder()
                .addLabeledComponent(myLabel.getText(), this.myTextArea)
                .getPanel();
    }


    @Override
    public JsonElement getJsonValue() {
        if(isEmpty(trim(myTextArea.getText()))){
            return JsonNull.INSTANCE;
        }

        return createNameObject(myTextArea.getText());
    }

    @Nullable
    @Override
    public ValidationInfo validate() {
        if(isRequired() && isEmpty(trim(myTextArea.getText()))){
            return new ValidationInfo(myLabel.getMyLabelText() + " is required.");
        }

        return null;
    }
}
