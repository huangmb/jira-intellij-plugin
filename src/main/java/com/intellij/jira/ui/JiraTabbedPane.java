package com.intellij.jira.ui;

import com.intellij.ui.components.JBTabbedPane;
import com.intellij.util.ui.JBUI;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

public class JiraTabbedPane extends JBTabbedPane {


    public JiraTabbedPane(int tabPlacement) {
        super(tabPlacement);
    }


    @NotNull
    @Override
    protected Insets getInsetsForTabComponent() {
        return JBUI.insets(0);
    }

}
