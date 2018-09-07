package com.intellij.jira.ui.labels;


import com.intellij.ide.BrowserUtil;
import com.intellij.ui.JBColor;
import com.intellij.ui.components.JBLabel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class JiraLinkLabel extends JBLabel {

    private static final Cursor HAND_CURSOR = new Cursor(Cursor.HAND_CURSOR);
    private static final Color LINK_COLOR = JBColor.BLUE;
    private String url;

    public JiraLinkLabel(String text, String url) {
        super(text);
        this.url = url;
        init();
    }

    private void init(){
        setHorizontalAlignment(SwingUtilities.LEFT);
        setToolTipText(this.url);
        setCursor(HAND_CURSOR);
        setForeground(LINK_COLOR);
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                SwingUtilities.invokeLater(() -> BrowserUtil.open(url));
            }
        });
    }


}
