package com.intellij.jira.ui.panels;

import com.intellij.jira.components.JiraActionManager;
import com.intellij.jira.rest.model.JiraIssue;
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
        this.mainPanel = new JBPanel();

        JBPanel issueDetails = new JBPanel().withBackground(JBColor.WHITE);
        issueDetails.setLayout(new BoxLayout(issueDetails, BoxLayout.Y_AXIS));

        JBPanel issueKeyPanel = new JBPanel().andTransparent();
        issueKeyPanel.setAlignmentX(JLabel.LEFT_ALIGNMENT);
        issueKeyPanel.add(new JBLabel("Key: " + issue.getKey()));

        JBPanel issueSummaryPanel = new JBPanel().andTransparent();
        issueSummaryPanel.setAlignmentX(JLabel.LEFT_ALIGNMENT);
        issueSummaryPanel.add(new JBLabel("Summary: " + issue.getSummary()));

        JBPanel issueDescriptionPanel = new JBPanel().andTransparent();
        issueDescriptionPanel.setAlignmentX(JLabel.LEFT_ALIGNMENT);
        issueDescriptionPanel.add(new JBLabel("Description: " + issue.getDescription()));

        issueDetails.add(issueKeyPanel);
        issueDetails.add(issueSummaryPanel);
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
