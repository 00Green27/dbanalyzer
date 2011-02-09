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

package ru.green.ui.table;

import java.util.ArrayList;

import javax.swing.table.AbstractTableModel;

/**
 * 
 * @author Edward Dagaev
 */
public class ArrayListTableModel extends AbstractTableModel {

	private ArrayList rows;
	private String[] columnNames = {};

	/**
	 * 
	 * @param columnNames
	 * @param rows
	 */
	public ArrayListTableModel(String[] columnNames, ArrayList rows) {
		this.columnNames = columnNames;
		this.rows = rows;
	}

	@Override
	public String getColumnName(int column) {
		if (columnNames[column] != null) {
			return columnNames[column];
		} else {
			return "";
		}
	}

	@Override
	public int getRowCount() {
		return rows.size();
	}

	@Override
	public int getColumnCount() {
		return columnNames.length;
	}

	@Override
	public Object getValueAt(int aRow, int aColumn) {
		ArrayList row = (ArrayList) rows.get(aRow);
		return row.get(aColumn);
	}
}
