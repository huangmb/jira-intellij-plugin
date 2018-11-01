package com.intellij.jira.ui.editors;

import com.intellij.ui.components.JBLabel;

public abstract class AbstractFieldEditor implements FieldEditor {

    protected String issueKey;
    protected MyLabel myLabel;
    private boolean required;

    public AbstractFieldEditor(String fieldName, String issueKey) {
        this(fieldName, issueKey, false);
    }

    public AbstractFieldEditor(String fieldName, String issueKey, boolean required) {
        this.issueKey = issueKey;
        this.myLabel = new MyLabel(fieldName, required);
        this.required = required;
    }

    @Override
    public boolean isRequired() {
        return required;
    }


    class MyLabel extends JBLabel{
        private String myLabelText;

        public MyLabel(String myLabelText, boolean required) {
            super();
            this.myLabelText = myLabelText;
            setText(myLabelText + (required ? " *" : ""));
        }


        public String getMyLabelText() {
            return myLabelText;
        }

    }

}
