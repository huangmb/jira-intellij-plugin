package com.intellij.jira.rest.model.jql;

import com.intellij.jira.tasks.JiraServer;
import org.jetbrains.annotations.NotNull;

public class JiraServerJQLSearcher extends JQLSearcher {


    public JiraServerJQLSearcher(@NotNull JiraServer jiraServer) {
        super("Jira server", jiraServer.getDefaultSearchQuery());
    }


    @Override
    public boolean isEditable() {
        return false;
    }
}
