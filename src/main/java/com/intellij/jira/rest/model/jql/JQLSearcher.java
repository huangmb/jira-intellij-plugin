package com.intellij.jira.rest.model.jql;

import java.util.Objects;

import static com.intellij.openapi.util.text.StringUtil.trim;

public class JQLSearcher {

    private String alias;
    private String jql;
    private boolean selected;

    public JQLSearcher(String jql) {
        this("Undefined", jql);
    }

    public JQLSearcher(String alias, String jql) {
        this.alias = trim(alias);
        this.jql = trim(jql);
    }

    public String getAlias() {
        return alias;
    }

    public String getJql() {
        return jql;
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
