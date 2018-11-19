package com.intellij.jira.rest.model.jql;

import com.intellij.openapi.util.text.StringUtil;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

import static com.intellij.openapi.util.text.StringUtil.trim;

public class JQLSearcher {

    private String alias;
    private String jql;
    private boolean selected;

    public JQLSearcher(String jql) {
        this(null, jql, false);
    }

    public JQLSearcher(@Nullable String alias, String jql) {
        this(alias, jql, false);
    }

    public JQLSearcher(@Nullable String alias, String jql, boolean isDefault) {
        setAlias(alias);
        setJql(jql);
        setSelected(isDefault);
    }

    public String getAlias() {
        return alias;
    }

    private void setAlias(String alias) {
        this.alias = StringUtil.isEmpty(alias) ? "Undefined" : trim(alias);
    }

    public String getJql() {
        return jql;
    }

    private void setJql(String jql) {
        this.jql = trim(jql);
    }

    public boolean isDefault(){
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public boolean isEditable(){
        return true;
    }


    @Override
    public String toString() {
        return alias;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        JQLSearcher that = (JQLSearcher) o;
        return selected == that.selected &&
                Objects.equals(alias, that.alias) &&
                Objects.equals(jql, that.jql);
    }

    @Override
    public int hashCode() {
        return Objects.hash(alias, jql, selected);
    }
}
