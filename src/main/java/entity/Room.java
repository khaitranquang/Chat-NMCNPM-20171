package entity;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.websocket.Session;

public class Room {
	private int id;
	private String name;
	private int maxquota;
	private String description;
	private Set<Session> onlinelist;
	private List<Icon> icons;
	private String logourl;
	private int numberonline;
	
	public int getNumberonline() {
		return numberonline;
	}
	public void setNumberonline(int numberonline) {
		this.numberonline = numberonline;
	}
	public String getLogourl() {
		return logourl;
	}
	public void setLogourl(String logourl) {
		this.logourl = logourl;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getMaxquota() {
		return maxquota;
	}
	public void setMaxquota(int maxquota) {
		this.maxquota = maxquota;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public List<Icon> getIcons() {
		return icons;
	}
	public void setIcons(List<Icon> icons) {
		this.icons = icons;
	}
	
	public Set<Session> getOnlinelist() {
		return onlinelist;
	}
	public void setOnlinelist(Set<Session> onlinelist) {
		this.onlinelist = onlinelist;
	}
	public Room(int id, String name, int maxquota, String description,String logo, List<Icon> icons) {
		super();
		onlinelist = Collections.synchronizedSet(new HashSet<Session>());
		this.id = id;
		this.name = name;
		this.maxquota = maxquota;
		this.description = description;
		this.icons = icons;
		logourl=logo;
	}
	
}
