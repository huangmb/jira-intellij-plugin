package com.intellij.jira.components;

import java.util.List;

public interface Updater<T> {

    void update(List<T> t);

    void update(T t);

}
