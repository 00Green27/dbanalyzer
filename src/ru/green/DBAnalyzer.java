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

package ru.green;

import ru.green.ui.MainFrame;

import javax.swing.*;
import java.awt.*;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Основной класс программы
 *
 * @author ehd
 */
public class DBAnalyzer {
    private static final String PREFERENCES = "etc/preferences.xml";
    private static PropertyManager propMgr;

	/**
	 * Method main.
	 *
	 * @param args
	 *            String[]
	 */
	public static void main(String[] args) {
		initSystemProperties();
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					if (getBooleanPreference("view.theme")) {
						JFrame.setDefaultLookAndFeelDecorated(true);
						JDialog.setDefaultLookAndFeelDecorated(true);
						UIManager
								.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
					} else
						UIManager.setLookAndFeel(UIManager
								.getSystemLookAndFeelClassName());
				} catch (Exception e) {
					e.printStackTrace();
				}
				new MainFrame().setVisible(true);
			}
		});
	}

	/**
	 * Метод возврящает булево значение ключа из из файла настроек
	 *
	 * @param name
	 *            название ключа
	 *
	 * @return значение ключа
	 */
	public static final boolean getBooleanPreference(String name) {
		return getBooleanPreference(name, false);
	}

	/**
	 * Метод возврящает значение ключа из из файла настроек
	 *
	 * @param name
	 *            название ключа
	 *
	 * @return значение ключа
	 */
	public static String getPreference(String name) {
		return propMgr.getPreference(name);
	}

	/**
	 * Метод возврящает значение ключа из из файла настроек
	 *
	 * @param name
	 *            название ключа
	 * @param def
	 *            значение по умолчанию
	 *
	 * @return значение ключа
	 */
	public static String getPreference(String name, String def) {
		String value = propMgr.getPreference(name);
		if (value == null)
			return def;
		else
			return value;
	}

	/**
	 * Метод возврящает значение ключа из из файла описания GUI
	 *
	 * @param name
	 *            название ключа
	 *
	 * @return значение ключа
	 */
	public static String getProperty(String name) {
		return propMgr.getProperty(name);
	}

	/**
	 * Метод возврящает значение ключа из из файла описания GUI
	 *
	 * @param name
	 *            название ключа
	 * @param def
	 *            значение по умолчанию
	 *
	 * @return значение ключа
	 */
	public static String getProperty(String name, String def) {
		String value = propMgr.getProperty(name);
		if (value == null)
			return def;
		else
			return value;
	}

	/**
	 * Метод инициализации настоек
	 */
	public static void initSystemProperties() {
        System.setProperty("awt.useSystemAAFontSettings", "lcd");
        propMgr = new PropertyManager();
        try {
            propMgr.loadGuiProps(DBAnalyzer.class
                    .getResourceAsStream("/ru/green/gui.props"));
            propMgr.loadSettingsProps(new FileInputStream(PREFERENCES));
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

	public static void saveSettings() {
		try {
			propMgr.saveSettingsProps(new FileOutputStream(PREFERENCES));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Метод устанавливает значение для ключа в файле настроеек
	 *
	 * @param name
	 *            название ключа
	 * @param value
	 *            значение ключа
	 */
	public static void setPreference(String name, String value) {
		propMgr.setPreference(name, value);
	}

	/**
	 * Метод устанавливает значение для ключа в файле описания GUI
	 *
	 * @param name
	 *            название ключа
	 * @param value
	 *            значение ключа
	 */
	public static void setProperty(String name, String value) {
		propMgr.setProperty(name, value);
	}

	/**
	 * Метод возврящает булево значение ключа из из файла настроек
	 *
	 * @param name
	 *            название ключа
	 * @param def
	 *            значение по умолчанию
	 *
	 * @return значение ключа
	 */
	private static boolean getBooleanPreference(String name, boolean def) {
		String value = getPreference(name);
		if (value == null)
			return def;
		else if (value.equals("true") || value.equals("yes")
				|| value.equals("on"))
			return true;
		else if (value.equals("false") || value.equals("no")
				|| value.equals("off"))
			return false;
		else
			return def;
	}

	/**
	 * Метод устанавливает значение для ключа в файле настроеек
	 *
	 * @param name
	 *            название ключа
	 * @param value
	 *            значение ключа
	 */
	public static final void setBooleanPreference(String name, boolean value) {
		setPreference(name, value ? "true" : "false");
	}
}
