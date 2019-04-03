package com.intellij.jira.ui.dialog;

import com.intellij.jira.rest.model.JiraIssueLinkType;
import com.intellij.jira.rest.model.JiraIssueLinkTypeInfo;
import com.intellij.jira.tasks.AddIssueLinkTask;
import com.intellij.jira.ui.JiraIssueLinkTypeInfoListModel;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.ui.ScrollPaneFactory;
import com.intellij.ui.components.JBList;
import com.intellij.ui.components.JBPanel;
import com.intellij.util.ui.UI;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;
import java.util.List;

import static java.awt.BorderLayout.EAST;
import static java.awt.BorderLayout.WEST;
import static java.util.Objects.nonNull;
import static javax.swing.ListSelectionModel.SINGLE_SELECTION;
import static javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED;
import static javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED;

public class AddIssueLinkDialog extends DialogWrapper {

    protected JList<JiraIssueLinkTypeInfo> linkTypes;
    protected JList<String> issuesKey;
    protected String issueKey;
    private Project project;

    public AddIssueLinkDialog(@Nullable Project project, List<JiraIssueLinkType> linkTypes, List<String> issuesKey, String issueKey) {
        super(project, false);
        this.linkTypes = new JBList<>();
        this.linkTypes.setModel(new JiraIssueLinkTypeInfoListModel(linkTypes));
        this.linkTypes.setSelectionMode(SINGLE_SELECTION);
        this.linkTypes.setPreferredSize(UI.size(85, 250));

        this.issuesKey = new JBList(issuesKey);
        this.issuesKey.setSelectionMode(SINGLE_SELECTION);
        this.issuesKey.setPreferredSize(UI.size(85, 250));

        this.project = project;
        this.issueKey = issueKey;

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
        if(nonNull(selectedType) && nonNull(selectedIssue)){
            String inIssueKey = selectedType.isInward() ? selectedIssue : issueKey;
            String outIssueKey = selectedType.isInward() ? issueKey : selectedIssue;

            new AddIssueLinkTask(project, issueKey, selectedType.getName(), inIssueKey, outIssueKey).queue();
        }

        super.doOKAction();
    }

}
