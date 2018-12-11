package com.intellij.jira.server;

import com.intellij.tasks.config.TaskSettings;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.auth.AuthScope;

public class JiraRestClient2 {

    private final JiraServer2 myServer;
    private final HttpClient myClient;

    public JiraRestClient2(JiraServer2 myServer) {
        this.myServer = myServer;
        this.myClient = this.createClient();
    }


    private HttpClient createClient() {
        HttpClient client = new HttpClient(new MultiThreadedHttpConnectionManager());
        this.configureHttpClient(client);
        return client;
    }

    private void configureHttpClient(HttpClient client) {
        client.getParams().setConnectionManagerTimeout(3000L);
        client.getParams().setSoTimeout(TaskSettings.getInstance().CONNECTION_TIMEOUT);

        client.getParams().setCredentialCharset("UTF-8");
        client.getParams().setAuthenticationPreemptive(true);
        client.getState().setCredentials(AuthScope.ANY, new UsernamePasswordCredentials(this.getUsername(), this.getPassword()));
    }

    private String getUsername() {
        return myServer.getUsername();
    }

    private String getPassword() {
        return myServer.getPassword();
    }


}
