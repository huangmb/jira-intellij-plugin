package com.intellij.jira.server;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.ui.Messages;
import com.intellij.ui.JBSplitter;
import com.intellij.ui.ToolbarDecorator;
import com.intellij.ui.components.JBList;
import com.intellij.ui.components.JBPanel;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;

public class ConfigureJiraServersDialog extends DialogWrapper {

    private final Project myProject;
    private JBList<JiraServer2> myServers;
    private JiraServerEditor myJiraServerEditor;
    private int selectedServer = -1;


    public ConfigureJiraServersDialog(@NotNull Project project) {
        super(project, false);
        this.myProject = project;
        init();
    }

    @Nullable
    @Override
    protected JComponent createCenterPanel() {

        JBSplitter mySplitter = new JBSplitter(true, 0.6f);
        mySplitter.setFirstComponent(createServersPanel());
        mySplitter.setSecondComponent(createDetailsServerPanel());

        return mySplitter;
    }


    @Override
    protected void doOKAction() {
        if(selectedServer == -1){
            myProject.getComponent(JiraServerManager2.class).add(myJiraServerEditor.getJiraServer(), myJiraServerEditor.isSetDefault());
        }else {
            myProject.getComponent(JiraServerManager2.class).update(myJiraServerEditor.getJiraServer(), selectedServer, myJiraServerEditor.isSetDefault());
        }

        super.doOKAction();
    }

    private JComponent createServersPanel() {
        myServers = new JBList<>(myProject.getComponent(JiraServerManager2.class).getAllJiraServers());
        myServers.setSelectedIndex(myProject.getComponent(JiraServerManager2.class).getSelectedJiraServer());
        myServers.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        myServers.addListSelectionListener(e -> {
            selectedServer = myServers.getSelectedIndex();
            myJiraServerEditor.fill(myServers.getSelectedValue());
            myJiraServerEditor.setDefault(true);
        });

        JBPanel myPanel = new JBPanel(new BorderLayout());
        myPanel.add(ToolbarDecorator.createDecorator(myServers)
                        .setAddAction(button -> {
                            selectedServer = -1;
                            myJiraServerEditor.reset();
                        })
                        .setRemoveAction(button -> {
                            if (Messages.showOkCancelDialog(myProject, "You are going to delete this server, are you sure?","Delete Server", Messages.getQuestionIcon()) == Messages.OK) {
                                int selectedServer = myServers.getSelectedIndex();
                                myServers.remove(selectedServer);
                                myServers.repaint();
                                myProject.getComponent(JiraServerManager2.class).remove(selectedServer);
                                myServers.setSelectedIndex(myProject.getComponent(JiraServerManager2.class).getSelectedJiraServer());
                            }
                        })
                        .disableUpDownActions().createPanel(), BorderLayout.CENTER);

        return myPanel;
    }

    private JComponent createDetailsServerPanel() {
        myJiraServerEditor = new JiraServerEditor();

        return myJiraServerEditor.getPanel();
    }

}
