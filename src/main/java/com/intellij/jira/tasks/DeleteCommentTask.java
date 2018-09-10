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

import java.util.Optional;

import static java.util.Objects.nonNull;

public class DeleteCommentTask extends Task.Backgroundable {
    private String issueKey;
    private String commentId;

    public DeleteCommentTask(@NotNull Project project, String issueKey, String commentId) {
        super(project, "Deleting comment...");
        this.issueKey = issueKey;
        this.commentId = commentId;
    }

    @Override
    public void run(@NotNull ProgressIndicator indicator) {
        JiraServerManager jiraServerManager = myProject.getComponent(JiraServerManager.class);
        Optional<JiraServer> jiraServer = jiraServerManager.getConfiguredJiraServer();
        if(!jiraServer.isPresent()) {
            throw new JiraServerConfigurationNotFoundException();
        }

        Result result = jiraServer.get().deleteCommentToIssue(issueKey, commentId);
        if(!result.isValid()) {
            throw new InvalidResultException("Error", "Issue comment has not been deleted");
        }

        // Retrieve updated issue
        JiraIssue issue = jiraServer.get().getIssue(issueKey);
        // Update panels
        JiraIssueUpdater.getInstance().update(issue);
    }

    @Override
    public void onSuccess() {
        Notifications.Bus.notify(JiraNotificationComponent.getInstance().createNotification("Jira", "Comment deleted successfully"));
    }

    @Override
    public void onThrowable(@NotNull Throwable error) {
        String content = nonNull(error.getCause()) ? error.getCause().getMessage() : "";
        Notifications.Bus.notify(JiraNotificationComponent.getInstance().createNotificationError(error.getMessage(), content));
    }
}
