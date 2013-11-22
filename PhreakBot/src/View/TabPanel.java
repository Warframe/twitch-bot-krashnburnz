/*
 * Controller test class
 * Authors: Ching-Ting Huang
 * TCSS 360 (Group project) Spring 2013 
 * University of Washington
 */
package View;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.JTabbedPane;

/**
 * This class extends the JTabbedPane which formats the panels into tabs.
 * 
 * @author Daniel Henderson
 * @version 0.1
 */
public class TabPanel extends JTabbedPane {

	private static final long serialVersionUID = -7022036773088073145L;
	private List<JPanel> thePanels;
	
	/**
	 * Calling methods to populate the tabs.
	 * 
	 * @param control is the Controller in the model.
	 */
	public TabPanel() {

		final List<JPanel> panels = createTabs();
		visibility(panels);
	} //PanelMain
	
	/**
	 * Creates the list of JPanels that will be added to the tabbed pane.
	 * 
	 * @return the list of panels.
	 */
	private List<JPanel> createTabs() {
		List<JPanel> panels = new ArrayList<JPanel>();

		thePanels = panels;
		return panels;
	} //createTabs
	
	public List<JPanel> getListofPanels() {
		return thePanels;
	}
	
	/**
	 * This method determines which panels are visible and which are not based on the 
	 * current user's permission.
	 * 
	 * @param panels is the panels that are added to the tabbed pane.
	 */
	public void visibility(final List<JPanel> panels) {
		
		// Everyone should have a conference tab and User settings which are index 0 and 1
		addTab(panels.get(0).getName(), panels.get(0));
		addTab(panels.get(1).getName(), panels.get(1));
		
		/* Now lets check the remaining roles and set the appropriate window to Visible if true
		   We dont need to check admin (or normal USER), because they get the default windows ONLY.
		  permission order: 0:Author, 1:Reviewer, 2:SPC, 3:PC, 4:Admin
		  panel order: 0:Conferences, 1:USerSettings, 2:SubmittedPapers, 3:Review, 4:SubProgramChair, 5:ProgramChair*/
		
		
	} //visibility
} //class
