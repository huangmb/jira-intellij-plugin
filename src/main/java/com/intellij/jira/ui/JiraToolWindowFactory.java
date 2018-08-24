package com.intellij.jira.ui;

import com.intellij.jira.tasks.JiraServer;
import com.intellij.jira.tasks.JiraTaskManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.intellij.openapi.wm.ToolWindowType;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentManager;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.util.Optional;

public class JiraToolWindowFactory implements ToolWindowFactory {

    public static final String TOOL_WINDOW_ID = "JIRA";
    public static final String TAB_ISSUES = "Issues";
    public static final String TAB_PROJECTS = "Projects";

    @Override
    public void createToolWindowContent(@NotNull Project project, @NotNull ToolWindow toolWindow) {
        addIssuesTab(project, toolWindow);
        addProjectsTab(project, toolWindow);
        toolWindow.setType(ToolWindowType.DOCKED, null);
    }

    private static void addIssuesTab(Project project, ToolWindow toolWindow) {
        ContentManager contentManager = toolWindow.getContentManager();
        Content content = null;

        Optional<JiraServer> jiraServer = new JiraTaskManager(project).getConfiguredJiraServer();
        if(!jiraServer.isPresent()){
            content = contentManager.getFactory().createContent(createPlaceHolderPanel(), TAB_ISSUES, false);
        }
        else{
            JiraIssuesPanel issuesPanel = new JiraIssuesPanel(jiraServer.get());
            content = contentManager.getFactory().createContent(issuesPanel, TAB_ISSUES,false);
            contentManager.addDataProvider(issuesPanel);
        }

        contentManager.addContent(content);
    }

    private static JComponent createPlaceHolderPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        JPanel labelPanel = new JPanel();
        JLabel messageLabel = new JLabel("No Jira server found");
        messageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        labelPanel.add(messageLabel);
        panel.add(labelPanel, BorderLayout.CENTER);

        return panel;
    }

    private static void addProjectsTab(Project project, ToolWindow toolWindow) {
        JiraProjectsPanel projectsPanel = new JiraProjectsPanel();
        Content projectsContent = toolWindow.getContentManager().getFactory()
                .createContent(projectsPanel, TAB_PROJECTS,false);
        toolWindow.getContentManager().addDataProvider(projectsPanel);
        toolWindow.getContentManager().addContent(projectsContent);
    }


}
