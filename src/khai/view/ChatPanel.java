package khai.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextPane;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import javax.swing.text.StyledDocument;

public class ChatPanel extends JPanel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JTextPane chatFiled;
	private JButton chatButton;
	public ChatPanel() {
		this.setSize(new Dimension(600, 60));
		StyleContext context = new StyleContext();
		StyledDocument document = new DefaultStyledDocument(context);
		Style style = context.getStyle(StyleContext.DEFAULT_STYLE);
		StyleConstants.setAlignment(style, StyleConstants.ALIGN_LEFT);
		StyleConstants.setFontSize(style, 14);
		StyleConstants.setSpaceAbove(style, 4);
		StyleConstants.setSpaceBelow(style, 4);
		chatFiled = new JTextPane(document);
		chatFiled.setContentType("text/html");
		chatButton = new JButton("Chat");
		this.setLayout(new BorderLayout());
		this.add(chatFiled,BorderLayout.CENTER);
		this.add(chatButton,BorderLayout.EAST);
		
	}
	public JTextPane getChatFiled() {
		return chatFiled;
	}
	public void setChatFiled(JTextPane chatFiled) {
		this.chatFiled = chatFiled;
	}
	public JButton getChatButton() {
		return chatButton;
	}
	public void setChatButton(JButton chatButton) {
		this.chatButton = chatButton;
	}
	
	
}
