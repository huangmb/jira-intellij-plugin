package com.intellij.jira.rest.model;

public class JiraIssueStatus {

    private String id;
    private String self;
    private String name;
    private String description;
    private String iconUrl;

    public JiraIssueStatus() { }

    public String getId() {
        return id;
    }

    public String getSelf() {
        return self;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getIconUrl() {
        return iconUrl;
    }
}
