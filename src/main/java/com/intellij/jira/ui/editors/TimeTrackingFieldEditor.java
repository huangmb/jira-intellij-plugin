package com.intellij.jira.ui.editors;

import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.intellij.openapi.ui.ValidationInfo;
import com.intellij.ui.components.JBTextField;
import com.intellij.util.ui.FormBuilder;
import com.intellij.util.ui.UI;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.util.regex.Pattern;

import static com.intellij.jira.util.JiraGsonUtil.createPrimitive;
import static com.intellij.openapi.util.text.StringUtil.isEmpty;
import static com.intellij.openapi.util.text.StringUtil.isNotEmpty;
import static java.util.Objects.nonNull;

public class TimeTrackingFieldEditor extends AbstractFieldEditor {

    private static final Pattern TIME_TRACKING_PATTERN = Pattern.compile("((\\d+)[wdhm]\\s)+");
    private static final String ORIGINAL_ESTIMATE_FIELD = "Original Estimate";
    private static final String REMAINING_ESTIMATE_FIELD = "Remaining Estimate";

    private JTextField myFirstField;
    private MyLabel mySecondLabel;
    private JTextField mySecondField;


    public TimeTrackingFieldEditor(String issueKey, boolean required) {
        super(ORIGINAL_ESTIMATE_FIELD, issueKey, required);
        this.mySecondLabel = new MyLabel(REMAINING_ESTIMATE_FIELD, required);
    }

    @Override
    public JComponent createPanel() {
        this.myFirstField = new JBTextField();
        this.myFirstField.setPreferredSize(UI.size(255, 24));


        this.mySecondField = new JBTextField();
        this.mySecondField.setPreferredSize(UI.size(255, 24));


        return FormBuilder.createFormBuilder()
                .addLabeledComponent(this.myLabel, this.myFirstField)
                .addLabeledComponent(this.mySecondLabel, this.mySecondField)
                .getPanel();
    }


    @Override
    public JsonElement getJsonValue() {
        if(isEmpty(getOriginalEstimate()) && isEmpty(getRemainingEstimate())) {
            return JsonNull.INSTANCE;
        }

        JsonObject timeObject = new JsonObject();
        if(isNotEmpty(getOriginalEstimate())){
            timeObject.add("originalEstimate", createPrimitive(getOriginalEstimate()));
        }

        if(isNotEmpty(getRemainingEstimate())){
            timeObject.add("remainingEstimate", createPrimitive(getRemainingEstimate()));
        }

        return timeObject;
    }

    @Nullable
    @Override
    public ValidationInfo validate() {
        if(isRequired() && isEmpty(getOriginalEstimate()) ){
            return new ValidationInfo(myLabel.getMyLabelText() + " is required.");
        }

        if(isRequired() && isEmpty(getRemainingEstimate())){
            return new ValidationInfo(mySecondLabel.getMyLabelText() + " is required.");
        }

        if(isNotEmpty(getOriginalEstimate()) && !TIME_TRACKING_PATTERN.matcher(getOriginalEstimate()).matches()){
            return new ValidationInfo("Wrong format in " + myLabel.getMyLabelText() + " field.");
        }

        if(isNotEmpty(getRemainingEstimate()) && !TIME_TRACKING_PATTERN.matcher(getRemainingEstimate()).matches()){
            return new ValidationInfo("Wrong format in " + mySecondLabel.getMyLabelText() + " field.");
        }


        return null;
    }

    private String getOriginalEstimate(){
        return nonNull(myFirstField) ? myFirstField.getText() : "";
    }

    private String getRemainingEstimate(){
        return nonNull(mySecondField) ? mySecondField.getText() : "";
    }
}
