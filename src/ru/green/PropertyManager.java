/*
 * PropertyManager.java - Класс, управляющий настройками программы
 *
 * Copyright (C) 2010 ehd
 */
package ru.green;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

/**
 * Класс, управляющий настройками программы
 * 
 * @author ehd
 */
public class PropertyManager {
	private Properties gui = new Properties();
	private Properties settings = new Properties();

	String getPreference(String name) {
		String value = settings.getProperty(name);
		if (value != null)
			return value;
		else
			return "";
	}

	String getProperty(String name) {
		String value = gui.getProperty(name);
		if (value != null)
			return value;
		else
			return "";
	}

	/**
	 * Метод загружает настройки интерфейса из файла
	 * 
	 * @param in
	 *            поток ввода
	 * @throws IOException
	 *             ошибка ввода/вывода
	 */
	void loadGuiProps(InputStream in) throws IOException {
		loadProps(gui, in);
	}

	/**
	 * Метод загружает настройки программы из файла
	 * 
	 * @param in
	 *            поток ввода
	 * @throws IOException
	 *             ошибка ввода/вывода
	 */
	void loadSettingsProps(InputStream in) throws IOException {
		loadXmlProps(settings, in);
	}

	/**
	 * Метод сохраняет настройки интерфейса в файл
	 * 
	 * @param out
	 *            поток вывода
	 * @throws IOException
	 *             ошибка ввода/вывода
	 */
	void saveGuiProps(OutputStream out) throws IOException {
		gui.store(out, "DBAnalyzer properties");
	}

	/**
	 * Метод сохраняет настройки программы в файл
	 * 
	 * @param out
	 *            поток вывода
	 * @throws IOException
	 *             ошибка ввода/вывода
	 */
	void saveSettingsProps(OutputStream out) throws IOException {
		settings.storeToXML(out, "DBAnalyzer settings");
	}

	void setPreference(String name, String value) {
		if (value == null) {
			settings.setProperty(name, "");
		} else {
			settings.setProperty(name, value);
		}
	}

	void setProperty(String name, String value) {
		if (value == null) {
			gui.setProperty(name, "");
		} else {
			gui.setProperty(name, value);
		}
	}

	private void loadProps(Properties into, InputStream in) throws IOException {
		try {
			into.load(in);
		} finally {
			in.close();
		}
	}

	private void loadXmlProps(Properties into, InputStream in)
			throws IOException {
		try {
			into.loadFromXML(in);
		} finally {
			in.close();
		}
	}
}
