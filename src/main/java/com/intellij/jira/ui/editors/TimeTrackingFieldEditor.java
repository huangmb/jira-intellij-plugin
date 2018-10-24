package com.intellij.jira.ui.editors;

import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.intellij.ui.components.JBLabel;
import com.intellij.ui.components.JBTextField;
import com.intellij.util.ui.FormBuilder;

import javax.swing.*;
import java.util.Map;

public class TimeTrackingFieldEditor extends AbstractFieldEditor {

    private static final String ORIGINAL_ESTIMATE_FIELD = "Original Estimate";
    private static final String REMAINING_ESTIMATE_FIELD = "Remaining Estimate";

    private JTextField myFirstField;
    private JLabel mySecondLabel;
    private JTextField mySecondField;


    public TimeTrackingFieldEditor() {
        super(ORIGINAL_ESTIMATE_FIELD);

    }

    @Override
    public JComponent createPanel() {
        this.myFirstField = new JBTextField();

        this.mySecondLabel = new JBLabel(REMAINING_ESTIMATE_FIELD);
        this.mySecondField = new JBTextField();


        return FormBuilder.createFormBuilder()
                .addLabeledComponent(this.myFieldLabel, this.myFirstField)
                .addLabeledComponent(this.mySecondLabel, this.mySecondField)
                .getPanel();
    }

    @Override
    public Map<String, String> getInputValues() {
        myInputValues.put(ORIGINAL_ESTIMATE_FIELD, myFirstField.getText());
        myInputValues.put(REMAINING_ESTIMATE_FIELD, mySecondField.getText());

        return myInputValues;
    }

    @Override
    public JsonElement getJsonValue() {
        return JsonNull.INSTANCE;
    }
}
