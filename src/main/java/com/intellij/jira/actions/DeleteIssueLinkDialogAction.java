package com.intellij.jira.actions;

import com.intellij.icons.AllIcons;
import com.intellij.jira.ui.dialog.DeleteIssueLinkDialog;
import com.intellij.jira.util.JiraIssueLinkFactory;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

public class DeleteIssueLinkDialogAction extends JiraIssueAction {
    private static final ActionProperties properties = ActionProperties.of("Delete link",  AllIcons.General.Remove);

    private String issueKey;
    private JiraIssueLinkFactory issueLink;

    public DeleteIssueLinkDialogAction(String issueKey, JiraIssueLinkFactory issueLink) {
        super(properties);
        this.issueKey = issueKey;
        this.issueLink = issueLink;
    }


    @Override
    public void actionPerformed(AnActionEvent e) {
        Project project = e.getProject();
        if(isNull(project)){
            return;
        }

        DeleteIssueLinkDialog dialog = new DeleteIssueLinkDialog(project, issueKey, issueLink.create().getId());
        dialog.show();
    }

    @Override
    public void update(AnActionEvent e) {
       e.getPresentation().setEnabled(nonNull(issueLink.create()));
    }


}
