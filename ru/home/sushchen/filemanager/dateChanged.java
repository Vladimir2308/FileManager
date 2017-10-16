package ru.home.sushchen.filemanager;

import java.io.File;
import java.sql.Date;
import java.text.SimpleDateFormat;

public class dateChanged implements Comparable<dateChanged> {
	long date;
	File file;

	public dateChanged(File file) {
		this.file = file;
		date = file.lastModified();
	}

	public long getDate() {
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