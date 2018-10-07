package com.intellij.jira.actions;

import com.intellij.icons.AllIcons;
import com.intellij.openapi.actionSystem.AnActionEvent;

public class VersionPreviewDetailsAction extends JiraIssueAction  {

    private static final ActionProperties properties = ActionProperties.of("Version details",  AllIcons.Actions.PreviewDetails);

    public VersionPreviewDetailsAction() {
        super(properties);
    }

    @Override
    public void actionPerformed(AnActionEvent e) {
        // TODO
    }


}
