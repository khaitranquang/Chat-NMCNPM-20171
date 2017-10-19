package my;

import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.*;

import my.Icon;

public class IconBox {
	public int leng;
	public JMenu iconmenu;
	public DefaultListModel<Icon> model ;
	public JList<Icon> list;
	public IconBox(){
		iconmenu = new JMenu("Icon");
		model= new DefaultListModel<Icon>();
		list= new JList<Icon>(model);
		list.setCellRenderer(new ListCellRenderer<Icon>(){

			@Override
			public Component getListCellRendererComponent(JList list, Icon value, int index, boolean isSelected,
					boolean cellHasFocus) {
				JLabel label = new JLabel();
				label.setIcon(value.icon);
				return label;
			}
			
		});
		iconmenu.add(list);
	}
	public static void main(String[] args) throws IOException{
		JFrame a = new JFrame();
		a.setSize(500, 500);
		a.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		JMenuBar b = new JMenuBar();
		a.setLayout(new FlowLayout());
		IconBox c = new IconBox();
		b.add(c.iconmenu);
		a.add(b);
		a.setVisible(true);
	}
}
