package com.intellij.jira.server;

import com.intellij.configurationStore.XmlSerializer;
import com.intellij.jira.util.SimpleSelectableList;
import com.intellij.openapi.components.*;
import com.intellij.tasks.jira.JiraRepository;
import com.intellij.tasks.jira.JiraRepositoryType;
import com.intellij.util.xmlb.XmlSerializationException;
import com.intellij.util.xmlb.XmlSerializerUtil;
import com.intellij.util.xmlb.annotations.Tag;
import org.jdom.Element;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

import static java.util.Objects.nonNull;

@State(name = "JiraServerManager2", storages = @Storage(StoragePathMacros.WORKSPACE_FILE))
public class JiraServerManager2 implements ProjectComponent, PersistentStateComponent<JiraServerManager2.Config> {


    private SimpleSelectableList<JiraServer2> myJiraServers = new SimpleSelectableList<>();
    private Config myConfig = new Config();

    @Nullable
    @Override
    public Config getState() {
        myConfig.selected = myJiraServers.getSelectedItemIndex();
        myConfig.servers = XmlSerializer.serialize(getAllJiraServersAsArray());
        return myConfig;
    }

    @Override
    public void loadState(@NotNull Config config) {
        XmlSerializerUtil.copyBean(config, myConfig);

        myJiraServers.clear();
        Element element = config.servers;
        List<JiraServer2> servers = loadServers(element);
        myJiraServers.addAll(servers);

        myJiraServers.selectItem(config.selected);
    }

    private List<JiraServer2> loadServers(Element element) {
        List<JiraServer2> servers = new ArrayList<>();
        if(nonNull(element)){
            for(Element o : element.getChildren()){
                try{
                    JiraServer2 server = XmlSerializer.deserialize(o, JiraServer2.class);
                    servers.add(server);
                }catch (XmlSerializationException e) {
                    //LOG.error(e.getMessage(), e);
                }
            }
        }

        return servers;
    }

    public SimpleSelectableList<JiraServer2> getJiraServers() {
        return myJiraServers;
    }

    private JiraServer2[] getAllJiraServersAsArray(){
        return myJiraServers.getItems().toArray(new JiraServer2[0]);
    }

    public int getSelectedJiraServerIndex(){
        return myJiraServers.getSelectedItemIndex();
    }

    public JiraServer2 getCurrentJiraServer(){
        return myJiraServers.getItems().get(getSelectedJiraServerIndex());
    }

    public void setJiraServers(SimpleSelectableList<JiraServer2> servers) {
        this.myJiraServers = servers;
    }

    public JiraRepository getJiraRestApi(){
        return convertFrom(getCurrentJiraServer());
    }

    private JiraRepository convertFrom(JiraServer2 jiraServer){
        JiraRepository repository = new JiraRepositoryType().createRepository();
        repository.setUrl(jiraServer.getUrl());
        repository.setUsername(jiraServer.getUsername());
        repository.setPassword(jiraServer.getPassword());

        return repository;
    }



    public static class Config{

        @Tag("selected")
        public int selected;

        @Tag("servers")
        public Element servers;

    }

}
