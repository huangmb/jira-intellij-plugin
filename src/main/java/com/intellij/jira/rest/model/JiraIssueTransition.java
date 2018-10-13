package com.intellij.jira.rest.model;

import com.google.gson.JsonObject;

public class JiraIssueTransition {

    private String id;
    private String name;
    private JsonObject fields;

    public JiraIssueTransition() { }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public JsonObject getFields() {
        return fields;
    }
}
