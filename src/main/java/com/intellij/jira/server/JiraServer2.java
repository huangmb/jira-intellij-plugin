package com.intellij.jira.server;

import com.intellij.openapi.util.PasswordUtil;
import com.intellij.util.xmlb.annotations.Tag;
import com.intellij.util.xmlb.annotations.Transient;

import java.util.Objects;

@Tag("JiraServer")
public class JiraServer2 {

    private String url;
    private String username;
    private String password;


    public JiraServer2(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
    }

    @Tag("url")
    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Tag("username")
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Transient
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Tag("password")
    public String getEncodedPassword() {
        return PasswordUtil.encodePassword(this.getPassword());
    }

    public void setEncodedPassword(String password) {
        try {
            this.setPassword(PasswordUtil.decodePassword(password));
        } catch (NumberFormatException var3) { }
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        JiraServer2 that = (JiraServer2) o;
        return url.equals(that.url);
    }

    @Override
    public int hashCode() {
        return Objects.hash(url);
    }


    @Override
    public String toString() {
        return getUrl();
    }
}