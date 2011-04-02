/*
 * Copyright 2011 00Green27 <00Green27@gmail.com>
 *
 * This file is part of mDBExplorer.
 *
 * This code is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This code is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this work.  If not, see http://www.gnu.org/licenses/.
 */

package ru.green.utils;

import ru.green.DBAnalyzer;
import ru.green.dbutils.DBUrlReader;
import ru.green.model.Database;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Класс методов-утилит
 *
 * @author ehd
 */
public class Utils {
	private static ArrayList<Database> dbList;
	private static final DateFormat DATE_FORMAT = new SimpleDateFormat(
			"dd.MM.yyyy");
	private static final DateFormat SQL_FORMAT = new SimpleDateFormat(
			"yyyy-MM-dd");
	private static final DateFormat FORMATTER = new SimpleDateFormat(
			"yyyyMMdd-HHmm");

	public static double roundUpScale(double value) {
		return roundUpScale(value, 2);
	}

	public static double roundUpScale(double value, int scale) {
		BigDecimal decimal = new BigDecimal(value);
		return decimal.setScale(scale, BigDecimal.ROUND_HALF_UP).doubleValue();
	}

	/**
	 * Метод переводит строку подключения из формата Firbird/Interbase в jdbc
	 * url, т.е. <code>
	 * DbServer:E:\ex_prod_database\2009\Александровский\EX_PROD.GDB
	 * </code> в <code>
	 * jdbc:firebirdsql://DbServer/E:/ex_prod_database/2009/Александровский/ex_prod.gdb
	 * </code>
	 *
	 * @param url
	 *            Строка подключения в формате Firbird/Interbase
	 * @return возращает строку подключения в формате jdbc url
	 */
	public static String constructURL(String url) {
		return DBAnalyzer.getPreference("connection.protocol") + "//"
				+ url.replace("\\", "/").replaceFirst(":", "/") + "?"
				+ DBAnalyzer.getPreference("connection.parameters");
	}

	public synchronized static Date parseDate(String date) {
		try {
			return DATE_FORMAT.parse(date);
		} catch (ParseException e) {
			return new Date();
		}
	}

	public synchronized static Date parseSQLDate(String date) {
		try {
			return SQL_FORMAT.parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
			return new Date();
		}
	}

	public synchronized static String formatDate(Date date) {
		return DATE_FORMAT.format(date);
	}

	public synchronized static String formatFileNameDate(Date date) {
		return FORMATTER.format(date);

	}

	public static boolean openFile(final File file) {
		if (!Desktop.isDesktopSupported()) {
			return false;
		}

		Desktop desktop = Desktop.getDesktop();
		if (!desktop.isSupported(Desktop.Action.OPEN)) {
			return false;
		}

		try {
			desktop.open(file);
		} catch (IOException e) {
			return false;
		}
		return true;
	}

	public static boolean openFile(String file) {
		return openFile(new File(file));
	}

	public static String[] match(String sql) {
		Pattern p = Pattern.compile("\\$\\w+");
		String[] split = p.split(sql);
		for (String string : split) {
			sql = sql.replace(string, " ");
		}
		return sql.trim().split(" ");
	}

	public static ImageIcon getIcon(String iconName) {
		if (Utils.class.getResource(String.format(
				"/ru/green/ui/resources/%s.png", iconName)) == null)
			iconName = "pixel";
		return new ImageIcon(Utils.class.getResource(String.format(
				"/ru/green/ui/resources/%s.png", iconName)));
	}

	public static Object formatDecimal(Object decimal) {
		DecimalFormat fmt = new DecimalFormat("#0.00");
		DecimalFormatSymbols dfs = fmt.getDecimalFormatSymbols();
		dfs.setDecimalSeparator('.');
		fmt.setDecimalFormatSymbols(dfs);
		return fmt.format(decimal);
	}

	public static String formatDecimal(String decimal) {
		return formatDecimal(decimal);
	}

	public static String getLst() {
		String list = DBAnalyzer.getPreference("connection.list").replace("\\",
				"/");
		int startIndex = 0;
		int endIndex = list.length() - 4;
		if (list.lastIndexOf("/") > 0)
			startIndex = list.lastIndexOf("/") + 1;
		if (list.indexOf(".lst") > 0)
			endIndex = list.indexOf(".lst");
		return list.substring(startIndex, endIndex);
	}

	public static ArrayList<Database> getDBList() {
		if (dbList == null)
			return dbList = new DBUrlReader().getList();
		else {
			return dbList;
		}
	}

	public static ArrayList<Database> getDBList(String list) {
		return dbList = new DBUrlReader().getList(list);
	}

	public static boolean isNumeric(String number) {
		boolean isValid = false;
		String expression = "[-+]?[0-9]*\\.?[0-9]+$";
		CharSequence inputStr = number;
		Pattern pattern = Pattern.compile(expression);
		Matcher matcher = pattern.matcher(inputStr);
		if (matcher.matches()) {
			isValid = true;
		}
		return isValid;
	}

	/**
	 * Translates the given String into ASCII code.
	 *
	 * @param input the input which contains native characters like umlauts etc
	 * @return the input in which native characters are replaced through ASCII code
	 */
	public static String nativeToAscii( String input ) {
		if (input == null) {
			return null;
		}
		StringBuilder buffer = new StringBuilder( input.length() + 60 );
		for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            if (c <= 0x7E) {
                buffer.append(c);
            }
            else {
            	buffer.append("\\u");
            	String hex = Integer.toHexString(c);
            	for (int j = hex.length(); j < 4; j++ ) {
            		buffer.append( '0' );
            	}
            	buffer.append( hex );
            }
        }
		return buffer.toString();
	}


	/**
	 * Translates the given String into ASCII code.
	 *
	 * @param input the input which contains native characters like umlauts etc
	 * @return the input in which native characters are replaced through ASCII code
	 */
	public static String asciiToNative( String input ) {
		if (input == null) {
			return null;
		}
		StringBuilder buffer = new StringBuilder( input.length() );
		boolean precedingBackslash = false;
		for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            if (precedingBackslash) {
            	switch (c) {
            	case 'f': c = '\f'; break;
            	case 'n': c = '\n'; break;
            	case 'r': c = '\r'; break;
            	case 't': c = '\t'; break;
            	case 'u':
            		String hex = input.substring( i + 1, i + 5 );
            		c = (char) Integer.parseInt(hex, 16 );
            		i += 4;
            	}
            	precedingBackslash = false;
            } else {
            	precedingBackslash = (c == '\\');
            }
            if (!precedingBackslash) {
                buffer.append(c);
            }
        }
		return buffer.toString();
	}

    public static void main(String[] args) {
        String s = "Привет";
        System.out.println(Utils.nativeToAscii(s));
        s = Utils.nativeToAscii(s);
        System.out.println(Utils.asciiToNative(s));
    }



}
