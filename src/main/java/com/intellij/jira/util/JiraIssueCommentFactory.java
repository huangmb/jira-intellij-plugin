package com.intellij.jira.util;

import com.intellij.jira.rest.model.JiraIssueComment;
import com.intellij.openapi.util.Factory;

@FunctionalInterface
public interface JiraIssueCommentFactory extends Factory<JiraIssueComment> {

}
