package com.intellij.jira.ui.table;

import com.intellij.jira.helper.ColumnInfoHelper;
import com.intellij.jira.rest.model.JiraIssue;
import com.intellij.util.ui.ListTableModel;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class JiraIssueListTableModel extends ListTableModel<JiraIssue> {

    public JiraIssueListTableModel(@NotNull List<JiraIssue> jiraIssues) {
        super();
        setColumnInfos(ColumnInfoHelper.getHelper().generateColumnsInfo());
        setItems(jiraIssues);
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false;
    }


}
