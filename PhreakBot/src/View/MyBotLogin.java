/*
 * Login class for the login and registration window of the program
 * Author: Daniel Henderson
 * TCSS 360 Spring 2013 
 * University of Washington
 */

package View;

import java.awt.Color;
import java.awt.Container;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import Model.MyBotMain;
import Model.User;

public class MyBotLogin extends JPanel implements Runnable{
	/**
	 *@author Daniel Henderson
	 * Login class, used to setup the login window and registration to
	 * the system, including all the login functionality of the program.
	 */
	private static final long serialVersionUID = 6316166873535398641L;

	//private JPanel login;
	private JButton btnConnect;	
	private JButton btnDonate;	
	private JButton btnRegisterNow;	
	private JButton btnOathKey;

		
	//Bot name
	private JLabel labelBotname;
	private JTextField txtBotname;
	
	//Streamer name
	private JLabel labelMyChannelName;
	private JTextField txtChannelName;
	
	//Twitch IP
	private JLabel labelTwitchIP;
	private JTextField txtTwitchIP;
	
	//Twitch port
	private JLabel labelTwitchPort;
	private JTextField txtTwitchPort;
	
	//Both Login password
	private JLabel labelPassword;
	private JPasswordField txtPassword;	
	
	//Point name
	private JLabel labelPointsName;
	private JTextField txtPointName;
	
	//Save Credentials
	private JLabel labelSaveCreds;
	private JCheckBox cboxCredsSave;
	private MyBotApp mainBotApp;
	
	
    private Dimension windowSize;

	
	/**
	 * Constructor for the login class
	 * @param myBotApp 
	 * @param Controller the main controller class for the program
	 */
	public MyBotLogin(MyBotApp myBotApp) {
		this.setLayout(new FlowLayout());
		mainBotApp = myBotApp;
	} //Login
	
	/**
	 * Primary method that will setup the GUI for the Login window
	 */
	private void setup() {
		this.setLayout(new FlowLayout());
		windowSize = new Dimension(400, 250);
		Dimension loginSize = new Dimension(200, 400);
		this.setPreferredSize(loginSize);
		Dimension txtsize = new Dimension(200, 25);
		//Bot Name
		labelBotname = new JLabel("Bot Name: ");
		txtBotname = new JTextField();
		txtBotname.setPreferredSize(txtsize);
		this.add(labelBotname);
		this.add(txtBotname);
		
		//Both Login password
		labelPassword= new JLabel("Twitch Oath Key: ");;
		txtPassword= new JPasswordField();	
		txtPassword.setPreferredSize(txtsize);
		this.add(labelPassword);
		this.add(txtPassword);
		
		//Streamer name
		labelMyChannelName = new JLabel("Channel Name: ");
		txtChannelName = new JTextField();
		txtChannelName.setPreferredSize(txtsize);
		this.add(labelMyChannelName);
		this.add(txtChannelName);
		
		//Twitch IP
		labelTwitchIP= new JLabel("Twitch IP: ");
		txtTwitchIP = new JTextField("199.9.250.229");
		txtTwitchIP.setPreferredSize(txtsize);
		this.add(labelTwitchIP);
		this.add(txtTwitchIP);
		
		//Twitch port
		labelTwitchPort= new JLabel("Twitch Port: ");
		txtTwitchPort = new JTextField("6667");
		txtTwitchPort.setPreferredSize(txtsize);
		this.add(labelTwitchPort);
		this.add(txtTwitchPort);

		
		//Point name
		labelPointsName= new JLabel("Points Name: ");;
		txtPointName = new JTextField();
		txtPointName.setPreferredSize(txtsize);
		this.add(labelPointsName);
		this.add(txtPointName);
		
		//Save Credentials
		labelSaveCreds= new JLabel("Save Credentials?");;
		cboxCredsSave = new JCheckBox();
		cboxCredsSave.setSelected(true);
		this.add(labelSaveCreds);
		this.add(cboxCredsSave);

		
		//call method to create button and listeners
		setupBtns();

		this.add(btnDonate);
		this.add(btnOathKey);
		this.add(btnConnect);
	}
	
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
	 * Helper method that sets up the various buttons and Listeners
	 * via inner classes for each button.
	 */
	private void setupBtns() {
		btnOathKey = new JButton("OathKey");
		btnConnect = new JButton("Connect");
		btnDonate = new JButton("Donate");
	  	btnRegisterNow = new JButton("Register");


	  	//Login button listener setup.
		btnConnect.addActionListener(new ActionListener() {
		      public void actionPerformed(final ActionEvent the_event) {
		    	  String botName = getBotName();
		    	  String channelName = getChannelName();
		    	  String botPassword = getBotPassword();
		    	  String twitchIp = getTwitchIp();
		    	  String twitchPort = getTwitchPort();
		    	  String pointName = getPointName();
		    	  if(botName.isEmpty()) {
		    		  JOptionPane.showMessageDialog(null, "Required information missing! Please type a bot name."); 
		    	  } else if(channelName.isEmpty()) {
		    		  JOptionPane.showMessageDialog(null, "Required information missing! Please type a channel name."); 
		    	  } else if(botPassword.isEmpty()) {
		    		  JOptionPane.showMessageDialog(null, "Required information missing! Please input your Bot's Twitch Oathkey."); 
		    	  } else if(twitchIp.isEmpty()) {
		    		  JOptionPane.showMessageDialog(null, "Required information missing! Please type an IP adress for Twitch."); 
		    	  } else if(twitchPort.isEmpty()) {
		    		  JOptionPane.showMessageDialog(null, "Required information missing! Please type a Port for Twitch."); 
		    	  } else if(pointName.isEmpty()) {
		    		  JOptionPane.showMessageDialog(null, "Required information missing! Please type the name you want your channel points to be called."); 
		    		  
		    	  }  else if(pointName.equalsIgnoreCase("points") || pointName.equalsIgnoreCase("point")) {
		    		  JOptionPane.showMessageDialog(null, "Please call the points something other then 'point' or 'points'. \n" +
		    		  									"This could cause confusion when messages about 'points' are displayed!"); 
		    		  
		    	  }else {
		    		  String [] myArgs = new String [7];
		    		  myArgs[0] = botName;
		    		  myArgs[1] = channelName;
		    		  myArgs[2] = botPassword;
		    		  myArgs[3] = twitchIp;
		    		  myArgs[4] = twitchPort;
		    		  myArgs[5] = pointName;
		    		  myArgs[6] = ""+mainBotApp.checkAdvertTimer();
		    		  setConnectBtnName("Connecting...");
		    		  isConnectBtnEnabled(false);
		    		  mainBotApp.setStringOfUsers(myArgs);
		    		  Thread connectThread = new Thread(mainBotApp);
		    		  connectThread.start();
		    	  }
		      }
		});
		
		//About button listener setup.
		btnOathKey.addActionListener(new ActionListener() {
		      public void actionPerformed(final ActionEvent the_event) {
		    	  if (the_event.getSource() == btnOathKey) {
		    		  String botName = getBotName();
		    		  if(botName.isEmpty()) {
		    			  JOptionPane.showMessageDialog(null, "You can get your oath key from http://www.twitchapps.com/tmi/ \n" +
		    					  								"\n" + 
		    					  								"Please be sure you log into twitch with your BOT INFORMATION to get your bot's oathkey!");
		    		  } else {
		    			  JOptionPane.showMessageDialog(null, "You can get your oath key from http://www.twitchapps.com/tmi/ \n" +
		    					  								"\n" + 
		    					  								"Please be sure you log into twitch with your BOT INFORMATION to get your bot's oathkey!");
		    		  }
		    		}
		    	}
		      });


		
		//Register button listener setup (Register button on main login window)
		btnDonate.addActionListener(new ActionListener() {
		      public void actionPerformed(final ActionEvent the_event) {
		    	  try {
					openWebpage(new URL("https://www.paypal.com/cgi-bin/webscr?cmd=_s-xclick&hosted_button_id=AZ7PBS9ZSQLQA"));
				} catch (MalformedURLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		        }
		      });
		
		  //Register button listener setup (Register button inside the registration window)
	  	  btnRegisterNow.addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent the_event) {
				
		        }
		      });
	}

	/**
	 * Helper method that clears the login textfields.
	 */
	public void clearLoginBox() {
		txtBotname.setText("");
		txtPassword.setText("");
	}
	
	/**
	 * Helper method that will format the registration window using the
	 * GridBag layout and constraints.
	 * @param JLabel[] contains the various labels for the reg window
	 * @param JTextField[] contains the various text fields for the reg window
	 * @param JLabel[] contains the various error labels
	 * @param GridBagLayout the layout used for the Reg window
	 * @param Container this is the panel for the Reg window
	 */
	private void addLabelTextRows(JLabel[] labels,
			JTextField[] textFields, JLabel[] errorLabels,
			GridBagLayout gridbag,
			Container container) {
		
		GridBagConstraints c = new GridBagConstraints();
		c.anchor = GridBagConstraints.EAST;
		int numLabels = labels.length;

		for (int i = 0; i < numLabels; i++) {
			c.gridwidth = GridBagConstraints.RELATIVE; //next-to-last
			c.fill = GridBagConstraints.NONE;      //reset to default
			c.weightx = 0.0;                       //reset to default
			container.add(labels[i], c);

			c.gridwidth = GridBagConstraints.REMAINDER;     //end row
			c.fill = GridBagConstraints.HORIZONTAL;
			c.weightx = 1.0;
			container.add(textFields[i], c);
			container.add(errorLabels[i], c);
		}
		JPanel spaceBuff1 = new JPanel();
		c.gridwidth = GridBagConstraints.REMAINDER;     //end row
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = 1.0;
		container.add(spaceBuff1, c);
		
		c.gridwidth = GridBagConstraints.RELATIVE; //next-to-last
		c.fill = GridBagConstraints.NONE;      //reset to default
		c.weightx = 0.0;                       //reset to default
		JPanel spaceBuff2 = new JPanel();


		container.add(spaceBuff2, c);
		container.add(btnRegisterNow, c);
	}
	/**
	 * Helper method that will return a boolean value as to whether
	 * the save crednetials button is checked.
	 * @return Boolean True is checked, false otherwise
	*/
	public boolean getSaveCreds() {
		return cboxCredsSave.isSelected();
		
	}
	



	
	//getters
	public String getChannelName() {
		return txtChannelName.getText().toLowerCase();
	}

	public String getBotName() {
		return txtBotname.getText().toLowerCase();
	}
	
	public String getBotPassword() {
		return String.valueOf(txtPassword.getPassword());
	}
	
	public String getTwitchIp() {
		return txtTwitchIP.getText();
	}
	
	public String getTwitchPort() {
		return txtTwitchPort.getText();
	}
	
	public String getPointName() {
		return txtPointName.getText().toLowerCase();
	}
	
	public MyBotMain getBotMain() {
		return null;
		
	}
	
	// Setters
	public void setSaveCreds(boolean the_answer) {
		cboxCredsSave.setSelected(the_answer);
		
	}
	
	public void setChannelName(String the_text) {
		txtChannelName.setText(the_text);
	}

	public void setBotName(String the_text) {
		txtBotname.setText(the_text);
	}
	
	public void setBotPassword(String the_text) {
		txtPassword.setText(the_text);
	}
	
	public void setTwitchIp(String the_text) {
		txtTwitchIP.setText(the_text);
	}
	
	public void setTwitchPort(String the_text) {
		txtTwitchPort.setText(the_text);
	}
	
	public void setPointName(String the_text) {
		txtPointName.setText(the_text);
	}
	
	public synchronized void isConnectBtnEnabled(boolean answer) {
		btnConnect.setEnabled(answer);
	}
	
	public synchronized void setConnectBtnName(String the_text) {
		btnConnect.setText(the_text);
	}

	@Override
	public void run() {
		setup();
		
	}
	
	public void isBotNametextEnabled(boolean answer) {
		txtBotname.setEnabled(answer);
	}
	public void isOathtextEnabled(boolean answer) {
		txtPassword.setEnabled(answer);
	}
	public void isChanneltextEnabled(boolean answer) {
		txtChannelName.setEnabled(answer);
	}
	public void isIPtextEnabled(boolean answer) {
		txtTwitchIP.setEnabled(answer);
	}
	public void isPorttextEnabled(boolean answer) {
		txtTwitchPort.setEnabled(answer);
	}
	public void isPointstextEnabled(boolean answer) {
		txtPointName.setEnabled(answer);
	}
	public void isCredsCheckEnabled(boolean answer) {
		cboxCredsSave.setEnabled(answer);
	}

} 
