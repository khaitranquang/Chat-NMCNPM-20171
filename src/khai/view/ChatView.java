package khai.view;

import java.awt.BorderLayout;

import javax.swing.JFrame;

public class ChatView extends JFrame{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JTextPanePanel textPanePanel;
	private OnlineListPanel onlineListPanel;
	private ChatPanel chatPanel;
	public ChatView() {
		textPanePanel =  new JTextPanePanel();
		onlineListPanel = new OnlineListPanel();
		chatPanel = new ChatPanel();
		this.setLayout(new BorderLayout());
		this.setSize(600,600);
		this.setLocation(350, 100);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setResizable(false);
		this.add(textPanePanel,BorderLayout.CENTER);
		this.add(onlineListPanel,BorderLayout.EAST);
		this.add(chatPanel, BorderLayout.SOUTH);
		this.setVisible(true);
	}
	public JTextPanePanel getTextPanePanel() {
		return textPanePanel;
	}
	public void setTextPanePanel(JTextPanePanel textPanePanel) {
		this.textPanePanel = textPanePanel;
	}
	public OnlineListPanel getOnlineListPanel() {
		return onlineListPanel;
	}
	public void setOnlineListPanel(OnlineListPanel onlineListPanel) {
		this.onlineListPanel = onlineListPanel;
	}
	public ChatPanel getChatPanel() {
		return chatPanel;
	}
	public void setChatPanel(ChatPanel chatPanel) {
		this.chatPanel = chatPanel;
	}
	public static void main(String[] args) {
		new ChatView();
	}
}
