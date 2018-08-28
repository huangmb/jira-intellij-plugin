package com.intellij.jira.tasks;

import com.intellij.jira.rest.JiraRestClient;
import com.intellij.jira.rest.model.JiraIssue;
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


    public List<JiraIssue> getIssues() {
        try {
            return this.jiraRestClient.findIssues();
        } catch (Exception e) {
            log.error("No issues found");
            return ContainerUtil.emptyList();
        }
    }


}
