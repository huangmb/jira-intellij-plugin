package com.intellij.jira.ui.dialog;

import com.intellij.jira.tasks.AddCommentTask;
import com.intellij.jira.util.JiraLabelUtil;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.ComboBox;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.ui.ValidationInfo;
import com.intellij.ui.CollectionComboBoxModel;
import com.intellij.ui.JBColor;
import com.intellij.ui.ScrollPaneFactory;
import com.intellij.ui.components.JBPanel;
import com.intellij.util.ui.FormBuilder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;
import java.util.List;

import static com.intellij.openapi.util.text.StringUtil.isEmpty;
import static com.intellij.openapi.util.text.StringUtil.trim;
import static java.util.Objects.nonNull;
import static javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER;
import static javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED;

public class AddCommentDialog extends DialogWrapper {

    public static final String ALL_USERS = "All users";

    private Project project;
    private String issueKey;
    private List<String> projectRoles;

    private ComboBox<String> myComboBox;
    private CollectionComboBoxModel<String> myComboBoxItems;
    private JTextArea commentArea;

    public AddCommentDialog(@Nullable Project project, @NotNull String issueKey, List<String> projectRoles) {
        super(project, false);
        this.project = project;
        this.issueKey = issueKey;
        this.projectRoles = projectRoles;
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
        commentArea.setBorder(BorderFactory.createLineBorder(JBColor.border()));
        commentArea.setLineWrap(true);
        commentArea.setWrapStyleWord(true);
        panel.add(ScrollPaneFactory.createScrollPane(commentArea, VERTICAL_SCROLLBAR_AS_NEEDED, HORIZONTAL_SCROLLBAR_NEVER), BorderLayout.CENTER);

        projectRoles.add(0, ALL_USERS);

        myComboBoxItems = new CollectionComboBoxModel(projectRoles);
        myComboBox = new ComboBox(myComboBoxItems);

        return FormBuilder.createFormBuilder()
                .addComponent(panel)
                .addLabeledComponent(JiraLabelUtil.createLabel("Viewable by"), myComboBox)
                .getPanel();
    }


    @NotNull
    @Override
    protected Action[] createActions() {
        return new Action[]{new AddCommentExecuteAction(), myCancelAction};
    }


    @Nullable
    @Override
    public JComponent getPreferredFocusedComponent() {
        return commentArea;
    }

    @Nullable
    @Override
    protected ValidationInfo doValidate() {
        if(isEmpty(trim(commentArea.getText()))){
            return new ValidationInfo("Comment body can not be empty!", commentArea);
        }

        return null;
    }

    @Override
    protected void doOKAction() {
        if(nonNull(project)){
            new AddCommentTask(project, issueKey, escapeComment(), (String) myComboBox.getSelectedItem()).queue();
        }
        close(0);
    }

    private class AddCommentExecuteAction extends OkAction{

    }


    private String escapeComment(){
        return commentArea.getText().replace("\n", "\\r\\n");
    }



}
