package com.intellij.jira.ui.editors;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.intellij.ide.DataManager;
import com.intellij.jira.rest.model.JiraIssueLinkType;
import com.intellij.jira.rest.model.JiraIssueLinkTypeInfo;
import com.intellij.jira.tasks.JiraServer;
import com.intellij.jira.tasks.JiraServerManager;
import com.intellij.jira.ui.JiraIssueLinkTypeInfoListModel;
import com.intellij.openapi.actionSystem.ActionPlaces;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.ui.ScrollPaneFactory;
import com.intellij.ui.components.JBList;
import com.intellij.ui.components.JBPanel;
import com.intellij.util.PlatformIcons;
import com.intellij.util.ui.FormBuilder;
import com.intellij.util.ui.JBDimension;
import com.intellij.util.ui.UI;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;
import java.awt.event.InputEvent;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.intellij.jira.util.JiraGsonUtil.createNameObject;
import static com.intellij.jira.util.JiraGsonUtil.createObject;
import static java.awt.BorderLayout.EAST;
import static java.awt.BorderLayout.WEST;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static java.util.stream.Collectors.toList;
import static javax.swing.ListSelectionModel.SINGLE_SELECTION;
import static javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED;
import static javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED;

public class LinkedIssueFieldEditor extends AbstractFieldEditor {

    private JTextField myTextField;
    private JButton myButton;

    private JiraIssueLinkTypeInfo mySelectedLinkType;
    private String mySelectedIssue;

    public LinkedIssueFieldEditor(String fieldName) {
        super(fieldName);
    }

    @Override
    public JComponent createPanel() {
        this.myTextField = new JTextField();
        this.myTextField.setEnabled(false);
        this.myTextField.setEditable(false);
        this.myTextField.setPreferredSize(UI.size(255, this.myTextField.getHeight()));

        this.myButton = new JButton(PlatformIcons.UP_DOWN_ARROWS);
        this.myButton.addActionListener(e -> {
            InputEvent inputEvent = e.getSource() instanceof InputEvent ? (InputEvent)e.getSource() : null;
            AddIssueLinkDialogAction myAction = new AddIssueLinkDialogAction();
            myAction.actionPerformed(AnActionEvent.createFromAnAction(myAction, inputEvent, ActionPlaces.UNKNOWN, DataManager.getInstance().getDataContext(myTextField)));
        });

        JPanel panel = new JBPanel(new GridLayout(1,1));
        panel.add(myTextField);
        panel.add(myButton);

        return FormBuilder.createFormBuilder()
                .addLabeledComponent(myFieldLabel, panel)
                .getPanel();
    }

    @Override
    public Map<String, String> getInputValues() {
        myInputValues.put(myFieldLabel.getText(), myTextField.getText());

        return myInputValues;
    }

    @Override
    public JsonElement getJsonValue() {
        if(isNull(mySelectedLinkType)){
            return JsonNull.INSTANCE;
        }

        JsonArray array = new JsonArray();
        JsonObject rootObject = new JsonObject();
        JsonObject addObject = new JsonObject();
        addObject.add("type", createNameObject(mySelectedLinkType.getName()));
        addObject.add(mySelectedLinkType.isInward() ? "inwardIssue" : "outwardIssue", createObject("key", mySelectedIssue));


        rootObject.add("add", addObject);
        array.add(rootObject);
        return array;
    }

    private class AddIssueLinkDialogAction extends AnAction {

        public AddIssueLinkDialogAction() {
            super(PlatformIcons.UP_DOWN_ARROWS);
        }


        @Override
        public void actionPerformed(AnActionEvent e) {
            Project project = e.getProject();
            if(nonNull(project)){
                JiraServerManager serverManager = project.getComponent(JiraServerManager.class);
                Optional<JiraServer> jiraServer = serverManager.getConfiguredJiraServer();
                if(jiraServer.isPresent()){
                    List<JiraIssueLinkType> issueLinkTypes = jiraServer.get().getIssueLinkTypes();
                    List<String> issues = jiraServer.get().getIssues().stream().map(issue -> issue.getKey()).collect(toList());

                    AddIssueLinkDialog dialog = new AddIssueLinkDialog(project, issueLinkTypes, issues);
                    dialog.show();
                }
            }
        }
    }





    public class AddIssueLinkDialog extends DialogWrapper {
        private JList<JiraIssueLinkTypeInfo> linkTypes;
        private JList<String> issuesKey;

        public AddIssueLinkDialog(@Nullable Project project, List<JiraIssueLinkType> linkTypes, List<String> issuesKey) {
            super(project, false);
            this.linkTypes = new JBList<>();
            this.linkTypes.setModel(new JiraIssueLinkTypeInfoListModel(linkTypes));
            this.linkTypes.setSelectionMode(SINGLE_SELECTION);
            this.linkTypes.setPreferredSize(new JBDimension(75, 250));

            //issuesKey.remove(issueKey);
            this.issuesKey = new JBList(issuesKey);
            this.issuesKey.setSelectionMode(SINGLE_SELECTION);
            this.issuesKey.setPreferredSize(new JBDimension(75, 250));

            init();
        }

        @Nullable
        @Override
        protected JComponent createCenterPanel() {
            JBPanel panel = new JBPanel(new BorderLayout());
            panel.setPreferredSize(UI.size(150, 250));
            panel.add(ScrollPaneFactory.createScrollPane(linkTypes, VERTICAL_SCROLLBAR_AS_NEEDED, HORIZONTAL_SCROLLBAR_AS_NEEDED), WEST);
            panel.add(issuesKey, EAST);

            return panel;
        }

        @Override
        protected void doOKAction() {
            JiraIssueLinkTypeInfo selectedType = linkTypes.getSelectedValue();
            String selectedIssue = issuesKey.getSelectedValue();
            mySelectedLinkType = selectedType;
            mySelectedIssue = selectedIssue;
            if(nonNull(selectedType) && nonNull(selectedIssue)){
                myTextField.setText(selectedType.getDescription() + " " + selectedIssue);
            }

            super.doOKAction();
        }
    }




}
