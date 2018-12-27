package com.intellij.jira.server;

import com.intellij.jira.tasks.RefreshIssuesTask;
import com.intellij.jira.util.JiraPanelUtil;
import com.intellij.jira.util.SimpleSelectableList;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.ui.Splitter;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.ui.*;
import com.intellij.ui.components.JBList;
import com.intellij.ui.components.JBPanel;
import com.intellij.util.containers.ConcurrentFactoryMap;
import com.intellij.util.ui.UI;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static java.util.Objects.nonNull;

public class ConfigureJiraServersDialog extends DialogWrapper {

    private final static JPanel EMPTY_PANEL = JiraPanelUtil.createPlaceHolderPanel("No servers.");
    private final static String EMPTY_PANEL_NAME = "empty.panel";

    private final Project myProject;
    private final JiraServerManager2 myManager;

    private SimpleSelectableList<JiraServer2> myServers;
    private JBList<JiraServer2> myServersList;
    private List<JiraServerEditor> myEditors = new ArrayList<>();

    private JPanel myJiraServerEditor;
    private Splitter mySplitter;

    private int count;
    private final Map<JiraServer2, String> myServerNames = ConcurrentFactoryMap.createMap(server -> Integer.toString(count++));

    public ConfigureJiraServersDialog(@NotNull Project project) {
        super(project, false);
        this.myProject = project;
        this.myManager = project.getComponent(JiraServerManager2.class);
        init();
    }


    @Override
    protected void init() {
        myJiraServerEditor = new JPanel(new CardLayout());
        myJiraServerEditor.add(EMPTY_PANEL, EMPTY_PANEL_NAME);
        myServers = myManager.getJiraServers();
        CollectionListModel listModel = new CollectionListModel(new ArrayList());
        for(JiraServer2 server : myServers.getItems()){
            JiraServer2 clone = server.clone();
            listModel.add(clone);
        }


        for(JiraServer2 server : myServers.getItems()){
            addJiraServerEditor(server);
        }


        myServersList = new JBList();
        myServersList.setModel(listModel);
        myServersList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        myServersList.setSelectedIndex(myServers.getSelectedItemIndex());

        myServersList.addListSelectionListener(e -> {
            JiraServer2 selectedServer = getSelectedServer();
            if(nonNull(selectedServer)) {
                String name = myServerNames.get(selectedServer);
                ((CardLayout) myJiraServerEditor.getLayout()).show(myJiraServerEditor, name);
                mySplitter.doLayout();
                mySplitter.repaint();
            }
        });

        myServersList.setCellRenderer(new ColoredListCellRenderer() {
            @Override
            protected void customizeCellRenderer(@NotNull JList list, Object value, int index, boolean selected, boolean hasFocus) {
                JiraServer2 server = (JiraServer2)value;
                append(StringUtil.isEmpty(server.getUrl()) ? "<undefined>" : server.getUrl(), SimpleTextAttributes.REGULAR_ATTRIBUTES);
            }
        });

        setTitle("Configure Servers");
        super.init();
    }

    @Nullable
    @Override
    protected JComponent createCenterPanel() {
        mySplitter = new JBSplitter(true, 0.6f);
        mySplitter.setFirstComponent(createServersPanel());
        mySplitter.setSecondComponent(createDetailsServerPanel());

        return mySplitter;
    }


    @Override
    protected void doOKAction() {
        myManager.setJiraServers(myServers);
        updateIssues();

        super.doOKAction();
    }

    private JComponent createServersPanel() {
        JBPanel myPanel = new JBPanel(new BorderLayout());
        myPanel.setMinimumSize(UI.size(-1, 100));
        myPanel.add(ToolbarDecorator.createDecorator(myServersList)
                        .setAddAction(button -> {
                            addJiraServer();
                        })
                        .setRemoveAction(button -> {
                            if (Messages.showOkCancelDialog(myProject, "You are going to delete this server, are you sure?","Delete Server", Messages.getQuestionIcon()) == Messages.OK) {
                                removeJiraServer();
                            }
                        })
                        .disableUpDownActions().createPanel(), BorderLayout.CENTER);

        return myPanel;
    }


    private void addJiraServer(){
        JiraServer2 server = new JiraServer2();
        myServers.add(server);
        ((CollectionListModel) myServersList.getModel()).add(server);
        addJiraServerEditor(server);
        myServersList.setSelectedIndex(myServersList.getModel().getSize() - 1);
    }

    private void addJiraServerEditor(JiraServer2 server){
        JiraServerEditor editor = new JiraServerEditor(server);
        myEditors.add(editor);
        String name = myServerNames.get(server);
        myJiraServerEditor.add(editor.getPanel(), name);
        myJiraServerEditor.doLayout();
    }


    private void removeJiraServer(){
        int selectedServer = myServersList.getSelectedIndex();
        if(selectedServer > -1){
            ((CollectionListModel) myServersList.getModel()).remove(selectedServer);
            myServers.remove(selectedServer);
            myServersList.setSelectedIndex(myServers.getSelectedItemIndex());
        }
    }


    private void updateIssues(){
        new RefreshIssuesTask(myProject).queue();
    }

    private boolean isDefaultServer(){
        return myServers.getSelectedItemIndex() == myServersList.getSelectedIndex();
    }

    private JComponent createDetailsServerPanel() {
        return myJiraServerEditor;
    }

    @Nullable
    private JiraServer2 getSelectedServer(){
        return myServersList.getSelectedValue();
    }
}
