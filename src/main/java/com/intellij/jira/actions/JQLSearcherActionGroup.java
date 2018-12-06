package com.intellij.jira.actions;

import com.intellij.icons.AllIcons;
import com.intellij.jira.tasks.JiraServer;
import com.intellij.jira.tasks.JiraServerManager;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.DefaultActionGroup;
import com.intellij.openapi.actionSystem.Separator;
import com.intellij.openapi.project.Project;

import java.util.Optional;

import static java.util.Objects.isNull;

public class JQLSearcherActionGroup extends DefaultActionGroup {

    public JQLSearcherActionGroup() {
        super("JQL Searchers", true);
        getTemplatePresentation().setIcon(AllIcons.Vcs.Changelist);
        add(new AddJQLSearcherAction());
        add(new EditJQLSearcherAction());
        add(new Separator());
        add(new ConfigureJQLSearchersAction());
    }

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

}
