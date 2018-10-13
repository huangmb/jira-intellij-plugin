package com.intellij.jira.ui.editors;

import javax.swing.*;

public interface FieldEditor {


     JComponent getLabel();

     String getLabelValue();

     JComponent getInput();

     String getInputValue();


}
