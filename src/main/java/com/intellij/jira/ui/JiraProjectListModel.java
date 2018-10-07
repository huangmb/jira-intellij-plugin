package com.intellij.jira.ui;

import com.intellij.jira.rest.model.JiraProject;

import javax.swing.*;
import java.util.List;

public class JiraProjectListModel  extends AbstractListModel<JiraProject> {

    private final List<JiraProject> projects;

    public JiraProjectListModel(List<JiraProject> projects) {
        this.projects = projects;
    }

    @Override
    public int getSize() {
        return projects.size();
    }

    @Override
    public JiraProject getElementAt(int index) {
        return projects.get(index);
    }
}
