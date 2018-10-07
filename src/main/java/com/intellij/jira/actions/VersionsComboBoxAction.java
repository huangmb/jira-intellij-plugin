package com.intellij.jira.actions;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.DefaultActionGroup;
import com.intellij.openapi.actionSystem.Presentation;
import com.intellij.openapi.actionSystem.ex.ComboBoxAction;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.util.HashMap;
import java.util.Map;

public class VersionsComboBoxAction extends ComboBoxAction {

    private Map<String, AnAction> versionsActions = new HashMap<>();

    public VersionsComboBoxAction() {
        super();
    }

    @Override
    public void update(AnActionEvent e) {
        super.update(e);
        Presentation presentation = e.getPresentation();
        presentation.setText("Prueba");
    }

    public void addAction(String versionId, AnAction action){
        versionsActions.put(versionId, action);
    }


    @NotNull
    @Override
    protected DefaultActionGroup createPopupActionGroup(JComponent button) {
        DefaultActionGroup actionGroup = new DefaultActionGroup();
        versionsActions.values().forEach(action -> actionGroup.add(action));

        return actionGroup;
    }
}
