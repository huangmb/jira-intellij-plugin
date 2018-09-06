package com.intellij.jira.rest;

import com.intellij.jira.rest.model.JiraIssueTransition;
import com.intellij.util.containers.ContainerUtil;

import java.util.List;

public class JiraIssueTransitionsWrapper <T extends JiraIssueTransition> extends JiraResponseWrapper {

    private List<T> transitions = ContainerUtil.emptyList();

    public JiraIssueTransitionsWrapper() { }

    public List<T> getTransitions() {
        return transitions;
    }
}
