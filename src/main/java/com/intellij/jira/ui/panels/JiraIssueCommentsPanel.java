package com.intellij.jira.ui.panels;

import com.intellij.jira.rest.JiraIssueCommentsWrapper;
import com.intellij.jira.rest.model.JiraIssueComment;
import com.intellij.jira.util.JiraIssueUtil;
import com.intellij.jira.util.JiraLabelUtil;
import com.intellij.jira.util.JiraPanelUtil;
import com.intellij.openapi.ui.SimpleToolWindowPanel;
import com.intellij.ui.JBColor;
import com.intellij.ui.ScrollPaneFactory;
import com.intellij.ui.components.JBLabel;
import com.intellij.ui.components.JBPanel;
import com.intellij.util.ui.JBUI;

import javax.swing.*;
import java.awt.*;

import static java.awt.BorderLayout.*;
import static javax.swing.BoxLayout.Y_AXIS;
import static javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED;

class JiraIssueCommentsPanel extends SimpleToolWindowPanel {

    private JiraIssueCommentsWrapper comments;

    public JiraIssueCommentsPanel(JiraIssueCommentsWrapper comments) {
        super(true);
        this.comments = comments;
        initContent();
    }


    private void initContent(){
        if(comments.getComments().isEmpty()){
            setContent(JiraPanelUtil.createPlaceHolderPanel("No comments"));
        }
        else{
            JBPanel commentsPanel = new JBPanel();
            commentsPanel.setLayout(new BoxLayout(commentsPanel, Y_AXIS));
            comments.getComments().forEach(comment -> commentsPanel.add(createCommentPanel(comment)));

            JBPanel panel = JiraPanelUtil.createWhitePanel(new BorderLayout());
            panel.add(ScrollPaneFactory.createScrollPane(commentsPanel, VERTICAL_SCROLLBAR_AS_NEEDED), CENTER);

            setContent(panel);
        }
    }

    private JPanel createCommentPanel(JiraIssueComment comment) {
        JBPanel commentPanel = JiraPanelUtil.createWhitePanel(new BorderLayout())
                                    .withBorder(JBUI.Borders.customLine(JBColor.border(), 0 ,0, 1, 0));

        JBPanel wrapper = JiraPanelUtil.createWhitePanel(new BorderLayout())
                            .withBorder(JBUI.Borders.empty(2, 4));


        // User and created date
        JBPanel priorityPanel = JiraPanelUtil.createWhitePanel(new GridLayout(1,2));
        JBLabel authorLabel = JiraLabelUtil.createBoldLabel(comment.getAuthor().getDisplayName());
        JBLabel createdLabel = JiraLabelUtil.createItalicLabel(JiraIssueUtil.getCreated(comment));
        createdLabel.setHorizontalAlignment(SwingConstants.RIGHT);

        priorityPanel.add(authorLabel);
        priorityPanel.add(createdLabel);

        // Body
        JTextArea commentArea = new JTextArea(comment.getBody());
        commentArea.setLineWrap(true);
        commentArea.setEditable(false);

        wrapper.add(priorityPanel, PAGE_START);
        wrapper.add(commentArea, CENTER);

        commentPanel.add(wrapper, CENTER);

        return commentPanel;
    }


}
