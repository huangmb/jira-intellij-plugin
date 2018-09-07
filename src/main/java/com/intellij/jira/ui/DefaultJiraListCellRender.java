package com.intellij.jira.ui;

import com.intellij.ui.JBColor;
import com.intellij.util.ui.JBUI;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

public class DefaultJiraListCellRender extends JPanel implements ListCellRenderer<Object> {

    private static final Border DEFAULT_LINE_BORDER = JBUI.Borders.customLine(JBColor.border(),0, 0, 1, 0);

    public DefaultJiraListCellRender() {
        setLayout(new BorderLayout());

    }

    @Override
    public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        setBorder(DEFAULT_LINE_BORDER);
        setComponentOrientation(list.getComponentOrientation());


        if (isSelected) {
            setBackground(list.getSelectionBackground());
            setForeground(list.getSelectionForeground());
        }
        else {
            setBackground(list.getBackground());
            setForeground(list.getForeground());
        }

        setEnabled(list.isEnabled());
        setFont(list.getFont());

        return this;
    }
}
