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
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import Model.MyBot;
import Model.User;

/**
 * CurrentViewer tab to display current viewer of the channel including rank (based on points accumulated), name (nick),
 * points, sub/mod. 
 * Include functionality for add/subtract points from specific user, display general user info when click on 1 user.
 * @author Ching-Ting Huang
 */
public class CurrentViewers extends JPanel {
	

	private static final long serialVersionUID = -5136528463461500682L;
	/**
	 * Message embedded in textfield for streamer to input points to add/take away from his/her viewer
	 */
	private static final String MESSAGE = "Enter points to add or subtract";
	
	/**
	 * Controller
	 */
	private MyBot my_bot;
	
	/**
	 * The scroll pane containing all viewers' metadata that has ever been to the streamer's channel.
	 */
	private ScrollPane scroll;
	
	/**
	 * Selected viewer's name/ID/nick-name.
	 */
	private JLabel id;
	
	/**
	 * After selecting a viewer from the scroll pane, this shows whether this viewer is a subscriber.
	 */
	private boolean isASub = false;
	
	/**
	 * After selecting a viewer from the scroll pane, this shows whether this viewer is a moderator.
	 */
	private boolean isAMod = false;
	
	/**
	 * After selecting a viewer from the scroll pane, this shows whether this viewer is "SPECIAL" (future feature?).
	 */
	private boolean isVIP = true;
	
	/**
	 * Check if a viewer is being selected in the scroll panel (add/subtract point management check).
	 */
	private boolean isSelected = false;
	
	/**
	 * Constructor: initialize class & get access to controller
	 * 
	 * @param bot is the controller
	 */
	public CurrentViewers(MyBot bot) {
		my_bot = bot;
		scroll = //my_bot.getCurUsers().length == 0 ? null : 
			new ScrollPane(my_bot, "viewer");
		setup();
	} //constructor
	
	/**
	 * General layout of this panel: scroll pane, user metadata. 
	 */
	private void setup() {
		JScrollPane pane = scroll == null ? noViewerPanel() : scroll.getScrollPane();
		final JLabel flag = new JLabel("users");
		flag.addPropertyChangeListener(new PropertyChangeListener() {

			@Override
			public void propertyChange(PropertyChangeEvent e) {
				if (e.getSource() == flag) {
					isSelected = true;
					Object[] data = scroll.getRow(scroll.getSelectedRowNum());
					id.setText((String) data[1]);
					String sub = (String) data[3];
					isASub = sub.equals("Yes") ? true : false;
					String mod = (String) data[4];
					isAMod = mod.equals("Yes") ? true : false;
					
				}
			}
		});
		//scroll.setFlag(flag);
		
		setLayout(new BorderLayout());
		add(pane, BorderLayout.CENTER);
		add(misc(), BorderLayout.SOUTH);
	} //generalSetup
	
	private JScrollPane noViewerPanel() {
		JPanel panel = new JPanel();
		panel.add(new JLabel("No viewers currently."));
		return new JScrollPane(panel);
	}
	
	/**
	 * Include user information on bottom of panel: add/subtract points from a user, user metadata
	 */
	private Container misc() {
		Container border = new Container();
		border.setLayout(new BorderLayout());
		border.add(pointManagement(), BorderLayout.NORTH);
		border.add(info(), BorderLayout.CENTER);
		border.add(other(), BorderLayout.EAST);
		return border;
	} //misc
	
	/**
	 * Textfield and buttons for adding/subtracting points from a user selected in the panel.
	 * 
	 * @return container included buttons and textfield
	 */
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
				String input = points.getText();
				if (isSelected) {
					if (input != null && !input.isEmpty()) {
						if (!input.equals(MESSAGE)) {
							try {				//NEED TO ADD POINTS TO THE USER HERE!!!!!!!!!!!!!!!!!!!!!!
								int p = Integer.parseInt(input);
								my_bot.getAllUnP().incrementTankerPoints(id.getText(), p);
								JOptionPane.showMessageDialog(null, p + " points has been added to " + id.getText() + "!", "Update", JOptionPane.INFORMATION_MESSAGE);
								points.setForeground(Color.GRAY);
								points.setText(MESSAGE);

							} catch (NumberFormatException e) {
								JOptionPane.showMessageDialog(null, "Please input a number (integer)!", "Input Error", JOptionPane.ERROR_MESSAGE);
							}
						}
					} else {
						JOptionPane.showMessageDialog(null, "Please input desired number to add points!", "Update", JOptionPane.WARNING_MESSAGE);
					}
				} else {
					JOptionPane.showMessageDialog(null, "Please select a user first before adding points!", "Update", JOptionPane.WARNING_MESSAGE);
				}
			}
		});
		
		sub.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {			
				String input = points.getText();
				if (isSelected) {
					if (input != null && !input.isEmpty()) {
						if (!input.equals(MESSAGE)) {
							try {				//NEED TO ADD POINTS TO THE USER HERE!!!!!!!!!!!!!!!!!!!!!!
								int p = Integer.parseInt(input);
								my_bot.getAllUnP().incrementTankerPoints(id.getText(), -p);
								JOptionPane.showMessageDialog(null, p + " points has been taken from " + id.getText() + "!", "Update", JOptionPane.INFORMATION_MESSAGE);
								points.setForeground(Color.GRAY);
								points.setText(MESSAGE);

							} catch (NumberFormatException e) {
								JOptionPane.showMessageDialog(null, "Please input a number (integer)!", "Input Error", JOptionPane.ERROR_MESSAGE);
							}
						}
					} else {
						JOptionPane.showMessageDialog(null, "Please input desired number to subtract points!", "Update", JOptionPane.WARNING_MESSAGE);
					}
				} else {
					JOptionPane.showMessageDialog(null, "Please select a user first before subtracting points!", "Update", JOptionPane.WARNING_MESSAGE);
				}
			}
		});
		
		flow.add(points);
		flow.add(add);
		flow.add(sub);
		return flow;
	} //pointManagement
	
	/**
	 * Display information about the channel's viewers: total viewers, total moderators, total points current viewer
	 * accumulated, total subscribers
	 * 
	 * @return container including labels displaying information.
	 */
	private Container info() {
		Container grid = new Container();
		grid.setLayout(new GridLayout(4, 1));

		Container viewers = new Container();
		viewers.setLayout(new FlowLayout(FlowLayout.LEFT));
		JLabel lbview = new JLabel("Total Viewers:");
		JLabel txview = new JLabel("" + my_bot.getCurUsers().length);
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
		int total = 0;
		JLabel txvpt = new JLabel();
		if (my_bot.getCurUsers().length > 0) {
			User[] current = my_bot.getCurUsers();
			Map<User, Integer> map = my_bot.getAllUnP().getUserMap();
			for (int i = 0; i < current.length; i++) {
				total += map.get(current[i]);
			}
			txvpt = new JLabel("" + total);
		} else {
			txvpt = new JLabel("" + my_bot.getCurUsers().length);
		}
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
	
	/**
	 * For future features identifying unique users: subscriber, moderator, "special".
	 * 
	 * @return container including checkboxes.
	 */
	private Container other() {
		Container grid = new Container();
		grid.setLayout(new GridLayout(3, 1));

		JCheckBox isSub = new JCheckBox("Is Sub?");
		isSub.setHorizontalTextPosition(SwingConstants.LEFT);
		isSub.setSelected(isASub);
		isSub.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
		});
		
		JCheckBox isMod = new JCheckBox("Is Mod?");
		isMod.setHorizontalTextPosition(SwingConstants.LEFT);
		isMod.setSelected(isAMod);
		isMod.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				
			}
			
		});
		
		JCheckBox isSpecial = new JCheckBox("Special");
		isSpecial.setHorizontalTextPosition(SwingConstants.LEFT);
		isSpecial.setSelected(isVIP);
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
