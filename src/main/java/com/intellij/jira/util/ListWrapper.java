package com.intellij.jira.util;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

public class ListWrapper<T> {

    private List<T> values;

    public ListWrapper(@NotNull List<T> values) {
        checkNotNull(values, "values can not be null");
        this.values = values;
    }


    public boolean isEmpty(){
        return values.isEmpty();
    }


    public boolean isNotEmpty(){
        return !isEmpty();
    }

    @Nullable
    public T getFirst(){
        return isNotEmpty() ? values.get(0) : null;
    }

    public List<T> getValues() {
        return values;
    }
}
