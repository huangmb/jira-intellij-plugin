package com.intellij.jira.rest;

import com.google.gson.reflect.TypeToken;
import com.intellij.jira.rest.model.JiraIssue;
import com.intellij.tasks.jira.JiraRepository;
import com.intellij.util.containers.ContainerUtil;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.GetMethod;

import java.lang.reflect.Type;
import java.util.List;

public class JiraRestClient {
    private static final Type ISSUES_WRAPPER_TYPE = (new TypeToken<JiraIssuesWrapper<JiraIssue>>(){}).getType();
    private static final Integer DEFAULT_MAX_ISSUES_RESULTS = 100;

    private JiraRepository jiraRepository;

    public JiraRestClient(JiraRepository jiraRepository) {
        this.jiraRepository = jiraRepository;
    }

    public List<JiraIssue> findIssues() throws Exception {
        GetMethod method = getBasicSearchMethod(this.jiraRepository.getSearchQuery(), DEFAULT_MAX_ISSUES_RESULTS);
        method.setQueryString(method.getQueryString() + "&fields=" + JiraIssue.REQUIRED_FIELDS);
        String response = jiraRepository.executeMethod(method);
        return parseIssues(response);
    }


    private GetMethod getBasicSearchMethod(String jql, int maxResults){
        GetMethod method = new GetMethod(this.jiraRepository.getRestUrl(new String[]{"search"}));
        method.setQueryString(new NameValuePair[]{new NameValuePair("jql", jql), new NameValuePair("maxResults", String.valueOf(maxResults))});
        return method;
    }


    private List<JiraIssue> parseIssues(String response){
        JiraIssuesWrapper<JiraIssue> wrapper = JiraRepository.GSON.fromJson(response, ISSUES_WRAPPER_TYPE);
        if(wrapper == null){
            return ContainerUtil.emptyList();
        }
        return wrapper.getIssues();
    }

}
