package com.intellij.jira.tasks;

import com.intellij.tasks.Task;
import com.intellij.tasks.jira.JiraRepository;
import org.fest.util.Lists;

import java.util.List;

public class JiraServer {

    private JiraRepository jiraRepository;

    public JiraServer(JiraRepository jiraRepository) {
        this.jiraRepository = jiraRepository;
    }


    public List<Task> getIssues() {
        try {
            return Lists.newArrayList(jiraRepository.getIssues(null, 0, 5, true));
        } catch (Exception e) {
            return Lists.emptyList();
        }
    }


}
