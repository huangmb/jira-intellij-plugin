package com.intellij.jira.ui.table;

import com.intellij.jira.rest.model.JiraIssue;
import com.intellij.ui.table.TableView;

import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.util.List;

import static javax.swing.ListSelectionModel.SINGLE_SELECTION;

public class JiraIssueTableView extends TableView<JiraIssue> {

    public JiraIssueTableView(List<JiraIssue> issues) {
        super();
        setModelAndUpdateColumns(new JiraIssueListTableModel(issues));
        setSelectionMode(SINGLE_SELECTION);
        setIntercellSpacing(new Dimension());
        setShowGrid(false);
        setRowHeight(25);
    }


    @Override
    public TableCellRenderer getCellRenderer(int row, int column) {
        return super.getCellRenderer(row, column);
    }

    @Override
    protected TableColumnModel createDefaultColumnModel() {
        TableColumnModel columnModel = super.createDefaultColumnModel();
        columnModel.setColumnMargin(0);
        return columnModel;
    }


}
