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

import net.miginfocom.swing.MigLayout;
import ru.green.DBAnalyzer;
import ru.green.dbutils.QueryRunner;
import ru.green.dbutils.handlers.ArrayListHandler;
import ru.green.model.Database;
import ru.green.utils.Utils;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ProgressDialog extends JDialog {
	private final MainFrame frame;
	private JLabel dbLabel;
	private QueryTask queryTask;

	public ProgressDialog(JFrame frame, boolean modal) {
		super(frame, modal);
		this.frame = (MainFrame) frame;
		setTitle(DBAnalyzer.getProperty("progress-dialog.title"));
		setLayout(new MigLayout("insets dialog", "[]10[]",
				"[align top][][grow 100]"));
		setResizable(false);
		JLabel infLabel = new JLabel(Utils.getIcon(DBAnalyzer
				.getProperty("progress-dialog.information.icon")));
		dbLabel = new JLabel("label");
		JProgressBar progressBar = new JProgressBar();
		progressBar.setIndeterminate(true);

		JButton cancelButton = new JButton(
				DBAnalyzer.getProperty("common.cancel"));
		cancelButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent evt) {
				queryTask.cancel(true);
				ProgressDialog.this.setVisible(false);
				ProgressDialog.this.dispose();
			}
		});

		add(infLabel);
		add(dbLabel, "wrap");
		add(progressBar, "span,w 100%");
		add(cancelButton, "tag right, skip, bottom");
		setSize(500, 200);
		setLocationRelativeTo(frame);

	}

	public void run(String sql) {
		if (!sql.toLowerCase().contains("select ")) {
			JOptionPane.showMessageDialog(frame,
					DBAnalyzer.getProperty("msg.query-not-supported"),
					DBAnalyzer.getProperty("app.label"),
					JOptionPane.INFORMATION_MESSAGE);
			return;
		}
		queryTask = new QueryTask(sql, frame);
		queryTask.execute();
		setVisible(true);
	}

	public static void main(String args[]) {
		java.awt.EventQueue.invokeLater(new Runnable() {

			@Override
			public void run() {
				new ProgressDialog(null, true).setVisible(true);
			}
		});
	}

	private class QueryTask extends SwingWorker<ArrayList<ArrayList>, Void> {

		private String sql;
		private String[] split;
		private final MainFrame frame;
		private String[] columnsName;

		public QueryTask(String sql, MainFrame frame) {
			this.sql = sql;
			this.frame = frame;
		}

		@Override
		protected ArrayList<ArrayList> doInBackground() throws Exception {
			frame.getStPanel().clearLog();
			ArrayListHandler alh = new ArrayListHandler();
			ArrayList<Database> list = Utils.getDBList();
			QueryRunner runner;
			ArrayList<ArrayList> result = null;
			ArrayList<ArrayList> all = new ArrayList<ArrayList>();

//			String query = fixSql(this.sql);
			String query = this.sql.replace("\"", "'");
//            query = Utils.nativeToAscii(query);
//            System.out.println(query);

            // if (DBAnalyzer.getBooleanPreference("update")
			// && (sql.toLowerCase().startsWith("insert ")
			// || sql.toLowerCase().startsWith("update ") || sql
			// .toLowerCase().startsWith("delete "))) {
			// query = sql;
			// }
			for (Database database : list) {
				if (database.isSelected()) {
					if (isCancelled()) {
						break;
					}
					dbLabel.setText(database.getName());
					runner = new QueryRunner(database);
					try {
						// if (DBAnalyzer.getBooleanPreference("update")
						// && (sql.toLowerCase().startsWith("insert ")
						// || sql.toLowerCase().startsWith(
						// "update ") || sql.toLowerCase()
						// .startsWith("delete "))) {
						// int res = runner.executeUpdate(query);
						// String msg = database.getName() + ": " + res;
						// if (sql.toLowerCase().startsWith("insert "))
						// msg += " записей добавлено.";
						// if (sql.toLowerCase().startsWith("update "))
						// msg += " записей обновлено.";
						// if (sql.toLowerCase().startsWith("delete "))
						// msg += " записей удалено.";
						// frame.getStPanel().appendText(msg);
						// continue;
						// }
						result = (ArrayList<ArrayList>) runner
								.query(query, alh);
						for (ArrayList a : result) {
							a.add(0, database.getName());
						}
						all.addAll(result);
						columnsName = runner.getColumnsName();
						runner.invalidateConnection();
                    } catch (SQLException ex) {
//						frame.getStPanel().appendText(
//								new SQLException(database.getName(), ex));
						Logger.getLogger(MainFrame.class.getName()).log(
								Level.SEVERE, null, ex);
//                        for(Throwable e : ex ) {
                            frame.getStPanel().appendText("Database: "+ database.getName() +"\n", ex.getMessage());
//                        }
                    }
                }
            }
            createFieldsName();
            // split = new String[] { "1", "2" };
            // jTable1.setModel(new ArrayListTableModel(split, all));
			return all;
		}

        //TODO: fixSql этот метод нужно переделать
		private String fixSql(String sql) {
			// select pk as ddd, npp as 'dddd' from id where npp=999
			String s = sql.toLowerCase();
			int indexFrom = s.indexOf("from ");
			String[] temp = sql.substring(6, indexFrom).trim().split(",");
			for (int i = 0; i < temp.length; i++) {
				int index = temp[i].toLowerCase().indexOf(" as ");
				if (index > 0)
					temp[i] = temp[i].substring(0, index).trim();
				else
					temp[i] = temp[i].trim();
			}
			StringBuilder result = new StringBuilder("select ");
			for (int i = 0; i < temp.length; i++)
				if (i < temp.length - 1)
					result.append(temp[i]).append(", ");
				else
					result.append(temp[i]).append(" ");
			result.append(sql.substring(indexFrom).replace("\"", "'"));
			return result.toString();
		}

        private void createFieldsName() {
            String[] newColumnsName = new String[columnsName.length + 1];
            String[] fieldsName = parseFields();
            for (String s : columnsName) {
                System.out.println("CC "+ s);
            }
            if (columnsName.length == fieldsName.length) {
                columnsName = fieldsName;
            }

            System.arraycopy(columnsName, 0, newColumnsName, 1,
                    columnsName.length);
            newColumnsName[0] = "Отдел";
            split = newColumnsName;
            for (int i = 0; i < split.length; i++) {
                if (split[i].contains("-"))
                    split[i] = "Вычитание";
                if (split[i].contains("+"))
                    split[i] = "Сложение";
            }
        }

        private String[] parseFields() {
            int startComment = this.sql.indexOf("/*");
            int endComment = this.sql.lastIndexOf("*/");
            if (startComment >= 0 && endComment >0) {
                String comment = this.sql.substring(startComment, endComment);
                int index = comment.indexOf("@fieldName");
                String fields = "";
                if(index >0)
                    fields = comment.substring(index).replace("@fieldName","").trim();
                if (fields.contains(";")) {
                    return fields.substring(0, fields.indexOf("\n")).trim().split(";");
                } else {
                    return new String[]{fields};
                }
            }
            return new String[] {};
        }

        //TODO: createFieldsName этот метод нужно переделать
		private void createFieldsNameOld() {
			if (sql.indexOf("* ") > 0) {
				String[] newColumnsName = new String[columnsName.length + 1];
				System.arraycopy(columnsName, 0, newColumnsName, 1,
						columnsName.length);
				newColumnsName[0] = "Отдел";
				split = newColumnsName;
				return;
			}

			// select pk as 'PK', npp as Number from id where npp=999
			String s = this.sql.replace("SELECT", "select")
					.replace(" FROM ", " from ").replace(" AS ", " as ")
					.replace("select", "").replace("'", "").replace("\"", " ");
			int indexOpenBracket = s.indexOf("(");
			int indexCloseBracket = s.indexOf(")");
			int indexFrom = s.indexOf("from ");
			if (indexFrom > indexOpenBracket && indexFrom > indexCloseBracket) {
				indexFrom = s.lastIndexOf("from ");
			}
			String[] temp = s.substring(0, indexFrom).split(",");
			for (int i = 0; i < temp.length; i++) {
				if (temp[i].contains("-"))
					temp[i] = "Вычитание";
				if (temp[i].contains("+"))
					temp[i] = "Сложение";
			}
			split = temp;
			for (int i = 0; i < temp.length; i++) {
				int index = temp[i].indexOf(" as ");
				if (index > 0)
					split[i] = temp[i].substring(index + 4).trim();
				else
					split[i] = temp[i].trim();
			}

			String[] newColumnsName = new String[split.length + 1];
			System.arraycopy(split, 0, newColumnsName, 1, split.length);
			newColumnsName[0] = "Отдел";
			split = newColumnsName;
		}

		@Override
		public void done() {
			ProgressDialog.this.setVisible(false);
			if (isCancelled()) {
				return;
			}
			ArrayList<ArrayList> all = null;
			try {
				all = get();
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (ExecutionException e) {
				e.printStackTrace();
			}
			ProgressDialog.this.frame.setResult(split, all);
			frame.displayMessage(DBAnalyzer.getProperty("msg.query-complite"));
		}
	}
}

// TODO: парсер хреноват. Нужно доделать
// TODO: вместо парсера сделать строку с названиями столбцов
