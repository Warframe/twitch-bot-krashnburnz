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



import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import Model.MyBot;

/**
 * User tab displaying all users: rank (by points accumulated), user-name (nick), points, sub/mod.
 * Including add/subtract points ability from program + user general information.
 * 
 * @author Ching-Ting Huang
 */
public class Users extends JPanel {
	
	private static final long serialVersionUID = -2544375717703845827L;
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
	 * After selecting a viewer from the scroll pane, this is that user's join date to the streamer's channel.
	 */
	private JLabel joinDate;
	//private Date joinDate;
	
	/**
	 * After selecting a viewer from the scroll pane, this is that user's email address.
	 */
	private JLabel email;
	
	/**
	 * Selected viewer's name/ID/nick-name.
	 */
	private JLabel id;
	
	/**
	 * Selected viewer's total accumulated points.
	 */
	private JLabel pt;
	
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
	public Users(MyBot bot) {
		my_bot = bot;
		scroll = new ScrollPane(bot, "user");
		joinDate = new JLabel("0/0/0");
		email = new JLabel("Not Available");
		id = new JLabel("");
		pt = new JLabel("");
		setup();
	}
	
	/**
	 * General layout of this panel: scroll pane, user metadata. 
	 */
	private void setup() {
		JScrollPane pane = scroll.getScrollPane();
		final JLabel flag = new JLabel("users");
		flag.addPropertyChangeListener(new PropertyChangeListener() {

			@Override
			public void propertyChange(PropertyChangeEvent e) {
				if (e.getSource() == flag) {
					isSelected = true;
					
					int selectedRow = scroll.getSelectedRowNum();
					id.setText((String) scroll.getData(selectedRow, 1));
					pt.setText(String.valueOf(scroll.getData(selectedRow, 2)));
					String sub = (String) scroll.getData(selectedRow, 3);
					isASub = sub.equals("Yes") ? true : false;
					String mod = (String) scroll.getData(selectedRow, 4);
					isAMod = mod.equals("Yes") ? true : false;

					//join date to-be-implemented
					//e-mail to-be-implemented
				}
			}
		});
		scroll.setFlag(flag);
		
		setLayout(new BorderLayout());
		add(pane, BorderLayout.CENTER);
		add(misc(), BorderLayout.SOUTH);
	} //generalSetup
	
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
		JButton search = new JButton("Search");
		
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
								scroll.updateTable(scroll.getSelectedRowNum(), 2, -p);								JOptionPane.showMessageDialog(null, p + " points has been taken from " + id.getText() + "!", "Update", JOptionPane.INFORMATION_MESSAGE);
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
		
		search.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				boolean found = false;
				String input = JOptionPane.showInputDialog("Please enter the username you are searching for");
				for (int i = 0; i < scroll.getNumRow() - 1; i++) {
					if (input.equals(scroll.getData(i, 1))) {
						scroll.scrollTo(i);
						scroll.setFocusTo(i);
						found = true;
					}
				}
				
				if (!found) {
					JOptionPane.showMessageDialog(null, "No match found. Please try again.", "Search Result", JOptionPane.INFORMATION_MESSAGE);
				}
			}
		});
		
		
		flow.add(points);
		flow.add(add);
		flow.add(sub);
		flow.add(search);
		return flow;
	} //pointManagement
	
	/**
	 * Select a user from panel will display the user's metadata as follows: Join date, total points accumulated, email,
	 * nick-name, other misc. info.
	 * 
	 * @return container including labels displaying information.
	 */
	private Container info() {
		Container grid = new Container();
		grid.setLayout(new GridLayout(4, 2));

		Container join = new Container();
		join.setLayout(new FlowLayout(FlowLayout.LEFT));
		JLabel lbjoin = new JLabel("Join Date:");
		join.add(lbjoin);
		join.add(joinDate);
		
		Container total = new Container();
		total.setLayout(new FlowLayout(FlowLayout.LEFT));
		JLabel lbtotal = new JLabel("Total:");
		total.add(lbtotal);
		total.add(pt);
		
		Container mail = new Container();
		mail.setLayout(new FlowLayout(FlowLayout.LEFT));
		JLabel lbmail = new JLabel("E-Mail:");
		mail.add(lbmail);
		mail.add(email);
		
		Container misc1 = new Container();
		misc1.setLayout(new FlowLayout(FlowLayout.LEFT));
		JLabel lbmisc1 = new JLabel("Misc Info:");
		JLabel txmisc1 = new JLabel("N/A");
		misc1.add(lbmisc1);
		misc1.add(txmisc1);
		
		Container nick = new Container();
		nick.setLayout(new FlowLayout(FlowLayout.LEFT));
		JLabel lbid = new JLabel("Misc ID:");
		nick.add(lbid);
		nick.add(id);
		
		Container misc2 = new Container();
		misc2.setLayout(new FlowLayout(FlowLayout.LEFT));
		JLabel lbmisc2 = new JLabel("Misc Info:");
		JLabel txmisc2 = new JLabel("N/A");
		misc2.add(lbmisc2);
		misc2.add(txmisc2);
		
		Container misc3 = new Container();
		misc3.setLayout(new FlowLayout(FlowLayout.LEFT));
		JLabel lbmisc3 = new JLabel("Misc Info:");
		JLabel txmisc3 = new JLabel("N/A");
		misc3.add(lbmisc3);
		misc3.add(txmisc3);
		
		Container misc4 = new Container();
		misc4.setLayout(new FlowLayout(FlowLayout.LEFT));
		JLabel lbmisc4 = new JLabel("Misc Info:");
		JLabel txmisc4 = new JLabel("N/A");
		misc4.add(lbmisc4);
		misc4.add(txmisc4);

		grid.add(join);
		grid.add(total);
		grid.add(mail);
		grid.add(misc1);
		grid.add(nick);
		grid.add(misc2);
		grid.add(misc3);
		grid.add(misc4);
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
} //class
