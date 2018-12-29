package com.intellij.jira.ui.editors;

import com.intellij.ide.DataManager;
import com.intellij.jira.server.JiraServerManager;
import com.intellij.jira.server.JiraRestApi;
import com.intellij.openapi.actionSystem.ActionPlaces;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.ui.ValidationInfo;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.ui.ScrollPaneFactory;
import com.intellij.ui.components.JBList;
import com.intellij.ui.components.JBPanel;
import com.intellij.util.ui.FormBuilder;
import com.intellij.util.ui.UI;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;
import java.awt.event.InputEvent;
import java.util.List;

import static java.awt.BorderLayout.CENTER;
import static java.util.Objects.nonNull;
import static javax.swing.ListSelectionModel.MULTIPLE_INTERVAL_SELECTION;
import static javax.swing.ListSelectionModel.SINGLE_SELECTION;
import static javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED;
import static javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED;

public abstract class SelectFieldEditor extends AbstractFieldEditor {

    protected JPanel myPanel;
    protected JTextField myTextField;
    protected JButton myButton;
    protected PickerDialogAction myButtonAction;
    protected boolean isMultiSelect;


    public SelectFieldEditor(String fieldName, String issueKey, boolean required, boolean isMultiSelect) {
        super(fieldName, issueKey, required);
        this.isMultiSelect = isMultiSelect;
    }

    @Override
    public JComponent createPanel() {
        this.myButton.addActionListener(e -> {
            InputEvent inputEvent = e.getSource() instanceof InputEvent ? (InputEvent)e.getSource() : null;
            myButtonAction.actionPerformed(AnActionEvent.createFromAnAction(myButtonAction, inputEvent, ActionPlaces.UNKNOWN, DataManager.getInstance().getDataContext(myTextField)));
        });

        return FormBuilder.createFormBuilder()
                .addLabeledComponent(this.myLabel, myPanel)
                .getPanel();
    }


    @Nullable
    @Override
    public ValidationInfo validate() {
        if(isRequired() && StringUtil.isEmpty(myTextField.getText())){
            return new ValidationInfo(myLabel.getMyLabelText() + " is required.");
        }

        return null;
    }

    abstract class PickerDialogAction extends AnAction{

        protected JiraRestApi myJiraRestApi;
        protected Project myProject;

        public PickerDialogAction() {
            super();
        }

        @Override
        public void actionPerformed(AnActionEvent e) {
            Project project = e.getProject();
            if(nonNull(project)){
                myProject = project;
                myJiraRestApi = project.getComponent(JiraServerManager.class).getJiraRestApi();
            }
        }
    }


    abstract class PickerDialog<E> extends DialogWrapper {

        protected JBList<E> myList;

        public PickerDialog(@Nullable Project project, @NotNull String title, List<E> items ) {
            super(project, false);
            setTitle(title);
            this.myList = new JBList(items);
            this.myList.setPreferredSize(getPreferredSizeList());
            this.myList.setSelectionMode(isMultiSelect ? MULTIPLE_INTERVAL_SELECTION: SINGLE_SELECTION);

            init();
        }

        public Dimension getPreferredSizeList(){
            return UI.size(75, 250);
        }

        @Nullable
        @Override
        protected JComponent createCenterPanel() {
            JBPanel panel = new JBPanel(new BorderLayout());
            panel.setPreferredSize(UI.size(100, 250));
            panel.add(ScrollPaneFactory.createScrollPane(myList, VERTICAL_SCROLLBAR_AS_NEEDED, HORIZONTAL_SCROLLBAR_AS_NEEDED), CENTER);


            return panel;
        }


    }








}
