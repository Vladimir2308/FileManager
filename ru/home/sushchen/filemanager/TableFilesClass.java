package ru.home.sushchen.filemanager;


import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;

import javax.swing.filechooser.FileSystemView;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import javax.swing.tree.TreePath;

import java.awt.Desktop;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.LinkedList;
import java.util.Vector;

public class TableFilesClass extends JPanel {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private static MyTableModel tableModel;
    Controller controller;
    public Box contents;
    protected JTable table;
    TreeClass treeObject;
    public Box jListBox;
    static Vector<Vector<Object>> list;
    private Object[][] data;
    int rowSelected = -1;
    TreePath SelectedtreePaths;

    // static LinkedList<File> listNull;
    public File open;
    public boolean pressed = false;

    public TableFilesClass(TreeClass treeObject) {
	this.treeObject = treeObject;
	tableModel = new MyTableModel();
	table = new JTable(tableModel);
	table.setFillsViewportHeight(true);
	table.setOpaque(true);
	table.setAutoCreateRowSorter(true);

	TableRowSorter<TableModel> sorter = new TableRowSorter<TableModel>(
		table.getModel());
	sorter.setSortable(0, false);
	table.setRowSorter(sorter);
	// Отключаем возможность сортировки по столбцу со иконками ,
	// так как в заголовке столбца кнопка открытия родительской папки
	
	
	table.addMouseListener(new MouseAdapter() {
	    public void mouseClicked(MouseEvent e) {
		if (e.getClickCount() == 2) {
		    Point p = e.getPoint();
		    int row = table.rowAtPoint(p);
		    int col = table.columnAtPoint(p);
		    if (!table.getColumnName(col).equals(
			    table.getModel().getColumnName(5))) {
			// проверяем что двойной щелчок был не по checkbox
			// нельзя сравнивать int так как столбцы таблицы можно
			// менять местами
			row = table.convertRowIndexToModel(row);
			// преобразуем номер отсортированной ячейки из View в
			// номер ячейки Model

			if (data[row][6].getClass().getName()
				.equals("java.io.File")) {

			    File expanededInTable = (File) data[row][6];

			    if (expanededInTable.isDirectory()) {
				addNodeSetPath(expanededInTable);
			    } else {
				try {
				    Desktop.getDesktop().open(expanededInTable);
				} catch (Exception e1) {

				    Controller.showPane(e1.toString());
				}
			    }
			}
		    }
		}
	    }
	});
	Action openUpDir = new AbstractAction() {
	    /**
	     * 
	     */
	    private static final long serialVersionUID = 1L;

	    public void actionPerformed(ActionEvent e) {
		controller.openUpDir();

	    }
	};

	new PushableTableHeader(table, "\u25B2", openUpDir,
		"Открыть родительскую папку");
	TableColumn column = null;
	for (int i = 0; i < 6; i++) {
	    column = table.getColumnModel().getColumn(i);
	    if (i == 0) {
		column.setPreferredWidth(30);
	    } else if ((i == 2) || (i == 4)) {
		column.setPreferredWidth(160);
	    } else if (i == 5) {
		column.setPreferredWidth(60);
	    } else {
		column.setPreferredWidth(200);
	    }
	}

	// table.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
	contents = new Box(BoxLayout.Y_AXIS);
	contents.add(new JScrollPane(table));

    }

    void setController(Controller controller) {
	this.controller = controller;
    }

    public void addNodeSetPath(File expanededInTable) {
	treeObject.AddNodeSetPath(expanededInTable);
    }

    public Box getBox() {
	return contents;
    }

    boolean setEditAndFocus(File file) {

	int colNumber = 0;
	for (int i = 0; i < 6; i++) {
	    if ((table.getColumnName(i).equals("Выбрать"))) {

		colNumber = i;

	    }
	}
	for (int i = 0; i < table.getRowCount(); i++) {

	    if ((Boolean) table.getValueAt(i, colNumber) == true) {
		rowSelected = i;
		break;
	    }
	}

	for (int i = 0; i < 6; i++) {
	    if ((table.getColumnName(i).equals("Имя"))) {

		colNumber = i;

	    }
	}
	table.editCellAt(rowSelected, colNumber);
	JTextField tf = (JTextField) table.getEditorComponent();
	String str = tf.getText();
	if (str.lastIndexOf('.') == -1) {
	    tf.selectAll();
	} else {
	    tf.select(0, str.lastIndexOf('.'));
	}
	tf.requestFocusInWindow();
	return true;

    }

    public File getOpen() {
	return open;
    }

    public void update() {
	tableModel.fireTableDataChanged();

    }

    @SuppressWarnings("rawtypes")
    public Vector getdataList() {
	return list;
    }

    public static MyTableModel getTableModel() {
	return tableModel;
    }

    public class MyTableModel extends AbstractTableModel {

	private static final long serialVersionUID = 1L;
	private String[] columnNames = { "\u25B2", "Имя", "Тип", "Размер",
		"Дата изменения", "Выбрать", };

	private FileSystemView fileSystemView = FileSystemView
		.getFileSystemView();
	TreePath treePaths;

	MyTableModel() {
	    list = new Vector<Vector<Object>>();
	    showRoot();
	}

	String getColumnNames(int number) {
	    return columnNames[number];
	}

	public final void showRoot() {

	    File[] root = new File[File.listRoots().length];
	    root = File.listRoots();
	    data = new Object[root.length][columnNames.length + 1];
	    Icon icn;
	    for (int i = 0; i < root.length; i++) {
		FileSystemView fsv = FileSystemView.getFileSystemView();
		if (fsv.getSystemIcon(root[i]) == null)
		    icn = new ImageIcon("images\\file.png");
		else
		    icn = fsv.getSystemIcon(root[i]);
		data[i][0] = icn;
		data[i][1] = ((String) fileSystemView
			.getSystemDisplayName(root[i])).equals("") ? root[i]
			.toString() : fileSystemView
			.getSystemDisplayName(root[i]);
		data[i][2] = fileSystemView.getSystemDisplayName(root[i]);

		data[i][3] = fileSize.freeSpace(root[i].getFreeSpace());
		data[i][4] = new dateChanged(root[i]);
		Vector<Object> v = new Vector<Object>();
		v.add(root[i]);
		v.add("X");
		data[i][5] = new Boolean(list.contains(v)) ;

		data[i][6] = root[i];

	    }
	}

	public int getColumnCount() {
	    return columnNames.length;
	}

	public int getRow() {
	    return columnNames.length;
	}

	public void setData(boolean value, int row, int col) {
	    data[row][col] = value;
	    TableList.updatetableList();

	}

	public int getRowCount() {
	    return data.length;
	}

	public void delListSelected(int row) {
	    list.remove((File) data[row][6]);
	}

	public String getColumnName(int col) {
	    return columnNames[col];
	}

	public Object getValueAt(int row, int col) {
	    return data[row][col];
	}

	public TreePath getSelectedtreePaths() {
	    return SelectedtreePaths;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Class getColumnClass(int c) {

	    return getValueAt(0, c).equals(null) ? getValueAt(0, c - 1)
		    .getClass() : getValueAt(0, c).getClass();
	}

	public boolean isCellEditable(int row, int col) {
	    if (rowSelected >= 0)
		row = table.convertRowIndexToView(row);
	    if (row == rowSelected & col == 1) {

		return true;
	    }
	    if (col < 5) {
		return false;
	    } else {
		return true;
	    }
	}

	public void setValueAt(Object value, int row, int col) {
	    if (rowSelected >= 0) {
		row = table.convertRowIndexToView(row);
		if (row == rowSelected & col == 1) {
		    @SuppressWarnings("rawtypes")
		    String oldName = ((File) ((Vector) list.get(0)).get(0))
			    .getName();
		    if (!((String) value).equals(oldName)) {
			@SuppressWarnings("rawtypes")
			String fullAdr = ((File) ((Vector) list.get(0)).get(0))
				.getPath();
			String c = fullAdr.substring(0,
				fullAdr.lastIndexOf(oldName));
			File fileNewIntroduced = new File(c + value);

			@SuppressWarnings("rawtypes")
			File fileOld = (File) ((Vector) list.get(0)).get(0);
			fileOld.renameTo(fileNewIntroduced);

			data[row][1] = value;
			data[row][2] = TreeNodeClass
				.getFileExtension(fileNewIntroduced);
			data[row][4] = new dateChanged(fileNewIntroduced);

			data[row][6] = fileNewIntroduced;

			treeObject.renameFile(fileOld, fileNewIntroduced);

			Vector<Object> v = new Vector<Object>();

			v.add((File) data[row][6]);
			v.add("X");
		    }
		    list.removeElementAt(0);

		    TreeNodeClass selected = treeObject
			    .getSelectedTreeNodeClass();
		    TableFilesClass.getTableModel().ShowContent(selected.file);
		    TableList.updatelist();

		    tableModel.fireTableDataChanged();

		    rowSelected = -1;

		}
		return;
	    }

	    boolean b = (Boolean) value;
	    if (b) {

		Vector<Object> v = new Vector<Object>();

		v.add((File) data[row][6]);
		v.add("X");

		list.add(v);

	    } else {

		Vector<Object> v = new Vector<Object>();
		v.add((File) data[row][6]);
		v.add("X");
		list.remove(v);
	    }
	    if (list.size() == 1) {
		SelectedtreePaths = treePaths;
	    } else
		SelectedtreePaths = null;
	    TableList.updatelist();
	    data[row][col] = value;
	    tableModel.fireTableDataChanged();
	    pressed = false;
	}

	public void ShowContent(File file) {
	    Icon icn;
	    if (file.exists()) {

		if (TreeNodeClass.isDir(file) && TreeNodeClass.isEmpty(file)) {
		    data = new String[1][columnNames.length + 1];

		    data[0][0] = "";
		    data[0][1] = "Эта папка пуста";
		    data[0][2] = "";
		    data[0][3] = "";
		    data[0][4] = "";
		    data[0][5] = "";
		    data[0][6] = file.getName();

		} else if (TreeNodeClass.isDir(file)
			&& !TreeNodeClass.isEmpty(file)) {

		    data = new Object[file.listFiles().length][columnNames.length + 1];
		    for (int i = 0; i < file.listFiles().length; i++) {

			FileSystemView fsv = FileSystemView.getFileSystemView();
			if (fsv.getSystemIcon(file.listFiles()[i]) == null)
			    icn = new ImageIcon("images\\file.png");
			else
			    icn = fsv.getSystemIcon(file.listFiles()[i]);

			data[i][0] = icn;
			data[i][1] = file.listFiles()[i].getName();

			data[i][2] = TreeNodeClass.getFileExtension(file
				.listFiles()[i]);

			data[i][3] = new fileSize(file.listFiles()[i]);

			data[i][4] = new dateChanged(file.listFiles()[i]);
			Vector<Object> v = new Vector<Object>();
			v.add(file.listFiles()[i]);
			v.add("X");

			data[i][5] = new Boolean(list.contains(v));

			data[i][6] = file.listFiles()[i];
			treeObject.insertHashMap(file.listFiles()[i]);

		    }
		} else {
		    data = new Object[1][columnNames.length + 1];

		    FileSystemView fsv = FileSystemView.getFileSystemView();
		    if (fsv.getSystemIcon(file) == null)
			icn = new ImageIcon("images\\file.png");
		    else
			icn = fsv.getSystemIcon(file);

		    data[0][0] = icn;
		    data[0][1] = file.getName();
		    data[0][2] = TreeNodeClass.getFileExtension(file);
		    data[0][3] = new fileSize(file);
		    data[0][4] = new dateChanged(file);
		    data[0][5] = new Boolean(false);
		    data[0][6] = file;
		    treeObject.insertHashMap(file);
		}
		// tableModel.fireTableDataChanged();

	    } else {
		data = new String[1][7];

		data[0][0] = "";
		data[0][1] = "Файл не существует";
		data[0][2] = "";
		data[0][3] = "";
		data[0][4] = "";
		data[0][5] = "";
		data[0][6] = "";
	    }
	   
	   
	    LinkedList<Integer> select = new LinkedList<Integer>();
	    for ( int i= 0 ;i<data.length;i++){
		if ((Boolean)data[i][5].equals(true)){
		    
		
		   select.add(i);
		}    
	}
	    
	tableModel.fireTableDataChanged();
	 ListSelectionModel selModel = table.getSelectionModel();
	 while (!select.isEmpty()){
	     selModel.addSelectionInterval(select.peek(),select.pop());
	 }
	table.setSelectionModel(selModel);
	}

	public void ShowContent(File file, TreePath treePaths) {
	    this.treePaths = treePaths;
	    ShowContent(file);
	}

    }
}
