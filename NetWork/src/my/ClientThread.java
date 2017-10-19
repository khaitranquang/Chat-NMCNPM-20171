package my;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ClientThread extends Thread {
	public final String QUIT = "0", LOGIN = "1", REGISTER = "2", SEND = "3", UPDATE = "4", LOGOUT = "5", ACCEPT = "OK",
			NOT = "NOT", QUIT_AND_CLOSE = "6", DENY = "DENY", FILE = "7", ICON = "8", SENDICON = "9", CHANGEROOM = "10",
			SYS = "11";
	public ArrayList<ClientThread> list;
	public String name;
	public Server sv;
	public Socket ctrSoc, dataSoc;
	public DataInputStream ctrIn, dataIn;
	public DataOutputStream ctrOut, dataOut;

	public ClientThread(Socket temp1, Socket temp2, Server a) throws IOException {
		this.sv = a;
		this.ctrSoc = temp1;
		this.dataSoc = temp2;
		ObjectOutputStream out;
		try {
			out = new ObjectOutputStream(dataSoc.getOutputStream());
			out.writeObject(sv.logo);
			out.flush();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		ctrIn = new DataInputStream(ctrSoc.getInputStream());
		dataIn = new DataInputStream(dataSoc.getInputStream());
		ctrOut = new DataOutputStream(ctrSoc.getOutputStream());
		dataOut = new DataOutputStream(dataSoc.getOutputStream());
	}

	public void run() {
		while (true) {

			String stt;
			try {
				stt = ctrIn.readUTF();
				if (stt.compareTo(QUIT) == 0) {
					ctrIn.close();
					ctrOut.close();
					dataIn.close();
					dataOut.close();
					ctrSoc.close();
					dataSoc.close();
					break;
				} else if (stt.compareTo(LOGIN) == 0) {
					String user = dataIn.readUTF();
					String pass = dataIn.readUTF();
					String room = dataIn.readUTF();
					ResultSet rs = sv.stmt.executeQuery(sv.getPass(user));
					String a = null;
					if (rs.next()) {
						a = rs.getString(1);
					}
					System.out.println(a);
					if (a != null && pass.compareTo(a) == 0) {
						boolean temp = true;
						if (room.compareTo("SOICT") == 0) {
							list = sv.SOICT;
						} else if (room.compareTo("SAMI") == 0) {
							list = sv.SAMI;
						} else if (room.compareTo("SEP") == 0) {
							list = sv.SEP;
						} else if (room.compareTo("OTHER") == 0) {
							list = sv.OTHER;
						}
						for (int i = 0; i < sv.SOICT.size(); i++) {
							if (sv.SOICT.get(i).name.compareTo(user) == 0)
								temp = false;
						}
						for (int i = 0; i < sv.SAMI.size(); i++) {
							if (sv.SAMI.get(i).name.compareTo(user) == 0)
								temp = false;
						}
						for (int i = 0; i < sv.SEP.size(); i++) {
							if (sv.SEP.get(i).name.compareTo(user) == 0)
								temp = false;
						}
						for (int i = 0; i < sv.OTHER.size(); i++) {
							if (sv.OTHER.get(i).name.compareTo(user) == 0)
								temp = false;
						}
						if (temp) {
							name = user;
							ctrOut.writeUTF(ACCEPT);
							ctrOut.flush();
							list.add(this);
							sv.sendALL(user + " is online now!", list);
							sv.sendUpdate(list);
						} else {
							ctrOut.writeUTF(DENY);
							ctrOut.flush();
						}

					} else {
						ctrOut.writeUTF(NOT);
						ctrOut.flush();
					}
				} else if (stt.compareTo(REGISTER) == 0) {
					String user = dataIn.readUTF();
					String pass = dataIn.readUTF();
					ResultSet rs = sv.stmt.executeQuery(sv.getPass(user));
					String a = null;
					if (rs.next()) {
						a = rs.getString(1);
					}
					if (a != null) {
						ctrOut.writeUTF(NOT);
						ctrOut.flush();
					} else {
						try {
							int s = sv.stmt.executeUpdate(sv.add(user, pass));
							ctrOut.writeUTF(ACCEPT);
							ctrOut.flush();
						} catch (Exception e) {
							ctrOut.writeUTF(NOT);
							ctrOut.flush();
						}

					}

				} else if (stt.compareTo(SEND) == 0) {
					String temp = dataIn.readUTF();
					sv.sendALL(temp, list);
				} else if (stt.compareTo(LOGOUT) == 0) {
					try {
						list.remove(this);
						sv.sendALL(name + " is offline!", list);
						sv.sendUpdate(list);
					} catch (Exception e) {

					}

				} else if (stt.compareTo(QUIT_AND_CLOSE) == 0) {
					try {
						list.remove(this);
						sv.sendALL(name + " is offline!", list);
						sv.sendUpdate(list);
						ctrIn.close();
						ctrOut.close();
						dataIn.close();
						dataOut.close();
						ctrSoc.close();
						dataSoc.close();

					} catch (Exception e) {

					}
					break;
				} else if (stt.compareTo(FILE) == 0) {
					String filename = dataIn.readUTF();
					long b = dataIn.readLong();
					byte[] temp = new byte[(int) b];
					dataIn.read(temp);
					sv.sendAllFile(name, filename, temp, list, b);
				} else if (stt.compareTo(ICON) == 0) {
					ctrOut.writeUTF(ICON);
					ctrOut.flush();
					dataOut.write(sv.iconlist.size());
					dataOut.flush();
					ObjectOutputStream out2 = new ObjectOutputStream(dataSoc.getOutputStream());
					for (int i = 0; i < sv.iconlist.size(); i++) {
						out2.writeObject(sv.iconlist.get(i));
						out2.flush();
					}
				} else if (stt.compareTo(SENDICON) == 0) {
					int temp = dataIn.read();
					sv.sendALLIcon(name, temp, list);
				} else if (stt.compareTo(CHANGEROOM) == 0) {
					String room = dataIn.readUTF();
					list.remove(this);
					sv.sendALL(name + " changed to room " + room + " !", list);
					sv.sendUpdate(list);
					if (room.compareTo("SOICT") == 0) {
						list = sv.SOICT;
						list.add(this);
						sv.sendUpdate(list);
						sv.sendALL(name + " joined room", list);
					} else if (room.compareTo("SAMI") == 0) {
						list = sv.SAMI;
						list.add(this);
						sv.sendUpdate(list);
						sv.sendALL(name + " joined room", list);
					} else if (room.compareTo("SEP") == 0) {
						list = sv.SEP;
						list.add(this);
						sv.sendUpdate(list);
						sv.sendALL(name + " joined room", list);
					} else if (room.compareTo("OTHER") == 0) {
						list = sv.OTHER;
						list.add(this);
						sv.sendUpdate(list);
						sv.sendALL(name + " joined room", list);
					}

				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}

	public void send(String temp) throws IOException {
		ctrOut.writeUTF(SEND);
		ctrOut.flush();
		dataOut.writeUTF(temp);
		dataOut.flush();
	}

	public void sendUp(String temp) throws IOException {
		ctrOut.writeUTF(UPDATE);
		ctrOut.flush();
		dataOut.writeUTF(temp);
		dataOut.flush();
	}

	public void sendFile(String name, String filename, byte[] e, long leng) throws IOException {
		ctrOut.writeUTF(FILE);
		ctrOut.flush();
		dataOut.writeUTF(name);
		dataOut.flush();
		dataOut.writeUTF(filename);
		dataOut.flush();
		dataOut.writeLong(leng);
		dataOut.flush();
		dataOut.write(e);
		dataOut.flush();
	}

	public void disconnect() throws IOException {
		ctrOut.writeUTF(QUIT);
		ctrOut.flush();
		ctrIn.close();
		ctrOut.close();
		dataIn.close();
		dataOut.close();
		ctrSoc.close();
		dataSoc.close();

	}

	public void sendIcon(String name, int index) throws IOException {
		ctrOut.writeUTF(SENDICON);
		ctrOut.flush();
		dataOut.writeUTF(name);
		dataOut.flush();
		dataOut.write(index);
		dataOut.flush();

	}
}
