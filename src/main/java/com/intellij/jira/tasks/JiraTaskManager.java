package com.intellij.jira.tasks;

import com.intellij.openapi.components.AbstractProjectComponent;
import com.intellij.openapi.project.Project;
import com.intellij.tasks.TaskManager;
import com.intellij.tasks.TaskRepository;
import com.intellij.tasks.jira.JiraRepository;

import java.util.Arrays;
import java.util.Optional;

public class JiraTaskManager extends AbstractProjectComponent {

    private static final String JIRA = "JIRA";

    public JiraTaskManager(Project project) {
        super(project);
    }

    public Optional<JiraServer> getConfiguredJiraServer(){
        return Arrays.stream(getTaskManager().getAllRepositories())
                .filter(repo -> isJiraRepository(repo) && repo.isConfigured())
                .map(repo -> new JiraServer((JiraRepository) repo))
                .findFirst();

    }

    private TaskManager getTaskManager(){
        return myProject.getComponent(TaskManager.class);
    }

    private boolean isJiraRepository(TaskRepository taskRepository){
        return taskRepository.getRepositoryType().getName().equals(JIRA);
    }


}
