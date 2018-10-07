package com.intellij.jira.rest.model;

import com.intellij.jira.rest.JiraIssueCommentsWrapper;
import com.intellij.util.containers.ContainerUtil;

import java.util.Date;
import java.util.List;
import java.util.Objects;

public class JiraIssue {

    public static final String REQUIRED_FIELDS = "summary,description,created,updated,duedate,resolutiondate,assignee,reporter,issuetype,status,priority,comment,issuelinks,project,versions";

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

    public JiraUser getAssignee() {
        return fields.assignee;
    }

    public JiraUser getCreator() {
        return fields.creator;
    }

    public JiraUser getReporter() {
        return fields.reporter;
    }

    public JiraIssueCommentsWrapper getComments(){
        return fields.comment;
    }

    public List<JiraIssueLink> getIssueLinks(){
        return fields.issuelinks;
    }

    public JiraProject getProject(){
        return fields.project;
    }

    public List<JiraProjectVersion> getVersions(){
        return fields.versions;
    }

    public String getUrl(){
        return self.replaceFirst("(/rest([\\w/]+))", "/browse/" + getKey());
    }

    public boolean isResolved(){
        return getStatus().isDoneCategory();
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
        private JiraUser assignee;
        private JiraUser creator;
        private JiraUser reporter;
        private JiraIssueCommentsWrapper comment;
        private List<JiraIssueLink> issuelinks = ContainerUtil.emptyList();
        private JiraProject project;
        private List<JiraProjectVersion> versions = ContainerUtil.emptyList();

        public Fields() { }


    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        JiraIssue jiraIssue = (JiraIssue) o;
        return Objects.equals(id, jiraIssue.id) &&
                Objects.equals(key, jiraIssue.key);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, key);
    }
}
