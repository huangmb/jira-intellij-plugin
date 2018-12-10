package com.intellij.jira.ui.dialog;

import com.intellij.jira.components.JQLSearcherManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.ValidationInfo;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static com.intellij.openapi.util.text.StringUtil.trim;
import static java.util.Objects.nonNull;

public class NewJQLSearcherDialog extends EditJQLSearcherDialog {


    public NewJQLSearcherDialog(@NotNull Project project) {
        super(project, null);
        setTitle("New JQL Searcher");
    }


    @Nullable
    @Override
    protected ValidationInfo doValidate() {
        if(myProject.getComponent(JQLSearcherManager.class).alreadyExistJQLSearcherWithAlias(trim(myAliasField.getText()))){
            return new ValidationInfo("Alias already exist");
        }

        return super.doValidate();
    }

    @Override
    protected void doOKAction() {
        if(nonNull(myProject)){
            JQLSearcherManager jqlManager = getJqlSearcherManager();
            jqlManager.add(getJqlSearcher());
        }

        close(0);
    }






}
