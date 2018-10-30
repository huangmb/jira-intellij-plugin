package com.intellij.jira.helper;

import com.google.gson.JsonElement;
import com.intellij.jira.rest.model.JiraIssue;
import com.intellij.jira.rest.model.JiraIssueFieldProperties;
import com.intellij.jira.ui.editors.FieldEditor;
import com.intellij.jira.ui.editors.FieldEditorFactory;

import javax.swing.*;
import java.util.Map;

public final class TransitionFieldHelper {


    public static FieldEditorInfo createFieldEditorInfo(JiraIssueFieldProperties properties, JiraIssue issueKey){
        return new FieldEditorInfo(properties, issueKey);
    }

    public static FieldEditorInfo createCommentFieldEditorInfo(FieldEditor fieldEditor){
        return new FieldEditorInfo("comment", false, fieldEditor);
    }


    public static class FieldEditorInfo {

        private FieldEditor editor;
        private String name;
        private boolean required;

        private FieldEditorInfo(JiraIssueFieldProperties properties, JiraIssue issue) {
            this(properties.getSchema().getFieldName(), properties.isRequired(), FieldEditorFactory.create(properties, issue));
        }

        private FieldEditorInfo(String jsonFieldName, boolean required, FieldEditor fieldEditor) {
            this.name = jsonFieldName;
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
