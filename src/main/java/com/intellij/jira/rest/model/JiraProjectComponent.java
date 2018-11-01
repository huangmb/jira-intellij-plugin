package com.intellij.jira.rest.model;

public class JiraProjectComponent {

    private String id;
    private String self;
    private String name;

    public JiraProjectComponent() { }

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
