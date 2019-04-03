package com.intellij.jira.ui.dialog;

import com.intellij.jira.tasks.DeleteIssueLinkTask;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.ui.components.JBLabel;
import com.intellij.ui.components.JBPanel;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;

import static java.util.Objects.nonNull;

public class DeleteIssueLinkDialog extends DialogWrapper {

    private Project project;
    private String issueKey;
    private String issueLinkId;

    public DeleteIssueLinkDialog(Project project, String issueKey, String issueLinkId) {
        super(project, false);
        this.project = project;
        this.issueKey = issueKey;
        this.issueLinkId = issueLinkId;
        init();
    }

    @Override
    protected void init() {
        setTitle("Delete Issue Link");
        super.init();
    }

    @Nullable
    @Override
    protected JComponent createCenterPanel() {
        JBPanel panel = new JBPanel(new BorderLayout());
        JBLabel label = new JBLabel("Are you sure you want to delete this link?");
        panel.add(label, BorderLayout.CENTER);

        return panel;
    }


    @NotNull
    @Override
    protected Action[] createActions() {
        return new Action[]{new DeleteIssueLinkExecuteAction(), myCancelAction};
    }


    @Override
    protected void doOKAction() {
        if(nonNull(project)){
            new DeleteIssueLinkTask(project, issueKey, issueLinkId).queue();
        }
        close(0);
    }

    private class DeleteIssueLinkExecuteAction extends OkAction{

    }

}
