package com.intellij.jira.actions;

import com.intellij.icons.AllIcons;
import com.intellij.jira.rest.model.JiraUser;
import com.intellij.jira.tasks.JiraServer;
import com.intellij.jira.tasks.JiraServerManager;
import com.intellij.jira.ui.popup.JiraIssueAssignableUsersPopup;
import com.intellij.jira.util.JiraIssueFactory;
import com.intellij.openapi.actionSystem.ActionGroup;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;

import java.util.List;
import java.util.Optional;

import static java.util.Objects.nonNull;

public class JiraIssueAssigneePopupAction extends JiraIssueAction {
    private static final ActionProperties properties = ActionProperties.of("Assign",  AllIcons.Modules.Types.UserDefined);

    private JiraIssueFactory issueFactory;

    public JiraIssueAssigneePopupAction(JiraIssueFactory factory) {
        super(properties);
        this.issueFactory = factory;
    }

    @Override
    public void actionPerformed(AnActionEvent e) {
        Project project = e.getProject();
        if(nonNull(project)) {
            JiraServerManager jiraServerManager = project.getComponent(JiraServerManager.class);
            Optional<JiraServer> jiraServer = jiraServerManager.getConfiguredJiraServer();
            if(jiraServer.isPresent()){
                List<JiraUser> assignableUsers = jiraServer.get().getAssignableUsers(issueFactory.create().getKey());
                JiraIssueAssignableUsersPopup popup = new JiraIssueAssignableUsersPopup(createActionGroup(assignableUsers), project);
                popup.showInCenterOf(getComponent());
            }
        }
    }

    @Override
    public void update(AnActionEvent e) {
        e.getPresentation().setEnabled(nonNull(issueFactory.create()));
    }

    private ActionGroup createActionGroup(List<JiraUser> assignableUsers){
        JiraIssueActionGroup group = new JiraIssueActionGroup(getComponent());
        assignableUsers.forEach(u -> group.add(new JiraIssueAssignmentExecuteAction(u.getKey(), issueFactory.create().getKey())));
        group.add(new JiraIssueAssignmentExecuteAction(issueFactory.create().getKey())); // Unassigned action
        return group;
    }

}
