package com.intellij.jira.rest.model;

public class JiraIssuePriority {

    private String id;
    private String self;
    private String name;
    private String iconUrl;

    public JiraIssuePriority() { }

    public String getId() {
        return id;
    }

    public String getSelf() {
        return self;
    }

    public String getName() {
        return name;
    }

    public String getIconUrl() {
        return iconUrl;
    }
}
