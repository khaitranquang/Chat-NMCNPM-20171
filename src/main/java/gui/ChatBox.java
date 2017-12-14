package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import websocket.WebsocketClientEndpoint;
import websocket.WebsocketClientEndpoint.MessageHandler;

public class ChatBox extends JFrame implements ActionListener, MouseListener, KeyListener,WindowListener {
	/**
	 * 
	 */

	private static final long serialVersionUID = 1L;
	private JSONParser parser;
	private JPanel chatPanel, onlinePanel, buttonPanel;
	private JScrollPane chatScroll, onlineScroll;
	private JTextPane chatPane, onlinePane;
	private JLabel onlineLabel;
	private JButton chatButton, quitButton;
	private JMenu iconMenu;
	private WebsocketClientEndpoint client;
	private List<String> iconList;
	private JSONArray onlineList;
	private String roomName;
	private JTable iconTable;
	private DefaultTableModel tableModel;
	private JTextField textField;
	private StringBuilder chatContent;
	private List<JLabel> onlineNumberLabel;
	private PrepareChat prepareChat;
	public ChatBox(PrepareChat chat,WebsocketClientEndpoint client,List<JLabel> onlineNumberLabel, String roomName) {
		this.prepareChat=chat;
		this.client = client;
		this.roomName = roomName;
		this.onlineNumberLabel=onlineNumberLabel;
		chatContent = new StringBuilder("");
		parser = new JSONParser();
		this.setTitle(roomName);
		this.setSize(500, 500);
		this.setSize(610, 530);
		this.setLocation(350, 100);
		this.setResizable(false);
		this.addWindowListener(this);
		initGUI();
		handMessage();
	}

	public void handMessage() {
		client.addMessageHandler(new MessageHandler() {

			public void handleMessage(String message) {
				JSONObject object;
				try {
					object = (JSONObject) parser.parse(message);
					System.out.println(message);
					String status = (String) object.get("status");
					if (status.equals("iconlist")) {
						handIconList(object);
					} else if (status.equals("onlinelist")) {
						handOnlineList(object);
					} else if (status.equals("text")) {
						handText(object);
					} else if (status.equals("icon")) {
						handIcon(object);
					} else if(status.equals("update")) {
						handUpdate(object);
					} else if(status.equals("avatar")) {
						handAvatar(object);
					}
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		});
	}

	public void handText(JSONObject object) {
		String username = (String) object.get("from");
		String avatar = (String) object.get("avatarurl");
		String mess = (String) object.get("message");
		chatContent.append("<br>" + "<img src=\"" + avatar + "\" width=\"30\" height=\"30\"/> "
				+ "<font face=\"Arial\" size=\"5\">" + username + ": "+ mess + "</font>");
		chatPane.setText(chatContent.toString());
		JScrollBar bar= chatScroll.getVerticalScrollBar();
		bar.setValue(bar.getMaximum());
	}
	public void handAvatar(JSONObject object) {
		String mess = (String) object.get("message");
		if(mess.equals("OKE")) {
			String url = (String) object.get("avatarurl");
			try {
				BufferedImage img = ImageIO.read(new URL(url));
				ImageIcon icon = new ImageIcon(img.getScaledInstance(120, 120, Image.SCALE_SMOOTH));
				prepareChat.getAvatarLabel().setIcon(icon);
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException d) {
				// TODO Auto-generated catch block
				JOptionPane.showMessageDialog(prepareChat.getAvatarLabel(), "Khởi động lại để hoàn tất");
			}
			
			
		}
		else {
			JOptionPane.showMessageDialog(prepareChat.getAvatarLabel(), "Đổi avatar không thành công!");
		}
	}
	public void handUpdate(JSONObject object) {
		String status = (String) object.get("status");
		if (status.equals("update")) {
			JSONArray array = (JSONArray) object.get("message");
			for(int i=0;i<array.size();i++) {
				JSONObject ob = (JSONObject) array.get(i);
				onlineNumberLabel.get(i).setText("Online Quantity:   " + ob.get("number"));
			}
			//
			//
			//
			//
			///
			//

		}
	}
	public void handIcon(JSONObject object) {
		String username = (String) object.get("from");
		String mess = (String) object.get("message");
		String avatar = (String) object.get("avatarurl");
		chatContent.append("<br>" + "<img src=\"" + avatar + "\" width=\"30\" height=\"30\"/> "
				+ "<font face=\"Arial\" size=\"5\">" + username + ": " + "</font>");
		chatContent.append( "<img src=\"" + mess + "\" width=\"50\" height=\"50\"/> ");
		chatPane.setText(chatContent.toString());
		JScrollBar bar= chatScroll.getVerticalScrollBar();
		bar.setValue(bar.getMaximum());
	}

	public void handOnlineList(JSONObject object) {
		onlineList = (JSONArray) object.get("userlist");
		StringBuilder builder = new StringBuilder("");
		for (int i = 0; i < onlineList.size(); i++) {
			JSONObject obj = (JSONObject) onlineList.get(i);
			String avatar = (String) obj.get("avatarurl");
			String username = (String) obj.get("username");
			builder.append("<br>" + "<img src=\"" + avatar + "\" width=\"20\" height=\"20\"/> "
					+ "<font face=\"Arial\" size=\"4\">" + username + "</font>");
		}
		onlinePane.setText(builder.toString());
	}

	@SuppressWarnings("unchecked")
	public void handIconList(JSONObject object) {
		iconList = (List<String>) object.get("icon");
		String[] columnname = { "", "", "", "" };
		ImageIcon[][] datas = new ImageIcon[iconList.size() / 4 + 1][4];
		for (int i = 0; i < iconList.size() / 4 + 1; i++) {
			for (int j = 0; j < 4; j++) {
				try {
					if (i * 4 + j < iconList.size()) {
						BufferedImage img = ImageIO.read(new URL(iconList.get(i * 4 + j)));
						datas[i][j] = new ImageIcon(img.getScaledInstance(60, 60, Image.SCALE_SMOOTH));
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		}
		tableModel.setDataVector(datas, columnname);
	}

	@SuppressWarnings("serial")
	public void initGUI() {
		// textpane
		chatPane = new JTextPane();
		onlinePane = new JTextPane();
		chatPane.setEditable(false);
		chatPane.setContentType("text/html");
		onlinePane.setEditable(false);
		onlinePane.setContentType("text/html");
		chatPane.setPreferredSize(new Dimension(470, 460));
		onlinePane.setPreferredSize(new Dimension(120, 450));

		// sroll

		chatScroll = new JScrollPane(chatPane, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		chatScroll.getVerticalScrollBar().addAdjustmentListener(new AdjustmentListener() {  
	        public void adjustmentValueChanged(AdjustmentEvent e) {  
	            e.getAdjustable().setValue(e.getAdjustable().getMaximum());  
	        }
	    });
		onlineScroll = new JScrollPane(onlinePane, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

		// textfile
		textField = new JTextField(30);
		textField.addKeyListener(this);

		// icon
		String[] columname = { "", "", "", "" };
		ImageIcon[][] datas = new ImageIcon[4][4];
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				datas[i][j] = new ImageIcon();
			}
		}
		tableModel = new DefaultTableModel(datas, columname) {
			@SuppressWarnings({ "unchecked", "rawtypes" })
			public Class getColumnClass(int column) {
				return new ImageIcon().getClass();
			}

			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		iconTable = new JTable(tableModel);
		iconTable.setRowHeight(50);
		iconTable.setCellSelectionEnabled(true);
		iconTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		iconTable.addMouseListener(this);
		iconMenu = new JMenu("ICON");
		iconMenu.add(iconTable);
		JMenuBar menubar = new JMenuBar();
		menubar.add(iconMenu);

		// button
		chatButton = new JButton("Chat");
		chatButton.addActionListener(this);
		// label
		onlineLabel = new JLabel("Online List");

		// Panel
		onlinePanel = new JPanel(new BorderLayout());
		buttonPanel = new JPanel();
		chatPanel = new JPanel();
		onlinePanel.add(onlineLabel, BorderLayout.NORTH);
		onlinePanel.add(onlineScroll, BorderLayout.CENTER);
		chatPanel.add(chatScroll);
		buttonPanel.setLayout(new FlowLayout());
		buttonPanel.add(textField);
		buttonPanel.add(chatButton);
		buttonPanel.add(menubar);
		buttonPanel.setBorder(BorderFactory.createLineBorder(Color.black));
		onlinePanel.setBorder(BorderFactory.createLineBorder(Color.black));
		chatPanel.setBorder(BorderFactory.createLineBorder(Color.black));

		this.setLayout(new BorderLayout());
		this.add(onlinePanel, BorderLayout.EAST);
		this.add(buttonPanel, BorderLayout.SOUTH);
		this.add(chatPanel, BorderLayout.CENTER);
		this.setVisible(true);
	}

	public JTable getIconTable() {
		return iconTable;
	}

	public void setIconTable(JTable iconTable) {
		this.iconTable = iconTable;
	}

	public DefaultTableModel getTableModel() {
		return tableModel;
	}

	public JTextField getTextField() {
		return textField;
	}

	public void setTextField(JTextField textField) {
		this.textField = textField;
	}

	public void setTableModel(DefaultTableModel tableModel) {
		this.tableModel = tableModel;
	}

	public String getRoomName() {
		return roomName;
	}

	public void setRoomName(String roomName) {
		this.roomName = roomName;
	}

	public JSONArray getOnlineList() {
		return onlineList;
	}

	public void setOnlineList(JSONArray onlineList) {
		this.onlineList = onlineList;
	}

	public List<String> getIconList() {
		return iconList;
	}

	public void setIconList(List<String> iconList) {
		this.iconList = iconList;
	}

	public JSONParser getParser() {
		return parser;
	}

	public void setParser(JSONParser parser) {
		this.parser = parser;
	}

	public JPanel getChatPanel() {
		return chatPanel;
	}

	public void setChatPanel(JPanel chatPanel) {
		this.chatPanel = chatPanel;
	}

	public JPanel getOnlinePanel() {
		return onlinePanel;
	}

	public void setOnlinePanel(JPanel onlinePanel) {
		this.onlinePanel = onlinePanel;
	}

	public JPanel getButtonPanel() {
		return buttonPanel;
	}

	public void setButtonPanel(JPanel buttonPanel) {
		this.buttonPanel = buttonPanel;
	}

	public JScrollPane getChatScroll() {
		return chatScroll;
	}

	public void setChatScroll(JScrollPane chatScroll) {
		this.chatScroll = chatScroll;
	}

	public JScrollPane getOnlineScroll() {
		return onlineScroll;
	}

	public void setOnlineScroll(JScrollPane onlineScroll) {
		this.onlineScroll = onlineScroll;
	}

	public JTextPane getChatPane() {
		return chatPane;
	}

	public void setChatPane(JTextPane chatPane) {
		this.chatPane = chatPane;
	}

	public JTextPane getOnlinePane() {
		return onlinePane;
	}

	public void setOnlinePane(JTextPane onlinePane) {
		this.onlinePane = onlinePane;
	}

	public JLabel getOnlineLabel() {
		return onlineLabel;
	}

	public void setOnlineLabel(JLabel onlineLabel) {
		this.onlineLabel = onlineLabel;
	}

	public JButton getChatButton() {
		return chatButton;
	}

	public void setChatButton(JButton chatButton) {
		this.chatButton = chatButton;
	}

	public JButton getQuitButton() {
		return quitButton;
	}

	public void setQuitButton(JButton quitButton) {
		this.quitButton = quitButton;
	}

	public JMenu getIconMenu() {
		return iconMenu;
	}

	public void setIconMenu(JMenu iconMenu) {
		this.iconMenu = iconMenu;
	}

	public WebsocketClientEndpoint getClient() {
		return client;
	}

	public void setClient(WebsocketClientEndpoint client) {
		this.client = client;
	}

	@SuppressWarnings("unchecked")
	public void sendText() {
		if (!textField.getText().equals("")) {
			JSONObject object = new JSONObject();
			object.put("status", "text");
			object.put("message", textField.getText());
			object.put("size", 8);
			object.put("font", "Arial");
			textField.setText("");
			client.sendMessage(object.toJSONString());
		}
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == chatButton) {
			sendText();
		}
	}

	@SuppressWarnings("unchecked")
	public void mouseClicked(MouseEvent e) {
		if (iconTable.getSelectedRow() * 4 + iconTable.getSelectedColumn() < iconList.size()) {
			JSONObject ob = new JSONObject();
			ob.put("status", "icon");
			ob.put("message", iconList.get(iconTable.getSelectedRow() * 4 + iconTable.getSelectedColumn()));
			client.sendMessage(ob.toJSONString());
		}

	}

	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_ENTER) {
			sendText();
		}

	}

	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	public StringBuilder getChatContent() {
		return chatContent;
	}

	public void setChatContent(StringBuilder chatContent) {
		this.chatContent = chatContent;
	}

	public List<JLabel> getOnlineNumberLabel() {
		return onlineNumberLabel;
	}

	public void setOnlineNumberLabel(List<JLabel> onlineNumberLabel) {
		this.onlineNumberLabel = onlineNumberLabel;
	}

	public void windowOpened(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@SuppressWarnings("unchecked")
	public void windowClosing(WindowEvent e) {
		
		JSONObject object = new JSONObject();
		object.put("status", "outroom");
		client.sendMessage(object.toJSONString());
		prepareChat.setChatbox(null);
		dispose();
	}

	public void windowClosed(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	public void windowIconified(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	public void windowDeiconified(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	public void windowActivated(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	public void windowDeactivated(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	public PrepareChat getPrepareChat() {
		return prepareChat;
	}

	public void setPrepareChat(PrepareChat prepareChat) {
		this.prepareChat = prepareChat;
	}

}
