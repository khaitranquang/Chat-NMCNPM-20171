package repository;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;

import entity.Icon;
import entity.Room;

public class RoomRepository {
	public static Statement stm;
	static {
		try {
			stm = DriverManager.getConnection(
					"jdbc:mysql://localhost:3306/cnpmchat"
							+ "?autoReconnect=true&useSSL=false&zeroDateTimeBehavior=convertToNull",
					"root", "1234").createStatement();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
//	public Room getRoomByName(String name) {
//		String str = "select * from room where name='" + name + "';";
//
//		try {
//			ResultSet rs;
//			rs = stm.executeQuery(str);
//			if (rs.next()) {
//				int id =rs.getInt("id");
//				String rname = rs.getString("name");
//				String description = rs.getString("description");
//				int maxquota = rs.getInt("maxquota");
//				str="select icon.id, url from room,icon,room_icon_xref where icon.id=iconid and roomid =room.id and name='"+name+"';";
//				Set<Icon> icons = new HashSet<Icon>();
//				rs = stm.executeQuery(str);
//				while(rs.next()) {
//					icons.add(new Icon(rs.getInt("id"), rs.getString("url")));
//				}
//				return new Room(id, rname, maxquota, description, icons);
//			}
//				
//		} catch (SQLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//
//		return null;
//	}
//	public Room getRoomById(int id) {
//		String str = "select * from room where id=" + id + ";";
//
//		try {
//			ResultSet rs;
//			rs = stm.executeQuery(str);
//			if (rs.next()) {
//				int sid =rs.getInt("id");
//				String rname = rs.getString("name");
//				String description = rs.getString("description");
//				int maxquota = rs.getInt("maxquota");
//				str="select icon.id, url from room,icon,room_icon_xref where icon.id=iconid and roomid =room.id and room.id="+id+";";
//				Set<Icon> icons = new HashSet<Icon>();
//				rs = stm.executeQuery(str);
//				while(rs.next()) {
//					icons.add(new Icon(rs.getInt("id"), rs.getString("url")));
//				}
//				return new Room(sid, rname, maxquota, description, icons);
//			}
//				
//		} catch (SQLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//
//		return null;
//	}
	public List<Room> getAllRoom() {
		String str = "select * from room ;";
		List<Room> result = new LinkedList<Room>();
		try {
			ResultSet rs;
			rs = stm.executeQuery(str);
			while (rs.next()) {
				int sid =rs.getInt("id");
				String rname = rs.getString("name");
				String description = rs.getString("description");
				int maxquota = rs.getInt("maxquota");
				String logo = rs.getString("logourl");
				List<Icon> icons = new LinkedList<Icon>();
				result.add(new Room(sid, rname, maxquota, description,logo, icons));
			}
			for(Room room:result) {
				str="select icon.id, url from room,icon,room_icon_xref where icon.id=iconid and roomid =room.id and room.id="+room.getId()+";";
				ResultSet rss = stm.executeQuery(str);
				while(rss.next()) {
					room.getIcons().add(new Icon(rss.getInt("id"), rss.getString("url")));
				}
			}
				
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return result;
	}
}
