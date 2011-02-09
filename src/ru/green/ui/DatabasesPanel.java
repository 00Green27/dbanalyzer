package ru.green.ui;

import net.miginfocom.swing.MigLayout;
import ru.green.DBAnalyzer;
import ru.green.model.Database;
import ru.green.utils.IOUtils;
import ru.green.utils.Utils;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;

public class DatabasesPanel extends JPanel {
	private JCheckBox[] checkBoxes;
	private JPanel panel;
	private MainFrame mainFrame;

	public DatabasesPanel(final MainFrame mainFrame) {
		this.mainFrame = mainFrame;
		setLayout(new MigLayout("ins 0, wrap 2", "[grow][]", "[][][][][][][]"));
		panel = new JPanel(new MigLayout());

		JScrollPane sp = new JScrollPane(panel);
		// FormScroller scroller = new FormScroller(sp);
		// scroller.setType(Type.CHILD);
		// scroller.setScrollInsets(new Insets(0, 0, 0, 0));
		ArrayList<Database> list = Utils.getDBList();
		generateCheckboxes(list);
		JButton selectButton = new JButton(Utils.getIcon(DBAnalyzer
				.getProperty("database.select-button.icon")));
		selectButton.setToolTipText(DBAnalyzer
				.getProperty("database.select-button.tooltip"));
		selectButton.setFocusable(false);
		selectButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				setSelected(true);
			}
		});
		String lst = Utils.getLst();
		final JComboBox lstBox = new JComboBox();
		lstBox.setModel(new DefaultComboBoxModel(IOUtils.getLst()));
		lstBox.setSelectedItem(lst);
		lstBox.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String lst = String.valueOf(lstBox.getSelectedItem());
				ArrayList<Database> list = Utils.getDBList(lst);
				generateCheckboxes(list);
				//mainFrame.setTitle(MainFrame.TITLE + " - " + lst);
                mainFrame.setLst(lst);
			}
		});
		add(lstBox, "cell 0 2,growx");

		JButton editButton = new JButton(Utils.getIcon(DBAnalyzer
				.getProperty("database.edit-button.icon")));
		editButton.setToolTipText(DBAnalyzer
				.getProperty("database.edit-button.tooltip"));
		editButton.setFocusable(false);
		editButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				boolean openFile = Utils.openFile("etc" + File.separator
						+ String.valueOf(lstBox.getSelectedItem()) + ".lst");
			}
		});

		add(editButton, "flowx,cell 0 3");
		add(selectButton, "cell 0 3");
		add(sp, "cell 0 5 30000 1,width 100%,height 100%");

		JButton unselectButton = new JButton(Utils.getIcon(DBAnalyzer
				.getProperty("database.unselect-button.icon")));
		unselectButton.setToolTipText(DBAnalyzer
				.getProperty("database.unselect-button.tooltip"));
		unselectButton.setFocusable(false);
		unselectButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				setSelected(false);
			}
		});
		add(unselectButton, "cell 0 3");

		// JComboBox comboBox = new JComboBox();
		// add(comboBox, "cell 1 0 2 1,growx");
	}

	private void generateCheckboxes(ArrayList<Database> list) {
		panel.removeAll();
		checkBoxes = new JCheckBox[list.size()];
		for (int i = 0; i < list.size(); i++) {
			getCheckBoxes()[i] = new JCheckBox(list.get(i).getName());
			getCheckBoxes()[i].setToolTipText(list.get(i).getLocation());
			getCheckBoxes()[i].setSelected(list.get(i).isSelected());
			panel.add(getCheckBoxes()[i], "wrap");
		}
		panel.invalidate();
		panel.revalidate();
	}

	public JCheckBox[] getCheckBoxes() {
		return checkBoxes;

	}

	private void setSelected(boolean b) {
		for (int i = 0; i < checkBoxes.length; i++) {
			getCheckBoxes()[i].setSelected(b);
		}
	}

}
