package com.intellij.jira.ui.panels;

import com.intellij.jira.components.JiraActionManager;
import com.intellij.jira.rest.model.JiraIssue;
import com.intellij.jira.util.JiraIconUtil;
import com.intellij.openapi.actionSystem.ActionGroup;
import com.intellij.openapi.actionSystem.ActionManager;
import com.intellij.openapi.actionSystem.ActionToolbar;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.ui.SimpleToolWindowPanel;
import com.intellij.tools.SimpleActionGroup;
import com.intellij.ui.JBColor;
import com.intellij.ui.ScrollPaneFactory;
import com.intellij.ui.components.JBLabel;
import com.intellij.ui.components.JBPanel;
import org.fest.util.Lists;

import javax.swing.*;
import java.awt.*;
import java.util.List;

import static com.intellij.jira.ui.JiraToolWindowFactory.TOOL_WINDOW_ID;
import static javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED;
import static javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED;

public class JiraIssueDetailsPanel extends SimpleToolWindowPanel {

    private ActionToolbar toolbar;
    private JBPanel mainPanel;
    private JiraIssue currentIssue;

    public JiraIssueDetailsPanel(JiraIssue issue) {
        super(true, true);
        setToolbar();
        setMainPanel(issue);
    }

    public void updateIssue(JiraIssue issue){
        setMainPanel(issue);
        this.mainPanel.repaint();
    }

    private void setMainPanel(JiraIssue issue) {
        this.currentIssue = issue;
        this.mainPanel = new JBPanel(new BorderLayout());

        JBPanel issueDetails = new JBPanel().withBackground(JBColor.WHITE);
        issueDetails.setLayout(new BoxLayout(issueDetails, BoxLayout.Y_AXIS));

        // Key
        JBPanel issueKeyPanel = new JBPanel(new BorderLayout()).andTransparent();
        JBLabel keyLabel = new JBLabel(issue.getKey());
        keyLabel.setHorizontalAlignment(SwingUtilities.LEFT);
        issueKeyPanel.add(keyLabel, BorderLayout.LINE_START);

        // Summary
        JBPanel issueSummaryPanel = new JBPanel(new BorderLayout()).andTransparent();
        JBLabel summaryLabel = new JBLabel("Summary: " + issue.getSummary());
        summaryLabel.setHorizontalAlignment(SwingUtilities.LEFT);
        issueSummaryPanel.add(summaryLabel, BorderLayout.LINE_START);

        // Type and Status
        JBPanel typeAndStatusPanel = new JBPanel(new GridLayout(1, 2)).andTransparent();
        JBPanel typePanel = new JBPanel(new BorderLayout()).andTransparent();
        JBLabel typeLabel = new JBLabel("Type: ");
        typeLabel.setHorizontalAlignment(SwingUtilities.LEFT);

        JBLabel typeValueLabel = new JBLabel(JiraIconUtil.getIcon(issue.getIssuetype().getIconUrl()));
        typeValueLabel.setHorizontalAlignment(SwingUtilities.LEFT);
        typeValueLabel.setText(issue.getIssuetype().getName());

        typePanel.add(typeLabel, BorderLayout.LINE_START);
        typePanel.add(typeValueLabel, BorderLayout.CENTER);


        JBPanel statusPanel = new JBPanel(new BorderLayout()).andTransparent();
        JBLabel statusLabel = new JBLabel("Status: ");
        statusLabel.setHorizontalAlignment(SwingUtilities.LEFT);

        JBLabel statusValueLabel = new JBLabel(JiraIconUtil.getIcon(issue.getStatus().getIconUrl()));
        statusValueLabel.setHorizontalAlignment(SwingUtilities.LEFT);
        statusValueLabel.setText(issue.getStatus().getName());

        statusPanel.add(statusLabel, BorderLayout.LINE_START);
        statusPanel.add(statusValueLabel, BorderLayout.CENTER);


        typeAndStatusPanel.add(typePanel);
        typeAndStatusPanel.add(statusPanel);

        // Priority and Assignee
        JBPanel priorityAndAssigneePanel = new JBPanel(new GridLayout(1, 2)).andTransparent();
        JBLabel priorityLabel = new JBLabel(JiraIconUtil.getSmallIcon(issue.getPriority().getIconUrl()));
        priorityLabel.setHorizontalAlignment(SwingUtilities.LEFT);
        priorityLabel.setText("Priority: " + issue.getPriority().getName());

        JBLabel assigneeLabel = new JBLabel("Assigne: " + (issue.getAssignee() != null ? issue.getAssignee().getDisplayName() : "-"));
        assigneeLabel.setHorizontalAlignment(SwingUtilities.LEFT);
        priorityAndAssigneePanel.add(priorityLabel);
        priorityAndAssigneePanel.add(assigneeLabel);

        // Description
        JBPanel issueDescriptionPanel = new JBPanel().andTransparent();
        JTextArea descriptionArea = new JTextArea("Description: " + issue.getDescription());

        issueDescriptionPanel.add(descriptionArea);

        issueDetails.add(issueKeyPanel);
        issueDetails.add(issueSummaryPanel);
        issueDetails.add(typeAndStatusPanel);
        issueDetails.add(priorityAndAssigneePanel);
        issueDetails.add(issueDescriptionPanel);

        this.mainPanel.add(ScrollPaneFactory.createScrollPane(issueDetails, VERTICAL_SCROLLBAR_AS_NEEDED, HORIZONTAL_SCROLLBAR_AS_NEEDED), BorderLayout.CENTER);
        super.setContent(this.mainPanel);

    }

    private void setToolbar(){
        this.toolbar = ActionManager.getInstance().createActionToolbar(TOOL_WINDOW_ID, createActionGroup(), true);
        this.toolbar.setTargetComponent(this);

        Box toolBarBox = Box.createHorizontalBox();
        toolBarBox.add(this.toolbar.getComponent());
        super.setToolbar(toolBarBox);
    }



    private ActionGroup createActionGroup(){
        SimpleActionGroup group = new SimpleActionGroup();
        getIssuePanelActions().forEach((group)::add);
        return group;
    }

    private List<AnAction> getIssuePanelActions(){
        return Lists.newArrayList(JiraActionManager.getInstance().getJiraIssuesRefreshAction());
    }
}
