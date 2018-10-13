package com.intellij.jira.tasks;

import com.intellij.jira.components.JiraIssueUpdater;
import com.intellij.jira.exceptions.InvalidResultException;
import com.intellij.jira.helper.TransitionFieldHelper;
import com.intellij.jira.helper.TransitionFieldHelper.FieldEditorInfo;
import com.intellij.jira.rest.model.JiraIssue;
import com.intellij.jira.util.Result;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class TransitIssueTask extends AbstractBackgroundableTask {

    private String issueId;
    private String transitionId;
    private Map<String, FieldEditorInfo> requiredFields;
    private Map<String, FieldEditorInfo> optionalFields;

    public TransitIssueTask(@NotNull Project project, String issueId, String transitionId, Map<String, FieldEditorInfo> requiredFields, Map<String, FieldEditorInfo> optionalFields) {
        super(project, "Transiting Issue...");
        this.issueId = issueId;
        this.transitionId = transitionId;
        this.requiredFields = requiredFields;
        this.optionalFields = optionalFields;
    }

    @Override
    public void run(@NotNull ProgressIndicator indicator) {
        JiraServer jiraServer = getJiraServer();
        Result result = jiraServer.transitIssue(issueId, transitionId, requiredFields, optionalFields);
        if(!result.isValid()) {
            throw new InvalidResultException("Transition error", "Issue has not been updated");
        }

        // Retrieve updated issue
        Result issueResult = jiraServer.getIssue(issueId);
        if(issueResult.isValid()){
            JiraIssue issue = (JiraIssue) issueResult.get();
            // Update panels
            JiraIssueUpdater.getInstance().update(issue);
        }

    }


    @Override
    public void onSuccess() {
        showNotification("Transition successful", "Issue status has been updated");
    }

}
