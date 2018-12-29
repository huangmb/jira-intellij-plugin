package com.intellij.jira.ui.editors;

import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.intellij.jira.rest.model.JiraIssueUser;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

import static com.intellij.jira.util.JiraGsonUtil.createArrayNameObjects;
import static com.intellij.jira.util.JiraGsonUtil.createNameObject;
import static com.intellij.openapi.util.text.StringUtil.isEmpty;
import static com.intellij.openapi.util.text.StringUtil.trim;
import static com.intellij.util.containers.ContainerUtil.getFirstItem;
import static java.util.Objects.nonNull;
import static java.util.stream.Collectors.toList;

public class UserSelectFieldEditor extends SelectFieldEditor {

    private List<String> selectedUsers = new ArrayList<>();

    public UserSelectFieldEditor(String fieldName, String issueKey, boolean required) {
        this(fieldName, issueKey, required, false);
    }

    public UserSelectFieldEditor(String fieldName, String issueKey, boolean required, boolean isMultiSelect) {
        super(fieldName, issueKey, required, isMultiSelect);
        myButtonAction = new UserPickerDialogAction();
    }

    @Override
    public JsonElement getJsonValue() {
        if(isEmpty(trim(myTextField.getText()))){
            return JsonNull.INSTANCE;
        }

        if(isMultiSelect){
            return createArrayNameObjects(selectedUsers);
        }

        return createNameObject(getFirstItem(selectedUsers));
    }


    private class UserPickerDialogAction extends PickerDialogAction {

        public UserPickerDialogAction() {
            super();
        }

        @Override
        public void actionPerformed(AnActionEvent e) {
            super.actionPerformed(e);
            if(nonNull(myJiraRestApi)){
                List<String> users = myJiraRestApi.getAssignableUsers(issueKey).stream().map(JiraIssueUser::getKey).collect(toList());
                UserPickerDialog dialog = new UserPickerDialog(myProject, users);
                dialog.show();
            }

        }
    }


    class UserPickerDialog extends PickerDialog<String> {

        public UserPickerDialog(@Nullable Project project, List<String> items) {
            super(project, "Users", items);
        }

        @Override
        protected void doOKAction() {
            selectedUsers = myList.getSelectedValuesList();
            myTextField.setText(selectedUsers.isEmpty() ? "" : String.join(", ", selectedUsers));


            super.doOKAction();
        }
    }




}
