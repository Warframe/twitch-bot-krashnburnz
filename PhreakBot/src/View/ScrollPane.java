/*  
 * Last Edited: 06/07/2013 by Ching-Ting
 * 
 * [NOTE]: To set up propertyChangeListener, setFlag() method MUST be called by
 * passing in a JLabel.
 */
package View;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.Timer;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import Model.MyBot;
import Model.MyBotUserPoints;
import Model.User;

/**
 * This class populates the list of inputs into a table to be put onto the GUI.
 * 
 * @author Ching-Ting Huang
 * @version UWTCSS 360, Spring 2013. 3-squared-software-systems.
 */
public class ScrollPane {

	//time between thread checking if any update to be done to the Jtable for "Current Viewer" tab
	private static final int UPDATE_SEC = 25;
	//controller
	private MyBot theBot;
	//interface to map of all user and points
	private MyBotUserPoints allUserNPoints;
	//array of all users currently watching channel
	private User[] curU;
	//dtm used for "Current Viewer" & "Users" tab only (due to JTable header and cell format)
	private DefaultTableModel generalModel;
	//dtm used for "Version" tab only
	private DefaultTableModel versionModel;
	//JTable used to display data
	private JTable table;
	//wrapper class for JTable to be scroll-able
	private JScrollPane scrollPane;
	//PropertyChange to inform other GUI-components something is being clicked on the JTable
	private JLabel flag;
	//data in the JTable
	private Object[][] input;
	//timer for thread to check if there's any updates to the user watching the channel
	private Timer timer;
	//row in JTable being clicked currently
	private int row;

	/**
	 * Depending on the type of input, different list is being put into a 2D-array in order
	 * to populate a JTable that will later be put into JScrollPane to be displayed.
	 * 
	 * @param control is the Model Controller.
	 * @param para is the type of inputs being passed in in String representation: user (user tab), viewer (current viewer tab),
	 * 		  version (version tab).
	 * @author Ching-Ting Huang
	 */
	public ScrollPane(final MyBot bot, final String para) {
		theBot = bot;
		allUserNPoints = bot.getAllUnP();
		curU = bot.getCurUsers();
		
		if (para.equals("viewer")) {
			setTime(para);
		}
		
		row = -1;
		Object[][] data = getList(para);
		String[] header = getHeader(para);
		input = data;

		//Note: this DefaultTableModel is used only for User/Current Viewer tab, NOT for Version tab!
		generalModel = new DefaultTableModel(data, header) {
			private static final long serialVersionUID = -1411446165832277578L;
			@Override
			public boolean isCellEditable(int row, int column) {
				//make all cells editable = false
				return false;
			}
			
			@Override
			public Class<?> getColumnClass(int colNum) {
				switch (colNum) {
				case 0:
					return Integer.class;
				case 1:
					return String.class;
				case 2:
					return Integer.class;
				case 3: 
					return String.class;
				case 4: 
					return String.class;
				default:
					return String.class;
				}
			}
		};
		
		//Note: This DefaultTableModel is for Version tab ONLY!
		versionModel = new DefaultTableModel(data, header) {
			private static final long serialVersionUID = -1411446165832277578L;
			@Override
			public boolean isCellEditable(int row, int column) {
				//make all cells editable = false
				return false;
			}
			
			@Override
			public Class<?> getColumnClass(int colNum) {
				switch (colNum) {
				case 0:
					return Integer.class;
				case 1:
					return Date.class;
				case 2:
					return Integer.class;
				case 3: 
					return String.class;
				default:
					return String.class;
				}
			}
		};

		table = new JTable(generalModel);
		tableSetUp(table, para);
		cellMod(table);
		scrollPane = new JScrollPane(table);
		table.setFillsViewportHeight(true);
	} //Table
	
	/**
	 * Initialize timer and set up thread to check for updates in data based on timer.
	 * 
	 * @param para 
	 */
	private void setTime(final String para) {
		int delay = UPDATE_SEC * 1000;
		ActionListener checkUpdate = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				final User[] now = theBot.getCurUsers();
				CheckCurrentWatcher update = new CheckCurrentWatcher(curU, now, generalModel, allUserNPoints);
				update.execute();
				curU = Arrays.copyOf(now, now.length);
				System.out.println(UPDATE_SEC + "s has passed!");
			}
		};
		timer = new Timer(delay, checkUpdate);
		timer.start();
		System.out.println("SwingWorkerThread Timer START!");

	} //setTime
	
	/**
	 * Using DefaultTableCellRenderer to accomplish:
	 * 
	 * 1) Color-code "Yes"/"No" for subscriber/moderator column.
	 * 2) Due to override for getColumnClass() from DefaultTableModel, integer align right & string align left.
	 * 	  This will force all data to center in the cell regardless of class.
	 * 
	 * @param table is the table.
	 */
	private void cellMod(JTable table) {
		for (int i = 0; i < table.getColumnCount(); i++) {
			DefaultTableCellRenderer center = new DefaultTableCellRenderer() {

				private static final long serialVersionUID = -3894436645096493165L;

				@Override
				public Component getTableCellRendererComponent(JTable table,
						Object value, boolean isSelected, boolean hasFocus, int row, int column) {
					
					Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
					if (value.toString().equals("Yes")) {
						c.setForeground(Color.GREEN);
					} else if (value.toString().equals("No")) {
						c.setForeground(Color.RED);
					}
					return c;
				}
			};
			center.setHorizontalAlignment(JLabel.CENTER);
			table.getColumnModel().getColumn(i).setCellRenderer(center);
		}
	}

	/**
	 * Sets up functions of the JTable: enable only row selection, set cell editable false,
	 * draw horizontal & vertical lines between column and row, one selection at an instance.
	 * 
	 * @param table is the JTable.
	 * @author Ching-Ting Huang
	 */
	private void tableSetUp(final JTable table, final String type) {
		table.setCellSelectionEnabled(false);
		table.setDragEnabled(false);
		table.setRowSelectionAllowed(true);
		table.setShowHorizontalLines(false);
		table.setShowVerticalLines(true);
		table.setAutoCreateRowSorter(true);
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		
		table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				if (!e.getValueIsAdjusting()) {
					row = table.convertRowIndexToModel(table.getSelectedRow());
					if (row >= 0) {
						if (flag != null) {
							flag.setText(flag.getText() + "+");
						}
					}
				}
			}

		});
	} //tableSetup

	/**
	 * Put all user (ever recorded) info into array to be displayed in JTable include
	 * rank, name (nick-name), points collected, isSubscriber?, isMod?...etc.
	 * 
	 * @return 2D-array of the users and their meta-data.
	 * @author Ching-Ting Huang
	 */
	private Object[][] putAllUserToArray() {
		int userNum = allUserNPoints.getTotalUserCount();
		Map<User, Integer> unp = allUserNPoints.getUserMap();
		Object[][] data = new Object[userNum][5];
		
		int count = 0;
		for(Map.Entry<User, Integer> entry : unp.entrySet()) {
			data[count][0] = theBot.getAllUnP().getRank(entry.getKey().getNick());
			data[count][1] = entry.getKey().getNick();
			data[count][2] = entry.getValue();
			data[count][3] = "No";
			data[count][4] = "No";
			count++;
		}
		return data;
	} //putAuthToArray
	
	/**
	 * Put users (in channel currently) info into array to be displayed in JTable include
	 * rank, name (nick-name), points collected, isSubscriber?, isMod?...etc.
	 * 
	 * @return 2D-array of the users and their meta-data.
	 * @author Ching-Ting Huang
	 */
	private Object[][] putCurUserToArray() {
		Map<User, Integer> unp = allUserNPoints.getUserMap();
		int userNum = curU.length;
		Object[][] data = new Object[userNum][5];
		
		for(int i = 0; i < userNum; i++) {
			User user = curU[i];

			data[i][0] = allUserNPoints.getRank(user.getNick());
			data[i][1] = user.getNick();
			if (unp.containsKey(user)) {
				data[i][2] = unp.get(user);
			} else {
				data[i][2] = "N/A";
			}
			data[i][3] = "No";
			data[i][4] = "No";
		}
		return data;
	} //putAuthToArray

	/**
	 * Setup table header depending on type of input.
	 * 
	 * @param type is the list type passed in (conference, paper).
	 * @return string array as table header.
	 * @author Ching-Ting Huang
	 */
	private String[] getHeader(final String type) {
		String[] header = null;
		if (type.equals("user") || type.equals("viewer")) {
			header = new String[]{"Rank", "Name", "Points", "Subscriber", "Moderator"};
		} else if (type.equals("version")) {
			header = new String[]{"Version", "Released", "Doc. Version", "Description"};
		} 
		return header;
	} //getHeader

	/**
	 * Depending on the input type, different lists are used to populate 2D array that
	 * will be displayed on the JTable/JScrollPane.
	 * 
	 * @param type is the input type: conference(conf), program chair(pc), subprogram chair(spc),
	 * 		  reviewer(rev), author(auth).
	 * @return 2D-array representation of the input list.
	 * @author Ching-Ting Huang
	 */
	private Object[][] getList(final String type) {
		Object[][] data = null;
		if (type.equals("user")) {
			data = putAllUserToArray();
		} else if (type.equals("viewer")) {
			data = putCurUserToArray();
		/*} else if (type.equals("version")) {
			
		} */
			
		} 
		return data;
	} //getList
	
	/**
	 * Stop timer updating TableModel, used when quitting program.
	 * 
	 * @return time stopped status = true
	 */
	public boolean stopUpdateTimer() {
		if (timer != null) {
			timer.stop();
		}
		return true;
	} //stopUpdateTimer

	/**
	 * This is a JLabel made by caller with added propertyChangeListener. When this label
	 * is passed into this class as a field and its text is later changed when a row is
	 * selected in the table, the propertyChangeListener will notify the change on the 
	 * caller side.
	 * 
	 * @param label is the 'flag' to notify the caller a row has been selected in the table.
	 * @author Ching-Ting Huang
	 */
	public void setFlag(final JLabel label) {
		flag = label;
	} //setFlag

	/**
	 * The current selected row in the table.
	 * 
	 * @return the selected row index in the table.
	 * @author Ching-Ting Huang
	 */
	public int getSelectedRowNum() {
		return row;
	} //getRowNum

	/**
	 * Gets the specified row in the 2D-array
	 * 
	 * @return the row of data from the 2D-array.
	 * @author Ching-Ting Huang
	 */
	public Object[] getRow(final int row) {
		int size = input[row].length;
		Object[] result = new Object[size];
		for (int i = 0; i < size; i++) {
			if (input[row][i] != null) {
				result[i] = input[row][i];
			}
		}
		return result;
	} //getInput

	/**
	 * Gets the current scrollpane.
	 * 
	 * @return JScrollPane.
	 * @author Ching-Ting Huang
	 */
	public JScrollPane getScrollPane() {
		return scrollPane;
	} //getScrollPane
	
	/////////////////////////////////////INNER CLASS: SWINGWORKER FOR UPDATING JTABLE///////////////////////
	
	/**
	 * This is a separate "thread" to handle 1) finding the difference between previous and current arrays of 
	 * stream viewers based on timer call, 2) update JTable/TableModel according to result. Due to the potential
	 * time-consuming calculation, a separate thread is introduced in case of GUI-lock.
	 * 
	 * @author Ching-Ting Huang
	 * @version 05/20/2014
	 */
	private class CheckCurrentWatcher extends SwingWorker<UpdateLists, Integer> {
		
		//list of users before most recent timer is called
		private User[] prev;
		//list of users after most recent timer is called
		private User[] now;
		//model used currently for JTable
		private DefaultTableModel model;
		//list of all user and their points for updating purposes
		private MyBotUserPoints munp;
		
		/**
		 * Initialize fields.
		 * 
		 * @param current is the array of users before most recent timer interrupt.
		 * @param newArray is the array of users after most recent timer interrupt.
		 * @param dtm is the DefaultTableModel being used on JTable currently.
		 * @param unp is the interface to the Map of User-to-points.
		 */
		private CheckCurrentWatcher(User[] current, User[] newArray, DefaultTableModel dtm, MyBotUserPoints unp) {
			prev = current.clone();
			now = newArray.clone();
			model = dtm;
			munp = unp;
		} //constructor
		
		/**
		 * Perform 'diff' operation on past and current list of viewers and get 2 lists of users: 1) additional
		 * users join the channel or 2) users who left the channel.
		 */
		@Override
		protected UpdateLists doInBackground() throws Exception {
			//parse 2 arrays to find if any people join/left channel
			UpdateLists lists = new UpdateLists();
			Set<User> tmp_prev = new HashSet<User> (Arrays.asList(prev));
			Set<User> tmp_now = new HashSet<User> (Arrays.asList(now));
			Set<User> common = new HashSet<User>(tmp_prev);	
			
			common.retainAll(tmp_now);			//common elements between two sets
			tmp_prev.removeAll(common);			//take off common element from prev_set
			tmp_now.removeAll(common);			//take off common element from now_set
			
			if (tmp_prev.size() > 0) {
				lists.left.addAll(tmp_prev);	//list of people left channel
			}
			
			if (tmp_now.size() > 0) {
				lists.join.addAll(tmp_now);		//list of people joined channel
			}
			
			return lists;
		} //doInBackground
		
		/**
		 * Roll out update based on "joined-list" and "left-list" to TableModel so JTable can be updated.
		 */
		@Override
		protected void done() {
			//update TableModel/JTable for new information based on two lists
			try {
				UpdateLists results = get();
				List<User> join = results.join;
				List<User> left = results.left;
				Map<User, Integer> unp = munp.getUserMap();
				
				if (join.size() > 0) {
					for (int i = 0; i < join.size(); i++) {
						Object[] data = new Object[5];
						User u = join.get(i);
						data[0] = munp.getRank(u.getNick());
						data[1] = u.getNick();
						if (unp.containsKey(u)) {
							data[2] = unp.get(u);
						} else {
							data[2] = "N/A";
						}
						data[3] = "No";
						data[4] = "No";
						model.addRow(data);
					}
				}
				
				if (left.size() > 0) {
					for (int j = 0; j < left.size(); j++) {
						User u = left.get(j);
						for (int k = 0; k < model.getRowCount(); k++) {
							String name = (String) model.getValueAt(k, 1);
							if (name.equals(u.getNick())) {
								model.removeRow(k);
							}
						}
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		} //done
	} //Inner: (CheckCurrentWatcher) SwingWorker
	
	//////////////////////////////////UTILITY CLASS///////////////////////////////////////////
	
	/**
	 * This class captures two lists: a list of additional viewers watching the stream that were not watching since the
	 * most recent call from timer, and a list of viewers who left the stream.
	 * 
	 * @author Ching-Ting Huang
	 * @version 05/20/2014
	 */
	private class UpdateLists {
		private UpdateLists() {
			join = new ArrayList<User>();
			left = new ArrayList<User>();
		}
		
		List<User> join;
		List<User> left;
	} //Inner: (UpdateLists) 
} //Outer  class
