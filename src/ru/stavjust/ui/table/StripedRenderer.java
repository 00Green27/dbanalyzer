package ru.stavjust.ui.table;

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