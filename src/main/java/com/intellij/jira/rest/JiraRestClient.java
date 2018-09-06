package com.intellij.jira.rest;

import com.intellij.jira.rest.model.JiraIssue;
import com.intellij.jira.rest.model.JiraIssueTransition;
import com.intellij.jira.rest.model.JiraIssueUser;
import com.intellij.tasks.jira.JiraRepository;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.*;

import java.io.UnsupportedEncodingException;
import java.util.List;

import static com.intellij.jira.rest.JiraIssueParser.*;

public class JiraRestClient {
    private static final Integer DEFAULT_MAX_ISSUES_RESULTS = 100;
    private static final String ISSUE = "issue";
    private static final String TRANSITIONS = "transitions";
    private static final String SEARCH = "search";

    private JiraRepository jiraRepository;

    public JiraRestClient(JiraRepository jiraRepository) {
        this.jiraRepository = jiraRepository;
    }

    public JiraIssue getIssue(String issueIdOrKey) throws Exception {
        GetMethod method = new GetMethod(this.jiraRepository.getRestUrl(ISSUE, issueIdOrKey));
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
        GetMethod method = new GetMethod(this.jiraRepository.getRestUrl(ISSUE, issueId, TRANSITIONS));
        String response = jiraRepository.executeMethod(method);
        return parseIssueTransitions(response);
    }


    public String transitIssue(String issueId, String transitionId) throws Exception {
        String requestBody = "{\"transition\": {\"id\": \"" + transitionId + "\"}}";
        PostMethod method = new PostMethod(this.jiraRepository.getRestUrl(ISSUE, issueId, TRANSITIONS));
        method.setRequestEntity(createJsonEntity(requestBody));
        return jiraRepository.executeMethod(method);
    }

    public List<JiraIssueUser> getAssignableUsers(String issueKey) throws Exception {
        GetMethod method = new GetMethod(this.jiraRepository.getRestUrl("user", "assignable", SEARCH));
        method.setQueryString(new NameValuePair[]{new NameValuePair("issueKey", issueKey)});
        String response = jiraRepository.executeMethod(method);
        return parseUsers(response);
    }



    public String assignUserToIssue(String username, String issueKey) throws Exception {
        String requestBody = "{\"name\": \"" + username + "\"}";
        PutMethod method = new PutMethod(this.jiraRepository.getRestUrl(ISSUE, issueKey, "assignee"));
        method.setRequestEntity(createJsonEntity(requestBody));
        return jiraRepository.executeMethod(method);
    }

    private GetMethod getBasicSearchMethod(String jql, int maxResults){
        GetMethod method = new GetMethod(this.jiraRepository.getRestUrl(SEARCH));
        method.setQueryString(new NameValuePair[]{new NameValuePair("jql", jql), new NameValuePair("maxResults", String.valueOf(maxResults))});
        return method;
    }


    private static RequestEntity createJsonEntity(String requestBody) {
        try {
            return new StringRequestEntity(requestBody, "application/json", "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new AssertionError("UTF-8 encoding is not supported");
        }
    }



}
