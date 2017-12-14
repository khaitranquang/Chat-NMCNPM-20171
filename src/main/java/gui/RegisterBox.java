/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import org.json.simple.JSONObject;

import websocket.WebsocketClientEndpoint;

/**
 *
 * @author Tuan Anh
 */
public class RegisterBox extends JDialog implements ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * @param args
	 *            the command line arguments
	 */
	private JLabel lbUsername;
	private JTextField tfUsername;
	private JLabel lbPass;
	private JPasswordField password;
	private JButton btnSingUP;
	private JButton btnCancel;
	private JLabel lbTitle;
	private JLabel lbAge;
	private JTextField textAge;
	private JLabel lbFullName;
	private JTextField textFullName;
	private JPasswordField confirmPass;
	private JLabel lbConfirmPass;
	private WebsocketClientEndpoint client;
	private LoginBox loginBox;

	public RegisterBox(WebsocketClientEndpoint client, LoginBox login) {
		this.loginBox = login;
		this.client = client;
		this.addWindowListener(new WindowAdapter() {

			@Override
			public void windowClosing(WindowEvent e) {
				loginBox.setEnabled(true);
			}
			
		});
		this.add(createPanelTitle(), BorderLayout.NORTH);
		this.add(createPanelLogin(), BorderLayout.CENTER);
		this.add(createButtonPanel(), BorderLayout.SOUTH);

		this.setTitle("Ðăng kí");
		// this.setVisible(true);
		this.setLocation(400, 200);
		this.setResizable(false);

		pack();

		setVisible(true);
	}

	public JPanel createPanelLogin() {
		JPanel panel = new JPanel();
		lbUsername = new JLabel("Username");
		
		lbPass = new JLabel("Password");
	
		lbAge = new JLabel("Age");
		lbFullName = new JLabel("Full Name");

		lbConfirmPass = new JLabel("Confirm Pass");
		

		tfUsername = new JTextField(10);
		password = new JPasswordField(10);

		
		textAge = new JTextField(10);
		
		textFullName = new JTextField(10);
		
		confirmPass = new JPasswordField(10);
	

		panel.setLayout(new GridLayout(5, 2, 5, 5));
		panel.add(lbFullName);
		panel.add(textFullName);
		panel.add(lbAge);
		panel.add(textAge);
		panel.add(lbUsername);
		panel.add(tfUsername);
		panel.add(lbPass);
		panel.add(password);
		panel.add(lbConfirmPass);
		panel.add(confirmPass);

		panel.setBorder(new EmptyBorder(2, 2, 2, 20));
		return panel;
	}

	public JPanel createPanelTitle() {
		JPanel panel = new JPanel();
		lbTitle = new JLabel("Ðăng kí");
		lbTitle.setHorizontalAlignment(SwingConstants.CENTER);
		panel.add(lbTitle, BorderLayout.SOUTH);
		return panel;
	}

	public JPanel createButtonPanel() {
		JPanel panel = new JPanel();

		btnSingUP = new JButton("SingUp");
		btnCancel = new JButton("Cancel");
		btnSingUP.addActionListener(this);
		btnCancel.addActionListener(this);
		panel.setLayout(new GridLayout(1, 2, 10, 10));

		panel.add(btnSingUP);
		panel.add(btnCancel);

		panel.setBorder(new EmptyBorder(10, 20, 10, 20));
		return panel;
	}

	@SuppressWarnings({ "unchecked", "deprecation" })
	public void actionPerformed(ActionEvent e) {
		if(e.getSource()==btnCancel) {
			dispose();
			loginBox.setEnabled(true);
		}
		else if (e.getSource()==btnSingUP){
			JSONObject ob = new JSONObject();
			ob.put("status", "signup");
			ob.put("fullname", textFullName.getText());
			ob.put("age", textAge.getText());
			ob.put("username", tfUsername.getText());
			ob.put("password", password.getText());
			client.sendMessage(ob.toJSONString());
		
		}
		
	}

}
