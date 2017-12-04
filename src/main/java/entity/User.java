package entity;

public class User {
	public static final boolean ADMIN=true,NORMAL=false;
	private int id;
	private String username;
	private String password;
	private int age;
	private String avatarUrl;
	private boolean role;
	private String fullName;
	
	public User(int id, String username, String password, int age, String avatarUrl, boolean role, String fullName) {
		super();
		this.id = id;
		this.username = username;
		this.password = password;
		this.age = age;
		this.avatarUrl = avatarUrl;
		this.role = role;
		this.fullName = fullName;
	}
	public String getFullName() {
		return fullName;
	}
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
	public String getAvatarUrl() {
		return avatarUrl;
	}
	public void setAvatarUrl(String avatarUrl) {
		this.avatarUrl = avatarUrl;
	}
	public boolean isRole() {
		return role;
	}
	public void setRole(boolean role) {
		this.role = role;
	}
	
}
