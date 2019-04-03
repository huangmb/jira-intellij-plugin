package com.intellij.jira.rest.model;

public class JiraIssueLink {

    private String id;
    private String self;
    private JiraIssueLinkType type;
    private JiraIssue inwardIssue;
    private JiraIssue outwardIssue;

    protected JiraIssueLink() { }

    public String getId() {
        return id;
    }

    public JiraIssueLinkType getType() {
        return type;
    }

    public JiraIssue getInwardIssue() {
        return inwardIssue;
    }

    public JiraIssue getOutwardIssue() {
        return outwardIssue;
    }
}
