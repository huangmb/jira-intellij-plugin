package com.intellij.jira.ui;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.intellij.openapi.wm.ToolWindowType;
import com.intellij.ui.content.Content;
import org.jetbrains.annotations.NotNull;

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
        JiraIssuesPanel issuesPanel = new JiraIssuesPanel();
        Content issuesContent = toolWindow.getContentManager().getFactory()
                .createContent(issuesPanel, TAB_ISSUES,false);
        toolWindow.getContentManager().addDataProvider(issuesPanel);
        toolWindow.getContentManager().addContent(issuesContent);
    }

    private static void addProjectsTab(Project project, ToolWindow toolWindow) {
        JiraProjectsPanel projectsPanel = new JiraProjectsPanel();
        Content projectsContent = toolWindow.getContentManager().getFactory()
                .createContent(projectsPanel, TAB_PROJECTS,false);
        toolWindow.getContentManager().addDataProvider(projectsPanel);
        toolWindow.getContentManager().addContent(projectsContent);
    }


}
