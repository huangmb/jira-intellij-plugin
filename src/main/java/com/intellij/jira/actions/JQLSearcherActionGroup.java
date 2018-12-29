package com.intellij.jira.actions;

import com.intellij.icons.AllIcons;
import com.intellij.jira.server.JiraServerManager;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.DefaultActionGroup;
import com.intellij.openapi.actionSystem.Separator;
import com.intellij.openapi.project.Project;

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
            JiraServerManager manager = project.getComponent(JiraServerManager.class);
            if(manager.hasJiraServerConfigured()){
                event.getPresentation().setEnabled(true);
            }
            else{
                event.getPresentation().setEnabled(false);
            }
        }
    }

}
