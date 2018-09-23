package com.intellij.jira.rest.model;

import java.awt.*;

public class JiraIssueStatus {

    private String id;
    private String self;
    private String name;
    private String description;
    private String iconUrl;
    private JiraIssueStatusCategory statusCategory;

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

    public Color getCategoryColor(){
        return statusCategory.getColor();
    }

    public boolean isInProgressCategory(){
        return statusCategory.isInProgressCategory();
    }

    public boolean isDoneCategory(){
        return statusCategory.isDoneCategory();
    }
}
