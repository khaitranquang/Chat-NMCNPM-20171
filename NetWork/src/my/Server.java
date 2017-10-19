package my;

import java.awt.FlowLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Hashtable;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

public class Server extends JFrame{
	public Connection conn;
	public Statement stmt;
	public ImageIcon logo;
	public ArrayList<Icon> iconlist;
//	public DataBase data;
	public ServerSocket svCtrSocket,svDataSocket;
	public ArrayList<ClientThread> SOICT,SAMI,SEP,OTHER;
	public Server() throws IOException, ClassNotFoundException{
		try{
			conn= DriverManager.getConnection(
						"jdbc:mysql://localhost:3306/mydb?autoReconnect=true&useSSL=true", "root", "khailinh1997");
			stmt=  conn.createStatement();
		}
		catch(Exception e){
			JOptionPane.showMessageDialog(null, "Database is not connected");
			System.exit(0);
		}
		iconlist = new ArrayList<Icon>();
		BufferedImage image = ImageIO.read(new File("E:\\Java\\NetWork\\image\\dog.png"));
		iconlist.add(new Icon("dog",new ImageIcon(image.getScaledInstance(50, 50, image.SCALE_SMOOTH))));
		image = ImageIO.read(new File("E:\\Java\\NetWork\\image\\muggy.jpg"));
		iconlist.add(new Icon("muggy",new ImageIcon(image.getScaledInstance(50, 50, image.SCALE_SMOOTH))));
		image = ImageIO.read(new File("E:\\Java\\NetWork\\image\\muggykhoc.jpg"));
		iconlist.add(new Icon("muggykhoc",new ImageIcon(image.getScaledInstance(50, 50, image.SCALE_SMOOTH))));
		image = ImageIO.read(new File("E:\\Java\\NetWork\\image\\muggyhun.jpg"));
		iconlist.add(new Icon("muggyhun",new ImageIcon(image.getScaledInstance(50, 50, image.SCALE_SMOOTH))));
		image = ImageIO.read(new File("E:\\Java\\NetWork\\image\\dogbua.jpg"));
		iconlist.add(new Icon("dogbua",new ImageIcon(image.getScaledInstance(50, 50, image.SCALE_SMOOTH))));
		image = ImageIO.read(new File("E:\\Java\\NetWork\\image\\dogchaitoc.jpg"));
		iconlist.add(new Icon("dogdien",new ImageIcon(image.getScaledInstance(50, 50, image.SCALE_SMOOTH))));
		image = ImageIO.read(new File("E:\\Java\\NetWork\\image\\dogcry.jpg"));
		iconlist.add(new Icon("dogcry",new ImageIcon(image.getScaledInstance(50, 50, image.SCALE_SMOOTH))));
		image = ImageIO.read(new File("E:\\Java\\NetWork\\image\\sharingan.jpg"));
		logo = new ImageIcon(image.getScaledInstance(30, 30, image.SCALE_SMOOTH));
		this.setLayout(new FlowLayout());
		this.setSize(200,80);
		JTextField temp=new JTextField(10);
		temp.setEditable(false);
		temp.setText("Server is ready!");
		this.add(temp);
		this.setLocation(350,300);
		this.setVisible(true);
//		try{ObjectInputStream in = new ObjectInputStream(new FileInputStream("data.dat"));
//		data = (DataBase) in.readObject();
//		in.close();}
//		catch(Exception e){
//			data = new DataBase();
//		}
//		data.add("test", "test");
		svCtrSocket = new ServerSocket(2000);
		svDataSocket = new ServerSocket(2001);
		SOICT = new ArrayList<ClientThread>();
		SAMI = new ArrayList<ClientThread>();
		SEP = new ArrayList<ClientThread>();
		OTHER = new ArrayList<ClientThread>();
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.addWindowListener(new WindowAdapter(){
			public void	windowClosing(WindowEvent e){
				try {
					
//					File a = new File("data.dat");
//					if(!a.exists()) a.createNewFile();
// 					ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("data.dat"));
//					out.writeObject(data);
//					out.close();
					disconnectAll(SOICT);
					disconnectAll(SAMI);
					disconnectAll(SEP);
					disconnectAll(OTHER);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		while(true){
			Socket temp1 = svCtrSocket.accept();
			Socket temp2 = svDataSocket.accept();
			ClientThread temp3 = new ClientThread(temp1,temp2,this);
			temp3.start();
		}
	}
	public void sendALL(String temp,ArrayList<ClientThread> list) throws IOException{
		for(int i=0;i<list.size();i++){
			list.get(i).send(temp);
		}
	}
	public String updateMessage(ArrayList<ClientThread> list){
		StringBuilder a = new StringBuilder();
		a.append(list.size());
		for(int i=0;i<list.size();i++){
			a.append("\n"+list.get(i).name);
		}
		return a.toString();
	}
	public void sendUpdate(ArrayList<ClientThread> list) throws IOException{
		for(int i=0;i<list.size();i++){
			list.get(i).sendUp(updateMessage(list));
		}
	}
	public void disconnectAll(ArrayList<ClientThread> list) throws IOException{
		for(int i=0;i<list.size();i++){
			list.get(i).disconnect();
		}
	}
	public void sendAllFile(String temp,String filename,byte[] e,ArrayList<ClientThread> list,long leng) throws IOException{
		for(int i=0;i<list.size();i++){
			if(list.get(i).name.compareTo(temp)!=0){
				list.get(i).sendFile(temp,filename,e,leng);
			}
			
		}
	}
	public void sendALLIcon(String name,int index,ArrayList<ClientThread> list) throws IOException{
		for(int i=0;i<list.size();i++){
			list.get(i).sendIcon(name, index);
		}
	}
	public static void main(String[] args) throws IOException, ClassNotFoundException{
		Server a = new Server();
	}
	public String add(String username,String password){
		return "insert into Nick(username,password) values ('"+username+"','"+password+"');";
	}
	public String getPass(String username){
		return "select password from Nick where username='"+username+"';";
	}
	public String getUser(String username){
		return "select username from Nick where username='"+username+"';";
	}
}
