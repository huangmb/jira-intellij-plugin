package com.intellij.jira.components;

import com.intellij.jira.events.JiraIssueEventListener;
import com.intellij.jira.rest.model.JiraIssue;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.ApplicationComponent;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class JiraIssueUpdater implements ApplicationComponent, Updater<JiraIssue>  {

    private List<JiraIssueEventListener> listeners;

    protected JiraIssueUpdater() {
        listeners = new ArrayList<>();
    }

    public static JiraIssueUpdater getInstance() {
        return ApplicationManager.getApplication().getComponent(JiraIssueUpdater.class);
    }

    @Override
    public void disposeComponent() { listeners.clear();}



    public void addListener(JiraIssueEventListener listener){
        listeners.add(listener);
    }

    @Override
    public void update(List<JiraIssue> issues) {
        listeners.forEach(j ->
                SwingUtilities.invokeLater(() -> j.update(issues)));
    }

    @Override
    public void update(JiraIssue issue) {
        listeners.forEach(j ->
            SwingUtilities.invokeLater(() -> j.update(issue)));
    }

}
