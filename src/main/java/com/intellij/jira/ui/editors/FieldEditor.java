package com.intellij.jira.ui.editors;

import com.google.gson.JsonElement;

import javax.swing.*;
import java.util.Map;

public interface FieldEditor {

     JComponent createPanel();

     Map<String, String> getInputValues();

     JsonElement getJsonValue();

}
