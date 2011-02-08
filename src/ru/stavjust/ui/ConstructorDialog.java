package ru.stavjust.ui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import net.miginfocom.swing.MigLayout;

import org.jdom.JDOMException;

import ru.stavjust.DBAnalyzer;
import ru.stavjust.model.Field;
import ru.stavjust.model.Table;
import ru.stavjust.ui.table.MapTableModel;
import ru.stavjust.utils.MapReader;
import ru.stavjust.utils.Utils;

import javax.swing.JToolBar;

public class ConstructorDialog extends JDialog {

	private final JPanel contentPanel = new JPanel();

	private String[] tablesName;
	private ArrayList<Table> tables;
	private ArrayList<Field> fields;
	private String[] fieldsArray;

	private JComboBox comboBox;
	private JPanel panel;
	private JPanel critPanel;
	private JTable table;
	private String sqlFields;
	private String humanFields;
	private String sql;
	private String humanSql;
	private String sqlCrit;
	private String humanCrit;
	private SelectQuery query;

	private final MainFrame frame;

	private JButton newButton;

	public String getHumanQuery() {
		if (query != null)
			return query.constructHumanQuery();
		return "";
	}

	public String getQuery() {
		if (query != null)
			return query.constructQuery();
		return "";
	}

	public void setVisible(boolean b) {
		super.setVisible(b);
	}

	/**
	 * Create the dialog.
	 */
	public ConstructorDialog(MainFrame frame) {
		super(frame, true);
		this.frame = frame;
		setTitle(DBAnalyzer.getProperty("constructor-dialog.title"));
		init();

		ActionHandler handler = new ActionHandler();

		setSize(600, 520);
		setMinimumSize(getSize());
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new MigLayout("", "[][][][grow]",
				"[][][grow][][grow][grow]"));
		{
			JLabel tableLabel = new JLabel(
					DBAnalyzer.getProperty("constructor-dialog.tables.label"));
			contentPanel.add(tableLabel, "cell 1 1,alignx left");
		}
		{
			comboBox = new JComboBox(tablesName);
			comboBox.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent arg0) {
					readFields();
					reloadCriteria();

				}
			});
			contentPanel.add(comboBox, "cell 2 1 2 1,growx");
		}
		{
			JPanel panel_1 = new JPanel();
			panel_1.setBorder(new TitledBorder(null, DBAnalyzer
					.getProperty("constructor-dialog.columns.label"),
					TitledBorder.LEADING, TitledBorder.TOP, null, null));
			contentPanel.add(panel_1, "cell 1 2 3 1,grow");
			panel_1.setLayout(new MigLayout("", "[][grow]", "[grow]"));
			{
				JScrollPane scrollPane = new JScrollPane();
				// scrollPane.setBorder(new EmptyBorder(new Insets(0, 0, 0,
				// 0)));
				panel_1.add(scrollPane, "cell 0 0 2 1,grow");
				{
					table = new JTable();
					scrollPane.setViewportView(table);
				}
			}
		}
		readFields();
		{
			JButton button = new JButton(
					DBAnalyzer.getProperty("constructor-dialog.add-criterion"));
			contentPanel.add(button, "cell 1 3 2 1");
			critPanel = new JPanel();
			critPanel.setBorder(new TitledBorder(null, "",
					TitledBorder.LEADING, TitledBorder.TOP, null, null));

			contentPanel.add(critPanel, "cell 1 4 3 1,height 100%,grow");
			critPanel.setLayout(new BorderLayout(0, 0));
			{
				panel = new JPanel();
				JScrollPane sp = new JScrollPane(panel);
				sp.setBorder(new EmptyBorder(new Insets(0, 0, 0, 0)));
				critPanel.add(sp);
				panel.setLayout(new MigLayout("wrap 1", "[grow]"));
				// panel.add(new СriterionPanel(fieldsArray, panel), "growx");
			}
			button.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent arg0) {
					panel.add(new CriterionPanel(fieldsArray, panel), "growx");
					panel.revalidate();
				}
			});
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setBorder(new EmptyBorder(0, 0, 5, 5));
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton(
						DBAnalyzer.getProperty("common.create"));
				okButton.addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {
						constructQuery();
						dispose();
					}
				});
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
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
		{
			JToolBar toolBar = new JToolBar();
			toolBar.setFloatable(false);
			getContentPane().add(toolBar, BorderLayout.NORTH);
			{
				newButton = new JButton(Utils.getIcon(DBAnalyzer
						.getProperty("constructor-dialog.new-button.icon")));
				newButton.setToolTipText(DBAnalyzer
						.getProperty("constructor-dialog.new-burron.tooltip"));
				newButton.setFocusable(false);
				newButton.addActionListener(handler);
				toolBar.add(newButton);
			}
		}
		setLocationRelativeTo(null);
	}

	protected void reloadCriteria() {
		panel.removeAll();
		// panel.add(new СriterionPanel(fieldsArray, panel), "growx");
		panel.repaint();

	}

	protected void readFields() {
		for (Table t : tables) {
			if (t.getTitle().equals(comboBox.getSelectedItem())) {
				fields = t.getFileds();
				fieldsArray = new String[fields.size()];
				for (int i = 0; i < fields.size(); i++) {
					fieldsArray[i] = fields.get(i).getTitle();
					fields.get(i).setSelected(false);
				}
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

	@Deprecated
	private void readSelectedFields() {
		StringBuilder sql = new StringBuilder();
		StringBuilder hs = new StringBuilder();
		for (Field f : fields) {
			if (f.isSelected()) {
				sql.append(f.getName()).append(" as '").append(f.getTitle())
						.append("', ");
				hs.append(f.getTitle()).append(", ");
			}
		}
		if (sql.length() > 0) {
			sql.delete(sql.length() - 2, sql.length());
			hs.delete(hs.length() - 2, hs.length());

		}
		this.sqlFields = sql.toString();
		this.humanFields = hs.toString();
	}

	/**
	 * Поместить выбранные поля в конструктор запросов.
	 */
	private void getSelectedFields() {
		for (Field f : fields) {
			if (f.isSelected()) {
				query.addColumn(f.getName(), f.getTitle());
				query.addHumanColumn(f.getTitle());
			}
		}
	}

	private void getCriteria() {
		for (int i = 0; i < panel.getComponents().length; i++) {
			CriterionPanel cp = (CriterionPanel) panel.getComponents()[i];
			if (cp.getOperand().equals("OR")) {
				GroupCriteria criteria = new GroupCriteria();
				GroupCriteria human = new GroupCriteria();
				String fieldType = getFieldType(cp.getField());
				String value = getValue(cp.getValue(), fieldType);
				String humanOperator = cp.getHumanOperator();
				String operator = cp.getOperator();
				if (value.equals("NULL")) {
					if (operator.equals("<>")) {
						operator = "IS NOT";
						humanOperator = " не ";
					} else {
						operator = "IS";
						humanOperator = " есть ";
					}

				}
				criteria.addLeftCriterion(getFieldName(cp.getField()),
						operator, value);
				human.addLeftCriterion(cp.getField(), humanOperator, value);
				CriterionPanel cpInGroup = (CriterionPanel) panel
						.getComponents()[++i];

				fieldType = getFieldType(cpInGroup.getField());
				value = getValue(cpInGroup.getValue(), fieldType);
				humanOperator = cpInGroup.getHumanOperator();
				operator = cpInGroup.getOperator();
				if (value.equals("NULL")) {
					if (operator.equals("<>")) {
						operator = "IS NOT";
						humanOperator = " не ";
					} else {
						operator = "IS";
						humanOperator = " есть ";
					}

				}
				criteria.addRightCriterion(getFieldName(cpInGroup.getField()),
						operator, value);
				human.addRightCriterion(cpInGroup.getField(), humanOperator,
						value);
				human.setOperand(" или ");

				query.addGroupCriteria(criteria);
				query.addHumanGroupCriteria(human);
			} else {
				String fieldType = getFieldType(cp.getField());
				String value = getValue(cp.getValue(), fieldType);
				String humanOperator = cp.getHumanOperator();
				String operator = cp.getOperator();
				if (value.equals("NULL")) {
					if (operator.equals("<>")) {
						operator = "IS NOT";
						humanOperator = " не ";
					} else {
						operator = "IS";
						humanOperator = " есть ";
					}

				}
				query.addCriteria(getFieldName(cp.getField()), operator, value);
				query.addHumanCriteria(cp.getField(), humanOperator, value);
			}

		}

	}

	@Deprecated
	private void readCriteria() {
		StringBuilder sql = new StringBuilder();
		StringBuilder hs = new StringBuilder();
		for (int i = 0; i < panel.getComponents().length; i++) {
			CriterionPanel cp = (CriterionPanel) panel.getComponents()[i];
			if (i != 0) {
				sql.append(cp.getOperand() + " ");
				hs.append(cp.getHumanOperand() + " ");
			}
			sql.append(getFieldName(cp.getField()) + " ");
			hs.append(cp.getField() + " ");
			sql.append(cp.getOperator() + " ");
			hs.append(cp.getHumanOperator() + " ");
			String fieldType = getFieldType(cp.getField());
			if (fieldType.toUpperCase().equals("NUMERIC")) {
				sql.append(cp.getValue());
				hs.append(cp.getValue());
			} else {
				sql.append("'" + cp.getValue() + "' ");
				hs.append("'" + cp.getValue() + "' ");
			}
		}
		this.sqlCrit = sql.toString();
		this.humanCrit = hs.toString();
	}

	private String getValue(String value, String fieldType) {
		if (fieldType.toUpperCase().equals("NUMERIC")) {
			if (value.isEmpty())
				return "NULL";
			return value;
		} else if (fieldType.toUpperCase().equals("DATE") && value.isEmpty()) {
			return "NULL";
		} else {
			return quote(value);
		}
	}

	private String quote(String s) {
		if (s == null)
			return "null";

		StringBuffer str = new StringBuffer();
		str.append('\'');
		for (int i = 0; i < s.length(); i++) {
			if (s.charAt(i) == '\\' || s.charAt(i) == '\"'
					|| s.charAt(i) == '\'') {
				str.append('\\');
			}
			str.append(s.charAt(i));
		}
		str.append('\'');
		return str.toString();
	}

	private String getFieldType(String field) {
		for (Field f : fields) {
			if (field.equals(f.getTitle()))
				return f.getType();
		}
		return "";
	}

	private String getFieldName(String field) {
		for (Field f : fields) {
			if (field.equals(f.getTitle()))
				return f.getName();
		}
		return "";
	}

	@Deprecated
	private void createQuery() {
		readSelectedFields();
		readCriteria();
		String query = "Выбрать столбцы ";
		if (this.sqlFields.isEmpty()) {
			this.sqlFields = "*";
			query = "Выбрать все столбцы";
		}

		this.sql = "SELECT " + this.sqlFields + " FROM " + getTableName();
		this.humanSql = query + this.humanFields + " из таблицы "
				+ comboBox.getSelectedItem();
		if (!this.sqlCrit.isEmpty()) {
			this.sql += " WHERE " + this.sqlCrit;
			this.humanSql += " где " + this.humanCrit;
		}
	}

	private void constructQuery() {
		query = new SelectQuery();
		getSelectedFields();
		getCriteria();
		query.setTable(getTableName());
		query.setHumanTable((String) comboBox.getSelectedItem());
	}

	private String getTableName() {
		String name = "";
		String item = (String) comboBox.getSelectedItem();
		for (Table t : tables) {
			if (t.getTitle().equals(item))
				name = t.getName();
		}
		return name;
	}

	class ActionHandler implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == newButton) {
				readFields();
				reloadCriteria();
			}
		}
	}

}

class SelectQuery {
	private String table;
	private String hTable;
	private List<StringBuilder> selection = new ArrayList<StringBuilder>();
	private List<StringBuilder> criteria = new ArrayList<StringBuilder>();
	private List<StringBuilder> hSelection = new ArrayList<StringBuilder>();
	private List<StringBuilder> hCriteria = new ArrayList<StringBuilder>();

	public void setTable(String table) {
		this.table = table;
	}

	public String getTable() {
		return table;
	}

	public void setHumanTable(String table) {
		this.hTable = table;
	}

	public String getHumanTable() {
		return hTable;
	}

	public void addColumn(String name, String title) {
		StringBuilder column = new StringBuilder(name);
		if (title != null && !title.isEmpty()) {
			column.append(" AS '").append(title).append('\'');
		}
		selection.add(column);
	}

	public void addHumanColumn(String title) {
		hSelection.add(new StringBuilder(title));
	}

	public void addHumanCriteria(String left, String operator, String right) {
		StringBuilder criterion = new StringBuilder();
		criterion.append(left).append(" " + operator + " ").append(right);
		hCriteria.add(criterion);
	}

	public void addCriteria(String left, String operator, String right) {
		StringBuilder criterion = new StringBuilder();
		criterion.append(left).append(" " + operator + " ").append(right);
		criteria.add(criterion);
	}

	public void addGroupCriteria(GroupCriteria group) {
		criteria.add(new StringBuilder(group.toString()));
	}

	public void addHumanGroupCriteria(GroupCriteria group) {
		hCriteria.add(new StringBuilder(group.toString()));
	}

	public String constructQuery() {
		StringBuilder query = new StringBuilder("SELECT ");
		if (!selection.isEmpty()) {
			appendList(query, selection, ",");
		} else {
			query.append('*');
		}
		query.append(" FROM ");
		query.append(getTable());
		if (criteria.size() > 0) {
			query.append(" WHERE ");
			appendList(query, criteria, " AND ");
		}
		return query.toString();

	}

	public String constructHumanQuery() {
		StringBuilder query = new StringBuilder("Выбрать ");
		if (!selection.isEmpty()) {
			query.append("столбцы ");
			appendList(query, hSelection, ",");
		} else {
			query.append("все столбцы ");
		}
		query.append(" из таблицы ");
		query.append(getHumanTable());
		if (hCriteria.size() > 0) {
			query.append(" где ");
			appendList(query, hCriteria, " и ");
		}
		return query.toString();

	}

	private void appendList(StringBuilder out,
			Collection<StringBuilder> collection, String separator) {
		Iterator<StringBuilder> i = collection.iterator();
		boolean hasNext = i.hasNext();
		while (hasNext) {
			StringBuilder s = i.next();
			out.append(s);
			hasNext = i.hasNext();
			if (hasNext) {
				out.append(separator).append(' ');
			}
		}
	}
}

class GroupCriteria {
	private final StringBuilder leftCriterion = new StringBuilder();
	private final StringBuilder rightCriterion = new StringBuilder();
	private String operand = "OR";

	public void setOperand(String operand) {
		this.operand = operand;

	}

	public void addLeftCriterion(String left, String operator, String right) {
		leftCriterion.append(left).append(" " + operator + " ").append(right);
	}

	public void addRightCriterion(String left, String operator, String right) {
		rightCriterion.append(left).append(" " + operator + " ").append(right);
	}

	public String toString() {
		return new StringBuilder("(" + leftCriterion).append(' ')
				.append(operand).append(' ').append(rightCriterion + ")")
				.toString();
	}
}

class Criteia {
	private final String left;
	private final String operand;
	private final String right;

	Criteia(String left, String operand, String right) {
		this.left = left;
		this.operand = operand;
		this.right = right;
	}

	public String toString() {
		return new StringBuilder(left).append(' ').append(operand).append(' ')
				.append(right).toString();
	}
}
