package com.intellij.jira.ui;

import com.intellij.jira.rest.model.JiraIssue;
import com.intellij.jira.tasks.JiraServer;
import com.intellij.jira.tasks.JiraServerManager;
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

    private JiraIssuesPanel issuesPanel;

    @Override
    public void createToolWindowContent(@NotNull Project project, @NotNull ToolWindow toolWindow) {
        createContent(project, toolWindow);

        project.getComponent(JiraServerManager.class).addConfigurationServerChangedListener(() -> {
            SwingUtilities.invokeLater(() -> updateContent(project));
        });

        toolWindow.setType(ToolWindowType.DOCKED, null);
    }

    private void createContent(Project project, ToolWindow toolWindow) {
        ContentManager contentManager = toolWindow.getContentManager();
        contentManager.removeAllContents(true);

        Optional<JiraServer> jiraServer =  getJiraServer(project);
        issuesPanel = new JiraIssuesPanel(jiraServer);

        Content content = contentManager.getFactory().createContent(issuesPanel, TAB_ISSUES, false);
        contentManager.addDataProvider(issuesPanel);
        contentManager.addContent(content);
    }


    private void updateContent(Project project){
        getJiraServer(project)
            .ifPresent(jiraServer -> issuesPanel.update(jiraServer.getIssues()));
    }


    private Optional<JiraServer> getJiraServer(Project project){
        return project.getComponent(JiraServerManager.class).getConfiguredJiraServer();
    }

    public void update(JiraIssue issue){
        issuesPanel.update(issue);
    }

}
