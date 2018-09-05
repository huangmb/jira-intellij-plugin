package com.intellij.jira.rest;

import com.google.gson.reflect.TypeToken;
import com.intellij.jira.rest.model.JiraIssue;
import com.intellij.jira.rest.model.JiraIssueTransition;
import com.intellij.tasks.jira.JiraRepository;
import com.intellij.util.containers.ContainerUtil;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.commons.httpclient.methods.StringRequestEntity;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.util.List;

public class JiraRestClient {
    private static final Type ISSUES_WRAPPER_TYPE = (new TypeToken<JiraIssuesWrapper<JiraIssue>>(){}).getType();
    private static final Type ISSUE_TRANSITION_WRAPPER_TYPE = (new TypeToken<JiraIssueTransitionsWrapper<JiraIssueTransition>>(){}).getType();
    private static final Integer DEFAULT_MAX_ISSUES_RESULTS = 100;

    private JiraRepository jiraRepository;

    public JiraRestClient(JiraRepository jiraRepository) {
        this.jiraRepository = jiraRepository;
    }

    public JiraIssue getIssue(String issueId) throws Exception {
        GetMethod method = new GetMethod(this.jiraRepository.getRestUrl("issue", issueId));
        method.setQueryString(method.getQueryString() + "?fields=" + JiraIssue.REQUIRED_FIELDS);
        String response = jiraRepository.executeMethod(method);
        return parseIssue(response);
    }

    public List<JiraIssue> findIssues() throws Exception {
        GetMethod method = getBasicSearchMethod(this.jiraRepository.getSearchQuery(), DEFAULT_MAX_ISSUES_RESULTS);
        method.setQueryString(method.getQueryString() + "&fields=" + JiraIssue.REQUIRED_FIELDS);
        String response = jiraRepository.executeMethod(method);
        return parseIssues(response);
    }


    public List<JiraIssueTransition> getTransitions(String issueId) throws Exception {
        GetMethod method = new GetMethod(this.jiraRepository.getRestUrl("issue", issueId, "transitions"));
        String response = jiraRepository.executeMethod(method);
        return parseIssueTransitions(response);
    }

    private GetMethod getBasicSearchMethod(String jql, int maxResults){
        GetMethod method = new GetMethod(this.jiraRepository.getRestUrl(new String[]{"search"}));
        method.setQueryString(new NameValuePair[]{new NameValuePair("jql", jql), new NameValuePair("maxResults", String.valueOf(maxResults))});
        return method;
    }

    private JiraIssue parseIssue(String response){
        return JiraRepository.GSON.fromJson(response, JiraIssue.class);
    }


    private List<JiraIssue> parseIssues(String response){
        JiraIssuesWrapper<JiraIssue> wrapper = JiraRepository.GSON.fromJson(response, ISSUES_WRAPPER_TYPE);
        if(wrapper == null){
            return ContainerUtil.emptyList();
        }
        return wrapper.getIssues();
    }

    private List<JiraIssueTransition> parseIssueTransitions(String response){
        JiraIssueTransitionsWrapper<JiraIssueTransition> wrapper = JiraRepository.GSON.fromJson(response, ISSUE_TRANSITION_WRAPPER_TYPE);
        if(wrapper == null){
            return ContainerUtil.emptyList();
        }
        return wrapper.getTransitions();
    }

    public String doTransition(String issueId, String transitionId) throws Exception {
        String requestBody = "{\"transition\": {\"id\": \"" + transitionId + "\"}}";
        PostMethod method = new PostMethod(this.jiraRepository.getRestUrl("issue", issueId, "transitions"));
        method.setRequestEntity(createJsonEntity(requestBody));
        return jiraRepository.executeMethod(method);
    }


    private static RequestEntity createJsonEntity(String requestBody) {
        try {
            return new StringRequestEntity(requestBody, "application/json", "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new AssertionError("UTF-8 encoding is not supported");
        }
    }

}
