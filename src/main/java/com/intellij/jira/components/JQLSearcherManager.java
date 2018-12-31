package com.intellij.jira.components;

import com.intellij.configurationStore.XmlSerializer;
import com.intellij.jira.rest.model.jql.JQLSearcher;
import com.intellij.jira.util.SimpleSelectableList;
import com.intellij.openapi.components.*;
import com.intellij.openapi.project.Project;
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

@State(name = "JQLSearcherManager", storages = @Storage(StoragePathMacros.WORKSPACE_FILE))
public class JQLSearcherManager implements ProjectComponent, PersistentStateComponent<JQLSearcherManager.Config> {

    private final static JQLSearcher DEFAULT_JQL = new JQLSearcher("Assigned to me", "assignee = currentUser()");
    private final Project myProject;

    private SimpleSelectableList<JQLSearcher> mySearchers = new SimpleSelectableList<>();
    private Config myConfig = new Config();

    @Nullable
    @Override
    public Config getState() {
        myConfig.selected = mySearchers.getSelectedItemIndex();
        myConfig.searchers = XmlSerializer.serialize(getSearchersAsArray());
        return myConfig;
    }

    @Override
    public void loadState(@NotNull Config config) {
        XmlSerializerUtil.copyBean(config, myConfig);

        mySearchers.clear();
        Element element = config.searchers;
        List<JQLSearcher> searchers = loadSearchers(element);
        mySearchers.addAll(searchers);

        mySearchers.selectItem(config.selected);
    }

    private List<JQLSearcher> loadSearchers(Element element) {
        List<JQLSearcher> searchers = new ArrayList<>();
        if(nonNull(element)){
            for(Element o : element.getChildren()){
                try{
                    JQLSearcher searcher = XmlSerializer.deserialize(o, JQLSearcher.class);
                    searchers.add(searcher);
                }catch (XmlSerializationException e) {
                    //LOG.error(e.getMessage(), e);
                }
            }
        }

        return searchers;
    }


    public List<JQLSearcher> getSearchers() {
        return mySearchers.getItems();
    }


    public JQLSearcher getSelectedSearcher(){
        return mySearchers.hasSelectedItem() ? mySearchers.getSelectedItem() : null;
    }

    public int getSelectedSearcherIndex(){
        return mySearchers.getSelectedItemIndex();
    }

    private JQLSearcher[] getSearchersAsArray(){
        return getSearchers().toArray(new JQLSearcher[0]);
    }

    public boolean hasSelectedSearcher(){
        return mySearchers.hasSelectedItem();
    }

    public void setSearchers(SimpleSelectableList<JQLSearcher> mySearchers) {
        this.mySearchers = mySearchers;
        notifyObservers();
    }

    protected JQLSearcherManager(Project project) {
        this.myProject = project;
        this.mySearchers.add(DEFAULT_JQL);
    }


    public void add(JQLSearcher searcher, boolean selected){
        this.mySearchers.add(searcher, selected);
        notifyObservers();
    }


    public void remove(JQLSearcher searcher) {
        this.mySearchers.remove(searcher);
        notifyObservers();
    }

    public void update(String oldAliasSearcher, JQLSearcher updatedSearcher, boolean selected){
        JQLSearcher oldSearcher = findByAlias(oldAliasSearcher);
        if(isNull(oldSearcher)){
            return;
        }

        mySearchers.update(mySearchers.getItems().indexOf(oldSearcher), updatedSearcher, selected);
        notifyObservers();
    }

    @Nullable
    private JQLSearcher findByAlias(String alias){
        return mySearchers.getItems().stream()
                .filter(searcher -> searcher.getAlias().equals(alias))
                .findFirst().orElse(null);
    }



    private void notifyObservers(){
        getJqlSearcherObserver().update(getSearchers());
    }

    private JQLSearcherObserver getJqlSearcherObserver(){
        return myProject.getComponent(JQLSearcherObserver.class);
    }


    public static class Config{
        @Tag("selected")
        public int selected;

        @Tag("searchers")
        public Element searchers;
    }


}
