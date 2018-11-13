package com.intellij.jira.ui.editors;

import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.intellij.jira.rest.model.JiraProjectVersion;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import static com.intellij.jira.util.JiraGsonUtil.createArrayObject;
import static com.intellij.jira.util.JiraGsonUtil.createObject;
import static com.intellij.openapi.util.text.StringUtil.isEmpty;
import static com.intellij.openapi.util.text.StringUtil.trim;
import static com.intellij.util.containers.ContainerUtil.getFirstItem;
import static java.util.Objects.nonNull;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;


public class VersionSelectFieldEditor extends DataSelectFieldEditor<JiraProjectVersion> {

    public VersionSelectFieldEditor(String fieldName, String issueKey, boolean required, boolean isMultiSelect, List<JiraProjectVersion> items) {
        super(fieldName, issueKey, required, isMultiSelect, items);
        myButtonAction = new VersionPickerDialogAction();
    }

    @Override
    public JsonElement getJsonValue() {
        if(isEmpty(trim(myTextField.getText()))){
            return JsonNull.INSTANCE;
        }

        List<String> values = selectedItems.stream().map(JiraProjectVersion::getName).collect(toList());
        if(isMultiSelect){
            return createArrayObject("key", values);
        }

        return createObject("key", getFirstItem(values));
    }


    class VersionPickerDialogAction extends PickerDialogAction{

        public VersionPickerDialogAction() { }

        @Override
        public void actionPerformed(AnActionEvent e) {
            Project project = e.getProject();
            if(nonNull(project)){
                new VersionPickerDialog(project).show();
            }
        }
    }



    class VersionPickerDialog extends PickerDialog<JiraProjectVersion>{

        VersionPickerDialog(@Nullable Project project) {
            super(project, "Versions", myItems);
        }

        @Override
        protected void doOKAction() {
            selectedItems = myList.getSelectedValuesList();
            myTextField.setText(selectedItems.isEmpty() ? "" :  selectedItems.stream().map(JiraProjectVersion::getName).collect(joining(", ")));


            super.doOKAction();
        }
    }


}
