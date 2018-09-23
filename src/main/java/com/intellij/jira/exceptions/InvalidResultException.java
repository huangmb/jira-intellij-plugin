package com.intellij.jira.exceptions;

public class InvalidResultException extends RuntimeException {

    public InvalidResultException(String message, String detailsMessage) {
        super(message, new Throwable(detailsMessage));
    }

}
