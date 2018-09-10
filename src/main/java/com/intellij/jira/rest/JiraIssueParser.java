package com.intellij.jira.rest;

import com.google.gson.reflect.TypeToken;
import com.intellij.jira.rest.model.JiraIssue;
import com.intellij.jira.rest.model.JiraIssueComment;
import com.intellij.jira.rest.model.JiraIssueTransition;
import com.intellij.jira.rest.model.JiraIssueUser;
import com.intellij.tasks.jira.JiraRepository;
import com.intellij.util.containers.ContainerUtil;

import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.List;

public class JiraIssueParser {
    private static final Type ISSUES_WRAPPER_TYPE = (new TypeToken<JiraIssuesWrapper<JiraIssue>>(){}).getType();
    private static final Type ISSUE_TRANSITION_WRAPPER_TYPE = (new TypeToken<JiraIssueTransitionsWrapper<JiraIssueTransition>>(){}).getType();

    private JiraIssueParser() { }


    public static JiraIssue parseIssue(String response){
        return JiraRepository.GSON.fromJson(response, JiraIssue.class);
    }


    public static List<JiraIssue> parseIssues(String response){
        JiraIssuesWrapper<JiraIssue> wrapper = JiraRepository.GSON.fromJson(response, ISSUES_WRAPPER_TYPE);
        if(wrapper == null){
            return ContainerUtil.emptyList();
        }
        return wrapper.getIssues();
    }

    public static List<JiraIssueTransition> parseIssueTransitions(String response){
        JiraIssueTransitionsWrapper<JiraIssueTransition> wrapper = JiraRepository.GSON.fromJson(response, ISSUE_TRANSITION_WRAPPER_TYPE);
        if(wrapper == null){
            return ContainerUtil.emptyList();
        }
        return wrapper.getTransitions();
    }


    public static List<JiraIssueUser> parseUsers(String response) {
        return Arrays.asList(JiraRepository.GSON.fromJson(response, JiraIssueUser[].class));
    }

    public static JiraIssueComment parseIssueComment(String response){
        return JiraRepository.GSON.fromJson(response, JiraIssueComment.class);
    }

}
