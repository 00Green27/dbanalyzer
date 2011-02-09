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

import java.awt.Color;
import java.awt.Component;

import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;

public class StripedRenderer implements TableCellRenderer {
	private static final Color SELECTED_ROW_COLOR = new Color(61, 128, 223);
	private static final Color ODD_ROW_COLOR = Color.white;
	private static final Color EVEN_ROW_COLOR = new Color(241, 245, 250);
	public static final DefaultTableCellRenderer DEFAULT_RENDERER = new DefaultTableCellRenderer();

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value,
			boolean isSelected, boolean hasFocus, int row, int column) {
		Component renderer = DEFAULT_RENDERER.getTableCellRendererComponent(
				table, value, isSelected, hasFocus, row, column);
		Color foreground, background;
		foreground = Color.black;
		if (isSelected) {
			background = SELECTED_ROW_COLOR;
			foreground = Color.white;
		} else {
			if (row % 2 == 0) {
				background = EVEN_ROW_COLOR;
			} else {
				background = ODD_ROW_COLOR;
			}
		}

		renderer.setForeground(foreground);
		renderer.setBackground(background);
		return renderer;
	}
}
