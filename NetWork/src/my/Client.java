package my;

import java.awt.AWTException;
import java.awt.Component;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JFileChooser;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JTextPane;
import javax.swing.ListCellRenderer;
import javax.swing.Timer;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class Client {
	public String use;
	public TrayIcon temp2;
	public ImageIcon icon;
	public SystemTray sys;
	public static String cp;
	public final int OPEN=1,SAVE=2,READ=3,RECIEVE=4;
	public final String QUIT="0",LOGIN="1",REGISTER="2",SEND="3",UPDATE="4",LOGOUT="5"
			,ACCEPT="OK",NOT="NOT",QUIT_AND_CLOSE="6",FILE="7",DENY="DENY",ICON="8",SENDICON="9"
			,CHANGEROOM="10",SYS="11";
	public Thread read;
	public boolean status;
	public String name;
	public Socket ctrSoc;
	public Socket dataSoc;
	public LoginBox lg;
	public ChatBox cb;
	public DataInputStream crIn,dtIn;
	public DataOutputStream crOut,dtOut;
	public Client() throws UnknownHostException, IOException, ClassNotFoundException, AWTException{
		InetAddress add= InetAddress.getByName("localhost");
		ctrSoc = new Socket(add,2000);
		dataSoc = new Socket(add,2001);
		ObjectInputStream in = new ObjectInputStream(dataSoc.getInputStream());
		icon=(ImageIcon) in.readObject();
		crIn = new DataInputStream(ctrSoc.getInputStream());
		dtIn = new DataInputStream(dataSoc.getInputStream());
		crOut = new DataOutputStream(ctrSoc.getOutputStream());
		dtOut = new DataOutputStream(dataSoc.getOutputStream());
		sys=SystemTray.getSystemTray();
		lg = new LoginBox();
		lg.frame.setIconImage(icon.getImage());
		cb = new ChatBox();
		cb.frame.setIconImage(icon.getImage());	
		PopupMenu menutemp = new PopupMenu();
		MenuItem Logout = new MenuItem("Logout");MenuItem Quit = new MenuItem("Quit");
		menutemp.add(Logout);menutemp.add(Quit);
		temp2 = new TrayIcon(icon.getImage(),"ChatRoom by NBK");
		temp2.setImageAutoSize(true);
		temp2.setPopupMenu(menutemp);
		sys.add(temp2);
		Logout.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					status=false;
					lg.save();
					crOut.writeUTF(QUIT_AND_CLOSE);
					crOut.flush();
					read.stop();
					crOut.close();
					crIn.close();
					dtOut.close();
					dtIn.close();
					ctrSoc.close();
					dataSoc.close();
					lg.frame.dispose();
					cb.frame.dispose();
					
					Client b = new Client();
					
					
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (ClassNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (AWTException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
			}
			
		});
		Quit.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				exit();
				
			}
			
		});
		lg.Login.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				try {
					login();
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
			
		});
		lg.Register.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				lg.frame.setVisible(false);
				RegisterBox temp = new RegisterBox();
				temp.username.addKeyListener(new KeyAdapter(){
					public void keyPressed(KeyEvent e){
						if(e.getKeyCode()==KeyEvent.VK_ENTER){
							temp.Register.doClick();
						}
					}
				});
				temp.passwordagain.addKeyListener(new KeyAdapter(){
					public void keyPressed(KeyEvent e){
						if(e.getKeyCode()==KeyEvent.VK_ENTER){
							temp.Register.doClick();
						}
					}
				});
				temp.password.addKeyListener(new KeyAdapter(){
					public void keyPressed(KeyEvent e){
						if(e.getKeyCode()==KeyEvent.VK_ENTER){
							temp.Register.doClick();
						}
					}
				});
				temp.Register.addActionListener(new ActionListener(){

					@Override
					public void actionPerformed(ActionEvent e) {
						try {
							String pass1 = temp.password.getText();
							String pass2 = temp.passwordagain.getText();
							if(pass1.compareTo("")==0||pass2.compareTo("")==0||temp.username.getText().compareTo("")==0){
								JOptionPane.showMessageDialog(null, "Hay nhap day du thong tin!");
							}
							else if(pass1.compareTo(pass2)==0){
									crOut.writeUTF(REGISTER);
									crOut.flush();
									dtOut.writeUTF(temp.username.getText());
									dtOut.flush();
									dtOut.writeUTF(temp.password.getText());
									dtOut.flush();
									String temp1 = crIn.readUTF();
									if(temp1.compareTo(ACCEPT)==0){
										lg.frame.setVisible(true);
										temp.frame.dispose();
										lg.username.setText(temp.username.getText());
										lg.password.setText(temp.password.getText());
									}
									else{
										JOptionPane.showMessageDialog(null, "Tai khoan da ton tai!");
									}
								}
								else{
									JOptionPane.showMessageDialog(null, "Mat khau khong trung nhau!");
								}
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							JOptionPane.showMessageDialog(null, "Server is Offline!");
						}
					}
					
				});
			    temp.Quit.addActionListener(new ActionListener(){

					@Override
					public void actionPerformed(ActionEvent arg0) {
						temp.frame.dispose();
						lg.frame.setVisible(true);
					}
			    	
			    });
			}
			
		});
		lg.Quit.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				try {
					lg.save();
					crOut.writeUTF(QUIT);
					crOut.flush();
					crIn.close();
					crOut.close();
					dtIn.close();
					dtOut.close();
					ctrSoc.close();
					dataSoc.close();
					System.exit(0);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					JOptionPane.showMessageDialog(null, "Server is Offline!");
					System.exit(0);
				}
				
			}
			
		});
		lg.frame.addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent e){
				try {
					lg.save();
					crOut.writeUTF("0");
					crOut.flush();
					crIn.close();
					crOut.close();
					dtIn.close();
					dtOut.close();
					ctrSoc.close();
					dataSoc.close();
					System.exit(0);
				} catch (IOException d) {
					JOptionPane.showMessageDialog(null, "Server is Offline!");
					System.exit(0);
				}
			}
		});
	    cb.Chat.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				chat();
			}
	    	
	    });
	    cb.Logout.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					status=false;
					lg.save();
					crOut.writeUTF(QUIT_AND_CLOSE);
					crOut.flush();
					read.stop();
					crOut.close();
					crIn.close();
					dtOut.close();
					dtIn.close();
					ctrSoc.close();
					dataSoc.close();
					lg.frame.dispose();
					cb.frame.dispose();
					
					Client b = new Client();
					
					
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (ClassNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (AWTException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
			}
	    	
	    });
	    cb.Quit.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				exit();
				
			}
	    	
	    });
	    cb.frame.addWindowListener(new WindowAdapter(){
	    	public void windowClosing(WindowEvent e){
	    		exit();
	    	}
	    });
	    cb.Open.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					setFile("Send a file",OPEN);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
			}
	    	
	    });
	    cb.Saveas.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					setFile("Open a file",READ);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
			}
	    	
	    });
	    cb.Exit.addActionListener(new ActionListener(){
	    	

			@Override
			public void actionPerformed(ActionEvent e) {
				exit();
				
			}
	    });
	    cb.Save.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					setFile("Save a file",SAVE);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
			}
	    	
	    });
	    lg.username.addKeyListener(new KeyAdapter(){
	    	public void keyPressed(KeyEvent e){	
	    		if(e.getKeyCode()==KeyEvent.VK_ENTER){
	    			lg.Login.doClick();
	    		}
	    	}
	    });
	    lg.password.addKeyListener(new KeyAdapter(){
	    	public void keyPressed(KeyEvent e){	
	    		if(e.getKeyCode()==KeyEvent.VK_ENTER){
	    			lg.Login.doClick();
	    		}
	    	}
	    });
	    cb.textField.addKeyListener(new KeyAdapter(){
	    	public void keyPressed(KeyEvent e){
	    		if(e.getKeyCode()==KeyEvent.VK_ENTER){
	    			cb.Chat.doClick();
	    		}
	    	}
	    });
	    
	    cb.iconbox.list.addListSelectionListener(new ListSelectionListener(){

			@Override
			public void valueChanged(ListSelectionEvent e) {
				if(cb.iconbox.list.getSelectedIndex()!=0){
					Icon temp =cb.iconbox.list.getSelectedValue();
					try {
						int a =cb.iconbox.list.getSelectedIndex();
						crOut.writeUTF(SENDICON);
						crOut.flush();
						dtOut.write(cb.iconbox.list.getSelectedIndex());
						dtOut.flush();
						cb.textField.requestFocus();
						cb.iconbox.list.setSelectedIndex(0);
						
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
				
			}
	    	
	    });
	    cb.SOICT.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				changeRoom("SOICT");
			}
	    	
	    });
	    cb.SAMI.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				changeRoom("SAMI");
			}
	    	
	    });
	    cb.SEP.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				changeRoom("SEP");
			}
	    	
	    });
	    cb.OTHER.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				changeRoom("OTHER");
			}
	    	
	    });
	    cb.Copy.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				cp = cb.textArea.getSelectedText();
			}
	    	
	    });
	
	    read = new Thread(new Runnable(){

			@Override
			public void run() {
				while(status){
			    	String stt;
					try {
						stt = crIn.readUTF();
						if(stt.compareTo(SEND)==0){
				    		String temp = dtIn.readUTF();
				    		cb.textArea.append(temp+"\n");
				    	}
						else if(stt.compareTo(UPDATE)==0){
							String temp=dtIn.readUTF();
							cb.online.setText(temp);
						}
						else if(stt.compareTo(QUIT)==0){
							JOptionPane.showMessageDialog(null, "Server is Offline!");
							System.exit(0);
						}
						else if(stt.compareTo(FILE)==0){
							use = dtIn.readUTF();
		
								int a=JOptionPane.showConfirmDialog(cb.textArea, use+" da gui cho ban 1 file! Ban co nhan khong?");
								if(a==JOptionPane.YES_OPTION){
									setFile("Save a file",RECIEVE);	
								}
								else{
									dtIn.readUTF();
									cb.textArea.append("Ban da tu choi 1 file tu "+use+"!\n");
									long leng = dtIn.readLong();
									byte[] re = new byte[(int) leng];
									dtIn.read(re);
								}
							
						}
						else if(stt.compareTo(ICON)==0){
							int leng = dtIn.read();
							cb.iconbox.leng=leng;
							ObjectInputStream in = new ObjectInputStream(dataSoc.getInputStream());
							cb.iconbox.model.addElement(new Icon("nothing",new ImageIcon()));
							for(int i=0;i<leng;i++){
								Icon temp=(Icon) in.readObject();
								cb.iconbox.model.addElement(temp); 
							} 
							
							cb.iconbox.list.setModel(cb.iconbox.model);
						}
						else if(stt.compareTo(SENDICON)==0){
							String name = dtIn.readUTF();
							int index = dtIn.read();
							
									ImageIcon a = cb.iconbox.list.getModel().getElementAt(index).icon;
									JOptionPane s = new JOptionPane(cb.iconbox.list.getModel().getElementAt(index).name);
									s.setIcon(a);
									JDialog dialog = s.createDialog(cb.textArea,name+": ");
									dialog.setSize(170,90);
									Timer timer = new Timer(1000, new ActionListener() {

							            @Override
							            public void actionPerformed(ActionEvent arg0) {
							                dialog.dispose();
							            }

							        });
							        timer.start();
							        dialog.setVisible(true);
							
						}
					} catch (IOException e) {
						// TODO Auto-generated catch block
						
					} catch (ClassNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
			    	
			    }
				
			}
	    	
	    });
	    
	}
	public void changeRoom(String room){
		if(cb.room.compareTo(room)==0){
			JOptionPane.showMessageDialog(cb.textArea, "Ban dang o phong "+room+" roi!");
		}
		else{
			try {
				cb.room=room;
				crOut.writeUTF(CHANGEROOM);
				crOut.flush();
				dtOut.writeUTF(room);
				dtOut.flush();
				cb.frame.setTitle("Nick: "+cb.name+"   Room:"+cb.room);
				cb.textArea.setText("");
				cb.textArea.append("Xin chao!Ban dang o phong "+room+"\n");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	public void exit(){
		try {
			lg.save();
			status = false;
			crOut.writeUTF(QUIT_AND_CLOSE);
			crOut.flush();
			crOut.close();
			crIn.close();
			dtOut.close();
			dtIn.close();
			ctrSoc.close();
			dataSoc.close();
			System.exit(0);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
	public void setFile(String title,int type) throws IOException{
		JFileChooser chooser = new JFileChooser();
		chooser.setDialogTitle(title);
		int select =0;
		if(type==OPEN){
			select = chooser.showOpenDialog(cb.textArea);
		}
		else if(type==SAVE){
			select = chooser.showSaveDialog(cb.textArea);
		}
		else if(type==READ){
			select = chooser.showOpenDialog(cb.textArea);
		}
		else if(type==RECIEVE){
			chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			select = chooser.showSaveDialog(cb.textArea);
		}
		
		if(select==JFileChooser.APPROVE_OPTION){
			File temp = chooser.getSelectedFile();
			if(type==OPEN){
				int a = JOptionPane.showConfirmDialog(cb.textArea, "Ban gui file nay den moi nguoi khong?");
				if(a==JOptionPane.YES_OPTION){
					crOut.writeUTF(FILE);
					crOut.flush();
					DataInputStream in = new DataInputStream(new FileInputStream(temp));
					byte[] send = new byte[(int) temp.length()];
					in.read(send);
					in.close();
					dtOut.writeUTF(temp.getName());
					dtOut.flush();
					dtOut.writeLong((long) temp.length());
					dtOut.flush();
					dtOut.write(send);
					dtOut.flush();
				}
				else{
					setFile("Send a File",OPEN);
				}
				
			}
			else if(type==SAVE){
				int a = JOptionPane.showConfirmDialog(null, "Ban co muon save chatlog khong?");
				if(a==JOptionPane.YES_OPTION){
					BufferedWriter out = new BufferedWriter(new FileWriter(temp));
					out.write(cb.textArea.getText());
					out.close();
				}
				else{
					setFile("Save a File",SAVE);
				}
			}
			else if(type==READ){
				int a = JOptionPane.showConfirmDialog(null, "Ban co muon doc file nay khong?");
				if(a==JOptionPane.YES_OPTION){
					BufferedReader in = new BufferedReader(new FileReader(temp));
					StringBuilder s = new StringBuilder();
					String dd=in.readLine();
					while(dd!=null){
						s.append(dd+"\n");
						dd=in.readLine();
					}
					cb.textField.setText(s.toString());
				}
				else{
					setFile("Open a File",READ);
				}
			}
			else if(type==RECIEVE){
					String filename = dtIn.readUTF();
					long leng = dtIn.readLong();
					byte[] re = new byte[(int) leng];
					dtIn.read(re);
					FileOutputStream out = new FileOutputStream(chooser.getSelectedFile().getPath()+"\\"+filename);
					out.write(re);
					out.close();
					cb.textArea.append("Ban da nhan 1 file tu "+use+" tai: "+chooser.getSelectedFile().getPath()+"\\"+filename+" !\n");
			}
		}
		else{
			if(type==RECIEVE){
				String temp=dtIn.readUTF();
				cb.textArea.append("Ban da tu choi file "+temp+" tu "+use+"!\n");
				long leng = dtIn.readLong();
				byte[] re = new byte[(int) leng];
				dtIn.read(re);
			}
		}
	}
	public void chat(){
		try {
			String temp=cb.textField.getText();
			cb.textField.requestFocus();
			cb.scroll.getVerticalScrollBar().setValue(cb.scroll.getVerticalScrollBar().getMaximum());
			if(temp.compareTo("")!=0){
				crOut.writeUTF(SEND);
				crOut.flush();
				dtOut.writeUTF(name+": "+temp);
				dtOut.flush();
				cb.textField.setText("");
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void login() throws ClassNotFoundException{
		try {
			crOut.writeUTF(LOGIN);
			crOut.flush();
			dtOut.writeUTF(lg.username.getText());
			dtOut.flush();
			dtOut.writeUTF(lg.password.getText());
			dtOut.flush();
			dtOut.writeUTF((String) lg.room.getSelectedItem());
			dtOut.flush();
			String temp = crIn.readUTF();
			if(temp.compareTo(ACCEPT)==0){
				if(lg.remember.isSelected()&&!lg.data.exist(lg.username.getText())){
					lg.data.add(lg.username.getText(), lg.password.getText());
				}
				lg.save();
				cb.room=(String) lg.room.getSelectedItem();
				lg.frame.dispose();
				cb.frame.setVisible(true);
				cb.name=lg.username.getText();
				cb.frame.setTitle("Nick: "+cb.name+"   Room:"+cb.room);
				name=cb.name;
				cb.textArea.append("Xin chao! Ban dang o phong "+cb.room+"\n");
				status=true;
				cb.textField.requestFocus();
				read.start();
				crOut.writeUTF(ICON);
				crOut.flush();
				temp2.setToolTip("ChatRoom by NBK: "+name);
				
			}
			else if(temp.compareTo(NOT)==0){
				JOptionPane.showMessageDialog(null, "Tai khoan hoac mat khau khong chinh xac!");
			}
			else if(temp.compareTo(DENY)==0){
				JOptionPane.showMessageDialog(null, "Tai khoan dang co nguoi su dung!");
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			JOptionPane.showMessageDialog(null, "Server is Offline!");
		}
	}
	public static void main(String[]args) {
		try{
			Client a = new Client();
			
		}
		catch(Exception e){
			JOptionPane.showMessageDialog(null, "Server is not online!");
		}
	}
}
