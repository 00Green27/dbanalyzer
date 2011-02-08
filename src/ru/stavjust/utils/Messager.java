package ru.stavjust.utils;

import javax.swing.JOptionPane;

/**
 * @author ehd
 */
public class Messager {

	private static String title = "JExport";

	/**
	 * 
	 * @param msg
	 */
	public static void informationMessage(String msg) {
		JOptionPane.showMessageDialog(null, msg, title,
				JOptionPane.INFORMATION_MESSAGE);
	}

	/**
	 * 
	 * @param msg
	 */
	public static void errorMessage(String msg) {
		JOptionPane.showMessageDialog(null, msg, title,
				JOptionPane.ERROR_MESSAGE);
	}

	/**
	 * 
	 * @param msg
	 */
	public static void exceptionMessage(String msg) {
		JOptionPane.showMessageDialog(null,
				"Произошла следующая исключительная ситуация:\n" + msg, title,
				JOptionPane.ERROR_MESSAGE);
	}

	/**
	 * 
	 * @param msg
	 */
	public static void resultMessage(String msg) {
		JOptionPane.showMessageDialog(null, msg, "JExport",
				JOptionPane.PLAIN_MESSAGE);
	}
}
