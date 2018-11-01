package com.intellij.jira.rest;

import com.google.gson.reflect.TypeToken;
import com.intellij.jira.rest.model.*;
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

    public static JiraUser parseUser(String response) {
        return JiraRepository.GSON.fromJson(response, JiraUser.class);
    }

    public static List<JiraUser> parseUsers(String response) {
        return Arrays.asList(JiraRepository.GSON.fromJson(response, JiraUser[].class));
    }

    public static JiraIssueComment parseIssueComment(String response){
        return JiraRepository.GSON.fromJson(response, JiraIssueComment.class);
    }

    public static List<JiraIssuePriority> parseIssuePriorities(String response){
        return Arrays.asList(JiraRepository.GSON.fromJson(response, JiraIssuePriority[].class));
    }

    public static List<JiraProject> parseProjects(String response){
        return Arrays.asList(JiraRepository.GSON.fromJson(response, JiraProject[].class));
    }

    public static List<JiraProjectVersionDetails> parseProjectVersionsDetails(String response){
        return  Arrays.asList(JiraRepository.GSON.fromJson(response, JiraProjectVersionDetails[].class));
    }

    public static List<JiraIssueLinkType> parseIssueLinkTypes(String response){
        JiraIssueLinkTypesWrapper wrapper = JiraRepository.GSON.fromJson(response, JiraIssueLinkTypesWrapper.class);
        if(wrapper == null){
            return ContainerUtil.emptyList();
        }

        return wrapper.getIssueLinkTypes();
    }

    public static List<JiraGroup> parseGroups(String response){
        JiraGroupsWrapper wrapper = JiraRepository.GSON.fromJson(response, JiraGroupsWrapper.class);
        if(wrapper == null){
            return ContainerUtil.emptyList();
        }

        return wrapper.getGroups();
    }

}
