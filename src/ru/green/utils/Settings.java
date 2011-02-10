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

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import ru.green.dbutils.DBUrlReader;
import ru.green.model.Database;

/**
 * @author ehd
 */
public class Settings {

	private static final String CONNECTION_DRIVER = "connection.driver-class";
	private static final String DATABASE_PROTOCOL = "connection.protocol";
	private static final String CONNECTION_USER = "connection.user";
	private static final String CONNECTION_PASSWORD = "connection.password";
	private static final String CONNECTION_PARAMETERS = "connection.parameters";
	private static final String CONNECTION_LIST = "connection.list";
	private static final String PREFERENCES_FILE = "etc/preferences.xml";
	private static final Settings self;
	private static ArrayList<Database> dbList;
	private final Properties props = new Properties();

	static {
		self = new Settings();
	}

	private static Settings getInstance() {
		return self;
	}

	private Settings() {
		FileInputStream in = null;
		try {
			try {
				in = new FileInputStream(PREFERENCES_FILE);
				props.loadFromXML(in);
			} finally {
				if (in != null) {
					in.close();
				}
			}
		} catch (IOException ex) {
			Logger.getLogger(Settings.class.getName()).log(Level.SEVERE, null,
					ex);
		}
	}

	public static void load(FileInputStream in) throws IOException {
		try {
			in = new FileInputStream(PREFERENCES_FILE);
			getInstance().props.loadFromXML(in);
		} finally {
			in.close();
		}
	}

	public static void store() throws IOException {
		getInstance().props.storeToXML(new FileOutputStream(PREFERENCES_FILE),
				new Date().toString());
	}

	public static String getDriver() {
		return getInstance().props.getProperty(CONNECTION_DRIVER);
	}

	public static void setDriver(String value) {
		getInstance().props.setProperty(CONNECTION_DRIVER, value);
	}

	public static String getProtocol() {
		return getInstance().props.getProperty(DATABASE_PROTOCOL);
	}

	public static void setProtocol(String value) {
		getInstance().props.setProperty(DATABASE_PROTOCOL, value);
	}

	public static String getParameters() {
		return getInstance().props.getProperty(CONNECTION_PARAMETERS);
	}

	public static void setParameters(String value) {
		getInstance().props.setProperty(CONNECTION_PARAMETERS, value);
	}

	public static String getUser() {
		return getInstance().props.getProperty(CONNECTION_USER);
	}

	public static boolean isSkinEnabled() {
		return Boolean.parseBoolean(getInstance().props.getProperty("skin",
				"true"));
	}

	public static boolean isUpdateEnabled() {
		return Boolean.parseBoolean(getInstance().props.getProperty("update",
				"false"));
	}

	public static void setUser(String value) {
		getInstance().props.setProperty(CONNECTION_USER, value);
	}

	public static String getPassword() {
		String password = getInstance().props.getProperty(CONNECTION_PASSWORD);
		if (password.isEmpty()) {
			return "dbrnjhbz";
		} else {
			return password;
		}
	}

	public static void setPassword(String value) {
		getInstance().props.setProperty(CONNECTION_PASSWORD, value);
	}

	public static String getList() {
		return getInstance().props.getProperty(CONNECTION_LIST);
	}

	public static boolean getBooleanProperty(String value) {
		return Boolean.parseBoolean(getInstance().props.getProperty(value));
	}

	public static String getLst() {
		return getList().substring(
				Settings.getList().replace("\\", "/").lastIndexOf("/") + 1,
				Settings.getList().indexOf(".lst"));
	}

	public static void setList(String value) {
		getInstance().props.setProperty(CONNECTION_LIST, value);
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
}
