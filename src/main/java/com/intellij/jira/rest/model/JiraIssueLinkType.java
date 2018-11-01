package com.intellij.jira.rest.model;

public class JiraIssueLinkType {

    private String id;
    private String name;
    private String self;
    private String inward;
    private String outward;

    protected JiraIssueLinkType() { }

    public String getInward() {
        return inward;
    }

    public String getOutward() {
        return outward;
    }

    public String getName() {
        return name;
    }
}
