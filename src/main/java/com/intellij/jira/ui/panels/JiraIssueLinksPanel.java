package com.intellij.jira.ui.panels;

import com.intellij.jira.rest.model.JiraIssueLink;
import com.intellij.jira.util.JiraLabelUtil;
import com.intellij.jira.util.JiraPanelUtil;
import com.intellij.openapi.ui.SimpleToolWindowPanel;
import com.intellij.ui.ScrollPaneFactory;
import com.intellij.ui.components.JBLabel;
import com.intellij.ui.components.JBPanel;
import com.intellij.util.ui.JBUI;

import javax.swing.*;
import java.awt.*;
import java.util.List;

import static java.awt.BorderLayout.CENTER;
import static java.util.Objects.nonNull;
import static javax.swing.BoxLayout.Y_AXIS;
import static javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED;

public class JiraIssueLinksPanel extends SimpleToolWindowPanel {

    private List<JiraIssueLink> issueLinks;

    public JiraIssueLinksPanel(List<JiraIssueLink> issueLinks) {
        super(true, true);
        this.issueLinks = issueLinks;
        initContent();
    }

    private void initContent() {
        if(issueLinks.isEmpty()){
            setContent(JiraPanelUtil.createPlaceHolderPanel("No links"));
        }
        else{
            JBPanel issueLinksPanel = new JBPanel();
            issueLinksPanel.setLayout(new BoxLayout(issueLinksPanel, Y_AXIS));
            issueLinks.forEach(issueLink -> issueLinksPanel.add(createIssueLinkPanel(issueLink)));

            JBPanel panel = JiraPanelUtil.createWhitePanel(new BorderLayout());
            panel.add(ScrollPaneFactory.createScrollPane(issueLinksPanel, VERTICAL_SCROLLBAR_AS_NEEDED), CENTER);

            setContent(panel);
        }

    }

    private JPanel createIssueLinkPanel(JiraIssueLink issueLink) {
        JBPanel wrapper = JiraPanelUtil.createWhitePanel(new BorderLayout())
                .withBorder(JBUI.Borders.empty(2, 4));

        String issueLinkLabel;
        if(nonNull(issueLink.getInwardIssue())){
            issueLinkLabel = issueLink.getType().getInward() + " " + issueLink.getInwardIssue().getKey();
        }else{
            issueLinkLabel = issueLink.getType().getOutward() + " " + issueLink.getOutwardIssue().getKey();
        }

        JBLabel authorLabel = JiraLabelUtil.createBoldLabel(issueLinkLabel);
        wrapper.add(authorLabel, BorderLayout.PAGE_START);

        return wrapper;
    }


}
