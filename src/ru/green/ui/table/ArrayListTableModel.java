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
