package com.intellij.jira.ui.editors;

import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.intellij.jira.rest.model.JiraCustomFieldOption;
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

public class OptionSelectFieldEditor extends DataSelectFieldEditor<JiraCustomFieldOption> {

    OptionSelectFieldEditor(String fieldName, String issueKey, boolean required, boolean isMultiSelect, List<JiraCustomFieldOption> items) {
        super(fieldName, issueKey, required, isMultiSelect, items);
        myButtonAction = new OptionPickerDialogAction();
    }

    @Override
    public JsonElement getJsonValue() {
        if(isEmpty(trim(myTextField.getText()))){
            return JsonNull.INSTANCE;
        }

        List<String> values = selectedItems.stream().map(JiraCustomFieldOption::getValue).collect(toList());
        if(isMultiSelect){
            return createArrayObject("value", values);
        }

        return createObject("value", getFirstItem(values));
    }

    class OptionPickerDialogAction extends PickerDialogAction{

        OptionPickerDialogAction() { }

        @Override
        public void actionPerformed(AnActionEvent e) {
            Project project = e.getProject();
            if(nonNull(project)){
                new OptionPickerDialog(project).show();
            }
        }
    }



    class OptionPickerDialog extends PickerDialog<JiraCustomFieldOption>{

        OptionPickerDialog(@Nullable Project project) {
            super(project, "Options", myItems);
        }

        @Override
        protected void doOKAction() {
            selectedItems = myList.getSelectedValuesList();
            myTextField.setText(selectedItems.isEmpty() ? "" :  selectedItems.stream().map(JiraCustomFieldOption::getValue).collect(joining(", ")));


            super.doOKAction();
        }
    }


}
