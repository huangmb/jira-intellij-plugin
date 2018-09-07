package com.intellij.jira.ui.panels;

import com.intellij.jira.rest.JiraIssueCommentsWrapper;
import com.intellij.jira.rest.model.JiraIssueComment;
import com.intellij.jira.ui.JiraIssueCommentListModel;
import com.intellij.jira.ui.renders.JiraIssueCommentListCellRenderer;
import com.intellij.openapi.ui.SimpleToolWindowPanel;
import com.intellij.ui.ScrollPaneFactory;
import com.intellij.ui.components.JBList;
import com.intellij.ui.components.JBPanel;

import java.awt.*;

import static java.awt.BorderLayout.CENTER;
import static javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED;

class JiraIssueCommentsPanel extends SimpleToolWindowPanel {

    private JiraIssueCommentsWrapper comments;

    JiraIssueCommentsPanel(JiraIssueCommentsWrapper comments) {
        super(true);
        this.comments = comments;
        initContent();
    }


    private void initContent(){
        JBPanel panel = new JBPanel(new BorderLayout());

        JBList<JiraIssueComment> issueCommentList = new JBList<>();
        issueCommentList.setEmptyText("No comments");
        issueCommentList.setModel(new JiraIssueCommentListModel(comments.getComments()));
        issueCommentList.setCellRenderer(new JiraIssueCommentListCellRenderer());

        panel.add(ScrollPaneFactory.createScrollPane(issueCommentList, VERTICAL_SCROLLBAR_AS_NEEDED), CENTER);

        setContent(panel);
    }


}
