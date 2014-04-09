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

public class CurrentViewers extends JPanel {
	

	private static final long serialVersionUID = -5136528463461500682L;
	private static final String MESSAGE = "Enter points to add or subtract";
	private MyBot theBot;
	private ScrollPane scroll;
	
	public CurrentViewers(MyBot bot) {
		theBot = bot;
		//scroll = new ScrollPane(theBot, "viewer");
		setup();
	} //constructor
	
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
		grid.setLayout(new GridLayout(4, 1));

		Container viewers = new Container();
		viewers.setLayout(new FlowLayout(FlowLayout.LEFT));
		JLabel lbview = new JLabel("Total Viewers:");
		JLabel txview = new JLabel("test");
		viewers.add(lbview);
		viewers.add(txview);
		
		Container mods = new Container();
		mods.setLayout(new FlowLayout(FlowLayout.LEFT));
		JLabel lbmods = new JLabel("Total Moderators:");
		JLabel txmods = new JLabel("text");
		mods.add(lbmods);
		mods.add(txmods);
		
		Container vpoints = new Container();
		vpoints.setLayout(new FlowLayout(FlowLayout.LEFT));
		JLabel lbvpt = new JLabel("Total Points:");
		JLabel txvpt = new JLabel("test");
		vpoints.add(lbvpt);
		vpoints.add(txvpt);
		
		Container vsubs = new Container();
		vsubs.setLayout(new FlowLayout(FlowLayout.LEFT));
		JLabel lbvsubs = new JLabel("Total Subscribers:");
		JLabel txvsubs = new JLabel("test");
		vsubs.add(lbvsubs);
		vsubs.add(txvsubs);

		grid.add(viewers);
		grid.add(mods);
		grid.add(vpoints);
		grid.add(vsubs);
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
	
} //CurrentViewers
