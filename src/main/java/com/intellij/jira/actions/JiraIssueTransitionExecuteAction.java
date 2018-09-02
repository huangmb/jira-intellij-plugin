package com.intellij.jira.actions;

import com.intellij.jira.notifications.JiraNotificationComponent;
import com.intellij.notification.Notifications;
import com.intellij.openapi.actionSystem.AnActionEvent;

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
        // TODO: execute and update table and panel
        String content = String.format("Executed transition %s in issue %s", transitionId, issueId);
        Notifications.Bus.notify(JiraNotificationComponent.getInstance().createNotification("JIRA", content));
    }
}
