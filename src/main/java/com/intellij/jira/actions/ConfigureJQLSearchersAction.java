package com.intellij.jira.actions;

import com.intellij.icons.AllIcons;
import com.intellij.jira.ui.dialog.ConfigureJQLSearchersDialog;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;

import static java.util.Objects.nonNull;

public class ConfigureJQLSearchersAction extends AnAction {

    public ConfigureJQLSearchersAction() {
        super("Configure...", null, AllIcons.General.Settings);
    }

    @Override
    public void actionPerformed(AnActionEvent e) {
        Project project = e.getProject();
        if(nonNull(project)){
            ConfigureJQLSearchersDialog dialog = new ConfigureJQLSearchersDialog(project);
            dialog.show();
        }

    }

}
