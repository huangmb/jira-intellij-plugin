package com.intellij.jira.ui.renders;

import com.intellij.jira.rest.model.JiraProject;
import com.intellij.jira.util.JiraLabelUtil;
import com.intellij.ui.components.JBLabel;
import com.intellij.ui.components.JBPanel;
import com.intellij.util.ui.JBUI;

import javax.swing.*;
import java.awt.*;

public class JiraProjectListCellRender extends DefaultJiraListCellRender {

    private JBLabel projectKeyLabel;
    private JBLabel projectNameLabel;
    private JBLabel projectTypeLabel;
    private JBLabel projectLeadLabel;

    public JiraProjectListCellRender() {
        super();
        init();
    }

    private void init() {
        JBPanel projectPanel = new JBPanel().withBorder(JBUI.Borders.empty(4, 5)).andTransparent();
        projectPanel.setLayout(new BoxLayout(projectPanel, BoxLayout.X_AXIS));

        projectKeyLabel =  JiraLabelUtil.createEmptyLabel();
        projectNameLabel = JiraLabelUtil.createEmptyLabel();
        /*projectTypeLabel = JiraLabelUtil.createEmptyLabel();
        projectLeadLabel = JiraLabelUtil.createEmptyLabel();*/

        projectPanel.add(projectKeyLabel);
        projectPanel.add(Box.createHorizontalStrut(20));
        projectPanel.add(projectNameLabel);
       /* projectPanel.add(Box.createHorizontalStrut(20));
        projectPanel.add(projectTypeLabel);
        projectPanel.add(Box.createHorizontalStrut(20));
        projectPanel.add(projectLeadLabel);*/

        add(projectPanel);
    }

    @Override
    public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

        setBorder(null);
        JiraProject project = (JiraProject) value;

        projectKeyLabel.setText(project.getKey());
        projectNameLabel.setText(project.getName());
        /*projectTypeLabel.setText(project.getProjectTypeKey());
        projectLeadLabel.setText(project.getLeadName());*/


        return this;
    }
}
