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

public class AddCommentTask extends Task.Backgroundable {

    private String issueKey;
    private String body;

    public AddCommentTask(@Nullable Project project, String issueKey, String body) {
        super(project, "Adding a comment");
        this.issueKey = issueKey;
        this.body = body;
    }

    @Override
    public void run(@NotNull ProgressIndicator indicator) {
        JiraServerManager jiraServerManager = myProject.getComponent(JiraServerManager.class);
        Optional<JiraServer> jiraServer = jiraServerManager.getConfiguredJiraServer();
        if(!jiraServer.isPresent()) {
            throw new JiraServerConfigurationNotFoundException();
        }

        Result result = jiraServer.get().addCommentToIssue(body, issueKey);
        if(!result.isValid()) {
            throw new InvalidResultException("Error", "Issue comment has not been added");
        }

        // Retrieve updated issue
        JiraIssue issue = jiraServer.get().getIssue(issueKey);
        // Update panels
        JiraIssueUpdater.getInstance().update(issue);
    }

    @Override
    public void onSuccess() {
        Notifications.Bus.notify(JiraNotificationComponent.getInstance().createNotification("Jira", "Comment added successfully"));
    }

    @Override
    public void onThrowable(@NotNull Throwable error) {
        String content = nonNull(error.getCause()) ? error.getCause().getMessage() : "";
        Notifications.Bus.notify(JiraNotificationComponent.getInstance().createNotificationError(error.getMessage(), content));
    }


}
