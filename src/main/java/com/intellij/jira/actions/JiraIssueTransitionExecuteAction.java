package com.intellij.jira.actions;

import com.intellij.jira.tasks.TransitIssueTask;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;

import static java.util.Objects.nonNull;

public class JiraIssueTransitionExecuteAction extends JiraIssueAction {

    private String transitionId;
    private String issueId;

    public JiraIssueTransitionExecuteAction(String transitionName, String transitionId, String issueId) {
        super(ActionProperties.of(transitionName));
        this.transitionId = transitionId;
        this.issueId = issueId;
    }

    @Override
    public void actionPerformed(AnActionEvent e) {
        Project project = e.getProject();
        if(nonNull(project)) {
            new TransitIssueTask(project, issueId, transitionId).queue();
        }
    }
}
