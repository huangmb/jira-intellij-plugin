package com.intellij.jira.ui;

import com.intellij.jira.rest.model.JiraIssue;
import com.intellij.jira.tasks.JiraServer;
import com.intellij.jira.tasks.JiraServerManager;
import com.intellij.jira.ui.panels.JiraIssuesPanel;
import com.intellij.jira.ui.panels.JiraProjectsPanel;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.intellij.openapi.wm.ToolWindowType;
import com.intellij.ui.content.Content;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.util.Optional;

public class JiraToolWindowFactory implements ToolWindowFactory {

    public static final String TOOL_WINDOW_ID = "JIRA";
    public static final String TAB_ISSUES = "Issues";
    public static final String TAB_PROJECTS = "Projects";

    private JiraIssuesPanel issuesPanel;
    private JiraProjectsPanel projectsPanel;

    @Override
    public void createToolWindowContent(@NotNull Project project, @NotNull ToolWindow toolWindow) {
        createContent(project, toolWindow);

        project.getComponent(JiraServerManager.class).addConfigurationServerChangedListener(() -> {
            SwingUtilities.invokeLater(() -> createContent(project, toolWindow));
        });

        toolWindow.setType(ToolWindowType.DOCKED, null);
    }

    private void createContent(Project project, ToolWindow toolWindow) {
        toolWindow.getContentManager().removeAllContents(true);

        Optional<JiraServer> jiraServer =  getJiraServer(project);
        addIssuesTab(jiraServer, toolWindow);
        addProjectsTab(jiraServer, toolWindow);
    }

    private void addIssuesTab( Optional<JiraServer> jiraServer, ToolWindow toolWindow){
        issuesPanel = new JiraIssuesPanel(jiraServer);
        Content content = toolWindow.getContentManager().getFactory().createContent(issuesPanel, TAB_ISSUES, false);
        toolWindow.getContentManager().addDataProvider(issuesPanel);
        toolWindow.getContentManager().addContent(content);
    }

    private void addProjectsTab( Optional<JiraServer> jiraServer, ToolWindow toolWindow){
        if(jiraServer.isPresent()){
            projectsPanel = new JiraProjectsPanel(jiraServer.get());
            Content content = toolWindow.getContentManager().getFactory().createContent(projectsPanel, TAB_PROJECTS, false);
            toolWindow.getContentManager().addDataProvider(projectsPanel);
            toolWindow.getContentManager().addContent(content);
        }
    }



    private Optional<JiraServer> getJiraServer(Project project){
        return project.getComponent(JiraServerManager.class).getConfiguredJiraServer();
    }

    public void update(JiraIssue issue){
        issuesPanel.update(issue);
    }

}
