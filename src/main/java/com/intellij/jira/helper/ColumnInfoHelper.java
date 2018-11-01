package com.intellij.jira.helper;

import com.intellij.jira.rest.model.JiraIssue;
import com.intellij.jira.ui.renders.JiraIconAndTextTableCellRenderer;
import com.intellij.jira.ui.renders.JiraIssueStatusTableCellRenderer;
import com.intellij.jira.ui.renders.JiraIssueTableCellRenderer;
import com.intellij.util.ui.ColumnInfo;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;

import static com.intellij.jira.util.JiraIssueUtil.*;
import static java.util.Objects.nonNull;

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
    public ColumnInfo[] generateColumnsInfo() {
        return new ColumnInfo[]{new KeyColumnInfo("Key"),
                new JiraIssueColumnInfo("Summary") {
            public String valueOf(JiraIssue issue) {
                return getSummary(issue);
            }

            @Nullable
            @Override
            public String getMaxStringValue() {
                return "";
            }

            @Override
            public int getAdditionalWidth() {
                return 400;
            }
        },
           new JiraIssueColumnInfo("Assignee") {
            public String valueOf(JiraIssue issue) {
                return getAssignee(issue);
            }

            @Nullable
            @Override
            public String getMaxStringValue() {
                return "";
            }

            @Override
            public int getAdditionalWidth() {
                return 70;
            }

        }, new IssueTypeColumnInfo("Type")
         , new PriorityColumnInfo("Priority")
         , new StatusColumnInfo("Status")
         , new CreatedColumnInfo("Created")
        };
    }






    private abstract static class AbstractColumnInfo extends ColumnInfo<JiraIssue, String>{
        private static final TableCellRenderer ICON_AND_TEXT_RENDERER = new JiraIconAndTextTableCellRenderer();
        private String columnName;

        AbstractColumnInfo(String name) {
            super(name);
            this.columnName = name;
        }

        @Nullable
        @Override
        public String getMaxStringValue() {
            return columnName;
        }

        @Nullable
        @Override
        public TableCellRenderer getRenderer(JiraIssue issue) {
            return ICON_AND_TEXT_RENDERER;
        }


    }

    private static class IssueTypeColumnInfo extends JiraIssueColumnInfo{

        IssueTypeColumnInfo(String name) {
            super(name);
        }

        @Nullable
        @Override
        public String valueOf(JiraIssue issue) {
            return getIssueType(issue);
        }


    }

    private static class PriorityColumnInfo extends AbstractColumnInfo{

        PriorityColumnInfo(String name) {
            super(name);
        }

        @Nullable
        @Override
        public String valueOf(JiraIssue issue) {
            return getPriority(issue);
        }


        @Override
        public TableCellRenderer getCustomizedRenderer(JiraIssue issue, TableCellRenderer renderer) {
            if(renderer instanceof JiraIconAndTextTableCellRenderer && nonNull(issue.getPriority())){
                ((JiraIconAndTextTableCellRenderer) renderer).setIconUrl(issue.getPriority().getIconUrl());
                ((JiraIconAndTextTableCellRenderer) renderer).emptyText();
                ((JiraIconAndTextTableCellRenderer) renderer).setToolTipText(valueOf(issue));
            }

            return renderer;
        }
    }

    private static class StatusColumnInfo extends JiraIssueColumnInfo{

        StatusColumnInfo(String name) {
            super(name);
        }

        @Nullable
        @Override
        public String valueOf(JiraIssue issue) {
            return getStatus(issue);
        }

        @Nullable
        @Override
        public TableCellRenderer getRenderer(JiraIssue issue) {
            return new JiraIssueStatusTableCellRenderer(issue.getStatus().getName(), issue.getStatus().getCategoryColor(), issue.getStatus().isInProgressCategory());
        }

    }

    private abstract static class JiraIssueColumnInfo extends ColumnInfo<JiraIssue, String> {
        private static final JiraIssueTableCellRenderer JIRA_ISSUE_RENDERER = new JiraIssueTableCellRenderer();

        JiraIssueColumnInfo(@NotNull String name) {
            super(name);
        }


        @Nullable
        @Override
        public TableCellRenderer getRenderer(JiraIssue issue) {
            return JIRA_ISSUE_RENDERER;
        }


    }

    private class KeyColumnInfo extends JiraIssueColumnInfo{

        KeyColumnInfo(@NotNull String name) {
            super(name);
        }

        @Nullable
        @Override
        public String valueOf(JiraIssue issue) {
            return getKey(issue);
        }

        @Nullable
        @Override
        public String getMaxStringValue() {
            return "";
        }

        @Override
        public int getAdditionalWidth() {
            return 90;
        }

        @Override
        public TableCellRenderer getCustomizedRenderer(JiraIssue o, TableCellRenderer renderer) {
            if(renderer instanceof JiraIssueTableCellRenderer){
                ((JiraIssueTableCellRenderer) renderer).setHorizontalAlignment(SwingUtilities.LEFT);
            }
            return renderer;
        }
    }

    private class CreatedColumnInfo extends JiraIssueColumnInfo{

        CreatedColumnInfo(@NotNull String name) {
            super(name);
        }

        @Nullable
        @Override
        public String valueOf(JiraIssue issue) {
            return getCreated(issue);
        }

        @Override
        public TableCellRenderer getCustomizedRenderer(JiraIssue o, TableCellRenderer renderer) {
            if(renderer instanceof JiraIssueTableCellRenderer){
                ((JiraIssueTableCellRenderer) renderer).setHorizontalAlignment(SwingUtilities.RIGHT);
            }
            return renderer;
        }

    }


}
