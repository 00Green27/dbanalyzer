/*
 * DBUrlReader.java - Класс считывает из файла список подключений к базам. Имя подключения и url.
 *
 * Copyright (C) 2010 ehd
 */
package ru.stavjust.dbutils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Scanner;

import ru.stavjust.DBAnalyzer;
import ru.stavjust.model.Database;
import ru.stavjust.utils.Utils;

/**
 * Класс считывает из файла список подключений к базам. Имя подключения и url.
 * 
 * @author ehd
 */
public class DBUrlReader {
	ArrayList<Database> list = new ArrayList<Database>();

	public DBUrlReader() {
		this.read(DBAnalyzer.getPreference("connection.list"));
	}

	/**
	 * Считать данные из файла.
	 */
	public void read(String file) {
		BufferedReader in = null;
		try {
			in = new BufferedReader(new InputStreamReader(new FileInputStream(
					file), "windows-1251"));

		} catch (FileNotFoundException e) {
			// TODO: exception
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO: exception
			e.printStackTrace(); // To change body of catch statement use File |
									// Settings | File Templates.
		}
		Scanner scanner = new Scanner(in);
		list.clear();
		while (scanner.hasNextLine()) {
			// Database db = new Database(scanner.nextLine(),
			// Utils.constructURL(scanner.nextLine()));
			Database db = new Database();
			db.setName(scanner.nextLine());
			String location = scanner.nextLine();
			db.setLocation(location);
			db.setUrl(Utils.constructURL(location));
			list.add(db);
		}
		try {
			if (in != null) {
				in.close();
			}
		} catch (IOException ignored) {
		}
		scanner.close();
	}

	/**
	 * Получить список подключений.
	 * 
	 * @return Список подключений.
	 */
	public ArrayList<Database> getList() {
		return list;
	}

	public ArrayList<Database> getList(String lstName) {
		this.read("etc" + File.separator + lstName + ".lst");
		return list;
	}
}
