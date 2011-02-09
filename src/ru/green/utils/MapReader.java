/*
 * Report.java - Класс считывает информацию о полях из файла xml
 *
 * Copyright (C) 2010 ehd
 */
package ru.green.utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

import ru.green.model.Field;
import ru.green.model.Table;

/**
 * Класс считывает информацию о полях из файла xml
 * 
 * @author ehd
 */
public class MapReader {

	private Document document;
	private String file = "etc/map.xml";

	public MapReader() throws JDOMException, IOException {
		SAXBuilder build = new SAXBuilder();
		document = build.build(file);
	}

	public ArrayList<Table> read() {
		ArrayList<Table> tablesName = new ArrayList<Table>();
		Element rootElement = document.getRootElement();
		for (Iterator iterator = rootElement.getChildren().iterator(); iterator
				.hasNext();) {
			Element table = (Element) iterator.next();
			List filds = table.getChildren();
			ArrayList<Field> fields = new ArrayList<Field>();
			for (Iterator itr = filds.iterator(); itr.hasNext();) {
				Element field = (Element) itr.next();
				fields.add(new Field(field.getAttributeValue("name"), field
						.getAttributeValue("type"), field
						.getAttributeValue("title")));
			}
			tablesName.add(new Table(table.getAttributeValue("name"), table
					.getAttributeValue("title"), fields));
		}
		return tablesName;
	}
}
