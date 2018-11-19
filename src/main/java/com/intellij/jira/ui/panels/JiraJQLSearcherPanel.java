package com.intellij.jira.ui.panels;

import com.intellij.jira.components.JQLSearcherManager;
import com.intellij.jira.rest.model.jql.JQLSearcher;
import com.intellij.jira.tasks.RefreshIssuesTask;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.ComboBox;
import com.intellij.ui.CollectionComboBoxModel;
import com.intellij.ui.components.JBPanel;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Set;

import static java.util.Objects.nonNull;

public class JiraJQLSearcherPanel extends JBPanel {

    private Project myProject;
    private ComboBox<JQLSearcher> myComboBox;
    private CollectionComboBoxModel<JQLSearcher> myComboBoxItems;

    public JiraJQLSearcherPanel(Project project) {
        super();
        this.myProject = project;
        init();
        installListeners();
    }

    private void init() {
        Set<JQLSearcher> jqlSearchers = getJQLSearcherManager().getJQLSearchers();
        JQLSearcher deafaultJQLSearcher = getJQLSearcherManager().getDeafaultJQLSearcher();
        if(jqlSearchers.isEmpty()){
            jqlSearchers.add(deafaultJQLSearcher);
            jqlSearchers.add(new JQLSearcher("assignee = currentUser()"));
        }
        myComboBoxItems = new CollectionComboBoxModel(new ArrayList(jqlSearchers));
        myComboBox = new ComboBox(myComboBoxItems, 300);
        myComboBox.setSelectedItem(deafaultJQLSearcher);
        add(myComboBox);
    }

    private void installListeners() {
        this.myComboBox.addActionListener(e -> {
            JQLSearcher selectedItem = (JQLSearcher) this.myComboBox.getSelectedItem();
            if(nonNull(selectedItem)){
                selectedItem.setSelected(true);
                getJQLSearcherManager().update(selectedItem);
                SwingUtilities.invokeLater(() -> new RefreshIssuesTask(myProject).queue());
            }
        });
    }


    private JQLSearcherManager getJQLSearcherManager(){
        return myProject.getComponent(JQLSearcherManager.class);
    }





}
