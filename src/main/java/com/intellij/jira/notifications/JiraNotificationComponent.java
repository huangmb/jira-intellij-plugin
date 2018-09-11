package com.intellij.jira.notifications;

import com.intellij.notification.Notification;
import com.intellij.notification.NotificationGroup;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.ApplicationComponent;

import static com.intellij.notification.NotificationDisplayType.BALLOON;
import static com.intellij.notification.NotificationType.ERROR;
import static com.intellij.notification.NotificationType.INFORMATION;

public class JiraNotificationComponent implements ApplicationComponent {

    private static final NotificationGroup JIRA_NOTIFICATION_GROUP = new NotificationGroup("Jira Notifications", BALLOON, true);


    public static JiraNotificationComponent getInstance(){
        return ApplicationManager.getApplication().getComponent(JiraNotificationComponent.class);
    }

    public Notification createNotification(String title, String content){
        return JIRA_NOTIFICATION_GROUP.createNotification(title, null, content, INFORMATION);
    }

    public Notification createNotificationError(String title, String content){
        return JIRA_NOTIFICATION_GROUP.createNotification(title, null, content, ERROR);
    }



}
