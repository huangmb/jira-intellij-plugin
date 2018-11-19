package com.intellij.jira.tasks;

import com.intellij.jira.components.JQLSearcherManager;
import com.intellij.jira.components.JiraIssueUpdater;
import com.intellij.jira.exceptions.JiraServerConfigurationNotFoundException;
import com.intellij.jira.rest.model.JiraIssue;
import com.intellij.jira.rest.model.jql.JQLSearcher;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;

public class RefreshIssuesTask extends AbstractBackgroundableTask {

    public RefreshIssuesTask(@NotNull Project project) {
        super(project, "Updating Issues from Server");
    }

    @Override
    public void run(@NotNull ProgressIndicator indicator) {
        JiraServerManager jiraServerManager = myProject.getComponent(JiraServerManager.class);
        Optional<JiraServer> jiraServer = jiraServerManager.getConfiguredJiraServer();
        if(!jiraServer.isPresent()) {
            throw new JiraServerConfigurationNotFoundException("Cannot connect to server for updating issues");
        }

        JQLSearcherManager jqlSearcherManager = myProject.getComponent(JQLSearcherManager.class);
        JQLSearcher searcher = jqlSearcherManager.getDeafaultJQLSearcher();

        List<JiraIssue> issues = jiraServer.get().getIssues(searcher.getJql());
        JiraIssueUpdater.getInstance().update(issues);
    }

    @Override
    public void onSuccess() {
        showNotification("Jira", "Issues are now up to date");
    }

}
