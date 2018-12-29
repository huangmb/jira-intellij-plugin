package com.intellij.jira.tasks;

import com.intellij.jira.exceptions.JiraServerConfigurationNotFoundException;
import com.intellij.jira.components.JiraNotificationManager;
import com.intellij.jira.server.JiraRestApi;
import com.intellij.jira.server.JiraServerManager;
import com.intellij.notification.Notifications;
import com.intellij.openapi.progress.Task;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

public abstract class AbstractBackgroundableTask extends Task.Backgroundable {

    public AbstractBackgroundableTask(@NotNull Project project, @NotNull String title) {
        super(project, title, false, ALWAYS_BACKGROUND);
    }


    @NotNull
    public JiraRestApi getJiraRestApi() throws JiraServerConfigurationNotFoundException{
        JiraServerManager jiraServerManager = myProject.getComponent(JiraServerManager.class);
        JiraRestApi jiraRestApi = jiraServerManager.getJiraRestApi();
        if(isNull(jiraRestApi)) {
            throw new JiraServerConfigurationNotFoundException();
        }

        return jiraRestApi;
    }

    public void showNotification(String title, String content){
        Notifications.Bus.notify(JiraNotificationManager.getInstance().createNotification(title, content));
    }


    @Override
    public void onThrowable(@NotNull Throwable error) {
        String content = nonNull(error.getCause()) ? error.getCause().getMessage() : "";
        Notifications.Bus.notify(JiraNotificationManager.getInstance().createNotificationError(error.getMessage(), content));
    }

}
