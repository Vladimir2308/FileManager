package ru.home.sushchen.filemanager;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.Vector;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;

public class TableList {

    private static TableFilesClass tableFiles;
    static TreeClass treeObject;
    static JTable tableList;
    Controller controller;
    Box JTableBox;
    private static Vector list;
    private static String delButton = "X";
    static Object[][] data;
    public Vector<String> columnNames;
    private static DefaultTableModel model;

    public TableList(TableFilesClass tableFiles, TreeClass treeObject) {
	this.tableFiles = tableFiles;
	this.treeObject = treeObject;
	list = tableFiles.getdataList();
	columnNames = new Vector<String>();

	columnNames.addElement("Выделенные файлы и папки");
	columnNames.addElement("X");

	model = new DefaultTableModel(list, columnNames);
	tableList = new JTable(model);
	tableList.setFillsViewportHeight(true);
	tableList.setShowGrid(false);
	tableList.setShowHorizontalLines(true);
	tableList.getColumnModel().getColumn(1).setMaxWidth(50);

	Action clearList = new AbstractAction() {
	    public void actionPerformed(ActionEvent e) {
		removeAllFile();

	    }
	};

	new PushableTableHeader(tableList, "X", clearList,
		"Очистить список выбранных файлов и папок");

	JTableBox = new Box(BoxLayout.Y_AXIS);
	JTableBox.add(new JScrollPane(tableList));

    }

    void setController(Controller controller) {
	this.controller = controller;
    }

    public Box getTableBox() {
	return JTableBox;
    }

    public static void updatelist() {

	// list = TableFilesClass.getdataList();
	model.fireTableDataChanged();

    }

    public JTable getTableList() {
	return tableList;
    }

    public DefaultTableModel getTableModelList() {
	return model;
    }

    public static File getSelectedFile() {
	return treeObject.getSelectedFile();
    }

    public static void updatetableList() {

	tableFiles.update();
	model.fireTableStructureChanged();
	model.fireTableDataChanged();

    }

    public static void removeFile(int row) {

	list.remove(row);

	model.fireTableDataChanged();
    }

    public void removeAllFile() {
	// clear list of selected files
	controller.clearList();



    }
}

class ButtonColumn extends AbstractCellEditor implements TableCellRenderer,
	TableCellEditor, ActionListener, MouseListener {
    private static Container pane;
    private JTable table;
    private Action action;
    private int mnemonic;
    private Border originalBorder;
    private Border focusBorder;

    private JButton renderButton;
    private JButton editButton;
    private Object editorValue;
    private boolean isButtonColumnEditor;

    public ButtonColumn(JTable table, Action action, int column) {
	this.table = table;
	this.action = action;

	renderButton = new JButton();
	editButton = new JButton();
	editButton.setFocusPainted(false);
	editButton.addActionListener(this);
	originalBorder = editButton.getBorder();
	setFocusBorder(new LineBorder(Color.BLUE));

	TableColumnModel columnModel = table.getColumnModel();
	columnModel.getColumn(column).setCellRenderer(this);
	columnModel.getColumn(column).setCellEditor(this);
	table.addMouseListener(this);
    }

    public Border getFocusBorder() {
	return focusBorder;
    }

    public void setFocusBorder(Border focusBorder) {
	this.focusBorder = focusBorder;
	editButton.setBorder(focusBorder);
    }

    public int getMnemonic() {
	return mnemonic;
    }

    public void setMnemonic(int mnemonic) {
	this.mnemonic = mnemonic;
	renderButton.setMnemonic(mnemonic);
	editButton.setMnemonic(mnemonic);
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value,
	    boolean isSelected, int row, int column) {
	if (value == null) {
	    editButton.setText("");
	    editButton.setIcon(null);
	} else if (value instanceof Icon) {
	    editButton.setText("");
	    editButton.setIcon((Icon) value);
	} else {
	    editButton.setText(value.toString());
	    editButton.setIcon(null);
	}

	this.editorValue = value;
	return editButton;
    }

    @Override
    public Object getCellEditorValue() {
	return editorValue;
    }

    //
    // Implement TableCellRenderer interface
    //
    public Component getTableCellRendererComponent(JTable table, Object value,
	    boolean isSelected, boolean hasFocus, int row, int column) {
	if (isSelected) {
	    renderButton.setForeground(table.getSelectionForeground());
	    renderButton.setBackground(table.getSelectionBackground());
	} else {
	    renderButton.setForeground(table.getForeground());
	    renderButton.setBackground(UIManager.getColor("Button.background"));
	}

	if (hasFocus) {
	    renderButton.setBorder(focusBorder);
	} else {
	    renderButton.setBorder(originalBorder);
	}

	// renderButton.setText( (value == null) ? "" : value.toString() );
	if (value == null) {
	    renderButton.setText("");
	    renderButton.setIcon(null);
	} else if (value instanceof Icon) {
	    renderButton.setText("");
	    renderButton.setIcon((Icon) value);
	} else {
	    renderButton.setText(value.toString());
	    renderButton.setIcon(null);
	}

	return renderButton;
    }

    //
    // Implement ActionListener interface
    //
    /*
     * The button has been pressed. Stop editing and invoke the custom Action
     */
    public void actionPerformed(ActionEvent e) {
	int row = table.convertRowIndexToModel(table.getEditingRow());
	fireEditingStopped();

	// Invoke the Action

	ActionEvent event = new ActionEvent(table,
		ActionEvent.ACTION_PERFORMED, "" + row);
	action.actionPerformed(event);
    }

    //
    // Implement MouseListener interface
    //
    /*
     * When the mouse is pressed the editor is invoked. If you then then drag
     * the mouse to another cell before releasing it, the editor is still
     * active. Make sure editing is stopped when the mouse is released.
     */
    public void mousePressed(MouseEvent e) {
	if (table.isEditing() && table.getCellEditor() == this)
	    isButtonColumnEditor = true;
    }

    public void mouseReleased(MouseEvent e) {
	if (isButtonColumnEditor && table.isEditing())
	    table.getCellEditor().stopCellEditing();
	System.out.println(" table.getCellEditor() " + table.getCellEditor());
	isButtonColumnEditor = false;
    }

    public void mouseClicked(MouseEvent e) {

    }

    public void mouseEntered(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {
    }
}

class PushableTableHeader extends JPanel {
    Action action;
    JTableHeader header;
    JTable table;
    TableColumn column;
    public String toolTipText;

    public PushableTableHeader(JTable table, String columnName, Action action,
	    String toolTipText) {
	// setLayout(new BorderLayout());
	column = table.getColumn(columnName);

	header = table.getTableHeader();
	this.action = action;
	this.table = table;
	this.toolTipText = toolTipText;
	ButtonHeaderRenderer renderer = new ButtonHeaderRenderer();
	column.setHeaderRenderer(renderer);

	header.addMouseListener(new HeaderListener(header, renderer, action,
		toolTipText, columnName));
    }

    public class HeaderListener extends MouseAdapter implements ActionListener {
	JTableHeader header;
	ButtonHeaderRenderer renderer;
	Action action;
	String columnName;

	HeaderListener(JTableHeader header, ButtonHeaderRenderer renderer,
		Action action, String toolTipText, String columnName) {
	    this.header = header;
	    this.renderer = renderer;
	    this.action = action;
	    this.columnName = columnName;

	}

	@Override
	public void mousePressed(MouseEvent e) {
	    int col = header.columnAtPoint(e.getPoint());

	    renderer.setPressedColumn(col);
	    header.repaint();

	}

	@Override
	public void mouseReleased(MouseEvent e) {
	    int col = header.columnAtPoint(e.getPoint());

	    renderer.setPressedColumn(-1);
	    header.repaint();
	    if (table.getColumnName(col).equals(columnName)) {

		ActionEvent event = new ActionEvent(table,
			ActionEvent.ACTION_PERFORMED, "");
		action.actionPerformed(event);
	    }
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {

	}
    }

    class ButtonHeaderRenderer extends JButton implements TableCellRenderer {
	int pushedColumn;

	public ButtonHeaderRenderer() {
	    pushedColumn = -1;
	    setMargin(new Insets(0, 0, 0, 0));
	}

	@Override
	public Component getTableCellRendererComponent(JTable table,
		Object value, boolean isSelected, boolean hasFocus, int row,
		int column) {
	    setText((value == null) ? "" : value.toString());
	    boolean isPressed = (column == pushedColumn);
	    getModel().setPressed(isPressed);
	    getModel().setArmed(isPressed);
	    // setToolTipText("�������� ������ ��������� ������ � �����");
	    setToolTipText(toolTipText);
	    return this;
	}

	public void setPressedColumn(int col) {
	    pushedColumn = col;
	}
    }
}