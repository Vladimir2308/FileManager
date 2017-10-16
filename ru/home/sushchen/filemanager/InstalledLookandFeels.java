package ru.home.sushchen.filemanager;
import javax.swing.UIManager;

public class InstalledLookandFeels {

	public static void main(String args[]) {
		UIManager.LookAndFeelInfo laf[] = UIManager
				.getInstalledLookAndFeels();
		for (int i = 0, n = laf.length; i < n; i++) {
			System.out.print("LAF Name: " + laf[i].getName() + "\t");
			System.out.println("  LAF Class name: "
					+ laf[i].getClassName());
		}
		System.exit(0);
	}
}