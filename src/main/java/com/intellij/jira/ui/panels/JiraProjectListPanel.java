package com.intellij.jira.ui.panels;

import com.intellij.jira.actions.GoToIssuePopupAction;
import com.intellij.jira.actions.JiraIssueActionGroup;
import com.intellij.jira.components.JiraActionManager;
import com.intellij.jira.rest.model.JiraProject;
import com.intellij.jira.tasks.JiraServer;
import com.intellij.jira.ui.JiraProjectListModel;
import com.intellij.jira.ui.renders.JiraProjectListCellRender;
import com.intellij.openapi.actionSystem.ActionGroup;
import com.intellij.openapi.actionSystem.ActionManager;
import com.intellij.openapi.actionSystem.ActionToolbar;
import com.intellij.openapi.actionSystem.Separator;
import com.intellij.openapi.ui.SimpleToolWindowPanel;
import com.intellij.ui.IdeBorderFactory;
import com.intellij.ui.SideBorder;
import com.intellij.ui.components.JBList;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.util.List;

import static com.intellij.jira.ui.JiraToolWindowFactory.TOOL_WINDOW_ID;
import static javax.swing.ListSelectionModel.SINGLE_SELECTION;

public class JiraProjectListPanel extends SimpleToolWindowPanel {

    private final JiraServer jiraServer;
    private JBList<JiraProject> projectList;
    private JiraProjectPreviewPanel projectPreviewPanel;

    public JiraProjectListPanel(@NotNull JiraServer jiraServer, @NotNull JiraProjectPreviewPanel projectPreviewPanel) {
        super(true, true);
        this.jiraServer = jiraServer;
        this.projectPreviewPanel = projectPreviewPanel;
        setBorder(IdeBorderFactory.createBorder(SideBorder.RIGHT));
        init();
    }

    private void init() {
        initToolbar();
        initContent();
    }

    private void initToolbar() {
        ActionToolbar actionToolbar = ActionManager.getInstance().createActionToolbar(TOOL_WINDOW_ID, createActionGroup(), true);
        actionToolbar.setTargetComponent(this);
        Box toolBarBox = Box.createHorizontalBox();
        toolBarBox.add(actionToolbar.getComponent());
        super.setToolbar(toolBarBox);
    }

    private ActionGroup createActionGroup(){
        JiraIssueActionGroup group = new JiraIssueActionGroup(this);
        group.add(JiraActionManager.getInstance().getJiraIssuesRefreshAction());
        group.add(new GoToIssuePopupAction());
        group.add(Separator.getInstance());
        group.add(ActionManager.getInstance().getAction("tasks.configure.servers"));
        return group;
    }


    private void initContent() {

        List<JiraProject> projects = jiraServer.getProjects();

        projectList = new JBList<>();
        projectList.setEmptyText("No projects");
        projectList.setModel(new JiraProjectListModel(projects));
        projectList.setCellRenderer(new JiraProjectListCellRender());
        projectList.setSelectionMode(SINGLE_SELECTION);
        projectList.addListSelectionListener(event -> {
            SwingUtilities.invokeLater(() -> this.projectPreviewPanel.showProject(projectList.getSelectedValue()));
        });


       /* JBSplitter splitter = new JBSplitter();
        splitter.setProportion(0.4f);
        splitter.setFirstComponent(projectList);
        splitter.setSecondComponent(projectPreviewPanel);
        splitter.setShowDividerIcon(false);
        splitter.setDividerWidth(2);*/


        super.setContent(projectList);

    }

}
