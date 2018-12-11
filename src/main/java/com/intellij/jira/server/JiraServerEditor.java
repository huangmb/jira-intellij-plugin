package com.intellij.jira.server;

import com.intellij.ui.components.JBLabel;
import com.intellij.ui.components.JBTextField;
import com.intellij.util.ui.FormBuilder;
import com.intellij.util.ui.JBUI;
import com.intellij.util.ui.UI;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.util.Arrays;

public class JiraServerEditor {

    private JLabel myUrlLabel;
    private JTextField myUrlField;

    private JLabel myUsernameLabel;
    private JTextField myUsernameField;

    private JLabel myPasswordLabel;
    private JPasswordField myPasswordField;

    private JCheckBox mySetDefault;

    private JiraServer2 myJiraServer;

    public JiraServerEditor() {
    }

    public JiraServerEditor(JiraServer2 jiraServer) {
        this.myJiraServer = jiraServer;
    }


    public JPanel getPanel(){
        this.myUrlLabel = new JBLabel("Server URL:", 4);
        this.myUrlField = new JBTextField();
        this.myUrlField.setPreferredSize(UI.size(300, 24));

        this.myUsernameLabel = new JBLabel("Username:", 4);
        this.myUsernameField = new JBTextField();
        this.myUsernameField.setPreferredSize(UI.size(300, 24));

        this.myPasswordLabel = new JBLabel("Password:", 4);
        this.myPasswordField = new JPasswordField();
        this.myPasswordField.setPreferredSize(UI.size(300, 24));

        this.mySetDefault = new JCheckBox("Set Default");
        this.mySetDefault.setBorder(JBUI.Borders.emptyRight(4));
        this.mySetDefault.setSelected(false);


        return FormBuilder.createFormBuilder()
                .addLabeledComponent(this.myUrlLabel, this.myUrlField)
                .addLabeledComponent(this.myUsernameLabel, this.myUsernameField)
                .addLabeledComponent(this.myPasswordLabel, this.myPasswordField)
                .addComponent(mySetDefault)
                .getPanel();
    }


    public JiraServer2 getJiraServer(){
        return new JiraServer2(myUrlField.getText(), myUsernameField.getText(), Arrays.toString(myPasswordField.getPassword()));
    }

    public boolean isSetDefault(){
        return mySetDefault.isSelected();
    }

    public void setDefault(boolean isDefault){
        mySetDefault.setSelected(isDefault);
    }


    public void reset(){
        myUrlField.setText("");
        myUsernameField.setText("");
        myPasswordField.setText("");
        mySetDefault.setSelected(false);
    }

    public void fill(@NotNull JiraServer2 jiraServer){
        myUrlField.setText(jiraServer.getUrl());
        myUsernameField.setText(jiraServer.getUsername());
        myPasswordField.setText(jiraServer.getPassword());
    }

}
