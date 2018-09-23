package com.intellij.jira.tasks;

import com.intellij.jira.exceptions.JiraServerConfigurationNotFoundException;
import com.intellij.jira.notifications.JiraNotificationComponent;
import com.intellij.notification.Notifications;
import com.intellij.openapi.progress.Task;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

import static java.util.Objects.nonNull;

public abstract class AbstractBackgroundableTask extends Task.Backgroundable {

    public AbstractBackgroundableTask(@NotNull Project project, @NotNull String title) {
        super(project, title, false, ALWAYS_BACKGROUND);
    }


    @NotNull
    public JiraServer getJiraServer() throws JiraServerConfigurationNotFoundException{
        JiraServerManager jiraServerManager = myProject.getComponent(JiraServerManager.class);
        Optional<JiraServer> jiraServer = jiraServerManager.getConfiguredJiraServer();
        if(!jiraServer.isPresent()) {
            throw new JiraServerConfigurationNotFoundException();
        }

        return jiraServer.get();
    }

    public void showNotification(String title, String content){
        Notifications.Bus.notify(JiraNotificationComponent.getInstance().createNotification(title, content));
    }


    @Override
    public void onThrowable(@NotNull Throwable error) {
        String content = nonNull(error.getCause()) ? error.getCause().getMessage() : "";
        Notifications.Bus.notify(JiraNotificationComponent.getInstance().createNotificationError(error.getMessage(), content));
    }

}
