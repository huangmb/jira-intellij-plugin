package com.intellij.jira.ui.panels;

import com.intellij.jira.rest.model.JiraIssue;
import com.intellij.jira.ui.JiraTabbedPane;
import com.intellij.jira.util.JiraPanelUtil;
import com.intellij.openapi.ui.SimpleToolWindowPanel;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

import static java.util.Objects.isNull;

public class JiraIssueDetailsPanel extends SimpleToolWindowPanel {


    public JiraIssueDetailsPanel(){
        super(true);
        setEmptyContent();
    }


    public void showIssue(@Nullable JiraIssue issue) {
        if(isNull(issue)){
            setEmptyContent();
        }else{
            JiraTabbedPane tabbedPane = new JiraTabbedPane(JTabbedPane.BOTTOM);
            tabbedPane.addTab("Preview", new JiraIssuePreviewPanel(issue));
            tabbedPane.addTab(String.format("Comments (%d)", issue.getComments().getTotal()), new JiraIssueCommentsPanel(issue.getComments()));
            tabbedPane.addTab(String.format("Links (%d)", issue.getIssueLinks().size()), new JiraIssueLinksPanel(issue.getIssueLinks()));

            setContent(tabbedPane);
        }

    }

    public void setEmptyContent(){
        setContent(JiraPanelUtil.createPlaceHolderPanel("Select issue to view details"));
    }

}
