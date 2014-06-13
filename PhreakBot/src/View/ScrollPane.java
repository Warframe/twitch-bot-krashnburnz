/*  
 * Last Edited: 05/22/2014 by Ching-Ting
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
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import Model.MyBot;
import Model.MyBotUserPoints;
import Model.User;

/**
 * This class populates the list of inputs into a table to be put onto the GUI.
 * 
 * @author Ching-Ting Huang
 * @version 06/07/2013
 */
public class ScrollPane {

	/**
	 * time between thread checking if any update to be done to the Jtable for "Current Viewer" tab.
	 */
	private static final int UPDATE_SEC = 25;
	
	
	private static final int RENEW_MIN = 5;
	
	
	/**
	 * when tick==TICK_WAIT, JTable will force a repaint to keep up with accumulated point updates.
	 */
	private static final int TICK_WAIT = 12;
	
	/**
	 * counter to force JTable for Current User tab to redraw after tick reach TICK_WAIT.
	 */
	private static int tick = 0;
	
	/**
	 * check if current viewer list in controller is empty or not.
	 */
	private static boolean curUserEmpty = true;
	
	/**
	 * check if all user list in controller is empty or not.
	 */
	private static boolean allUserEmpty = true;
	
	/**
	 * controller.
	 */
	private MyBot theBot;
	
	/**
	 * interface to map of all user and points.
	 */
	private MyBotUserPoints allUserNPoints;
	
	private List<String> mods;
	
	/**
	 * array of all users currently watching channel.
	 */
	private User[] curU;
	
	/**
	 * dtm used for "Current Viewer" & "Users" tab only (due to JTable header and cell format).
	 */
	private DefaultTableModel generalModel;
	
	/**
	 * dtm used for "Version" tab only.
	 */
	private DefaultTableModel versionModel;
	
	/**
	 * dtm used for "Current Viewer" when 1) program start up w/ no viewer, 
	 * 2) no viewer present. Will be replaced if viewer exists.
	 */
	private DefaultTableModel emptyModel;
	
	/**
	 * JTable used to display data
	 */
	private JTable table;
	
	/**
	 * wrapper class for JTable to be scroll-able.
	 */
	private JScrollPane scrollPane;
	
	/**
	 * PropertyChange to inform other GUI-components something is being clicked on the JTable.
	 */
	private JLabel flag;
	
	/**
	 * flag for checking if current viewer tab's JTable has been updated in order to update other misc. info 
	 * given the updated JTable.
	 */
	private JLabel viewerUpdate;
	
	/**
	 * timer for thread to check if there's any updates to the user watching the channel.
	 */
	private Timer timer;
	
	/**
	 * row in JTable being clicked currently.
	 */
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
		init();
		if (para.equals("viewer")) {
			setViewerTime();
		} else if (para.equals("user")) {
			setUserTime();
		}
		row = -1;
		Object[][] data = getList(para);
		String[] header = getHeader(para);		
		modelSetup(data, header, para);
	} //constructor
	
	private void modelSetup(Object[][] data, String[] header, String para) {
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
		versionModel = new DefaultTableModel() {
			private static final long serialVersionUID = -1411446165832277578L;
			@Override
			public boolean isCellEditable(int row, int column) {
				//make all cells editable = false
				return false;
			}
			/*
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
			}*/
		};
		versionModel.setColumnIdentifiers(header);

		emptyModel = new DefaultTableModel() {	
			private static final long serialVersionUID = 7235000206295729521L;
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
		emptyModel.setColumnIdentifiers(header);

		switch (para) {
			case "user": 	table = allUserEmpty ? new JTable(emptyModel) : new JTable(generalModel);
							break;
			case "viewer": 	table = curUserEmpty ? new JTable(emptyModel) : new JTable(generalModel);
							break;
			case "version": table = new JTable(versionModel);
							break;
		}
		
		((DefaultTableModel)table.getModel()).addTableModelListener(new TableModelListener() {
			@Override
			public void tableChanged(TableModelEvent e) {
				if (viewerUpdate != null) {
					viewerUpdate.setText(viewerUpdate.getText().toString() + "+");
				}
			}
		});

		
		if (para.equals("viewer") || para.equals("user")) {
			cellMod();
		}
		tableSetUp(para);
		
		scrollPane = new JScrollPane(table);
		table.setFillsViewportHeight(true);
	} //modelSetup
	
	/**
	 * Sets boolean flag for whether data is available to be put into JTable or not. If they are available, 
	 * fields are initialized. If not, boolean flags and place holders are set.
	 */
	private void init() {
		mods = theBot.getMods();
		if (theBot.getAllUnP().getUserMap().size() > 0) {
			allUserEmpty = false;
		}
		allUserNPoints = theBot.getAllUnP();
		
		if (theBot.getCurUsers().length > 0) {
			curUserEmpty = false;
			curU = theBot.getCurUsers();
		} else {
			curU = new User[0];
		}
	} //checkData
	
	/**
	 * Initialize timer and set up thread to check for updates in data based on timer.
	 */
	private void setViewerTime() {
		int iniDelay = 15 * 1000; 			//15s delay initially
		int delay = UPDATE_SEC * 1000;		//25s delay after timer initialize
		ActionListener checkUpdate = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				tick++;
				final User[] now = theBot.getCurUsers();
				
				if (tick < TICK_WAIT) {
					CheckCurrentWatcher update = new CheckCurrentWatcher(curU, now);
					if (!curUserEmpty || now.length > 0) {
						update.execute();
						curU = Arrays.copyOf(now, now.length);
						table.validate();
					} else {
						update.cancel(true);
					}
				} else {
					System.out.print("Updating CV...");
					boolean update = updateTable("viewer");
					if (update) {
						System.out.print("updated.\n");
					} else {
						System.out.print("update failed.\n");
					}
					tick = 0;
				}
			}
		};
		timer = new Timer(iniDelay, checkUpdate);
		timer.start();
		timer.setDelay(delay);
		System.out.println("CV-timer: start.");
	} //setTime
	
	/**
	 * Initialize timer to update entire User tab JTable by time.
	 */
	private void setUserTime() {
		int delay = (RENEW_MIN * 60) * 1000;		//5min
		ActionListener checkUpdate = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.print("Updating Users...");
				boolean update = updateTable("user");
				if (update) {
					System.out.print("updated.\n");
				} else {
					System.out.print("update failed.\n");
				}
			}
		};
		timer = new Timer(delay, checkUpdate);
		timer.start();
		System.out.println("U-timer: start.");
	}
	
	/**
	 * Using DefaultTableCellRenderer to accomplish:
	 * 
	 * 1) Color-code "Yes"/"No" for subscriber/moderator column.
	 * 2) Due to override for getColumnClass() from DefaultTableModel, integer align right & string align left.
	 * 	  This will force all data to center in the cell regardless of class.
	 * 
	 * @param table is the table.
	 */
	private void cellMod() {
		for (int i = 0; i < table.getColumnCount(); i++) {
			DefaultTableCellRenderer center = new DefaultTableCellRenderer() {
				private static final long serialVersionUID = -3894436645096493165L;
				@Override
				public Component getTableCellRendererComponent(JTable t,
															   Object value, 
															   boolean isSelected, 
															   boolean hasFocus, 
															   int row, 
															   int column) {
					Component c = super.getTableCellRendererComponent(t, value, isSelected, hasFocus, row, column);
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
	} //cellMod

	/**
	 * Sets up functions of the JTable: enable only row selection, set cell editable false,
	 * draw horizontal & vertical lines between column and row, one selection at an instance.
	 * 
	 * @param table is the JTable.
	 * @author Ching-Ting Huang
	 */
	private void tableSetUp(final String type) {
		table.setCellSelectionEnabled(false);
		table.setDragEnabled(false);
		table.setRowSelectionAllowed(true);
		table.setShowHorizontalLines(false);
		table.setShowVerticalLines(false);
		table.setAutoCreateRowSorter(true);
		table.setAutoCreateColumnsFromModel(false);
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		
		table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				if (!e.getValueIsAdjusting()) {
					//try {
						row = table.convertRowIndexToModel(table.getSelectedRow());
						if (row >= 0) {
							if (flag != null) {
								flag.setText(flag.getText() + "+");
							}
						}
					//} catch (IndexOutOfBoundsException f) {
						
					//}


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
			data[count][0] = allUserNPoints.getRank(entry.getKey().getNick());
			data[count][1] = entry.getKey().getNick();
			data[count][2] = entry.getValue();
			data[count][3] = "No";
			data[count][4] = mods.contains(entry.getKey().getNick()) ? "Yes" : "No";
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
				data[i][2] = "0";
			}
			data[i][3] = "No";
			data[i][4] = mods.contains(user.getNick()) ? "Yes" : "No";
		}
		return data;
	} //putAuthToArray
	
	/**
	 * Force update entire JTable by time constraint.
	 * 
	 * @return success of update.
	 */
	private boolean updateTable(final String para) {
		Object[][] data = para.equals("viewer") ? putCurUserToArray() : putAllUserToArray();
		for (int i = 0; i < data.length; i++) {
			for (int j = 0; j < data[0].length; j++) {
				((DefaultTableModel) table.getModel()).setValueAt(data[i][j], i, j);
			}
		}
		if (para.equals("user") && row > -1) {
			int selected = table.convertRowIndexToView(row);
			table.setRowSelectionInterval(selected, selected);
		}
		return true;
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
	 * @param type is the input type: user, current viewer, version
	 * @return 2D-array representation of the input list.
	 * @author Ching-Ting Huang
	 */
	private Object[][] getList(final String type) {
		Object[][] data = null;
		
		switch (type) {
			case "user": 	data = allUserEmpty ? null : putAllUserToArray();
							break;
			case "viewer":	data = curUserEmpty? null : putCurUserToArray();
							break;
		}
		
		/*} else if (type.equals("version")) {
		
		} */	
		 
		return data;
	} //getList
	
	/**
	 * Stop timer updating TableModel, used when quitting program.
	 * 
	 * @return timer still running = false
	 */
	public boolean stopUpdateTimer() {
		if (timer != null) {
			timer.stop();
			return timer.isRunning();
		}
		return false;
	} //stopUpdateTimer

	/**
	 * This is a JLabel made by caller with added propertyChangeListener. When this label
	 * is passed into this class as a field and its text is later changed when a row is
	 * selected in the table, the propertyChangeListener will notify the change on the 
	 * caller side (Flag to catch row selection in JTable).
	 * 
	 * @param label is the 'flag' to notify the caller a row has been selected in the table.
	 */
	public void setFlag(final JLabel label) {
		flag = label;
	} //setFlag
	
	/**
	 * This is a JLabel made by caller with added propertyChangeListener. When this label
	 * is passed into this class as a field and its text is later changed when a row is
	 * selected in the table, the propertyChangeListener will notify the change on the 
	 * caller side (Flag to catch JTable updates).
	 * 
	 * @param label is the 'flag' to notify the caller data in JTable has been updated.
	 */
	public void setCVChecker(final JLabel label) {
		viewerUpdate = label;
	} //setCVChecker

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
	 * Gets the data at specified row & column.
	 * 
	 * @param row of table.
	 * @param column of table.
	 * @return the cell data.
	 */
	public Object getData(final int row, final int column) {
		return ((DefaultTableModel)table.getModel()).getValueAt(row, column);
	} //getInput
	
	/**
	 * Provide functionality to scroll JTable to desired row. Used for searching.
	 * 
	 * @param row
	 */
	public void scrollTo(int row) {
		int r = table.convertRowIndexToView(row);
		table.scrollRectToVisible(table.getCellRect(r,  0,  true));
	} //scrollTo
	
	/**
	 * Provide functionality to highlight desired row. Used for searching.
	 * 
	 * @param row
	 */
	public void setFocusTo(int row) {
		int r = table.convertRowIndexToView(row);
		table.setRowSelectionInterval(r, r);
	} //setFocusTo
	
	/**
	 * This gets the number of rows there currently is in the JTable.
	 * 
	 * @return number of row in JTable.
	 */
	public int getNumRow() {
		return ((DefaultTableModel)table.getModel()).getRowCount();
	} //getNumRow
	
	/**
	 * This gets the number of columns there currently is in the JTable.
	 * 
	 * @return number of column in JTable.
	 */
	public int getNumCol() {
		return ((DefaultTableModel)table.getModel()).getColumnCount();
	} //getNumCol
	
	/**
	 * Let JTable to be updated with accurate information after UI update.
	 * 
	 * @param row row of JTable.
	 * @param col column of JTable.
	 * @param newpt program-user point input to be added/subtracted to a specific user based on selected row.
	 * @return whether the operation is successful.
	 */
	public boolean updateTable(final int row, final int col, final int newpt) {
		//int r = table.convertRowIndexToView(row);
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				int oldpt = (int) ((DefaultTableModel) table.getModel()).getValueAt(row, col);
				((DefaultTableModel)table.getModel()).setValueAt(oldpt + newpt, row, col);
			}
		});
		return true;
	} //updateTable

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
		
		/**
		 * Initialize fields.
		 * 
		 * @param current is the array of users before most recent timer interrupt.
		 * @param newArray is the array of users after most recent timer interrupt.
		 * @param dtm is the DefaultTableModel being used on JTable currently.
		 * @param unp is the interface to the Map of User-to-points.
		 */
		private CheckCurrentWatcher(User[] current, User[] newArray) {
			prev = current.length > 0 ? current.clone() : new User[0];
			now = newArray.length > 0 ? newArray.clone() : new User[0];
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
			
			if (tmp_prev.isEmpty() && tmp_now.isEmpty()) {
				lists.left.clear();
				lists.join.clear();
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
				Map<User, Integer> unp = allUserNPoints.getUserMap();
				
				if (join.isEmpty() && left.isEmpty() && now.length > 0) {
					//highly doubtful anything will make it pass this if-case, but it's here in case something is wrong
					if (((DefaultTableModel) table.getModel()).getDataVector().isEmpty()) {
						for (int z = 0; z < now.length; z++) {
							Object[] data = new Object[5];
							User u = now[z];
							data[0] = allUserNPoints.getRank(u.getNick());
							data[1] = u.getNick();
							if (unp.containsKey(u)) {
								data[2] = unp.get(u);
							} else {
								data[2] = "0";
							}
							data[3] = "No";
							data[4] = mods.contains(u.getNick()) ? "Yes" : "No";
							((DefaultTableModel) table.getModel()).insertRow(0, data);
						}
					}
					
				} else {
					
 					if (join.size() > 0) {
						for (int i = 0; i < join.size(); i++) {
							Object[] data = new Object[5];
							User u = join.get(i);
							data[0] = allUserNPoints.getRank(u.getNick());
							data[1] = u.getNick();
							if (unp.containsKey(u)) {
								data[2] = unp.get(u);
							} else {
								data[2] = "0";
							}
							data[3] = "No";
							data[4] = mods.contains(u.getNick()) ? "Yes" : "No";
							((DefaultTableModel) table.getModel()).insertRow(0, data);
						}
					} //join size j > 0
					
					if (left.size() > 0) {
						for (int j = 0; j < left.size(); j++) {
							User u = left.get(j);
							for (int k = 0; k < ((DefaultTableModel) table.getModel()).getRowCount(); k++) {
								String name = (String) ((DefaultTableModel) table.getModel()).getValueAt(k, 1);
								if (name.equals(u.getNick())) {
									((DefaultTableModel) table.getModel()).removeRow(k);
								}
							}
						}
					} //left size > 0
				}
				//table.clearSelection();
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
