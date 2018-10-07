package com.intellij.jira.rest.model;

import java.util.Date;

public class JiraIssueComment {

    private String self;
    private String id;
    private JiraUser author;
    private String body;
    private Date created;
    private Date updated;

    public JiraIssueComment() { }


    public JiraUser getAuthor() {
        return author;
    }

    public String getBody() {
        return body;
    }

    public Date getCreated() {
        return created;
    }

    public String getId() {
        return id;
    }
}
