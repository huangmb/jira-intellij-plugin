package com.intellij.jira.rest.model;

import com.intellij.jira.util.JiraLabelUtil;

import java.awt.*;

public class JiraIssueStatusCategory {

    private String id;
    private String key;
    private String self;
    private String name;
    private String colorName;

    public JiraIssueStatusCategory() { }


    public Color getColor(){
        switch (key){
            case "new": return JiraLabelUtil.TO_DO_COLOR;
            case "indeterminate": return JiraLabelUtil.IN_PROGRESS_COLOR;
            case "done": return JiraLabelUtil.DONE_COLOR;
            default: return JiraLabelUtil.UNDEFINED_COLOR;
        }
    }

    public boolean isInProgressCategory(){
        return "indeterminate".equals(key);
    }


}
