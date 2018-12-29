package com.intellij.jira.ui.editors;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.intellij.icons.AllIcons;
import com.intellij.ide.DataManager;
import com.intellij.jira.rest.model.JiraIssue;
import com.intellij.jira.rest.model.JiraIssueLinkType;
import com.intellij.jira.rest.model.JiraIssueLinkTypeInfo;
import com.intellij.jira.server.JiraServerManager;
import com.intellij.jira.server.JiraRestApi;
import com.intellij.jira.ui.JiraIssueLinkTypeInfoListModel;
import com.intellij.openapi.actionSystem.ActionPlaces;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.ui.ValidationInfo;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.ui.ScrollPaneFactory;
import com.intellij.ui.components.JBList;
import com.intellij.ui.components.JBPanel;
import com.intellij.util.ui.FormBuilder;
import com.intellij.util.ui.UI;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;
import java.awt.event.InputEvent;
import java.util.List;

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

    protected JPanel myPanel;
    protected JTextField myTextField;
    protected JButton myButton;


    private JiraIssueLinkTypeInfo mySelectedLinkType;
    private String mySelectedIssue;

    public LinkedIssueFieldEditor(String fieldName, String issueKey, boolean required) {
        super(fieldName, issueKey, required);
    }

    @Override
    public JComponent createPanel() {

        this.myButton.setIcon(AllIcons.Ide.UpDown);
        this.myButton.addActionListener(e -> {
            InputEvent inputEvent = e.getSource() instanceof InputEvent ? (InputEvent)e.getSource() : null;
            AddIssueLinkDialogAction myAction = new AddIssueLinkDialogAction();
            myAction.actionPerformed(AnActionEvent.createFromAnAction(myAction, inputEvent, ActionPlaces.UNKNOWN, DataManager.getInstance().getDataContext(myTextField)));
        });


        return FormBuilder.createFormBuilder()
                .addLabeledComponent(myLabel, myPanel)
                .getPanel();
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

    @Nullable
    @Override
    public ValidationInfo validate() {
        if(isRequired() && StringUtil.isEmpty(myTextField.getText())){
            return new ValidationInfo(myLabel.getMyLabelText() + " is required.");
        }

        return null;
    }

    private class AddIssueLinkDialogAction extends AnAction {

        public AddIssueLinkDialogAction() {
            super();
        }


        @Override
        public void actionPerformed(AnActionEvent e) {
            Project project = e.getProject();
            if(nonNull(project)){
                JiraServerManager manager = project.getComponent(JiraServerManager.class);
                JiraRestApi jiraRestApi = manager.getJiraRestApi();
                if(nonNull(jiraRestApi)){
                    List<JiraIssueLinkType> issueLinkTypes = jiraRestApi.getIssueLinkTypes();
                    List<String> issues = jiraRestApi.getIssues().stream().map(JiraIssue::getKey).collect(toList());

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
            this.linkTypes.setPreferredSize(UI.size(85, 250));

            issuesKey.remove(issueKey);
            this.issuesKey = new JBList(issuesKey);
            this.issuesKey.setSelectionMode(SINGLE_SELECTION);
            this.issuesKey.setPreferredSize(UI.size(85, 250));

            setTitle("Issue Link");
            init();
        }

        @Nullable
        @Override
        protected JComponent createCenterPanel() {
            JBPanel panel = new JBPanel(new BorderLayout());
            panel.setPreferredSize(UI.size(200, 250));
            panel.add(ScrollPaneFactory.createScrollPane(linkTypes, VERTICAL_SCROLLBAR_AS_NEEDED, HORIZONTAL_SCROLLBAR_AS_NEEDED), WEST);
            panel.add(ScrollPaneFactory.createScrollPane(issuesKey, VERTICAL_SCROLLBAR_AS_NEEDED, HORIZONTAL_SCROLLBAR_AS_NEEDED), EAST);

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
