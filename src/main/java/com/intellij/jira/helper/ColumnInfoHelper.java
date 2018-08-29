package com.intellij.jira.helper;

import com.intellij.jira.rest.model.JiraIssue;
import com.intellij.jira.util.JiraIconUtil;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.ui.table.TableView;
import com.intellij.util.ui.ColumnInfo;
import com.intellij.util.ui.table.IconTableCellRenderer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.util.List;

import static com.intellij.jira.util.JiraIssueUtil.*;

public class ColumnInfoHelper {

    private static ColumnInfoHelper helper;

    private ColumnInfoHelper(){ }

    @NotNull
    public static ColumnInfoHelper getHelper(){
        if(helper == null){
            helper = new ColumnInfoHelper();
        }

        return helper;
    }

    @NotNull
    public ColumnInfo[] generateColumnsInfo(@NotNull List<JiraIssue> issues, @NotNull TableView<JiraIssue> issueTable) {

        ColumnInfoHelper.ItemAndWidth key = new ColumnInfoHelper.ItemAndWidth("", 0);
        ColumnInfoHelper.ItemAndWidth summary = new ColumnInfoHelper.ItemAndWidth("", 0);
        ColumnInfoHelper.ItemAndWidth assignee = new ColumnInfoHelper.ItemAndWidth("", 0);
        ColumnInfoHelper.ItemAndWidth type = new ColumnInfoHelper.ItemAndWidth("", 0);
        ColumnInfoHelper.ItemAndWidth priority = new ColumnInfoHelper.ItemAndWidth("", 0);
        ColumnInfoHelper.ItemAndWidth status = new ColumnInfoHelper.ItemAndWidth("", 0);
        ColumnInfoHelper.ItemAndWidth created = new ColumnInfoHelper.ItemAndWidth("", 0);


        for (JiraIssue issue : issues){
            key = this.getMax(key, getKey(issue), issueTable);
            summary = this.getMax(summary, getSummary(issue), issueTable);
            assignee = this.getMax(assignee, getAssignee(issue), issueTable);
            type = this.getMax(type, getType(issue), issueTable);
            priority = this.getMax(priority, getPriority(issue), issueTable);
            status = this.getMax(status, getStatus(issue), issueTable);
            created = this.getMax(created, getCreated(issue), issueTable);
        }

        ColumnInfo[] columns = new ColumnInfo[]{new ColumnInfoHelper.JiraIssueColumnInfo("Key", key.myItem) {
            public String valueOf(JiraIssue issue) {
                return getKey(issue);
            }
        }, new ColumnInfoHelper.JiraIssueColumnInfo("Summary", summary.myItem) {
            public String valueOf(JiraIssue issue) {
                return getSummary(issue);
            }
        }, new ColumnInfoHelper.JiraIssueColumnInfo("Assignee", assignee.myItem) {
            public String valueOf(JiraIssue issue) {
                return getAssignee(issue);
            }
        }, new ColumnInfoHelper.IssueTypeColumnInfo("Type")
                , new ColumnInfoHelper.PriorityColumnInfo("Priority")
                , new ColumnInfoHelper.StatusColumnInfo("Status")
                , new ColumnInfoHelper.JiraIssueColumnInfo("Created", created.myItem) {
            public String valueOf(JiraIssue issue) {
                return getCreated(issue);
            }
        }};

        return columns;
    }

    private ColumnInfoHelper.ItemAndWidth getMax(ColumnInfoHelper.ItemAndWidth current, String candidate, TableView<JiraIssue> issueTable) {
        int width = issueTable.getFontMetrics(issueTable.getFont()).stringWidth(candidate);
        return width > current.myWidth ? new ColumnInfoHelper.ItemAndWidth(candidate, width) : current;
    }



    private static class IconAndTextTableCellRenderer extends IconTableCellRenderer {

        private String iconUrl;
        private String label;

        public void setIconUrl(String iconUrl) {
            this.iconUrl = iconUrl;
        }

        public void setLabel(String label) {
            this.label = StringUtil.isNotEmpty(label) ? label : "";
        }

        public void emptyText(){
            this.label = "";
        }

        @Nullable
        @Override
        protected Icon getIcon(@NotNull Object value, JTable table, int row) {
            return JiraIconUtil.getSmallIcon(iconUrl);
        }

        @Override
        protected boolean isCenterAlignment() {
            return true;
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean selected, boolean focus, int row, int column) {
            super.getTableCellRendererComponent(table, value, selected, focus, row, column);
            setText(label);
            return this;
        }
    }

    private static class IssueTypeColumnInfo extends ColumnInfo<JiraIssue, String>{
        private final TableCellRenderer ICON_AND_TEXT_RENDERER = new ColumnInfoHelper.IconAndTextTableCellRenderer();

        IssueTypeColumnInfo(String name) {
            super(name);
        }

        @Nullable
        @Override
        public String valueOf(JiraIssue issue) {
            return issue.getIssuetype().getName();
        }

        @Nullable
        @Override
        public TableCellRenderer getRenderer(JiraIssue issue) {
            return ICON_AND_TEXT_RENDERER;
        }

        @Override
        public TableCellRenderer getCustomizedRenderer(JiraIssue issue, TableCellRenderer renderer) {
            if(renderer instanceof ColumnInfoHelper.IconAndTextTableCellRenderer){
                ((ColumnInfoHelper.IconAndTextTableCellRenderer) renderer).setIconUrl(issue.getIssuetype().getIconUrl());
                ((ColumnInfoHelper.IconAndTextTableCellRenderer) renderer).emptyText();
                ((ColumnInfoHelper.IconAndTextTableCellRenderer) renderer).setToolTipText(valueOf(issue));
                ((ColumnInfoHelper.IconAndTextTableCellRenderer) renderer).setAlignmentX(Component.CENTER_ALIGNMENT);
            }

            return renderer;
        }

    }

    private static class PriorityColumnInfo extends ColumnInfo<JiraIssue, String>{
        private final TableCellRenderer ICON_AND_TEXT_RENDERER = new ColumnInfoHelper.IconAndTextTableCellRenderer();

        PriorityColumnInfo(String name) {
            super(name);
        }

        @Nullable
        @Override
        public String valueOf(JiraIssue issue) {
            return issue.getPriority().getName();
        }

        @Nullable
        @Override
        public TableCellRenderer getRenderer(JiraIssue issue) {
            return ICON_AND_TEXT_RENDERER;
        }

        @Override
        public TableCellRenderer getCustomizedRenderer(JiraIssue issue, TableCellRenderer renderer) {
            if(renderer instanceof ColumnInfoHelper.IconAndTextTableCellRenderer){
                ((ColumnInfoHelper.IconAndTextTableCellRenderer) renderer).setIconUrl(issue.getPriority().getIconUrl());
                ((ColumnInfoHelper.IconAndTextTableCellRenderer) renderer).emptyText();
                ((ColumnInfoHelper.IconAndTextTableCellRenderer) renderer).setToolTipText(valueOf(issue));
            }

            return renderer;
        }
    }

    private static class StatusColumnInfo extends ColumnInfo<JiraIssue, String>{
        private final TableCellRenderer ICON_AND_TEXT_RENDERER = new ColumnInfoHelper.IconAndTextTableCellRenderer();

        StatusColumnInfo(String name) {
            super(name);
        }

        @Nullable
        @Override
        public String valueOf(JiraIssue issue) {
            return issue.getStatus().getName();
        }

        @Nullable
        @Override
        public TableCellRenderer getRenderer(JiraIssue issue) {
            return ICON_AND_TEXT_RENDERER;
        }

        @Override
        public TableCellRenderer getCustomizedRenderer(JiraIssue issue, TableCellRenderer renderer) {
            if(renderer instanceof ColumnInfoHelper.IconAndTextTableCellRenderer){
                ((ColumnInfoHelper.IconAndTextTableCellRenderer) renderer).setIconUrl(issue.getStatus().getIconUrl());
                ((ColumnInfoHelper.IconAndTextTableCellRenderer) renderer).emptyText();
                ((ColumnInfoHelper.IconAndTextTableCellRenderer) renderer).setToolTipText(issue.getStatus().getName());
            }

            return renderer;
        }
    }

    private abstract static class JiraIssueColumnInfo extends ColumnInfo<JiraIssue, String> {
        @NotNull
        private final String myMaxString;

        JiraIssueColumnInfo(@NotNull String name, @NotNull String maxString) {
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

}
