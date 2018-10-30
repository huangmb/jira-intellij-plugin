package com.intellij.jira.ui.editors;

import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonPrimitive;
import com.intellij.openapi.ui.ValidationInfo;
import com.intellij.ui.components.JBTextField;
import com.intellij.util.ui.FormBuilder;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

import static com.intellij.openapi.util.text.StringUtil.isEmpty;
import static com.intellij.openapi.util.text.StringUtil.trim;

public class CustomTextFieldEditor extends AbstractFieldEditor {

    protected JBTextField myTextField;

    public CustomTextFieldEditor(String fieldName, String issueKey, boolean required) {
        super(fieldName, issueKey, required);
    }

    @Override
    public JComponent createPanel() {
        this.myTextField = new JBTextField();

        return FormBuilder.createFormBuilder()
                .addLabeledComponent(this.myLabel, this.myTextField)
                .getPanel();
    }


    @Override
    public JsonElement getJsonValue() {
        if(isEmpty(trim(myTextField.getText()))){
            return JsonNull.INSTANCE;
        }

        return new JsonPrimitive(myTextField.getText());
    }

    @Nullable
    @Override
    public ValidationInfo validate() {
        if(isEmpty(trim(myTextField.getText()))){
            return new ValidationInfo(myLabel.getMyLabelText() + " is required");
        }

        return null;
    }
}
