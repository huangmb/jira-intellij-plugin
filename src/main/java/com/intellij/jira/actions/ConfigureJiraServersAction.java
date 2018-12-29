package com.intellij.jira.actions;

import com.intellij.icons.AllIcons;
import com.intellij.jira.ui.dialog.ConfigureJiraServersDialog;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;

import static java.util.Objects.isNull;

public class ConfigureJiraServersAction extends JiraIssueAction {

    private static final ActionProperties properties = ActionProperties.of("Configure Servers...",  AllIcons.General.Settings);

    public ConfigureJiraServersAction() {
        super(properties);
    }

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        Project project = e.getProject();
        if(isNull(project)){
            return;
        }

        ConfigureJiraServersDialog dlg = new ConfigureJiraServersDialog(project);
        dlg.show();
    }




}
