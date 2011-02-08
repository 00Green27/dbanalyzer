package ru.stavjust.ui;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;

import net.miginfocom.swing.MigLayout;
import ru.stavjust.DBAnalyzer;
import ru.stavjust.utils.ManifestReader;
import ru.stavjust.utils.Utils;

public class AboutDialog extends JDialog {

	public AboutDialog(MainFrame frame) {
		super(frame, true);
		setTitle(DBAnalyzer.getProperty("about-dialog.title"));
		getContentPane()
				.setLayout(
						new MigLayout("insets dialog", "[]40[grow]",
								"[][][][][][grow]"));

		JLabel logoLabel = new JLabel(Utils.getIcon(DBAnalyzer
				.getProperty("about-dialog.logo")));
		getContentPane().add(logoLabel, "cell 0 0 1 5");

		JLabel appLabel = new JLabel(
				DBAnalyzer.getProperty("about-dialog.app.name"));
		Font f = appLabel.getFont();
		appLabel.setFont(new Font(f.getName(), f.getStyle(), 24));
		getContentPane().add(appLabel, "cell 1 0");

		JLabel versionLabel = new JLabel("Версия: "
				+ ManifestReader.getImplementationVersion());
		getContentPane().add(versionLabel, "cell 1 1");

		JLabel dateLabel = new JLabel("Дата сборки: "
				+ ManifestReader.getBuiltDate());
		getContentPane().add(dateLabel, "cell 1 2");

		JButton button = new JButton("OK");
		button.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});

		JLabel copyrightLabel = new JLabel(
				DBAnalyzer.getProperty("about-dialog.copyright"));
		getContentPane().add(copyrightLabel, "cell 1 3");
		getContentPane().add(button, "tag right,cell 1 5,aligny bottom");

		pack();
		setResizable(false);
		setLocationRelativeTo(frame);
	}
}
