package com.intellij.jira.util;

public interface Result<T> {

    boolean isValid();


    T get();

}
