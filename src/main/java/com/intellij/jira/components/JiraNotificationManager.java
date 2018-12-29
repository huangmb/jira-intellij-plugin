package com.intellij.jira.components;

import com.intellij.notification.Notification;
import com.intellij.notification.NotificationGroup;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.ApplicationComponent;

import static com.intellij.notification.NotificationDisplayType.*;
import static com.intellij.notification.NotificationType.ERROR;
import static com.intellij.notification.NotificationType.INFORMATION;

public class JiraNotificationManager implements ApplicationComponent {

    private static final NotificationGroup BALLON_NOTIFICATION_GROUP = new NotificationGroup("Jira Notifications", BALLOON, true);
    private static final NotificationGroup NONE_NOTIFICATION_GROUP = new NotificationGroup("Jira Notifications", NONE, true);


    public static JiraNotificationManager getInstance(){
        return ApplicationManager.getApplication().getComponent(JiraNotificationManager.class);
    }

    public Notification createNotification(String title, String content){
        return BALLON_NOTIFICATION_GROUP.createNotification(title, null, content, INFORMATION);
    }

    public Notification createNotificationError(String title, String content){
        return BALLON_NOTIFICATION_GROUP.createNotification(title, null, content, ERROR);
    }

    public Notification createSilentNotification(String title, String content){
        return BALLON_NOTIFICATION_GROUP.createNotification(title, null, content, INFORMATION);
    }

}
