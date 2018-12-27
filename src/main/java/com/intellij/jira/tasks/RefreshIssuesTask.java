package com.intellij.jira.tasks;

import com.intellij.jira.components.JQLSearcherManager;
import com.intellij.jira.components.JiraIssueUpdater;
import com.intellij.jira.rest.model.JiraIssue;
import com.intellij.jira.rest.model.jql.JQLSearcher;
import com.intellij.jira.server.JiraServerManager2;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.project.Project;
import com.intellij.tasks.jira.JiraRepository;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class RefreshIssuesTask extends AbstractBackgroundableTask {

    public RefreshIssuesTask(@NotNull Project project) {
        super(project, "Updating Issues from Server");
    }

    @Override
    public void run(@NotNull ProgressIndicator indicator) {

        JiraRepository jiraRestApi = myProject.getComponent(JiraServerManager2.class).getJiraRestApi();
        JiraServer jiraServer = new JiraServer(jiraRestApi);

        JQLSearcherManager jqlSearcherManager = myProject.getComponent(JQLSearcherManager.class);
        JQLSearcher searcher = jqlSearcherManager.getDeafaultJQLSearcher();

        List<JiraIssue> issues = jiraServer.getIssues(searcher.getJql());
        JiraIssueUpdater.getInstance().update(issues);
    }

    @Override
    public void onSuccess() {
        showNotification("Jira", "Issues are now up to date");
    }

}
