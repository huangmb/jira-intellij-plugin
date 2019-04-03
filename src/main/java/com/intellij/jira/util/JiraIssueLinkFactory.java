package com.intellij.jira.util;

import com.intellij.jira.rest.model.JiraIssueLink;
import com.intellij.openapi.util.Factory;

@FunctionalInterface
public interface JiraIssueLinkFactory extends Factory<JiraIssueLink> {

}
