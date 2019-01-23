package com.intellij.jira.tasks;

import com.intellij.jira.exceptions.InvalidResultException;
import com.intellij.jira.helper.TransitionFieldHelper.FieldEditorInfo;
import com.intellij.jira.rest.model.JiraIssue;
import com.intellij.jira.server.JiraRestApi;
import com.intellij.jira.util.Result;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class TransitIssueTask extends AbstractBackgroundableTask {

    private String issueId;
    private String transitionId;
    private Map<String, FieldEditorInfo> fields;

    public TransitIssueTask(@NotNull Project project, String issueId, String transitionId, Map<String, FieldEditorInfo> transitionFields) {
        super(project, "Transiting Issue...");
        this.issueId = issueId;
        this.transitionId = transitionId;
        this.fields = transitionFields;
    }

    @Override
    public void run(@NotNull ProgressIndicator indicator) {
        JiraRestApi jiraRestApi = getJiraRestApi();
        Result result = jiraRestApi.transitIssue(issueId, transitionId, fields);
        if(!result.isValid()) {
            throw new InvalidResultException("Transition error", "Issue has not been updated");
        }

        // Retrieve updated issue
        Result issueResult = jiraRestApi.getIssue(issueId);
        if(issueResult.isValid()){
            JiraIssue issue = (JiraIssue) issueResult.get();
            // Update panels
            getJiraIssueUpdater().update(issue);
        }

    }


    @Override
    public void onSuccess() {
        showNotification("Transition successful", "Issue status has been updated");
    }

}
