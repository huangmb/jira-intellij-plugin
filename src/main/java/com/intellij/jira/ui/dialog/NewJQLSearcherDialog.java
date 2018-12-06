package com.intellij.jira.ui.dialog;

import com.intellij.jira.components.JQLSearcherManager;
import com.intellij.jira.components.JQLSearcherObserver;
import com.intellij.jira.rest.model.jql.JQLSearcher;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.ui.ValidationInfo;
import com.intellij.tasks.jira.jql.JqlLanguage;
import com.intellij.ui.EditorTextField;
import com.intellij.ui.LanguageTextField;
import com.intellij.ui.components.JBLabel;
import com.intellij.ui.components.JBTextField;
import com.intellij.util.ui.FormBuilder;
import com.intellij.util.ui.JBUI;
import com.intellij.util.ui.UI;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

import java.awt.*;
import java.util.ArrayList;

import static java.util.Objects.nonNull;

public class NewJQLSearcherDialog extends EditJQLSearcherDialog {


    public NewJQLSearcherDialog(@NotNull Project project) {
        super(project, null);
        setTitle("New JQL Searcher");
    }



    @Override
    protected void doOKAction() {
        if(nonNull(myProject)){
            JQLSearcherManager jqlManager = getJqlSearcherManager();
            jqlManager.add(getJqlSearcher());
            //getJqlSearcherObserver().update(jqlManager.getJQLSearchers());
        }

        close(0);
    }






}
