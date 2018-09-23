package com.intellij.jira.actions;

import com.intellij.icons.AllIcons;
import com.intellij.jira.ui.dialog.AddCommentDialog;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;

import static java.util.Objects.isNull;

public class AddCommentDialogAction extends JiraIssueAction{
    private static final ActionProperties properties = ActionProperties.of("Add a comment",  AllIcons.General.Add);

    private String issueKey;

    public AddCommentDialogAction(String issueKey) {
        super(properties);
        this.issueKey = issueKey;
    }

    @Override
    public void actionPerformed(AnActionEvent e) {
        Project project = e.getProject();
        if(isNull(project)){
            return;
        }

        AddCommentDialog commentDialog = new AddCommentDialog(project, issueKey);
        commentDialog.show();
    }
}
