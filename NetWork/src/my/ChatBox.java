package my;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import javax.swing.*;
import javax.swing.border.Border;

public class ChatBox {
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
	public JTextArea textArea;
	public JPanel panelonline,icon;
	public JPanel panelbutton;
	public JTextArea online;
	public JCheckBox WordWrap;
	public ChatBox(){
		SelectRoom= new JMenu("Select Room");
		iconbox = new IconBox();
		frame = new JFrame(name);
		onlinelabel = new JLabel("Online List");
		Chat = new JButton("Chat");
		Logout = new JButton("Logout");
		Quit = new JButton("Quit");
		textField = new JTextField(30);
		textArea = new JTextArea(27,30);
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

			@Override
			public void actionPerformed(ActionEvent e) {
				textArea.setText("");
				
			}
			
		});
		WordWrap = new JCheckBox("WordWrap");
		Format.add(WordWrap);
		WordWrap.setSelected(true);
		WordWrap.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				if(WordWrap.isSelected()){
					textArea.setWrapStyleWord(true);
				}
				else{
					textArea.setWrapStyleWord(false);
				}
				
			}
			
		});
		Info.addActionListener(new ActionListener(){

			@Override
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
		textArea.setLineWrap(true);
		textArea.setWrapStyleWord(true);
		online.setEditable(false);
		frame.setVisible(false);
	
	}
	public static void main(String[] args){
		ChatBox a = new ChatBox();
		a.frame.setVisible(true);
	}
}
