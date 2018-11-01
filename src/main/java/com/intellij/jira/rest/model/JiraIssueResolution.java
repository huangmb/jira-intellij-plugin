package com.intellij.jira.rest.model;

public class JiraIssueResolution {

    private String id;
    private String self;
    private String name;

    public JiraIssueResolution() { }

    public String getId() {
        return id;
    }

    public String getSelf() {
        return self;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name;
    }
}
