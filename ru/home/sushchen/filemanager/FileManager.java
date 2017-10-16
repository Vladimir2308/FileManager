package ru.home.sushchen.filemanager;

import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.plaf.ColorUIResource;

public class FileManager {

    public static void main(String[] args) {

        try {
            for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    UIManager.put("nimbusSelectionBackground",
                            new ColorUIResource(10, 36, 106));


                }
            }
        } catch (Exception e) {
            MainWindow
                    .showPane("LaF Nimbus is not available, manager will work with default UI");

        }

        javax.swing.SwingUtilities.invokeLater(new Runnable() {

            public void run() {

                MainWindow mainWin = new MainWindow();
//		MainWindow creat all visible elements
                mainWin.createAndShowGUI();
                Controller controller = new Controller();
//		Controller manage all aplication
                mainWin.setObjects(mainWin, controller);
//      transfer refers for Objects
            }
        });
    }

}
