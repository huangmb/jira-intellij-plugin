package com.intellij.jira.ui.dialog;

import com.intellij.jira.helper.TransitionFieldHelper;
import com.intellij.jira.helper.TransitionFieldHelper.FieldEditorInfo;
import com.intellij.jira.rest.model.JiraIssue;
import com.intellij.jira.rest.model.JiraIssueFieldProperties;
import com.intellij.jira.rest.model.JiraIssueTransition;
import com.intellij.jira.tasks.TransitIssueTask;
import com.intellij.jira.ui.JiraIssueTransitionListModel;
import com.intellij.jira.ui.renders.JiraIssueTransitionListCellRenderer;
import com.intellij.jira.util.JiraLabelUtil;
import com.intellij.jira.util.JiraPanelUtil;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.ui.ValidationInfo;
import com.intellij.tasks.jira.JiraRepository;
import com.intellij.ui.JBColor;
import com.intellij.ui.ScrollPaneFactory;
import com.intellij.ui.components.JBLabel;
import com.intellij.ui.components.JBList;
import com.intellij.ui.components.JBPanel;
import com.intellij.util.ui.FormBuilder;
import com.intellij.util.ui.JBDimension;
import com.intellij.util.ui.JBUI;
import com.intellij.util.ui.UI;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.intellij.jira.helper.TransitionFieldHelper.createCommentFieldEditorInfo;
import static com.intellij.jira.util.JiraPanelUtil.createPanelWithVerticalLine;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static javax.swing.ListSelectionModel.SINGLE_SELECTION;
import static javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER;
import static javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED;

public class IssueTransitionDialog extends DialogWrapper {

    private Project project;
    private JiraIssue issue;

    private List<JiraIssueTransition> transitions;
    private JiraIssueTransition selectedIssueTransition;

    private JPanel transitionsPanel;
    private JPanel transitionFieldsPanel;
    private JPanel transitionPreviewPanel;

    private Map<String, FieldEditorInfo> transitionFields = new HashMap<>();


    public IssueTransitionDialog(@Nullable Project project, @NotNull JiraIssue issue, List<JiraIssueTransition> transitions) {
        super(project, false);
        this.project = project;
        this.issue = issue;
        this.transitions = transitions;
        myOKAction = new TransitIssueExecuteAction().disabled();
        init();
    }

    @Override
    protected void init() {
        setTitle("Transit Issue " + issue.getKey());
        super.init();
    }

    @Nullable
    @Override
    protected JComponent createCenterPanel() {
        JBPanel panel = new JBPanel(new BorderLayout());
        transitionsPanel = new JBPanel(new BorderLayout());
        JBList<JiraIssueTransition> transitionList = new JBList<>();
        transitionList.setEmptyText("No transitions");
        transitionList.setModel(new JiraIssueTransitionListModel(transitions));
        transitionList.setCellRenderer(new JiraIssueTransitionListCellRenderer());
        transitionList.setSelectionMode(SINGLE_SELECTION);
        transitionList.setPreferredSize(new JBDimension(100, 300));
        transitionList.setBorder(BorderFactory.createLineBorder(JBColor.border()));
        transitionList.addListSelectionListener(e -> {
                SwingUtilities.invokeLater(() -> {
                    updateTransitionFieldPanel(transitionList.getSelectedValue());
                    updateTransitionPreviewPanel(transitionList.getSelectedValue());
                });
        });

        transitionsPanel.add(transitionList, BorderLayout.CENTER);

        transitionFieldsPanel = new JBPanel(new GridBagLayout());
        transitionFieldsPanel.setMinimumSize(UI.size(450, 300));
        transitionFieldsPanel.setBorder(JBUI.Borders.empty(5));
        transitionFieldsPanel.add(JiraPanelUtil.createPlaceHolderPanel("Select transition"), new GridBagConstraints());

        transitionPreviewPanel = new JBPanel(new BorderLayout());
        transitionPreviewPanel.setMinimumSize(UI.size(100, 300));

        panel.add(transitionsPanel, BorderLayout.WEST);
        panel.add(ScrollPaneFactory.createScrollPane(transitionFieldsPanel, VERTICAL_SCROLLBAR_AS_NEEDED, HORIZONTAL_SCROLLBAR_NEVER), BorderLayout.CENTER);
        panel.add(transitionPreviewPanel, BorderLayout.EAST);
        panel.setMinimumSize(UI.size(650, 300));

        return panel;
    }

    private void updateTransitionFieldPanel(JiraIssueTransition transition) {
        selectedIssueTransition = transition;
        myOKAction.setEnabled(true);

        List<JiraIssueFieldProperties> transitionFields = transition.getFields().entrySet().stream()
                .map(entry -> JiraRepository.GSON.fromJson(entry.getValue(), JiraIssueFieldProperties.class))
                .collect(Collectors.toList());


        this.transitionFields.clear();

        transitionFieldsPanel.removeAll();
        transitionFieldsPanel.setLayout(new GridBagLayout());

        if(transitionFields.isEmpty()){
            transitionFieldsPanel.add(JiraPanelUtil.createPlaceHolderPanel("No fields required"), new GridBagConstraints());
        }else{
            createTransitionFields(transitionFields);
        }


        transitionFieldsPanel.revalidate();
        transitionFieldsPanel.repaint();

    }


    private void updateTransitionPreviewPanel(JiraIssueTransition transition){
        transitionPreviewPanel.removeAll();

        JBPanel sourceStatusPanel = new JBPanel();
        sourceStatusPanel.setBorder(JBUI.Borders.empty(5));
        JBLabel sourceStatusLabel = JiraLabelUtil.createStatusLabel(issue.getStatus());
        sourceStatusPanel.add(sourceStatusLabel);

        JPanel verticalLinePanel = createPanelWithVerticalLine();

        JBPanel targetStatusPanel = new JBPanel();
        targetStatusPanel.setBorder(JBUI.Borders.empty(5));
        JBLabel targetStatusLabel = JiraLabelUtil.createStatusLabel(transition.getTo());
        targetStatusPanel.add(targetStatusLabel);

        transitionPreviewPanel.add(sourceStatusPanel, BorderLayout.PAGE_START);
        transitionPreviewPanel.add(verticalLinePanel, BorderLayout.CENTER);
        transitionPreviewPanel.add(targetStatusPanel, BorderLayout.PAGE_END);

        transitionPreviewPanel.revalidate();
        transitionPreviewPanel.repaint();
    }




    private void createTransitionFields(List<JiraIssueFieldProperties> transitionFields) {
        FormBuilder formBuilder = FormBuilder.createFormBuilder().setAlignLabelOnRight(true);

        transitionFields.forEach(fieldProperties -> {
            FieldEditorInfo info = TransitionFieldHelper.createFieldEditorInfo(fieldProperties, issue);
            this.transitionFields.put(info.getName(), info);

            formBuilder.addComponent(info.getPanel());
        });

        FieldEditorInfo commentInfo = createCommentFieldEditorInfo(issue.getKey());
        this.transitionFields.put(commentInfo.getName(), commentInfo);
        formBuilder.addComponent(commentInfo.getPanel());

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.anchor = GridBagConstraints.NORTH;
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.weighty = 1;


        transitionFieldsPanel.add(formBuilder.getPanel(), constraints);

    }

    @Nullable
    @Override
    protected ValidationInfo doValidate() {
        if(isNull(selectedIssueTransition)){
            return new ValidationInfo("You must select transition");
        }

        for(FieldEditorInfo info : transitionFields.values()){
            ValidationInfo fieldValidation = info.validateField();
            if(nonNull(fieldValidation)){
                return fieldValidation;
            }

        }

        return null;
    }


    @Override
    protected void doOKAction() {
        if(nonNull(project)){
            new TransitIssueTask(project, issue.getId(), selectedIssueTransition.getId(), transitionFields).queue();
        }

        close(0);
    }

    private class TransitIssueExecuteAction extends OkAction{

        public TransitIssueExecuteAction disabled(){
            setEnabled(false);
            return this;
        }
    }


}
