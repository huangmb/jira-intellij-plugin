package com.intellij.jira.ui;

import com.intellij.ui.components.JBTabbedPane;
import com.intellij.util.ui.JBUI;
import org.jetbrains.annotations.Nullable;

import java.awt.*;

public class JiraTabbedPane extends JBTabbedPane {


    public JiraTabbedPane(int tabPlacement) {
        super(tabPlacement);
    }


    @Override
    public void setTabComponentInsets(@Nullable Insets tabInsets) {
        super.setTabComponentInsets(JBUI.insets(0));
    }
}
