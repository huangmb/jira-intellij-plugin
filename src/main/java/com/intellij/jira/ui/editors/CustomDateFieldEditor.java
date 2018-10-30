package com.intellij.jira.ui.editors;

import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonPrimitive;
import com.intellij.openapi.ui.ValidationInfo;
import com.intellij.ui.components.JBLabel;
import com.intellij.util.ui.FormBuilder;
import com.intellij.util.ui.UI;
import com.intellij.util.ui.UIUtil;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import static com.intellij.openapi.util.text.StringUtil.isEmpty;
import static com.intellij.openapi.util.text.StringUtil.isNotEmpty;
import static com.intellij.openapi.util.text.StringUtil.trim;
import static java.util.Objects.nonNull;

public class CustomDateFieldEditor extends AbstractFieldEditor {

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

    protected JFormattedTextField myFormattedTextField;
    protected String myDescriptionField;

    public CustomDateFieldEditor(String fieldName, String issueKey, boolean required) {
        super(fieldName, issueKey, required);
        this.myDescriptionField = "(e.g. yyyy-MM-dd)";
    }

    @Override
    public JComponent createPanel() {
        this.myFormattedTextField = new JFormattedTextField(getDateFormat());
        this.myFormattedTextField.setPreferredSize(UI.size(250, 24));

        JBLabel myDescriptionLabel = new JBLabel();
        myDescriptionLabel.setComponentStyle(UIUtil.ComponentStyle.SMALL);
        myDescriptionLabel.setText(myDescriptionField);

        return FormBuilder.createFormBuilder()
                .addLabeledComponent(this.myLabel, this.myFormattedTextField)
                .addComponentToRightColumn(myDescriptionLabel)
                .getPanel();
    }


    public SimpleDateFormat getDateFormat(){
        return DATE_FORMAT;
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

    @Nullable
    @Override
    public ValidationInfo validate() {
        if(isRequired() && isEmpty(trim(myFormattedTextField.getText()))){
            return new ValidationInfo(myLabel.getMyLabelText() + " is required.");
        }else{
            if(isNotEmpty(trim(myFormattedTextField.getText()))){
                try{
                    LocalDate.parse(myFormattedTextField.getText(), DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                }catch (DateTimeParseException e){
                    return new ValidationInfo("Wrong format in " + myLabel.getMyLabelText() + " field.");
                }
            }
        }

        return null;
    }
}
