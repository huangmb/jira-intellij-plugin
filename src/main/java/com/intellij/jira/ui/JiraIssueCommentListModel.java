package com.intellij.jira.ui;

import com.intellij.jira.rest.model.JiraIssueComment;

import javax.swing.*;
import java.util.List;

public class JiraIssueCommentListModel extends AbstractListModel<JiraIssueComment> {


    private final List<JiraIssueComment> comments;

    public JiraIssueCommentListModel(List<JiraIssueComment> comments) {
        this.comments = comments;
    }

    @Override
    public int getSize() {
        return comments.size();
    }

    @Override
    public JiraIssueComment getElementAt(int index) {
        return comments.get(index);
    }
}
