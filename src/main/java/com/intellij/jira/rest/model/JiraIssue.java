package com.intellij.jira.rest.model;

import java.util.Date;

public class JiraIssue {

    public static final String REQUIRED_FIELDS = "summary,description,created,updated,duedate,resolutiondate,assignee,reporter,issuetype,status,priority";

    private String id;
    private String self;
    private String key;
    private JiraIssue.Fields fields;

    public JiraIssue() { }

    public String getId() {
        return id;
    }

    public String getSelf() {
        return self;
    }

    public String getKey() {
        return key;
    }

    public String getSummary() {
        return fields.summary;
    }

    public String getDescription() {
        return fields.description;
    }

    public Date getCreated() {
        return fields.created;
    }

    public Date getUpdated() {
        return fields.updated;
    }

    public Date getResolutiondate() {
        return fields.resolutiondate;
    }

    public Date getDuedate() {
        return fields.duedate;
    }

    public JiraIssueType getIssuetype() {
        return fields.issuetype;
    }

    public JiraIssueStatus getStatus() {
        return fields.status;
    }

    public JiraIssuePriority getPriority() {
        return fields.priority;
    }

    public JiraIssueUser getAssignee() {
        return fields.assignee;
    }

    public JiraIssueUser getCreator() {
        return fields.creator;
    }

    public JiraIssueUser getReporter() {
        return fields.reporter;
    }

    public static class Fields{

        private String summary;
        private String description;
        private Date created;
        private Date updated;
        private Date resolutiondate;
        private Date duedate;
        private JiraIssueType issuetype;
        private JiraIssueStatus status;
        private JiraIssuePriority priority;
        private JiraIssueUser assignee;
        private JiraIssueUser creator;
        private JiraIssueUser reporter;

        public Fields() { }


    }


}
