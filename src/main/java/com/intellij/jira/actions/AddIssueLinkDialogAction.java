package com.intellij.jira.actions;

import com.intellij.icons.AllIcons;
import com.intellij.jira.rest.model.JiraIssue;
import com.intellij.jira.rest.model.JiraIssueLinkType;
import com.intellij.jira.server.JiraRestApi;
import com.intellij.jira.server.JiraServerManager;
import com.intellij.jira.ui.dialog.AddIssueLinkDialog;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;

import java.util.List;

import static java.util.Objects.nonNull;
import static java.util.stream.Collectors.toList;

public class AddIssueLinkDialogAction extends JiraIssueAction {
    private static final ActionProperties properties = ActionProperties.of("New Issue Link",  AllIcons.General.Add);

    private String issueKey;

    public AddIssueLinkDialogAction(String issueKey) {
        super(properties);
        this.issueKey = issueKey;
    }


    @Override
    public void actionPerformed(AnActionEvent e) {
        Project project = e.getProject();
        if(nonNull(project)){
            JiraServerManager manager = project.getComponent(JiraServerManager.class);
            JiraRestApi jiraRestApi = manager.getJiraRestApi();
            if(nonNull(jiraRestApi)){
                List<JiraIssueLinkType> issueLinkTypes = jiraRestApi.getIssueLinkTypes();
                List<String> issues = jiraRestApi.getIssues().stream().map(JiraIssue::getKey).collect(toList());
                issues.remove(issueKey);

                openIssueLinkDialog(project, issueLinkTypes, issues);
            }
        }
    }

    public void openIssueLinkDialog(Project project, List<JiraIssueLinkType> issueLinkTypes, List<String> issues) {
        AddIssueLinkDialog dialog = new AddIssueLinkDialog(project, issueLinkTypes, issues, issueKey);
        dialog.show();
    }


}
