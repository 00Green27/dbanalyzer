package ru.green.ui;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import java.awt.*;

/**
 * Панель вывода информации об исключительной ситуации
 * 
 * @author ehd
 */
public class StackTracePanel extends JPanel {

	private static final long serialVersionUID = -5263037529596408859L;
    private SimpleAttributeSet headStyle;
    private SimpleAttributeSet errorStyle;
    private StyledDocument document;

    static {
        final SimpleAttributeSet databaseNameStyle = new SimpleAttributeSet();
        StyleConstants.setBold(databaseNameStyle, true);
    }

    public StackTracePanel() {
		initComponents();

	}

	private void initComponents() {

		scrollPane = new JScrollPane();
		logPane =  new JTextPane();
		logPane.setEditable(false);
        document = logPane.getStyledDocument();
		msgLabel = new JLabel("");
//		logCheckBox = new JCheckBox(
//				DBAnalyzer.getProperty("log.log-checkbox.label"));
//		logCheckBox.setSelected(true);

        initStyle();

		scrollPane.setViewportView(logPane);

		GroupLayout layout = new GroupLayout(this);
		this.setLayout(layout);
		layout.setHorizontalGroup(layout
				.createParallelGroup(GroupLayout.Alignment.LEADING)
				.addGroup(
						layout.createSequentialGroup()
								.addComponent(msgLabel).addContainerGap())
				.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 500,
						Short.MAX_VALUE));
		layout.setVerticalGroup(layout.createParallelGroup(
				GroupLayout.Alignment.LEADING)
				.addGroup(
                        layout.createSequentialGroup()
                                .addComponent(msgLabel)
                                .addPreferredGap(
                                        LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(scrollPane,
                                        GroupLayout.DEFAULT_SIZE, 150,
                                        Short.MAX_VALUE)));
	}

    private void initStyle() {

//		Font font = new Font("Verdana", Font.PLAIN, 10);
//		logPane.setFont(font);

        headStyle = new SimpleAttributeSet();
//        StyleConstants.setBold(headStyle, true);
        StyleConstants.setForeground(headStyle, new Color(0, 0, 127));
        errorStyle = new SimpleAttributeSet();
        StyleConstants.setForeground(errorStyle, new Color(127, 0, 0));

    }


    public void appendText(String head, String text) {
//		logPane.append(text + "\n");
//		logPane.append("=======================================================\n\n");

        try {
            document.insertString(document.getLength(), head, headStyle);
            document.insertString(document.getLength(), text, errorStyle);
            document.insertString(document.getLength(), "\n\n", null);
        } catch (BadLocationException ignored) {
        } finally {
            logPane.setCaretPosition(0);
        }


	}

//	public void appendText(SQLException t) {
//		String exception;
//		if (!logCheckBox.isSelected()) {
//			StringWriter sw = new StringWriter();
//			t.printStackTrace(new PrintWriter(sw));
//			exception = sw.toString();
//		} else {
//			exception = t.getMessage() + "\n";
//		}
//		logPane.append(exception);
//		logPane.append("=======================================================\n\n");
//		logPane.setCaretPosition(0);
//	}

	public void clearLog() {
		logPane.setText("");
	}

	public int getTextCount() {
		return logPane.getText().length();
	}

	private JLabel msgLabel;
//	private JCheckBox logCheckBox;
	private JTextPane logPane;
	private JScrollPane scrollPane;

}
