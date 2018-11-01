package com.intellij.jira.rest.model;

public class JiraProjectVersion {

    private String id;
    private String name;
    private String self;
    private boolean archived;
    private boolean released;

    public JiraProjectVersion() { }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getSelf() {
        return self;
    }

    public boolean isArchived() {
        return archived;
    }

    public boolean isReleased() {
        return released;
    }

    @Override
    public String toString() {
        return name;
    }
}
