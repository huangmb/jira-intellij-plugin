package com.intellij.jira.tasks;

import com.intellij.concurrency.JobScheduler;
import com.intellij.openapi.components.AbstractProjectComponent;
import com.intellij.openapi.project.Project;
import com.intellij.tasks.TaskManager;
import com.intellij.tasks.TaskRepository;
import com.intellij.tasks.jira.JiraRepository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class JiraTaskManager extends AbstractProjectComponent {
    private static final String JIRA = "JIRA";

    private List<Runnable> listeners = new ArrayList();
    private Long currentJiraServerHash = 0L;
    private ScheduledFuture checkJiraServerChangesJob;

    public JiraTaskManager(Project project) {
        super(project);
    }


    @Override
    public void projectOpened() {
        syncJiraIssuesIfProceed();
        checkJiraServerChangesJob = JobScheduler.getScheduler().scheduleWithFixedDelay(() -> {
                if(!listeners.isEmpty()){
                    syncJiraIssuesIfProceed();
                }
        }, 3, 3, TimeUnit.SECONDS);
    }

    @Override
    public void projectClosed() {
        listeners.clear();
        checkJiraServerChangesJob.cancel(false);
    }

    private synchronized void syncJiraIssuesIfProceed(){
        long jiraServerHash = getConfiguredJiraRepository().map(repo -> (long) repo.hashCode()).orElse(0L);

        if (currentJiraServerHash != jiraServerHash) {
            currentJiraServerHash = jiraServerHash;
            syncJiraIssues();
        }
    }

    public synchronized void syncJiraIssues(){
        listeners.forEach(Runnable::run);
    }


    private Optional<TaskRepository> getConfiguredJiraRepository(){
        return Arrays.stream(getTaskManager().getAllRepositories())
                .filter(repo -> isJiraRepository(repo) && repo.isConfigured())
                .findFirst();
    }

    public Optional<JiraServer> getConfiguredJiraServer(){
        return getConfiguredJiraRepository().map(repo -> new JiraServer((JiraRepository) repo));
    }


    public void addConfigurationServerChangedListener(Runnable doRun){
        listeners.add(doRun);
    }

    private TaskManager getTaskManager(){
        return myProject.getComponent(TaskManager.class);
    }

    private boolean isJiraRepository(TaskRepository taskRepository){
        return taskRepository.getRepositoryType().getName().equals(JIRA);
    }


}
