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
