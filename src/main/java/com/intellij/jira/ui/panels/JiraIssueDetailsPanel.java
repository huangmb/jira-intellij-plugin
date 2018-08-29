package com.intellij.jira.ui.panels;

import com.intellij.jira.components.JiraActionManager;
import com.intellij.jira.rest.model.JiraIssue;
import com.intellij.jira.ui.labels.LinkLabel;
import com.intellij.jira.util.JiraIconUtil;
import com.intellij.jira.util.JiraLabelUtil;
import com.intellij.jira.util.JiraPanelUtil;
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
import static com.intellij.jira.util.JiraLabelUtil.BOLD;
import static com.intellij.jira.util.JiraPanelUtil.MARGIN_BOTTOM;
import static java.awt.BorderLayout.*;
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
        if(!currentIssue.getKey().equals(issue.getKey())) {
            this.mainPanel.removeAll();
            setMainPanel(issue);
            this.mainPanel.repaint();
        }
    }

    private void setMainPanel(JiraIssue issue) {
        this.currentIssue = issue;
        this.mainPanel = new JBPanel(new BorderLayout())
                            .withBackground(JBColor.WHITE)
                            .withBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));


        JBPanel issueDetails = new JBPanel().withBackground(JBColor.WHITE);
        issueDetails.setLayout(new BoxLayout(issueDetails, BoxLayout.Y_AXIS));

        // Key
        JBPanel issueKeyPanel = JiraPanelUtil.createWhitePanel(new BorderLayout()).withBorder(MARGIN_BOTTOM);
        JBLabel keyLabel = JiraLabelUtil.createLinkLabel(issue.getKey(), issue.getSelf());
        issueKeyPanel.add(keyLabel, LINE_START);

        // Summary
        JBPanel issueSummaryPanel = JiraPanelUtil.createWhitePanel(new BorderLayout()).withBorder(MARGIN_BOTTOM);
        JBLabel summaryLabel = JiraLabelUtil.createLabel(issue.getSummary());
        issueSummaryPanel.add(summaryLabel, LINE_START);

        // Type and Status
        JBPanel typeAndStatusPanel = JiraPanelUtil.createWhitePanel(new GridLayout(1, 2)).withBorder(MARGIN_BOTTOM);
        JBPanel typePanel = JiraPanelUtil.createWhitePanel(new BorderLayout());
        JBLabel typeLabel = JiraLabelUtil.createLabel("Type: ").withFont(BOLD);
        JBLabel typeValueLabel = JiraLabelUtil.createIconLabel(issue.getIssuetype().getIconUrl(), issue.getIssuetype().getName());

        typePanel.add(typeLabel, LINE_START);
        typePanel.add(typeValueLabel, CENTER);

        JBPanel statusPanel = JiraPanelUtil.createWhitePanel(new BorderLayout());
        JBLabel statusLabel = JiraLabelUtil.createLabel("Status: ").withFont(BOLD);
        JBLabel statusValueLabel = JiraLabelUtil.createIconLabel(issue.getStatus().getIconUrl(), issue.getStatus().getName());

        statusPanel.add(statusLabel, LINE_START);
        statusPanel.add(statusValueLabel, CENTER);

        typeAndStatusPanel.add(typePanel);
        typeAndStatusPanel.add(statusPanel);

        // Priority and Assignee
        JBPanel priorityAndAssigneePanel = JiraPanelUtil.createWhitePanel(new GridLayout(1, 2)).withBorder(MARGIN_BOTTOM);
        JBPanel priorityPanel = JiraPanelUtil.createWhitePanel(new BorderLayout());
        JBLabel priorityLabel = JiraLabelUtil.createLabel("Priority: ").withFont(BOLD);
        JBLabel priorityValueLabel = JiraLabelUtil.createIconLabel(JiraIconUtil.getSmallIcon(issue.getPriority().getIconUrl()), issue.getPriority().getName());

        priorityPanel.add(priorityLabel, LINE_START);
        priorityPanel.add(priorityValueLabel, CENTER);

        JBPanel assigneePanel = JiraPanelUtil.createWhitePanel(new BorderLayout());
        JBLabel assigneeLabel = JiraLabelUtil.createLabel("Assigne: ").withFont(BOLD);
        JBLabel assigneeValueLabel = JiraLabelUtil.createLabel(issue.getAssignee() != null ? issue.getAssignee().getDisplayName() : "-");

        assigneePanel.add(assigneeLabel, LINE_START);
        assigneePanel.add(assigneeValueLabel, CENTER);

        priorityAndAssigneePanel.add(priorityPanel);
        priorityAndAssigneePanel.add(assigneePanel);

        // Description
        JBPanel issueDescriptionPanel = JiraPanelUtil.createWhitePanel(new BorderLayout());
        JBLabel descriptionLabel = JiraLabelUtil.createLabel("Description").withFont(BOLD).withBorder(MARGIN_BOTTOM);
        JTextArea descriptionArea = new JTextArea(issue.getDescription());
        descriptionArea.setLineWrap(true);

        issueDescriptionPanel.add(descriptionLabel, PAGE_START);
        issueDescriptionPanel.add(descriptionArea, CENTER);

        issueDetails.add(issueKeyPanel);
        issueDetails.add(issueSummaryPanel);
        issueDetails.add(typeAndStatusPanel);
        issueDetails.add(priorityAndAssigneePanel);
        issueDetails.add(issueDescriptionPanel);

        this.mainPanel.add(ScrollPaneFactory.createScrollPane(issueDetails, VERTICAL_SCROLLBAR_AS_NEEDED, HORIZONTAL_SCROLLBAR_AS_NEEDED), CENTER);
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
