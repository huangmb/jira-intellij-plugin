package com.intellij.jira.server;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.ui.DocumentAdapter;
import com.intellij.ui.components.JBLabel;
import com.intellij.ui.components.JBTextField;
import com.intellij.util.ui.FormBuilder;
import com.intellij.util.ui.JBUI;
import com.intellij.util.ui.UI;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import javax.swing.event.DocumentEvent;

import static com.intellij.openapi.util.text.StringUtil.trim;
import static java.lang.String.valueOf;

public class JiraServerEditor {

    private JLabel myUrlLabel;
    private JTextField myUrlField;

    private JLabel myUsernameLabel;
    private JTextField myUsernameField;

    private JLabel myPasswordLabel;
    private JPasswordField myPasswordField;

    private JCheckBox myDefaultServerCheckbox;

    private JPanel myPanel;

    private final JiraServer2 myServer;
    private final boolean mySelectedServer;


    public JiraServerEditor(final JiraServer2 server) {
        this(server, false);
    }

    public JiraServerEditor(final JiraServer2 server, boolean selected) {
        this.myServer = server;
        this.mySelectedServer = selected;
        init();
    }

    private void init() {

        this.myUrlLabel = new JBLabel("Server URL:", 4);
        this.myUrlField = new JBTextField();
        this.myUrlField.setText(myServer.getUrl());
        this.myUrlField.setPreferredSize(UI.size(300, 24));

        this.myUsernameLabel = new JBLabel("Username:", 4);
        this.myUsernameField = new JBTextField();
        this.myUsernameField.setText(myServer.getUsername());
        this.myUsernameField.setPreferredSize(UI.size(300, 24));

        this.myPasswordLabel = new JBLabel("Password:", 4);
        this.myPasswordField = new JPasswordField();
        this.myPasswordField.setText(myServer.getPassword());
        this.myPasswordField.setPreferredSize(UI.size(300, 24));

        this.myDefaultServerCheckbox = new JCheckBox("Set Default");
        this.myDefaultServerCheckbox.setBorder(JBUI.Borders.emptyRight(4));
        this.myDefaultServerCheckbox.setSelected(mySelectedServer);


        installListener(myUrlField);
        installListener(myUsernameField);
        installListener(myPasswordField);
        installListener(myDefaultServerCheckbox);


        this.myPanel = FormBuilder.createFormBuilder()
                            .addLabeledComponent(this.myUrlLabel, this.myUrlField)
                            .addLabeledComponent(this.myUsernameLabel, this.myUsernameField)
                            .addLabeledComponent(this.myPasswordLabel, this.myPasswordField)
                            .addComponent(myDefaultServerCheckbox)
                            .getPanel();
    }

    private void installListener(JTextField textField) {
        textField.getDocument().addDocumentListener(new DocumentAdapter() {
            @Override
            protected void textChanged(@NotNull DocumentEvent e) {
                ApplicationManager.getApplication().invokeLater(() -> {
                    apply();
                });

            }
        });
    }

    private void installListener(JCheckBox checkBox) {
        checkBox.addActionListener(e -> apply());
    }



    public JPanel getPanel(){
        return myPanel;
    }



    public void apply(){
        this.myServer.setUrl(trim(myUrlField.getText()));
        this.myServer.setUsername(trim(myUsernameField.getText()));
        this.myServer.setPassword(trim(valueOf(myPasswordField.getPassword())));
    }



    public JiraServer2 getJiraServer(){
        JiraServer2 jiraServer = new JiraServer2();
        jiraServer.setUrl(myUrlField.getText());
        jiraServer.setUsername(myUsernameField.getText());
        jiraServer.setPassword(valueOf(myPasswordField.getPassword()));

        return jiraServer;
    }

    public boolean isSetDefault(){
        return myDefaultServerCheckbox.isSelected();
    }

    public void setDefault(boolean isDefault){
        myDefaultServerCheckbox.setSelected(isDefault);
    }


    public void reset(){
        myUrlField.setText("");
        myUsernameField.setText("");
        myPasswordField.setText("");
        myDefaultServerCheckbox.setSelected(false);
    }

    public void show(@NotNull JiraServer2 jiraServer, boolean selected){
        myUrlField.setText(jiraServer.getUrl());
        myUsernameField.setText(jiraServer.getUsername());
        myPasswordField.setText(jiraServer.getPassword());
        myDefaultServerCheckbox.setSelected(selected);
    }

}
