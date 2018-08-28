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
    private Long configurationHash = 0L;
    private ScheduledFuture job;

    public JiraTaskManager(Project project) {
        super(project);
    }


    @Override
    public void projectOpened() {
        syncTaskManager();
        job = JobScheduler.getScheduler().scheduleWithFixedDelay(() -> {
                if(!listeners.isEmpty()){
                    syncTaskManager();
                }
        }, 3, 3, TimeUnit.SECONDS);
    }

    public synchronized void syncTaskManager(){
        long newHash = Arrays.stream(getTaskManager().getAllRepositories())
                .filter(r -> isJiraRepository(r))
                .mapToLong(repo -> repo.hashCode())
                .reduce(0L, (sum, value) -> sum + value);

        if (configurationHash != newHash) {
            configurationHash = newHash;
            listeners.forEach(r -> r.run());
        }
    }


    @Override
    public void projectClosed() {
        listeners.clear();
        job.cancel(false);
    }

    public Optional<JiraServer> getConfiguredJiraServer(){
        return Arrays.stream(getTaskManager().getAllRepositories())
                .filter(repo -> isJiraRepository(repo) && repo.isConfigured())
                .map(repo -> new JiraServer((JiraRepository) repo))
                .findFirst();

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
