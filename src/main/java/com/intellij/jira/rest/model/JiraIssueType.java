package com.intellij.jira.rest.model;

public class JiraIssueType {

    private String id;
    private String self;
    private String name;
    private String description;
    private String iconUrl;
    private boolean subtask;

    public JiraIssueType() { }


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

    public boolean isSubtask() {
        return subtask;
    }

}
