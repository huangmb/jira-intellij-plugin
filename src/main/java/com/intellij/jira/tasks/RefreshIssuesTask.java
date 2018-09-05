package com.intellij.jira.tasks;

import com.intellij.jira.exceptions.JiraServerConfigurationNotFoundException;
import com.intellij.jira.notifications.JiraNotificationComponent;
import com.intellij.notification.Notifications;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.progress.Task;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

import static java.util.Objects.nonNull;

public class RefreshIssuesTask extends Task.Backgroundable{

    private Project project;

    public RefreshIssuesTask(@Nullable Project project) {
        super(project, "Updating Issues from Server", false, ALWAYS_BACKGROUND);
        this.project = project;
    }

    @Override
    public void run(@NotNull ProgressIndicator indicator) {
        JiraServerManager jiraServerManager = project.getComponent(JiraServerManager.class);
        Optional<JiraServer> jiraServer = jiraServerManager.getConfiguredJiraServer();
        if(!jiraServer.isPresent()) {
            throw new JiraServerConfigurationNotFoundException("Cannot connect to server for updating issues");
        }

        jiraServerManager.syncJiraIssues();
    }

    @Override
    public void onSuccess() {
        Notifications.Bus.notify(JiraNotificationComponent.getInstance().createNotification("Jira", "Issues are now up to date"));
    }


    @Override
    public void onThrowable(@NotNull Throwable error) {
        String content = nonNull(error.getCause()) ? error.getCause().getMessage() : "";
        Notifications.Bus.notify(JiraNotificationComponent.getInstance().createNotificationError(error.getMessage(), content));
    }
}
