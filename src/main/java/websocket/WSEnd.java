package websocket;

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
		HttpSession httpSession = (HttpSession) config.getUserProperties().get(HttpSession.class.getName());
		if (httpSession != null) {
			if (httpSession.getAttribute("login") != null) {
				User user = (User) httpSession.getAttribute("user");
				onlineList.add(user.getUsername());
				userSession.getUserProperties().put("user", user);
				initLogin.add(userSession);
			}
		}
	}

	@SuppressWarnings("unchecked")
	@OnMessage
	public void handleMessage(String mess, Session userSession) throws IOException, ParseException, SQLException {
		JSONObject jsobject = (JSONObject) jsonParser.parse(mess);

		String stt = (String) jsobject.get("status");
		if (stt.equals("signin")) {
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
					userSession.getUserProperties().put("room", 0);
					initLogin.add(userSession);
					roomlist.get(0).getOnlinelist().add(userSession);
					roomlist.get(0).setNumberonline(roomlist.get(0).getNumberonline()+1);
					sendAllRoom(0, "Online!", "Arial", 12, userSession);
					ob.put("message", "OKE");
					List<String> iconlist = new LinkedList<String>();
					for (Icon icon : roomlist.get(0).getIcons()) {
						iconlist.add(icon.getUrl());
					}
					ob.put("icon", iconlist);
				} else {
					ob.put("message", "ON");
				}

			} else {
				ob.put("message", "NO");
			}
			userSession.getBasicRemote().sendText(ob.toJSONString());

		} else if (stt.equals("changeroom")) {
			long room = (Long) jsobject.get("room");
			if (userSession.getUserProperties().get("room") != null) {
				int r = (Integer) userSession.getUserProperties().get("room");
				if (room != r) {
					sendAllRoom(r, "Offline!", "Roman", 12, userSession);
					roomlist.get(r).setNumberonline(roomlist.get(r).getNumberonline() - 1);
					roomlist.get(r).getOnlinelist().remove(userSession);
					roomlist.get((int) room).getOnlinelist().add(userSession);
					userSession.getUserProperties().put("room", (int) room);
					roomlist.get((int) room).setNumberonline(roomlist.get((int) room).getNumberonline() + 1);
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
			}
		} else if (stt.equals("text")) {
			String font = (String) jsobject.get("font");
			long size = (Long) jsobject.get("size");
			String message = (String) jsobject.get("message");
			Integer room = (Integer) userSession.getUserProperties().get("room");
			if (room != null) {
				sendAllRoom(room, message, font, (int) size, userSession);
			}
		} else if (stt.equals("icon")) {
			String message = (String) jsobject.get("message");
			int room = (Integer) userSession.getUserProperties().get("room");
			for (Session session : roomlist.get(room).getOnlinelist()) {
				session.getBasicRemote().sendText(getJsonIcon(message, userSession));
			}
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

	public void sendAllRoom(int room, String message, String font, int size, Session userSession) throws IOException {
		for (Session session : roomlist.get(room).getOnlinelist()) {
			session.getBasicRemote().sendText(getJsonText(message, "Roman", 12, userSession));
		}
	}

	public void initRoom(int room, Session userSession) throws IOException {
		userSession.getBasicRemote().sendText(getJsonOnlineList((int) room));
		userSession.getBasicRemote().sendText(getJsonIconList((int) room));
	}
}
