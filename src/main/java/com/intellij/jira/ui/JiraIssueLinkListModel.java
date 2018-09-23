package com.intellij.jira.ui;

import com.intellij.jira.rest.model.JiraIssueLink;

import javax.swing.*;
import java.util.List;

public class JiraIssueLinkListModel extends AbstractListModel<JiraIssueLink> {

    private List<JiraIssueLink> issueLinks;

    public JiraIssueLinkListModel(List<JiraIssueLink> issueLinks) {
        this.issueLinks = issueLinks;
    }

    @Override
    public int getSize() {
        return issueLinks.size();
    }



    @Override
    public JiraIssueLink getElementAt(int index) {
        return issueLinks.get(index);
    }
}
