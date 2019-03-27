package com.intellij.jira.ui.dialog;

import com.intellij.jira.server.JiraServer;
import com.intellij.jira.server.JiraServerEditor;
import com.intellij.jira.server.JiraServerManager;
import com.intellij.jira.tasks.RefreshIssuesTask;
import com.intellij.jira.util.JiraPanelUtil;
import com.intellij.jira.util.SimpleSelectableList;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.ui.Splitter;
import com.intellij.ui.*;
import com.intellij.ui.components.JBList;
import com.intellij.ui.components.JBPanel;
import com.intellij.util.containers.ConcurrentFactoryMap;
import com.intellij.util.ui.UI;
import icons.TasksCoreIcons;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import static com.intellij.openapi.ui.Messages.CANCEL_BUTTON;
import static com.intellij.openapi.ui.Messages.OK_BUTTON;
import static java.util.Objects.nonNull;

public class ConfigureJiraServersDialog extends DialogWrapper {

    private final static JPanel EMPTY_PANEL = JiraPanelUtil.createPlaceHolderPanel("No server selected.").withMinimumWidth(450).withMinimumHeight(100);
    private final static String EMPTY_PANEL_NAME = "empty.panel";

    private final Project myProject;
    private final JiraServerManager myManager;

    private SimpleSelectableList<JiraServer> myServers;
    private JBList<JiraServer> myServersList;
    private List<JiraServerEditor> myEditors = new ArrayList<>();

    private JPanel myJiraServerEditor;
    private Splitter mySplitter;

    private int count;
    private final Map<JiraServer, String> myServerNames = ConcurrentFactoryMap.createMap(server -> Integer.toString(count++));

    private BiConsumer<JiraServer, Boolean> myChangeListener;
    private Consumer<JiraServer> myChangeUrlListener;


    public ConfigureJiraServersDialog(@NotNull Project project) {
        super(project, false);
        this.myProject = project;
        this.myManager = project.getComponent(JiraServerManager.class);
        init();
    }


    @Override
    protected void init() {
        myJiraServerEditor = new JPanel(new CardLayout());
        myJiraServerEditor.add(EMPTY_PANEL, EMPTY_PANEL_NAME);

        myServers = new SimpleSelectableList<>();

        CollectionListModel listModel = new CollectionListModel(new ArrayList());
        for(JiraServer server : myManager.getJiraServers()){
            JiraServer clone = server.clone();
            listModel.add(clone);
            myServers.add(clone);
        }

        myServers.selectItem(myManager.getSelectedJiraServerIndex());

        this.myChangeListener = (server, selected) -> myServers.updateSelectedItem(server, selected);
        this.myChangeUrlListener = (server) -> ((CollectionListModel)myServersList.getModel()).contentsChanged(server);


        for(int i = 0; i < myServers.getItems().size(); i++){
            addJiraServerEditor(myServers.getItems().get(i), i == myManager.getSelectedJiraServerIndex());
        }


        myServersList = new JBList();
        myServersList.setEmptyText("No servers");
        myServersList.setModel(listModel);
        myServersList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        myServersList.addListSelectionListener(e -> {
            JiraServer selectedServer = getSelectedServer();
            if(nonNull(selectedServer)) {
                String name = myServerNames.get(selectedServer);
                updateEditorPanel(name);
            }
        });

        myServersList.setCellRenderer(new ColoredListCellRenderer() {
            @Override
            protected void customizeCellRenderer(@NotNull JList list, Object value, int index, boolean selected, boolean hasFocus) {
                JiraServer server = (JiraServer)value;
                setIcon(TasksCoreIcons.Jira);
                append(server.getPresentableName(), SimpleTextAttributes.REGULAR_ATTRIBUTES);
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

        if(myServers.hasSelectedItem()){
            myServersList.setSelectedValue(myServers.getSelectedItem(), true);
        }

        JBPanel myPanel = new JBPanel(new BorderLayout());
        myPanel.setMinimumSize(UI.size(-1, 200));
        myPanel.add(ToolbarDecorator.createDecorator(myServersList)
                        .setAddAction(button -> {
                            addJiraServer();
                        })
                        .setRemoveAction(button -> {
                            if (Messages.showOkCancelDialog(myProject, "You are going to delete this server, are you sure?","Delete Server", OK_BUTTON, CANCEL_BUTTON, Messages.getQuestionIcon()) == Messages.OK) {
                                removeJiraServer();
                            }
                        })
                        .disableUpDownActions().createPanel(), BorderLayout.CENTER);

        return myPanel;
    }


    private void addJiraServer(){
        JiraServer server = new JiraServer();
        myServers.add(server);
        ((CollectionListModel) myServersList.getModel()).add(server);
        addJiraServerEditor(server, false);
        myServersList.setSelectedIndex(myServersList.getModel().getSize() - 1);
    }

    private void addJiraServerEditor(JiraServer server, boolean selected){
        JiraServerEditor editor = new JiraServerEditor(myProject,server, selected, myChangeListener, myChangeUrlListener);
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

        if(myServers.isEmpty()){
            updateEditorPanel(EMPTY_PANEL_NAME);
        }


    }

    private void updateEditorPanel(String name){
        ((CardLayout) myJiraServerEditor.getLayout()).show(myJiraServerEditor, name);
        mySplitter.doLayout();
        mySplitter.repaint();
    }

    private void updateIssues(){
        new RefreshIssuesTask(myProject).queue();
    }


    private JComponent createDetailsServerPanel() {
        return myJiraServerEditor;
    }

    @Nullable
    private JiraServer getSelectedServer(){
        return myServersList.getSelectedValue();
    }
}
