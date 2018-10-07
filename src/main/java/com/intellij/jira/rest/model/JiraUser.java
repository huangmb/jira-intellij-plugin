package com.intellij.jira.rest.model;

import com.google.gson.annotations.SerializedName;

public class JiraUser {

    private String self;
    private String name;
    private String key;
    private String emailAdress;
    private String displayName;
    private JiraUser.Avatar avatarUrls;

    public JiraUser() { }

    public String getSelf() {
        return self;
    }

    public String getName() {
        return name;
    }

    public String getKey() {
        return key;
    }

    public String getEmailAdress() {
        return emailAdress;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getAvatarIcon(){
        return avatarUrls.xsmallIcon;
    }

    public class Avatar{

        @SerializedName("16x16")
        private String xsmallIcon;
        @SerializedName("24x24")
        private String smallIcon;
        @SerializedName("32x32")
        private String mediumIcon;
        @SerializedName("48x48")
        private String largeIcon;

        public Avatar() { }

    }

}
