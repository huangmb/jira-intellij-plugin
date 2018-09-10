package com.intellij.jira.ui.dialog;

import com.intellij.jira.tasks.AddCommentTask;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.ui.ValidationInfo;
import com.intellij.ui.JBColor;
import com.intellij.ui.components.JBPanel;
import org.apache.commons.lang.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;

import static java.util.Objects.nonNull;

public class AddCommentDialog extends DialogWrapper {

    private Project project;
    private String issueKey;
    private JTextArea commentArea;

    public AddCommentDialog(@Nullable Project project, @NotNull String issueKey) {
        super(project, false);
        this.project = project;
        this.issueKey = issueKey;
        init();
    }


    @Override
    protected void init() {
        setTitle(String.format("Add a comment to %s", issueKey));
        super.init();
    }

    @Nullable
    @Override
    protected JComponent createCenterPanel() {
        JBPanel panel = new JBPanel(new BorderLayout());
        commentArea = new JTextArea(6, 60);
        commentArea.setBorder(BorderFactory.createLineBorder(JBColor.darkGray));
        panel.add(commentArea, BorderLayout.CENTER);

        return panel;
    }


    @NotNull
    @Override
    protected Action[] createActions() {
        return new Action[]{new AddCommentExecuteAction(), myCancelAction};
    }


    @Nullable
    @Override
    protected ValidationInfo doValidate() {
        if(StringUtils.isBlank(commentArea.getText())){
            return new ValidationInfo("Comment body can not be empty!", commentArea);
        }

        return null;
    }

    @Override
    protected void doOKAction() {
        if(nonNull(project)){
            new AddCommentTask(project, issueKey, commentArea.getText()).queue();
        }
        close(0);
    }

    private class AddCommentExecuteAction extends OkAction{

    }





}
