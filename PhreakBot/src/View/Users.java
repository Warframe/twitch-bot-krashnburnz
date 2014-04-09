package View;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import Model.MyBot;

public class Users extends JPanel {
	
	private static final long serialVersionUID = -2544375717703845827L;
	private static final String MESSAGE = "Enter points to add or subtract";
	private MyBot theBot;
	
	public Users(MyBot bot) {
		theBot = bot;
		//scroll = new ScrollPane(theBot, "viewer");
		setup();
	}
	
	private void setup() {
		JScrollPane pane = new JScrollPane();//scroll.getScrollPane();
		pane.setWheelScrollingEnabled(true);
		
		setLayout(new BorderLayout());
		add(pane, BorderLayout.CENTER);
		add(misc(), BorderLayout.SOUTH);
	} //generalSetup
	
	private Container misc() {
		Container border = new Container();
		border.setLayout(new BorderLayout());
		border.add(pointManagement(), BorderLayout.NORTH);
		border.add(info(), BorderLayout.CENTER);
		border.add(other(), BorderLayout.EAST);
		return border;
	} //misc
	
	private Container pointManagement() {
		Container flow = new JPanel(new FlowLayout());
		final JTextField points = new JTextField(MESSAGE, 18);
		JButton add = new JButton("+ Points");
		JButton sub = new JButton("- Points");
		
		points.setForeground(Color.GRAY);
		points.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				points.setText("");
				points.setForeground(Color.BLACK);
			}
		});
		
		add.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				
				
				
				if (points.getText().equals(MESSAGE)) {
					JOptionPane.showMessageDialog(null, "Please select a user first before adding points!", "Update", JOptionPane.WARNING_MESSAGE);
				} else if (points.getText().isEmpty()) {
					JOptionPane.showMessageDialog(null, "Please input desired number to add points!", "Update", JOptionPane.WARNING_MESSAGE);
				} else {
					JOptionPane.showMessageDialog(null, points.getText() + " points has been added to *USERNAME HERE*" + "!", "Update", JOptionPane.INFORMATION_MESSAGE);
					points.setForeground(Color.GRAY);
					points.setText(MESSAGE);
				}
			}
		});
		
		sub.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				
				
				
				if (points.getText().equals(MESSAGE)) {
					JOptionPane.showMessageDialog(null, "Please select a user first before subtracting points!", "Update", JOptionPane.WARNING_MESSAGE);
				}  else if (points.getText().isEmpty()) {
					JOptionPane.showMessageDialog(null, "Please input desired number to subtract points!", "Update", JOptionPane.WARNING_MESSAGE);
				} else {
					JOptionPane.showMessageDialog(null, points.getText() + " points has been taken from *USERNAME HERE*" + "!", "Update", JOptionPane.INFORMATION_MESSAGE);
					points.setForeground(Color.GRAY);
					points.setText(MESSAGE);
				}
			}
		});
		
		flow.add(points);
		flow.add(add);
		flow.add(sub);
		return flow;
	} //pointManagement
	
	private Container info() {
		Container grid = new Container();
		grid.setLayout(new GridLayout(4, 2));

		Container join = new Container();
		join.setLayout(new FlowLayout(FlowLayout.LEFT));
		JLabel lbjoin = new JLabel("Join Date:");
		JLabel txjoin = new JLabel("test");
		join.add(lbjoin);
		join.add(txjoin);
		
		Container total = new Container();
		total.setLayout(new FlowLayout(FlowLayout.LEFT));
		JLabel lbtotal = new JLabel("Total:");
		JLabel txtotal = new JLabel("test");
		total.add(lbtotal);
		total.add(txtotal);
		
		Container mail = new Container();
		mail.setLayout(new FlowLayout(FlowLayout.LEFT));
		JLabel lbmail = new JLabel("E-Mail:");
		JLabel txmail = new JLabel("text");
		mail.add(lbmail);
		mail.add(txmail);
		
		Container misc1 = new Container();
		misc1.setLayout(new FlowLayout(FlowLayout.LEFT));
		JLabel lbmisc1 = new JLabel("Misc Info:");
		JLabel txmisc1 = new JLabel("test");
		misc1.add(lbmisc1);
		misc1.add(txmisc1);
		
		Container id = new Container();
		id.setLayout(new FlowLayout(FlowLayout.LEFT));
		JLabel lbid = new JLabel("Misc ID:");
		JLabel txid = new JLabel("test");
		id.add(lbid);
		id.add(txid);
		
		Container misc2 = new Container();
		misc2.setLayout(new FlowLayout(FlowLayout.LEFT));
		JLabel lbmisc2 = new JLabel("Misc Info:");
		JLabel txmisc2 = new JLabel("test");
		misc2.add(lbmisc2);
		misc2.add(txmisc2);
		
		Container misc3 = new Container();
		misc3.setLayout(new FlowLayout(FlowLayout.LEFT));
		JLabel lbmisc3 = new JLabel("Misc Info:");
		JLabel txmisc3 = new JLabel("test");
		misc3.add(lbmisc3);
		misc3.add(txmisc3);
		
		Container misc4 = new Container();
		misc4.setLayout(new FlowLayout(FlowLayout.LEFT));
		JLabel lbmisc4 = new JLabel("Misc Info:");
		JLabel txmisc4 = new JLabel("test");
		misc4.add(lbmisc4);
		misc4.add(txmisc4);

		grid.add(join);
		grid.add(total);
		grid.add(mail);
		grid.add(misc1);
		grid.add(id);
		grid.add(misc2);
		grid.add(misc3);
		grid.add(misc4);
		return grid;
	} //info
	
	private Container other() {
		Container grid = new Container();
		grid.setLayout(new GridLayout(3, 1));

		JCheckBox isSub = new JCheckBox("Is Sub?");
		isSub.setHorizontalTextPosition(SwingConstants.LEFT);
		isSub.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
		});
		
		JCheckBox isMod = new JCheckBox("Is Mod?");
		isMod.setHorizontalTextPosition(SwingConstants.LEFT);
		isMod.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				
			}
			
		});
		
		JCheckBox isSpecial = new JCheckBox("Special");
		isSpecial.setHorizontalTextPosition(SwingConstants.LEFT);
		isSpecial.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				
			}
			
		});
		
		grid.add(isSub);
		grid.add(isMod);
		grid.add(isSpecial);
		return grid;
	} //other
} //class
