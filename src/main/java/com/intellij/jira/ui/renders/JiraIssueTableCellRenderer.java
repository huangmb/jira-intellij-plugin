package com.intellij.jira.ui.renders;

import com.intellij.jira.util.JiraLabelUtil;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

public class JiraIssueTableCellRenderer extends DefaultTableCellRenderer {



    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        super.getTableCellRendererComponent(table, value, isSelected, false, row, column);
        if(!isSelected){
            setBackground(JiraLabelUtil.CELL_COLOR);
        }

        return this;
    }
}
