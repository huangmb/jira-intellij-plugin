package com.intellij.jira.ui;

import com.intellij.jira.tasks.JiraServer;
import com.intellij.jira.tasks.JiraTaskManager;
import com.intellij.jira.ui.panels.JiraIssuesPanel;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.intellij.openapi.wm.ToolWindowType;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentManager;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.util.Optional;

public class JiraToolWindowFactory implements ToolWindowFactory {

    public static final String TOOL_WINDOW_ID = "JIRA";
    public static final String TAB_ISSUES = "Issues";


    @Override
    public void createToolWindowContent(@NotNull Project project, @NotNull ToolWindow toolWindow) {
        createContent(project, toolWindow);

        project.getComponent(JiraTaskManager.class).addConfigurationServerChangedListener(() -> {
            SwingUtilities.invokeLater(() -> createContent(project, toolWindow));
        });

        toolWindow.setType(ToolWindowType.DOCKED, null);
    }

    private void createContent(Project project, ToolWindow toolWindow) {
        ContentManager contentManager = toolWindow.getContentManager();
        contentManager.removeAllContents(true);

        Optional<JiraServer> jiraServer =  project.getComponent(JiraTaskManager.class).getConfiguredJiraServer();
        JiraIssuesPanel issuesPanel = new JiraIssuesPanel(jiraServer);

        Content content = contentManager.getFactory().createContent(issuesPanel, TAB_ISSUES, false);
        contentManager.addDataProvider(issuesPanel);
        contentManager.addContent(content);
    }





}
