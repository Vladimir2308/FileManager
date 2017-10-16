package ru.home.sushchen.filemanager;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Vector;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

class Controller {

	/**Controller manage all aplication*/

	private TreeClass treeObject;
	private TableFilesClass tableFiles;

    private ArrayList<JButton> buttons;
    private Vector<Vector<Object>> list;
    private boolean cut;
    private boolean copyFail = false;
	private LinkedList<File> filesToCopy;
    private Vector<Vector<Object>> listCut;
    private MainWindow mainWin;

    @SuppressWarnings("unchecked")
    void setObjects(MainWindow mainWin, TreeClass treeObject,
					TableFilesClass tableFiles,
					ArrayList<JButton> buttons) {
	this.mainWin = mainWin;
	this.buttons = buttons;
	this.treeObject = treeObject;
	this.tableFiles = tableFiles;

	list = tableFiles.getdataList();

	filesToCopy = new LinkedList<>();
    }

    void newDir() {
	treeObject.newDir();
    }

   void copy() {

	int size = list.size();

	if (size == 0) {
	    showPane("Выберите файлы или папки");
	} else {
		for (Vector<Object> aList : list) {
			@SuppressWarnings("rawtypes")
			File file = (File) aList.get(0);
			if (!filesToCopy.add(file))
				copyFail = true;
			buttons.get(3).setEnabled(true);

		}
	}

    }

    void setDefaultCursor() {
	mainWin.setDefaultCursor();
    }

    void setWaitCursor() {
	mainWin.setWaitCursor();
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    void cut() {
	copy();
	cut = true;
	listCut = (Vector) list.clone();
    }

    private void deleteAfterCut() {
	if (copyFail) {
	    copyFail = false;
	    showPane("Не удалось вставить файл или папку");
	    return;
	}
	int size = listCut.size();
	if (size != 0) {
		for (Vector<Object> aListCut : listCut) {
			@SuppressWarnings("rawtypes")
			File file = ((File) aListCut.get(0));
			if (Stack.deleteFiles(file)) {
				treeObject.removeNodeFromTree(file);

			} else
				showPane("Не удалось удалить файл или папку");
		}
	}
	cut = false;
    }

    void insert() throws IOException {
	setWaitCursor();
	File fileSelected;
	fileSelected = treeObject.getSelectedForInsert();
		for (File aFilesToCopy : filesToCopy) {
			Stack.copyFiles(aFilesToCopy, fileSelected);
		}
	filesToCopy.clear();
	buttons.get(3).setEnabled(false);
	list.clear();
	TableFilesClass.getTableModel().ShowContent(fileSelected);
	TableList.updatelist();
	if (cut)
	    deleteAfterCut();
	setDefaultCursor();
    }

    @SuppressWarnings("unchecked")
    void delete() {
	setWaitCursor();
	list = tableFiles.getdataList();
	int size = list.size();
	if (size == 0)
	    showPane("Выберите файлы или папки для удаления");
	else {
		for (Vector<Object> aList : list) {
			@SuppressWarnings("rawtypes")
			File file = ((File) aList.get(0));

			if (Stack.deleteFiles(file)) {


				treeObject.removeNodeFromTree(file);

			} else
				showPane("Неудалось удалить файл" + file);
		}
	    list.clear();
	    treeObject.update();
	    TableList.updatelist();
	    tableFiles.update();
	}
	setDefaultCursor();
    }

    @SuppressWarnings({ "unchecked", "static-access" })
    void rename() {
	setWaitCursor();

	list = tableFiles.getdataList();

	int size = list.size();
	if (size == 0)
	    showPane("Выберите файл или папку");
	else if (size > 1)
	    showPane("Выберите только один файл или папку");
	else {
	    @SuppressWarnings("rawtypes")
	    File file = (File) list.get(0).get(0);
	    System.out.println("tableFiles.pressed " + tableFiles.pressed);

	    treeObject.setSelectionPath(tableFiles.getTableModel()
		    .getSelectedtreePaths());

	    if (!tableFiles.pressed) {
		tableFiles.setEditAndFocus(file);
		System.out.println("file2 " + file);
		tableFiles.pressed = true;
	    }
	}
	setDefaultCursor();
    }

    void about() {
	String version = "Программа, File Manager v 1.0, выполнена в учебных целях Сущень В.О. версия выпущена 16.10.2017 ";
	showPane(version);
    }

    void offButton(boolean isRoot) {
	if (!isRoot) {
	    for (JButton b : buttons) {
		if (b.getActionCommand().equals("Вставить")) {
		    if (filesToCopy.size() == 0) {
			b.setEnabled(false);
		    } else {
			b.setEnabled(true);
		    }
		    continue;
		}
		b.setEnabled(true);
	    }
	} else {
	    for (JButton b : buttons) {
		System.out.println(b.getActionCommand());
		if (b.getActionCommand().equals("О программе")) {
		    b.setEnabled(true);
		} else
		    b.setEnabled(false);
	    }
	}
    }

    void delListRow(int modelRow) {
	setWaitCursor();

	Vector<Object> vecDel = list.elementAt(modelRow);
	System.out.println("	vecDel " + Arrays.toString(vecDel.toArray()));
	list.remove(vecDel);
	System.out.println("List size " + list.size());

	TableList.updatelist();
	TreeNodeClass selected = treeObject.getSelectedTreeNodeClass();
	if (selected.path.equals("root"))
	    TableFilesClass.getTableModel().showRoot();
	else
	    TableFilesClass.getTableModel().ShowContent(selected.file);
	tableFiles.update();
	setDefaultCursor();
    }

    void clearList() {
	setWaitCursor();

	list.clear();

	TableList.updatelist();
	TreeNodeClass selected = treeObject.getSelectedTreeNodeClass();
	if (selected.path.equals("root"))
	    TableFilesClass.getTableModel().showRoot();
	else
	    TableFilesClass.getTableModel().ShowContent(selected.file);
	tableFiles.update();
	setDefaultCursor();
    }

    void openUpDir() {
	treeObject.setSelectionParentNode();
    }

    static void showPane(String message) {
	MainWindow.showPane(message);
    }

    static Object[] showPaneDFialog(String message, Object[] selectionValues) {
	final String TITLE_confirm = "Необходимо выбрать дейтствие";
		JPanel errorPanel = new JPanel();
	errorPanel.setOpaque(true);
	JCheckBox check = new JCheckBox(
		"Выполнять такой же выбор автоматически");
	Object[] selectionValuesWithCheck = new Object[selectionValues.length + 1];
	System.arraycopy(selectionValues, 0, selectionValuesWithCheck, 0,
		selectionValues.length);
	selectionValuesWithCheck[selectionValues.length] = check;
	int value = JOptionPane.showOptionDialog(errorPanel, message,
		TITLE_confirm, JOptionPane.DEFAULT_OPTION,
		JOptionPane.INFORMATION_MESSAGE, null,
		selectionValuesWithCheck, selectionValuesWithCheck[0]);
	if (check.isSelected())
	    System.out.println("Check is selected");
		return new Object[]{ value, check.isSelected() };

    }

}
