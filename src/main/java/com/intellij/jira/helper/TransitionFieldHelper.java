package com.intellij.jira.helper;

import com.google.gson.JsonElement;
import com.intellij.jira.rest.model.JiraIssueFieldProperties;
import com.intellij.jira.ui.editors.FieldEditor;
import com.intellij.jira.ui.editors.FieldEditorFactory;

import javax.swing.*;
import java.util.Map;

public final class TransitionFieldHelper {


    public static FieldEditorInfo createFieldEditorInfo(JiraIssueFieldProperties properties, String issueKey){
        return new FieldEditorInfo(properties, issueKey);
    }

    public static FieldEditorInfo createCommentFieldEditorInfo(FieldEditor fieldEditor){
        return new FieldEditorInfo("comment", false, fieldEditor);
    }


    public static class FieldEditorInfo {

        private FieldEditor editor;
        private String name;
        private boolean required;

        private FieldEditorInfo(JiraIssueFieldProperties properties, String issueKey) {
            this(properties.getSchema().getSystem(), properties.isRequired(), FieldEditorFactory.create(properties, issueKey));
        }

        private FieldEditorInfo(String fieldName, boolean required, FieldEditor fieldEditor) {
            this.name = fieldName;
            this.required = required;
            this.editor = fieldEditor;
        }

        public JComponent getPanel(){
            return editor.createPanel();
        }



        public Map<String, String> getInputValues(){
            return editor.getInputValues();
        }

        public JsonElement getJsonValue(){
            return editor.getJsonValue();
        }

        public String getName(){
            return name;
        }

        public boolean isRequired(){
            return required;
        }


    }





}
