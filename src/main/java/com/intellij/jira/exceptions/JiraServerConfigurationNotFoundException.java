package com.intellij.jira.exceptions;

public class JiraServerConfigurationNotFoundException extends RuntimeException {

    private static final String NO_JIRA_SERVER_FOUND = "No Jira server found";

    public JiraServerConfigurationNotFoundException() {
        super(NO_JIRA_SERVER_FOUND, new Throwable());
    }

    public JiraServerConfigurationNotFoundException(String detailMessage) {
        super(NO_JIRA_SERVER_FOUND, new Throwable(detailMessage));
    }
}
