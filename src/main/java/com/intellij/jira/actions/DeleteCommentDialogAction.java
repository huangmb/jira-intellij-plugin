package com.intellij.jira.actions;

import com.intellij.icons.AllIcons;
import com.intellij.jira.ui.dialog.DeleteCommentDialog;
import com.intellij.jira.util.JiraIssueCommentFactory;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

public class DeleteCommentDialogAction extends JiraIssueAction {

    private static final ActionProperties properties = ActionProperties.of("Delete comment",  AllIcons.General.Remove);

    private String issueKey;
    private JiraIssueCommentFactory comment;

    public DeleteCommentDialogAction(String issueKey, JiraIssueCommentFactory factory) {
        super(properties);
        this.issueKey = issueKey;
        this.comment = factory;
    }

    @Override
    public void actionPerformed(AnActionEvent e) {
        Project project = e.getProject();
        if(isNull(project)){
            return;
        }

        DeleteCommentDialog commentDialog = new DeleteCommentDialog(project, issueKey, comment.create().getId());
        commentDialog.show();
    }

    @Override
    public void update(AnActionEvent e) {
        e.getPresentation().setEnabled(nonNull(comment.create()));
    }
}
