package com.intellij.jira.actions;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.DefaultActionGroup;
import com.intellij.openapi.actionSystem.ex.ComboBoxAction;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.util.HashMap;
import java.util.Map;

import static java.util.Objects.isNull;

public class VersionsComboBoxAction extends ComboBoxAction {

    private Map<String, AnAction> versionsActions = new HashMap<>();

    public VersionsComboBoxAction() {
        super();
    }

    @Override
    public void update(AnActionEvent e) {
        if(isNull(e.getProject()) || versionsActions.isEmpty()){
            e.getPresentation().setEnabled(false);
        }else{
            e.getPresentation().setEnabled(true);
        }
    }

    public void addAction(String versionId, AnAction action){
        versionsActions.put(versionId, action);
    }

    @Override
    protected int getMaxRows() {
        return 10;
    }

    @NotNull
    @Override
    protected DefaultActionGroup createPopupActionGroup(JComponent button) {
        DefaultActionGroup actionGroup = new DefaultActionGroup();
        versionsActions.values().forEach(action -> actionGroup.add(action));

        return actionGroup;
    }
}
