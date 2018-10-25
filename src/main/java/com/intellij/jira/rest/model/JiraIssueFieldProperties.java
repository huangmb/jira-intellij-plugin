package com.intellij.jira.rest.model;

import com.google.gson.JsonArray;

import java.util.Arrays;
import java.util.List;

import static com.intellij.openapi.util.text.StringUtil.isNotEmpty;
import static java.util.Objects.isNull;

public class JiraIssueFieldProperties {

    private boolean required;
    private Schema schema;
    private String name;
    private String autoCompleteUrl;
    private JsonArray allowedValues;

    public JiraIssueFieldProperties() { }

    public boolean isRequired() {
        return required;
    }

    public Schema getSchema() {
        return schema;
    }

    public String getName() {
        return name;
    }

    public String getAutoCompleteUrl() {
        return autoCompleteUrl;
    }

    public JsonArray getAllowedValues() {
        return allowedValues;
    }

    public class Schema{

        private String type;
        private String items;
        private String system;
        private String custom;
        private String customId;

        public Schema() { }

        public String getType() {
            return type;
        }

        public String getItems() {
            return items;
        }

        public String getSystem() {
            return system;
        }

        public String getCustom() {
            if(isNull(custom)){
                return "";
            }

            List<String> words = Arrays.asList(custom.split(":"));
            return  !words.isEmpty() && words.size() > 1 ? words.get(1) : "";
        }

        public String getCustomId() {
            return customId;
        }

        public boolean isCustomField(){
            return isNotEmpty(getCustomId()) && isNotEmpty(getCustom());
        }


        public String getFieldName(){
            if(isCustomField()){
                return "customfield_" + getCustomId();
            }

            return getSystem();
        }

    }


}
