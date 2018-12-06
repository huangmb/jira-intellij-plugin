package com.intellij.jira.actions;

import com.intellij.icons.AllIcons;
import com.intellij.jira.ui.dialog.NewJQLSearcherDialog;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;

import static java.util.Objects.nonNull;

public class AddJQLSearcherAction extends AnAction {

    public AddJQLSearcherAction() {
        super("New JQL searcher", null, AllIcons.General.Add);
    }

    @Override
    public void actionPerformed(AnActionEvent e) {
        Project project = e.getProject();
        if(nonNull(project)){
            NewJQLSearcherDialog dialog = new NewJQLSearcherDialog(project);
            dialog.show();
        }

    }
}
