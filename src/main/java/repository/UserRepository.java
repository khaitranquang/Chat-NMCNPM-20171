package repository;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import entity.User;
import sun.misc.BASE64Encoder;
import sun.misc.BASE64Decoder;

@SuppressWarnings("restriction")
public class UserRepository {
	public static BASE64Encoder encoder = new BASE64Encoder();
	public static BASE64Decoder decoder = new BASE64Decoder();
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

	public User getUserByUserName(String username) {
		String str = "select * from user where username='" + username + "';";

		try {
			ResultSet rs;
			rs = stm.executeQuery(str);
			if (rs.next())
				return new User(rs.getInt("id"), rs.getString("username"), rs.getString("password"), rs.getInt("age"),
						rs.getString("avatarurl"), rs.getBoolean("role"), rs.getString("fullname"));
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}

	public void saveUser(String username, String password, String avatarurl, int age, String fullname)
			throws SQLException {
		String str = "insert into User(username,password,avatarurl,age,fullname) values ('" + username + "','"
				+ encoder.encode(password.getBytes()) + "','" + avatarurl + "'," + age + ",'" + fullname + "');";
		stm.executeUpdate(str);
	}

	public boolean checkUser(String username, String password) {
		User user = getUserByUserName(username);
		if (user == null) {
			return false;
		} else {
			if (encoder.encode(password.getBytes()).equals(user.getPassword())) {
				return true;
			} else {
				return false;
			}
		}
	}

	public void updateAvatar(User user, String avatarurl) {

		String query = "update User set avatarurl='" + avatarurl + "' where id=" + user.getId() + ";";
		try {
			stm.executeUpdate(query);
			user.setAvatarUrl(avatarurl);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void updatePassword(User user, String password) throws SQLException {
		String newpass = encoder.encode(password.getBytes());
		String query = "update User set password='" + newpass + "' where id=" + user.getId() + ";";
		stm.executeUpdate(query);
		user.setPassword(newpass);

	}
}
