package khai;

import java.awt.Component;
import java.awt.FlowLayout;
import java.io.IOException;

import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.ListCellRenderer;

public class IconBox {
	public int leng;
	public JMenu iconmenu;
	public DefaultListModel<Icon> model;
	public JList<Icon> list;

	public IconBox() {
		iconmenu = new JMenu("Icon");
		model = new DefaultListModel<Icon>();
		list = new JList<Icon>(model);
		list.setCellRenderer(new ListCellRenderer<Icon>() {

			public Component getListCellRendererComponent(JList list, Icon value, int index, boolean isSelected,
					boolean cellHasFocus) {

				JLabel label = new JLabel(value.icon);
				if (isSelected) {
					label.requestFocus();
				}
				return label;
			}

		});
		iconmenu.add(list);
	}

}
