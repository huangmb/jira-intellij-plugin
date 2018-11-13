package com.intellij.jira.ui.editors;

import java.util.List;


public abstract class DataSelectFieldEditor<T> extends SelectFieldEditor {

    protected List<T> myItems;
    protected List<T> selectedItems;

    public DataSelectFieldEditor(String fieldName, String issueKey, boolean required, boolean isMultiSelect, List<T> items) {
        super(fieldName, issueKey, required, isMultiSelect);
        myItems = items;
    }


}
