package com.intellij.jira.util;

import com.intellij.jira.rest.model.JiraIssueStatus;
import com.intellij.jira.ui.labels.JiraLinkLabel;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.ui.JBColor;
import com.intellij.ui.components.JBLabel;
import com.intellij.util.ui.JBFont;
import com.intellij.util.ui.JBUI;

import javax.swing.*;
import java.awt.*;

import static javax.swing.SwingConstants.LEFT;

public class JiraLabelUtil {

    public static final JBFont BOLD = JBUI.Fonts.label().asBold();
    public static final JBFont ITALIC = JBUI.Fonts.label().asItalic();

    public static final Color CELL_COLOR = new Color(211, 232, 240  );
    public static final Color ISSUE_LINK_COLOR = new Color(240, 216, 226);

    // Status
    public static final Color UNDEFINED_COLOR = new Color(192, 192, 192);
    public static final Color TO_DO_COLOR = new Color(74, 103, 133);
    public static final Color IN_PROGRESS_COLOR = new Color(255, 211, 81);
    public static final Color IN_PROGRESS_TEXT_COLOR = new Color(89, 67, 0);
    public static final Color DONE_COLOR = new Color(20, 137, 44);


    public static JBLabel createEmptyLabel(){
        return createLabel("");
    }

    public static JBLabel createLabel(String text){
        return createLabel(text, LEFT);
    }

    public static JBLabel createLabel(String text, int horizontalAlignment){
        JBLabel label = new JBLabel(text);
        label.setHorizontalAlignment(horizontalAlignment);
        return label;
    }

    public static JBLabel createIconLabel(Icon icon, String text){
       return new JBLabel(text, icon, LEFT);
    }

    public static JBLabel createIconLabel(String iconUrl, String text){
        return new JBLabel(text, JiraIconUtil.getIcon(iconUrl), LEFT);
    }

    public static JBLabel createBoldLabel(String text){
        return createLabel(text).withFont(BOLD);
    }

    public static JBLabel createItalicLabel(String text){
        return createLabel(text).withFont(ITALIC);
    }



    public static JBLabel createLinkLabel(String text, String url){
        return new JiraLinkLabel(text, url);
    }

    public static JBLabel createStatusLabel(JiraIssueStatus status){
        JBLabel label = new JBLabel(StringUtil.toUpperCase(status.getName()), LEFT);
        label.setFont(JBFont.create(new Font("SansSerif", Font.BOLD, 9)));
        label.setBorder(JBUI.Borders.empty(2, 2, 2, 3));
        label.setBackground(status.getCategoryColor());
        label.setForeground(status.isInProgressCategory() ?  IN_PROGRESS_TEXT_COLOR : JBColor.white);
        label.setOpaque(true);
        return label;
    }

    public static JBLabel createEmptyStatusLabel(){
        JBLabel label = new JBLabel("", LEFT);
        label.setFont(JBFont.create(new Font("SansSerif", Font.BOLD, 9)));
        label.setBorder(JBUI.Borders.empty(2, 2, 2, 3));
        label.setOpaque(true);
        return label;
    }

}
