package ru.home.sushchen.filemanager;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.attribute.BasicFileAttributes;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.filechooser.FileSystemView;

public class TreeNodeClass {
    // Этот класс содержит необходимую информацию для раскрытии или выделения в
    // Jtree или в Jtable записи
    String[] listFiles;
    String path;
    File file;
    File[] files;
    Boolean dir;
    Boolean added;
    Icon icn;
    Boolean isEmpty;
    String Name;
    static FileSystemView fsv = FileSystemView.getFileSystemView();

    public TreeNodeClass() {
	added = false;
    }

    public TreeNodeClass(String path) {
	this();
	this.path = path;

	file = fsv.createFileObject(path);
	if (fsv.getSystemIcon(file) == null)
	    icn = new ImageIcon("images\\file.png");
	else
	    icn = fsv.getSystemIcon(file);
    }

    public TreeNodeClass(File file) {
	this();
	this.file = file;
	path = file.getAbsolutePath();

	if (fsv.getSystemIcon(file) == null)
	    icn = new ImageIcon("images\\file.png");
	else
	    icn = fsv.getSystemIcon(file);
    }

    public void setName(String str) {
	Name = str;

    }

    public Icon getIcon() {
	return icn;
    }

    public static String getattrs(File file) {
	BasicFileAttributes attrs = null;
	try {
	    attrs = Files.readAttributes(file.toPath(),
		    BasicFileAttributes.class, LinkOption.NOFOLLOW_LINKS);
	} catch (IOException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
	return " " + attrs.isDirectory() + "	size:    " + attrs.size();
    }

    public static File[] getRoots() {
	return File.listRoots();
    }

    public static boolean isRoots(File file) {
	File[] listRoot = File.listRoots();
	for (File f : listRoot) {
	    if (f.equals(file))
		return true;
	}
	return false;
    }

    public static File getDesktop() {
	File desktop = new File(System.getProperty("user.home"), "Desktop");
	return desktop;
    }

    public File Parent() {
	return File.listRoots()[0].getParentFile();
    }

    public String toString() {
	if (Name != null) {
	    return Name;
	}
	return file.getName();
    }

    public static File[] getFiles(File file) {
	return file.listFiles();
    }

    public static String getPath(File file) {
	return file.getAbsolutePath();
	// return(file.getAbsolutePath());
    }

    public String getPathCan() {
	try {
	    return (file.getCanonicalPath());
	} catch (IOException e) {

	    e.printStackTrace();
	}
	return "No Path";
    }

    public static boolean isDir(File file) {

	return file.isDirectory();
    }

    public static String getFileExtension(File file) {
	if (TreeNodeClass.isDir(file))
	    return "Папка с файлами";
	String mystr = file.toString();
	int index = mystr.lastIndexOf('.');
	return index == -1 ? null : mystr.substring(index);
    }

    public static boolean isEmpty(File file) {
	if (file.exists()) {
	    if (file.isDirectory()) {
		if (file.list().length > 0) {
		    return false;
		} else {
		    return true;
		}
	    } else {
		return false;
	    }
	}
	return false;
    }
}
