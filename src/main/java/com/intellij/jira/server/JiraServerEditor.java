package com.intellij.jira.server;

import com.intellij.jira.tasks.TestJiraServerConnectionTask;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.progress.ProcessCanceledException;
import com.intellij.openapi.progress.ProgressManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.ui.DocumentAdapter;
import com.intellij.ui.components.JBLabel;
import com.intellij.ui.components.JBTextField;
import com.intellij.util.ui.FormBuilder;
import com.intellij.util.ui.JBUI;
import com.intellij.util.ui.UI;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import javax.swing.event.DocumentEvent;

import java.awt.*;
import java.net.UnknownHostException;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import static com.intellij.jira.util.JiraPanelUtil.MARGIN_BOTTOM;
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
    private JButton myTestButton;

    private JPanel myPanel;

    private final Project myProject;
    private final JiraServer myServer;
    private final boolean mySelectedServer;

    private BiConsumer<JiraServer, Boolean> myChangeListener;
    private Consumer<JiraServer> myChangeUrlListener;


    public JiraServerEditor(Project project, JiraServer server, boolean selected, BiConsumer<JiraServer, Boolean> changeListener, Consumer<JiraServer> changeUrlListener) {
        this.myProject = project;
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

        JPanel buttonPanel = new JPanel(new BorderLayout());
        buttonPanel.setBorder(MARGIN_BOTTOM);
        this.myTestButton = new JButton("Test");
        buttonPanel.add(myTestButton, BorderLayout.EAST);

        installListener(myUrlField);
        installListener(myUsernameField);
        installListener(myPasswordField);
        installListener(myDefaultServerCheckbox);
        installListener(myTestButton);


        this.myPanel = FormBuilder.createFormBuilder()
                            .addLabeledComponent(this.myUrlLabel, this.myUrlField)
                            .addLabeledComponent(this.myUsernameLabel, this.myUsernameField)
                            .addLabeledComponent(this.myPasswordLabel, this.myPasswordField)
                            .addComponent(myDefaultServerCheckbox)
                            .addComponentToRightColumn(buttonPanel)
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

    private void installListener(JButton button){
        button.addActionListener((event) -> {
            SwingUtilities.invokeLater(() -> {
                TestJiraServerConnectionTask task = new TestJiraServerConnectionTask(myProject, myServer);
                ProgressManager.getInstance().run(task);
                Exception e = task.getException();
                if (e == null) {
                    Messages.showMessageDialog(myProject, "Connection is successful", "Connection", Messages.getInformationIcon());
                }
                else if (!(e instanceof ProcessCanceledException)) {
                    String message = e.getMessage();
                    if (e instanceof UnknownHostException) {
                        message = "Unknown host: " + message;
                    }
                    if (message == null) {
                        message = "Unknown error";
                    }
                    Messages.showErrorDialog(myProject, StringUtil.capitalize(message), "Error");
                }
            });
        });
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
