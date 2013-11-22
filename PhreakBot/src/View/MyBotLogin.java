/*
 * Login class for the login and registration window of the program
 * Author: Daniel Henderson
 * TCSS 360 Spring 2013 
 * University of Washington
 */

package View;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import Model.User;

public class MyBotLogin extends JPanel{
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
	private JButton btnAbout;
	
	private JTextField txtBotname;	
	private JPasswordField txtPassword;	
	
	private JTextField txtChannelName;
	private JTextField txtTwitchIP;
	private JTextField txtTwitchPort;

	
	
	private JLabel labelBotname;	
	private JLabel labelMyChannelName;
	private JLabel labelTwitchIP;
	private JLabel labelTwitchPort;
	private JLabel labelPassword;
	private JLabel labelPointsName;
	private JLabel labelSaveCreds;
	
	
    private Dimension windowSize;

	
	/**
	 * Constructor for the login class
	 * @param Controller the main controller class for the program
	 */
	public MyBotLogin() {
		this.setLayout(new FlowLayout());
		setup();
	} //Login
	
	/**
	 * Primary method that will setup the GUI for the Login window
	 */
	private void setup() {
		this.setLayout(new FlowLayout());
		windowSize = new Dimension(400, 250);
		Dimension loginSize = new Dimension(200, 300);
		this.setPreferredSize(loginSize);
		
		labelBotname = new JLabel("Bot Name: ");
		labelMyChannelName = new JLabel("Channel Name: ");
		
		setupBtns();
		txtBotname = new JTextField();
		txtPassword = new JPasswordField();
		
		txtBotname.setPreferredSize(new Dimension(200, 25));
		txtPassword.setPreferredSize(new Dimension(200, 25));
		
		//Add Components to 
		this.add(labelBotname);
		this.add(txtBotname);
		
		this.add(labelMyChannelName);
		this.add(txtPassword);
		this.add(btnConnect);
		this.add(btnDonate);	
		this.add(btnAbout);
	}
	
	/**
	 * Helper method that sets up the various buttons and Listeners
	 * via inner classes for each button.
	 */
	private void setupBtns() {
		btnConnect = new JButton("Connect");
		btnDonate = new JButton("Donate");
	  	btnRegisterNow = new JButton("Register");
	  	btnAbout = new JButton("About");

	  	//Login button listener setup.
		btnConnect.addActionListener(new ActionListener() {
		      public void actionPerformed(final ActionEvent the_event) {
}
		      });
		
		//About button listener setup.
		btnAbout.addActionListener(new ActionListener() {
		      public void actionPerformed(final ActionEvent the_event) {
		    	  if (the_event.getSource() == btnAbout) {

		    				  JOptionPane.showMessageDialog(null, "Created by: Daniel Henderson \n" +
		    						  							  "Contact Email: krashnburnz@yahoo.com \n"); 
		    			  }
		    		  }
		      });
		
		//Register button listener setup (Register button on main login window)
		btnDonate.addActionListener(new ActionListener() {
		      public void actionPerformed(final ActionEvent the_event) {
		    	  
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
} 
