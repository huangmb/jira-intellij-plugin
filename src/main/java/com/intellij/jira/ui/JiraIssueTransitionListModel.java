package com.intellij.jira.ui;

import com.intellij.jira.rest.model.JiraIssueTransition;

import javax.swing.*;
import java.util.List;

public class JiraIssueTransitionListModel extends AbstractListModel<JiraIssueTransition> {

    private List<JiraIssueTransition> issueTransitions;

    public JiraIssueTransitionListModel(List<JiraIssueTransition> issueTransitions) {
        this.issueTransitions = issueTransitions;
    }

    @Override
    public int getSize() {
        return issueTransitions.size();
    }

    @Override
    public JiraIssueTransition getElementAt(int index) {
        return issueTransitions.get(index);
    }
}
