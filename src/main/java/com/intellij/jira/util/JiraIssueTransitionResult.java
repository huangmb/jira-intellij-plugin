package com.intellij.jira.util;

import com.intellij.openapi.util.text.StringUtil;
import org.jetbrains.annotations.Nullable;

public class JiraIssueTransitionResult implements Result{

    private String response;

    private JiraIssueTransitionResult(@Nullable String response) {
        this.response = response;
    }


    public static JiraIssueTransitionResult create(String response){
        return new JiraIssueTransitionResult(response);
    }

    public static JiraIssueTransitionResult error(){
        return new JiraIssueTransitionResult(null);
    }

    @Override
    public boolean isValid() {
        return StringUtil.isEmpty(response);
    }

    @Override
    public Object get() {
        return null;
    }
}
