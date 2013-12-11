/*
 * Authors: Daniel Henderson
 * Twitch Bot Application
 */
package View;


import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Observable;
import java.util.Observer;
import java.util.Scanner;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.KeyStroke;

import Model.MyBot;
import Model.MyBotMain;

/**
 * This class represents the panel after login that every tab sits on
 * 
 * @author Daniel Henderson
 * @version 0.1
 */
public class MyBotApp extends JFrame implements Observer, Runnable{		//can't extend JPanel if extending Observable for logout btn

	private static final long serialVersionUID = 9057758619093026546L;
	private Icon loadImage;
	private JLabel imageLabel;
	private JPanel consolePanel;
	private Console console;
	private TabPanel tabs;
	private MyBotLogin loginWindow;
	private JButton logout;
	private JPanel primaryPanel;
	private JLabel name;
	private MyBotMain thebotMain;
	private MyBot theBot;
	private boolean connectFlag;
	private ArrayList<String> thebetaUsers;
	private String [] theUsersToCheck;
	private boolean duringClosedbeta = true;
	private final JCheckBoxMenuItem debugSettingItem;
	private Frame tempFrame = new JFrame();
	private Thread threadBtn;


	//programFrame.setPreferredSize(new Dimension(800, 800));

	
	/**
	 * Instantiate the main panel and adding the tabbed panel.
	 * 
	 * @param control is the controller in the model that handles all queries.
	 * @param programFrame 
	 * @throws MalformedURLException 
	 */
	public MyBotApp() {
		//create menu bar for window
		tempFrame = new JFrame("Connection Console");
		tempFrame.addWindowListener(new java.awt.event.WindowAdapter() {
		    @SuppressWarnings("deprecation")
			@Override
		    public void windowClosing(java.awt.event.WindowEvent windowEvent) {
		    	if(threadBtn.isAlive() ) {
		    		threadBtn.stop();
		    	}
		    	if(theBot != null) {
					theBot.disconnect();
					theBot.dispose();	
		    	}
	    		  logout.setEnabled(false);
	    		  loginWindow.setConnectBtnName("Connect");
	    			debugSettingItem.setEnabled(true);
					loginWindow.isConnectBtnEnabled(true);
					loginWindow.isBotNametextEnabled(true);
					loginWindow.isOathtextEnabled(true);
					loginWindow.isChanneltextEnabled(true);
					loginWindow.isIPtextEnabled(true);
					loginWindow.isPorttextEnabled(true);
					loginWindow.isPointstextEnabled(true);
					loginWindow.isCredsCheckEnabled(true);
		    }
		});
		console = new Console();
		JMenuBar menuBar = new JMenuBar();;
		JMenu file = new JMenu("File");
		JMenu settings = new JMenu("Settings");
		JMenu helpMenu = new JMenu("Help");
		JMenu supportMenu = new JMenu("Support");
		settings.setMnemonic(KeyEvent.VK_S);
		settings.getAccessibleContext().setAccessibleDescription(
		        "General Bot Settings");
		file.setMnemonic(KeyEvent.VK_F);
		file.getAccessibleContext().setAccessibleDescription(
		        "File Settings");
		helpMenu.setMnemonic(KeyEvent.VK_H);
		helpMenu.getAccessibleContext().setAccessibleDescription(
		        "Help");
		supportMenu.setMnemonic(KeyEvent.VK_U);
		supportMenu.getAccessibleContext().setAccessibleDescription(
		        "Support Project");

		JMenuItem exiMenuItem = new JMenuItem("Exit",
                KeyEvent.VK_E);
		JMenuItem DocumentMenuItem = new JMenuItem("Documentation",
                KeyEvent.VK_D);
		JMenuItem AboutMenuItem = new JMenuItem("About",
                KeyEvent.VK_A);
		JMenuItem DevMenuItem = new JMenuItem("Help Develop",
                KeyEvent.VK_V);
		JMenuItem DonateMenuItem = new JMenuItem("Donate",
                KeyEvent.VK_O);
		debugSettingItem = new JCheckBoxMenuItem("Debug ON");
		debugSettingItem.setEnabled(true);
		debugSettingItem.setState(true);
		debugSettingItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                if (debugSettingItem.isSelected()) {
                	thebotMain.isDebugOn(true);
                } else {
                	thebotMain.isDebugOn(false);
                }
              }

          });
		exiMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_1, ActionEvent.ALT_MASK));
		exiMenuItem.getAccessibleContext().setAccessibleDescription("Exit the program...");
		exiMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
            	shutDownAndSave();
              }

          });
		
		DocumentMenuItem.addActionListener(new ActionListener() {
		      public void actionPerformed(final ActionEvent the_event) {
		    	  try {
					openWebpage(new URL("http://pugetsoundvapes.com/phreakbot/document/Phreak_Bot_About_Help.pdf"));
				} catch (MalformedURLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		        }
		      });
		
		DonateMenuItem.addActionListener(new ActionListener() {
		      public void actionPerformed(final ActionEvent the_event) {
		    	  try {
					openWebpage(new URL("https://www.paypal.com/cgi-bin/webscr?cmd=_s-xclick&hosted_button_id=AZ7PBS9ZSQLQA"));
				} catch (MalformedURLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		        }
		      });
		
		AboutMenuItem.addActionListener(new ActionListener() {
		      public void actionPerformed(final ActionEvent the_event) {
		  		JOptionPane.showMessageDialog(null, "Phreak Bot© was created by Daniel Henderson 2012\n" +
						"krashnburnz@yahoo.com \n" +
						"Please visit the project's home page for more information. \n" +
						"https://code.google.com/p/twitch-bot-krashnburnz/\n" +
						"This bot also use the PircBot Api created by Paul James Mutton");
		        }
		      });
		DevMenuItem.addActionListener(new ActionListener() {
		      public void actionPerformed(final ActionEvent the_event) {
		  		JOptionPane.showMessageDialog(null, "If you would like to contribute your time into improving \n" +
						"this project, please send the head developer an email and/or visit \n" +
						"the project's homepage at the URL listed below. \n" +
						"Head Developer's Email: krashnburnz@yahoo.com \n" +
						"Project Website: https://code.google.com/p/twitch-bot-krashnburnz/");
		        }
		      });

		
		file.add(exiMenuItem);
		settings.add(debugSettingItem);
		helpMenu.add(DocumentMenuItem);
		helpMenu.add(AboutMenuItem);
		supportMenu.add(DevMenuItem);
		supportMenu.add(DonateMenuItem);
		menuBar.add(file);
		menuBar.add(settings);
		menuBar.add(helpMenu);
		menuBar.add(supportMenu);
		setJMenuBar(menuBar);
		
		
		//create login window and various other panels
		connectFlag = false;
		primaryPanel = new JPanel();
		name = new JLabel("");
		loginWindow = new MyBotLogin(this);
		Thread loginThread = new Thread(loginWindow);
		loginThread.start();
		logout = logoutBtn(this);
		logout.setEnabled(false);
		Container loginScreen = wrapComponents();
		Container logoutbtn = btnLayout();
		
		//create panels for console
		consolePanel = console.getPanel();
		consolePanel.setVisible(true);

		//create load screen image for logging in
		try {
			loadImage = new ImageIcon(new URL("http://quicktake.morningstar.com/index/images/LoadingScreenAnimation.gif"));
		} catch (MalformedURLException e1) {
			System.out.println("Unable to load Loading image..");
			e1.printStackTrace();
		}
		imageLabel = new JLabel(loadImage);
		imageLabel.setVisible(false);
		//try to load user settings if exists
		loadFile();

		primaryPanel.add(loginScreen);
		
		//Create tempt frame for console while trying to connect
		primaryPanel.setPreferredSize(new Dimension(500, 500));
		primaryPanel.setVisible(true);
		
		setTitle("Stream Phreak Bot");
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		saveOnClosingWindow();
		setPreferredSize(new Dimension(500, 520));
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
	
	private static void setup() {
		
	}

	/**
	 * Save all information before closing the window and the program.
	 */
	private void saveOnClosingWindow() {
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				if (e.getSource() == MyBotApp.this) {
					shutDownAndSave();
				}
			} //windowClosing
		});
	} //closeWindow
	
	public static void openWebpage(URL url) {
	    try {
	        openWebpage(url.toURI());
	    } catch (URISyntaxException e) {
	        e.printStackTrace();
	    }
	}
	
	public static void openWebpage(URI uri) {
	    Desktop desktop = Desktop.isDesktopSupported() ? Desktop.getDesktop() : null;
	    if (desktop != null && desktop.isSupported(Desktop.Action.BROWSE)) {
	        try {
	            desktop.browse(uri);
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	    }
	}
	
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
	 * @author Daniel Henderson, Ching-Ting Huang
	 * @param conferencesApp 
	 */
	private JButton logoutBtn(final MyBotApp conferencesApp) {
		final JButton logout = new JButton("Disconnect");
		logout.addActionListener(new ActionListener() {
		      public void actionPerformed(final ActionEvent the_event) {
		    	  if (the_event.getSource() == logout) {
		    		  if(theBot.isConnected()) {
			    		  theBot.disconnect();
		    		  }
		    		  logout.setEnabled(false);
						primaryPanel.remove(consolePanel);
						primaryPanel.setVisible(false);
						tempFrame.setPreferredSize(new Dimension(400, 400));
						tempFrame.add(consolePanel);
						tempFrame.pack();
						tempFrame.setVisible(true);
		    		  loginWindow.setConnectBtnName("Connect");
		    			debugSettingItem.setEnabled(true);
						loginWindow.isConnectBtnEnabled(true);
						loginWindow.setVisible(true);
						primaryPanel.setVisible(true);
						theBot.wantDisconnect(true);
						theBot.dispose();
						loginWindow.isBotNametextEnabled(true);
						loginWindow.isOathtextEnabled(true);
						loginWindow.isChanneltextEnabled(true);
						loginWindow.isIPtextEnabled(true);
						loginWindow.isPorttextEnabled(true);
						loginWindow.isPointstextEnabled(true);
						loginWindow.isCredsCheckEnabled(true);
						//consolePanel.setVisible(false);
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
	
	private void shutDownAndSave() {

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
			}
			if(connectFlag) {
				theBot.disconnect();
				theBot.dispose();
				System.exit(0);
			} else {
				System.exit(0);
			}
		}
	}
	
	
	@SuppressWarnings("deprecation")
	public void tryConnect(String[] myArgs) {
		if(thebetaUsers.isEmpty()) {
			  JOptionPane.showMessageDialog(null, "Unable to download beta user list to verify your authentication.... please try again later");
		} else {
			loginWindow.isConnectBtnEnabled(false);
			tempFrame.setPreferredSize(new Dimension(400, 400));
			tempFrame.add(consolePanel);
			tempFrame.pack();
			tempFrame.setLocationRelativeTo(this);
			tempFrame.setVisible(true);
			//primaryPanel.add(consolePanel);
			AnimateConnect animatebtn = new AnimateConnect(loginWindow);
			threadBtn = new Thread(animatebtn);
			threadBtn.start();
			loginWindow.isBotNametextEnabled(false);
			loginWindow.isOathtextEnabled(false);
			loginWindow.isChanneltextEnabled(false);
			loginWindow.isIPtextEnabled(false);
			loginWindow.isPorttextEnabled(false);
			loginWindow.isPointstextEnabled(false);
			loginWindow.isCredsCheckEnabled(false);
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
					if(theBot.isConnected()) {
						tempFrame.remove(consolePanel);
						tempFrame.setVisible(false);
						primaryPanel.add(consolePanel);
						debugSettingItem.setEnabled(false);
						loginWindow.isConnectBtnEnabled(false);
						loginWindow.setVisible(false);
						consolePanel.setVisible(true);
						logout.setEnabled(true);
						theBot.wantDisconnect(false);
						debugSettingItem.setEnabled(true);
						threadBtn.stop();
					}		
				}
			}
			if(!gooduser) {
				threadBtn.stop();				
				loginWindow.setConnectBtnName("Connect");
				loginWindow.isConnectBtnEnabled(true);
				JOptionPane.showMessageDialog(null, "You are not authorized to use the bot during the closed beta. \n" +
													"If you would like to participate in the closed beta, please contact \n" +
													"the developers. Donations sent to krashnburnz@yahoo.com during this time \n" +
													"will ensure you a spot in the closed beta and all future releases. \n" +
													"All donations received are used to further the development of this project. \n" +
													"Thank you in advance for your support!");

				loginWindow.isBotNametextEnabled(true);
				loginWindow.isOathtextEnabled(true);
				loginWindow.isChanneltextEnabled(true);
				loginWindow.isIPtextEnabled(true);
				loginWindow.isPorttextEnabled(true);
				loginWindow.isPointstextEnabled(true);
				loginWindow.isCredsCheckEnabled(true);

			}

		}

	}

	@Override
	public void run() {
		tryConnect(theUsersToCheck);
		
	}
	
	public void setStringOfUsers(String[] myArgs) {
		theUsersToCheck = myArgs;

	}
} //class
