package com.intellij.jira.ui.popup;

import com.intellij.openapi.actionSystem.ActionGroup;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.impl.SimpleDataContext;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Condition;
import com.intellij.ui.popup.PopupFactoryImpl;
import org.jetbrains.annotations.NotNull;

public class JiraIssueTransitionsPopup extends PopupFactoryImpl.ActionGroupPopup {

    private static final String TITLE = "Transit to";


    public JiraIssueTransitionsPopup(@NotNull ActionGroup actionGroup,
                                     @NotNull Project project,
                                     Condition<AnAction> preselectActionCondition) {
        super(TITLE, actionGroup, SimpleDataContext.getProjectContext(project), false, false, false, false, null, -1, preselectActionCondition, null);
    }








}
