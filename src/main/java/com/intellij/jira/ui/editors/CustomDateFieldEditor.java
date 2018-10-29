package com.intellij.jira.ui.editors;

import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonPrimitive;
import com.intellij.ui.components.JBLabel;
import com.intellij.util.ui.FormBuilder;
import com.intellij.util.ui.UI;
import com.intellij.util.ui.UIUtil;

import javax.swing.*;
import java.text.SimpleDateFormat;
import java.util.Map;

import static com.intellij.openapi.util.text.StringUtil.isEmpty;
import static com.intellij.openapi.util.text.StringUtil.trim;
import static java.util.Objects.nonNull;

public class CustomDateFieldEditor extends AbstractFieldEditor {

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

    protected JFormattedTextField myFormattedTextField;
    protected String myDescriptionField;

    public CustomDateFieldEditor(String fieldName, String issueKey) {
        super(fieldName, issueKey);
        this.myDescriptionField = "Usage: YYYY-MM-DD";
    }

    @Override
    public JComponent createPanel() {
        this.myFormattedTextField = new JFormattedTextField(getDateFormat());
        this.myFormattedTextField.setPreferredSize(UI.size(250, 24));

        JBLabel myDescriptionLabel = new JBLabel();
        myDescriptionLabel.setComponentStyle(UIUtil.ComponentStyle.SMALL);
        myDescriptionLabel.setText(myDescriptionField);

        return FormBuilder.createFormBuilder()
                .addLabeledComponent(this.myFieldLabel, this.myFormattedTextField)
                .addComponentToRightColumn(myDescriptionLabel)
                .getPanel();
    }


    public SimpleDateFormat getDateFormat(){
        return DATE_FORMAT;
    }


    @Override
    public Map<String, String> getInputValues() {
        myInputValues.put(myFieldLabel.getText(), getValue());
        return myInputValues;
    }


    protected String getValue(){
        return nonNull(myFormattedTextField) ? trim(myFormattedTextField.getText()) : "";
    }

    @Override
    public JsonElement getJsonValue() {
        if(isEmpty(myFormattedTextField.getText())){
            return JsonNull.INSTANCE;
        }

        return new JsonPrimitive(getValue());
    }
}
