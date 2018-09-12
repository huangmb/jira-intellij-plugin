package com.intellij.jira.tasks;

import com.intellij.jira.components.JiraIssueUpdater;
import com.intellij.jira.exceptions.InvalidResultException;
import com.intellij.jira.rest.model.JiraIssue;
import com.intellij.jira.util.Result;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class AddCommentTask extends AbstractBackgroundableTask {

    private String issueKey;
    private String body;

    public AddCommentTask(@Nullable Project project, String issueKey, String body) {
        super(project, "Adding a comment");
        this.issueKey = issueKey;
        this.body = body;
    }

    @Override
    public void run(@NotNull ProgressIndicator indicator) {
        JiraServer jiraServer = getJiraServer();
        Result result = jiraServer.addCommentToIssue(body, issueKey);
        if(!result.isValid()) {
            throw new InvalidResultException("Error", "Issue comment has not been added");
        }

        // Retrieve updated issue
        JiraIssue issue = jiraServer.getIssue(issueKey);
        // Update panels
        JiraIssueUpdater.getInstance().update(issue);
    }

    @Override
    public void onSuccess() {
        showNotification("Jira", "Comment added successfully");
    }



}
