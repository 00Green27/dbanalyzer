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

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.EventObject;

import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.event.CellEditorListener;
import javax.swing.event.EventListenerList;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreeCellEditor;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

import ru.green.DBAnalyzer;
import ru.green.utils.Utils;

public class BookmarkPanel extends JPanel {

	private JTree tree;
	private final MainFrame frame;
	private FileTreeModel model;

	public BookmarkPanel(final MainFrame frame) {
		this.frame = frame;
		model = new FileTreeModel(DBAnalyzer.getPreference("misc.bookmark"));
		tree = new JTree(model);
		FileTreeCellRenderer renderer = new FileTreeCellRenderer();
		if (!DBAnalyzer.getBooleanPreference("view.theme")) {
			renderer.setClosedIcon(Utils.getIcon(DBAnalyzer
					.getProperty("bookmark.folder.icon")));
			renderer.setOpenIcon(Utils.getIcon(DBAnalyzer
					.getProperty("bookmark.folder-open.icon")));
		}
		renderer.setLeafIcon(Utils.getIcon(DBAnalyzer
				.getProperty("bookmark.leaf.icon")));

		tree.setCellRenderer(renderer);
		tree.setRootVisible(false);
		// tree.setCellEditor(new MyEditor());
		// tree.setEditable(true);
		tree.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent event) {
				if (((event.getModifiers() & InputEvent.BUTTON3_MASK) != 0)) {
					showMenu(event.getX(), event.getY());
				}
			}
		});
		tree.addTreeSelectionListener(new TreeSelectionListener() {
			@Override
			public void valueChanged(TreeSelectionEvent e) {
				File f = (File) tree.getLastSelectedPathComponent();
				if (f != null)
					frame.setText(f.getAbsoluteFile());

			}
		});
		setLayout(new BorderLayout(0, 0));
		JScrollPane scrollPane = new JScrollPane(tree);
		add(scrollPane);
	}

	protected void showMenu(int x, int y) {
		JPopupMenu popup = new JPopupMenu();
		JMenuItem mi = new JMenuItem(
				DBAnalyzer.getProperty("bookmark.delete.label"));
		JMenuItem addDir = new JMenuItem(
				DBAnalyzer.getProperty("bookmark.add-folder.label"));
		JMenuItem rename = new JMenuItem(
				DBAnalyzer.getProperty("bookmark.rename.label"));
		// TreePath path = tree.getSelectionPath();

		// int selRow = tree.getRowForLocation(x, y);
		TreePath path = tree.getPathForLocation(x, y);
		if (path == null)
			return;
		tree.setSelectionPath(path);
		Object node = path.getLastPathComponent();
		if (node == tree.getModel().getRoot()) {
			mi.setEnabled(false);
		}
		popup.add(addDir);
		popup.add(rename);
		popup.add(mi);

		rename.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				File f = (File) tree.getLastSelectedPathComponent();
				File parent = f.getParentFile();
				int index = f.getName().length();
				if (f.getName().lastIndexOf(".") > 0)
					index = f.getName().lastIndexOf(".");
				String name = f.getName().substring(0, index);
				String input = (String) JOptionPane.showInputDialog(frame,
						DBAnalyzer.getProperty("bookmark-dialog.rename.msg"),
						DBAnalyzer.getProperty("bookmark-dialog.rename.title"),
						JOptionPane.QUESTION_MESSAGE, null, null, (Object) name);
				if (input == null || input.isEmpty())
					return;
				f.renameTo(new File(parent, input + ".sql"));
				SwingUtilities.updateComponentTreeUI(tree);
			}
		});

		addDir.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// File f = (File) tree.getLastSelectedPathComponent();
				// File s;
				// if (f != null && f.isDirectory())
				// s = new File(f, "Новая папка");
				// else
				// s = new File((File) model.getRoot(), "Новая папка");
				// boolean b = s.mkdirs();

				String input = JOptionPane.showInputDialog(
						frame,
						DBAnalyzer
								.getProperty("bookmark-dialog.new-folder.msg"),
						DBAnalyzer
								.getProperty("bookmark-dialog.new-folder.title"),
						JOptionPane.QUESTION_MESSAGE);
				if (input == null || input.isEmpty())
					return;
				File root = (File) model.getRoot();
				File newDir = new File(root, input);
				if (!newDir.exists())
					newDir.mkdirs();
				SwingUtilities.updateComponentTreeUI(tree);
			}
		});
		mi.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				deleteSelectedItems();
			}
		});
		tree.makeVisible(path);
		popup.show(tree, x, y);
	}

	protected void deleteSelectedItems() {
		File f = (File) tree.getLastSelectedPathComponent();
		f.delete();
		SwingUtilities.updateComponentTreeUI(tree);

	}

	public void reloadTree() {
		model = new FileTreeModel(DBAnalyzer.getPreference("misc.bookmark"));
		tree.setModel(model);
		SwingUtilities.updateComponentTreeUI(this.tree);
	}
}

class FileTreeModel implements TreeModel {
	private File root;
	private EventListenerList listenerList = new EventListenerList();

	public FileTreeModel(File root) {
		this.root = root;
	}

	public FileTreeModel() {
	}

	public FileTreeModel(String root) {
		this.root = new File(root);
	}

	public void setRoot(File root) {
		this.root = root;
	}

	@Override
	public void addTreeModelListener(TreeModelListener l) {
		listenerList.add(TreeModelListener.class, l);
	}

	@Override
	public Object getChild(Object parent, int index) {
		File directory = (File) parent;
		String[] list = directory.list();
		return new File(directory, list[index]);
	}

	@Override
	public int getChildCount(Object parent) {
		File file = (File) parent;
		if (file.isDirectory())
			return file.list().length;
		else
			return 0;
	}

	@Override
	public int getIndexOfChild(Object parent, Object child) {
		File directory = (File) parent;
		File file = (File) child;
		String[] fileNames = directory.list();
		int result = -1;
		for (int i = 0; i < fileNames.length; i++) {
			if (file.getName().endsWith(fileNames[i])) {
				return i;
			}
		}
		return -1;
	}

	@Override
	public Object getRoot() {
		return root;
	}

	@Override
	public boolean isLeaf(Object node) {
		return ((File) node).isFile();
	}

	@Override
	public void removeTreeModelListener(TreeModelListener l) {
		listenerList.remove(TreeModelListener.class, l);

	}

	@Override
	public void valueForPathChanged(TreePath path, Object newValue) {
		File f = (File) path.getLastPathComponent();
		f.renameTo(new File((String) newValue));
	}

	public void fireTreeNodesInserted(TreeModelEvent e) {
		Object[] listeners = listenerList.getListenerList();
		for (Object listener : listeners) {
			TreeModelListener l = (TreeModelListener) listener;
			l.treeNodesInserted(e);
		}
	}

	public void fireTreeNodesRemoved(TreeModelEvent e) {
		Object[] listeners = listenerList.getListenerList();
		for (Object listener : listeners) {
			TreeModelListener l = (TreeModelListener) listener;
			l.treeNodesRemoved(e);
		}
	}

	public void fireTreeNodesChanged(TreeModelEvent e) {
		Object[] listeners = listenerList.getListenerList();
		for (Object listener : listeners) {
			TreeModelListener l = (TreeModelListener) listener;
			l.treeNodesChanged(e);
		}
	}

	public void fireTreeStructureChanged(TreeModelEvent e) {
		Object[] listeners = listenerList.getListenerList();
		for (Object listener : listeners) {
			TreeModelListener l = (TreeModelListener) listener;
			l.treeStructureChanged(e);
		}
	}
}

class FileTreeCellRenderer extends DefaultTreeCellRenderer {

	@Override
	public Component getTreeCellRendererComponent(JTree tree, Object value,
			boolean sel, boolean expanded, boolean leaf, int row,
			boolean hasFocus) {
		JLabel result = (JLabel) super.getTreeCellRendererComponent(tree,
				value, sel, expanded, leaf, row, hasFocus);
		String fileName = ((File) value).getName();
		if (fileName.endsWith(".sql"))
			fileName = fileName.substring(0, fileName.lastIndexOf("."));
		result.setText(fileName);
		return result;
	}
}

class MyEditor extends JTextField implements TreeCellEditor {
	private String parent;
	private File file;

	@Override
	public Object getCellEditorValue() {
		return this.getText();
	}

	@Override
	public boolean isCellEditable(EventObject evt) {
		return evt instanceof MouseEvent
				&& ((MouseEvent) evt).getClickCount() == 2;
	}

	@Override
	public Component getTreeCellEditorComponent(final JTree tree, Object value,
			boolean isSelected, boolean expanded, boolean leaf, int row) {
		file = (File) value;
		parent = file.getParent();
		String fileName = file.getName();
		if (fileName.endsWith(".sql"))
			fileName = fileName.substring(0, fileName.lastIndexOf("."));
		this.setText(fileName);
		this.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
					stopCellEditing();
				}
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					rename(e);
					SwingUtilities.updateComponentTreeUI(tree);
				}
			}
		});
		this.setColumns(10);
		return this;
	}

	@Override
	public void cancelCellEditing() {
	}

	@Override
	public void addCellEditorListener(CellEditorListener l) {

	}

	@Override
	public void removeCellEditorListener(CellEditorListener l) {
	}

	@Override
	public boolean shouldSelectCell(EventObject anEvent) {
		return true;
	}

	@Override
	public boolean stopCellEditing() {
		return false;
	}

	private void rename(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_ENTER) {
			file.renameTo(new File(parent, this.getText()));
		}
	}
}
