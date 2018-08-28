package com.intellij.jira.rest;

import com.intellij.jira.rest.model.JiraIssue;
import com.intellij.tasks.jira.rest.model.JiraResponseWrapper;
import com.intellij.util.containers.ContainerUtil;

import java.util.List;

public class JiraIssuesWrapper<T extends JiraIssue> extends JiraResponseWrapper {

    private List<T> issues = ContainerUtil.emptyList();

    public JiraIssuesWrapper() { }

    public List<T> getIssues() {
        return issues;
    }
}
