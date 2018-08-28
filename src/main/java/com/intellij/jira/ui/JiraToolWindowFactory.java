package com.intellij.jira.ui;

import com.intellij.ide.DataManager;
import com.intellij.jira.rest.model.JiraIssue;
import com.intellij.jira.tasks.JiraServer;
import com.intellij.jira.tasks.JiraTaskManager;
import com.intellij.jira.ui.panels.JiraIssueDetailsPanel;
import com.intellij.jira.ui.panels.JiraIssuesPanel;
import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.intellij.openapi.wm.ToolWindowType;
import com.intellij.ui.JBSplitter;
import com.intellij.ui.SimpleColoredComponent;
import com.intellij.ui.SimpleTextAttributes;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentManager;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.Optional;

public class JiraToolWindowFactory implements ToolWindowFactory {

    public static final String TOOL_WINDOW_ID = "JIRA";
    public static final String TAB_ISSUES = "Issues";

    private JBSplitter splitter;
    private JiraIssuesPanel issuesPanel;
    private JiraIssueDetailsPanel issueDetailsPanel;

    @Override
    public void createToolWindowContent(@NotNull Project project, @NotNull ToolWindow toolWindow) {
        createContent(project, toolWindow);

        project.getComponent(JiraTaskManager.class).addConfigurationServerChangedListener(() -> {
            SwingUtilities.invokeLater(() -> {
                createContent(project, toolWindow);
            });
        });


        toolWindow.setType(ToolWindowType.DOCKED, null);
    }

    private void createContent(Project project, ToolWindow toolWindow) {
        ContentManager contentManager = toolWindow.getContentManager();
        contentManager.removeAllContents(true);
        Content content = null;

        Optional<JiraServer> jiraServer =  project.getComponent(JiraTaskManager.class).getConfiguredJiraServer();
        if(!jiraServer.isPresent()){
            content = contentManager.getFactory().createContent(createPlaceHolderPanel(), TAB_ISSUES, false);
        }
        else{

            this.splitter = new JBSplitter();
            splitter.setProportion(0.6f);

            List<JiraIssue> issues = jiraServer.get().getIssues();
            this.issuesPanel = new JiraIssuesPanel(issues);
            this.splitter.setFirstComponent(this.issuesPanel);

            this.issueDetailsPanel = new JiraIssueDetailsPanel(issues.get(0));
            this.splitter.setSecondComponent(this.issueDetailsPanel);

            issuesPanel.getTable().getSelectionModel().addListSelectionListener(event -> {
                SwingUtilities.invokeLater(() -> {
                   JiraIssue selectedObject = (JiraIssue) issuesPanel.getTable().getSelectedObject();
                    issueDetailsPanel.updateIssue(selectedObject);
                });
            });

            content = contentManager.getFactory().createContent(this.splitter, TAB_ISSUES,false);
            contentManager.addDataProvider(this.issuesPanel);
        }

        contentManager.addContent(content);
    }

    private static JComponent createPlaceHolderPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        JPanel labelPanel = new JPanel();
        JLabel messageLabel = new JLabel("No Jira server found");
        JComponent configureLabel = createLink("Configure");
        messageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        messageLabel.setAlignmentY(Component.CENTER_ALIGNMENT);
        configureLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        configureLabel.setAlignmentY(Component.CENTER_ALIGNMENT);
        labelPanel.add(messageLabel);
        labelPanel.add(configureLabel);
        panel.add(labelPanel, BorderLayout.CENTER);

        return panel;
    }

    private static JComponent createLink(String text) {
        SimpleColoredComponent label = new SimpleColoredComponent();
        label.append(text, SimpleTextAttributes.LINK_ATTRIBUTES);
        label.setCursor(new Cursor(Cursor.HAND_CURSOR));
        label.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                AnAction action = ActionManager.getInstance().getAction("tasks.configure.servers");
                DataContext context = DataManager.getInstance().getDataContextFromFocus().getResult();
                AnActionEvent event = AnActionEvent.createFromAnAction(action, null, ActionPlaces.UNKNOWN, context);
                action.actionPerformed(event);
            }
        });

        return label;
    }


}
