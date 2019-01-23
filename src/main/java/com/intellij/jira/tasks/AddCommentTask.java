package com.intellij.jira.tasks;

import com.intellij.jira.exceptions.InvalidPermissionException;
import com.intellij.jira.exceptions.InvalidResultException;
import com.intellij.jira.rest.model.JiraIssue;
import com.intellij.jira.server.JiraRestApi;
import com.intellij.jira.util.Result;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static com.intellij.jira.rest.model.JiraPermission.COMMENT_ISSUE;

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
        JiraRestApi jiraRestApi = getJiraRestApi();
        // Check user permissions
        boolean hasPermission = jiraRestApi.userHasPermissionOnIssue(issueKey, COMMENT_ISSUE);
        if(!hasPermission){
            throw new InvalidPermissionException("Jira", "You don't have permission to add a comment");
        }

        Result result = jiraRestApi.addCommentToIssue(body, issueKey);
        if(!result.isValid()) {
            throw new InvalidResultException("Error", "Issue comment has not been added");
        }

        // Retrieve updated issue
        Result issueResult = jiraRestApi.getIssue(issueKey);
        if(issueResult.isValid()){
            JiraIssue issue = (JiraIssue) issueResult.get();
            // Update panels
            getJiraIssueUpdater().update(issue);
        }

    }

    @Override
    public void onSuccess() {
        showNotification("Jira", "Comment added successfully");
    }



}
