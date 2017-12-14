package gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.border.EmptyBorder;

/**
 * @author Tran Quang Khai
 *
 */
public class ChangePassBox extends JDialog{
	private JButton btnChangePass = new JButton("Apply");
	private JButton btnCancel = new JButton("Cancel");
	private JPasswordField tfOldPass = new JPasswordField(20);
	private JPasswordField tfNewPass = new JPasswordField(20);
	private JPasswordField tfReNewPass = new JPasswordField(20);
	
	public JButton getBtnChangePass() {
		return btnChangePass;
	}
	public JButton getBtnCancel() {
		return btnCancel;
	}
	public JPasswordField getTfOldPass() {
		return tfOldPass;
	}
	public JPasswordField getTfNewPass() {
		return tfNewPass;
	}
	public JPasswordField getTfReNewPass() {
		return tfReNewPass;
	}
	
	public ChangePassBox() {
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setPreferredSize(new Dimension(450, 200));
		setResizable(true);
		setTitle("Change Password");
		
		add(createMainPanel());
		pack();      				
		setLocationRelativeTo(null);
	}
	
	private JPanel createMainPanel() {
		JPanel mainPanel = new JPanel(new BorderLayout(5, 5));
		mainPanel.add(createFieldPanel(), BorderLayout.CENTER);
		mainPanel.add(createButtonPanel(), BorderLayout.PAGE_END);
		return mainPanel;
	}
	
	private JPanel createFieldPanel() {
		JPanel fieldPanel = new JPanel(new BorderLayout(10, 10));
		fieldPanel.setBorder(new EmptyBorder(5, 20, 10, 20));
		JPanel labelPanel = new JPanel(new GridLayout(3, 1, 5, 5));
		labelPanel.add(new JLabel("Mật khẩu cũ "));
		labelPanel.add(new JLabel("Mật khẩu mới "));
		labelPanel.add(new JLabel("Nhập lại mật khẩu mới"));
		
		JPanel inputPanel = new JPanel (new GridLayout(3, 1, 5, 5));
		inputPanel.add(tfOldPass);
		inputPanel.add(tfNewPass);
		inputPanel.add(tfReNewPass);
		
		fieldPanel.add(labelPanel, BorderLayout.WEST);
		fieldPanel.add(inputPanel, BorderLayout.CENTER);
		return fieldPanel;
	}
	
	private JPanel createButtonPanel() {
		JPanel buttonPanel = new JPanel(new GridLayout(1, 2, 10, 10));
		buttonPanel.setBorder(new EmptyBorder(5, 20, 10, 20));
		buttonPanel.add(btnChangePass);
		buttonPanel.add(btnCancel);
		return buttonPanel;
	}
}
