package gui;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import websocket.WebsocketClientEndpoint;
import websocket.WebsocketClientEndpoint.MessageHandler;


public class LoginBox extends JFrame implements ActionListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JTextField tfID;
	private JPasswordField tfPassword;
	private JButton btnLogin,btnSingUp;
	public WebsocketClientEndpoint client;
	private RegisterBox registerBox;
	static JSONParser jsonParser = new JSONParser();
	public LoginBox() throws URISyntaxException {
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(400,300);
		setLocationRelativeTo(null);
		setLayout(new BorderLayout(10,10));
		setTitle("Đăng nhập");
		settingClientSocket();
		
		try {
			setIconImage(ImageIO.read(new URL("http://localhost:8080/BTLCNPM/image/dog.png")));
		} catch (MalformedURLException e) {
			System.out.println("không read đc");
		} catch (IOException e) {
			System.out.println("không read đc");
		}
		
		add(createLabelPanel(),BorderLayout.WEST);
		add(createTextFieldPanel(),BorderLayout.CENTER);
		add(createButtonPanel(),BorderLayout.SOUTH);
		
		pack();
		setVisible(true);
	}

	private void settingClientSocket() throws URISyntaxException {
		client = new WebsocketClientEndpoint(new URI("ws://localhost:8080/BTLCNPM/chatroom"));
		client.addMessageHandler(new MessageHandler() {
			
			public void handleMessage(String message) {
				try {
					JSONObject object = (JSONObject) jsonParser.parse(message);
					String stt = (String) object.get("status");
					if(stt.equals("login")) {
						String mess =(String) object.get("message");
						if(mess.equals("OKE")) {
							@SuppressWarnings("unchecked")
							List<JSONObject> list =(List<JSONObject>) object.get("roomlist");
							PrepareChat preChat = new PrepareChat(client);
							dispose();
							preChat.setListRoom(list);
							preChat.setUserjson((JSONObject) object.get("userjson"));
							preChat.doShow();
							
						}
						else if(mess.equals("ON")) {
							JOptionPane.showMessageDialog(btnLogin, "Đã online!");
						}
						else if(mess.equals("NO")) {
							JOptionPane.showMessageDialog(btnLogin, "Sai tài khoản hoặc mật khẩu!");
						}
					}
					else if(stt.equals("register")) {
						String mess = (String) object.get("message");
						if(mess.equals("OKE")) {
							JOptionPane.showConfirmDialog(btnLogin, "Đăng kí thành công");
							
							setEnabled(true);
						}
						else {
							JOptionPane.showConfirmDialog(btnLogin, "Tên tài khoản đã tồn tại");
						}
					}
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				
			}
		});
	}
	private JPanel createButtonPanel() {
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(1, 3,10,10));
		panel.setBorder(new EmptyBorder(10, 10, 10, 10));
		
		btnLogin    = createButton("Login");
		btnSingUp    = createButton("SignUp");
		panel.add(btnLogin);
		panel.add(btnSingUp);
		
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
	
	public JTextField getTfID() {
		return tfID;
	}

	public void setTfID(JTextField tfID) {
		this.tfID = tfID;
	}

	public JPasswordField getTfPassword() {
		return tfPassword;
	}

	public void setTfPassword(JPasswordField tfPassword) {
		this.tfPassword = tfPassword;
	}

	public JButton getBtnLogin() {
		return btnLogin;
	}

	public void setBtnLogin(JButton btnLogin) {
		this.btnLogin = btnLogin;
	}

	public JButton getBtnSingUp() {
		return btnSingUp;
	}

	public void setBtnSingUp(JButton btnSingUp) {
		this.btnSingUp = btnSingUp;
	}

	public WebsocketClientEndpoint getClient() {
		return client;
	}

	public void setClient(WebsocketClientEndpoint client) {
		this.client = client;
	}

	public RegisterBox getRegisterBox() {
		return registerBox;
	}

	public void setRegisterBox(RegisterBox registerBox) {
		this.registerBox = registerBox;
	}

	public static JSONParser getJsonParser() {
		return jsonParser;
	}

	public static void setJsonParser(JSONParser jsonParser) {
		LoginBox.jsonParser = jsonParser;
	}

	@SuppressWarnings({ "unchecked", "deprecation" })
	public void actionPerformed(ActionEvent e) {
		if(e.getSource()==btnLogin) {
			JSONObject object = new JSONObject();
			object.put("status", "signin");
			object.put("username", tfID.getText());
			object.put("password", tfPassword.getText());
			client.sendMessage(object.toJSONString());
		}
		else if(e.getSource()==btnSingUp) {
	            setEnabled(false);
	            registerBox= new RegisterBox(client,this);
			
		}
}

	public static void main(String[] args) throws URISyntaxException {
		new LoginBox();
	}
}
