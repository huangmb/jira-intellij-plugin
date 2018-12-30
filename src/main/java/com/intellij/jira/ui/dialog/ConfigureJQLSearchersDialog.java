package com.intellij.jira.ui.dialog;

import com.intellij.jira.components.JQLSearcherManager;
import com.intellij.jira.rest.model.jql.JQLSearcher;
import com.intellij.jira.tasks.RefreshIssuesTask;
import com.intellij.jira.util.SimpleSelectableList;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.ui.Messages;
import com.intellij.ui.ToolbarDecorator;
import com.intellij.ui.components.JBPanel;
import com.intellij.ui.table.TableView;
import com.intellij.util.ui.ColumnInfo;
import com.intellij.util.ui.ListTableModel;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.util.ArrayList;

import static com.intellij.jira.util.JiraLabelUtil.getBgRowColor;
import static com.intellij.jira.util.JiraLabelUtil.getFgRowColor;

public class ConfigureJQLSearchersDialog extends DialogWrapper {

    private final Project myProject;
    private final JQLSearcherManager myManager;

    private SimpleSelectableList<JQLSearcher> mySearchers;

    private final ColumnInfo<JQLSearcher, String> ALIAS_COLUMN = new AliasSearcherColumnInfo();
    private final ColumnInfo<JQLSearcher, String> JQL_COLUMN = new JQLSearcherColumnInfo();


    private TableView<JQLSearcher> myTable;
    private ListTableModel<JQLSearcher> myModel;

    public ConfigureJQLSearchersDialog(@NotNull Project project) {
        super(project, false);
        this.myProject = project;
        this.myManager = project.getComponent(JQLSearcherManager.class);

        init();
    }


    @Override
    protected void init() {
        mySearchers = new SimpleSelectableList<>();

        myModel = new ListTableModel(new ColumnInfo[]{ALIAS_COLUMN, JQL_COLUMN}, new ArrayList());
        for(JQLSearcher searcher : myManager.getSearchers()){
            JQLSearcher clone = searcher.clone();
            mySearchers.add(clone);
            myModel.addRow(clone);
        }

        mySearchers.selectItem(myManager.getSelectedSearcherIndex());
        myTable = new TableView<>(myModel);


        setTitle("Configure JQL Searcher");
        super.init();
    }

    @Nullable
    @Override
    protected JComponent createCenterPanel() {
        JBPanel myPanel = new JBPanel(new BorderLayout());
        myPanel.add(ToolbarDecorator.createDecorator(myTable)
                        .setAddAction(button -> {
                            NewJQLSearcherDialog dlg = new NewJQLSearcherDialog(myProject, false);
                            if (dlg.showAndGet()) {
                                mySearchers.add(dlg.getJqlSearcher(), dlg.isSelectedSearcher());
                                myModel.addRow(dlg.getJqlSearcher());
                                myModel.fireTableDataChanged();
                            }
                        })
                        .setEditAction(button -> {
                            int selRow = myTable.getSelectedRow();
                            boolean isDefaultSearcher = selRow == mySearchers.getSelectedItemIndex();
                            JQLSearcher selectedSearcher = getSelectedJQLSearcher();
                            EditJQLSearcherDialog dlg = new EditJQLSearcherDialog(myProject, selectedSearcher, isDefaultSearcher, false);

                            if (dlg.showAndGet()) {
                                mySearchers.update(selRow, selectedSearcher, dlg.isSelectedSearcher());
                                myModel.fireTableDataChanged();
                            }
                        })
                        .setRemoveAction(button -> {
                            if (Messages.showOkCancelDialog(myProject, "You are going to delete this searcher, are you sure?","Delete Searcher", Messages.getQuestionIcon()) == Messages.OK) {

                                mySearchers.remove(myTable.getSelectedRow());
                                myModel.removeRow(myTable.getSelectedRow());
                                myModel.fireTableDataChanged();
                            }
                        })
                        .disableUpDownActions().createPanel(), BorderLayout.CENTER);



        return myPanel;
    }



    private JQLSearcher getSelectedJQLSearcher(){
        return myModel.getItem(myTable.getSelectedRow());
    }

    @Override
    protected void doOKAction() {
        myManager.setSearchers(mySearchers);
        new RefreshIssuesTask(myProject).queue();

        super.doOKAction();
    }





    private class JQLSearcherTableCellRenderer extends DefaultTableCellRenderer{

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

            if(row == mySearchers.getSelectedItemIndex()){
                setBackground(getBgRowColor(isSelected));
                setForeground(getFgRowColor(isSelected));
            }
            else{
                setBackground(isSelected ? table.getSelectionBackground() : table.getBackground());
                setForeground(isSelected ? table.getSelectionForeground() : table.getForeground());
            }

            return this;
        }
    }


    private abstract class SearcherColumnInfo extends ColumnInfo<JQLSearcher, String>{

        private final JQLSearcherTableCellRenderer JQL_SEARCHER_RENDERER = new JQLSearcherTableCellRenderer();

        public SearcherColumnInfo(String name) {
            super(name);
        }

        @Nullable
        @Override
        public TableCellRenderer getRenderer(JQLSearcher jqlSearcher) {
            return JQL_SEARCHER_RENDERER;
        }
    }


    private class AliasSearcherColumnInfo extends SearcherColumnInfo{

        public AliasSearcherColumnInfo() {
            super("Alias");
        }

        @Nullable
        @Override
        public String valueOf(JQLSearcher jqlSearcher) {
            return jqlSearcher.getAlias();
        }
    }

    private class JQLSearcherColumnInfo extends SearcherColumnInfo{

        public JQLSearcherColumnInfo() {
            super("JQL");
        }

        @Nullable
        @Override
        public String valueOf(JQLSearcher jqlSearcher) {
            return jqlSearcher.getJql();
        }
    }




}
