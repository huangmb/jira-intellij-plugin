package com.intellij.jira.components;

import com.intellij.jira.rest.model.jql.JQLSearcher;
import com.intellij.jira.rest.model.jql.JiraServerJQLSearcher;
import com.intellij.jira.tasks.JiraServer;
import com.intellij.jira.tasks.JiraServerManager;
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
        notifyViews();
    }


    public void addJiraServerJqlSearcher(){
        JiraServerManager jiraServerManager = myProject.getComponent(JiraServerManager.class);
        Optional<JiraServer> configuredJiraServer = jiraServerManager.getConfiguredJiraServer();
        if(configuredJiraServer.isPresent()){
            JQLSearcher jqlSearcher = new JiraServerJQLSearcher(configuredJiraServer.get());
            jqlSearchers.put(jqlSearcher.getAlias(), jqlSearcher);
            defaultJqlSearcher = jqlSearcher;
        }

    }

    public void remove(JQLSearcher selectedSearcher) {
        this.jqlSearchers.remove(selectedSearcher.getAlias());
        notifyViews();
    }

    public void update(String oldAliasSearcher, JQLSearcher updatedSearcher){
        if(jqlSearchers.containsKey(oldAliasSearcher)){
            checkDefault(updatedSearcher);
            if(oldAliasSearcher.equals(updatedSearcher.getAlias())){
                this.jqlSearchers.put(oldAliasSearcher, updatedSearcher);
            }else{
                this.jqlSearchers.remove(oldAliasSearcher);
                this.jqlSearchers.put(updatedSearcher.getAlias(), updatedSearcher);
            }

            notifyViews();
        }
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

        return new JQLSearcher("Assigned to me","assignee = currentUser()");
    }


    private void checkDefault(JQLSearcher searcher){
        if(searcher.isDefault()){
            jqlSearchers.values().forEach(s -> s.setSelected(false));
            defaultJqlSearcher = searcher;
        }
    }


    private void notifyViews(){
        getJqlSearcherObserver().update(getJQLSearchers());
    }

    private JQLSearcherObserver getJqlSearcherObserver(){
        return myProject.getComponent(JQLSearcherObserver.class);
    }

}
