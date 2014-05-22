package View;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import Model.MyBot;

/**
 * Version tab displaying past version of the program including version number, change notes, date.
 * Including functionality to display user's current program version + update program to latest version.
 * @author Ching-Ting Huang
 */
public class Version extends JPanel {
	
	private static final long serialVersionUID = -2544375717703845827L;
	
	/**
	 * The controller.
	 */
	private MyBot theBot;
	/**
	 * The scroll pane containing all revision of the program.
	 */
	private ScrollPane scroll;
	
	
	public Version(MyBot bot) {
		theBot = bot;
		//scroll = new ScrollPane(theBot, "version");
		setup();
	}
	
	private void setup() {
		DefaultTableModel dtm = new DefaultTableModel() {
			private static final long serialVersionUID = -1411446165832277578L;
			@Override
			public boolean isCellEditable(int row, int column) {
				//make all cells editable = false
				return false;
			}
		};
		dtm.setColumnIdentifiers(new String[]{"Version", "Released"});
		JTable table = new JTable(dtm);
		JScrollPane pane = new JScrollPane(table);
				//scroll.getScrollPane();
		pane.setWheelScrollingEnabled(true);
		
		
		JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, pane, infoPanel());
		split.setDividerLocation(200);
		setLayout(new BorderLayout());
		add(split, BorderLayout.CENTER);
		
		JPanel tmp = new JPanel();
		tmp.add(new JLabel("To Be Implemented."));
		//add(tmp, BorderLayout.CENTER);
		add(misc(), BorderLayout.SOUTH);
	} //generalSetup
	
	
	private Container infoPanel() {
		JPanel info = new JPanel();
		info.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createTitledBorder("Description"),
				BorderFactory.createEmptyBorder(5,5,5,5)));
		return info;
	} //infoPanel
	
	private Container misc() {
		Container border = new Container();
		border.setLayout(new FlowLayout());
		border.add(labels());
		border.add(interactive());
		//border.add(labels(), BorderLayout.WEST);
		//border.add(interactive(), BorderLayout.EAST);
		return border;
	} //misc
	
	private Container labels() {
		Container box = new Container();
		box.setLayout(new BoxLayout(box, BoxLayout.Y_AXIS));
		
		JLabel lbcloud = new JLabel("Current Version:");
		JLabel txcloud = new JLabel("VER. TEST");
		JLabel lbcur = new JLabel("Your Version:");
		JLabel txcur = new JLabel("VER. TEST");
		
		lbcloud.setAlignmentX(CENTER_ALIGNMENT);
		txcloud.setAlignmentX(CENTER_ALIGNMENT);
		lbcur.setAlignmentX(CENTER_ALIGNMENT);
		txcur.setAlignmentX(CENTER_ALIGNMENT);
		
		box.add(lbcloud);
		box.add(txcloud);
		box.add(lbcur);
		box.add(txcur);
		return box;
	} //labels
	
	private Container interactive() {
		Container border = new Container();
		Container btns = new Container();
		border.setLayout(new BorderLayout());
		btns.setLayout(new FlowLayout());
		
		JButton update = new JButton("Update");
		JButton log = new JButton("ChangeLog");
		JCheckBox backup = new JCheckBox("Create Backup");
		
		update.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
		
		log.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
		
		backup.setSelected(true);
		backup.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		
		btns.add(update);
		btns.add(log);
		border.add(btns, BorderLayout.NORTH);
		border.add(backup, BorderLayout.SOUTH);
		return border;
	} //interactive
} //class
