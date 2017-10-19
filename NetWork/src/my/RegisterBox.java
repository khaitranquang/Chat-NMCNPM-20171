package my;

import java.awt.FlowLayout;
import java.awt.ScrollPane;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.*;

public class RegisterBox {
	public JFrame frame;
	public JLabel user,pass,passagain;
	public JButton Register,Quit;
	public JTextField username,password,passwordagain;
	public RegisterBox(){
		frame = new JFrame("Register");
		frame.setSize(300, 200);
		frame.setLocation(350, 300);
		user = new JLabel("Username");
		pass = new JLabel("Password");
		passagain = new JLabel("Retype PassWord");
		Register = new JButton("Register");
		Quit = new JButton("Quit");
		username = new JTextField(15);
		password = new JTextField(15);
		passwordagain = new JTextField(15);
		frame.setLayout(new FlowLayout());
		frame.add(user);
		frame.add(username);
		frame.add(pass);
		frame.add(password);
		frame.add(passagain);
		frame.add(passwordagain);
		frame.add(Register);
		frame.add(Quit);
		frame.setResizable(false);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	public static void main(String[] args){
		RegisterBox a = new RegisterBox();
	
		a.frame.setVisible(true);	
	}
}
