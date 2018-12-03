package com.intellij.jira.ui.dialog;

import com.intellij.jira.components.JQLSearcherManager;
import com.intellij.jira.components.JQLSearcherObserver;
import com.intellij.jira.rest.model.jql.JQLSearcher;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.ui.ValidationInfo;
import com.intellij.openapi.util.text.StringUtil;
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

import static com.intellij.openapi.util.text.StringUtil.*;
import static java.util.Objects.nonNull;

public class EditJQLSearcherDialog extends DialogWrapper {

    protected Project myProject;

    private JBLabel myAliasLabel;
    protected JBTextField myAliasField;

    private JBLabel mySearchLabel;
    protected EditorTextField mySearchQueryField;

    protected JCheckBox mySetDefaultCheckBox;

    private final JQLSearcher myJQLSearcher;

    public EditJQLSearcherDialog(@NotNull Project project, @Nullable JQLSearcher searcher) {
        super(project, false);
        this.myProject = project;
        this.myJQLSearcher = searcher;

        setTitle("Edit JQL Searcher");
        init();
    }


    @Nullable
    @Override
    protected JComponent createCenterPanel() {
        this.myAliasLabel = new JBLabel("Alias:", 4);
        this.myAliasField = new JBTextField(getAlias());
        this.myAliasField.setPreferredSize(UI.size(300, 24));

        this.mySearchLabel = new JBLabel("Search:", 4);
        this.mySearchQueryField = new LanguageTextField(JqlLanguage.INSTANCE, this.myProject, getJql());
        this.mySearchQueryField.setPreferredSize(UI.size(300, 24));

        this.mySetDefaultCheckBox = new JCheckBox("Set Default");
        this.mySetDefaultCheckBox.setBorder(JBUI.Borders.emptyRight(4));
        this.mySetDefaultCheckBox.setSelected(isDefault());


        return FormBuilder.createFormBuilder()
                .addLabeledComponent(this.myAliasLabel, this.myAliasField)
                .addLabeledComponent(this.mySearchLabel, this.mySearchQueryField)
                .addComponent(mySetDefaultCheckBox)
                .getPanel();
    }


    private String getAlias(){
        return nonNull(myJQLSearcher) ? myJQLSearcher.getAlias() : "";
    }

    private String getJql(){
        return nonNull(myJQLSearcher) ? myJQLSearcher.getJql() : "";
    }

    private boolean isDefault(){
        return nonNull(myJQLSearcher) && myJQLSearcher.isDefault();
    }




    @Nullable
    @Override
    protected ValidationInfo doValidate() {
        if(isEmpty(trim(myAliasField.getText()))){
            return new ValidationInfo("Alias field is required");
        }

        if(myProject.getComponent(JQLSearcherManager.class).alreadyExistJQLSearcherWithAlias(trim(myAliasField.getText()))){
            return new ValidationInfo("Alias already exist");
        }

        if(isEmpty(trim(mySearchQueryField.getText()))){
            return new ValidationInfo("JQL field is required");
        }

        return null;
    }

    @Override
    protected void doOKAction() {
        if(nonNull(myProject)){
            JQLSearcher jqlSearcher = new JQLSearcher(myAliasField.getText(), mySearchQueryField.getText(), mySetDefaultCheckBox.isSelected());
            JQLSearcherManager jqlManager = myProject.getComponent(JQLSearcherManager.class);
            // TODO: 03/12/2018

        }
        

        super.doOKAction();
    }
}
