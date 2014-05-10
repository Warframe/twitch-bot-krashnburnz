/*  
 * Last Edited: 06/07/2013 by Ching-Ting
 * 
 * [NOTE]: To set up propertyChangeListener, setFlag() method MUST be called by
 * passing in a JLabel.
 */
package View;

import java.awt.Color;
import java.awt.Component;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
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

	private MyBot theBot;
	private MyBotUserPoints allUserNPoints;
	private User[] curU;
	private DefaultTableModel generalModel;
	private DefaultTableModel versionModel;
	private JTable table;
	private JScrollPane scrollPane;
	private JLabel flag;
	private Object[][] input;
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
} //class
