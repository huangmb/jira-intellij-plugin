package com.intellij.jira.tasks;

import com.intellij.jira.components.JiraIssueUpdater;
import com.intellij.jira.exceptions.InvalidResultException;
import com.intellij.jira.exceptions.JiraServerConfigurationNotFoundException;
import com.intellij.jira.notifications.JiraNotificationComponent;
import com.intellij.jira.rest.model.JiraIssue;
import com.intellij.jira.util.Result;
import com.intellij.notification.Notifications;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.progress.Task;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

import static java.util.Objects.nonNull;

public class AssignUserTask extends Task.Backgroundable {

    private String username;
    private String issueKey;

    public AssignUserTask(@Nullable Project project, String username, String issueKey) {
        super(project, "Assigning User to Issue...", false, ALWAYS_BACKGROUND);
        this.username = username;
        this.issueKey = issueKey;
    }

    @Override
    public void run(@NotNull ProgressIndicator indicator) {
        JiraServerManager jiraServerManager = myProject.getComponent(JiraServerManager.class);
        Optional<JiraServer> jiraServer = jiraServerManager.getConfiguredJiraServer();
        if(!jiraServer.isPresent()) {
            throw new JiraServerConfigurationNotFoundException();
        }

        Result result = jiraServer.get().assignUserToIssue(username, issueKey);
        if(!result.isValid()) {
            throw new InvalidResultException("Assignment error", "Issue has not been updated");
        }

        // Retrieve updated issue
        JiraIssue issue = jiraServer.get().getIssue(issueKey);
        // Update panels
        JiraIssueUpdater.getInstance().update(issue);
    }


    @Override
    public void onSuccess() {
        Notifications.Bus.notify(JiraNotificationComponent.getInstance().createNotification("Assignment successful", "Issue assignee has been updated"));
    }

    @Override
    public void onThrowable(@NotNull Throwable error) {
        String content = nonNull(error.getCause()) ? error.getCause().getMessage() : "";
        Notifications.Bus.notify(JiraNotificationComponent.getInstance().createNotificationError(error.getMessage(), content));
    }

}
