package ru.home.sushchen.filemanager;

import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.ArrayList;

public class MainWindow implements TreeSelectionListener {
    /**MainWindow creat all visible elements*/
    private TableFilesClass tableFiles;
    private static JPanel errorPanel;
    private Controller controller;
    private Cursor waitCursor = new Cursor(Cursor.WAIT_CURSOR);
    private Cursor defaultCursor = new Cursor(Cursor.DEFAULT_CURSOR);
    private JFrame frame;
    private TreeClass treeObject;
    private TableList tableSelection;
    private ArrayList<JButton> buttons = new ArrayList<>();

    private void addComponentsToPane(Container pane) {

        pane.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

        c.anchor = GridBagConstraints.NORTH;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridheight = 1;
        c.gridwidth = 1;
        c.gridx = GridBagConstraints.RELATIVE;
        c.gridy = GridBagConstraints.RELATIVE;
        c.insets = new Insets(15, 5, 15, 5);
        c.ipadx = 15;
        c.ipady = 15;
        c.weightx = 1;
        c.weighty = 1;

        String[] menuButtons = {"Новая папка", "Копировать", "Вырезать",
                "Вставить", "Удалить", "Переименовать", "О программе"};
        JButton button;

        ActionListener listener = new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                try {
                    doCommand(ae);
                } catch (Throwable t) {
                    showThrowable(t);
                }
            }
        };

        for (String b : menuButtons) {
            button = new JButton(b);
            button.addActionListener(listener);
            if (button.getActionCommand().equals("О программе")) {
                button.setEnabled(true);

            } else
                button.setEnabled(false);

            pane.add(button, c);
            buttons.add(button);

        }

        c.anchor = GridBagConstraints.CENTER;
        c.fill = GridBagConstraints.BOTH;
        c.gridheight = 1;
        c.gridwidth = 2;
        c.weightx = 1;
        c.weighty = 6;
        c.gridx = 0;
        c.gridy = 1;
        c.insets = new Insets(15, 5, 15, 5);
        c.ipadx = 15;
        c.ipady = 15;
        treeObject = new TreeClass();

        pane.add(treeObject.getTree(), c);

        treeObject.getTree().setMinimumSize(
                treeObject.getTree().getMaximumSize());
        JScrollPane treeView = new JScrollPane(treeObject.getTree());
        treeObject.getTree().addTreeSelectionListener(this);
        pane.add(treeView, c);

        c.anchor = GridBagConstraints.CENTER;
        c.fill = GridBagConstraints.BOTH;
        c.gridheight = 2;
        c.gridwidth = 5;
        c.weightx = 1;
        c.weighty = 9;
        c.gridx = 2;
        c.gridy = 1;
        c.insets = new Insets(15, 5, 15, 5);
        c.ipadx = 15;
        c.ipady = 15;

        tableFiles = new TableFilesClass(treeObject);


        pane.add(tableFiles.getBox(), c);

        c.anchor = GridBagConstraints.CENTER;
        c.fill = GridBagConstraints.BOTH;
        c.gridheight = 1;
        c.gridwidth = 2;
        c.weightx = 1;
        c.weighty = 3;
        c.gridx = 0;
        c.gridy = 2;
        c.insets = new Insets(15, 5, 15, 5);
        c.ipadx = 15;
        c.ipady = 15;

        tableSelection = new TableList(tableFiles, treeObject);

        Action delete = new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                doDelListRow(e);

            }
        };

        ButtonColumn buttonColumn = new ButtonColumn(
                tableSelection.getTableList(), delete, 1);
        buttonColumn.setMnemonic(KeyEvent.VK_D);

        pane.add(tableSelection.getTableBox(), c);


    }

    void setObjects(MainWindow mainWin, Controller controller) {
        this.controller = controller;
        controller.setObjects(mainWin, treeObject, tableFiles,
                buttons);
        treeObject.setController(controller);
        tableFiles.setController(controller);
        tableSelection.setController(controller);
    }

    void createAndShowGUI() {

        frame = new JFrame("File Manager");
        Image icon = new ImageIcon(getClass().getResource("/images/comp.png")).getImage();
        frame.setIconImage(icon);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        addComponentsToPane(frame.getContentPane());
        frame.pack();
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setVisible(true);

    }

    void setDefaultCursor() {
        frame.setCursor(defaultCursor);
    }

    void setWaitCursor() {
        frame.setCursor(waitCursor);
    }

    private void showThrowable(Throwable t) {
        t.printStackTrace();
        JOptionPane.showMessageDialog(errorPanel, t.toString(), t.getMessage(),
                JOptionPane.ERROR_MESSAGE);
    }

    static void showPane(String message) {

        errorPanel = new JPanel();
        errorPanel.setOpaque(true);
        JOptionPane.showMessageDialog(errorPanel, message, "", JOptionPane.INFORMATION_MESSAGE);
        errorPanel.repaint();
    }

    private void doCommand(ActionEvent ae) {
        switch (ae.getActionCommand()) {
            case "Новая папка":
                controller.newDir();
                break;
            case "Копировать":
                controller.copy();
                break;
            case "Вырезать":
                controller.cut();
                break;
            case "Вставить":
                try {
                    controller.insert();
                } catch (IOException e) {
                    showPane(" Ошибка " + e.toString());
                }
                break;
            case "Удалить":
                controller.delete();
                break;
            case "Переименовать":
                controller.rename();
                break;
            case "О программе":
                controller.about();
        }
    }

    private void doDelListRow(ActionEvent ae) {
        int modelRow = Integer.valueOf(ae.getActionCommand());
        controller.delListRow(modelRow);
    }

    @Override
    public void valueChanged(TreeSelectionEvent e) {
        DefaultMutableTreeNode node = (DefaultMutableTreeNode) e.getPath()
                .getLastPathComponent();
        controller.offButton(treeObject.isSelectedRoot(node));
    }
}