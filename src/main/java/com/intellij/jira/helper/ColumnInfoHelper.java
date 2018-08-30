package com.intellij.jira.helper;

import com.intellij.jira.rest.model.JiraIssue;
import com.intellij.jira.ui.table.JiraIssueTableCellRenderer;
import com.intellij.jira.util.JiraIconUtil;
import com.intellij.jira.util.JiraLabelUtil;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.util.ui.ColumnInfo;
import com.intellij.util.ui.table.IconTableCellRenderer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

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
    public ColumnInfo[] generateColumnsInfo() {
        return new ColumnInfo[]{new JiraIssueColumnInfo("Key") {
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

        }, new JiraIssueColumnInfo("Summary") {
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
         , new JiraIssueColumnInfo("Created") {
            public String valueOf(JiraIssue issue) {
                return getCreated(issue);
            }

            @Nullable
            @Override
            public String getMaxStringValue() {
                return "dd/MM/YYYY HH:mm";
            }

            @Override
            public int getAdditionalWidth() {
                return 10;
            }
        }};
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
            super.getTableCellRendererComponent(table, value, selected, false, row, column);
            setText(label);
            if(!selected){
                setBackground(JiraLabelUtil.CELL_COLOR);
            }



            return this;
        }
    }


    private static abstract class AbstractColumnInfo extends ColumnInfo<JiraIssue, String>{
        private static final TableCellRenderer ICON_AND_TEXT_RENDERER = new ColumnInfoHelper.IconAndTextTableCellRenderer();
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

    private static class IssueTypeColumnInfo extends AbstractColumnInfo{

        IssueTypeColumnInfo(String name) {
            super(name);
        }

        @Nullable
        @Override
        public String valueOf(JiraIssue issue) {
            return issue.getIssuetype().getName();
        }


        @Override
        public TableCellRenderer getCustomizedRenderer(JiraIssue issue, TableCellRenderer renderer) {
            if(renderer instanceof ColumnInfoHelper.IconAndTextTableCellRenderer){
                ((ColumnInfoHelper.IconAndTextTableCellRenderer) renderer).setIconUrl(issue.getIssuetype().getIconUrl());
                ((ColumnInfoHelper.IconAndTextTableCellRenderer) renderer).emptyText();
                ((ColumnInfoHelper.IconAndTextTableCellRenderer) renderer).setToolTipText(valueOf(issue));
            }



            return renderer;
        }

    }

    private static class PriorityColumnInfo extends AbstractColumnInfo{

        PriorityColumnInfo(String name) {
            super(name);
        }

        @Nullable
        @Override
        public String valueOf(JiraIssue issue) {
            return issue.getPriority().getName();
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

    private static class StatusColumnInfo extends AbstractColumnInfo{

        StatusColumnInfo(String name) {
            super(name);
        }

        @Nullable
        @Override
        public String valueOf(JiraIssue issue) {
            return issue.getStatus().getName();
        }


        @Override
        public TableCellRenderer getCustomizedRenderer(JiraIssue issue, TableCellRenderer renderer) {
            if(renderer instanceof ColumnInfoHelper.IconAndTextTableCellRenderer){
                ((ColumnInfoHelper.IconAndTextTableCellRenderer) renderer).setIconUrl(issue.getStatus().getIconUrl());
                ((ColumnInfoHelper.IconAndTextTableCellRenderer) renderer).emptyText();
                ((ColumnInfoHelper.IconAndTextTableCellRenderer) renderer).setToolTipText(valueOf(issue));
            }

            return renderer;
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


}
