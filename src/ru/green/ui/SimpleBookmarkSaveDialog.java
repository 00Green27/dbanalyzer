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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileFilter;
import java.util.Vector;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import net.miginfocom.swing.MigLayout;
import ru.green.DBAnalyzer;
import ru.green.utils.IOUtils;

public class SimpleBookmarkSaveDialog extends JDialog {
	private JComboBox dirComboBox;
	private String sql;
	private JTextField nameTextField;
	private String destinationFile;

	public String getDestinationFile() {
		if (destinationFile != null)
			return destinationFile;
		else
			return "";
	}

	public SimpleBookmarkSaveDialog(JFrame frame, boolean modal) {
		super(frame, modal);
		setTitle(DBAnalyzer.getProperty("bookmark-dialog.title"));
		getContentPane().setLayout(new MigLayout("", "[][][][]", "[][][][]"));
		JLabel bookmarkLabel = new JLabel(
				DBAnalyzer.getProperty("bookmark-dialog.bookmark-label"));
		JLabel nameLabel = new JLabel(
				DBAnalyzer.getProperty("bookmark-dialog.name-label"));
		JLabel dirLabel = new JLabel(
				DBAnalyzer.getProperty("bookmark-dialog.dir-label"));
		nameTextField = new JTextField();
		dirComboBox = new JComboBox(getModel());
		JButton saveButton = new JButton(DBAnalyzer.getProperty("common.done"));
		saveButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				String fileName = nameTextField.getText();
				if (fileName.isEmpty())
					fileName = DBAnalyzer.getProperty("common.new-file");
				destinationFile = "sql" + File.separator
						+ dirComboBox.getSelectedItem() + File.separator
						+ fileName + ".sql";
				IOUtils.writeFile(sql, destinationFile);
				dispose();
			}
		});

		JButton mkdirButton = new JButton(
				DBAnalyzer.getProperty("bookmark-dialog.mkdir-button"));
		mkdirButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				createDir();
			}
		});

		getContentPane().add(bookmarkLabel, "cell 0 0");
		getContentPane().add(nameLabel, "cell 0 1");
		getContentPane().add(nameTextField, "cell 1 1 3 1,growx");
		getContentPane().add(dirLabel, "cell 0 2");
		getContentPane().add(dirComboBox, "cell 1 2 3 1,growx");
		getContentPane().add(mkdirButton, "tag right,cell 1 3");

		JButton cancelButton = new JButton(
				DBAnalyzer.getProperty("common.cancel"));
		cancelButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		getContentPane().add(cancelButton, "cell 2 3");
		getContentPane().add(saveButton, "tag right,cell 3 3");
		pack();
		setResizable(false);
		setLocationRelativeTo(frame);

	}

	private DefaultComboBoxModel getModel() {
		File[] listFiles = new File("sql").listFiles(new FileFilter() {

			@Override
			public boolean accept(File pathname) {
				return pathname.isDirectory();
			}
		});
		Vector<String> v = new Vector<String>();
		for (File file : listFiles) {
			v.add(file.getName());
		}
		return new DefaultComboBoxModel(v);
	}

	public void show(String sql) {
		this.sql = sql;
		this.nameTextField.setText("");
		this.pack();
		this.setVisible(true);
	}

	private void createDir() {
		String input = JOptionPane.showInputDialog(null,
				DBAnalyzer.getProperty("bookmark-dialog.new-folder.msg"),
				DBAnalyzer.getProperty("bookmark-dialog.new-folder.title"),
				JOptionPane.QUESTION_MESSAGE);
		if (input == null || input.isEmpty())
			return;
		File file = new File("sql" + File.separator + input);
		if (!file.exists())
			file.mkdirs();

		dirComboBox.setModel(getModel());
		dirComboBox.setSelectedItem(input);
	}
}
