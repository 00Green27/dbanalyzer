/*
 *Copyright 2011 00Green27 <00Green27@gmail.com>
 *
 *This file is part of mDBExplorer.
 *
 *This code is free software: you can redistribute it and/or modify
 *it under the terms of the GNU General Public License as published by
 *the Free Software Foundation, either version 3 of the License, or
 *(at your option) any later version.
 *
 *This code is distributed in the hope that it will be useful,
 *but WITHOUT ANY WARRANTY; without even the implied warranty of
 *MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *GNU General Public License for more details.
 *
 *You should have received a copy of the GNU General Public License
 *along with this work.  If not, see http://www.gnu.org/licenses/.
 */

package ru.green.utils;

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
