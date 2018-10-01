package com.intellij.jira.ui.renders;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

import static com.intellij.jira.util.JiraLabelUtil.getBgRowColor;
import static com.intellij.jira.util.JiraLabelUtil.getFgRowColor;

public class JiraIssueTableCellRenderer extends DefaultTableCellRenderer {



    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        super.getTableCellRendererComponent(table, value, isSelected, false, row, column);

        setBackground(getBgRowColor(isSelected));
        setForeground(getFgRowColor(isSelected));

        return this;
    }
}
