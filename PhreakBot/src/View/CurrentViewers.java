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
	 * Controller
	 */
	private MyBotApp my_bot_app;
	
	/**
	 * The scroll pane containing all viewers' metadata that has ever been to the streamer's channel.
	 */
	private ScrollPane scroll;
	
	/**
	 * Selected viewer's name/ID/nick-name.
	 */
	private JLabel id;
	
	
	
	private JLabel viewerNum;
	private JLabel totalPt;
	
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
	 * @param myBotApp 
	 */
	public CurrentViewers(MyBot bot, MyBotApp myBotApp) {
		my_bot = bot;
		my_bot_app = myBotApp;
		scroll = new ScrollPane(my_bot, "viewer");
		viewerNum = new JLabel("0");
		totalPt = new JLabel("0");
		id = new JLabel("Please select a user.");
		setup();
	} //constructor
	
	/**
	 * General layout of this panel: scroll pane, user metadata. 
	 */
	private void setup() {
		JScrollPane pane = scroll == null ? noViewerPanel() : scroll.getScrollPane();
		final JLabel flag = new JLabel("flag");
		flag.addPropertyChangeListener(new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent e) {
				if (e.getSource() == flag) {
					isSelected = true;
					int selectedRow = scroll.getSelectedRowNum();
					id.setText((String) scroll.getData(selectedRow, 1));
					String sub = (String) scroll.getData(selectedRow, 3);
					isASub = sub.equals("Yes") ? true : false;
					String mod = (String) scroll.getData(selectedRow, 4);
					isAMod = mod.equals("Yes") ? true : false;
					
				}
			}
		});
		
		final JLabel checker = new JLabel("checker");
		checker.addPropertyChangeListener(new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent e) {
				if (e.getSource() == checker) {
					int viewers = scroll.getNumRow();
					int sum = 0;
					
					viewerNum.setText("" + viewers);
					for (int i = 0; i < viewers; i++) {
						if (scroll.getData(i, 2) instanceof Integer) {
							sum += (int) scroll.getData(i, 2);

						} else if (scroll.getData(i, 2) instanceof String) {
							sum += Integer.parseInt((String) scroll.getData(i, 2));

						}
					}
					totalPt.setText("" + sum);
				}
			}
		});
		scroll.setFlag(flag);
		scroll.setCVChecker(checker);
		
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
		final JButton add = new JButton("+ Points");
		final JButton sub = new JButton("- Points");
		final JButton addAll = new JButton("+ Give All");
		
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
							try {				
								int p = Integer.parseInt(input);
								my_bot.getAllUnP().incrementTankerPoints(id.getText(), p);
								scroll.updateTable(scroll.getSelectedRowNum(), 2, p);
								my_bot_app.updateView(1, id.getText(), p);
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
							try {				
								int p = Integer.parseInt(input);
								my_bot.getAllUnP().incrementTankerPoints(id.getText(), -p);
								scroll.updateTable(scroll.getSelectedRowNum(), 2, -p);
								my_bot_app.updateView(1, id.getText(), -p);
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
		
		addAll.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (e.getSource() == addAll) {
					if (scroll.getNumRow() > 0) {
						int response = JOptionPane.NO_OPTION;
						try {
							String input = JOptionPane.showInputDialog("Feelin' generous! Points give away time!");
							int points = (input == null) ? 0 : Integer.parseInt(input);
							//int points = Integer.parseInt(JOptionPane.showInputDialog("Feelin' generous! Points give away time!"));
							if (points > 0) {
								response = JOptionPane.showConfirmDialog(null, "Ready to be worshipped?", "Point GIVEaway", JOptionPane.YES_NO_OPTION);
		
							} else if (points < 0){
								response = JOptionPane.showConfirmDialog(null, "Are you suuure? That minus sign will produce many sad faces...", "Point TAKEaway", JOptionPane.YES_NO_OPTION);
							}
							
							if (response == JOptionPane.YES_OPTION) {
								User[] currentUser = my_bot.getCurUsers();
								for (int i = 0; i < currentUser.length; i++) {
									my_bot.getAllUnP().incrementTankerPoints(currentUser[i].getNick(), points);
									scroll.updateTable(i, 2, points);
								}
							}
						} catch (NumberFormatException ex) {
							JOptionPane.showMessageDialog(null, "Integer points would be nice.", "Wrong Format", JOptionPane.INFORMATION_MESSAGE);
						}
					} else {
						JOptionPane.showMessageDialog(null, "No one's here to witness your generosity...", "Sad-face", JOptionPane.INFORMATION_MESSAGE);
					}
				}
			}
		});
		
		flow.add(points);
		flow.add(add);
		flow.add(sub);
		flow.add(addAll);
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
		viewers.add(lbview);
		viewers.add(viewerNum);
		
		Container mods = new Container();
		mods.setLayout(new FlowLayout(FlowLayout.LEFT));
		JLabel lbmods = new JLabel("Total Moderators:");
		JLabel txmods = new JLabel("N/A");
		mods.add(lbmods);
		mods.add(txmods);
		
		Container vpoints = new Container();
		vpoints.setLayout(new FlowLayout(FlowLayout.LEFT));
		JLabel lbvpt = new JLabel("Total Points:");
		JLabel txvpt = new JLabel();
		vpoints.add(lbvpt);
		vpoints.add(totalPt);
		
		Container vsubs = new Container();
		vsubs.setLayout(new FlowLayout(FlowLayout.LEFT));
		JLabel lbvsubs = new JLabel("Total Subscribers:");
		JLabel txvsubs = new JLabel("N/A");
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
	
	/**
	 * Used for shutting down program.
	 * 
	 * @return whether the timer is still running.
	 */
	public boolean stopTimer() {
		return scroll.stopUpdateTimer();
	} //stopTimer
	
	/**
	 * Method used to keep the Panels in sync as far as User points are concerned.
	 * When other panels add/subtract points, this they callback to the GUi, that
	 * will then call this method to update this panel.
	 * @param userName
	 * @param points
	 */
	public void updateMyView(String userName, int points) {
		boolean found = false;
		for (int i = 0; i < scroll.getNumRow(); i++) {
			if (userName.equals(scroll.getData(i, 1))) {
				scroll.scrollTo(i);
				scroll.setFocusTo(i);
				found = true;
				scroll.updateTable(scroll.getSelectedRowNum(), 2, points);	
			}
		}
		
		if (!found) {
			JOptionPane.showMessageDialog(null, "No match found CurrentViewers list. Please try again.", "Search Result", JOptionPane.INFORMATION_MESSAGE);
		}
		
	}
} //CurrentViewers
