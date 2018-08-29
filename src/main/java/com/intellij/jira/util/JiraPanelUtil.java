package com.intellij.jira.util;

import com.intellij.ui.JBColor;
import com.intellij.ui.components.JBPanel;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

public class JiraPanelUtil {

    public static final Border MARGIN_BOTTOM = BorderFactory.createEmptyBorder(0 ,0, 10, 0);

    public static JBPanel createWhitePanel(@NotNull LayoutManager layout){
        return new JBPanel(layout)
                .withBackground(JBColor.WHITE);
    }


}
