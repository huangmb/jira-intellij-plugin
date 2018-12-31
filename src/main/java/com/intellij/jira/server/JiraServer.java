package com.intellij.jira.server;

import com.intellij.openapi.util.PasswordUtil;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.util.xmlb.annotations.Attribute;
import com.intellij.util.xmlb.annotations.Tag;
import com.intellij.util.xmlb.annotations.Transient;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

import static com.intellij.openapi.util.text.StringUtil.trim;

@Tag("JiraServer")
public class JiraServer {

    private String url;
    private String username;
    private String password;

    public JiraServer() { }

    public JiraServer(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
    }

    public JiraServer(JiraServer other){
        this(other.getUrl(), other.getUsername(), other.getPassword());
    }

    @Attribute("url")
    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Attribute("username")
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

    @Attribute("password")
    public String getEncodedPassword() {
        return PasswordUtil.encodePassword(this.getPassword());
    }

    public void setEncodedPassword(String password) {
        try {
            this.setPassword(PasswordUtil.decodePassword(password));
        } catch (NumberFormatException var3) { }
    }


    @Transient
    public String getPresentableName(){
        return StringUtil.isEmpty(trim(getUrl())) ? "<undefined>" : getUrl();
    }


    @NotNull
    @Override
    public JiraServer clone(){
        return new JiraServer(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        JiraServer that = (JiraServer) o;
        return url.equals(that.url);
    }

    @Override
    public int hashCode() {
        return Objects.hash(url);
    }


    @Override
    public String toString() {
        return getPresentableName();
    }
}
