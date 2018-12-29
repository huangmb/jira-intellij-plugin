package com.intellij.jira.actions;

import com.intellij.icons.AllIcons;
import com.intellij.jira.rest.model.JiraIssue;
import com.intellij.jira.rest.model.JiraIssuePriority;
import com.intellij.jira.server.JiraServerManager;
import com.intellij.jira.server.JiraRestApi;
import com.intellij.jira.ui.popup.JiraIssuePrioritiesPopup;
import com.intellij.jira.util.JiraIssueFactory;
import com.intellij.openapi.actionSystem.ActionGroup;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;

import java.util.List;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

public class JiraIssuePrioritiesPopupAction extends JiraIssueAction {
    private static final ActionProperties properties = ActionProperties.of("Change priority",  AllIcons.Ide.UpDown);
    private JiraIssueFactory issue;

    public JiraIssuePrioritiesPopupAction(JiraIssueFactory factory) {
        super(properties);
        this.issue = factory;
    }

    @Override
    public void actionPerformed(AnActionEvent e) {
        Project project = e.getProject();
        if(isNull(project)){
            return;
        }

        JiraServerManager manager = project.getComponent(JiraServerManager.class);
        JiraRestApi jiraRestApi = manager.getJiraRestApi();
        if(isNull(jiraRestApi)){
           return;
        }

        JiraIssue issue = this.issue.create();
        List<JiraIssuePriority> priorities = jiraRestApi.getIssuePriorities();

        JiraIssuePrioritiesPopup popup = new JiraIssuePrioritiesPopup(createActionGroup(priorities, issue), project);
        popup.showInCenterOf(getComponent());

    }

    @Override
    public void update(AnActionEvent e) {
        e.getPresentation().setEnabled(nonNull(issue.create()));
    }

    private ActionGroup createActionGroup(List<JiraIssuePriority> priorities, JiraIssue issue) {
        JiraIssueActionGroup group = new JiraIssueActionGroup(getComponent());
        priorities.forEach(p -> group.add(new JiraIssueChangePriorityAction(p.getName(), issue.getKey())));

        return group;
    }
}
