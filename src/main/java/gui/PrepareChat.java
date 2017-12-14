package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import repository.UserRepository;
import websocket.WebsocketClientEndpoint;
import websocket.WebsocketClientEndpoint.MessageHandler;

public class PrepareChat extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel LeftPanel;
	private JPanel RightPanel;
	private JPanel LTopPanel;
	private JPanel DisplayPanel;
	private JPanel LButtomPanel;
	private JLabel AvatarLabel;
	private JPanel InfoPanel;
	private JLabel nameLabel, ageLabel, fullnameLabel, uniLabel;
	private JPanel roomPanel;
	private Integer numberOfRoom;
	private List<JSONObject> listRoom;
	private JSONObject userjson;
	private WebsocketClientEndpoint client;
	private ChatBox chatbox;
	private JButton changeAvatar, changePassword, logout;
	private List<JLabel> onlineNumberLabel;
	private PrepareChat prepareChat;
	private ChangePassBox changePassBox;

	public PrepareChat(WebsocketClientEndpoint client) {
		this.client = client;
		this.prepareChat = this;
		onlineNumberLabel = new LinkedList<JLabel>();
		prepareGUI();
		handMessage();

	}

	private void handMessage() {
		client.addMessageHandler(new MessageHandler() {

			public void handleMessage(String message) {
				try {
					JSONParser parser = new JSONParser();
					JSONObject jsobject = (JSONObject) parser.parse(message);
					String status = (String) jsobject.get("status");
					if (status.equals("update")) {
						JSONArray array = (JSONArray) jsobject.get("message");
						for (int i = 0; i < array.size(); i++) {
							JSONObject ob = (JSONObject) array.get(i);
							onlineNumberLabel.get(i).setText("Online Quantity:   " + ob.get("number"));
						}
					} else if(status.equals("avatar")) {
						String mess = (String) jsobject.get("message");
						if(mess.equals("OK")) {
							 final String url = (String) jsobject.get("avatarurl");
							try {
								BufferedImage img = ImageIO.read(new URL(url));
								ImageIcon icon = new ImageIcon(img.getScaledInstance(120, 120, Image.SCALE_SMOOTH));
								AvatarLabel.setIcon(icon);
								JOptionPane.showMessageDialog(AvatarLabel, "Đổi avatar thành công!");
							} catch (MalformedURLException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (IOException d) {
								JOptionPane.showMessageDialog(AvatarLabel, "Khởi động lại để hoàn tất");
							}
							
							
						}
						else {
							JOptionPane.showMessageDialog(AvatarLabel, "Đổi avatar không thành công!");
						}
					} else if(status.equals("changepassword")) {
						String mess = (String) jsobject.get("message");
						if(mess.equals("OKE")) {
							JOptionPane.showMessageDialog(AvatarLabel, "Đổi mật khẩu thành công!");
						}
						else if (mess.equals("WRONG")) {
							JOptionPane.showMessageDialog(AvatarLabel, "Mật khẩu cũ không đúng");
						}
		                else if (mess.equals("NO")) {
		                	JOptionPane.showMessageDialog(AvatarLabel, "Lỗi server - Vui lòng thử lại sau");
		                }
					}
					
				} catch (org.json.simple.parser.ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		});
	}

	private void prepareGUI() {
		this.setLayout(new GridLayout(0, 2));
		LeftPanel = new JPanel(new BorderLayout());
		LTopPanel = new JPanel(new BorderLayout());
		RightPanel = new JPanel();

		// LEFT
		// Profile
		DisplayPanel = new JPanel(new BorderLayout());
		AvatarLabel = new JLabel();
		AvatarLabel.setBorder(BorderFactory.createRaisedSoftBevelBorder());
		InfoPanel = new JPanel(new GridLayout(4, 2));

		// info
		JLabel lb1 = new JLabel("  Nick Name: ");
		nameLabel = new JLabel();
		JLabel lb2 = new JLabel("  Age: ");
		ageLabel = new JLabel();
		JLabel lb3 = new JLabel("  Fullname: ");
		fullnameLabel = new JLabel();
		JLabel lb4 = new JLabel("  College: ");
		uniLabel = new JLabel();

		InfoPanel.add(lb1);
		InfoPanel.add(nameLabel);
		InfoPanel.add(lb2);
		InfoPanel.add(ageLabel);
		InfoPanel.add(lb3);
		InfoPanel.add(fullnameLabel);
		InfoPanel.add(lb4);
		InfoPanel.add(uniLabel);
		InfoPanel.setBorder(BorderFactory.createLineBorder(Color.CYAN));

		DisplayPanel.add(AvatarLabel, BorderLayout.LINE_START);
		DisplayPanel.add(InfoPanel, BorderLayout.CENTER);
		DisplayPanel.setBorder(new TitledBorder("Profile"));
		// setting buttom
		LButtomPanel = new JPanel();

		changeAvatar = new JButton("Change Avatar");
		changePassword = new JButton("Change Password");
		logout = new JButton("Logout");
		LButtomPanel.add(changeAvatar);
		LButtomPanel.add(changePassword);
		LButtomPanel.add(logout);
		
		changeAvatar.addActionListener(new ActionListener() {

			@SuppressWarnings({ "unchecked", "restriction" })
			public void actionPerformed(ActionEvent e) {
				JFileChooser chooser = new JFileChooser("E:\\Java\\BTLCNPM\\src\\main\\webapp\\image");
				int select = chooser.showOpenDialog(LButtomPanel);
				if (select == JFileChooser.APPROVE_OPTION) {
					File file = chooser.getSelectedFile();
					try {
					
						byte[] bytes = Files.readAllBytes(Paths.get(file.getAbsolutePath()));
						String str = UserRepository.encoder.encode(bytes);
						JSONObject object = new JSONObject();
						object.put("status", "avatar");
						object.put("name", file.getName());
						object.put("image", str);
						client.sendMessage(object.toJSONString());
						
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}

			}
		});
		
		/*
		 * Change Password Event
		 */
		changePassword.addActionListener(new ActionListener() {
			@SuppressWarnings({ "unchecked", "restriction" })
			public void actionPerformed(ActionEvent arg0) {
				changePassBox = new ChangePassBox();
				changePassBox.setVisible(true);
				
				JButton btnChangePass = changePassBox.getBtnChangePass();
				JButton btnCancel = changePassBox.getBtnCancel();
				
				btnChangePass.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						String oldPass = new String(changePassBox.getTfOldPass().getPassword());
						String newPass = new String(changePassBox.getTfNewPass().getPassword());
						String reNewPass = new String(changePassBox.getTfReNewPass().getPassword());
						
						if (oldPass.equals("") || newPass.equals("") || reNewPass.equals("")) {
							JOptionPane.showMessageDialog(new JDialog(), "Các trường dữ liệu không để trống");
							return;
						}
						if (!newPass.equals(reNewPass)) {
							JOptionPane.showMessageDialog(new JDialog(), "Hãy nhập lại mật khẩu mới đúng");
							return;
						}
						JSONObject object = new JSONObject();
						object.put("status", "changepassword");
						object.put("oldpassword", oldPass);
						object.put("password", newPass);
						client.sendMessage(object.toJSONString());
						changePassBox.setVisible(false);
					}
				});
				
				btnCancel.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						changePassBox.setVisible(false);
					}
				});
				
			}
		});

		// logout
		logout.addActionListener(new ActionListener() {

			@SuppressWarnings("unchecked")
			public void actionPerformed(ActionEvent e) {
				JSONObject ob = new JSONObject();
				ob.put("status", "signout");
				client.sendMessage(ob.toJSONString());
				if (chatbox != null) {
					chatbox.dispose();
				}

				dispose();
				try {
					client.getUserSession().close();
					new LoginBox();
				} catch (URISyntaxException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

			}
		});

		// set up
		LTopPanel.add(DisplayPanel, BorderLayout.PAGE_START);
		LeftPanel.add(LTopPanel, BorderLayout.NORTH);
		LeftPanel.add(LButtomPanel, BorderLayout.SOUTH);

		Border loweredetched = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
		Border raisedbevel = BorderFactory.createRaisedBevelBorder();
		LeftPanel.setBorder(BorderFactory.createCompoundBorder(loweredetched, raisedbevel));

		// Right
		roomPanel = new JPanel(new FlowLayout());

		JScrollPane scrollPane = new JScrollPane(RightPanel);
		scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

		RightPanel.add(roomPanel);
		RightPanel.setBorder(BorderFactory.createCompoundBorder(loweredetched, raisedbevel));

		// add left and right
		add(LeftPanel);
		add(scrollPane);
	}

	public void doShow() {

		// profile

		// User
		// avatar
		BufferedImage imgicon = null;
		try {
			imgicon = ImageIO.read(new URL((String) userjson.get("avatarurl")));
			Image img = imgicon.getScaledInstance(120, 120, Image.SCALE_SMOOTH);
			ImageIcon icon = new ImageIcon(img);
			AvatarLabel.setIcon(icon);

		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		} catch (IOException e) {
			
		}

		nameLabel.setText(userjson.get("username") + "");
		ageLabel.setText(userjson.get("age") + "");
		fullnameLabel.setText(userjson.get("fullname") + "");
		uniLabel.setText("Workless");

		// Room
		setNumberOfRoom(listRoom.size());
		int index = 0;
		for (JSONObject jsonObject : listRoom) {
			roomPanel.add(createRoomPanel(jsonObject, index++), BorderLayout.BEFORE_FIRST_LINE);
		}

		roomPanel.setPreferredSize(new Dimension(361, 100 * numberOfRoom));

		this.setTitle("Profile and Choose Room");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setSize(800, 460);
		this.setResizable(false);
		this.setVisible(true);
	}

	private JPanel createRoomPanel(JSONObject room, int index) {
		JPanel panel = new JPanel(new BorderLayout());
		BufferedImage imgicon = null;
		ImageIcon icon = null;
		final long roomindex = (Long) room.get("id");
		final String rName = (String) room.get("name");
		try {
			imgicon = ImageIO.read(new URL((String) room.get("logourl")));
			Image img = imgicon.getScaledInstance(70, 70, Image.SCALE_AREA_AVERAGING);
			icon = new ImageIcon(img);

		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		panel.setPreferredSize(new Dimension(360, 95));
		JButton imgButton = new JButton(icon);
		imgButton.setPreferredSize(new Dimension(80, 80));
		imgButton.setBorder(BorderFactory.createEtchedBorder());
		
		imgButton.addActionListener(new ActionListener() {
			@SuppressWarnings("unchecked")
			public void actionPerformed(ActionEvent e) {
				if (chatbox != null) {
					if (chatbox.getRoomName().equals(rName)) {
						JOptionPane.showMessageDialog(DisplayPanel, "Đã ở phòng này rồi");
						return;
					} else {
						chatbox.setRoomName(rName);
						chatbox.setChatContent(new StringBuilder(""));
						chatbox.getChatPane().setText("");
						chatbox.setTitle(rName);
						JSONObject ob = new JSONObject();
						ob.put("status", "changeroom");
						ob.put("room", roomindex - 1);
						client.sendMessage(ob.toJSONString());
						return;
					}

				}
				chatbox = new ChatBox(prepareChat, client, onlineNumberLabel, rName);
				JSONObject ob = new JSONObject();
				ob.put("status", "changeroom");
				ob.put("room", roomindex - 1);
				client.sendMessage(ob.toJSONString());

			}
		});
		JPanel infoPanel = new JPanel(new GridLayout(2, 1));
		String roomname = room.get("name").toString();
		if (roomname.length() < 40) {
			for (int i = 40 - roomname.length(); i > 0; i--)
				roomname += " ";
		} else {
			roomname = room.get("name").toString();
		}
		JLabel roomName = new JLabel("Room Name:      " + roomname);
		onlineNumberLabel.add(index, new JLabel("Online Quantity:   " + room.get("numberonline")));
		infoPanel.add(roomName);
		infoPanel.add(onlineNumberLabel.get(index));
		infoPanel.setBounds(80, 80, 200, 80);

		panel.add(imgButton, BorderLayout.LINE_START);
		panel.add(infoPanel, BorderLayout.EAST);
		panel.setBorder(BorderFactory.createEtchedBorder());

		return panel;
	}

	public JPanel getLeftPanel() {
		return LeftPanel;
	}

	public void setLeftPanel(JPanel leftPanel) {
		LeftPanel = leftPanel;
	}

	public JPanel getRightPanel() {
		return RightPanel;
	}

	public void setRightPanel(JPanel rightPanel) {
		RightPanel = rightPanel;
	}

	public JPanel getLTopPanel() {
		return LTopPanel;
	}

	public void setLTopPanel(JPanel lTopPanel) {
		LTopPanel = lTopPanel;
	}

	public JPanel getDisplayPanel() {
		return DisplayPanel;
	}

	public void setDisplayPanel(JPanel displayPanel) {
		DisplayPanel = displayPanel;
	}

	public JPanel getLButtomPanel() {
		return LButtomPanel;
	}

	public void setLButtomPanel(JPanel lButtomPanel) {
		LButtomPanel = lButtomPanel;
	}

	public JLabel getAvatarLabel() {
		return AvatarLabel;
	}

	public void setAvatarLabel(JLabel avatarLabel) {
		AvatarLabel = avatarLabel;
	}

	public JPanel getInfoPanel() {
		return InfoPanel;
	}

	public void setInfoPanel(JPanel infoPanel) {
		InfoPanel = infoPanel;
	}

	public JLabel getNameLabel() {
		return nameLabel;
	}

	public JButton getLogout() {
		return logout;
	}

	public void setLogout(JButton logout) {
		this.logout = logout;
	}

	public List<JLabel> getOnlineNumberLabel() {
		return onlineNumberLabel;
	}

	public void setOnlineNumberLabel(List<JLabel> onlineNumberLabel) {
		this.onlineNumberLabel = onlineNumberLabel;
	}

	public void setNameLabel(JLabel nameLabel) {
		this.nameLabel = nameLabel;
	}

	public JLabel getAgeLabel() {
		return ageLabel;
	}

	public void setAgeLabel(JLabel ageLabel) {
		this.ageLabel = ageLabel;
	}

	public JLabel getFullnameLabel() {
		return fullnameLabel;
	}

	public void setFullnameLabel(JLabel fullnameLabel) {
		this.fullnameLabel = fullnameLabel;
	}

	public JLabel getUniLabel() {
		return uniLabel;
	}

	public void setUniLabel(JLabel uniLabel) {
		this.uniLabel = uniLabel;
	}

	public JPanel getRoomPanel() {
		return roomPanel;
	}

	public void setRoomPanel(JPanel roomPanel) {
		this.roomPanel = roomPanel;
	}

	public Integer getNumberOfRoom() {
		return numberOfRoom;
	}

	public void setNumberOfRoom(Integer numberOfRoom) {
		this.numberOfRoom = numberOfRoom;
	}

	public List<JSONObject> getListRoom() {
		return listRoom;
	}

	public void setListRoom(List<JSONObject> listRoom) {
		this.listRoom = listRoom;
	}

	public JSONObject getUserjson() {
		return userjson;
	}

	public void setUserjson(JSONObject userjson) {
		this.userjson = userjson;
	}

	public WebsocketClientEndpoint getClient() {
		return client;
	}

	public void setClient(WebsocketClientEndpoint client) {
		this.client = client;
	}

	public ChatBox getChatbox() {
		return chatbox;
	}

	public void setChatbox(ChatBox chatbox) {
		this.chatbox = chatbox;
	}

	public JButton getChangeAvatar() {
		return changeAvatar;
	}

	public void setChangeAvatar(JButton changeAvatar) {
		this.changeAvatar = changeAvatar;
	}

	public JButton getChangePassword() {
		return changePassword;
	}

	public void setChangePassword(JButton changePassword) {
		this.changePassword = changePassword;
	}

	public PrepareChat getPrepareChat() {
		return prepareChat;
	}

	public void setPrepareChat(PrepareChat prepareChat) {
		this.prepareChat = prepareChat;
	}

}
