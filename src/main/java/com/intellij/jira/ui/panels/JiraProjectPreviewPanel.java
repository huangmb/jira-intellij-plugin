package com.intellij.jira.ui.panels;


import com.intellij.jira.actions.VersionPreviewDetailsAction;
import com.intellij.jira.actions.VersionUpdateAction;
import com.intellij.jira.actions.VersionsComboBoxAction;
import com.intellij.jira.rest.model.JiraProject;
import com.intellij.jira.rest.model.JiraProjectVersionDetails;
import com.intellij.jira.tasks.JiraServer;
import com.intellij.jira.util.JiraLabelUtil;
import com.intellij.jira.util.JiraPanelUtil;
import com.intellij.jira.util.ListWrapper;
import com.intellij.openapi.actionSystem.ActionGroup;
import com.intellij.openapi.actionSystem.ActionManager;
import com.intellij.openapi.actionSystem.ActionToolbar;
import com.intellij.openapi.actionSystem.DefaultActionGroup;
import com.intellij.openapi.ui.SimpleToolWindowPanel;
import com.intellij.ui.*;
import com.intellij.ui.components.JBLabel;
import com.intellij.ui.components.JBPanel;
import com.intellij.util.ui.FormBuilder;
import com.intellij.util.ui.JBUI;
import com.intellij.util.ui.UI;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.util.List;

import static com.intellij.jira.ui.JiraToolWindowFactory.TOOL_WINDOW_ID;
import static com.intellij.jira.util.JiraLabelUtil.BOLD;
import static com.intellij.jira.util.JiraPanelUtil.MARGIN_BOTTOM;
import static java.awt.BorderLayout.CENTER;
import static java.awt.BorderLayout.LINE_START;
import static javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED;
import static javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED;
import static javax.swing.SwingConstants.RIGHT;

public class JiraProjectPreviewPanel extends SimpleToolWindowPanel {

    private JiraServer jiraServer;
    private JiraProject project;
    private List<JiraProjectVersionDetails> verionsDetails;


    public JiraProjectPreviewPanel(@NotNull JiraServer jiraServer) {
        super(true, true);
        this.jiraServer = jiraServer;
        setBackground(JBColor.white);
        setContent(JiraPanelUtil.createPlaceHolderPanel("Select project to view details"));
    }


    public void showProject(@NotNull JiraProject project){
        this.project = project;
        this.verionsDetails = jiraServer.getProjectVersionDetails(project.getKey());
        setContent(createProjectWithVersionsPanel());
    }


    private JComponent createProjectWithVersionsPanel(){
        JBSplitter splitter = new JBSplitter();
        splitter.setProportion(0.5f);
        JComponent leftPanel = new ProjectDetailsPanel(project);
        leftPanel.setBorder(IdeBorderFactory.createBorder(SideBorder.RIGHT));
        splitter.setFirstComponent(leftPanel);
        splitter.setSecondComponent(new VersionsDetailsPanel(verionsDetails));
        splitter.setShowDividerIcon(false);
        splitter.setDividerWidth(1);

        return splitter;
    }


    public class ProjectDetailsPanel extends SimpleToolWindowPanel{

        private JiraProject project;

        public ProjectDetailsPanel(@NotNull JiraProject project) {
            super(true, true);
            this.project = project;
            init();
        }

        private void init() {
            initToolbar();
            initContent();
        }

        private void initContent() {
            JBPanel previewPanel = new JBPanel(new BorderLayout())
                    .withBackground(JBColor.WHITE)
                    .withBorder(JBUI.Borders.empty(1, 5, 1, 0));


            FormBuilder formBuilder = FormBuilder.createFormBuilder().setVerticalGap(0);

            // Key and name
            JBPanel keyAndNamePanel = JiraPanelUtil.createWhitePanel(new BorderLayout()).withBorder(MARGIN_BOTTOM);
            keyAndNamePanel.setPreferredSize(UI.size(450, 34));
            JBLabel keyAndNameLabel = JiraLabelUtil.createLabel(String.format("(%s) - %s", project.getKey(), project.getName()));
            keyAndNamePanel.add(keyAndNameLabel, LINE_START);


            // Type and lead
            JBPanel typeAndLeadPanel = JiraPanelUtil.createWhitePanel(new GridLayout(1, 2)).withBorder(MARGIN_BOTTOM);
            typeAndLeadPanel.setPreferredSize(UI.size(450, 34));
            JBPanel typePanel = JiraPanelUtil.createWhitePanel(new BorderLayout());
            JBLabel typeLabel = JiraLabelUtil.createLabel("Type: ").withFont(BOLD);
            JBLabel typeValueLabel = JiraLabelUtil.createLabel(project.getProjectTypeKey());

            typePanel.add(typeLabel, LINE_START);
            typePanel.add(typeValueLabel, CENTER);

            JBPanel leadPanel = JiraPanelUtil.createWhitePanel(new BorderLayout());
            JBLabel leadLabel = JiraLabelUtil.createLabel("Lead: ").withFont(BOLD);
            JBLabel leadValueLabel = JiraLabelUtil.createLabel(project.getLeadName());

            leadPanel.add(leadLabel, LINE_START);
            leadPanel.add(leadValueLabel, CENTER);

            typeAndLeadPanel.add(typePanel);
            typeAndLeadPanel.add(leadPanel);

            formBuilder.addComponent(keyAndNamePanel);
            formBuilder.addComponent(typeAndLeadPanel);

            // Roles
            // TODO

            JBPanel projectDetails = new JBPanel(new GridBagLayout()).withBackground(JBColor.WHITE);
            GridBagConstraints constraints = new GridBagConstraints();
            constraints.anchor = GridBagConstraints.NORTH;
            constraints.gridx = 0;
            constraints.gridy = 0;
            constraints.weightx = 1;
            constraints.weighty = 1;
            projectDetails.add(formBuilder.getPanel(), constraints);

            previewPanel.add(ScrollPaneFactory.createScrollPane(projectDetails, VERTICAL_SCROLLBAR_AS_NEEDED, HORIZONTAL_SCROLLBAR_AS_NEEDED), CENTER);

            setContent(previewPanel);
        }

        private void initToolbar() {
            ActionToolbar actionToolbar = ActionManager.getInstance().createActionToolbar(TOOL_WINDOW_ID, createActionGroup(), true);
            actionToolbar.setTargetComponent(this);

            Box toolBarBox = Box.createHorizontalBox();
            toolBarBox.add(actionToolbar.getComponent());
            setToolbar(toolBarBox);
        }

        private ActionGroup createActionGroup() {
            DefaultActionGroup group = new DefaultActionGroup();
            group.add(new VersionPreviewDetailsAction());

            return group;
        }


    }



    public class VersionsDetailsPanel extends SimpleToolWindowPanel{

        private ListWrapper<JiraProjectVersionDetails> versions;

        public VersionsDetailsPanel(List<JiraProjectVersionDetails> versions) {
            super(true, true);
            this.versions = new ListWrapper<>(versions);
            init();
        }

        private void init(){
            if(versions.isNotEmpty()){
                JiraProjectVersionDetails version = versions.getFirst();
                initToolbar();
                initContent(version);
            }
            else{
                setContent(JiraPanelUtil.createPlaceHolderPanel("No versions"));
            }
        }

        private void initToolbar() {
            ActionToolbar actionToolbar = ActionManager.getInstance().createActionToolbar(TOOL_WINDOW_ID, createActionGroup(), true);
            actionToolbar.setTargetComponent(this);

            Box toolBarBox = Box.createHorizontalBox();
            toolBarBox.add(actionToolbar.getComponent());
            setToolbar(toolBarBox);
        }

        private ActionGroup createActionGroup() {
            DefaultActionGroup group = new DefaultActionGroup();
            VersionsComboBoxAction versionsAction = new VersionsComboBoxAction();
            versions.getValues().forEach(v -> versionsAction.addAction(v.getId(), new VersionUpdateAction(v.getName())));
            group.add(versionsAction);

            return group;
        }

        private void initContent(JiraProjectVersionDetails version) {
            JBPanel previewPanel = new JBPanel(new BorderLayout())
                    .withBackground(JBColor.WHITE)
                    .withBorder(JBUI.Borders.empty(1, 5, 1, 0));

            FormBuilder formBuilder = FormBuilder.createFormBuilder().setVerticalGap(0);

            // Open
            JBPanel openPanel = JiraPanelUtil.createWhitePanel(new GridLayout(1, 2)).withBorder(MARGIN_BOTTOM);
            openPanel.setPreferredSize(UI.size(500, 34));
            JBLabel openLabel = JiraLabelUtil.createBoldLabel("To Do");
            JBLabel openValue = JiraLabelUtil.createLabel(version.getStatus().getToDo().getCount(), RIGHT);

            openPanel.add(openLabel);
            openPanel.add(openValue);

            // In progress
            JBPanel inProgressPanel = JiraPanelUtil.createWhitePanel(new GridLayout(1, 2)).withBorder(MARGIN_BOTTOM);
            inProgressPanel.setPreferredSize(UI.size(450, 34));
            JBLabel inProgressLabel = JiraLabelUtil.createBoldLabel("In Progress");
            JBLabel inProgressValue = JiraLabelUtil.createLabel(version.getStatus().getInProgress().getCount(), RIGHT);

            inProgressPanel.add(inProgressLabel);
            inProgressPanel.add(inProgressValue);

            // Done
            JBPanel donePanel = JiraPanelUtil.createWhitePanel(new GridLayout(1, 2)).withBorder(MARGIN_BOTTOM);
            donePanel.setPreferredSize(UI.size(420, 34));
            JBLabel doneLabel = JiraLabelUtil.createBoldLabel("Done");
            JBLabel doneValue = JiraLabelUtil.createLabel(version.getStatus().getComplete().getCount(), RIGHT);

            donePanel.add(doneLabel);
            donePanel.add(doneValue);


            formBuilder.addComponent(openPanel);
            formBuilder.addComponent(inProgressPanel);
            formBuilder.addComponent(donePanel);

            JBPanel versionDetails = new JBPanel(new GridBagLayout()).withBackground(JBColor.WHITE);
            GridBagConstraints c = new GridBagConstraints();
            c.anchor = GridBagConstraints.NORTH;
            c.gridx = 0;
            c.gridy = 0;
            c.weighty = 1;
            versionDetails.add(formBuilder.getPanel(), c);


            previewPanel.add(ScrollPaneFactory.createScrollPane(versionDetails, VERTICAL_SCROLLBAR_AS_NEEDED, HORIZONTAL_SCROLLBAR_AS_NEEDED));

            setContent(previewPanel);

        }


    }



}
