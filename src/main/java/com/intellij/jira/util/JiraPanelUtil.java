package com.intellij.jira.util;

import com.intellij.ui.JBColor;
import com.intellij.ui.components.JBLabel;
import com.intellij.ui.components.JBPanel;
import com.intellij.util.ui.JBUI;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

public class JiraPanelUtil {

    public static final Border MARGIN_BOTTOM = JBUI.Borders.emptyBottom(10);

    public static JBPanel createWhitePanel(@NotNull LayoutManager layout){
        return new JBPanel(layout)
                .withBackground(JBColor.WHITE);
    }


    public static JBPanel createPlaceHolderPanel(String text) {
        JBPanel panel = new JBPanel(new BorderLayout());
        JBPanel labelPanel = new JBPanel();
        JBLabel messageLabel = new JBLabel(text);
        messageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        messageLabel.setVerticalAlignment(SwingConstants.CENTER);
        labelPanel.add(messageLabel);
        panel.add(labelPanel, BorderLayout.CENTER);
        return panel;
    }

}
