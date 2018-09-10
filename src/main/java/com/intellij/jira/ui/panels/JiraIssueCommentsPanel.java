package com.intellij.jira.ui.panels;

import com.intellij.jira.actions.AddCommentDialogAction;
import com.intellij.jira.actions.JiraIssueActionGroup;
import com.intellij.jira.rest.JiraIssueCommentsWrapper;
import com.intellij.jira.rest.model.JiraIssue;
import com.intellij.jira.rest.model.JiraIssueComment;
import com.intellij.jira.ui.JiraIssueCommentListModel;
import com.intellij.jira.ui.renders.JiraIssueCommentListCellRenderer;
import com.intellij.openapi.actionSystem.ActionGroup;
import com.intellij.openapi.actionSystem.ActionManager;
import com.intellij.openapi.actionSystem.ActionToolbar;
import com.intellij.openapi.ui.SimpleToolWindowPanel;
import com.intellij.ui.ScrollPaneFactory;
import com.intellij.ui.components.JBList;
import com.intellij.ui.components.JBPanel;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;

import static com.intellij.jira.ui.JiraToolWindowFactory.TOOL_WINDOW_ID;
import static java.awt.BorderLayout.CENTER;
import static javax.swing.ListSelectionModel.SINGLE_SELECTION;
import static javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED;

class JiraIssueCommentsPanel extends SimpleToolWindowPanel {

    private JiraIssueCommentsWrapper comments;
    private String issueKey;

    JiraIssueCommentsPanel(@NotNull JiraIssue issue) {
        super(true);
        this.comments = issue.getComments();
        this.issueKey = issue.getKey();
        initToolbar();
        initContent();
    }

    private void initToolbar() {
        ActionToolbar actionToolbar = ActionManager.getInstance().createActionToolbar(TOOL_WINDOW_ID, createActionGroup(), true);
        actionToolbar.setTargetComponent(this);

        Box toolBarBox = Box.createHorizontalBox();
        toolBarBox.add(actionToolbar.getComponent());
        setToolbar(toolBarBox);
    }

    private ActionGroup createActionGroup() {
        JiraIssueActionGroup group = new JiraIssueActionGroup(this);
        group.add(new AddCommentDialogAction(issueKey));

        return group;
    }


    private void initContent(){
        JBPanel panel = new JBPanel(new BorderLayout());

        JBList<JiraIssueComment> issueCommentList = new JBList<>();
        issueCommentList.setEmptyText("No comments");
        issueCommentList.setModel(new JiraIssueCommentListModel(comments.getComments()));
        issueCommentList.setCellRenderer(new JiraIssueCommentListCellRenderer());
        issueCommentList.setSelectionMode(SINGLE_SELECTION);

        panel.add(ScrollPaneFactory.createScrollPane(issueCommentList, VERTICAL_SCROLLBAR_AS_NEEDED), CENTER);

        setContent(panel);
    }


}
