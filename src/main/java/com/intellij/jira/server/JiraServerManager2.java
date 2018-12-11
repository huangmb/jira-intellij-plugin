package com.intellij.jira.server;

import com.intellij.configurationStore.XmlSerializer;
import com.intellij.openapi.components.*;
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


    private final List<JiraServer2> myJiraServers = new ArrayList<>();
    private int mySelectedJiraServer = -1;
    private Config myConfig = new Config();

    @Nullable
    @Override
    public Config getState() {
        myConfig.selected = mySelectedJiraServer;
        myConfig.servers = XmlSerializer.serialize(getAllJiraServers());
        return myConfig;
    }

    @Override
    public void loadState(@NotNull Config config) {
        XmlSerializerUtil.copyBean(config, myConfig);

        myJiraServers.clear();
        Element element = config.servers;
        List<JiraServer2> servers = loadServers(element);
        myJiraServers.addAll(servers);

        mySelectedJiraServer = config.selected;
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

    public List<JiraServer2> getAllJiraServers() {
        return myJiraServers;
    }

    public int getSelectedJiraServer(){
        if(getAllJiraServers().isEmpty()){
            return -1;
        }

        return mySelectedJiraServer;
    }

    public void add(JiraServer2 jiraServer, boolean isSetDefault) {
        // First time
        if(myJiraServers.isEmpty()){
            myJiraServers.add(jiraServer);
            mySelectedJiraServer = 0;
            // update issues
        }else{
            myJiraServers.add(jiraServer);
            if(isSetDefault){
                mySelectedJiraServer = myJiraServers.size() - 1;
                // update issues
            }
        }

    }

    public void update(JiraServer2 jiraServer, int index, boolean isSetDefault) {
        if(index >= 0){
            myJiraServers.remove(index);
            myJiraServers.add(index, jiraServer);
            if(isSetDefault){
                mySelectedJiraServer = index;
                // update issues
            }
        }

    }

    public void remove(int index) {
        if(index >= 0){
            myJiraServers.remove(index);
            if(index < mySelectedJiraServer){
                mySelectedJiraServer = index;
            }

            if(mySelectedJiraServer > myJiraServers.size() - 1){
                mySelectedJiraServer--;
            }

            //update issues
        }

    }

    public static class Config{

        @Tag("selected")
        public int selected;

        @Tag("servers")
        public Element servers;

    }

}
