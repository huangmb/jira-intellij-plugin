package com.intellij.jira.ui.dialog;

import com.intellij.jira.components.JQLSearcherManager;
import com.intellij.jira.rest.model.jql.JQLSearcher;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.ui.Messages;
import com.intellij.ui.ToolbarDecorator;
import com.intellij.ui.components.JBPanel;
import com.intellij.ui.table.JBTable;
import com.intellij.util.ui.ColumnInfo;
import com.intellij.util.ui.ListTableModel;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class ConfigureJQLSearchersDialog extends DialogWrapper {

    private final Project myProject;
    private final ColumnInfo<JQLSearcher, String> ALIAS_COLUMN = new ColumnInfo<JQLSearcher, String>("Alias") {
        @Override
        public String valueOf(JQLSearcher jqlSearcher) {
            return jqlSearcher.getAlias();
        }
    };

    private final ColumnInfo<JQLSearcher, String> JQL_COLUMN = new ColumnInfo<JQLSearcher, String>("JQL") {
        @Override
        public String valueOf(JQLSearcher jqlSearcher) {
            return jqlSearcher.getJql();
        }
    };

    private final ColumnInfo<JQLSearcher, String> DEFAULT_COLUMN = new ColumnInfo<JQLSearcher, String>("Default") {
        @Override
        public String valueOf(JQLSearcher jqlSearcher) {
            return jqlSearcher.isDefault() ? "Y" : "";
        }
    };


    private final JBTable myTable;
    private List<JQLSearcher> mySearchers;
    private ListTableModel<JQLSearcher> myModel;

    public ConfigureJQLSearchersDialog(@NotNull Project project) {
        super(project, false);
        this.myProject = project;
        this.myTable = new JBTable();

        JQLSearcherManager manager = this.myProject.getComponent(JQLSearcherManager.class);

        mySearchers = new ArrayList<>();
        for(JQLSearcher jqlSearcher:  manager.getJQLSearchers()) {
            mySearchers.add(new JQLSearcher(jqlSearcher.getAlias(), jqlSearcher.getJql(), jqlSearcher.isDefault()));
        }
        myModel = new ListTableModel<>(new ColumnInfo[]{ALIAS_COLUMN, JQL_COLUMN, DEFAULT_COLUMN}, mySearchers,0);
        myTable.setModel(myModel);

        setTitle("Configure JQL Searcher");
        init();
    }

    @Nullable
    @Override
    protected JComponent createCenterPanel() {
        JBPanel myPanel = new JBPanel(new BorderLayout());
        myPanel.add(ToolbarDecorator.createDecorator(myTable)
                        .setAddAction(button -> {
                            NewJQLSearcherDialog dlg = new NewJQLSearcherDialog(myProject);
                            if (dlg.showAndGet()) {
                                mySearchers.add(dlg.getJqlSearcher());
                                myModel.fireTableDataChanged();
                            }
                        })
                        .setEditAction(button -> {
                            int selRow = myTable.getSelectedRow();
                            JQLSearcher selectedSearcher = myModel.getItem(myTable.getSelectedRow());
                            EditJQLSearcherDialog dlg = new EditJQLSearcherDialog(myProject, selectedSearcher);

                            if (dlg.showAndGet()) {
                                mySearchers.remove(selRow);
                                mySearchers.add(selRow, dlg.getJqlSearcher());
                                myModel.fireTableDataChanged();
                            }
                        })
                        .setRemoveAction(button -> {
                            if (Messages.showOkCancelDialog(myProject, "You are going to delete this searcher, are you sure?","Delete Searcher", Messages.getQuestionIcon()) == Messages.OK) {

                                JQLSearcher selectedSearcher = myModel.getItem(myTable.getSelectedRow());
                                this.myProject.getComponent(JQLSearcherManager.class).remove(selectedSearcher);

                                mySearchers.remove(myTable.getSelectedRow());
                                myModel.fireTableDataChanged();

                            }
                        })
                        .disableUpDownActions().createPanel(), BorderLayout.CENTER);



        return myPanel;
    }
}
