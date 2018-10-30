package com.intellij.jira.ui.editors;

import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.intellij.ui.components.JBLabel;
import com.intellij.ui.components.JBTextField;
import com.intellij.util.ui.FormBuilder;
import com.intellij.util.ui.UI;

import javax.swing.*;
import java.util.Map;

import static com.intellij.openapi.util.text.StringUtil.isEmpty;
import static com.intellij.openapi.util.text.StringUtil.isNotEmpty;
import static java.util.Objects.nonNull;

public class TimeTrackingFieldEditor extends AbstractFieldEditor {

    private static final String ORIGINAL_ESTIMATE_FIELD = "Original Estimate";
    private static final String REMAINING_ESTIMATE_FIELD = "Remaining Estimate";

    private JTextField myFirstField;
    private JLabel mySecondLabel;
    private JTextField mySecondField;


    public TimeTrackingFieldEditor(String issueKey) {
        super(ORIGINAL_ESTIMATE_FIELD, issueKey);

    }

    @Override
    public JComponent createPanel() {
        this.myFirstField = new JBTextField();
        this.myFirstField.setPreferredSize(UI.size(255, 24));

        this.mySecondLabel = new JBLabel(REMAINING_ESTIMATE_FIELD);
        this.mySecondField = new JBTextField();
        this.mySecondField.setPreferredSize(UI.size(255, 24));


        return FormBuilder.createFormBuilder()
                .addLabeledComponent(this.myFieldLabel, this.myFirstField)
                .addLabeledComponent(this.mySecondLabel, this.mySecondField)
                .getPanel();
    }

    @Override
    public Map<String, String> getInputValues() {
        myInputValues.put(ORIGINAL_ESTIMATE_FIELD, getOriginalEstimate());
        myInputValues.put(REMAINING_ESTIMATE_FIELD, getRemainingEstimate());

        return myInputValues;
    }

    @Override
    public JsonElement getJsonValue() {
        if(isEmpty(getOriginalEstimate()) && isEmpty(getRemainingEstimate())) {
            return JsonNull.INSTANCE;
        }

        JsonObject timeObject = new JsonObject();
        if(isNotEmpty(getOriginalEstimate())){
            timeObject.add("originalEstimate", new JsonPrimitive(getOriginalEstimate()));
        }

        if(isNotEmpty(getRemainingEstimate())){
            timeObject.add("remainingEstimate", new JsonPrimitive(getRemainingEstimate()));
        }

        return timeObject;
    }


    private String getOriginalEstimate(){
        return nonNull(myFirstField) ? myFirstField.getText() : "";
    }

    private String getRemainingEstimate(){
        return nonNull(mySecondField) ? mySecondField.getText() : "";
    }
}
