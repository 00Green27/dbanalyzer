package ru.green.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JTextField;

import net.miginfocom.swing.MigLayout;
import ru.green.DBAnalyzer;

public class CriterionPanel extends JPanel {
	private JTextField textField;
	private JComboBox fieldComboBox;
	private JComboBox comboBox;
	private JComboBox expressionComboBox;
	static final String[] operand = new String[] { "и", "или" };
	static final String[] sqlOperand = new String[] { "AND", "OR" };
	static final String[] humanOperator = { "равно", "не равно", "содержит",
			"не содержит", "меньше", "больше", "меньше или равно",
			"больше или равно" };
	static final String[] operator = { "=", "<>", "CONTAINING",
			"NOT CONTAINING", "<", ">", "<=", ">=" };

	public String getHumanOperator() {
		return humanOperator[expressionComboBox.getSelectedIndex()];
	}

	public String getOperator() {
		return operator[expressionComboBox.getSelectedIndex()];
	}

	public String getHumanOperand() {
		return operand[comboBox.getSelectedIndex()];
	}

	public String getOperand() {
		return sqlOperand[comboBox.getSelectedIndex()];
	}

	public int getOperation() {
		return comboBox.getSelectedIndex();
	}

	public String getField() {
		return (String) fieldComboBox.getSelectedItem();
	}

	public String getExpression() {
		return (String) expressionComboBox.getSelectedItem();
	}

	public String getValue() {
		return textField.getText();
	}

	/**
	 * Create the panel.
	 */
	public CriterionPanel(final String[] fields, final JPanel parent) {
		setLayout(new MigLayout("", "[][fill][grow][][]", "[]"));

		fieldComboBox = new WideComboBox(fields);
		add(fieldComboBox, "cell 0 0,w 30%!");

		expressionComboBox = new WideComboBox(humanOperator);
		add(expressionComboBox, "cell 1 0,w 20%!");

		textField = new JTextField();
		add(textField, "cell 2 0,grow");
		textField.setColumns(10);

		JButton delButton = new JButton(DBAnalyzer.getProperty("common.delete"));
		delButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				int count = parent.getComponentCount();
				parent.remove(CriterionPanel.this);
				parent.revalidate();
				parent.repaint();
			}
		});

		comboBox = new JComboBox(operand);
		add(comboBox, "cell 3 0");
		add(delButton, "cell 4 0");

	}

	public void setFields(String[] fields) {
		fieldComboBox.setModel(new DefaultComboBoxModel(fields));
	}

}
