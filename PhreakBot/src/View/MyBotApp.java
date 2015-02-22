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
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
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

import javax.swing.ButtonGroup;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JTabbedPane;
import javax.swing.KeyStroke;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

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
	private MyBotLogin loginWindow;
	private JButton logout;
	private JTabbedPane myTabs;
	private JPanel loginConsolePanel;
	private JPanel currentViewerPanel;
	private JPanel eventPanel;
	private JPanel usersPanel;
	private JPanel versionPanel;
	private JLabel name;
	private MyBotMain thebotMain;
	private MyBot theBot;
	private boolean connectFlag;
	private ArrayList<String> thebetaUsers;
	private String [] theUsersToCheck;
	private boolean duringClosedbeta = false;
	private final JCheckBoxMenuItem debugSettingItem;
	private Frame tempFrame = new JFrame();
	private Thread threadBtn;
	private JRadioButtonMenuItem rbMenuItem5; //int value 5
	private JRadioButtonMenuItem rbMenuItem10; //int value 10
	private JRadioButtonMenuItem rbMenuItem30; //int value 30
	private JRadioButtonMenuItem rbMenuItem0; //int value -1
	
	private JRadioButtonMenuItem rbmenuBackupYes; //option to backup
	private JRadioButtonMenuItem rbMenuBackupNo; //option to not backup
	private boolean waspreviouslyConnected = false;
	
	private JMenuItem importMenuItem;
	private JMenuItem exportMenuItem;


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
		
		//Create and setup Menu Bar
		JMenuBar menuBar = new JMenuBar();;
		JMenu file = new JMenu("File");
		JMenu settings = new JMenu("Settings");
		JMenu advertTime = new JMenu("Advertise accumulation");
		JMenu backupChoice = new JMenu("Periodic User Backups");
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
		rbMenuItem5 = new JRadioButtonMenuItem("5 mins. (Default)"); //int value 5
		rbMenuItem10 = new JRadioButtonMenuItem("10 mins"); //int value 10
		rbMenuItem30 = new JRadioButtonMenuItem("30 mins"); //int value 30
		rbMenuItem0 = new JRadioButtonMenuItem("Never"); //int value -1
		
		//add the submenu Adverttime
		ButtonGroup advertGroup = new ButtonGroup();
		advertGroup.add(rbMenuItem0);
		advertGroup.add(rbMenuItem5);
		advertGroup.add(rbMenuItem10);
		advertGroup.add(rbMenuItem30);
		advertTime.add(rbMenuItem0);
		advertTime.add(rbMenuItem5);
		advertTime.add(rbMenuItem10);
		advertTime.add(rbMenuItem30);
		rbMenuItem5.setSelected(true);
		
		//add submenu for Backups
		//This setting will basically create a copy of the USER_MAP file with a time stamp
		rbmenuBackupYes = new JRadioButtonMenuItem("Backup On ShutDown");
		rbMenuBackupNo= new JRadioButtonMenuItem("Don't Backup On ShutDown (Default)");
		ButtonGroup backupGroup = new ButtonGroup();
		backupGroup.add(rbmenuBackupYes);
		backupGroup.add(rbMenuBackupNo);
		rbMenuBackupNo.setSelected(true);
		backupChoice.add(rbmenuBackupYes);
		backupChoice.add(rbMenuBackupNo);

		
		JMenuItem exiMenuItem = new JMenuItem("Exit",
                KeyEvent.VK_E);
		JMenuItem DocumentMenuItem = new JMenuItem("Documentation",
                KeyEvent.VK_D);
		importMenuItem = new JMenuItem("Import Points...",
                KeyEvent.VK_I);
		importMenuItem.setEnabled(false);
		importMenuItem.setToolTipText("You must be connected for the Import option to enable...");
		exportMenuItem = new JMenuItem("Export Points...",
                KeyEvent.VK_X);
		exportMenuItem.setEnabled(false);
		exportMenuItem.setToolTipText("You must be connected for the Export option to enable...");
		JMenuItem AboutMenuItem = new JMenuItem("About",
                KeyEvent.VK_A);
		JMenuItem DevMenuItem = new JMenuItem("Help Develop",
                KeyEvent.VK_H);
		JMenuItem DonateMenuItem = new JMenuItem("Donate",
                KeyEvent.VK_D);
		JMenuItem ProjectMenuItem = new JMenuItem("Project Home",
                KeyEvent.VK_P);
		JMenuItem ChangesMenuItem = new JMenuItem("Change Log",
                KeyEvent.VK_C);
		JMenuItem SourceMenuItem = new JMenuItem("Source Code",
                KeyEvent.VK_S);
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
		
		
		//Setup Menu Item Listeners
		importMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
            	Object[] options = {"Yes, I understand the risk...",
                "Cancel..."};
            	
            	int result = JOptionPane.showOptionDialog(tempFrame,
            			"WARNING! Be very careful with this option. If used incorrectly \n" +
        					    "this can easily overwrite or erase your entire USER_MAP_FILE which stores\n"
        					    + "your points for everyone! Please be sure to manually create a backup\n" +
        					    "of your USER_MAP_FILE by navigating to where your Phreak_Bot_.rar \n" +
        					    "is opened from. Then just copy the USER_MAP_FILE and paste it in a safe place.\n \n"
        					    + "Do you understand the risk, have created a backup, and want to import points from \n" +
        					    "a plane .txt document (space delimented with       username      points            ? \n",
            		    tempFrame.getTitle(), JOptionPane.YES_NO_OPTION,
            		    JOptionPane.QUESTION_MESSAGE,
            		    null,    
            		    options,  
            		    options[0]); 
            	
                if(result == 0) {
                	final JFileChooser fc = new JFileChooser();
                	
                	int returnVal = fc.showOpenDialog(tempFrame);

                    if (returnVal == JFileChooser.APPROVE_OPTION) {
                        File file = fc.getSelectedFile();
                        
                        if(file != null) {
                        	boolean isSuccess = theBot.importUserMap(file);
                        	if(isSuccess) {
                        		JOptionPane.showMessageDialog(tempFrame,
                        			    "The import of the user points was successfull.");
                        	} else {
                        		JOptionPane.showMessageDialog(tempFrame,
                        			    "The import of the user points was unsuccessfull.",
                        			    "Error",
                        			    JOptionPane.ERROR_MESSAGE);
                        	}
                        }
                        System.out.println("Opening: " + file.getName());
                    } else {
                    	System.out.println("Open command cancelled by user.");
                    }
                }
              }
          });
		
		exportMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
            	boolean isSuccess = theBot.exportUserMap();
            	if(isSuccess) {
            		JOptionPane.showMessageDialog(tempFrame,
            			    "The export of the user points was successfull. The file name is export_user_points_timeStamp.txt");
            	} else {
            		JOptionPane.showMessageDialog(tempFrame,
            			    "The export of the user points was unsuccessfull.",
            			    "Error",
            			    JOptionPane.ERROR_MESSAGE);
            	}
              }

          });
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
		ProjectMenuItem.addActionListener(new ActionListener() {
		      public void actionPerformed(final ActionEvent the_event) {
		    	  try {
					openWebpage(new URL("https://code.google.com/p/twitch-bot-krashnburnz/"));
				} catch (MalformedURLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		        }
		      });
		
		SourceMenuItem.addActionListener(new ActionListener() {
		      public void actionPerformed(final ActionEvent the_event) {
		    	  try {
					openWebpage(new URL("https://code.google.com/p/twitch-bot-krashnburnz/source/checkout"));
				} catch (MalformedURLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		        }
		      });
		
		ChangesMenuItem.addActionListener(new ActionListener() {
		      public void actionPerformed(final ActionEvent the_event) {
		    	  try {
					openWebpage(new URL("https://code.google.com/p/twitch-bot-krashnburnz/source/list"));
				} catch (MalformedURLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		        }
		      });

		
		//add items to main and sub menu bars
		file.add(exportMenuItem);
		file.add(importMenuItem);
		file.add(exiMenuItem);
		settings.add(debugSettingItem);
		settings.add(advertTime);
		settings.add(backupChoice);
		helpMenu.add(DocumentMenuItem);
		helpMenu.add(AboutMenuItem);
		supportMenu.add(DevMenuItem);
		supportMenu.add(DonateMenuItem);
		supportMenu.add(ProjectMenuItem);
		supportMenu.add(SourceMenuItem);
		supportMenu.add(ChangesMenuItem);
		menuBar.add(file);
		menuBar.add(settings);
		menuBar.add(helpMenu);
		menuBar.add(supportMenu);
		setJMenuBar(menuBar);
		
		
		//create login window and various other panels
		myTabs = new JTabbedPane(JTabbedPane.TOP);
		connectFlag = false;
		name = new JLabel("");
		
		//Create Login window and thread for that window
		loginConsolePanel = new JPanel();
		loginWindow = new MyBotLogin(this);
		Thread loginThread = new Thread(loginWindow);
		loginThread.start();
		
		//Create panels for tabs
		currentViewerPanel = new JPanel();
		eventPanel = new JPanel();
		usersPanel = new JPanel();
		versionPanel  = new Version(theBot);
		
		//setup logout btn
		logout = logoutBtn(this);
		logout.setEnabled(false);
		
		//Create container for placement
		Container loginScreen = wrapComponentCenter(loginWindow);
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
		loginConsolePanel.add(loginScreen);
		
		//Setup the attributes for the console panel
		loginConsolePanel.setPreferredSize(new Dimension(500, 600));
		loginConsolePanel.setVisible(true);
		
		setTitle("Stream Phreak Bot");
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		saveOnClosingWindow();
		setPreferredSize(new Dimension(500, 650));
		setLayout(new BorderLayout());
		add(logoutbtn, BorderLayout.NORTH);
		
		//test labels
		JLabel test1 = new JLabel();
		test1.setText("To Be Implemented.");
		//JPanel paneltest = new JPanel();
		eventPanel.add(test1);
		//paneltest.add(test1);
		//paneltest.setVisible(true);

		//lets add the panels to the tabbed pane window
		myTabs.add("Login/Console", loginConsolePanel);
		myTabs.add("Current Viewers", currentViewerPanel);
		myTabs.add("Events", eventPanel);
		myTabs.add("Users", usersPanel);
		myTabs.add("Version", versionPanel);
		
		//disable tabs before user is connected: currentViewer (no user here anyway), events (not connected to start...),
		//MyBot is not initialized until connected, therefore usersPanel cannot be displayed as well
		myTabs.setEnabledAt(1, false);
		myTabs.setEnabledAt(2, false);
		myTabs.setEnabledAt(3, false);
		
		add(myTabs, BorderLayout.CENTER);
		//versionPanel.add(paneltest);

		//create image icon for the frame
	    ImageIcon imgicon = new ImageIcon("cloud.png");
        this.setIconImage(imgicon.getImage());
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
	
	/**
	 * Method that will download and populate a list containing authorized
	 * channels that can use the bot.
	 * 
	 * If the bot is in closed beta (Pre July 7th 2014), then it will download
	 * the authorized channels that are participating in the closed beta.
	 * 
	 * If the bot is in open beta, or any phase after that (Post July 7th 2014), 
	 * any channel can be connected to without a file
	 * or a check being made. 
	 */
	private void getTheFile(String myArgs) throws IOException, ClassNotFoundException {
		if(duringClosedbeta) {
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
		} else {
		    thebetaUsers = new ArrayList<String>();
		    thebetaUsers.add(myArgs);
		}
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
	        	 
	        	 /*
	        	  *   myArgs[0] = botName;
		    		  myArgs[1] = channelName;
		    		  myArgs[2] = botPassword;
		    		  myArgs[3] = twitchIp;
		    		  myArgs[4] = twitchPort;
		    		  myArgs[5] = pointName;
		    		  myArgs[6] = ""+mainBotApp.checkAdvertTimer();
		    		  myArgs[7] = thePointAccumDelay;
		    		  myArgs[8] = thePointAccumAmount;
	        	  */
	        	 //Set the Connection window to the saved settings
					loginWindow.setChannelName(user_settings.get(0));
					loginWindow.setBotName(user_settings.get(1));
					loginWindow.setBotPassword(user_settings.get(2));
					loginWindow.setTwitchIp(user_settings.get(3));
					loginWindow.setTwitchPort(user_settings.get(4));
					loginWindow.setPointName(user_settings.get(5));
					if(user_settings.size() >= 7) {
						System.out.println("User Settings size: " + user_settings.size());
						System.out.println("User file does have the save setting for the Advert Time, so load it : " + user_settings.get(6));
						int savedAdvert = Integer.parseInt(user_settings.get(6));
						if(savedAdvert == -1) {
							rbMenuItem0.setSelected(true);
						} else if (savedAdvert == 10) {
							rbMenuItem10.setSelected(true);
						} else if (savedAdvert == 30) {
							rbMenuItem30.setSelected(true);
						} else {
							rbMenuItem5.setSelected(true);
						}
					}
					if(user_settings.size() >= 8) {
						System.out.println("User file does have the - Backup Selected setting.");
						int savedBackup = Integer.parseInt(user_settings.get(7));
						if(savedBackup == 1) {
							rbmenuBackupYes.setSelected(true);
						} else {
							rbMenuBackupNo.setSelected(true);
						}
					}
					if(user_settings.size() >= 9) {
						System.out.println("User file does have the accumdelay and the accumAmount fields");
						loginWindow.setAccumDelay(user_settings.get(8));
						loginWindow.setAccumAmount(user_settings.get(9));
					}

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
		    		  waspreviouslyConnected = true;
		    		  if(theBot.isConnected()) {
			    		  theBot.disconnect();
		    		  }
		    		  logout.setEnabled(false);
						loginConsolePanel.remove(consolePanel);
						loginConsolePanel.setVisible(false);
						tempFrame.setPreferredSize(new Dimension(400, 400));
						tempFrame.add(consolePanel);
						tempFrame.pack();
						tempFrame.setVisible(true);
		    		  loginWindow.setConnectBtnName("Connect");
		    			debugSettingItem.setEnabled(true);
						loginWindow.isConnectBtnEnabled(true);
						loginWindow.setVisible(true);
						loginConsolePanel.setVisible(true);
						theBot.wantDisconnect(true);
						theBot.dispose();
						loginWindow.isBotNametextEnabled(true);
						loginWindow.isOathtextEnabled(true);
						loginWindow.isChanneltextEnabled(true);
						loginWindow.isIPtextEnabled(true);
						loginWindow.isPorttextEnabled(true);
						loginWindow.isPointstextEnabled(true);
						loginWindow.isCredsCheckEnabled(true);
						rbMenuItem0.setEnabled(true);
						rbMenuItem5.setEnabled(true);
						rbMenuItem10.setEnabled(true);
						rbMenuItem30.setEnabled(true);
						//consolePanel.setVisible(false);
						
						myTabs.setEnabledAt(1, false);
						myTabs.setEnabledAt(2, false);
						myTabs.setEnabledAt(3, false);
						myTabs.setSelectedIndex(0);

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
	 * @author Daniel Henderson
	 */
	private Container wrapComponentCenter(JPanel the_panel) {	
		Container center = new JPanel(new BorderLayout());
		center.add(the_panel, BorderLayout.CENTER);
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
			boolean saveBackupSelected = rbmenuBackupYes.isSelected();
			int savedBackupInt = 1;
			if(saveBackupSelected) {
				System.out.println("We might need to backup User Map Files since the option is selected! Let's check....");
				saveBackupUserFile();
			} else {
				System.out.println("We dont need to backup.");
				savedBackupInt = 0;
			}
			
			if (currentViewerPanel instanceof CurrentViewers) {
				((CurrentViewers) currentViewerPanel).stopTimer();
			} else if (usersPanel instanceof Users) {
				((Users) usersPanel).stopTimer();
			}
			
			MyBotApp.this.dispose();
			//SAVE BACKUP IF OPTIONS SELECTED
			if(loginWindow.getSaveCreds()) { //if save credentials is clicked, save data to file
				ArrayList<String> user_settings = new ArrayList<String>();
				//this will not check for empty boxes, concatenate with an empty string the contents
				user_settings.add("" + loginWindow.getChannelName());
				user_settings.add("" + loginWindow.getBotName());
				user_settings.add("" + loginWindow.getBotPassword());
				user_settings.add("" + loginWindow.getTwitchIp());
				user_settings.add("" + loginWindow.getTwitchPort());
				user_settings.add("" + loginWindow.getPointName());
				user_settings.add("" + checkAdvertTimer());
				user_settings.add("" + savedBackupInt);
				user_settings.add("" + loginWindow.getAccumDelay());
				user_settings.add("" + loginWindow.getAccumAmount());
				
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
		rbMenuItem0.setEnabled(false);
		rbMenuItem5.setEnabled(false);
		rbMenuItem10.setEnabled(false);
		rbMenuItem30.setEnabled(false);
		boolean gooduser = false;
        try {
			getTheFile(myArgs[1]);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Iterator<String> it = thebetaUsers.iterator();
		while(it.hasNext() && !gooduser) {
			String user = (String) it.next();
			//System.out.println(user);
			if(myArgs[1].equalsIgnoreCase(user) || !duringClosedbeta) {
				thebotMain = new MyBotMain(myArgs);
				theBot = thebotMain.getCreatedBot();
				theBot.setAdvertTimer(checkAdvertTimer());
				
				//New location for ScrollPane Panel
				currentViewerPanel = new CurrentViewers(theBot, this);
				usersPanel = new Users(theBot, this);
				myTabs.setComponentAt(1, currentViewerPanel);
				myTabs.setComponentAt(3, usersPanel);
				
				importMenuItem.setEnabled(true);
				exportMenuItem.setEnabled(true);
				connectFlag = true;
				gooduser = true;
				if(theBot.isConnected()) {
					tempFrame.remove(consolePanel);
					tempFrame.setVisible(false);
					loginConsolePanel.add(consolePanel);
					debugSettingItem.setEnabled(false);
					loginWindow.isConnectBtnEnabled(false);
					loginWindow.setVisible(false);
					consolePanel.setVisible(true);
					
					myTabs.setEnabledAt(1, true);
					myTabs.setEnabledAt(2, true);
					myTabs.setEnabledAt(3, true);
					
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

	@Override
	public void run() {
		tryConnect(theUsersToCheck);
		
	}
	
	public void setStringOfUsers(String[] myArgs) {
		theUsersToCheck = myArgs;

	}
	
	private void saveBackupUserFile() {
		if(theBot != null) {
			theBot.saveBackupUserFile();
		} else {
			System.out.println("Will not backup since the bot was not connected.");
		}

	}
	
	public int checkAdvertTimer() {
		int advertTimer;
		if(rbMenuItem0.isSelected()) {
			advertTimer = -1;
		} else if (rbMenuItem10.isSelected()) {
			advertTimer = 10;
		} else if (rbMenuItem30.isSelected()) {
			advertTimer = 30;
		} else {
			advertTimer = 5;
		}
		return advertTimer;
	}
	
	/**
	 * Callback Method used to keep the Panels in sync as far as User points are concerned.
	 * When panels add/subtract points, this methoid is called in the GUI as a callback, 
	 * which will then call the Update View method for that specific Panel.
	 * 
	 * Panels: if i = 1 its Users Panel calling, if i = 2 its CurrentViewer panel calling
	 * @param userName  of the user having their points edited
	 * @param points  Points that were added or subtracted
	 * @param i    the Panel Type
	 */
	public void updateView(int i, String name, int p) {
		int tabType = i;
		if(tabType == 2) { //Users Tab is calling update
			((CurrentViewers) currentViewerPanel).updateMyView(name, p);

		} else if(tabType == 1) {//CurrentViewer tab is calling update
			((Users) usersPanel).updateMyView(name, p);
		}
		
	}
} //class
