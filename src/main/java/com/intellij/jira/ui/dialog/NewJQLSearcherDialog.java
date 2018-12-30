package com.intellij.jira.ui.dialog;

import com.intellij.jira.components.JQLSearcherManager;
import com.intellij.jira.rest.model.jql.JQLSearcher;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;

import static java.util.Objects.nonNull;

public class NewJQLSearcherDialog extends EditJQLSearcherDialog {

    public NewJQLSearcherDialog(@NotNull Project project) {
        this(project, true);
    }

    public NewJQLSearcherDialog(@NotNull Project project, boolean applyOkAction) {
        super(project, new JQLSearcher(), false, applyOkAction);
        setTitle("New JQL Searcher");
    }


    @Override
    protected void doOKAction() {
        if(myApplyOkAction && nonNull(myProject)){
            JQLSearcherManager jqlManager = getJqlSearcherManager();
            jqlManager.add(mySearcher, myEditor.isSelectedSearcher());
        }

        close(0);
    }






}
