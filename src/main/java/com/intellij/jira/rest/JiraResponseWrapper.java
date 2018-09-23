package com.intellij.jira.rest;

public abstract class JiraResponseWrapper {

    private int startAt;
    private int maxResults;
    private int total;

    public JiraResponseWrapper() { }

    public int getStartAt() {
        return startAt;
    }

    public int getMaxResults() {
        return maxResults;
    }

    public int getTotal() {
        return total;
    }
}
