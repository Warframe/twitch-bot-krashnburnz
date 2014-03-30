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
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import Model.User;

/**
 * This class populates the list of inputs into a table to be put onto the GUI.
 * 
 * @author Ching-Ting Huang
 * @version UWTCSS 360, Spring 2013. 3-squared-software-systems.
 */
public class ScrollPane {

	private DefaultTableModel model;
	private DefaultTableCellRenderer cell;
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
	 * @param para is the type of inputs being passed in in String representation: Conference(conf),
	 * 		  ProgramChair(pc), SubprogramChair(spc), Reviewer(rev), Author(auth).
	 * @author Ching-Ting Huang
	 */
	public ScrollPane(final String para) {
		row = -1;
		Object[][] data = getList(para);
		String[] header = getHeader(para);
		input = data;

		model = new DefaultTableModel(data, header) {
			private static final long serialVersionUID = -1411446165832277578L;

			@Override
			public boolean isCellEditable(int row, int column) {
				//make all cells editable = false
				return false;
			}
		};

		table = new JTable(model);
		tableSetUp(table, para);
		scrollPane = new JScrollPane(table);
		table.setFillsViewportHeight(true);
		
		cell = new DefaultTableCellRenderer() {
			private static final long serialVersionUID = 1L;

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
		table.setDefaultRenderer(Object.class, cell);
	} //Table

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
		table.setShowHorizontalLines(true);
		table.setShowVerticalLines(true);
		table.setAutoCreateRowSorter(true);
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		if (type.equals("conf")) {
			//no extra column to hide in this case
		} else {
			table.removeColumn(table.getColumn("ID"));	//hide this column from view
		}
		
		table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				if (!e.getValueIsAdjusting()) {
					row = table.getSelectedRow();
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
	 * Putting list of all papers for that particular author into 2D-array
	 * as well as the papers' title, submitted conference and date of submission.
	 * 
	 * @param list is all the papers the author has submitted.
	 * @return 2D-array of the papers and its meta-data.
	 * @author Ching-Ting Huang
	 */
	private Object[][] putUserToArray(final List<User> list) {
		Object[][] data = new Object[list.size()][1];
		for (int i = 0; i < list.size(); i++) {
			Calendar date = Calendar.getInstance();
			data[i][0] = list.get(i).getNick();
			data[i][1] = list.get(i).getPrefix();
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
		if (type.equals("user")) {
			header = new String[]{"User", "points"};
		} else if (type.equals("pc")) {
			header = new String[]{"Title", "Author", "Subprogram Chair", "Assigned Reviewer(s)", "ID"};
		} else if (type.equals("spc")) {
			header = new String[]{"Title", "Author", "Assigned Reviewer(s)", "ID"};
		} else if (type.equals("rev")) {
			header = new String[]{"Title", "Author", "ID"};
		} else if (type.equals("auth")) {
			header = new String[]{"Title", "Submitted Conference", "Date Submitted", "ID"};
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
			
		} else if (type.equals("pc")) {
			
		} else if (type.equals("spc")) {
			
		} else if (type.equals("rev")) {
			
		} else if (type.equals("auth")) {
			
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
	 * Add additional items into the JTable/JScrollPane.
	 * 
	 * @param object is the array of data specified by the caller.
	 * @author Ching-Ting Huang
	 */
	public void addToTable(Object[] object) {
		/* Object[] object =
		 * Conference tab: Date, Conference Name, Program Chair Name
		 * Program Chair tab: Paper Title, Author Name, Subprogram Chair Name
		 * Subprogram Chair tab: Paper Title, Author Name, Reviewers List
		 * Reviewer tab: Paper Title, Author Name
		 * Author tab: Paper Title, Submitted Conference Name, Submission Date
		 */
		model.addRow(object);	
		int row = model.getRowCount();
		int col = model.getColumnCount();
		Object[][] temp = new Object[row][col];
		for (int i = 0; i < row; i++) {
			for (int j = 0; j < col; j++) {
				temp[i][j] = model.getValueAt(i, j);
			}
		}
		input = temp;
	} //addToTable

	/**
	 * Remove item in 2D-array at index. If looking for selected index, can call
	 * getSelectedRowNum() to get index.
	 * 
	 * @param index is the index number of the item to be deleted from the table.
	 * @return check if item is properly removed.
	 * @author Ching-Ting Huang
	 */
	public boolean removeFromTable(final int index) {
		boolean papersStillExist  = true;
		model.removeRow(index);
		int row = model.getRowCount();
		int col = model.getColumnCount();
		Object[][] temp = new Object[row][col];
		for (int i = 0; i < row; i++) {
			for (int j = 0; j < col; j++) {
				temp[i][j] = model.getValueAt(i, j);
			}
		}
		
		//Edited by Daniel, Jonathan
		input = temp;
		return papersStillExist;
	} //removeFromTable

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
