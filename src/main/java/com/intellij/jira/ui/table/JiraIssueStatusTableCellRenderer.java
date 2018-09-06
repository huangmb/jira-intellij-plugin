package com.intellij.jira.ui.table;

import com.intellij.openapi.util.text.StringUtil;
import com.intellij.ui.components.JBPanel;
import com.intellij.util.ui.JBFont;
import com.intellij.util.ui.JBUI;

import javax.swing.*;
import java.awt.*;

import static com.intellij.jira.util.JiraLabelUtil.CELL_COLOR;
import static com.intellij.jira.util.JiraLabelUtil.IN_PROGRESS_TEXT_COLOR;
import static com.intellij.ui.JBColor.white;
import static java.awt.BorderLayout.LINE_START;

public class JiraIssueStatusTableCellRenderer extends JiraIssueTableCellRenderer {

    private String statusName;
    private Color statusCategoryColor;
    private boolean isInProgressCategory;

    public JiraIssueStatusTableCellRenderer(String statusName, Color statusCategoryColor, boolean isInProgressCategory) {
        super();
        this.statusName = statusName;
        this.statusCategoryColor = statusCategoryColor;
        this.isInProgressCategory = isInProgressCategory;
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        Component label = super.getTableCellRendererComponent(table, value, isSelected, false, row, column);

        JBPanel panel = new JBPanel(new BorderLayout()).withBackground(label.getBackground());
        if(!isSelected){
            panel.withBackground(CELL_COLOR);
        }

        setText(StringUtil.toUpperCase(statusName));
        setBackground(statusCategoryColor);
        setForeground(isInProgressCategory ? IN_PROGRESS_TEXT_COLOR : white);
        setFont(JBFont.create(new Font("SansSerif", Font.BOLD, 8)));
        setBorder(JBUI.Borders.empty(4, 3));

        panel.setBorder(JBUI.Borders.empty(4, 3));
        panel.add(this, LINE_START);

        return panel;
    }

}
