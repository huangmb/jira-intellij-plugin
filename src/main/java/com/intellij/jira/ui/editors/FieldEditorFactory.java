package com.intellij.jira.ui.editors;

import com.google.gson.JsonArray;
import com.intellij.jira.rest.model.*;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.util.containers.ContainerUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import static com.intellij.jira.util.JiraGsonUtil.isEmpty;
import static com.intellij.tasks.jira.JiraRepository.GSON;

public class FieldEditorFactory {

    private static final Set<String> COMBOBOX_FIELDS = ContainerUtil.immutableSet( "resolution", "labels", "fixVersions", "priority", "components");
    private static final Set<String> TEXT_AREA_FIELDS = ContainerUtil.immutableSet("description", "environment");
    private static final Set<String> TEXT_FIELDS = ContainerUtil.immutableSet("summary");
    private static final Set<String> DATE_FIELDS = ContainerUtil.immutableSet("duedate");
    private static final Set<String> USER_PICKER_FIELDS = ContainerUtil.immutableSet("assignee", "reporter");

    public static FieldEditor create(JiraIssueFieldProperties properties, String issueKey){

        String fieldType = properties.getSchema().getSystem();
        if(TEXT_FIELDS.contains(fieldType)){
            return new CustomTextFieldEditor(getFieldName(properties), issueKey);
        }else if(TEXT_AREA_FIELDS.contains(fieldType)){
            return new CustomTextAreaFieldEditor(getFieldName(properties), issueKey);
        }else if(DATE_FIELDS.contains(fieldType)){
            return new CustomDateTextFieldEditor(getFieldName(properties), issueKey);
        }else if(USER_PICKER_FIELDS.contains(fieldType)) {
            return new UserTextFieldEditor(getFieldName(properties), issueKey);
        }else if("timetracking".equals(fieldType)){
            return new TimeTrackingFieldEditor(issueKey);
        }else if("issuelinks".equals(fieldType)){
            return new LinkedIssueFieldEditor(getFieldName(properties), issueKey);
        }

        return createCustomComboBoxFieldEditor(properties, issueKey);
    }

    public static FieldEditor createCommentFieldEditor(String issueKey){
        return new CommentFieldEditor(issueKey);
    }


    private static FieldEditor createCustomComboBoxFieldEditor(JiraIssueFieldProperties properties, String issueKey){
        List<?> items = new ArrayList<>();
        JsonArray values = properties.getAllowedValues();
        if(isEmpty(values)){
            if(StringUtil.isEmpty(properties.getAutoCompleteUrl())){
                return new CustomLabelFieldEditor(properties.getName(), issueKey);
            }

            //Get results from url or put a button with dialog action

        }


        String type = properties.getSchema().getType().equals("array") ? properties.getSchema().getItems() : properties.getSchema().getType();
        if("priority".equals(type)){
            items = Arrays.asList(GSON.fromJson(values, JiraIssuePriority[].class));
        }else if("version".equals(type)){
            items = Arrays.asList(GSON.fromJson(values, JiraProjectVersion[].class));
        }else if("resolution".equals(type)){
            items = Arrays.asList(GSON.fromJson(values, JiraIssueResolution[].class));
        }else if("component".equals(type)){
            items = Arrays.asList(GSON.fromJson(values, JiraProjectComponent[].class));
        }





        return new CustomComboBoxFieldEditor(getFieldName(properties), items, issueKey);
    }

    private static String getFieldName(JiraIssueFieldProperties properties){
        return properties.getName() + (properties.isRequired() ? "*" : "");
    }


}
