package com.intellij.jira.util;

import com.intellij.ui.components.JBLabel;
import com.intellij.util.ui.JBFont;
import com.intellij.util.ui.JBUI;

import javax.swing.*;

public class JiraLabelUtil {

    public static final JBFont BOLD = JBUI.Fonts.label().asBold();
    public static final JBFont ITALIC = JBUI.Fonts.label().asItalic();


    public static JBLabel createLabel(String text){
        return createLabel(text, SwingConstants.LEFT);
    }

    public static JBLabel createLabel(String text, int horizontalAlignment){
        JBLabel label = new JBLabel(text);
        label.setHorizontalAlignment(horizontalAlignment);

        return label;
    }

    public static JBLabel createIconLabel(Icon icon, String text){
       return new JBLabel(text, icon, SwingConstants.LEFT);
    }

    public static JBLabel createIconLabel(String iconUrl, String text){
        return new JBLabel(text, JiraIconUtil.getIcon(iconUrl), SwingConstants.LEFT);
    }


}
