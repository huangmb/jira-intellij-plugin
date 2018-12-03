package com.intellij.jira.components;

import com.intellij.jira.events.JiraIssueEventListener;
import com.intellij.jira.rest.model.jql.JQLSearcher;
import com.intellij.jira.rest.model.jql.JiraServerJQLSearcher;
import com.intellij.jira.tasks.JiraServer;
import com.intellij.jira.tasks.JiraServerManager;
import com.intellij.jira.ui.panels.JiraJQLSearcherPanel;
import com.intellij.openapi.components.ProjectComponent;
import com.intellij.openapi.project.Project;

import java.util.*;

import static java.util.Objects.nonNull;

public class JQLSearcherManager implements ProjectComponent {

    private final Project myProject;

    private Map<String, JQLSearcher> jqlSearchers = new HashMap<>();
    private JQLSearcher defaultJqlSearcher;

    protected JQLSearcherManager(Project project) {
        this.myProject = project;
    }


    public void add(JQLSearcher searcher){
        checkDefault(searcher);
        this.jqlSearchers.putIfAbsent(searcher.getAlias(), searcher);
    }

    public void update(String oldAliasSearcher, JQLSearcher updatedSearcher){
        checkDefault(updatedSearcher);
        this.jqlSearchers.put(oldAliasSearcher, updatedSearcher);
    }

    public boolean alreadyExistJQLSearcherWithAlias(String alias){
        return nonNull(jqlSearchers.get(alias));
    }

    public List<JQLSearcher> getJQLSearchers() {
        return new ArrayList<>(jqlSearchers.values());
    }

    public JQLSearcher getDeafaultJQLSearcher(){
        if(nonNull(defaultJqlSearcher)){
            return defaultJqlSearcher;
        }

        // Find de default jql in configured server jira
        JiraServerManager jiraServerManager = myProject.getComponent(JiraServerManager.class);
        Optional<JiraServer> configuredJiraServer = jiraServerManager.getConfiguredJiraServer();
        if(configuredJiraServer.isPresent()){
            return new JiraServerJQLSearcher(configuredJiraServer.get());
        }

        return new JQLSearcher("assignee = currentUser()");
    }


    private void checkDefault(JQLSearcher searcher){
        if(searcher.isDefault()){
            jqlSearchers.values().forEach(s -> s.setSelected(false));
            defaultJqlSearcher = searcher;
        }
    }

}
