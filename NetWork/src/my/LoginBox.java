package my;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class LoginBox {
	public JPanel panel1,panel2,panel3,panel4,panel5;
	public JComboBox room;
	public DefaultComboBoxModel model;
	public DataBase data;
	public JCheckBox remember;
	public JLabel user,rememberLabel,roomlabel;
	public JLabel pass;
	public JFrame frame;
	public JButton Login;
	public JButton Register;
	public JButton Quit;
	public JTextField username;
	public JPasswordField password;
	public LoginBox(){
		try {
			ObjectInputStream in = new ObjectInputStream(new FileInputStream("login.dat"));
			data = (DataBase) in.readObject();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			data= new DataBase();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		panel1 = new JPanel();panel2 = new JPanel();panel3 = new JPanel();panel4 = new JPanel();panel5 = new JPanel();
		room = new JComboBox();
		roomlabel = new JLabel("Select Room Chat");
		model = new DefaultComboBoxModel();
		model.addElement("SOICT");
		model.addElement("SAMI");
		model.addElement("SEP");
		model.addElement("OTHER");
		room.setModel(model);
		rememberLabel = new JLabel("Remember my password");
		remember = new JCheckBox();
		frame = new JFrame("Login");
		Login = new JButton("Login");
		Register = new JButton("Register");
		Quit = new JButton("Quit");
		user = new JLabel("Username");
		pass = new JLabel("Password");
		username = new JTextField(15);
		password = new JPasswordField(15);
		frame.setResizable(false);
		frame.setSize(300, 230);
		frame.setLocation(350,300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayout(new FlowLayout());
		panel1.add(user);
		panel1.add(username);
		panel4.add(pass);
		panel4.add(password);
		panel2.add(roomlabel);
		panel2.add(room);
		panel5.add(remember);
		panel5.add(rememberLabel);
		panel3.add(Login);
		panel3.add(Register);
		panel3.add(Quit);
		frame.add(panel1);frame.add(panel4);frame.add(panel5);frame.add(panel2);frame.add(panel3);
		username.getDocument().addDocumentListener(new DocumentListener(){

			@Override
			public void changedUpdate(DocumentEvent arg0) {
				
				
			}

			@Override
			public void insertUpdate(DocumentEvent arg0) {
				String a =data.pass(username.getText());
				if(a!=null){
					password.setText(a);
				}
				
				
			}

			@Override
			public void removeUpdate(DocumentEvent arg0) {
				
				String a =data.pass(username.getText());
				if(a!=null){
					password.setText(a);
				}
				
				
			}
			
		});
		frame.setVisible(true);
	}
	public void save() throws IOException{
		File a = new File("login.dat");
		if(!a.exists()) a.createNewFile();
		try {
			ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("login.dat"));
			out.writeObject(data);
			out.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static void main(String[] args){
		LoginBox a = new LoginBox();
		
	}
}
