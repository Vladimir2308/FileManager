//package ru.t1.Sushchen.Main;
//import java.awt.event.ActionEvent;
//import java.awt.event.ActionListener;
//import java.io.File;
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.LinkedList;
//import java.util.List;
//import java.util.Vector;
//
//import javax.swing.*;
// 
//public class ListSelected extends JFrame {
// 
//    static JList jList;
//    Vector list;
//    public Box jListBox;
//
// 
//    public ListSelected() {
//    	
//        list = TableSortDemo.getdataList();
//        
//      
// 
//            myListModel listModel=    new myListModel();
//        jList = new JList(listModel);
////        listModel.addElement(new JButton("Очистить"));
//        jList.setLayoutOrientation(JList.VERTICAL);
//		jListBox = new Box(BoxLayout.Y_AXIS);
//		jListBox.add(new JScrollPane(jList));
//		System.out.println("JList: " + Arrays.toString(list.toArray()));
////            button.addActionListener(new ActionListener() {
////            @Override
////            public void actionPerformed(ActionEvent e) {
////                data.add("Java duke");
////                updateJList();
////            }
////        });
//
//    }
//	public Box getJListBox() {
//		return jListBox ;
//	}
//    public static void updateJList() {
//        ((myListModel) jList.getModel()).update();
//    }
//    
//    public class myListModel extends AbstractListModel<Vector> {
// 
//        public void update() {
//            fireContentsChanged(this, 0, 0);
//        }
// 
//        @Override
//        public int getSize() {
//            return list.size();
//        }
// 
//        @Override
//        public Vector getElementAt(int index) {
//            return (Vector)list.get(index);
//        }
//    }
//}