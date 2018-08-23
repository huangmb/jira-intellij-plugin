package com.intellij.jira.components;

import com.intellij.openapi.actionSystem.ActionManager;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.ApplicationComponent;
import org.jetbrains.annotations.NotNull;

public class JiraActionManager implements ApplicationComponent {

    private AnAction jiraIssuesRefreshAction;
    private AnAction jiraSettingsAction;


    @Override
    public void initComponent() {
        jiraIssuesRefreshAction = ActionManager.getInstance().getAction("Jira.toolwindow.Refresh");
        jiraSettingsAction = ActionManager.getInstance().getAction("Jira.toolwindow.Settings");
    }

    @Override
    public void disposeComponent() {
        //do nothing
    }

    @NotNull
    @Override
    public String getComponentName() {
        return getClass().getSimpleName();
    }

    public static JiraActionManager getInstance() {
        return ApplicationManager.getApplication().getComponent(JiraActionManager.class);
    }

    public AnAction getJiraIssuesRefreshAction() {
        return jiraIssuesRefreshAction;
    }

    public AnAction getJiraSettingsAction() {
        return jiraSettingsAction;
    }
}
