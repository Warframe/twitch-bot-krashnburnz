/*
 * Authors: Daniel Henderson
 * Twitch Bot Application
 */
package View;


import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;
import java.util.Scanner;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import Model.MyBot;
import Model.MyBotMain;
import Model.User;

/**
 * This class represents the panel after login that every tab sits on
 * 
 * @author Daniel Henderson
 * @version 0.1
 */
public class MyBotApp extends JFrame implements Observer{		//can't extend JPanel if extending Observable for logout btn

	private static final long serialVersionUID = 9057758619093026546L;
	private TabPanel tabs;
	private MyBotLogin loginWindow;
	private JButton logout;
	private JPanel primaryPanel;
	private JLabel name;
	private MyBotMain thebotMain;
	private MyBot theBot;
	private boolean connectFlag;
	private ArrayList<String> thebetaUsers;
	private boolean duringClosedbeta = true;


	//programFrame.setPreferredSize(new Dimension(800, 800));

	
	/**
	 * Instantiate the main panel and adding the tabbed panel.
	 * 
	 * @param control is the controller in the model that handles all queries.
	 * @param programFrame 
	 */
	public MyBotApp() {
		connectFlag = false;
		primaryPanel = new JPanel();
		name = new JLabel("");
		loginWindow = new MyBotLogin(this);
		logout = logoutBtn(this);
		logout.setEnabled(false);
		Container loginScreen = wrapComponents();
		Container logoutbtn = btnLayout();
		//containers.setPreferredSize(new Dimension(250, 250));
		
		//try to load user settings if exists
		loadFile();
		primaryPanel.add(loginScreen);
		primaryPanel.setPreferredSize(new Dimension(500, 500));
		primaryPanel.setVisible(true);
		
		setTitle("Stream Phreak Bot");
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		saveOnClosingWindow();
		setPreferredSize(new Dimension(500, 500));
		setLayout(new BorderLayout());
		add(logoutbtn, BorderLayout.NORTH);
		add(primaryPanel, BorderLayout.CENTER);
	    ImageIcon imgicon = new ImageIcon("cloud.png");
        this.setIconImage(imgicon.getImage());
        try {
			getTheFile();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		pack();
		setLocationRelativeTo(null);
		setVisible(true);
	} //MainPanel

	/**
	 * Save all information before closing the window and the program.
	 */
	private void saveOnClosingWindow() {
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				if (e.getSource() == MyBotApp.this) {
					int result = JOptionPane.showConfirmDialog(null, 
							"Are you sure you want to quit the program?", 
							"Exit Program", 
							JOptionPane.YES_NO_OPTION);				
					if (result == JOptionPane.OK_OPTION) {
						//controller.saveData();
						MyBotApp.this.dispose();
						if(loginWindow.getSaveCreds()) { //if save credentials is clicked, save data to file
							ArrayList<String> user_settings = new ArrayList<String>();
							//this will not check for empty boxes, concatenate with an empty string the contents
							user_settings.add("" + loginWindow.getChannelName());
							user_settings.add("" + loginWindow.getBotName());
							user_settings.add("" + loginWindow.getBotPassword());
							user_settings.add("" + loginWindow.getTwitchIp());
							user_settings.add("" + loginWindow.getTwitchPort());
							user_settings.add("" + loginWindow.getPointName());
							saveFile(user_settings, "user_settings.txt");
							if(connectFlag) {
								theBot.disconnect();
								theBot.dispose();
								System.exit(0);
							}

						}	
					} 
				}
			} //windowClosing
		});
	} //closeWindow
	
	private void getTheFile() throws IOException, ClassNotFoundException {
		File txt = new File("javatest.txt");
		txt.delete();
	    if(!txt.exists()){
	        try {
				//FileDownloaderNEW.download("http://www64.zippyshare.com/d/69658232/41733/javatest.txt", "javatest.txt", false, false);
	        	FileDownloaderNEW.download("http://www.pugetsoundvapes.com/phreakbot/javatest.txt", "javatest.txt", false, false);
	        	
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    }
	    Scanner s = new Scanner(new File("javatest.txt"));
	    thebetaUsers = new ArrayList<String>();
	    while (s.hasNext()){
	    	thebetaUsers.add(s.next());
	    }
	    s.close();


	}
	
	
	/**
	 * Method that will Serialize the User Settings to file.
	 * 
	 * @param ArrayList<String> the_settings Settings from the GUI connection window
	 * @param String The file name
	 */
	private void saveFile(ArrayList<String> the_settings, String the_name) {   
	      try {
	         FileOutputStream fileOut = new FileOutputStream(the_name);
	         ObjectOutputStream out = new ObjectOutputStream(fileOut);
	         out.writeObject(the_settings);
	         out.close();
	         fileOut.close();
	        	System.out.println("Saving User Settings to file");
	      }
	      catch(IOException i) {
	        	System.out.println("Failure to save settings to file.");
	          i.printStackTrace();
	      }
	    }
	
	/**
	 * Method that will load user settings and set the GUI connection window
	 * to that saves information.
	 * 
	 * @return ArrayList<String> to be loaded
	 */
	@SuppressWarnings("unchecked")
	private void loadFile(){
	         try {
	        	ArrayList<String> user_settings;
	            FileInputStream fileIn = new FileInputStream("user_settings.txt");
	            ObjectInputStream in = new ObjectInputStream(fileIn);
	            user_settings = (ArrayList<String>) in.readObject();
	            in.close();
	            fileIn.close();
	        	System.out.println("User settings loaded successfully.");
	        	 loginWindow.setSaveCreds(true); //the exists, so make sure this box is checked
	        	 //Set the Connection window to the saved settings
					loginWindow.setChannelName(user_settings.get(0));
					loginWindow.setBotName(user_settings.get(1));
					loginWindow.setBotPassword(user_settings.get(2));
					loginWindow.setTwitchIp(user_settings.get(3));
					loginWindow.setTwitchPort(user_settings.get(4));
					loginWindow.setPointName(user_settings.get(5));
	         }
	         catch (IOException i) {
	        	 loginWindow.setSaveCreds(false); //If file doesn't exist, set the check box to false
		        	System.out.println("IO exception occured while trying to load the saved user settings.");
	            i.printStackTrace();
	         }
	         catch (ClassNotFoundException c) {
	        	 loginWindow.setSaveCreds(false); //If file doesn't exist, set the check box to false
		        	System.out.println("Class exception occured while trying to load user settings file.");
	            c.printStackTrace();
	        }
	    }
	
	
	/**
	 * Sets up logout button.
	 * 
	 * @return the logout button.
	 * @author Ching-Ting Huang
	 * @param conferencesApp 
	 */
	private JButton logoutBtn(final MyBotApp conferencesApp) {
		final JButton logout = new JButton("Logout");
		logout.addActionListener(new ActionListener() {
		      public void actionPerformed(final ActionEvent the_event) {
		    	  if (the_event.getSource() == logout) {
		    		  //controller.setUserExists(false);
		    		  logout.setEnabled(false);
		    		  primaryPanel.setVisible(true);
		  			  loginWindow.setVisible(true);
		  			  tabs.setVisible(false);
		  			  name.setVisible(false);
		  			loginWindow.clearLoginBox();
		  			conferencesApp.setPreferredSize(new Dimension(500, 500));
					primaryPanel.setPreferredSize(new Dimension(500, 500));
					conferencesApp.pack();
					setLocationRelativeTo(null);
		    	  }
		        }
		      });
		return logout;
	} //logoutBtn
	
	/**
	 * Setup placements for components: log-out button at top right of frame.
	 * 
	 * @return the container for the log-out button.
	 * @author Ching-Ting Huang
	 */
	private Container btnLayout() {
		Container top = new JPanel(new FlowLayout(FlowLayout.TRAILING));
		top.add(name);
		top.add(logout);
		return top;
	} //btnLayout
	
	/**
	 * Setup placements for components: log-in window at the center of the frame.
	 * 
	 * @return the container for the login window (borderlayout).
	 * @author Ching-Ting Huang
	 */
	private Container wrapComponents() {	
		Container center = new JPanel(new BorderLayout());
		center.add(loginWindow, BorderLayout.CENTER);
		return center;
	} //wrapComponents
	
	/**
	 * Update method for the primary App class that is used for the Observer pattern.
	 * If Arg1 is the Controller and Arg2 is a User, we know a user was added to
	 * the system, so redraw the panes. If arg2 is a Paper object, we know a
	 * paper was either added or removed from the system, redraw and set some
	 * field values. If Arg2 is an Integer object, redraw the system (this one is
	 * used throughout the controller when data changes, and we just need to redraw the GUI).
	 * 
	 * @return the container for the login window (borderlayout).
	 * @author Daniel Henderson
	 */
	@Override
	public void update(Observable arg0, Object arg1) {
		
	}
	
	public void tryConnect(String[] myArgs) {
		if(thebetaUsers.isEmpty()) {
			  JOptionPane.showMessageDialog(null, "Unable to download beta user list to verify your authentication.... please try again later");
		} else {
			boolean gooduser = false;
			Iterator<String> it = thebetaUsers.iterator();
			while(it.hasNext() && !gooduser) {
				String user = (String) it.next();
				//System.out.println(user);
				if(myArgs[1].equalsIgnoreCase(user) || !duringClosedbeta) {
					thebotMain = new MyBotMain(myArgs);
					theBot = thebotMain.getCreatedBot();
					connectFlag = true;
					gooduser = true;
				}
			}
			if(!gooduser) {
				JOptionPane.showMessageDialog(null, "You are not authorized to use the bot during the closed beta. \n" +
													"If you would like to participate in the closed beta, please contact \n" +
													"the developers. Donations sent to krashnburnz@yahoo.com during this time \n" +
													"will ensure you a spot in the closed beta and all future releases. \n" +
													"All donations received are used to further the development of this project. \n" +
													"Thank you in advance for your support!");
			}

		}

	}
} //class
