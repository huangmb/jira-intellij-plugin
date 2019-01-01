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

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

@State(name = "JiraServerManager", storages = @Storage(StoragePathMacros.WORKSPACE_FILE))
public class JiraServerManager implements ProjectComponent, PersistentStateComponent<JiraServerManager.Config> {

    private List<Runnable> myListeners = new ArrayList<>();
    private SimpleSelectableList<JiraServer> myJiraServers = new SimpleSelectableList<>();
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
        List<JiraServer> servers = loadServers(element);
        myJiraServers.addAll(servers);

        myJiraServers.selectItem(config.selected);
    }

    private List<JiraServer> loadServers(Element element) {
        List<JiraServer> servers = new ArrayList<>();
        if(nonNull(element)){
            for(Element o : element.getChildren()){
                try{
                    JiraServer server = XmlSerializer.deserialize(o, JiraServer.class);
                    servers.add(server);
                }catch (XmlSerializationException e) {
                    //LOG.error(e.getMessage(), e);
                }
            }
        }

        return servers;
    }


    public void addConfigurationServerChangedListener(Runnable runnable){
        myListeners.add(runnable);
    }

    public List<JiraServer> getJiraServers() {
        return myJiraServers.getItems();
    }

    public int getSelectedJiraServerIndex(){
        return myJiraServers.getSelectedItemIndex();
    }

    private JiraServer[] getAllJiraServersAsArray(){
        return getJiraServers().toArray(new JiraServer[0]);
    }

    public boolean hasJiraServerConfigured(){
        return myJiraServers.hasSelectedItem();
    }

    public JiraServer getCurrentJiraServer(){
        return myJiraServers.hasSelectedItem() ? myJiraServers.getItems().get(getSelectedJiraServerIndex()) : null;
    }

    public void setJiraServers(SimpleSelectableList<JiraServer> servers) {
        this.myJiraServers = servers;
        onServersChanged();
    }

    @Nullable
    public JiraRestApi getJiraRestApi(){
        return convertFrom(getCurrentJiraServer());
    }

    @NotNull
    public JiraRestApi getJiraRestApiFrom(@NotNull JiraServer jiraServer){
        return convertFrom(jiraServer);
    }

    @Nullable
    private JiraRestApi convertFrom(@Nullable JiraServer jiraServer){
        if(isNull(jiraServer)){
            return null;
        }

        JiraRepository repository = new JiraRepositoryType().createRepository();
        repository.setUrl(jiraServer.getUrl());
        repository.setUsername(jiraServer.getUsername());
        repository.setPassword(jiraServer.getPassword());

        return new JiraRestApi(repository);
    }

    private void onServersChanged(){
        myListeners.forEach(Runnable::run);
    }


    public static class Config{

        @Tag("selected")
        public int selected;

        @Tag("servers")
        public Element servers;

    }

}
