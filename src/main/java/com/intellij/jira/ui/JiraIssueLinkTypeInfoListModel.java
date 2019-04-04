package com.intellij.jira.ui;

import com.intellij.jira.rest.model.JiraIssueLinkType;
import com.intellij.jira.rest.model.JiraIssueLinkTypeInfo;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class JiraIssueLinkTypeInfoListModel  extends AbstractListModel<JiraIssueLinkTypeInfo> {

    private List<JiraIssueLinkTypeInfo> issueLinkTypes = new ArrayList<>();

    public JiraIssueLinkTypeInfoListModel(List<JiraIssueLinkType> issueLinkTypes) {
        issueLinkTypes.forEach(type -> {
            this.issueLinkTypes.add(JiraIssueLinkTypeInfo.inward(type));
            this.issueLinkTypes.add(JiraIssueLinkTypeInfo.outward(type));
        });

    }

    @Override
    public int getSize() {
        return issueLinkTypes.size();
    }

    @Override
    public JiraIssueLinkTypeInfo getElementAt(int index) {
        return issueLinkTypes.get(index);
    }


    public List<JiraIssueLinkTypeInfo> getIssueLinkTypes() {
        return issueLinkTypes;
    }
}
