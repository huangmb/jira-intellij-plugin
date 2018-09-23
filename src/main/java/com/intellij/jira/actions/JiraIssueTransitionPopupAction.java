package com.intellij.jira.actions;

import com.intellij.icons.AllIcons;
import com.intellij.jira.rest.model.JiraIssue;
import com.intellij.jira.rest.model.JiraIssueTransition;
import com.intellij.jira.tasks.JiraServer;
import com.intellij.jira.tasks.JiraServerManager;
import com.intellij.jira.ui.popup.JiraIssueTransitionsPopup;
import com.intellij.jira.util.JiraIssueFactory;
import com.intellij.openapi.actionSystem.ActionGroup;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;

import java.util.List;
import java.util.Optional;

import static java.util.Objects.nonNull;

public class JiraIssueTransitionPopupAction extends JiraIssueAction {
    private static final ActionProperties properties = ActionProperties.of("Transit",  AllIcons.Actions.Forward);

    private JiraIssueFactory issueFactory;


    public JiraIssueTransitionPopupAction(JiraIssueFactory factory) {
        super(properties);
        this.issueFactory = factory;
    }

    @Override
    public void actionPerformed(AnActionEvent e) {
        Project project = e.getProject();
        if(nonNull(project)) {
            JiraServerManager component = project.getComponent(JiraServerManager.class);
            Optional<JiraServer> jiraServer = component.getConfiguredJiraServer();
            if(jiraServer.isPresent()){
                JiraIssue issue = issueFactory.create();
                List<JiraIssueTransition> transitions = jiraServer.get().getTransitions(issue.getId());
                JiraIssueTransitionsPopup popup = new JiraIssueTransitionsPopup(createActionGroup(transitions, issue), project);
                popup.showInCenterOf(getComponent());

            }
        }

    }

    @Override
    public void update(AnActionEvent e) {
        e.getPresentation().setEnabled(nonNull(issueFactory.create()));
    }


    private ActionGroup createActionGroup(List<JiraIssueTransition> transitions, JiraIssue issue){
        JiraIssueActionGroup group = new JiraIssueActionGroup(getComponent());
        transitions.forEach(t -> group.add(new JiraIssueTransitionExecuteAction(t.getName(), t.getId(), issue.getId())));

        return group;
    }


}
