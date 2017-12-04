package khai;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import javax.swing.text.StyledDocument;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import khai.WebsocketClientEndpoint.MessageHandler;

public class ChatBox {
	static JSONParser jsonParser = new JSONParser();
	public String chatContent="";
	public WebsocketClientEndpoint clientSocket;
	public List<String> iconlist;
	public String username;
	public String room;
	public IconBox iconbox;
	public JPopupMenu popMenu1;
	public JMenuBar menu,iconbar;
	public JMenu File,Edit,Format,View,Help,SelectRoom;
	public JMenuItem New,Open,Save,Saveas,Print,Exit,Find,Replace,Copy,Paste,Delete,Cut,Min,Max,Close,Info,SOICT,
			SAMI,SEP,OTHER;
	public JScrollPane scroll;
	public String name;
	public JButton Logout;
	public JLabel chatarea;
	public JLabel onlinelabel;
	public JFrame frame;
	public JButton Chat;
	public JButton Quit;
	public JTextField textField;
	public JTextPane textArea;
	public JPanel panelonline,icon;
	public JPanel panelbutton;
	public JTextArea online;
	public JCheckBox WordWrap;
	public ChatBox(WebsocketClientEndpoint client,String username,List<String> str) throws MalformedURLException, IOException{
		this.iconlist=str;
		this.username= username;
		
		this.clientSocket=client;
		
		this.clientSocket.addMessageHandler(new MessageHandler() {
			
			public void handleMessage(String message) {
				JSONObject object = null;
				try {
					object = (JSONObject) jsonParser.parse(message);
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				String stt = (String) object.get("status");
				if(stt.equals("text")) {
					String username=(String) object.get("from");
					String avatar = (String) object.get("avatarurl");
					String mess = (String) object.get("message");
					chatContent+="<br>"+ "<img src=\""+avatar+ "\" width=\"30\" height=\"30\"/> "+"<font face=\"Arial\" size=\"10\">"+username+": "+"</font>";
					chatContent+="<br>"+"<font face=\"Arial\" size=\"10\">"+mess+"</font>";
					textArea.setText(chatContent);
				}
				else if(stt.equals("icon")) {
					String username=(String) object.get("from");
					String mess = (String) object.get("message");
					String avatar = (String) object.get("avatarurl");
					chatContent+="<br>"+  "<img src=\""+avatar+ "\" width=\"30\" height=\"30\"/> "+"<font face=\"Arial\" size=\"10\">"+username+": "+"</font>";
					chatContent+="<br>"+ "<img src=\""+mess+ "\" width=\"50\" height=\"50\"/> ";
					textArea.setText(chatContent);
					
				}
			}
		});
		SelectRoom= new JMenu("Select Room");
		iconbox = new IconBox();
		for(String url:iconlist) {
			System.out.println(url);
			BufferedImage buff = ImageIO.read(new URL(url));
			ImageIcon icon = new ImageIcon(buff.getScaledInstance(50, 50, Image.SCALE_SMOOTH));
			iconbox.model.addElement(new Icon(url, icon));
		}
		iconbox.list.setModel(iconbox.model);
		iconbox.list.addListSelectionListener(new ListSelectionListener() {
			
			public void valueChanged(ListSelectionEvent e) {
				JSONObject ob = new JSONObject();
				ob.put("status", "icon");
				ob.put("message", iconbox.list.getSelectedValue().name);
				clientSocket.sendMessage(ob.toJSONString());
				
			}
		});
		frame = new JFrame(name);
		this.frame.addWindowListener(new WindowAdapter() {

			@Override
			public void windowClosing(WindowEvent e) {
				try {
					clientSocket.userSession.close();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
			
		});
		onlinelabel = new JLabel("Online List");
		Chat = new JButton("Chat");
		Logout = new JButton("Logout");
		Quit = new JButton("Quit");
		textField = new JTextField(30);
		textArea = new JTextPane();
		textArea.setContentType("text/html");
		online = new JTextArea(30,10);
		panelonline = new JPanel();
		panelbutton = new JPanel();
		frame.setResizable(false);
		frame.setSize(610, 530);
		frame.setLocation(350, 100);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		panelonline.setSize(400, 100);
		panelbutton.setSize(500,50);
		frame.setLayout(new BorderLayout());
		frame.add(textArea,BorderLayout.WEST);
		frame.add(panelonline, BorderLayout.EAST);
		frame.add(panelbutton, BorderLayout.SOUTH);
		frame.setTitle(username);
		panelonline.setLayout(new BorderLayout());
		panelbutton.setLayout(new FlowLayout());
		panelonline.setBorder(BorderFactory.createLineBorder(Color.black));
		textArea.setBorder(BorderFactory.createLineBorder(Color.black));
		icon = new JPanel();iconbar = new JMenuBar();
		icon.setLayout(new FlowLayout());
		iconbar.add(iconbox.iconmenu);
		icon.add(iconbar);
		panelonline.add(onlinelabel,BorderLayout.NORTH);
		panelonline.add(online,BorderLayout.CENTER);
		panelbutton.add(textField);
		panelbutton.add(Chat);
		panelbutton.add(icon);
		panelbutton.add(Logout);
		panelbutton.add(Quit);
	
		this.textField.requestDefaultFocus();
		menu = new JMenuBar();
		File = new JMenu("File");Edit = new JMenu("Edit");Format = new JMenu("Format");
		View = new JMenu("View");Help = new JMenu("Help");
		New = new JMenuItem("New");Open = new JMenuItem("Send");Save = new JMenuItem("Save");
		Saveas = new JMenuItem("Open");Exit = new JMenuItem("Exit");Info = new JMenuItem("Infomation");
		Find = new JMenuItem("Find");Replace = new JMenuItem("Replace");
		SOICT = new JMenuItem("SOICT");SAMI = new JMenuItem("SAMI");SEP = new JMenuItem("SEP");
		OTHER = new JMenuItem("OTHER");
		SelectRoom.add(SOICT);SelectRoom.addSeparator();
		SelectRoom.add(SAMI);SelectRoom.addSeparator();
		SelectRoom.add(SEP);SelectRoom.addSeparator();SelectRoom.add(OTHER);
		File.add(New);File.addSeparator();File.add(SelectRoom);File.addSeparator();File.add(Saveas);File.addSeparator();
		File.add(Open);File.addSeparator();
		File.add(Save);File.addSeparator();File.add(Exit);Edit.add(Find);Edit.addSeparator();
		Edit.add(Replace);Help.add(Info);
		menu.add(File);menu.add(Edit);menu.add(Format);menu.add(View);menu.add(Help);
		frame.add(menu,BorderLayout.NORTH);
		scroll = new JScrollPane(textArea);
		frame.add(scroll, BorderLayout.CENTER);
		popMenu1 = new JPopupMenu();
		Copy=new JMenuItem("Copy");
		Paste=new JMenuItem("Paste");
		Cut=new JMenuItem("Cut");
		Delete = new JMenuItem("Delete");
		Min = new JMenuItem("Minimum");
		Max=new JMenuItem("Maximum");
		Close = new JMenuItem("Close");
		popMenu1.add(Copy);popMenu1.addSeparator();popMenu1.add(Cut);popMenu1.addSeparator();popMenu1.add(Paste);
		popMenu1.addSeparator();popMenu1.add(Delete);
		textArea.setComponentPopupMenu(popMenu1);
		New.addActionListener(new ActionListener(){

			public void actionPerformed(ActionEvent e) {
				textArea.setText("");
				
			}
			
		});
		WordWrap = new JCheckBox("WordWrap");
		Format.add(WordWrap);
		WordWrap.setSelected(true);
//		WordWrap.addActionListener(new ActionListener(){
//
//			public void actionPerformed(ActionEvent e) {
//				if(WordWrap.isSelected()){
//					textArea.setWrapStyleWord(true);
//				}
//				else{
//					textArea.setWrapStyleWord(false);
//				}
//				
//			}
//			
//		});
		Info.addActionListener(new ActionListener(){

			public void actionPerformed(ActionEvent arg0) {
				JOptionPane.showMessageDialog(null, "Chatroom version 1.0 by NBK", "Infomation", JOptionPane.INFORMATION_MESSAGE);
				
			}
			
		});
		Open.setMnemonic(KeyEvent.VK_D);
		Save.setMnemonic(KeyEvent.VK_S);
		Saveas.setMnemonic(KeyEvent.VK_O);
		Exit.setMnemonic(KeyEvent.VK_E);
		New.setMnemonic(KeyEvent.VK_N);
		File.setMnemonic(KeyEvent.VK_F);
		Edit.setMnemonic(KeyEvent.VK_E);
		Format.setMnemonic(KeyEvent.VK_W);
		WordWrap.setMnemonic(KeyEvent.VK_W);
		Help.setMnemonic(KeyEvent.VK_H);
		Info.setMnemonic(KeyEvent.VK_I);
		textArea.setEditable(false);
//		textArea.setLineWrap(true);
//		textArea.setWrapStyleWord(true);
		online.setEditable(false);
		frame.setVisible(true);
		Chat.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				if(!textField.getText().equals("")) {
					JSONObject object = new JSONObject();
					object.put("status", "text");
					object.put("message", textField.getText());
					object.put("size", 12);
					object.put("font", "Arial");
					textField.setText("");
					clientSocket.sendMessage(object.toJSONString());
				}
				
			}
		});
	}
	
}
