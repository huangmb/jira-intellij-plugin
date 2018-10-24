package com.intellij.jira.ui.editors;

import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.intellij.ui.JBColor;
import com.intellij.util.ui.FormBuilder;

import javax.swing.*;
import java.util.Map;

import static com.intellij.jira.util.JiraGsonUtil.createNameObject;
import static com.intellij.openapi.util.text.StringUtil.isEmpty;
import static com.intellij.openapi.util.text.StringUtil.trim;

public class CustomTextAreaFieldEditor extends AbstractFieldEditor {

    protected JTextArea myTextArea;

    public CustomTextAreaFieldEditor(String fieldName) {
        super(fieldName);
    }

    @Override
    public JComponent createPanel() {
        this.myTextArea = new JTextArea(6, 60);
        this.myTextArea.setBorder(BorderFactory.createLineBorder(JBColor.border()));


        return FormBuilder.createFormBuilder()
                .addLabeledComponent(myFieldLabel.getText(), this.myTextArea)
                .getPanel();
    }

    @Override
    public Map<String, String> getInputValues() {
        return null;
    }

    @Override
    public JsonElement getJsonValue() {
        if(isEmpty(trim(myTextArea.getText()))){
            return JsonNull.INSTANCE;
        }

        return createNameObject(myTextArea.getText());
    }
}
