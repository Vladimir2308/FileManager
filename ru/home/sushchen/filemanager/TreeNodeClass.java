package ru.home.sushchen.filemanager;

import java.io.File;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.filechooser.FileSystemView;

public class TreeNodeClass {
    String path;
    File file;
    private Icon icn;
    private String Name;
    private static FileSystemView fsv = FileSystemView.getFileSystemView();

    private TreeNodeClass() {
    }

    TreeNodeClass(String path) {
        this();
        this.path = path;

        file = fsv.createFileObject(path);
        if (fsv.getSystemIcon(file) == null)
            icn = new ImageIcon("images\\file.png");
        else
            icn = fsv.getSystemIcon(file);
    }

    TreeNodeClass(File file) {
        this();
        this.file = file;
        path = file.getAbsolutePath();

        if (fsv.getSystemIcon(file) == null)
            icn = new ImageIcon("images\\file.png");
        else
            icn = fsv.getSystemIcon(file);
    }

    void setName(String str) {
        Name = str;

    }

    Icon getIcon() {
        return icn;
    }

    static File[] getRoots() {
        return File.listRoots();
    }

    static File getDesktop() {
        return new File(System.getProperty("user.home"), "Desktop");
    }

    public String toString() {
        if (Name != null) {
            return Name;
        }
        return file.getName();
    }


    static boolean isDir(File file) {

        return file.isDirectory();
    }

    static String getFileExtension(File file) {
        if (TreeNodeClass.isDir(file))
            return "Папка с файлами";
        String mystr = file.toString();
        int index = mystr.lastIndexOf('.');
        return index == -1 ? null : mystr.substring(index);
    }

    static boolean isEmpty(File file) {
        if (file.exists()) {
            if (file.isDirectory()) {
                if (file.list() != null) {
                    return file.list().length <= 0;
                }
            }


        }
        return false;
    }
}
