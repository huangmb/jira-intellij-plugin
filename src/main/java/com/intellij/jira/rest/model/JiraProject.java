package com.intellij.jira.rest.model;

import static java.util.Objects.nonNull;

public class JiraProject {

    private String id;
    private String name;
    private String self;
    private String key;
    private String projectTypeKey;
    private JiraUser lead;

    public JiraProject() { }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getSelf() {
        return self;
    }

    public String getKey() {
        return key;
    }

    public String getProjectTypeKey() {
        return projectTypeKey;
    }

    public JiraUser getLead() {
        return lead;
    }

    public String getLeadName(){
        return nonNull(lead) ? lead.getName() : "";
    }

    public String getUrl(){
        return self.replaceFirst("(/rest([\\w/]+))", "/projects/" + getKey() + "/summary");
    }
}
