package ru.home.sushchen.filemanager;

import java.io.File;
import java.sql.Date;
import java.text.SimpleDateFormat;

public class dateChanged implements Comparable<dateChanged> {
    /**
     * This Class changing information for a sorting in tableFiles
     * and change representation of date
     */
    private long date;
    File file;

    dateChanged(File file) {
        this.file = file;
        date = file.lastModified();
    }

    private long getDate() {
        return date;
    }

    public String toString() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy   HH:mm");
        return sdf.format(new Date(date));
    }

    public int compareTo(dateChanged dC) {
        return Long.compare(date, dC.getDate());
    }

}