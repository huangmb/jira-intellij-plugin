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

import java.util.function.BiConsumer;
import java.util.function.Consumer;

import static com.intellij.openapi.util.text.StringUtil.trim;
import static java.lang.String.valueOf;

public class JiraServerEditor {

    private final static int DEFAULT_WIDTH = 450;
    private final static int DEFAULT_HEIGHT = 24;

    private JLabel myUrlLabel;
    private JTextField myUrlField;

    private JLabel myUsernameLabel;
    private JTextField myUsernameField;

    private JLabel myPasswordLabel;
    private JPasswordField myPasswordField;

    private JCheckBox myDefaultServerCheckbox;

    private JPanel myPanel;

    private final JiraServer myServer;
    private final boolean mySelectedServer;

    private BiConsumer<JiraServer, Boolean> myChangeListener;
    private Consumer<JiraServer> myChangeUrlListener;


    public JiraServerEditor(final JiraServer server, boolean selected, BiConsumer<JiraServer, Boolean> changeListener, Consumer<JiraServer> changeUrlListener) {
        this.myServer = server;
        this.mySelectedServer = selected;
        this.myChangeListener = changeListener;
        this.myChangeUrlListener = changeUrlListener;
        init();
    }

    private void init() {

        this.myUrlLabel = new JBLabel("Server URL:", 4);
        this.myUrlField = new JBTextField();
        this.myUrlField.setText(myServer.getUrl());
        this.myUrlField.setPreferredSize(UI.size(DEFAULT_WIDTH, DEFAULT_HEIGHT));

        this.myUsernameLabel = new JBLabel("Username:", 4);
        this.myUsernameField = new JBTextField();
        this.myUsernameField.setText(myServer.getUsername());
        this.myUsernameField.setPreferredSize(UI.size(DEFAULT_WIDTH, DEFAULT_HEIGHT));

        this.myPasswordLabel = new JBLabel("Password:", 4);
        this.myPasswordField = new JPasswordField();
        this.myPasswordField.setText(myServer.getPassword());
        this.myPasswordField.setPreferredSize(UI.size(DEFAULT_WIDTH, DEFAULT_HEIGHT));

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
        checkBox.addActionListener(e -> defaultServerChanged());
    }



    public JPanel getPanel(){
        return myPanel;
    }



    private void apply(){
        this.myServer.setUrl(trim(myUrlField.getText()));
        this.myServer.setUsername(trim(myUsernameField.getText()));
        this.myServer.setPassword(trim(valueOf(myPasswordField.getPassword())));
        this.myChangeUrlListener.accept(myServer);
    }

    private void defaultServerChanged(){
        this.myChangeListener.accept(myServer, myDefaultServerCheckbox.isSelected());
    }


}
