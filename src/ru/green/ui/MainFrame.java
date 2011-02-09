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

package ru.green.ui;

import info.clearthought.layout.TableLayout;
import org.fife.ui.autocomplete.AutoCompletion;
import org.fife.ui.autocomplete.CompletionProvider;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;
import org.noos.xing.mydoggy.Content;
import org.noos.xing.mydoggy.ContentManager;
import org.noos.xing.mydoggy.ToolWindow;
import org.noos.xing.mydoggy.ToolWindowAnchor;
import org.noos.xing.mydoggy.plaf.MyDoggyToolWindowManager;

import ru.green.DBAnalyzer;
import ru.green.dbutils.DBUtils;
import ru.green.model.Database;
import ru.green.report.Report;
import ru.green.ui.table.ArrayListTableModel;
import ru.green.ui.table.StripedRenderer;
import ru.green.utils.IOUtils;
import ru.green.utils.ManifestReader;
import ru.green.utils.Messager;
import ru.green.utils.Utils;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.*;
import java.awt.print.PrinterException;
import java.io.*;
import java.math.BigDecimal;
import java.util.*;
import java.util.List;

public class MainFrame extends JFrame {
	public static final String TITLE = "DBAnalyzer "
			+ ManifestReader.getImplementationVersion() + " ("
			+ ManifestReader.getBuiltDate() + ")";

	private static final Color TABLE_GRID_COLOR = new Color(0xd9d9d9);
	private ProgressDialog progressDialog;
	private JTable resultTable;
	private JPopupMenu menu;
	private RSyntaxTextArea textArea;
	private String[] columnNames;
	private ArrayList<ArrayList> rows;
	private Report reporter = new Report();
	private MyDoggyToolWindowManager toolWindowManager;
	private JPanel panel;
	private SimpleBookmarkSaveDialog sbsPanel;
	private BookmarkPanel bookmarkPanel;
	private DatabasesPanel dbPanel;
	private String openedFile = "";
	private StackTracePanel stPanel;
	private JTabbedPane tabs;
	private JTable paramTable;
	private JButton saveButton;
	private JButton historyButton;
	private boolean neededSave;
	private JMenuItem saveMenuItem;
	private AboutDialog aboutDialog;
	private ConstructorDialog constructor;
	private SettingsDialog settingsDialog;
    private JFileChooser fc;

	Action executeQueryAction = new ExecuteQueryAction();
	Action saveAction = new SaveAction();
	Action newAction = new NewAction();
	Action openAction = new OpenAction();
	Action reportAction = new ReportAction();
	Action printAction = new PrintAction();
	Action saveBookmarkAction = new SaveBookmarkAction();
	Action fieldsAction = new FieldsAction();
	Action constructorAction = new ConstructorAction();
	Action settingsAction = new SettingsAction();
	Action showHistoryAction = new ShowHistoryAction();

	private Action actionHandler = new ActionHandler();

	private JMenuItem helpMenuItem;

	private JMenuItem exitMenuItem;

	private Content content;

	private JSplitPane splitPane;

	private JScrollPane rtScrollPane;

	private JMenuItem editorView;

	private Action showEditorAction = new ShowEditorAction();

	private JScrollPane taScrollPane;

	private JMenuItem printMenuItem;
    private HistoryDialog historyDialog;

	private SystemTray tray;

	private TrayIcon trayIcon;
    private String tempdir;
    private String lst;
    private String script;

    public void setLst(String lst) {
        this.lst = lst;
        setTitle();
    }

    public void setScript(String script) {
        this.script=script;
        setTitle();
    }

    private void setTitle() {
        if (script != null){
            if (script.lastIndexOf(File.separator) > 0)  {
                script = "..." + script.substring(script.lastIndexOf(File.separator));
            }
            this.setTitle(lst + " - [" + script + "] - " + TITLE);
        } else
            this.setTitle(lst + " - " + TITLE);
    }

    public StackTracePanel getStPanel() {
		return stPanel;
	}


    public MainFrame() {
        lst=Utils.getLst();
        setTitle(lst + " - " + TITLE);
		setIconImage(Toolkit.getDefaultToolkit().getImage(
				getClass().getResource(DBAnalyzer.getProperty("app.icon"))));
		setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

		progressDialog = new ProgressDialog(MainFrame.this, true);
        historyDialog = new HistoryDialog(MainFrame.this);
		sbsPanel = new SimpleBookmarkSaveDialog(MainFrame.this, true);
		stPanel = new StackTracePanel();
		bookmarkPanel = new BookmarkPanel(this);
		dbPanel = new DatabasesPanel(MainFrame.this);
		aboutDialog = new AboutDialog(this);
		menu = new JPopupMenu();
		constructor = new ConstructorDialog(this);
		settingsDialog = new SettingsDialog(this);
        fc = new JFileChooser();
        fc.addChoosableFileFilter(new FileNameExtensionFilter("Текстовый файл (*.txt)", "txt"));
        fc.addChoosableFileFilter(new FileNameExtensionFilter("SQL файл (*.sql)", "sql"));

        tempdir = System.getProperty("java.io.tmpdir");

        if ( !(tempdir.endsWith("/") || tempdir.endsWith("\\")) )
            tempdir = tempdir + File.separator; //System.getProperty("file.separator");

        JToolBar toolbar = new JToolBar();
		toolbar.setFloatable(false);
		toolbar.setFocusable(false);
		toolbar.setRollover(true);

		JButton runButton = new JButton(
				DBAnalyzer.getProperty("run-button.toolbar-label"));
		runButton.setToolTipText(DBAnalyzer.getProperty("run-button.tooltip"));
		runButton.setIcon(Utils.getIcon(DBAnalyzer
				.getProperty("run-button.icon")));

		runButton.setFocusable(false);

		runButton.addActionListener(executeQueryAction);

		JButton reportButton = new JButton(
				DBAnalyzer.getProperty("report-query.toolbar-label"));
		reportButton.setToolTipText(DBAnalyzer
				.getProperty("report-query.tooltip"));
		reportButton.setIcon(Utils.getIcon(DBAnalyzer
				.getProperty("report-query.icon")));
		reportButton.setFocusable(false);
		reportButton.addActionListener(reportAction);
		// new ActionListener() {
		// @Override
		// public void actionPerformed(ActionEvent evt) {
		// report();
		// }
		// });

		JButton bookmarkButton = new JButton(
				DBAnalyzer.getProperty("bookmark.toolbar-label"));
		bookmarkButton.setToolTipText(DBAnalyzer
				.getProperty("bookmark.tooltip"));
		bookmarkButton.setIcon(Utils.getIcon(DBAnalyzer
				.getProperty("bookmark.icon")));
		bookmarkButton.setFocusable(false);
		bookmarkButton.addActionListener(saveBookmarkAction);

		saveButton = new JButton(
				DBAnalyzer.getProperty("save-query.toolbar-label"));
		saveButton.setToolTipText(DBAnalyzer.getProperty("save-query.tooltip"));
		saveButton.setIcon(Utils.getIcon(DBAnalyzer
				.getProperty("save-query.icon")));
		saveButton.setFocusable(false);
		saveButton.addActionListener(saveAction);
		saveButton.setEnabled(neededSave);

		JButton newButton = new JButton(
				DBAnalyzer.getProperty("new-query.toolbar.label"));
		newButton.setToolTipText(DBAnalyzer.getProperty("new-query.tooltip"));
		newButton.setIcon(Utils.getIcon(DBAnalyzer
				.getProperty("new-query.icon")));
		newButton.setFocusable(false);
		newButton.addActionListener(newAction);

        historyButton = new JButton(
                DBAnalyzer.getProperty("history.toolbar-label"));
        historyButton.setToolTipText(DBAnalyzer.getProperty("history.tooltip"));
		historyButton.setIcon(Utils.getIcon(DBAnalyzer
                .getProperty("history.icon")));
		historyButton.setFocusable(false);
		historyButton.addActionListener(showHistoryAction);


		toolbar.add(newButton);
		toolbar.add(runButton);
		toolbar.add(reportButton);
		toolbar.add(saveButton);
		toolbar.add(bookmarkButton);
		toolbar.add(historyButton);
		getContentPane().add(toolbar, BorderLayout.NORTH);

		toolbar.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
				.put(KeyStroke.getKeyStroke(KeyEvent.VK_E,
						InputEvent.CTRL_DOWN_MASK), "executeQuery");
		toolbar.getActionMap().put("executeQuery", executeQueryAction);
		toolbar.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
				.put(KeyStroke.getKeyStroke(KeyEvent.VK_B,
						InputEvent.CTRL_DOWN_MASK), "saveBookmark");

		toolbar.getActionMap().put("saveBookmark", saveBookmarkAction);
		toolbar.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
				.put(KeyStroke.getKeyStroke(KeyEvent.VK_S,
						InputEvent.CTRL_DOWN_MASK), "saveAction");
		toolbar.getActionMap().put("saveAction", saveAction);

		toolbar.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
				.put(KeyStroke.getKeyStroke(KeyEvent.VK_R,
						InputEvent.CTRL_DOWN_MASK), "report");
		toolbar.getActionMap().put("report", reportAction);

		toolbar.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
				.put(KeyStroke.getKeyStroke(KeyEvent.VK_N,
						InputEvent.CTRL_DOWN_MASK), "new");
		toolbar.getActionMap().put("new", newAction);

		panel = new JPanel(new TableLayout(new double[][] { { 0, -1, 0 },
				{ 0, -1, 0 } }));
		getContentPane().add(panel);
		initToolWindowManager();
		// ToolWindow debugTool = toolWindowManager.getToolWindow("Закладки");
		initSysTray();
		setTrayVisible(DBAnalyzer.getBooleanPreference("view.tray"));
		initPopUpMenu();

		JMenuBar menuBar = createMenus();
		setJMenuBar(menuBar);

		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		setSize(screenSize.width - 100, screenSize.height - 100);
		setLocationRelativeTo(null);
	}

	private void initSysTray() {
		if (SystemTray.isSupported()) {
			tray = SystemTray.getSystemTray();
			Image image = Toolkit.getDefaultToolkit()
					.getImage(
							getClass().getResource(
									DBAnalyzer.getProperty("tray.icon")));

			ActionListener exitListener = new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					System.exit(0);
				}
			};
			PopupMenu popup = new PopupMenu();
			MenuItem settingsItem = new MenuItem(
					DBAnalyzer.getProperty("tray.popup.settings"));
			settingsItem.addActionListener(settingsAction);
			popup.add(settingsItem);

			MenuItem exitItem = new MenuItem(
					DBAnalyzer.getProperty("tray.popup.exit"));
			exitItem.addActionListener(exitListener);
			popup.add(exitItem);

			trayIcon = new TrayIcon(image, TITLE, popup);
			trayIcon.setImageAutoSize(true);
			// ActionListener actionListener = new ActionListener() {
			// public void actionPerformed(ActionEvent e) {
			// trayIcon.displayMessage("Action Event",
			// "An Action Event Has Been Peformed!",
			// TrayIcon.MessageType.INFO);
			// if(MainFrame.this.isVisible()) {
			// MainFrame.this.setVisible(false);
			// } else {
			// MainFrame.this.setVisible(true);
			// MainFrame.this.repaint();
			// }
			// }
			// };
			// trayIcon.addActionListener(actionListener);
			// try {
			// tray.add(trayIcon);
			// } catch (AWTException e) {
			// e.printStackTrace();
			// }

		}
	}

	public void displayMessage(String msg) {
		trayIcon.displayMessage(DBAnalyzer.getProperty("app.label"), msg,
				TrayIcon.MessageType.INFO);
	}

	public void setTrayVisible(boolean b) {
		if (b) {
			if (tray.getTrayIcons().length == 0) {
				try {
					tray.add(trayIcon);
				} catch (AWTException e) {
					e.printStackTrace();
				}
			}
		} else if (tray.getTrayIcons().length > 0) {
			tray.remove(trayIcon);
		}
	}

	private void executeQuery() {
		if (textArea.getText().length() == 0) {
			JOptionPane.showMessageDialog(MainFrame.this,
					DBAnalyzer.getProperty("msg.empty-query"),
					DBAnalyzer.getProperty("app.label"),
					JOptionPane.INFORMATION_MESSAGE);
			return;
		}
//		String sql = trimCommentsFromLeft(textArea.getText().trim());
		String sql = textArea.getText().trim();
		sql = replaceParams(sql);
		DefaultTableModel model = (DefaultTableModel) paramTable.getModel();
		if (model.getColumnCount() > 0)
			printMenuItem.setEnabled(true);
		if (!sql.isEmpty()) {
			// TODO: Нужно переделать считыватель баз
			ArrayList<Database> list = Utils.getDBList();
			for (int i = 0; i < list.size(); i++) {
				JCheckBox[] checkBoxes = dbPanel.getCheckBoxes();
				list.get(i).setSelected(checkBoxes[i].isSelected());
			}
			progressDialog.run(sql);
		}
	}

	private void save() {
		if (!openedFile.isEmpty()) {
			IOUtils.writeFile(textArea.getText(), openedFile);
			this.neededSave = false;
			this.saveButton.setEnabled(neededSave());
			this.saveMenuItem.setEnabled(neededSave());
		}
	}

	// TODO: искать по \n
	private String trimCommentsFromLeft(String sql) {
		if (sql.indexOf("SELECT ") > 0) {
			return sql.substring(sql.indexOf("SELECT "));
		} else if (sql.indexOf("select ") > 0) {
			return sql.substring(sql.indexOf("select "));
		}
		return sql;
	}

	// private String trimCommentsFromLeft(String sql) {
	// sql = sql.toLowerCase();
	// int select = gtZero(sql.indexOf("select "));
	// int insert = gtZero(sql.indexOf("insert "));
	// int update = gtZero(sql.indexOf("update "));
	// int delete = gtZero(sql.indexOf("delete "));
	//
	// System.out.println(sql.indexOf("select ") + "\t" + insert + "\t"
	// + update + "\t" + sql.indexOf("delete "));
	// if (insert > 0 && insert < select) {
	// System.out.println("1" + sql.substring(insert));
	// return sql.substring(insert);
	// } else if (update > 0 && update < select) {
	// System.out.println("2" + sql.substring(update));
	// return sql.substring(update);
	// }
	// if (delete > 0 && update < select) {
	// System.out.println("3" + sql.substring(delete));
	// return sql.substring(delete);
	// } else if (select > 0) {
	// System.out.println("4" + sql.substring(select));
	// return sql.substring(select);
	// }
	// return sql;
	// }

	/**
	 * Проверяет параметр больше нуля или нет.
	 *
	 * @param num
	 *            параметр
	 * @return возврящает параметр, если больше 0, иначе возвращает 0
	 */
	private int gtZero(int num) {
		if (num > 0)
			return num;
		else
			return 0;
	}

	private String replaceParams(String sql) {
		DefaultTableModel model = (DefaultTableModel) paramTable.getModel();
		int rows = model.getRowCount();
		if (sql.indexOf("$") > 0)
			for (int i = 0; i < rows; i++) {
				Object param = model.getValueAt(i, 0);
				Object value = model.getValueAt(i, 1);
				// System.out.println("$" + (i + 1)+"\t"+ value.toString());
				sql = sql.replace(param.toString(), value.toString());
			}
		// System.out.println("replace params: " + sql);
		return sql;
	}

	protected void saveBookmark() {
		String sql = textArea.getText().trim();
		// if (!sql.isEmpty()) {
		sbsPanel.show(sql);
		bookmarkPanel.reloadTree();
		// }
		openedFile = sbsPanel.getDestinationFile();
		this.neededSave = false;
		this.saveButton.setEnabled(neededSave());
		this.saveMenuItem.setEnabled(neededSave());
	}

	public void setResult(String[] columnNames, ArrayList<ArrayList> rows) {
		this.columnNames = columnNames;
		this.rows = rows;

		if (columnNames != null && rows != null) {
			ArrayListTableModel model = new ArrayListTableModel(columnNames,
					rows);
			resultTable.setModel(model);
			resultTable.setRowSorter(new TableRowSorter<TableModel>(model));
		}
		if (stPanel.getTextCount() != 0) {
			// tabs.setSelectedIndex(1);
			ToolWindow logTool = toolWindowManager.getToolWindow(DBAnalyzer
					.getProperty("tool-window.log.label"));
			logTool.setActive(true);
		}
	}

	private void report() {
		Runnable reportDialog = new Runnable() {
			@Override
			public void run() {
				Random rnd = new Random();
				if (columnNames == null) {
					JOptionPane.showMessageDialog(MainFrame.this,
							DBAnalyzer.getProperty("msg.no-data"),
							DBAnalyzer.getProperty("app.label"),
							JOptionPane.INFORMATION_MESSAGE);
					return;
				}
				try {
					reporter.readTemplate();
					prepareReport();
//					new File("etc/temp/").mkdirs();
					String file = tempdir + UUID.randomUUID() + ".xls";
					reporter.write(file);
					Utils.openFile(file);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

		};
		SwingUtilities.invokeLater(reportDialog);
		// if (stPanel.getTextCount() > 0) {
		// tabs.setSelectedComponent(sbsPanel);
		// }
	}

	private void prepareReport() {
		String sql = textArea.getText();
		String caption = parseCaption(sql);
		reporter.addMergedRegion(columnNames.length);
		if (!caption.equals("")) {
			reporter.setCell(0, 0, caption);
		} else if (!neededSave()) {
			String title = null;
			if (openedFile.isEmpty() || openedFile == null)
				title = sql;
			else
				title = openedFile.substring(
						openedFile.lastIndexOf(File.separator) + 1,
						openedFile.lastIndexOf("."));
			reporter.setCell(0, 0, title);
		}

		for (int i = 0; i < columnNames.length; i++)
			reporter.setCaptionCell(1, i, columnNames[i]);
		for (int i = 0; i < rows.size(); i++)
			for (int j = 0; j < rows.get(i).size(); j++) {
				String val = String.valueOf(rows.get(i).get(j));
				if (Utils.isNumeric(val)) {
					reporter.setNumericCell(i + 2, j, Double.parseDouble(val));
				} else {
					reporter.setCell(i + 2, j, val);
				}
			}
	}

	private String parseCaption(String sql) {
//		if ((sql.indexOf("--!") >= 0) && (sql.indexOf("!--") >= 0)) {
//			return sql.substring(sql.indexOf("--!") + 3, sql.indexOf("!--"));
//		} else {
//			return "";
//		}

        if ((sql.indexOf("/*") >= 0) && (sql.indexOf("*/") > 0)) {
             int index = sql.indexOf("@title");
                String title = "";
                if(index >0)
                    title = sql.substring(index,sql.indexOf("*/")).replace("@title","");
                if (title.indexOf("\n") > 0)
                    title = title.substring(0, title.indexOf("\n")).trim();
            return title.trim();
        } else {
			return "";
		}
	}

	private void initPopUpMenu() {
		JMenuItem copyCell = new JMenuItem(
				DBAnalyzer.getProperty("result-tablel.copy"));
		copyCell.addActionListener(new CopyCellValueAction());

		JMenuItem copyRow = new JMenuItem(
				DBAnalyzer.getProperty("result-tablel.copy-row"));
		copyRow.addActionListener(new CopyRowsValueAction());

		JMenuItem countRows = new JMenuItem(
				DBAnalyzer.getProperty("result-tablel.rows"));
		countRows.addActionListener(new CountRowsAction(this));

		JMenuItem countSelectedRows = new JMenuItem(
				DBAnalyzer.getProperty("result-tablel.selected-rows"));
		countSelectedRows.addActionListener(new CountSelectedRowsAction());

		JMenuItem rowSumm = new JMenuItem(
				DBAnalyzer.getProperty("result-tablel.sum"));
		rowSumm.addActionListener(new RowSummAction());
		rowSumm.setIcon(Utils.getIcon(DBAnalyzer
				.getProperty("result-tablel.sum.icon")));

		JMenuItem print = new JMenuItem(
				DBAnalyzer.getProperty("result-tablel.print"));
		print.addActionListener(printAction);
		KeyStroke ctrlP = KeyStroke.getKeyStroke(DBAnalyzer
				.getProperty("result-tablel.print.keystroke"));
		print.setAccelerator(ctrlP);
		print.setIcon(Utils.getIcon(DBAnalyzer
				.getProperty("result-tablel.print.icon")));

		JMenuItem report = new JMenuItem(
				DBAnalyzer.getProperty("result-tablel.report"));
		KeyStroke ctrlR = KeyStroke.getKeyStroke(DBAnalyzer
				.getProperty("result-tablel.report.keystroke"));
		report.setAccelerator(ctrlR);

		report.setIcon(Utils.getIcon(DBAnalyzer
				.getProperty("result-tablel.report.icon")));
		report.addActionListener(new ReportAction());

		resultTable.addMouseListener(new ShowPopupMenu());

		menu.add(copyCell);
		menu.add(copyRow);
		menu.add(new JSeparator());
		menu.add(countRows);
		menu.add(countSelectedRows);
		menu.add(rowSumm);
		menu.add(new JSeparator());
		menu.add(print);
		menu.add(report);
	}

	protected void initToolWindowManager() {
		MyDoggyToolWindowManager myDoggyToolWindowManager = new MyDoggyToolWindowManager(
				this);
		this.toolWindowManager = myDoggyToolWindowManager;

		toolWindowManager.registerToolWindow(DBAnalyzer
				.getProperty("tool-window.bookmark.label"), // Id
				"", // Title
				(Utils.getIcon(DBAnalyzer
						.getProperty("tool-window.bookmark.icon"))), // Icon
				bookmarkPanel, // Component
				ToolWindowAnchor.LEFT); // Anchor

		toolWindowManager.registerToolWindow(DBAnalyzer
				.getProperty("tool-window.database.label"), // Id
				"", // Title
				Utils.getIcon(DBAnalyzer
						.getProperty("tool-window.database.icon")), // Icon
				dbPanel, // Component
				ToolWindowAnchor.LEFT); // Anchor

		toolWindowManager
				.registerToolWindow(DBAnalyzer
						.getProperty("tool-window.param.label"), // Id
						"", // Title
						Utils.getIcon(DBAnalyzer
								.getProperty("tool-window.param.icon")), // Icon
						createParamsDock(), // Component
						ToolWindowAnchor.RIGHT); // Anchor
		// Register a Tool.

		toolWindowManager.registerToolWindow(
				DBAnalyzer.getProperty("tool-window.log.label"), // Id
				"", // Title
				Utils.getIcon(DBAnalyzer.getProperty("tool-window.log.icon")), // Icon
				stPanel, // Component
				ToolWindowAnchor.BOTTOM); // Anchor
		// Register a Tool.

		// Made all tools available
		for (ToolWindow window : toolWindowManager.getToolWindows())
			window.setAvailable(true);

		// toolWindowManager.setToolTipText("Закладки");

		initContentManager();
		panel.add(myDoggyToolWindowManager, "1,1,");
	}

	protected void initContentManager() {
		textArea = new RSyntaxTextArea();
		textArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_SQL);
		textArea.setText("");
//        Font font = null;
//        try {
//            font = Font.createFont(Font.TRUETYPE_FONT, new FileInputStream("etc/consola.ttf"));
//
//            textArea.setFont(new Font(font.getFontName(), Font.PLAIN, 14));
//        } catch (FontFormatException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        textArea.setCurrentLineHighlightColor(new Color(232, 242, 254));
		textArea.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				setParams(textArea.getText());
			}
		});
		textArea.getDocument().addDocumentListener(new DocumentListener() {

			@Override
			public void removeUpdate(DocumentEvent e) {
				update();
			}

			@Override
			public void insertUpdate(DocumentEvent e) {
				update();
			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				update();

			}

			private void update() {
				MainFrame.this.neededSave = true;
				MainFrame.this.saveButton.setEnabled(neededSave());
				MainFrame.this.saveMenuItem.setEnabled(neededSave());
			}
		});
		taScrollPane = new JScrollPane(textArea);

		resultTable = new JTable();
		resultTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		if (!DBAnalyzer.getBooleanPreference("view.theme")) {
			resultTable.setGridColor(TABLE_GRID_COLOR);
			TableCellRenderer renderer = new StripedRenderer();
			resultTable.setDefaultRenderer(Object.class, renderer);
			resultTable.setFillsViewportHeight(true);
		}
		resultTable.addMouseListener(new ShowPopupMenu());
		rtScrollPane = new JScrollPane(resultTable);

		splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);

		// stPanel = new StackTracePanel();
		// tabs = new JTabbedPane(JTabbedPane.BOTTOM);
		// tabs.addTab("Результат", rtScrollPane);
		// tabs.addTab("Лог", stPanel);

		splitPane.setTopComponent(taScrollPane);
		splitPane.setBottomComponent(rtScrollPane);
		// splitPane.setBottomComponent(tabs);

		splitPane.setDividerLocation(140);
		// add(splitPane, BorderLayout.CENTER);

		CompletionProvider provider = DBUtils.createCompletionProvider();
		AutoCompletion ac = new AutoCompletion(provider);
		ac.install(textArea);

		ContentManager contentManager = toolWindowManager.getContentManager();
		content = contentManager.addContent("Work panel", "Work panel", null, // An
																				// icon
				splitPane);
		if (!DBAnalyzer.getBooleanPreference("view.editor")) {
			content.setComponent(rtScrollPane);
		}
	}

	protected boolean neededSave() {
		return (neededSave && ((!openedFile.isEmpty()) ? true : false));
	}

    public void setText(String text) {
        textArea.setText(text);
        textArea.setCaretPosition(0);
    }

	public void setText(File file) {

		try {
			textArea.read(new BufferedReader(new InputStreamReader(
					new FileInputStream(file), "windows-1251")), null);
			textArea.setCaretPosition(0);
		} catch (IOException e) {
			return;
		}
		setParams(textArea.getText().trim());
		this.openedFile = file.getAbsolutePath();
        setScript(openedFile);
		this.neededSave = false;
		MainFrame.this.saveButton.setEnabled(neededSave());
		MainFrame.this.saveMenuItem.setEnabled(neededSave());

		ToolWindow paramTool = toolWindowManager.getToolWindow(DBAnalyzer
				.getProperty("tool-window.param.label"));
		if (!paramTable.getModel().getValueAt(0, 0).equals("")) {
			paramTool.setActive(true);
		} else {
			paramTool.setVisible(false);
		}
	}

	private void setParams(String sql) {
		String[] strings = Utils.match(sql);
		List<String> list = Arrays.asList(strings);
		Set<String> set = new HashSet<String>(list);
		String[] result = new String[set.size()];
		set.toArray(result);

		String[][] data = new String[result.length][2];
		for (int i = 0; i < result.length; i++) {
			data[i][0] = result[i];
			data[i][1] = "";
		}
		String[] columns = { "Параметр", "Значение" };
		DefaultTableModel model = new DefaultTableModel(data, columns);
		paramTable.setModel(model);
	}

	private JComponent createParamsDock() {
		paramTable = new JTable();
		setParams("");
		return new JScrollPane(paramTable);

	}

	public static void main(String args[]) {
		java.awt.EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					UIManager.setLookAndFeel(UIManager
							.getSystemLookAndFeelClassName());
				} catch (Exception e) {
					e.printStackTrace();
				}
				new MainFrame().setVisible(true);
			}
		});
	}

	private JMenuBar createMenus() {
		JMenuItem mi;
		JMenuBar menuBar = new JMenuBar();

		JMenu fileMenu = menuBar.add(new JMenu(DBAnalyzer
				.getProperty("menu.file")));
		createMenuItem(fileMenu, "new-query.label", "new-query.mnemonic",
				"new-query.keystroke", "new-query.description", newAction,
				"new-query.icon");

        createMenuItem(fileMenu, "open-query.label", "open-query.mnemonic",
				"open-query.keystroke", "open-query.description", openAction,
				"open-query.icon");
		fileMenu.addSeparator();

		saveMenuItem = createMenuItem(fileMenu, "save-query.label",
				"save-query.mnemonic", "save-query.keystroke",
				"save-query.description", saveAction, "save-query.icon");
		saveMenuItem.setEnabled(neededSave);
		fileMenu.addSeparator();

		createMenuItem(fileMenu, "report-query.label", "report-query.mnemonic",
				"report-query.keystroke", "report-query.description",
				reportAction, "report-query.icon");
		printMenuItem = createMenuItem(fileMenu, "print-query.label",
				"print-query.mnemonic", "print-query.keystroke",
				"print-query.description", printAction, "print-query.icon");
		printMenuItem.setEnabled(resultTable.getModel().getColumnCount() > 0);
		fileMenu.addSeparator();

		exitMenuItem = createMenuItem(fileMenu, "exit.label", "exit.mnemonic",
				"exit.keystroke", "exit.description", actionHandler,
				"exit.icon");

		JMenu viewMenu = menuBar.add(new JMenu(DBAnalyzer
				.getProperty("menu.view")));
		editorView = createCheckBoxMenuItem(viewMenu, "editor-view.label",
				"editor-view.mnemonic", "editor-view.keystroke",
				"editor-view.desciption", showEditorAction);
		editorView.setSelected(DBAnalyzer.getBooleanPreference("view.editor"));
		// ButtonGroup viewButtonGroup = new ButtonGroup();
		// mi = createButtonGroupMenuItem(viewMenu, "editor-view.label",
		// "editor-view.mnemonic", "editor-view.keystroke",
		// "editor-view.desciption", null, viewButtonGroup);
		// createButtonGroupMenuItem(viewMenu, "designer-view.label",
		// "designer-view.mnemonic", "designer-view.keystroke",
		// "designer-view.desciption", null, viewButtonGroup);
		// mi.setSelected(neededSave);

		JMenu serviceMenu = menuBar.add(new JMenu(DBAnalyzer
				.getProperty("menu.tools")));
		createMenuItem(serviceMenu, "bookmark.label", "bookmark.mnemonic",
				"bookmark.keystroke", "bookmark.description",
				saveBookmarkAction, "bookmark.icon");
		serviceMenu.addSeparator();
		createMenuItem(serviceMenu, "query-designer.label",
				"query-designer.mnemonic", "query-designer.keystroke",
				"query-designer.description", constructorAction,
				"query-designer.icon");
		createMenuItem(serviceMenu, "filds-designer.label",
				"filds-designer.mnemonic", "filds-designer.keystroke",
				"filds-designer.description", fieldsAction,
				"filds-designer.icon");
		serviceMenu.addSeparator();
		createMenuItem(serviceMenu, "preferences.label",
				"preferences.mnemonic", "preferences.keystroke",
				"preferences.description", settingsAction, "preferences.icon");

		JMenu helpMenu = menuBar.add(new JMenu(DBAnalyzer
				.getProperty("menu.help")));
		helpMenuItem = createMenuItem(helpMenu, "about.label",
				"about.mnemonic", "about.keystroke", "about.description",
				actionHandler, "about.icon");
		return menuBar;
	}

	private JMenuItem createMenuItem(JMenu menu, String label, String mnemonic,
			String keyStroke, String description, Action action, String icon) {
		JMenuItem mi = menu.add(new JMenuItem(DBAnalyzer.getProperty(label)));
		if (DBAnalyzer.getProperty(mnemonic) != null
				&& !DBAnalyzer.getProperty(mnemonic).isEmpty()) {
			mi.setMnemonic(getMnemonic(DBAnalyzer.getProperty(mnemonic)));
		}
		mi.setAccelerator(KeyStroke.getKeyStroke(DBAnalyzer
				.getProperty((keyStroke))));
		mi.getAccessibleContext().setAccessibleDescription(
				DBAnalyzer.getProperty((description)));
		mi.addActionListener(action);
		// if (action == null) {
		// mi.setEnabled(false);
		// }
		mi.setIcon(Utils.getIcon(DBAnalyzer.getProperty(icon)));
		return mi;

	}

	private JMenuItem createButtonGroupMenuItem(JMenu menu, String label,
			String mnemonic, String keyStroke, String description,
			Action action, ButtonGroup buttonGroup) {
		JRadioButtonMenuItem mi = (JRadioButtonMenuItem) menu
				.add(new JRadioButtonMenuItem(DBAnalyzer.getProperty(label)));
		buttonGroup.add(mi);
		if (DBAnalyzer.getProperty(mnemonic) != null
				&& !DBAnalyzer.getProperty(mnemonic).isEmpty())
			mi.setMnemonic(getMnemonic(DBAnalyzer.getProperty(mnemonic)));
		mi.setAccelerator(KeyStroke.getKeyStroke(DBAnalyzer
				.getProperty(keyStroke)));
		mi.getAccessibleContext().setAccessibleDescription(
				DBAnalyzer.getProperty(description));
		mi.addActionListener(action);
		return mi;
	}

	private JMenuItem createCheckBoxMenuItem(JMenu menu, String label,
			String mnemonic, String keyStroke, String description, Action action) {
		JCheckBoxMenuItem mi = (JCheckBoxMenuItem) menu
				.add(new JCheckBoxMenuItem(DBAnalyzer.getProperty(label)));
		if (DBAnalyzer.getProperty(mnemonic) != null
				&& !DBAnalyzer.getProperty(mnemonic).isEmpty())
			mi.setMnemonic(getMnemonic(DBAnalyzer.getProperty(mnemonic)));
		mi.getAccessibleContext().setAccessibleDescription(
				DBAnalyzer.getProperty((description)));
		mi.addActionListener(action);
		return mi;
	}

	private char getMnemonic(String mnemonic) {
		return mnemonic.charAt(0);
	}



    private void saveHistory() {
        String sql = textArea.getText().trim();
        int i = sql.toLowerCase().indexOf("select");
        if (i > 0) {
            sql = sql.substring(i);
        }
        IOUtils.writeFile(sql.replace("\n", "") + System.getProperty("line.separator"), HistoryDialog.HISTORY_FILE, true);
    }



	// *******************************************************
	// ********************** Actions **********************
	// *******************************************************
	class CopyCellValueAction extends AbstractAction {

		public void actionPerformed(ActionEvent e) {
			int row = resultTable.getSelectedRow();
			int cell = resultTable.getSelectedColumn();
			String text = String.valueOf(resultTable.getValueAt(row, cell));
			Clipboard clipboard = Toolkit.getDefaultToolkit()
					.getSystemClipboard();
			StringSelection selection = new StringSelection(text);
			clipboard.setContents(selection, null);
		}
	}

	class CopyRowsValueAction extends AbstractAction {

		public void actionPerformed(ActionEvent e) {
			int[] rows = resultTable.getSelectedRows();
			StringBuilder text = new StringBuilder();
			int columnCount = resultTable.getColumnCount();
			for (int row : rows) {
				for (int cell = 0; cell < columnCount; cell++) {
					text.append(String.valueOf(resultTable
							.getValueAt(row, cell)));
					if (cell != columnCount - 1) {
						text.append('\t');
					}
				}
				text.append('\n');
			}
			Clipboard clipboard = Toolkit.getDefaultToolkit()
					.getSystemClipboard();
			StringSelection selection = new StringSelection(text.toString());
			clipboard.setContents(selection, null);
		}
	}

	class CountRowsAction extends AbstractAction {
		protected CountRowsAction(MainFrame frame) {
			super("CountRowsAction");
		}

		public void actionPerformed(ActionEvent e) {
			Messager.resultMessage(DBAnalyzer.getProperty("msg.table.rows")
					+ " " + resultTable.getRowCount());
		}
	}

	class CountSelectedRowsAction extends AbstractAction {

		protected CountSelectedRowsAction() {
			super("CountSelectedRowsAction");
		}

		public void actionPerformed(ActionEvent e) {
			Messager.resultMessage(DBAnalyzer
					.getProperty("msg.table.selected-rows")
					+ " "
					+ resultTable.getSelectedRows().length);
		}
	}

	class PrintAction extends AbstractAction {

		protected PrintAction() {
			super("PrintAction");
		}

		public void actionPerformed(ActionEvent e) {

			try {
				if (resultTable.getModel().getColumnCount() > 0)
					resultTable.print();
			} catch (PrinterException ex) {
				ex.printStackTrace();
			}
		}
	}

	class RowSummAction extends AbstractAction {

		protected RowSummAction() {
			super("RowSummAction");
		}

		public void actionPerformed(ActionEvent e) {
			int selectedColumn = resultTable.getSelectedColumn();
			try {
				double sum = 0;
				for (int i = 0; i < resultTable.getRowCount(); i++)
					sum += Double.parseDouble(String.valueOf(resultTable
							.getValueAt(i, selectedColumn)));
				BigDecimal bd = BigDecimal.valueOf(sum);
				if ((bd.doubleValue() - bd.intValue()) == 0)
					Messager.resultMessage(DBAnalyzer
							.getProperty("msg.table.sum") + " " + bd.intValue());
				else
					Messager.resultMessage(DBAnalyzer
							.getProperty("msg.table.sum")
							+ " "
							+ Utils.formatDecimal(bd));// .setScale(2,
														// BigDecimal.ROUND_HALF_UP).doubleValue()
			} catch (NumberFormatException ex) {
				Messager.resultMessage(DBAnalyzer
						.getProperty("msg.table.format"));
			}
		}
	}

	class ReportAction extends AbstractAction {

		protected ReportAction() {
			super("ReportAction");
		}

		@Override
		public void actionPerformed(ActionEvent evt) {
			report();
		}
	}

	class ShowPopupMenu extends MouseAdapter {

		public void mouseClicked(MouseEvent e) {
			if (SwingUtilities.isRightMouseButton(e)) {
				JTable source = (JTable) e.getSource();
				int row = source.rowAtPoint(e.getPoint());
				int column = source.columnAtPoint(e.getPoint());
				if (source.getSelectedRows().length <= 1) {
					source.changeSelection(row, column, false, false);
				}
				menu.show(source, e.getX(), e.getY());
			}
		}
	}

	class ExecuteQueryAction extends AbstractAction {
		@Override
		public void actionPerformed(ActionEvent evt) {
            saveHistory();
            executeQuery();
        }
	}

	class SaveAction extends AbstractAction {
		@Override
		public void actionPerformed(ActionEvent evt) {
			save();
		}
	}

	class NewAction extends AbstractAction {
		@Override
		public void actionPerformed(ActionEvent evt) {
			textArea.setText("");
			openedFile = "";
			MainFrame.this.neededSave = false;
			MainFrame.this.saveButton.setEnabled(neededSave());
			MainFrame.this.saveMenuItem.setEnabled(neededSave());
            MainFrame.this.setTitle(lst + " - " + TITLE);
            MainFrame.this.script = null;
		}
	}

	class ActionHandler extends AbstractAction {

		@Override
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == helpMenuItem)
				aboutDialog.setVisible(true);
			if (e.getSource() == exitMenuItem)
				System.exit(0);
		}

	}

	class SaveBookmarkAction extends AbstractAction {
		@Override
		public void actionPerformed(ActionEvent evt) {
			saveBookmark();
		}
	}

	class ShowEditorAction extends AbstractAction {
		@Override
		public void actionPerformed(ActionEvent evt) {
			if (!editorView.isSelected()) {
				content.setComponent(rtScrollPane);
			} else {
				content.setComponent(splitPane);
				splitPane.setDividerLocation(100);
				splitPane.setTopComponent(taScrollPane);
				splitPane.setBottomComponent(rtScrollPane);
			}

		}
	}

	class ConstructorAction extends AbstractAction {
		@Override
		public void actionPerformed(ActionEvent evt) {
			constructor.setVisible(true);
			String query = constructor.getQuery();
			if (!query.isEmpty()) {
				textArea.setText(query);
				setParams(textArea.getText().trim());
			}
			ToolWindow paramTool = toolWindowManager.getToolWindow(DBAnalyzer
					.getProperty("tool-window.param.label"));
			if (!paramTable.getModel().getValueAt(0, 0).equals("")) {
				paramTool.setActive(true);
			} else {
				paramTool.setVisible(false);
			}
		}
	}

	class FieldsAction extends AbstractAction {
		@Override
		public void actionPerformed(ActionEvent evt) {
			if (!textArea.getText().isEmpty()) {
				FieldsDialog fieldsDialog = new FieldsDialog(MainFrame.this,
						MainFrame.this.textArea.getText(), true);
				fieldsDialog.setVisible(true);

				String query = fieldsDialog.getQuery();
				if (!query.isEmpty())
					textArea.setText(query);
			} else {
				JOptionPane.showMessageDialog(MainFrame.this,
						DBAnalyzer.getProperty("msg.empty-query"),
						DBAnalyzer.getProperty("app.label"),
						JOptionPane.INFORMATION_MESSAGE);
			}
		}
	}

	class SettingsAction extends AbstractAction {
		@Override
		public void actionPerformed(ActionEvent evt) {
			settingsDialog.setVisible(true);
			bookmarkPanel.reloadTree();
		}
	}

    private class OpenAction extends AbstractAction {

        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            int returnValue = fc.showOpenDialog(MainFrame.this);
            if (returnValue == JFileChooser.APPROVE_OPTION) {
                MainFrame.this.setText(fc.getSelectedFile());
            }
        }
    }


    class ShowHistoryAction extends AbstractAction {
        @Override
        public void actionPerformed(ActionEvent evt) {
            historyDialog.setVisible(true);
        }
    }

}
