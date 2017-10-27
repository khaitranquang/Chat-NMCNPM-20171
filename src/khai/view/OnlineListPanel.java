package khai.view;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import javax.swing.text.StyledDocument;

public class OnlineListPanel extends JPanel{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JScrollPane scrollPane;
	private JTextPane onlineList;
	private StringBuilder stringBuilder;
	public OnlineListPanel() {
		this.setPreferredSize(new Dimension(100, 300));
		StyleContext context = new StyleContext();
		StyledDocument document = new DefaultStyledDocument(context);
		Style style = context.getStyle(StyleContext.DEFAULT_STYLE);
		StyleConstants.setAlignment(style, StyleConstants.ALIGN_LEFT);
		StyleConstants.setFontSize(style, 14);
		StyleConstants.setSpaceAbove(style, 4);
		StyleConstants.setSpaceBelow(style, 4);
		onlineList = new JTextPane(document);
		onlineList.setContentType("text/html");
		onlineList.setEditable(false);
		scrollPane = new JScrollPane(onlineList);
		scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		stringBuilder = new StringBuilder(onlineList.getText());
		this.setLayout(new BorderLayout());
		this.add(scrollPane,BorderLayout.CENTER);
	}
	public void append(String text) {
		stringBuilder.append(text+"<br>");
		onlineList.setText(stringBuilder.toString());
	}
	public JScrollPane getScrollPane() {
		return scrollPane;
	}
	public void setScrollPane(JScrollPane scrollPane) {
		this.scrollPane = scrollPane;
	}
	public JTextPane getOnlineList() {
		return onlineList;
	}
	public void setOnlineList(JTextPane onlineList) {
		this.onlineList = onlineList;
	}
	public StringBuilder getStringBuilder() {
		return stringBuilder;
	}
	public void setStringBuilder(StringBuilder stringBuilder) {
		this.stringBuilder = stringBuilder;
	}
	
}
