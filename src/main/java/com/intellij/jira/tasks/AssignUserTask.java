package com.intellij.jira.tasks;

import com.intellij.jira.exceptions.InvalidResultException;
import com.intellij.jira.rest.model.JiraIssue;
import com.intellij.jira.server.JiraRestApi;
import com.intellij.jira.util.Result;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;

public class AssignUserTask extends AbstractBackgroundableTask {

    private String username;
    private String issueKey;

    public AssignUserTask(@NotNull Project project, String username, String issueKey) {
        super(project, "Assigning User to Issue...");
        this.username = username;
        this.issueKey = issueKey;
    }

    @Override
    public void run(@NotNull ProgressIndicator indicator) {
        JiraRestApi jiraRestApi = getJiraRestApi();
        Result result = jiraRestApi.assignUserToIssue(username, issueKey);
        if(!result.isValid()) {
            throw new InvalidResultException("Assignment error", "Issue has not been updated");
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
        showNotification("Assignment successful", "Issue assignee has been updated");
    }

}
