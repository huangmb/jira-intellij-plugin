package com.intellij.jira.tasks;

import com.intellij.jira.components.JiraIssueUpdater;
import com.intellij.jira.exceptions.InvalidResultException;
import com.intellij.jira.rest.model.JiraIssue;
import com.intellij.jira.util.Result;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;

public class TransitIssueTask extends AbstractBackgroundableTask {

    private String issueId;
    private String transitionId;

    public TransitIssueTask(@NotNull Project project, String issueId, String transitionId) {
        super(project, "Transiting Issue...");
        this.issueId = issueId;
        this.transitionId = transitionId;
    }

    @Override
    public void run(@NotNull ProgressIndicator indicator) {
        JiraServer jiraServer = getJiraServer();
        Result result = jiraServer.transitIssue(issueId, transitionId);
        if(!result.isValid()) {
            throw new InvalidResultException("Transition error", "Issue has not been updated");
        }

        // Retrieve updated issue
        JiraIssue issue = jiraServer.getIssue(issueId);
        // Update panels
        JiraIssueUpdater.getInstance().update(issue);
    }


    @Override
    public void onSuccess() {
        showNotification("Transition successful", "Issue status has been updated");
    }

}
