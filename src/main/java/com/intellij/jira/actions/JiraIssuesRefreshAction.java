package com.intellij.jira.actions;

import com.intellij.jira.notifications.JiraNotificationComponent;
import com.intellij.jira.tasks.JiraServer;
import com.intellij.jira.tasks.JiraTaskManager;
import com.intellij.notification.Notifications;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;

import java.util.Optional;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

public class JiraIssuesRefreshAction extends AnAction {


    @Override
    public void update(AnActionEvent event) {
        Project project = event.getProject();
        if (isNull(project)|| !project.isInitialized() || project.isDisposed()) {
            event.getPresentation().setEnabled(false);
        } else {
            JiraTaskManager component = project.getComponent(JiraTaskManager.class);
            Optional<JiraServer> jiraServer = component.getConfiguredJiraServer();
            if(jiraServer.isPresent()){
                event.getPresentation().setEnabled(true);
            }
            else{
                event.getPresentation().setEnabled(false);
            }
        }
    }

    @Override
    public void actionPerformed(AnActionEvent event) {
        Project project = event.getProject();
        if(nonNull(project)){
            JiraTaskManager component = project.getComponent(JiraTaskManager.class);
            Optional<JiraServer> jiraServer = component.getConfiguredJiraServer();
            if(jiraServer.isPresent()){
                component.syncJiraIssues();
                Notifications.Bus.notify(JiraNotificationComponent.getInstance().createNotification("JIRA", "Issues are up to date"));
            }
            else{
                Notifications.Bus.notify(JiraNotificationComponent.getInstance().createNotificationError("No Jira server found", "Cannot update issues"));
            }
        }

    }

}
