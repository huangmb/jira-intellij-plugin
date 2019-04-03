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

import static com.intellij.jira.rest.model.JiraPermission.LINK_ISSUE;

public class AddIssueLinkTask extends AbstractBackgroundableTask {

    private String issueKey;

    private String linkType;
    private String inIssueKey;
    private String outIssueKey;


    public AddIssueLinkTask(@Nullable Project project, String issueKey, String linkType, String inIssueKey, String outIssueKey) {
        super(project, "Adding issue link");
        this.issueKey = issueKey;
        this.linkType = linkType;
        this.inIssueKey = inIssueKey;
        this.outIssueKey = outIssueKey;
    }

    @Override
    public void run(@NotNull ProgressIndicator indicator) {
        JiraRestApi jiraRestApi = getJiraRestApi();
        // Check if user has permission to create links
        boolean hasPermission = jiraRestApi.userHasPermissionOnIssue(issueKey, LINK_ISSUE);
        if(!hasPermission){
            throw new InvalidPermissionException("Jira", "You don't have permission to create issue links");
        }

        Result result = jiraRestApi.addIssueLink(linkType, inIssueKey, outIssueKey);
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
        showNotification("Jira", "Issue Link created successfully");
    }





}
