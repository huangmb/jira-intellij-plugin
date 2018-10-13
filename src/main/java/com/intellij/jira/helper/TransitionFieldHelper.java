package com.intellij.jira.helper;

import com.intellij.jira.rest.model.JiraIssueFieldProperties;
import com.intellij.jira.ui.editors.FieldEditor;
import com.intellij.jira.ui.editors.FieldEditorFactory;

import javax.swing.*;

public final class TransitionFieldHelper {


    public static FieldEditorInfo createFieldEditorInfo(JiraIssueFieldProperties properties){
        return new FieldEditorInfo(properties);
    }

    public static FieldEditorInfo createCommentFieldEditorInfo(FieldEditor fieldEditor){
        return new FieldEditorInfo("comment", false, "string", fieldEditor);
    }


    public static class FieldEditorInfo {

        private FieldEditor editor;
        private String system;
        private boolean required;
        private boolean array;

        private FieldEditorInfo(JiraIssueFieldProperties properties) {
            this(properties.getSchema().getSystem(), properties.isRequired(), properties.getSchema().getType(), FieldEditorFactory.create(properties));
        }

        private FieldEditorInfo(String system, boolean required, String type, FieldEditor fieldEditor) {
            this.system = system;
            this.required = required;
            this.array = "array".equals(type);
            this.editor = fieldEditor;
        }


        public JComponent getLabel() {
            return editor.getLabel();
        }

        public String getLabelValue(){
            return editor.getLabelValue();
        }

        public JComponent getInput() {
            return editor.getInput();
        }

        public String getInputValue(){
            return editor.getInputValue();
        }

        public String getSystem(){
            return system;
        }

        public boolean isRequired(){
            return required;
        }

        public boolean isArray(){
            return array;
        }

    }





}
