package com.intellij.jira.tasks;

import com.intellij.jira.rest.JiraRestClient;
import com.intellij.jira.rest.model.JiraIssue;
import com.intellij.jira.rest.model.JiraIssueTransition;
import com.intellij.jira.util.JiraIssueTransitionResult;
import com.intellij.tasks.jira.JiraRepository;
import com.intellij.util.containers.ContainerUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class JiraServer {

    private static final Logger log = LoggerFactory.getLogger(JiraServer.class);

    private JiraRestClient jiraRestClient;

    public JiraServer(JiraRepository jiraRepository) {
        this.jiraRestClient = new JiraRestClient(jiraRepository);
    }


    public JiraIssue getIssue(String issueId){
        try {
            return this.jiraRestClient.getIssue(issueId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        // TODO: poner Result
        return null;
    }


    public List<JiraIssue> getIssues() {
        try {
            return this.jiraRestClient.findIssues();
        } catch (Exception e) {
            log.error("No issues found");
            return ContainerUtil.emptyList();
        }
    }


    public List<JiraIssueTransition> getTransitions(String issueId){
        try {
            return jiraRestClient.getTransitions(issueId);
        } catch (Exception e) {
            log.error(String.format("No transitions was found for issue '%s'", issueId));
            return ContainerUtil.emptyList();
        }
    }


    public JiraIssueTransitionResult doTransition(String issueId, String transitionId){
        try {
            String response = jiraRestClient.doTransition(issueId, transitionId);
            return JiraIssueTransitionResult.create(response);
        } catch (Exception e) {
            log.error(String.format("Error executing transition '%s' in issue '%s'", transitionId, issueId));
            return JiraIssueTransitionResult.error();
        }
    }

}
