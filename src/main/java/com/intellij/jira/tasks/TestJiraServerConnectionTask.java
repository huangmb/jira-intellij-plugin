package com.intellij.jira.tasks;

import com.intellij.jira.server.JiraRestApi;
import com.intellij.jira.server.JiraServer;
import com.intellij.jira.server.JiraServerManager;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.progress.ProcessCanceledException;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.progress.Task;
import com.intellij.openapi.project.Project;
import com.intellij.tasks.TaskRepository;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class TestJiraServerConnectionTask extends Task.Modal {

    private Project myProject;
    private Exception myException;
    private TaskRepository.CancellableConnection myConnection;
    private JiraServer myServer;

    public TestJiraServerConnectionTask(@Nullable Project project, @NotNull JiraServer server) {
        super(project, "Test connection", true);
        this.myProject = project;
        this.myServer = server;
        this.myConnection = new TaskRepository.CancellableConnection() {
            protected void doTest() throws Exception {
                JiraRestApi api = myProject.getComponent(JiraServerManager.class).getJiraRestApiFrom(myServer);
                api.testConnection();
            }

            public void cancel() { }
        };
    }

    @Override
    public void run(@NotNull ProgressIndicator indicator) {
        indicator.setText("Connecting to " + myServer.getUrl() + "...");
        indicator.setFraction(0);
        indicator.setIndeterminate(true);
        try {

            Future<Exception> future = ApplicationManager.getApplication().executeOnPooledThread(myConnection);
            while (true) {
                try {
                    myException = future.get(100, TimeUnit.MILLISECONDS);
                    return;
                }
                catch (TimeoutException ignore) {
                    try {
                        indicator.checkCanceled();
                    }
                    catch (ProcessCanceledException e) {
                        myException = e;
                        myConnection.cancel();
                        return;
                    }
                }
                catch (Exception e) {
                    myException = e;
                    return;
                }
            }
        }
        catch (Exception e) {
            myException = e;
        }
    }

    @Override
    public void onCancel() {
        if (myConnection != null) {
            myConnection.cancel();
        }
    }

    public Exception getException() {
        return myException;
    }
}
