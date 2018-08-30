package com.intellij.jira.ui.panels;

import com.intellij.jira.components.JiraActionManager;
import com.intellij.jira.rest.model.JiraIssue;
import com.intellij.jira.tasks.JiraServer;
import com.intellij.jira.ui.table.JiraIssueTableView;
import com.intellij.jira.util.JiraPanelUtil;
import com.intellij.openapi.actionSystem.ActionGroup;
import com.intellij.openapi.actionSystem.ActionManager;
import com.intellij.openapi.actionSystem.ActionToolbar;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.ui.SimpleToolWindowPanel;
import com.intellij.tools.SimpleActionGroup;
import com.intellij.ui.JBSplitter;
import com.intellij.ui.ScrollPaneFactory;
import org.fest.util.Lists;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.Optional;

import static com.intellij.jira.ui.JiraToolWindowFactory.TOOL_WINDOW_ID;

public class JiraIssuesPanel extends SimpleToolWindowPanel {

    private Optional<JiraServer> jiraServer;
    private JiraIssueDetailsPanel issueDetailsPanel;

    public JiraIssuesPanel(Optional<JiraServer> jiraServer) {
        super(false, true);
        this.jiraServer = jiraServer;
        init();
    }

    private void init() {
        setToolbar();
        setContent();
    }

    private void setContent() {
        JComponent content;
        if(!jiraServer.isPresent()){
            content = JiraPanelUtil.createPlaceHolderPanel("No Jira server found");
        }else{
            List<JiraIssue> issues = jiraServer.get().getIssues();
            issueDetailsPanel = new JiraIssueDetailsPanel();

            JiraIssueTableView issueTable = new JiraIssueTableView(issues);
            issueTable.getSelectionModel().addListSelectionListener(event -> {
                SwingUtilities.invokeLater(() -> {
                    this.issueDetailsPanel.updateIssue(issueTable.getSelectedObject());
                });
            });


            JPanel issuesPanel = new JPanel(new BorderLayout());
            issuesPanel.add(ScrollPaneFactory.createScrollPane(issueTable), BorderLayout.CENTER);


            JBSplitter splitter = new JBSplitter();
            splitter.setProportion(0.6f);
            splitter.setFirstComponent(issuesPanel);
            splitter.setSecondComponent(issueDetailsPanel);

            content = splitter;
        }

        super.setContent(content);

    }



    private void setToolbar(){
        ActionToolbar actionToolbar = ActionManager.getInstance().createActionToolbar(TOOL_WINDOW_ID, createActionGroup(), false);
        actionToolbar.setTargetComponent(this);
        Box toolBarBox = Box.createHorizontalBox();
        toolBarBox.add(actionToolbar.getComponent());
        super.setToolbar(toolBarBox);
    }

    private ActionGroup createActionGroup(){
        SimpleActionGroup group = new SimpleActionGroup();
        getIssuePanelActions().forEach((group)::add);
        return group;
    }

    private List<AnAction> getIssuePanelActions(){
        return Lists.newArrayList(JiraActionManager.getInstance().getJiraIssuesRefreshAction(),
                                    ActionManager.getInstance().getAction("tasks.configure.servers"));
    }

}
