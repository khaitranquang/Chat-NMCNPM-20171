package khai;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import khai.WebsocketClientEndpoint.MessageHandler;


public class LoginBox extends JFrame implements ActionListener{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JTextField tfID;
	private JPasswordField tfPassword;
	private JButton btnLogin,btnClose;
	public WebsocketClientEndpoint client;
	static JSONParser jsonParser = new JSONParser();
	public LoginBox() throws URISyntaxException {
		client = new WebsocketClientEndpoint(new URI("ws://localhost:8080/BTLCNPM/chatroom"));
		client.addMessageHandler(new MessageHandler() {
			
			public void handleMessage(String message) {
				JSONObject object = null;
				try {
					object = (JSONObject) jsonParser.parse(message);
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				String stt = (String) object.get("status");
				if(stt.equals("login")) {
					if(object.get("message").equals("OKE")) {
						List<String> list = (List<String>) object.get("icon");
						String temp =tfID.getText();
						try {
							new ChatBox(client, temp,list);
						} catch (MalformedURLException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						dispose();
					}
					else if(object.get("message").equals("NO")){
						JOptionPane.showMessageDialog(null, "Sai tk or mk");
					}
					else {
						JOptionPane.showMessageDialog(null, "Online rồi");
					}
				}
				else if(stt.equals("register")) {
					if(object.get("message").equals("OKE")) {
						JOptionPane.showMessageDialog(null, "Success");
					}
					else {
						JOptionPane.showMessageDialog(null, "NOT");
					}
				}
				
			}
		});
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(400,300);
		setLocationRelativeTo(null);
		setLayout(new BorderLayout(10,10));
		setTitle("Đăng nhập");
		
		add(createLabelPanel(),BorderLayout.WEST);
		add(createTextFieldPanel(),BorderLayout.CENTER);
		add(createButtonPanel(),BorderLayout.SOUTH);
		
		pack();
		setVisible(true);
	}

	private JPanel createButtonPanel() {
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(1, 3,10,10));
		panel.setBorder(new EmptyBorder(10, 10, 10, 10));
		
		btnLogin    = createButton("Login");
		btnClose    = createButton("Close");
		panel.add(btnLogin);
		panel.add(btnClose);
		
		return panel;
	}

	private JPanel createTextFieldPanel() {
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(2, 1,10,10));
		panel.setBorder(new EmptyBorder(10, 10, 10, 10));
		
		tfID = new JTextField(15);
		tfPassword = new JPasswordField(15);
		tfID.addKeyListener(new KeyAdapter(){

			@Override
			public void keyPressed(KeyEvent arg0) {
				if(arg0.getKeyCode()==KeyEvent.VK_ENTER) {
					tfPassword.requestFocus();
				}
			}
			
		});
		tfPassword.addKeyListener(new KeyAdapter(){

			@Override
			public void keyPressed(KeyEvent arg0) {
				if(arg0.getKeyCode()==KeyEvent.VK_ENTER) {
					btnLogin.doClick();
				}
			}
			
		});
		panel.add(tfID);
		panel.add(tfPassword);
		return panel;
	}

	private JPanel createLabelPanel() {
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(2, 1,10,10));
		panel.setBorder(new EmptyBorder(10, 10, 10, 10));
		
		panel.add(new JLabel("Username"));
		panel.add(new JLabel("Password"));
		return panel;
	}
	
	private JButton createButton(String name){
		JButton button = new JButton(name);
		button.addActionListener(this);
		return button;
	}
	@SuppressWarnings("unused")
	public void actionPerformed(ActionEvent e) {
			if(e.getSource()==btnLogin) {
				JSONObject object = new JSONObject();
				object.put("status", "signin");
				object.put("username", tfID.getText());
				object.put("password", tfPassword.getText());
				client.sendMessage(object.toJSONString());
			}
			else if(e.getSource()==btnClose) {
				JSONObject object = new JSONObject();
				object.put("status", "register");
				object.put("username", tfID.getText());
				object.put("password", tfPassword.getText());
				object.put("age", 18);
				object.put("fullname", "Nguyễn Bá Khải");
				object.put("avatarurl", "http://localhost:8080/BTLCNPM/gif.gif");
				client.sendMessage(object.toJSONString());
			}
	}
	public static void main(String[] args) throws URISyntaxException {
		new LoginBox();
	}
}
