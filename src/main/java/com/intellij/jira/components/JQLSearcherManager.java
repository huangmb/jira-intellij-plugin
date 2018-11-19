package com.intellij.jira.components;

import com.intellij.jira.rest.model.jql.JQLSearcher;
import com.intellij.jira.rest.model.jql.JiraServerJQLSearcher;
import com.intellij.jira.tasks.JiraServer;
import com.intellij.jira.tasks.JiraServerManager;
import com.intellij.openapi.components.AbstractProjectComponent;
import com.intellij.openapi.project.Project;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static java.util.Objects.nonNull;

public class JQLSearcherManager extends AbstractProjectComponent {

    private Set<JQLSearcher> jqlSearchers = new HashSet<>();
    private JQLSearcher selectedJqlSearcher;

    protected JQLSearcherManager(Project project) {
        super(project);
    }


    public void add(JQLSearcher searcher){
        this.jqlSearchers.add(searcher);
        if(searcher.isDefault()){
            selectedJqlSearcher = searcher;
        }
    }

    public void update(JQLSearcher searcher){
        add(searcher);
    }


    public Set<JQLSearcher> getJQLSearchers() {
        return jqlSearchers;
    }

    public JQLSearcher getDeafaultJQLSearcher(){

        if(nonNull(selectedJqlSearcher)){
            return selectedJqlSearcher;
        }

        // Find de default jql in configured server jira
        JiraServerManager jiraServerManager = myProject.getComponent(JiraServerManager.class);
        Optional<JiraServer> configuredJiraServer = jiraServerManager.getConfiguredJiraServer();
        if(configuredJiraServer.isPresent()){
            return new JiraServerJQLSearcher(configuredJiraServer.get());
        }

        return new JQLSearcher("assignee = currentUser()");
    }

}
