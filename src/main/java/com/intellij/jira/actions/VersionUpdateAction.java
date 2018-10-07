package com.intellij.jira.actions;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;

public class VersionUpdateAction extends AnAction {

    private String versionName;

    public VersionUpdateAction(String versionName) {
        super(versionName);
        this.versionName = versionName;
    }

    @Override
    public void actionPerformed(AnActionEvent e) {

    }


}
