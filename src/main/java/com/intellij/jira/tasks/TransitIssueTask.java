package com.intellij.jira.tasks;

import com.intellij.jira.components.JiraIssueUpdater;
import com.intellij.jira.exceptions.InvalidResultException;
import com.intellij.jira.exceptions.JiraServerConfigurationNotFoundException;
import com.intellij.jira.notifications.JiraNotificationComponent;
import com.intellij.jira.rest.model.JiraIssue;
import com.intellij.jira.util.JiraIssueTransitionResult;
import com.intellij.notification.Notifications;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.progress.Task;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

import static java.util.Objects.nonNull;

public class TransitIssueTask extends Task.Backgroundable {

    private Project project;
    private String issueId;
    private String transitionId;

    public TransitIssueTask(@Nullable Project project, String issueId, String transitionId) {
        super(project, "Transiting Issue...", false, ALWAYS_BACKGROUND);
        this.project = project;
        this.transitionId = transitionId;
        this.issueId = issueId;
    }

    @Override
    public void run(@NotNull ProgressIndicator indicator) {
        JiraServerManager jiraServerManager = project.getComponent(JiraServerManager.class);
        Optional<JiraServer> jiraServer = jiraServerManager.getConfiguredJiraServer();
        if(!jiraServer.isPresent()) {
            throw new JiraServerConfigurationNotFoundException();
        }

        JiraIssueTransitionResult result = jiraServer.get().doTransition(issueId, transitionId);
        if(!result.isValid()) {
            throw new InvalidResultException("Transition error", "Issue has not been updated");
        }

        // Retrieve updated issue
        JiraIssue issue = jiraServer.get().getIssue(issueId);
        // Update panels
        JiraIssueUpdater.getInstance().update(issue);
    }


    @Override
    public void onSuccess() {
        Notifications.Bus.notify(JiraNotificationComponent.getInstance().createNotification("Transition successful", "Issue status has been updated"));
    }

    @Override
    public void onThrowable(@NotNull Throwable error) {
        String content = nonNull(error.getCause()) ? error.getCause().getMessage() : "";
        Notifications.Bus.notify(JiraNotificationComponent.getInstance().createNotificationError(error.getMessage(), content));
    }
}
