package com.intellij.jira.actions;

import com.intellij.jira.tasks.JiraServer;
import com.intellij.jira.tasks.JiraServerManager;
import com.intellij.jira.tasks.RefreshIssuesTask;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;

import java.util.Optional;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

public class JiraIssuesRefreshAction extends AnAction {


    @Override
    public void update(AnActionEvent event) {
        Project project = event.getProject();
        if (isNull(project)|| !project.isInitialized() || project.isDisposed()) {
            event.getPresentation().setEnabled(false);
        } else {
            JiraServerManager component = project.getComponent(JiraServerManager.class);
            Optional<JiraServer> jiraServer = component.getConfiguredJiraServer();
            if(jiraServer.isPresent()){
                event.getPresentation().setEnabled(true);
            }
            else{
                event.getPresentation().setEnabled(false);
            }
        }
    }

    @Override
    public void actionPerformed(AnActionEvent event) {
        Project project = event.getProject();
        if(nonNull(project)){
            new RefreshIssuesTask(project).queue();
        }

    }

}
