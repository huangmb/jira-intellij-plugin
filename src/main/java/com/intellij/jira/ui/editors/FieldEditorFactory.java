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
import static java.util.Objects.isNull;

public class FieldEditorFactory {


    private static final Set<String> TEXT_AREA_FIELDS = ContainerUtil.immutableSet("description", "environment");
    private static final Set<String> TEXT_FIELDS = ContainerUtil.immutableSet("summary");
    private static final Set<String> DATE_FIELDS = ContainerUtil.immutableSet("duedate");
    private static final Set<String> USER_PICKER_FIELDS = ContainerUtil.immutableSet("assignee", "reporter");

    private static final Set<String> CF_TEXT_FIELDS = ContainerUtil.immutableSet( "float", "textfield");

    public static FieldEditor create(JiraIssueFieldProperties properties, JiraIssue issue){

        if(properties.getSchema().isCustomField()){
            return createCustomFieldEditor(properties, issue);
        }

        String fieldType = properties.getSchema().getSystem();
        if(TEXT_FIELDS.contains(fieldType)){
            return new CustomTextFieldEditor(getFieldName(properties), issue.getKey());
        }else if(TEXT_AREA_FIELDS.contains(fieldType)){
            return new CustomTextAreaFieldEditor(getFieldName(properties), issue.getKey());
        }else if(DATE_FIELDS.contains(fieldType)){
            return new CustomDateFieldEditor(getFieldName(properties), issue.getKey());
        }else if(USER_PICKER_FIELDS.contains(fieldType)) {
            return new UserSelectFieldEditor(getFieldName(properties), issue.getKey());
        }else if("timetracking".equals(fieldType)){
            return new TimeTrackingFieldEditor(issue.getKey());
        }else if("issuelinks".equals(fieldType)){
            return new LinkedIssueFieldEditor(getFieldName(properties), issue.getKey());
        }else if("issuetype".equals(fieldType)){
            return new CustomLabelFieldEditor(getFieldName(properties), issue.getIssuetype().getName(), issue.getKey());
        }

        return createCustomComboBoxFieldEditor(properties, issue.getKey());
    }

    public static FieldEditor createCommentFieldEditor(String issueKey){
        return new CommentFieldEditor(issueKey);
    }


    private static FieldEditor createCustomComboBoxFieldEditor(JiraIssueFieldProperties properties, String issueKey){
        List<?> items = new ArrayList<>();
        JsonArray values = properties.getAllowedValues();
        if(isNull(values) || isEmpty(values)){
            if(StringUtil.isEmpty(properties.getAutoCompleteUrl())){
                return new CustomLabelFieldEditor(properties.getName(), issueKey);
            }

        }

        boolean isArray = properties.getSchema().getType().equals("array");
        String type = isArray ? properties.getSchema().getItems() : properties.getSchema().getType();
        if("priority".equals(type)){
            items = Arrays.asList(GSON.fromJson(values, JiraIssuePriority[].class));
        }else if("version".equals(type)){
            items = Arrays.asList(GSON.fromJson(values, JiraProjectVersion[].class));
        }else if("resolution".equals(type)){
            items = Arrays.asList(GSON.fromJson(values, JiraIssueResolution[].class));
        }else if("component".equals(type)){
            items = Arrays.asList(GSON.fromJson(values, JiraProjectComponent[].class));
        }


        return new CustomComboBoxFieldEditor(getFieldName(properties), items, issueKey, isArray);
    }

    private static FieldEditor createCustomFieldEditor(JiraIssueFieldProperties properties, JiraIssue issue) {
        List<?> items = new ArrayList<>();
        boolean isArray = properties.getSchema().getType().equals("array");
        String customFieldType = properties.getSchema().getCustom();
        JsonArray values = properties.getAllowedValues();
        if(isNull(values) || isEmpty(values)){
            if(CF_TEXT_FIELDS.contains(customFieldType)){
                return new CustomTextFieldEditor(getFieldName(properties), issue.getKey());
            }else if("userpicker".equals(customFieldType) || "multiuserpicker".equals(customFieldType)){
                return new UserSelectFieldEditor(getFieldName(properties), issue.getKey(), isArray);
            }else if("grouppicker".equals(customFieldType) || "multigrouppicker".equals(customFieldType)){
                return new GroupSelectFieldEditor(getFieldName(properties), issue.getKey(), isArray);
            }else if("datepicker".equals(customFieldType)){
                return new CustomDateFieldEditor(getFieldName(properties), issue.getKey());
            }else if("datetime".equals(customFieldType)){
                return new CustomDateTimeFieldEditor(getFieldName(properties), issue.getKey());
            }
            else{
                return new CustomLabelFieldEditor(getFieldName(properties), issue.getKey());
            }

        }


        String type =  isArray ? properties.getSchema().getItems() : properties.getSchema().getType();
        if("option".equals(type)){
            items = Arrays.asList(GSON.fromJson(values, JiraCustomFieldOption[].class));
        }else if("project".equals(type)){
            items = Arrays.asList(GSON.fromJson(values, JiraProject[].class));
        }else if("version".equals(type)){
            items = Arrays.asList(GSON.fromJson(values, JiraProjectVersion[].class));
        }


        return new CustomComboBoxFieldEditor(getFieldName(properties), items, issue.getKey(), isArray);
    }

    private static String getFieldName(JiraIssueFieldProperties properties){
        return properties.getName() + (properties.isRequired() ? " *" : "");
    }


}
