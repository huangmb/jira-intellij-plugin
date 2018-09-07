package com.intellij.jira.ui;

import com.intellij.jira.rest.model.JiraIssueLink;
import com.intellij.jira.util.JiraLabelUtil;
import com.intellij.ui.components.JBLabel;
import com.intellij.ui.components.JBPanel;
import com.intellij.util.ui.JBUI;

import javax.swing.*;
import java.awt.*;

import static com.intellij.jira.util.JiraLabelUtil.BOLD;
import static com.intellij.jira.util.JiraLabelUtil.ISSUE_LINK_COLOR;
import static java.util.Objects.nonNull;

public class JiraIssueLinkListCellRenderer extends DefaultJiraListCellRender {

    private JBLabel typeLabel;
    private JBLabel issueKeyLabel;

    public JiraIssueLinkListCellRenderer() {
        super();
        init();
    }

    private void init() {
        JBPanel issueLinkpanel = new JBPanel(new BorderLayout())
                .withBorder(JBUI.Borders.empty(4, 5)).andTransparent();
        typeLabel =  JiraLabelUtil.createEmptyLabel().withFont(BOLD);
        issueKeyLabel = JiraLabelUtil.createEmptyLabel().withBorder(JBUI.Borders.emptyLeft(10));
        issueLinkpanel.add(typeLabel, BorderLayout.LINE_START);
        issueLinkpanel.add(issueKeyLabel, BorderLayout.CENTER);
        add(issueLinkpanel);
    }

    @Override
    public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
        JiraIssueLink issueLink = (JiraIssueLink) value;

        setBorder(JBUI.Borders.emptyBottom(2));
        setBackground(ISSUE_LINK_COLOR);

        String typeText = nonNull(issueLink.getInwardIssue()) ? issueLink.getType().getInward() : issueLink.getType().getOutward();
        String issueKeyText = nonNull(issueLink.getInwardIssue()) ? issueLink.getInwardIssue().getKey() : issueLink.getOutwardIssue().getKey();

        typeLabel.setText(typeText);
        issueKeyLabel.setText(issueKeyText);


        return this;
    }

}
