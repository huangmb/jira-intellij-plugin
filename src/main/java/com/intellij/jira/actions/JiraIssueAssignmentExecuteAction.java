package com.intellij.jira.actions;

import com.intellij.openapi.actionSystem.AnActionEvent;

public class JiraIssueAssignmentExecuteAction extends JiraIssueAction {

    private String username;
    private String issueKey;

    public JiraIssueAssignmentExecuteAction(String issueKey) {
        super(ActionProperties.of("Unassigned"));
        this.username = "-1";
        this.issueKey = issueKey;
    }

    public JiraIssueAssignmentExecuteAction(String username, String issueKey) {
        super(ActionProperties.of(username));
        this.username = username;
        this.issueKey = issueKey;
    }

    @Override
    public void actionPerformed(AnActionEvent e) {
        // TODO: task to assign
    }
}
