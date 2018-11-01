package com.intellij.jira.rest;

import com.intellij.jira.rest.model.JiraIssueLinkType;
import com.intellij.util.containers.ContainerUtil;

import java.util.List;

public class JiraIssueLinkTypesWrapper {

    private List<JiraIssueLinkType> issueLinkTypes = ContainerUtil.emptyList();

    public JiraIssueLinkTypesWrapper() { }

    public List<JiraIssueLinkType> getIssueLinkTypes() {
        return issueLinkTypes;
    }
}
