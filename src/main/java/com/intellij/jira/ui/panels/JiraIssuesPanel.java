package com.intellij.jira.ui.panels;

import com.intellij.jira.components.JiraActionManager;
import com.intellij.jira.rest.model.JiraIssue;
import com.intellij.openapi.actionSystem.ActionGroup;
import com.intellij.openapi.actionSystem.ActionManager;
import com.intellij.openapi.actionSystem.ActionToolbar;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.ui.SimpleToolWindowPanel;
import com.intellij.tools.SimpleActionGroup;
import com.intellij.ui.ScrollPaneFactory;
import com.intellij.ui.table.TableView;
import com.intellij.util.text.DateFormatUtil;
import com.intellij.util.ui.ColumnInfo;
import com.intellij.util.ui.ListTableModel;
import org.fest.util.Lists;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.util.List;

import static com.intellij.jira.ui.JiraToolWindowFactory.TOOL_WINDOW_ID;
import static java.util.Objects.nonNull;
import static javax.swing.ListSelectionModel.SINGLE_SELECTION;

public class JiraIssuesPanel extends SimpleToolWindowPanel {

    private ActionToolbar toolbar;
    private final TableView<JiraIssue> myTable;

    public JiraIssuesPanel(List<JiraIssue> issues) {
        super(false, true);
        setToolbar();
        Box toolBarBox = Box.createHorizontalBox();
        toolBarBox.add(toolbar.getComponent());
        super.setToolbar(toolBarBox);
        toolbar.getComponent().setVisible(true);

        this.myTable = new TableView();
        updateModel(issues);
        this.myTable.setSelectionMode(SINGLE_SELECTION);
        this.myTable.setStriped(true);
        this.myTable.setGridColor(Color.getHSBColor(205, 33, 83));
        this.myTable.setShowGrid(false);
        this.myTable.setRowHeight(25);


        JPanel issuesPanel = new JPanel(new BorderLayout());
        issuesPanel.add(ScrollPaneFactory.createScrollPane(this.myTable), BorderLayout.CENTER);

        super.setContent(issuesPanel);


    }

    private void updateModel(List<com.intellij.jira.rest.model.JiraIssue> issues) {
        this.myTable.setModelAndUpdateColumns(new ListTableModel(this.generateColumnsInfo(issues), issues, 0));
    }

    public TableView getTable(){
        return this.myTable;
    }

    @NotNull
    private ColumnInfo[] generateColumnsInfo(@NotNull List<JiraIssue> issues) {

        JiraIssuesPanel.ItemAndWidth key = new JiraIssuesPanel.ItemAndWidth("", 0);
        JiraIssuesPanel.ItemAndWidth summary = new JiraIssuesPanel.ItemAndWidth("", 0);
        JiraIssuesPanel.ItemAndWidth assignee = new JiraIssuesPanel.ItemAndWidth("", 0);
        JiraIssuesPanel.ItemAndWidth type = new JiraIssuesPanel.ItemAndWidth("", 0);
        JiraIssuesPanel.ItemAndWidth priority = new JiraIssuesPanel.ItemAndWidth("", 0);
        JiraIssuesPanel.ItemAndWidth status = new JiraIssuesPanel.ItemAndWidth("", 0);
        JiraIssuesPanel.ItemAndWidth created = new JiraIssuesPanel.ItemAndWidth("", 0);


        for (JiraIssue issue : issues){
            key = this.getMax(key, getKey(issue));
            summary = this.getMax(summary, getSummary(issue));
            assignee = this.getMax(assignee, getAssignee(issue));
            type = this.getMax(type, getType(issue));
            priority = this.getMax(priority, getPriority(issue));
            status = this.getMax(status, getStatus(issue));
            created = this.getMax(created, getCreated(issue));
        }

        ColumnInfo[] columns = new ColumnInfo[]{new JiraIssuesPanel.JiraIssueColumnInfo("Key", key.myItem) {
            public String valueOf(JiraIssue issue) {
              return getKey(issue);
            }
        }, new JiraIssuesPanel.JiraIssueColumnInfo("Summary", summary.myItem) {
            public String valueOf(JiraIssue issue) {
                return getSummary(issue);
            }
        }, new JiraIssuesPanel.JiraIssueColumnInfo("Assignee", assignee.myItem) {
            public String valueOf(JiraIssue issue) {
                return getAssignee(issue);
            }
        },new JiraIssuesPanel.JiraIssueColumnInfo("Type", type.myItem) {
            public String valueOf(JiraIssue issue) {
                return getType(issue);
            }
        },new JiraIssuesPanel.JiraIssueColumnInfo("Priority", priority.myItem) {
            public String valueOf(JiraIssue issue) {
                return getPriority(issue);
            }
        },new JiraIssuesPanel.JiraIssueColumnInfo("Status", status.myItem) {
            public String valueOf(JiraIssue issue) {
                return getStatus(issue);
            }
        },new JiraIssuesPanel.JiraIssueColumnInfo("Created", created.myItem) {
            public String valueOf(JiraIssue issue) {
                return getCreated(issue);
            }
        }};

        return columns;
    }

    private JiraIssuesPanel.ItemAndWidth getMax(JiraIssuesPanel.ItemAndWidth current, String candidate) {
        int width = this.myTable.getFontMetrics(this.myTable.getFont()).stringWidth(candidate);
        return width > current.myWidth ? new JiraIssuesPanel.ItemAndWidth(candidate, width) : current;
    }

    private static String getKey(JiraIssue jiraIssue) {
        return jiraIssue.getKey();
    }

    private static String getSummary(JiraIssue jiraIssue) {
        return jiraIssue.getSummary();
    }

    private static String getAssignee(JiraIssue jiraIssue) {
        return nonNull(jiraIssue.getAssignee()) ? jiraIssue.getAssignee().getName() : "";
    }

    private static String getType(JiraIssue jiraIssue) {
        return jiraIssue.getIssuetype().getName();
    }

    private static String getPriority(JiraIssue jiraIssue) {
        return nonNull(jiraIssue.getPriority()) ? jiraIssue.getPriority().getName() : "";
    }

    private static String getStatus(JiraIssue jiraIssue) {
        return jiraIssue.getStatus().getName();
    }

    private static String getCreated(JiraIssue jiraIssue) {
        return DateFormatUtil.formatPrettyDateTime(jiraIssue.getCreated());
    }

    private abstract static class JiraIssueColumnInfo extends ColumnInfo<JiraIssue, String> {
        @NotNull
        private final String myMaxString;

        public JiraIssueColumnInfo(@NotNull String name, @NotNull String maxString) {
            super(name);
            this.myMaxString = maxString;
        }

        public String getMaxStringValue() {
            return this.myMaxString;
        }

        public int getAdditionalWidth() {
            return 10;
        }
    }

    private static class ItemAndWidth {
        private final String myItem;
        private final int myWidth;

        private ItemAndWidth(String item, int width) {
            this.myItem = item;
            this.myWidth = width;
        }
    }

    private void setToolbar(){
        this.toolbar = ActionManager.getInstance().createActionToolbar(TOOL_WINDOW_ID, createActionGroup(), false);
        this.toolbar.setTargetComponent(this);
    }

    private ActionGroup createActionGroup(){
        SimpleActionGroup group = new SimpleActionGroup();
        getIssuePanelActions().forEach((group)::add);
        return group;
    }

    private List<AnAction> getIssuePanelActions(){
        return Lists.newArrayList(JiraActionManager.getInstance().getJiraIssuesRefreshAction(),
                                    ActionManager.getInstance().getAction("tasks.configure.servers"));
    }

}
