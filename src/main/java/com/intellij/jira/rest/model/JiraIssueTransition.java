package com.intellij.jira.rest.model;

import com.google.gson.JsonObject;

public class JiraIssueTransition {

    private String id;
    private String name;
    private JiraIssueStatus to;
    private JsonObject fields;

    public JiraIssueTransition() { }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public JiraIssueStatus getTo() {
        return to;
    }

    public JsonObject getFields() {
        return fields;
    }
}
