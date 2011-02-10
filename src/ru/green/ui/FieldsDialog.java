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

package ru.green.ui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import net.miginfocom.swing.MigLayout;

import org.jdom.JDOMException;

import ru.green.DBAnalyzer;
import ru.green.model.Field;
import ru.green.model.Table;
import ru.green.ui.table.MapTableModel;
import ru.green.utils.MapReader;

public class FieldsDialog extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JTable table;
	private String[] tablesName;
	private ArrayList<Table> tables;
	private JComboBox comboBox;
	private ArrayList<Field> fields;
	private String sqlFields;
	private String humanFields;
	private JLabel titleLabel;
	private final MainFrame frame;
	private String query;
	private String tableName;

	public String getQuery() {
		return sqlFields;
	}

	public String getHumanQuery() {
		return humanFields;
	}

	/**
	 * Create the dialog.
	 */
	public FieldsDialog(MainFrame frame, String query, boolean isSQL) {
		super(frame, true);
		this.frame = frame;
		this.query = query;
		setTitle(DBAnalyzer.getProperty("fields-dialog.title"));
		init();
		if (isSQL)
			tableName = findTableName(query);
		else
			tableName = query;
		System.out.println(tableName);
		String tableTitle = "";
		for (Table t : tables) {
			if (t.getName().equals(tableName))
				tableTitle = t.getTitle();
		}
		setBounds(100, 100, 450, 300);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new MigLayout("", "[][grow]", "[][grow]"));
		{
			JLabel tableLabel = new JLabel(
					DBAnalyzer.getProperty("fields-dialog.table.label"));
			contentPanel.add(tableLabel, "cell 0 0,alignx trailing");
		}
		{
			// comboBox = new JComboBox(tablesName);
			// comboBox.setEnabled(false);
			// comboBox.setSelectedItem(tableTitle);
			// comboBox.addActionListener(new ActionListener() {
			//
			// @Override
			// public void actionPerformed(ActionEvent arg0) {
			// setTable();
			// }
			// });
			titleLabel = new JLabel(tableTitle);
			contentPanel.add(titleLabel, "cell 1 0,growx");
		}
		{
			JScrollPane scrollPane = new JScrollPane();
			contentPanel.add(scrollPane, "cell 0 1 2 1,grow");
			{

				table = new JTable();
				table.setFillsViewportHeight(true);
				setTable();
				scrollPane.setViewportView(table);
			}
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setBorder(new EmptyBorder(0, 0, 5, 5));
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton(
						DBAnalyzer.getProperty("common.ok"));
				okButton.addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {
						StringBuilder sql = new StringBuilder();
						StringBuilder hs = new StringBuilder();
						for (Field f : fields) {
							if (f.isSelected()) {
								sql.append(f.getName()).append(" AS '")
										.append(f.getTitle()).append("', ");
								hs.append(f.getTitle()).append(", ");
							}
						}
						if (sql.length() > 0) {
							sql.delete(sql.length() - 2, sql.length());
							hs.delete(hs.length() - 2, hs.length());

						}
						FieldsDialog.this.sqlFields = createSql(sql.toString());
						FieldsDialog.this.humanFields = hs.toString();

						dispose();

						// Iterator<Field> i = fields.iterator();
						// boolean hasNext = i.hasNext();
						// while (hasNext) {
						// Field f = (Field) i.next();
						// if (f.isSelected()) {
						//
						// sql.append(f.getName() + " AS '" + f.getTitle()
						// + "'");
						// hs.append(f.getTitle());
						// }
						// hasNext = i.hasNext();
						// if (hasNext) {
						// sql.append(", ");
						// hs.append(", ");
						// }
						// }
						// FieldsDialog.this.sqlFields = sql.toString();
						// FieldsDialog.this.humanFields = hs.toString();
						// System.out.println(sqlFields);
						// System.out.println(humanFields);
					}

				});
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
				setLocationRelativeTo(frame);
			}
			{
				JButton cancelButton = new JButton(
						DBAnalyzer.getProperty("common.cancel"));
				cancelButton.addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {
						dispose();
					}
				});
				buttonPane.add(cancelButton);
			}
		}
	}

	private String createSql(String fields) {
		String sql = "SELECT " + fields + " FROM " + this.tableName;
		String param = "";
		if (this.query.indexOf(" WHERE ") > 0)
			param = this.query.substring(this.query.indexOf(" WHERE ") + 7);
		else if (this.query.indexOf(" where ") > 0)
			param = this.query.substring(this.query.indexOf(" where ") + 7);
		if (!param.isEmpty())
			sql += " WHERE " + param;
		return sql;
	}

	// TODO: Поиск таблицы с учетом алиасов
	private String findTableName(String sql) {
		sql = sql.toUpperCase().replace("\n", " ");
		System.out.println(sql);
		if (sql.indexOf(" FROM ") > 0)
			sql = sql.substring(sql.indexOf(" FROM ") + 6);
		else if (sql.indexOf(" from ") > 0)
			sql = sql.substring(sql.indexOf(" from ") + 6);
		sql = sql.trim();
		sql = sql.substring(0, sql.indexOf(" "));
		return sql.trim();
	}

	private void setTable() {
		for (Table t : tables) {
			// if (t.getTitle().equals(comboBox.getSelectedItem())) {
			if (t.getTitle().equals(titleLabel.getText())) {
				fields = t.getFileds();
				MapTableModel model = new MapTableModel(fields);
				table.setModel(model);
				table.setRowSorter(new TableRowSorter<TableModel>(model));
				table.getColumnModel().getColumn(0).setMaxWidth(20);
			}
		}
	}

	private void init() {
		MapReader xml;
		try {
			xml = new MapReader();
			tables = xml.read();
			tablesName = new String[tables.size()];
			for (int i = 0; i < tables.size(); i++) {
				tablesName[i] = tables.get(i).getTitle();
			}
		} catch (JDOMException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
