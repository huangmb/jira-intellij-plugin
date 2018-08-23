package com.intellij.jira.ui;

import com.intellij.jira.components.JiraActionManager;
import com.intellij.openapi.actionSystem.ActionGroup;
import com.intellij.openapi.actionSystem.ActionManager;
import com.intellij.openapi.actionSystem.ActionToolbar;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.ui.SimpleToolWindowPanel;
import com.intellij.tools.SimpleActionGroup;
import com.intellij.ui.ScrollPaneFactory;
import com.intellij.ui.components.JBList;
import org.fest.util.Lists;

import javax.swing.*;
import java.awt.*;
import java.util.List;

import static com.intellij.jira.ui.JiraToolWindowFactory.TOOL_WINDOW_ID;

public class JiraIssuesPanel extends SimpleToolWindowPanel {

    private ActionToolbar toolbar;

    public JiraIssuesPanel() {
        super(false, true);
        setToolbar();
        Box toolBarBox = Box.createHorizontalBox();
        toolBarBox.add(toolbar.getComponent());
        super.setToolbar(toolBarBox);
        toolbar.getComponent().setVisible(true);

        //Empty list
        JBList lista = new JBList();

        JPanel issuesPanel = new JPanel(new BorderLayout());
        issuesPanel.add(ScrollPaneFactory.createScrollPane(lista), BorderLayout.CENTER);
        super.setContent(issuesPanel);
    }


    private void setToolbar(){
        this.toolbar = ActionManager.getInstance().createActionToolbar(TOOL_WINDOW_ID, createActionGroup(), false);
        this.toolbar.setTargetComponent(this);
    }

    private ActionGroup createActionGroup(){
        SimpleActionGroup group = new SimpleActionGroup();
        getIssuePanelActions().forEach((group)::add);
        return group;
    }

    private List<AnAction> getIssuePanelActions(){
        return Lists.newArrayList(JiraActionManager.getInstance().getJiraIssuesRefreshAction(),
                                    JiraActionManager.getInstance().getJiraSettingsAction());
    }

}
