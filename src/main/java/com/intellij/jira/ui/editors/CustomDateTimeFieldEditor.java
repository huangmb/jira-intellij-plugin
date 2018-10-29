package com.intellij.jira.ui.editors;

import java.sql.Date;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;


public class CustomDateTimeFieldEditor extends CustomDateFieldEditor {

    private static final SimpleDateFormat DATE_TIME_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static final String ISO_FORMAT = "yyy-MM-dd'T'HH:mm:ss.ss+0000";

    public CustomDateTimeFieldEditor(String fieldName, String issueKey) {
        super(fieldName, issueKey);
        this.myDescriptionField = "Usage: YYYY-MM-DD HH:mm:ss";
    }


    @Override
    protected String getValue() {
        String dateTime = super.getValue();
        String[] words = dateTime.split(" ");
        if(words.length != 2){
            return "";
        }

        LocalDate ld = Date.valueOf(words[0]).toLocalDate();
        LocalTime lt = Time.valueOf(words[1]).toLocalTime();

        String isoDateTime = DateTimeFormatter.ofPattern(ISO_FORMAT).format(LocalDateTime.of(ld, lt));

        return isoDateTime;
    }

    @Override
    public SimpleDateFormat getDateFormat() {
        return DATE_TIME_FORMAT;
    }

}
