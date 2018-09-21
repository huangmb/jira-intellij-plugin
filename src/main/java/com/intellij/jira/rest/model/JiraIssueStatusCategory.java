package com.intellij.jira.rest.model;

import com.intellij.jira.util.JiraLabelUtil;

import java.awt.*;

public class JiraIssueStatusCategory {

    private static final String TO_DO_KEY = "new";
    private static final String IN_PROGRESS_KEY = "indeterminate";
    private static final String DONE_KEY = "done";

    private String id;
    private String key;
    private String self;
    private String name;
    private String colorName;

    public JiraIssueStatusCategory() { }


    public Color getColor(){
        switch (key){
            case TO_DO_KEY: return JiraLabelUtil.TO_DO_COLOR;
            case IN_PROGRESS_KEY: return JiraLabelUtil.IN_PROGRESS_COLOR;
            case DONE_KEY: return JiraLabelUtil.DONE_COLOR;
            default: return JiraLabelUtil.UNDEFINED_COLOR;
        }
    }

    public boolean isInProgressCategory(){
        return IN_PROGRESS_KEY.equals(key);
    }

    public boolean isDoneCategory(){
        return DONE_KEY.equals(key);
    }

}
