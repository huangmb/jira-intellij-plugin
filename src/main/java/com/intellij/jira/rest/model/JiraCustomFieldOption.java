package com.intellij.jira.rest.model;

public class JiraCustomFieldOption {

    private String id;
    private String self;
    private String value;

    public JiraCustomFieldOption() { }

    public String getId() {
        return id;
    }

    public String getSelf() {
        return self;
    }

    public String getValue() {
        return value;
    }


    @Override
    public String toString() {
        return value;
    }
}
