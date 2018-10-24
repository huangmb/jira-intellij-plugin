package com.intellij.jira.rest.model;

public class JiraIssueLinkTypeInfo {

    private String name;
    private String description;
    private boolean inward;

    private JiraIssueLinkTypeInfo(String id, String description, boolean inward) {
        this.name = id;
        this.description = description;
        this.inward = inward;
    }

    public static JiraIssueLinkTypeInfo inward(JiraIssueLinkType type){
        return new JiraIssueLinkTypeInfo(type.getName(), type.getInward(), true);
    }

    public static JiraIssueLinkTypeInfo outward(JiraIssueLinkType type){
        return new JiraIssueLinkTypeInfo(type.getName(), type.getOutward(), false);
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public boolean isInward() {
        return inward;
    }

    @Override
    public String toString() {
        return description;
    }
}
