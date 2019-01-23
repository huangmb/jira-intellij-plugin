package com.intellij.jira.components;

import com.intellij.jira.events.JiraIssueEventListener;
import com.intellij.jira.rest.model.JiraIssue;
import com.intellij.openapi.components.ProjectComponent;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class JiraIssueUpdater implements ProjectComponent, Updater<JiraIssue>  {

    private List<JiraIssueEventListener> listeners;


    @Override
    public void projectOpened() {
        listeners = new ArrayList<>();
    }

    @Override
    public void projectClosed() {
        listeners.clear();
    }



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
