package ru.home.sushchen.filemanager;

import java.io.File;
import java.text.NumberFormat;

public class fileSize implements Comparable<fileSize> {
    /**
     * This Class changing information for a sorting in tableFiles
     * and change representation of size of file
     */
        private Long size;
    File file;
    private NumberFormat nf;

    fileSize(File file) {
        this.file = file;
        if (file.isDirectory())
            size = 0L;
        else
            size = file.length();
        nf = NumberFormat.getInstance();
        nf.setMaximumFractionDigits(2);
    }

    private long getsize() {
        return size;
    }

    public String toString() {
        String stringSize;


        if (size == 0) {
            stringSize = "";
        } else if (size < 1024) {
            stringSize = nf.format(size.doubleValue()) + " байт";
        } else if (size < 1024 * 1024) {
            stringSize = nf.format(size.doubleValue() / 1024) + " Кбайт";
        } else if (size < 1024 * 1024 * 1024)
            stringSize = nf.format(size.doubleValue() / (1024 * 1024)) + " Мбайт";
        else
            stringSize = nf.format(size.doubleValue() / (1024 * 1024 * 1024)) + " Гбайт";
        return stringSize;
    }

    static String freeSpace(Long sizeSpace) {
        String stringSize;
        NumberFormat nf = NumberFormat.getInstance();
        nf.setMaximumFractionDigits(2);

        if (sizeSpace == 0)
            stringSize = "";
        else if (sizeSpace < 1024) {
            stringSize = nf.format(sizeSpace.doubleValue()) + " байт";
        } else if (sizeSpace < 1024 * 1024) {
            stringSize = nf.format(sizeSpace.doubleValue() / 1024) + " Кбайт";
        } else if (sizeSpace < 1024 * 1024 * 1024)
            stringSize = nf.format(sizeSpace.doubleValue() / (1024 * 1024)) + " Мбайт";
        else
            stringSize = nf.format(sizeSpace.doubleValue() / (1024 * 1024 * 1024)) + " Гбайт";

        return "Свободно: " + stringSize;
    }

    @Override
    public int compareTo(fileSize fS) {

        return Long.compare(size, fS.getsize());
    }
}
