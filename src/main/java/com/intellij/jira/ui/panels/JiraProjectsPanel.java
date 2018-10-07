package com.intellij.jira.ui.panels;

import com.intellij.jira.tasks.JiraServer;
import com.intellij.openapi.ui.SimpleToolWindowPanel;
import com.intellij.ui.JBSplitter;
import org.jetbrains.annotations.Nullable;

import static com.intellij.jira.util.JiraPanelUtil.createPlaceHolderPanel;
import static java.util.Objects.isNull;

public class JiraProjectsPanel extends SimpleToolWindowPanel {

    private JiraServer jiraServer;
    private JiraProjectListPanel projectListPanel;
    private JiraProjectPreviewPanel projectPreviewPanel;

    public JiraProjectsPanel(@Nullable JiraServer jiraServer) {
        super(true, true);
        this.jiraServer = jiraServer;
        init();
    }


    private void init() {
        initContent();
    }

    private void initContent() {
        if(isNull(jiraServer)){
            super.setContent(createPlaceHolderPanel("No server found"));
        }
        else{
            projectPreviewPanel = new JiraProjectPreviewPanel(jiraServer);
            projectListPanel = new JiraProjectListPanel(jiraServer, projectPreviewPanel);

            JBSplitter splitter = new JBSplitter();
            splitter.setProportion(0.4f);
            splitter.setFirstComponent(projectListPanel);
            splitter.setSecondComponent(projectPreviewPanel);
            splitter.setShowDividerIcon(false);
            splitter.setDividerWidth(2);


            super.setContent(splitter);
        }


    }




}
