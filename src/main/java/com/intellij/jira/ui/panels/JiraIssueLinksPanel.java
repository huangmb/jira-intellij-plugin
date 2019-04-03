package com.intellij.jira.ui.panels;

import com.intellij.jira.actions.AddIssueLinkDialogAction;
import com.intellij.jira.actions.DeleteIssueLinkDialogAction;
import com.intellij.jira.actions.JiraIssueActionGroup;
import com.intellij.jira.rest.model.JiraIssueLink;
import com.intellij.jira.ui.JiraIssueLinkListModel;
import com.intellij.jira.ui.renders.JiraIssueLinkListCellRenderer;
import com.intellij.openapi.actionSystem.ActionGroup;
import com.intellij.openapi.actionSystem.ActionManager;
import com.intellij.openapi.actionSystem.ActionToolbar;
import com.intellij.openapi.ui.SimpleToolWindowPanel;
import com.intellij.ui.ScrollPaneFactory;
import com.intellij.ui.components.JBList;
import com.intellij.ui.components.JBPanel;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.Objects;

import static com.intellij.jira.ui.JiraToolWindowFactory.TOOL_WINDOW_ID;
import static java.awt.BorderLayout.CENTER;
import static javax.swing.ListSelectionModel.SINGLE_SELECTION;
import static javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER;
import static javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED;

class JiraIssueLinksPanel extends SimpleToolWindowPanel {

    private String issueKey;
    private List<JiraIssueLink> issueLinks;
    private JBList<JiraIssueLink> issueLinkList;
    private JiraIssueLink issueLink;

    JiraIssueLinksPanel(List<JiraIssueLink> issueLinks, String issueKey) {
        super(true, true);
        this.issueKey = issueKey;
        this.issueLinks = issueLinks;
        initToolbar();
        initContent();
    }

    private void initToolbar() {
        ActionToolbar actionToolbar = ActionManager.getInstance().createActionToolbar(TOOL_WINDOW_ID, createActionGroup(), true);
        actionToolbar.setTargetComponent(this);

        Box toolBarBox = Box.createHorizontalBox();
        toolBarBox.add(actionToolbar.getComponent());
        setToolbar(toolBarBox);
    }

    private ActionGroup createActionGroup() {
        JiraIssueActionGroup group = new JiraIssueActionGroup(this);
        group.add(new AddIssueLinkDialogAction(issueKey));
        group.add(new DeleteIssueLinkDialogAction(issueKey, () -> issueLink));

        return group;
    }


    private void initContent() {
        JBPanel panel = new JBPanel(new BorderLayout());

        issueLinkList = new JBList<>();
        issueLinkList.setEmptyText("No links");
        issueLinkList.setModel(new JiraIssueLinkListModel(issueLinks));
        issueLinkList.setCellRenderer(new JiraIssueLinkListCellRenderer());
        issueLinkList.setSelectionMode(SINGLE_SELECTION);
        issueLinkList.addListSelectionListener(e -> {
            SwingUtilities.invokeLater(this::updateToolbarActions);
        });

        panel.add(ScrollPaneFactory.createScrollPane(issueLinkList, VERTICAL_SCROLLBAR_AS_NEEDED, HORIZONTAL_SCROLLBAR_NEVER), CENTER);

        setContent(panel);
    }

    private void updateToolbarActions() {
        JiraIssueLink selectedLink = issueLinkList.getSelectedValue();
        if(!Objects.equals(issueLink, selectedLink)){
            issueLink = selectedLink;
            initToolbar();
        }
    }


}
