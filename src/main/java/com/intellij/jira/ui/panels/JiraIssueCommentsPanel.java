package com.intellij.jira.ui.panels;

import com.intellij.jira.util.JiraPanelUtil;
import com.intellij.openapi.ui.SimpleToolWindowPanel;
import com.intellij.ui.JBColor;

class JiraIssueCommentsPanel extends SimpleToolWindowPanel {

    public JiraIssueCommentsPanel() {
        super(true);
        setContent(JiraPanelUtil.createPlaceHolderPanel("No comments to display").withBackground(JBColor.WHITE));
    }

}
