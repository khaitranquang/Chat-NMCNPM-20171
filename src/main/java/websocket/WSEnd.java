package websocket;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpSession;
import javax.websocket.EndpointConfig;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import config.ConfigHttpSession;
import entity.Icon;
import entity.Room;
import entity.User;
import repository.RoomRepository;
import repository.UserRepository;

@ServerEndpoint(value = "/chatroom", configurator = ConfigHttpSession.class)
public class WSEnd {

	public static UserRepository userRepository = new UserRepository();
	public static RoomRepository roomRepository = new RoomRepository();
	public static JSONParser jsonParser = new JSONParser();
	public static List<Room> roomlist;
	public static List<String> onlineList = new LinkedList<String>();
	public static Set<Session> initLogin = Collections.synchronizedSet(new HashSet<Session>());
	static {
		roomlist = roomRepository.getAllRoom();
	}

	@OnOpen
	public void handOpen(Session userSession, EndpointConfig config) {
		userSession.setMaxBinaryMessageBufferSize(10240000);
		userSession.setMaxTextMessageBufferSize(10240000);
		HttpSession httpSession = (HttpSession) config.getUserProperties().get(HttpSession.class.getName());
		if (httpSession != null) {
			if (httpSession.getAttribute("login") != null) {
				System.out.println("web");
				User user = (User) httpSession.getAttribute("user");
				if (!onlineList.contains(user.getUsername()))
					onlineList.add(user.getUsername());
				userSession.getUserProperties().put("user", user);
				initLogin.add(userSession);
			}

		}

	}

	@OnError
	public void handError(Session userSession, Throwable throwable) {
		Integer room = (Integer) userSession.getUserProperties().get("room");
		if (room != null) {
			roomlist.get(room).getOnlinelist().remove(userSession);
		}
		User user = (User) userSession.getUserProperties().get("user");
		if (user != null) {
			for (int i = 0; i < onlineList.size(); i++) {
				if (onlineList.get(i).equals(user.getUsername())) {
					onlineList.remove(i);
				}
			}
		}
		initLogin.remove(userSession);
	}

	@OnMessage
	public void handleMessage(String mess, Session userSession, EndpointConfig config)
			throws IOException, ParseException, SQLException {
		JSONObject jsobject = (JSONObject) jsonParser.parse(mess);
		System.out.println(mess);
		String stt = (String) jsobject.get("status");
		if (stt.equals("signin")) {
			handSignIn(userSession, jsobject);
		} else if (stt.equals("changeroom")) {
			handChangeRoom(userSession, jsobject);
		} else if (stt.equals("text")) {
			handText(userSession, jsobject);
		} else if (stt.equals("icon")) {
			handIcon(userSession, jsobject);
		} else if (stt.equals("avatar")) {
			handAvatar(userSession, jsobject);
		} else if (stt.equals("signup")) {
			handRegister(userSession, jsobject);
		} else if (stt.equals("changepassword")) {
			handChangePassword(userSession, jsobject);
		} else if (stt.equals("signout")) {
			handLogout(userSession, jsobject);
		} else if (stt.equals("outroom")) {
			handOutRoom(userSession, jsobject);
		} else if (stt.equals("logout")) {
			handOutRoom(userSession, jsobject);
			handLogout(userSession, jsobject);

		}

	}

	@OnClose
	public void handClose(Session userSession) throws IOException {
		initLogin.remove(userSession);
		if (userSession.getUserProperties().get("room") != null) {
			int room = (Integer) userSession.getUserProperties().get("room");
			roomlist.get(room).getOnlinelist().remove(userSession);
			sendAllRoom(room, "Offline!", "Roman", 12, userSession);
			roomlist.get(room).setNumberonline(roomlist.get(room).getNumberonline() - 1);
			sendUpdateRoom();
		}
	}

	@SuppressWarnings({ "unchecked", "restriction" })
	public void handAvatar(Session userSession, JSONObject jsobject) {
		String bytesImage = (String) jsobject.get("image");
		String filename = (String) jsobject.get("name");
		JSONObject ob = new JSONObject();
		ob.put("status", "avatar");
		User user = (User) userSession.getUserProperties().get("user");
		byte[] bytes = null;
		try {
			bytes = UserRepository.decoder.decodeBuffer(bytesImage);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("khoong decode ddc");
			return;
		}
		String url = "E:\\Java\\BTLCNPM\\src\\main\\webapp\\avatar\\" + user.getUsername() + filename;
		try {
			File file = new File(url);
			if (!file.exists())
				file.createNewFile();
			FileOutputStream out = new FileOutputStream(file);
			out.write(bytes);
			out.close();
			System.out.println("save done");
			user.setAvatarUrl("http://localhost:8080/BTLCNPM/avatar/" + user.getUsername() + filename);
			userRepository.updateAvatar(user, user.getAvatarUrl());
			ob.put("message", "OK");
			ob.put("avatarurl", user.getAvatarUrl());
			System.out.println("oke done");
		} catch (IOException e) {
			ob.put("message", "EXIST");
			System.out.println("catch done");
		}
		try {
			userSession.getBasicRemote().sendText(ob.toJSONString());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@SuppressWarnings("unchecked")
	public void handChangePassword(Session userSession, JSONObject jsobject) throws IOException {
		String oldpassword = (String) jsobject.get("oldpassword");
		String password = (String) jsobject.get("password");
		User user = (User) userSession.getUserProperties().get("user");
		JSONObject ob = new JSONObject();
		ob.put("status", "changepassword");
		try {
			if (userRepository.checkUser(user.getUsername(), oldpassword)) {
				userRepository.updatePassword(user, password);
				ob.put("message", "OKE");
			} else {
				ob.put("message", "WRONG");

			}
		} catch (SQLException e) {
			ob.put("message", "NO");
		}
		userSession.getBasicRemote().sendText(ob.toJSONString());
	}

	@SuppressWarnings("unchecked")
	public void handSignIn(Session userSession, JSONObject jsobject) throws IOException {
		String username = (String) jsobject.get("username");
		String password = (String) jsobject.get("password");
		JSONObject ob = new JSONObject();
		ob.put("status", "login");
		if (userRepository.checkUser(username, password)) {

			boolean flag = true;
			for (String str : onlineList) {
				if (str.equals(username)) {
					flag = false;
				}
			}
			if (flag) {
				onlineList.add(username);
				User user = userRepository.getUserByUserName(username);
				userSession.getUserProperties().put("user", user);
				initLogin.add(userSession);
				ob.put("message", "OKE");
				List<JSONObject> list = new LinkedList<JSONObject>();
				for (Room room : roomlist) {
					JSONObject obj = new JSONObject();
					obj.put("id", room.getId());
					obj.put("name", room.getName());
					obj.put("numberonline", room.getNumberonline());
					obj.put("maxquota", room.getMaxquota());
					obj.put("description", room.getDescription());
					obj.put("logourl", room.getLogourl());
					list.add(obj);
				}
				JSONObject userjson = new JSONObject();
				userjson.put("username", user.getUsername());
				userjson.put("fullname", user.getFullName());
				userjson.put("age", user.getAge());
				userjson.put("avatarurl", user.getAvatarUrl());
				ob.put("userjson", userjson);
				ob.put("roomlist", list);

			} else {
				ob.put("message", "ON");
			}

		} else {
			ob.put("message", "NO");
		}
		userSession.getBasicRemote().sendText(ob.toJSONString());
	}

	public void handText(Session userSession, JSONObject jsobject) throws IOException {
		String font = (String) jsobject.get("font");
		long size = (Long) jsobject.get("size");
		String message = (String) jsobject.get("message");
		Integer room = (Integer) userSession.getUserProperties().get("room");
		if (room != null) {
			sendAllRoom(room, message, font, (int) size, userSession);
		}
	}

	public void handChangeRoom(Session userSession, JSONObject jsobject) throws IOException {
		long room = (Long) jsobject.get("room");
		if (userSession.getUserProperties().get("room") != null) {
			int r = (Integer) userSession.getUserProperties().get("room");
			if (room != r) {
				userSession.getUserProperties().put("room", (int) room);
				roomlist.get(r).getOnlinelist().remove(userSession);
				sendAllRoom(r, "Offline!", "Roman", 12, userSession);
				roomlist.get(r).setNumberonline(roomlist.get(r).getNumberonline() - 1);

				for (Session session : roomlist.get(r).getOnlinelist()) {
					session.getBasicRemote().sendText(getJsonOnlineList(r));
				}
				roomlist.get((int) room).getOnlinelist().add(userSession);
				roomlist.get((int) room).setNumberonline(roomlist.get((int) room).getNumberonline() + 1);
				for (Session session : roomlist.get((int) room).getOnlinelist()) {
					session.getBasicRemote().sendText(getJsonOnlineList((int) room));
				}
				sendUpdateRoom();
				initRoom((int) room, userSession);
				sendAllRoom((int) room, "Online!", "Roman", 12, userSession);
			} else {
				System.out.println("bằng");
			}
		} else {
			roomlist.get((int) room).getOnlinelist().add(userSession);
			userSession.getUserProperties().put("room", (int) room);
			initRoom((int) room, userSession);
			sendAllRoom((int) room, "Online!", "Roman", 12, userSession);
			roomlist.get((int) room).setNumberonline(roomlist.get((int) room).getNumberonline() + 1);
			sendUpdateRoom();
			for (Session session : roomlist.get((int) room).getOnlinelist()) {
				session.getBasicRemote().sendText(getJsonOnlineList((int) room));
			}
		}
	}

	public void handIcon(Session userSession, JSONObject jsobject) throws IOException {
		String message = (String) jsobject.get("message");
		int room = (Integer) userSession.getUserProperties().get("room");
		for (Session session : roomlist.get(room).getOnlinelist()) {
			session.getBasicRemote().sendText(getJsonIcon(message, userSession));
		}
	}

	@SuppressWarnings("unchecked")
	public String getJsonOnlineList(int room) {
		JSONObject object = new JSONObject();
		object.put("status", "onlinelist");
		object.put("number", roomlist.get(room).getOnlinelist().size());
		JSONArray array = new JSONArray();
		for (Session session : roomlist.get(room).getOnlinelist()) {
			JSONObject ob = new JSONObject();
			User user = (User) session.getUserProperties().get("user");
			ob.put("username", user.getUsername());
			ob.put("avatarurl", user.getAvatarUrl());
			array.add(ob);
		}
		object.put("userlist", array);
		return object.toJSONString();
	}

	@SuppressWarnings({ "unused", "unchecked" })
	public void handRegister(Session userSession, JSONObject jsobject) throws SQLException, IOException {
		String username = (String) jsobject.get("username");
		String password = (String) jsobject.get("password");
		long age = Long.parseLong((String) jsobject.get("age"));
		String fullname = (String) jsobject.get("fullname");
		JSONObject ob = new JSONObject();
		ob.put("status", "register");
		User user = userRepository.getUserByUserName(username);
		if (user != null) {
			ob.put("message", "NO");
		} else {
			userRepository.saveUser(username, password, "http://localhost:8080/BTLCNPM//logo/bk-logo.png", (int) age,
					fullname);
			ob.put("message", "OKE");
		}
		userSession.getBasicRemote().sendText(ob.toJSONString());
	}

	public void handLogout(Session userSession, JSONObject jsobject) throws IOException {
		System.out.println("go");
		User user = (User) userSession.getUserProperties().get("user");
		if (user != null) {
			for (int i = 0; i < onlineList.size(); i++) {
				if (onlineList.get(i).equals(user.getUsername())) {
					onlineList.remove(i);
				}
			}

		}
		userSession.getUserProperties().remove("user");
		initLogin.remove(userSession);
	}

	public void handOutRoom(Session userSession, JSONObject jsobject) throws IOException {
		Integer room = (Integer) userSession.getUserProperties().get("room");
		roomlist.get(room).getOnlinelist().remove(userSession);
		sendAllRoom(room, "Offline!", "Roman", 12, userSession);
		roomlist.get(room).setNumberonline(roomlist.get(room).getNumberonline() - 1);
		userSession.getUserProperties().remove("room");
		sendUpdateRoom();
		for (Session session : roomlist.get(room).getOnlinelist()) {
			session.getBasicRemote().sendText(getJsonOnlineList(room));
		}
	}

	@SuppressWarnings("unchecked")
	public String getJsonIconList(int room) {
		JSONObject object = new JSONObject();
		object.put("status", "iconlist");
		List<String> iconlist = new LinkedList<String>();
		for (Icon icon : roomlist.get(room).getIcons()) {
			iconlist.add(icon.getUrl());
		}
		object.put("icon", iconlist);
		return object.toJSONString();
	}

	@SuppressWarnings("unchecked")
	public String getJsonText(String message, String font, int size, Session session) {
		User user = (User) session.getUserProperties().get("user");
		JSONObject object = new JSONObject();
		object.put("status", "text");
		object.put("from", user.getUsername());
		object.put("avatarurl", user.getAvatarUrl());
		object.put("font", font);
		object.put("size", size);
		object.put("message", message);
		return object.toJSONString();
	}

	@SuppressWarnings("unchecked")
	public String getJsonIcon(String message, Session session) {
		User user = (User) session.getUserProperties().get("user");
		JSONObject object = new JSONObject();
		object.put("status", "icon");
		object.put("from", user.getUsername());
		object.put("avatarurl", user.getAvatarUrl());
		object.put("message", message);
		return object.toJSONString();
	}

	@SuppressWarnings("unchecked")
	public void sendUpdateRoom() throws IOException {
		JSONObject object = new JSONObject();
		JSONArray array = new JSONArray();
		for (int i = 0; i < roomlist.size(); i++) {
			JSONObject temp = new JSONObject();
			temp.put("room", i);
			temp.put("number", roomlist.get(i).getNumberonline());
			array.add(temp);
		}
		object.put("status", "update");
		object.put("message", array);
		for (Session session : initLogin) {
			session.getBasicRemote().sendText(object.toJSONString());
		}
	}

	public void sendAllRoom(int room, String message, String font, int size, Session userSession) throws IOException {
		for (Session session : roomlist.get(room).getOnlinelist()) {
			session.getBasicRemote().sendText(getJsonText(message, "Roman", 12, userSession));
		}
	}

	public void initRoom(int room, Session userSession) throws IOException {
		userSession.getBasicRemote().sendText(getJsonOnlineList((int) room));

		userSession.getBasicRemote().sendText(getJsonIconList((int) room));
		System.out.println(getJsonIconList((int) room));
	}
}
