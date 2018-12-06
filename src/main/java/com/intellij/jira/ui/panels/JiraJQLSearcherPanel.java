package com.intellij.jira.ui.panels;

import com.intellij.jira.components.JQLSearcherManager;
import com.intellij.jira.components.JQLSearcherObserver;
import com.intellij.jira.events.JQLSearcherEventListener;
import com.intellij.jira.rest.model.jql.JQLSearcher;
import com.intellij.jira.tasks.RefreshIssuesTask;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.ComboBox;
import com.intellij.ui.CollectionComboBoxModel;
import com.intellij.ui.components.JBPanel;
import com.intellij.util.ui.JBUI;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static java.util.Objects.nonNull;

public class JiraJQLSearcherPanel extends JBPanel implements JQLSearcherEventListener {

    private Project myProject;
    private ComboBox<JQLSearcher> myComboBox;
    private CollectionComboBoxModel<JQLSearcher> myComboBoxItems;
    private JQLSearcher mySelectedSearcher;

    public JiraJQLSearcherPanel(Project project) {
        super(new BorderLayout());
        this.myProject = project;
        init();
        installListeners();
    }

    private void init() {
        setBorder(JBUI.Borders.empty(2, 4));
        List<JQLSearcher> jqlSearchers = getJQLSearcherManager().getJQLSearchers();
        mySelectedSearcher = getJQLSearcherManager().getDeafaultJQLSearcher();
        if(jqlSearchers.isEmpty()){
            jqlSearchers.add(mySelectedSearcher);
        }

        myComboBoxItems = new CollectionComboBoxModel(new ArrayList(jqlSearchers));
        myComboBox = new ComboBox(myComboBoxItems, 300);
        myComboBox.setSelectedItem(mySelectedSearcher);
        add(myComboBox, BorderLayout.WEST);
    }

    private void installListeners() {
        this.myComboBox.addActionListener(e -> {
            JQLSearcher selectedItem = (JQLSearcher) this.myComboBox.getSelectedItem();
            if(nonNull(selectedItem)){
                selectedItem.setSelected(true);
                getJQLSearcherManager().update(mySelectedSearcher.getAlias(), selectedItem);
                mySelectedSearcher = selectedItem;
                SwingUtilities.invokeLater(() -> new RefreshIssuesTask(myProject).queue());
            }
        });

        getJQLSearcherObserver().addListener(this);

    }


    private JQLSearcherManager getJQLSearcherManager(){
        return myProject.getComponent(JQLSearcherManager.class);
    }

    private JQLSearcherObserver getJQLSearcherObserver(){
        return myProject.getComponent(JQLSearcherObserver.class);
    }

    @Override
    public void update(List<JQLSearcher> jqlSearchers) {
        myComboBoxItems.removeAll();
        myComboBoxItems.add(jqlSearchers);
        myComboBoxItems.update();
    }

    @Override
    public void update(JQLSearcher jqlSearcher) {

    }
}
