package com.intellij.jira.actions;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.DataKey;
import org.jetbrains.annotations.NotNull;

public class VersionUpdateAction extends AnAction {

    private String versionName;

    public VersionUpdateAction(@NotNull String versionName) {
        super(versionName);
        this.versionName = versionName;
    }

    @Override
    public void actionPerformed(AnActionEvent e) {
        // obtener datos version
        DataKey<String> version = DataKey.create("version");
        String data = version.getData(e.getDataContext());
    }


}
