package ru.home.sushchen.filemanager;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class StackFileToDelCopy {
	private int maxSize; // size of stack array
	private File[] stackArray;
	private int top; // top of stack



	public StackFileToDelCopy(int s) // constructor
	{
		maxSize = s;
		stackArray = new File[maxSize];
		top = -1;
	}


	public void push(File p) // put item on top of stack
	{
		stackArray[++top] = p;
	}


	public File pop() // take item from top of stack
	{
		return stackArray[top--];
	}


	public File peek() // peek at top of stack
	{
		return stackArray[top];
	}


	public boolean isEmpty() // true if stack is empty
	{
		return (top == -1);
	}

}

class Stack {
	static int theNumber;
	static int countfileToDel;
	static StackFileToDelCopy theStackToDel;
	static int countfileToCopy;
	static StackFileToDelCopy theStackToCopy;


	public static boolean deleteFiles(File file) {
		theStackToDel = new StackFileToDelCopy(1000000); // make a stack

		boolean deleteBoolean = true;
		theStackToDel.push(file);
		while (!theStackToDel.isEmpty()) {
			File temp = theStackToDel.peek();
			if (!temp.isDirectory() || TreeNodeClass.isEmpty(temp)) {
				System.out.println(temp);
				if (temp.delete()) {
					 theStackToDel.pop();

				} else {
					deleteBoolean = false;

				}

			} else {
				for (int i = 0; i < temp.listFiles().length; i++) {
					System.out.println(temp + " i " + i + "  "
							+ temp.listFiles()[i]);
					theStackToDel.push(temp.listFiles()[i]);

				}
			}
		}

		return deleteBoolean;

	}


	public static boolean copyFiles(File source, File dirToInsert) {
		theStackToCopy = new StackFileToDelCopy(100000);
		int choiceActionDirAuto = -1;
		int choiceActionFileAuto = -1;
		// String paperSource = "";
		// String paperToInsert = "";
		boolean cancelCopy = false;
		boolean cancelDirCopy = false;
		boolean copySuccessfully = true;
		Map<File, File> hashMapPathToCopy;
		hashMapPathToCopy = new HashMap<File, File>();
		theStackToCopy.push(source);
		hashMapPathToCopy.put(source, dirToInsert);
		while (!theStackToCopy.isEmpty()) {
			File newFile;
			File tempFile = theStackToCopy.pop();
			dirToInsert = hashMapPathToCopy.get(tempFile);
			hashMapPathToCopy.remove(tempFile);
			String tempFileName = tempFile.getName();

			newFile = new File(dirToInsert.getPath() + "\\" + tempFileName);

			System.out.println("tempFile   " + tempFile);
			System.out.println("newFile " + newFile);

			if (newFile.exists()) {
				Object[] choice = new Object[2];
				if (newFile.isDirectory()) {
					if (choiceActionDirAuto < 0) {
						Object[] selectionValues = {
								"Произвести слияние папок",
								"Заменить папку и все файлы в этой папке",
								"Сохранить обе папки", "Отмена" };
						choice = Controller.showPaneDFialog(
								"Папка с таким именем уже существует: "
										+ newFile.getName(), selectionValues);
						if ((boolean) choice[1]) {
							System.out.println(" choice " + choice[0]);
							choiceActionDirAuto = (Integer) choice[0];
						}
					}
					int choiseActionDir = (choiceActionDirAuto < 0) ? (Integer) choice[0]
							: choiceActionDirAuto;
					switch (choiseActionDir) {
					case 0:

						break;
					case 1:
						deleteFiles(newFile);
						System.out.println(newFile.mkdir());
						break;
					case 2:
						// Save both folder
						for (int i = 1; i < 1000; i++) {

							newFile = new File(dirToInsert.getPath() + "\\"
									+ tempFileName + "(" + i + ")");

							if (!newFile.exists())
								break;

						}
						newFile.mkdir();

						break;
					case 3:

						cancelDirCopy = true;
						break;
					}

				} else {
					if (choiceActionFileAuto < 0) {
						Object[] selectionValues =  { "Заменить файл",
								"Сохранить оба файла", "Отмена" };
						choice = Controller.showPaneDFialog(
								"Файл с таким именем уже существует:"
										+ newFile.getName(), selectionValues);
						if ((boolean) choice[1]) {
							System.out.println(" choice " + choice[0]);
							choiceActionFileAuto = (Integer) choice[0];
						}
					}

					int choiseActionFile = (choiceActionFileAuto < 0) ? (Integer) choice[0]
							: choiceActionFileAuto;
					switch (choiseActionFile) {
					case 0:
						deleteFiles(newFile);

						break;
					case 1:
						// Save both files

						for (int i = 1; i < 1000; i++) {
							System.out.println(source.getName());

							newFile = new File(dirToInsert.getPath()
									+ "\\"
									+ tempFileName.substring(0,
											tempFileName.lastIndexOf('.'))
									+ "("
									+ i
									+ ")"
									+ tempFileName.substring(tempFileName
											.lastIndexOf('.')));

							if (!newFile.exists())
								break;
						}

						break;
					case 2:
						cancelCopy = true;
						break;

					}
					if (!cancelCopy) {
						if (!copyFile(tempFile, newFile))
							copySuccessfully = false;

						cancelCopy = false;
					}

				}

			} else {

				if (tempFile.isDirectory()) {
					newFile.mkdir();

				} else {

					if (!copyFile(tempFile, newFile))
						copySuccessfully = false;

					cancelCopy = false;

				}
			}
			if (!cancelDirCopy) {
				if (tempFile.isDirectory()) {
					if (tempFile.listFiles().length > 0) {

						for (File file : tempFile.listFiles()) {

							theStackToCopy.push(file);
							hashMapPathToCopy.put(file, newFile);
						}
					}
					cancelDirCopy = false;
				}

			}
		}
		System.out.println("copySuccessfully " + copySuccessfully);
		return copySuccessfully;

	}

	public static boolean copyFile(File source, File dest) {

		FileInputStream ins = null;
		FileOutputStream outs = null;
		try {

			ins = new FileInputStream(source);
			outs = new FileOutputStream(dest);
			byte[] buffer = new byte[1024];
			int length;

			while ((length = ins.read(buffer)) > 0) {
				outs.write(buffer, 0, length);
			}
			ins.close();
			outs.flush();
			outs.close();
			System.out.println("File copied successfully!!");
			return true;
		} catch (IOException ioe) {
			ioe.printStackTrace();
			System.out.println(ioe);
			return false;
		}
	}

}