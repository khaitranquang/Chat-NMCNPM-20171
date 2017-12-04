package khai;

import java.io.Serializable;

import javax.swing.ImageIcon;

public class Icon implements Serializable{
	public String name;
	public ImageIcon icon;
	public Icon(String name,ImageIcon icon){
		this.name=name;
		this.icon=icon;
	}
}
