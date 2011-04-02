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
import java.io.File;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JTabbedPane;
import net.miginfocom.swing.MigLayout;
import javax.swing.JLabel;
import javax.swing.JTextField;

import ru.green.DBAnalyzer;
import ru.green.utils.IOUtils;
import ru.green.utils.Utils;

import javax.swing.JCheckBox;
import javax.swing.JComboBox;

public class SettingsDialog extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JTextField driverTextField;
	private JTextField protocolTextField;
	private JTextField userTextField;
	private JTextField passwordTextField;
	private JTextField parametersTextField;
	private JButton okButton;
	private JButton cancelButton;
	private JCheckBox editorCheckBox;
	private JCheckBox themeCheckBox;
	private JTextField bookmarkTextField;
	private JComboBox listComboBox;
	private JButton reviewButton;
	private JFileChooser fileOpen;
	private JTabbedPane tabbedPane;
	private JCheckBox trayCheckBox;
	private final MainFrame frame;

	/**
	 * Create the dialog.
	 *
	 * @param frame
	 */
	public SettingsDialog(MainFrame frame) {
		super(frame, true);
		this.frame = frame;
		setTitle(DBAnalyzer.getProperty("settings-dialog.title"));
		setBounds(100, 100, 520, 320);

		ActionHandler handler = new ActionHandler();

		fileOpen = new JFileChooser();
		fileOpen.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new BorderLayout(0, 0));
		tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		contentPanel.add(tabbedPane);
		JPanel connectionPanel = new JPanel();
		tabbedPane.addTab(
				DBAnalyzer.getProperty("settings-dialog.connection.label"),
				null, connectionPanel, null);
		connectionPanel.setLayout(new MigLayout("", "[][grow][]",
				"[][][][][][][]"));
		JLabel connectionLabel = new JLabel(
				DBAnalyzer.getProperty("settings-dialog.connection.driver"));
		connectionPanel.add(connectionLabel, "cell 0 0,alignx trailing");
		driverTextField = new JTextField(
				DBAnalyzer.getPreference("connection.driver-class"));
		connectionPanel.add(driverTextField, "cell 1 0,growx");
		driverTextField.setColumns(10);
		JLabel driverLabel = new JLabel(
				DBAnalyzer.getProperty("settings-dialog.connection.protocol"));
		connectionPanel.add(driverLabel, "cell 0 1,alignx trailing");
		protocolTextField = new JTextField(
				DBAnalyzer.getPreference("connection.protocol"));
		connectionPanel.add(protocolTextField, "cell 1 1,growx");
		protocolTextField.setColumns(10);
		JLabel protocolLabel = new JLabel(
				DBAnalyzer.getProperty("settings-dialog.connection.user"));
		connectionPanel.add(protocolLabel, "cell 0 2,alignx trailing");
		userTextField = new JTextField(
				DBAnalyzer.getPreference("connection.user"));
		connectionPanel.add(userTextField, "cell 1 2,growx");
		userTextField.setColumns(10);
		JLabel userLabel = new JLabel(
				DBAnalyzer.getProperty("settings-dialog.connection.password"));
		connectionPanel.add(userLabel, "cell 0 3,alignx trailing");
		passwordTextField = new JTextField(
				DBAnalyzer.getPreference("connection.password"));
		connectionPanel.add(passwordTextField, "cell 1 3,growx");
		passwordTextField.setColumns(10);
		JLabel passwordLabel = new JLabel(
				DBAnalyzer.getProperty("settings-dialog.connection.parameters"));
		connectionPanel.add(passwordLabel, "cell 0 4,alignx trailing");
		parametersTextField = new JTextField(
				DBAnalyzer.getPreference("connection.parameters"));
		connectionPanel.add(parametersTextField, "cell 1 4,growx");
		parametersTextField.setColumns(10);

		JLabel listLabel = new JLabel(
				DBAnalyzer.getProperty("settings-dialog.connection.lst"));
		connectionPanel.add(listLabel, "cell 0 5,alignx trailing");

		String lst = Utils.getLst();
		listComboBox = new JComboBox();
		listComboBox.setModel(new DefaultComboBoxModel(IOUtils.getLst()));
		listComboBox.setSelectedItem(lst);
		connectionPanel.add(listComboBox, "cell 1 5,growx");

		JPanel viewPanel = new JPanel();
		tabbedPane.addTab(DBAnalyzer.getProperty("settings-dialog.view.label"),
				null, viewPanel, null);
		viewPanel.setLayout(new MigLayout("", "[][]", "[][][][]"));

		themeCheckBox = new JCheckBox(
				DBAnalyzer.getProperty("settings-dialog.view.theme"));
		themeCheckBox
				.setSelected(DBAnalyzer.getBooleanPreference("view.theme"));
		viewPanel.add(themeCheckBox, "cell 0 0");

		editorCheckBox = new JCheckBox(
				DBAnalyzer.getProperty("settings-dialog.view.editor"));
		editorCheckBox.setSelected(DBAnalyzer
				.getBooleanPreference("view.editor"));
		viewPanel.add(editorCheckBox, "cell 0 1");

		trayCheckBox = new JCheckBox(
				DBAnalyzer.getProperty("settings-dialog.view.tray"));
		trayCheckBox.setSelected(DBAnalyzer.getBooleanPreference("view.tray"));
		viewPanel.add(trayCheckBox, "cell 0 2");

		JPanel miscPanel = new JPanel();
		tabbedPane.addTab(DBAnalyzer.getProperty("settings-dialog.misc.label"),
				null, miscPanel, null);
		miscPanel.setLayout(new MigLayout("", "[][grow][][]", "[][][]"));

		JLabel bookmarkLabel = new JLabel(
				DBAnalyzer.getProperty("settings-dialog.misc.bookmark"));
		miscPanel.add(bookmarkLabel, "cell 0 0,alignx trailing");

		bookmarkTextField = new JTextField();
		miscPanel.add(bookmarkTextField, "cell 1 0,growx");
		bookmarkTextField.setColumns(10);

		reviewButton = new JButton(
				DBAnalyzer.getProperty("settings-dialog.misc.review"));
		reviewButton.addActionListener(handler);
		miscPanel.add(reviewButton, "cell 2 0");

		JPanel buttonPane = new JPanel();
		buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
		getContentPane().add(buttonPane, BorderLayout.SOUTH);

		okButton = new JButton(DBAnalyzer.getProperty("common.ok"));
		okButton.addActionListener(handler);
		buttonPane.add(okButton);
		getRootPane().setDefaultButton(okButton);
		cancelButton = new JButton(DBAnalyzer.getProperty("common.cancel"));
		cancelButton.addActionListener(handler);
		buttonPane.add(cancelButton);
		setLocationRelativeTo(frame);
	}

	@Override
	public void setVisible(boolean b) {
		tabbedPane.setSelectedIndex(0);
		bookmarkTextField.setText(DBAnalyzer.getPreference("misc.bookmark"));
		super.setVisible(b);

	}

	private void save() {
		DBAnalyzer.setPreference("connection.driver-class",
				driverTextField.getText());
		DBAnalyzer.setPreference("connection.protocol",
				protocolTextField.getText());
		DBAnalyzer.setPreference("connection.parameters",
				parametersTextField.getText());
		DBAnalyzer.setPreference("connection.user", userTextField.getText());
		DBAnalyzer.setPreference("connection.password",
				passwordTextField.getText());
		DBAnalyzer.setBooleanPreference("view.theme",
				themeCheckBox.isSelected());
		DBAnalyzer.setBooleanPreference("view.editor",
				editorCheckBox.isSelected());
		DBAnalyzer.setBooleanPreference("view.tray", trayCheckBox.isSelected());
		frame.setTrayVisible(trayCheckBox.isSelected());
		String bookmark = bookmarkTextField.getText();
		if (bookmark.isEmpty())
			bookmark = "sql";
		DBAnalyzer.setPreference("misc.bookmark", bookmark);
		DBAnalyzer.setPreference("connection.list", "etc" + File.separator
				+ listComboBox.getSelectedItem() + ".lst");
		DBAnalyzer.saveSettings();
		dispose();
	}

	private void chooseDir() {
		int ret = fileOpen.showOpenDialog(this);
		if (ret == JFileChooser.APPROVE_OPTION) {
			bookmarkTextField.setText(fileOpen.getSelectedFile()
					.getAbsolutePath());
		}
	}

	class ActionHandler implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == okButton) {
				save();
			} else if (e.getSource() == cancelButton) {
				dispose();
			} else if (e.getSource() == reviewButton) {
				chooseDir();
			}

		}

	}

}
