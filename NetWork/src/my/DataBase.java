package my;

import java.io.Serializable;
import java.util.Hashtable;

public class DataBase implements Serializable{
	public Hashtable<String,String> data;
	public DataBase(){
		data = new Hashtable<String,String>();
	}
	public void add(String username,String password){
		data.put(username, password);
	}
	public boolean exist(String username){
		return data.contains(username);
	}
	public String pass(String username){
		return data.get(username);
	}
	public void remove(String username){
		data.remove(username);
	}
}
