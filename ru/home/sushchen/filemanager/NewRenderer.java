package ru.home.sushchen.filemanager;

import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeCellRenderer;



public class NewRenderer extends JLabel implements TreeCellRenderer {
    public Component getTreeCellRendererComponent(JTree tree, Object value, boolean isSelected,
        boolean isExpanded, boolean isLeaf, int row, boolean hasFocus) {
      Object userObject = ((DefaultMutableTreeNode) value).getUserObject();
      setText(value.toString() + " [" + userObject.getClass().getName() + "]");
      isLeaf=false;
      return this;
    }
  }