package com.intellij.jira.tasks;

import com.intellij.jira.components.JiraIssueUpdater;
import com.intellij.jira.exceptions.InvalidResultException;
import com.intellij.jira.rest.model.JiraIssue;
import com.intellij.jira.util.Result;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;

public class ChangeIssuePriorityTask extends AbstractIssueTask {

    private String priorityName;
    private String issueIdOrKey;

    public ChangeIssuePriorityTask(@NotNull Project project, String priorityName, String issueIdOrKey) {
        super(project, "Updating Issue Priority...");
        this.priorityName = priorityName;
        this.issueIdOrKey = issueIdOrKey;
    }

    @Override
    public void run(@NotNull ProgressIndicator indicator) {
        JiraServer jiraServer = getJiraServer();
        Result result = jiraServer.changeIssuePriority(priorityName, issueIdOrKey);
        if(!result.isValid()){
            throw new InvalidResultException("Error", "Issue priority has not been updated");
        }

        // Retrieve updated issue
        JiraIssue issue = jiraServer.getIssue(issueIdOrKey);
        // Update panels
        JiraIssueUpdater.getInstance().update(issue);

    }

    @Override
    public void onSuccess() {
        showNotification("Jira", "Issue priority updated");
    }

}
