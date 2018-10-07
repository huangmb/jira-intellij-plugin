package com.intellij.jira.rest.model;

public class JiraProjectVersionDetails {

    private String id;
    private String name;
    private boolean released;
    private boolean archived;
    private DateInfo startDate;
    private DateInfo releaseDate;
    private StatusInfo status;

    public JiraProjectVersionDetails() { }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public boolean isReleased() {
        return released;
    }

    public boolean isArchived() {
        return archived;
    }

    public DateInfo getStartDate() {
        return startDate;
    }

    public DateInfo getReleaseDate() {
        return releaseDate;
    }

    public StatusInfo getStatus() {
        return status;
    }

    public class DateInfo{
        private String formated;
        private String iso;
        private String datePickerFormatted;

        public DateInfo() { }

        public String getFormated() {
            return formated;
        }

        public String getIso() {
            return iso;
        }

        public String getDatePickerFormatted() {
            return datePickerFormatted;
        }
    }

    public class StatusInfo{

        private JqlInfo toDo;
        private JqlInfo inProgress;
        private JqlInfo complete;

        public StatusInfo() { }


        public JqlInfo getToDo() {
            return toDo;
        }

        public JqlInfo getInProgress() {
            return inProgress;
        }

        public JqlInfo getComplete() {
            return complete;
        }

        public class JqlInfo{
            private int count;
            private String jqlUrl;

            public JqlInfo() { }

            public String getCount() {
                return String.valueOf(count);
            }

            public String getJqlUrl() {
                return jqlUrl;
            }
        }

    }


}
