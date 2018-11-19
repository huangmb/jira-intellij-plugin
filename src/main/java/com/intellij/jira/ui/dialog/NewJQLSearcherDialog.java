package com.intellij.jira.ui.dialog;

import com.intellij.jira.components.JQLSearcherManager;
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
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

import static java.util.Objects.nonNull;

public class NewJQLSearcherDialog extends DialogWrapper {

    private Project myProject;

    private JBLabel myAliasLabel;
    private JBTextField myAliasField;

    private JBLabel mySearchLabel;
    private EditorTextField mySearchQueryField;

    private JCheckBox mySetDefaultCheckBox;

    public NewJQLSearcherDialog(@Nullable Project project) {
        super(project, false);
        this.myProject = project;

        init();
    }

    @Override
    protected void init() {
        setTitle("New JQL Searcher");
        super.init();
    }

    @Nullable
    @Override
    protected JComponent createCenterPanel() {

        this.myAliasLabel = new JBLabel("Alias:", 4);
        this.myAliasField = new JBTextField();

        this.mySearchLabel = new JBLabel("Search:", 4);
        this.mySearchQueryField = new LanguageTextField(JqlLanguage.INSTANCE, this.myProject, "");

        this.mySetDefaultCheckBox = new JCheckBox("Set Default");
        this.mySetDefaultCheckBox.setBorder(JBUI.Borders.emptyRight(4));

        return FormBuilder.createFormBuilder()
                .addLabeledComponent(this.myAliasLabel, this.myAliasField)
                .addLabeledComponent(this.mySearchLabel, this.mySearchQueryField)
                .addComponent(mySetDefaultCheckBox)
                .getPanel();
    }

    @Nullable
    @Override
    protected ValidationInfo doValidate() {
        // TODO

        return null;
    }

    @Override
    protected void doOKAction() {
        if(nonNull(myProject)){
            JQLSearcher jqlSearcher = new JQLSearcher(myAliasField.getText(), mySearchQueryField.getText(), mySetDefaultCheckBox.isSelected());
            myProject.getComponent(JQLSearcherManager.class).add(jqlSearcher);
            //update the comboBox
        }

        super.doOKAction();
    }
}
