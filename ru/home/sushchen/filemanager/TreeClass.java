package ru.home.sushchen.filemanager;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JTree;
import javax.swing.UIManager;
import javax.swing.border.LineBorder;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeExpansionListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeCellRenderer;
import javax.swing.tree.TreePath;

public class TreeClass extends JFrame implements TreeSelectionListener,
	TreeExpansionListener {
    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;
    protected JTree tree;
    Controller controller;
    private DefaultTreeModel model;
    private DefaultMutableTreeNode root;
    public DefaultMutableTreeNode selectedNode;
    TreePath treePaths;
    Map<File, DefaultMutableTreeNode> hashMap;

    public TreeClass() {
	hashMap = new HashMap<File, DefaultMutableTreeNode>();

	root = new DefaultMutableTreeNode("Мой компьютер");
	hashMap.put(new File("Мой компьютер"), root);
	TreeNodeClass r = new TreeNodeClass(TreeNodeClass.getDesktop());

	DefaultMutableTreeNode desktop = new DefaultMutableTreeNode(r, true);

	root.add(desktop);

	hashMap.put(TreeNodeClass.getDesktop(), desktop);
	for (File f : TreeNodeClass.getRoots()) {
	    r = new TreeNodeClass(f);
	    r.setName(f.toString());
	    DefaultMutableTreeNode tempnode = new DefaultMutableTreeNode(r,
		    true);
	    root.add(tempnode);
	    hashMap.put(f, tempnode);
	}

	model = new DefaultTreeModel(root, true);
	tree = new JTree(model);
	tree.setOpaque(true);
	TreePath path = tree.getPathForRow(0);
	tree.setSelectionPath(path);
	selectedNode = root;

	tree.setShowsRootHandles(true);
	tree.addTreeSelectionListener(this);
	tree.addTreeExpansionListener(this);

	TreeCellRenderer renderer2 = new IconCellRenderer();
	tree.setCellRenderer(renderer2);

    }

    void setController(Controller controller) {
	this.controller = controller;
    }

    public void valueChanged(TreeSelectionEvent e) {
	controller.setWaitCursor();

	selectedNode = (DefaultMutableTreeNode) e.getPath()
		.getLastPathComponent();

	treePaths = tree.getSelectionPath();

	if (selectedNode == null)
	    return;
	if (selectedNode != root) {
	    TreeNodeClass selected = (TreeNodeClass) selectedNode
		    .getUserObject();

	    TableFilesClass.getTableModel().ShowContent(selected.file,
		    treePaths);

	} else {
	    TableFilesClass.getTableModel().showRoot();
	   

	}
	controller.setDefaultCursor();
    }

    public void setSelectionPath(TreePath path) {
	tree.setSelectionPath(path);
    }

    public void setSelectionParentNode() {
	DefaultMutableTreeNode selDefMutTreeNode = getSelectedTreeNode();
	if (selDefMutTreeNode != root) {

	    DefaultMutableTreeNode parentDefMutTreeNode = (DefaultMutableTreeNode) selDefMutTreeNode
		    .getParent();

	    setSelectionPath(new TreePath(parentDefMutTreeNode.getPath()));
	}
    }

    public void AddNodeSetPath(File file) {
	/*
	 * Этот метод необхадим для открытия папки в JTree, корорая была открыта
	 * двойным щелчком в tableFiles
	 */
	controller.setWaitCursor();
	TreePath pathOld = tree.getSelectionPath();
	System.out.println(tree.getSelectionPath());
	DefaultMutableTreeNode node;
	if (!hashMap.containsKey(file)) {
	    TreeNodeClass r = new TreeNodeClass(file);

	    node = TreeNodeClass.isDir(file) ? new DefaultMutableTreeNode(

	    r, true) : new DefaultMutableTreeNode(r, false);
	    DefaultMutableTreeNode currentSelected = getSelectedTreeNode();

	    System.out.println("Path : " + node.getPath());
	    model.insertNodeInto(node, getSelectedTreeNode(), 0);
	    hashMap.put(file, node);
	} else
	    node = hashMap.get(file);
	TreePath pathNew = new TreePath(node.getPath());

	tree.expandPath(pathNew);
	tree.setSelectionPath(pathNew);
	controller.setDefaultCursor();
    }

    public Map getHashMap() {
	return hashMap;
    }

    public void newDir() {
	DefaultMutableTreeNode SelectedTreeNode = getSelectedTreeNode();
	// Получаем выделеннуюнную запись в JTree
	TreeNodeClass selected = (TreeNodeClass) SelectedTreeNode
		.getUserObject();
	// Получаем файл TreeNodeClass сопаставленный данной записи в Jtree
	if (!selected.file.isDirectory()) {
	    SelectedTreeNode = (DefaultMutableTreeNode) getSelectedTreeNode()
		    .getParent();
	    selected = (TreeNodeClass) SelectedTreeNode.getUserObject();
	}
	File newDir = new File(selected.file.getPath() + "\\Новая папка");
	if (newDir.exists()) {
	    for (int i = 1; i < 1000; i++) {
		newDir = new File(selected.file.getPath() + "\\Новая папка("
			+ i + ")");
		if (!newDir.exists())
		    break;
	    }
	}
	newDir.mkdir();
	TreeNodeClass temp = new TreeNodeClass(newDir);
	DefaultMutableTreeNode insertedFolder = new DefaultMutableTreeNode(
		temp, true);
	getTreeModel().insertNodeInto(insertedFolder, SelectedTreeNode, 0);
	hashMap.put(newDir, insertedFolder);
	TableFilesClass.getTableModel().ShowContent(selected.file);

    }

    public void removeNodeFromTree(File file) {
	DefaultMutableTreeNode nodeDel = hashMap.get(file);

	System.out.println(Arrays.toString(nodeDel.getPath()));
	DefaultMutableTreeNode parentNode = (DefaultMutableTreeNode) nodeDel
		.getParent();
	nodeDel.removeFromParent();
	System.out.println("Удаляемая :" + nodeDel + " parentNode: "
		+ parentNode);

	hashMap.remove(file);
	getTreeModel().nodeStructureChanged(parentNode);
	update();

	// System.out.println(" path " +
	// treeObject.getTreeModel().getPathToRoot((hashMap.get(file))));

    }

    public void renameFile(File oldName, File newName) {

	// model.removeNodeFromParent(arg0);
	DefaultMutableTreeNode renameNode = hashMap.get(oldName);
	TreeNodeClass treeNodeNewName = new TreeNodeClass(newName);
	renameNode.setUserObject(treeNodeNewName);
	hashMap.remove(oldName);

	hashMap.put(newName, renameNode);
	update();
	// model.insertNodeInto(node , getSelectedTreeNode(), 0);
    }

    public void insertHashMap(File file) {

	//

	if (!hashMap.containsKey(file)) {
	    TreeNodeClass r = new TreeNodeClass(file);

	    DefaultMutableTreeNode node = TreeNodeClass.isDir(file) ? new DefaultMutableTreeNode(

	    r, true)
		    : new DefaultMutableTreeNode(r, false);

	    model.insertNodeInto(node, getSelectedTreeNode(), 0);
	    hashMap.put(file, node);
	    // System.out.println("Selected In hasmap, file: [Мой компьютер, "+
	    // file );
	    // System.out.println("Selected In hasmap, node: "+
	    // Arrays.toString(node.getPath()));
	}

    }

    public void update() {
	TreePath path = tree.getSelectionPath();
	model.reload();
	tree.expandPath(path);
	tree.setSelectionPath(path);

    }

    public void treeCollapsed(TreeExpansionEvent arg0) {

    }

    public void treeExpanded(TreeExpansionEvent arg0) {
	controller.setWaitCursor();
	DefaultMutableTreeNode expandedFolder = (DefaultMutableTreeNode) arg0
		.getPath().getLastPathComponent();
	// Определяем запись в JTree, которая была раскрыта

	if (expandedFolder != root) {
	    TreeNodeClass expanded = (TreeNodeClass) expandedFolder
		    .getUserObject();
	    // Определяем файл которому соответствовала запись в JTree

	    if (TreeNodeClass.isDir(expanded.file)
		    & !TreeNodeClass.isEmpty(expanded.file)) {
		int countFiles = expanded.file.listFiles().length;

		if (countFiles > 0) {
		    for (int i = 0; i < countFiles; i++) {
			TreeNodeClass temp = new TreeNodeClass(
				expanded.file.listFiles()[i]);
			// создаём объекты для раскрытых файлов дерева
			DefaultMutableTreeNode expandedFiles = TreeNodeClass
				.isDir(expanded.file.listFiles()[i]) ? new DefaultMutableTreeNode(

			temp, true)
				: new DefaultMutableTreeNode(temp);
			// создаём записи для созданных объектов
			if (!hashMap.containsKey(expanded.file.listFiles()[i])) {
			    // Проверяем были ли добавлен
			    model.insertNodeInto(expandedFiles, expandedFolder,
				    0);
			    // добавляем созданные записи в дерево
			    hashMap.put(expanded.file.listFiles()[i],
				    expandedFiles);

			}

		    }
		}
	    }
	}

	controller.setDefaultCursor();
    }

    public DefaultMutableTreeNode getSelectedTreeNode() {
	return selectedNode;
    }

    public File getSelectedForInsert() {
	TreeNodeClass selected = (TreeNodeClass) selectedNode.getUserObject();
	if (!selected.file.isDirectory()) {
	    DefaultMutableTreeNode SelectedNodeForInsert = (DefaultMutableTreeNode) selectedNode
		    .getParent();
	    selected = (TreeNodeClass) SelectedNodeForInsert.getUserObject();
	}
	return selected.file;
    }

    public TreeNodeClass getSelectedTreeNodeClass() {
	TreeNodeClass selectedTN;
	if (selectedNode != root) {
	    selectedTN = (TreeNodeClass) selectedNode.getUserObject();
	} else
	    selectedTN = new TreeNodeClass("root");
	return selectedTN;
    }

    public JTree getTree() {
	return tree;
    }

    public File getSelectedFile() {
	if (selectedNode != root)
	    return ((TreeNodeClass) selectedNode.getUserObject()).file;
	else
	    return null;
    }

    public DefaultTreeModel getTreeModel() {
	return model;
    }

    public boolean isSelectedRoot(DefaultMutableTreeNode Node) {
	return (Node == root);
    }

}

class IconCellRenderer extends DefaultTreeCellRenderer

{
    protected Color textSelectionColor;
    protected Color textNonSelectionColor;
    protected Color bkSelectionColor;
    protected Color bkNonSelectionColor;
    protected Color borderSelectionColor;

    protected boolean selected;

    public IconCellRenderer() {
	super();
	textSelectionColor = UIManager.getColor("Tree.selectionForeground");
	textNonSelectionColor = UIManager.getColor("Tree.textForeground");
	bkSelectionColor = UIManager.getColor("Tree.selectionBackground");
	bkNonSelectionColor = UIManager.getColor("Tree.textBackground");
	borderSelectionColor = UIManager.getColor("Tree.selectionBorderColor");
	setOpaque(false);
	

    }

    public Component getTreeCellRendererComponent(JTree tree, Object value,
	    boolean sel, boolean expanded, boolean leaf, int row,
	    boolean hasFocus)

    {

	DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;

	Icon icon;

	if (!node.isRoot()) {

	    TreeNodeClass obj = (TreeNodeClass) node.getUserObject();

	    setText(obj.toString());

	    TreeNodeClass idata = (TreeNodeClass) obj;

	    TreePath path = new TreePath(node.getPath());
	    if (!TreeNodeClass.isDir(obj.file) || (path.getPathCount() == 2))

		setIcon(idata.getIcon());
	    else {

		if (tree.isExpanded(new TreePath(node.getPath())))
		    icon = new ImageIcon("images\\open.png");
		else
		    icon = new ImageIcon("images\\close.png");
		setIcon(icon);

	    }

	} else {
	    icon = new ImageIcon("images\\comp.png");
	    setIcon(icon);
	    setText("Мой компьютер");
	}
	 if (hasFocus) 
         {
//           setBorder(new LineBorder(new Color(99, 130, 191)));
	     setBorder(new LineBorder(null, 0));
         }
         else
         {
           setBorder(new LineBorder(null, 0));
         }

	setFont(tree.getFont());
	setForeground(sel ? textSelectionColor : textNonSelectionColor);
	setBackground(sel ? bkSelectionColor : bkNonSelectionColor);
	setFocusable ( false );
	selected = sel;
	return this;

    }

    public void paintComponent(Graphics g) {
	Color bColor = getBackground();
	Icon icon = getIcon();
	setFocusable ( false );
	g.setColor(bColor);
	int offset = 0;
	if (icon != null && getText() != null)
	    offset = (icon.getIconWidth() + getIconTextGap());
	g.fillRect(offset, 0, getWidth() - 1 - offset, getHeight() - 1);

	if (selected) {
	    g.setColor(borderSelectionColor);
	    
	    g.drawRect(offset, 0, getWidth() - 1 - offset, getHeight() - 1);
	}
	
	super.paintComponent(g);
    }
}
