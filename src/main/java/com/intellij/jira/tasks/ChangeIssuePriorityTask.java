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

public class ChangeIssuePriorityTask extends Task.Backgroundable {

    private String priorityName;
    private String issueIdOrKey;

    public ChangeIssuePriorityTask(@Nullable Project project, String priorityName, String issueIdOrKey) {
        super(project, "Updating Issue Priority...", false, ALWAYS_BACKGROUND);
        this.priorityName = priorityName;
        this.issueIdOrKey = issueIdOrKey;
    }

    @Override
    public void run(@NotNull ProgressIndicator indicator) {
        JiraServerManager jiraServerManager = myProject.getComponent(JiraServerManager.class);
        Optional<JiraServer> jiraServer = jiraServerManager.getConfiguredJiraServer();
        if(!jiraServer.isPresent()) {
            throw new JiraServerConfigurationNotFoundException();
        }

        Result result = jiraServer.get().changeIssuePriority(priorityName, issueIdOrKey);
        if(!result.isValid()){
            throw new InvalidResultException("Error", "Issue priority has not been updated");
        }

        // Retrieve updated issue
        JiraIssue issue = jiraServer.get().getIssue(issueIdOrKey);
        // Update panels
        JiraIssueUpdater.getInstance().update(issue);

    }

    @Override
    public void onSuccess() {
        Notifications.Bus.notify(JiraNotificationComponent.getInstance().createNotification("Jira", "Issue priority updated"));
    }

    @Override
    public void onThrowable(@NotNull Throwable error) {
        String content = nonNull(error.getCause()) ? error.getCause().getMessage() : "";
        Notifications.Bus.notify(JiraNotificationComponent.getInstance().createNotificationError(error.getMessage(), content));
    }

}
