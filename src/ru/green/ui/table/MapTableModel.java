package ru.green.ui.table;

import java.util.ArrayList;

import javax.swing.table.AbstractTableModel;

import ru.green.model.Field;

public class MapTableModel extends AbstractTableModel {

	private final ArrayList<Field> fields;

	public MapTableModel(ArrayList<Field> fields) {
		this.fields = fields;

	}

	@Override
	public int getColumnCount() {
		return 3;
	}

	@Override
	public String getColumnName(int columnIndex) {
		switch (columnIndex) {
		case 0:
			return "Показать";

		case 1:
			return "Наименование";
		case 2:
			return "Значение";
		}
		return "";
	}

	@Override
	public int getRowCount() {
		return getFields().size();
	}

	@Override
	public Class<?> getColumnClass(int columnIndex) {
		if (columnIndex == 0)
			return Boolean.class;
		else {
			return String.class;
		}
	}

	@Override
	public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
		if (columnIndex == 0)
			getFields().get(rowIndex).setSelected(
					Boolean.parseBoolean(aValue.toString()));
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		Field field = getFields().get(rowIndex);
		switch (columnIndex) {
		case 0:
			return field.isSelected();
		case 1:
			return field.getTitle();
		case 2:
			return field.getName();
		}
		return "";
	}

	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		return columnIndex == 0;
	}

	public ArrayList<Field> getFields() {
		return fields;
	}
}
