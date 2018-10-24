package com.intellij.jira.ui.editors;

import com.intellij.icons.AllIcons;
import com.intellij.ide.DataManager;
import com.intellij.jira.tasks.JiraServer;
import com.intellij.jira.tasks.JiraServerManager;
import com.intellij.openapi.actionSystem.ActionPlaces;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.ui.ScrollPaneFactory;
import com.intellij.ui.components.JBList;
import com.intellij.ui.components.JBPanel;
import com.intellij.ui.components.JBTextField;
import com.intellij.util.ui.FormBuilder;
import com.intellij.util.ui.UI;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;
import java.awt.event.InputEvent;
import java.util.List;
import java.util.Optional;

import static java.awt.BorderLayout.CENTER;
import static java.util.Objects.nonNull;
import static java.util.stream.Collectors.toList;
import static javax.swing.ListSelectionModel.SINGLE_SELECTION;
import static javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED;
import static javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED;

public class UserTextFieldEditor extends CustomTextFieldEditor {

    private JButton myButton;
    private String selectedUser;

    public UserTextFieldEditor(String fieldName, String issueKey) {
        super(fieldName, issueKey);
    }

    @Override
    public JComponent createPanel() {
        this.myTextField = new JBTextField();
        this.myTextField.setEnabled(false);
        this.myTextField.setEditable(false);
        this.myTextField.setPreferredSize(UI.size(280, this.myTextField.getHeight()));

        this.myButton = new JButton(AllIcons.Modules.Types.UserDefined);
        this.myButton.setPreferredSize(UI.size(20, this.myButton.getHeight()));
        this.myButton.addActionListener(e -> {
            InputEvent inputEvent = e.getSource() instanceof InputEvent ? (InputEvent)e.getSource() : null;
            UserPickerDialogAction myAction = new UserPickerDialogAction();
            myAction.actionPerformed(AnActionEvent.createFromAnAction(myAction, inputEvent, ActionPlaces.UNKNOWN, DataManager.getInstance().getDataContext(myTextField)));
        });

        JPanel panel = new JBPanel(new GridLayout(1,1));
        panel.setPreferredSize(UI.size(300, panel.getHeight()));
        panel.add(myTextField);
        panel.add(myButton);


        return FormBuilder.createFormBuilder()
                .addLabeledComponent(this.myFieldLabel, panel)
                .getPanel();
    }



    private class UserPickerDialogAction extends AnAction {

        public UserPickerDialogAction() {
            super(AllIcons.Modules.Types.UserDefined);
        }


        @Override
        public void actionPerformed(AnActionEvent e) {
            Project project = e.getProject();
            if(nonNull(project)){
                JiraServerManager serverManager = project.getComponent(JiraServerManager.class);
                Optional<JiraServer> jiraServer = serverManager.getConfiguredJiraServer();
                if(jiraServer.isPresent()){
                    List<String> users = jiraServer.get().getAssignableUsers(issueKey).stream().map(user -> user.getKey()).collect(toList());

                    UserPickerDialog dialog = new UserPickerDialog(project, users);
                    dialog.show();
                }
            }
        }
    }


    public class UserPickerDialog extends DialogWrapper {
        private JList<String> users;

        public UserPickerDialog(@Nullable Project project, List<String> users) {
            super(project, false);

            this.users = new JBList(users);
            this.users.setPreferredSize(UI.size(75, 250));
            this.users.setSelectionMode(SINGLE_SELECTION);
            if(nonNull(selectedUser)){
                this.users.setSelectedValue(selectedUser, true);
            }


            setTitle("Users");
            init();
        }

        @Nullable
        @Override
        protected JComponent createCenterPanel() {
            JBPanel panel = new JBPanel(new BorderLayout());
            panel.setPreferredSize(UI.size(100, 250));
            panel.add(ScrollPaneFactory.createScrollPane(users, VERTICAL_SCROLLBAR_AS_NEEDED, HORIZONTAL_SCROLLBAR_AS_NEEDED), CENTER);


            return panel;
        }

        @Override
        protected void doOKAction() {
            selectedUser = users.getSelectedValue();
            myTextField.setText(nonNull(selectedUser) ? selectedUser : "");


            super.doOKAction();
        }
    }









}
