package Model;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Map;
import java.util.Observable;

public class MyCustomAdverts extends Observable implements Runnable, Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Method that will Serialize the User map to save to file.
	 * 
	 * @param Map<User, Integer> the_Usermap
	 * @param String The file name
	 */
	public void saveFile(ArrayList<String> the_customAdverts, String the_name) {   
	      try {
	         FileOutputStream fileOut = new FileOutputStream(the_name);
	         ObjectOutputStream out = new ObjectOutputStream(fileOut);
	         out.writeObject(the_customAdverts);
	         out.close();
	         fileOut.close();
	        	System.out.println("Saving " + the_name + " to file");
	      }
	      catch(IOException i) {
	        	System.out.println("Failure to save " + the_name + " to file.");
	          i.printStackTrace();
	      }
	    }
	
	/**
	 * Method that will de-serialize a saved User map and set the current one to that.
	 * 
	 * @return Map<User, Integer> to be loaded
	 * @param String The file name.
	 */
	@SuppressWarnings("unchecked")
	public static Map<User, Integer> loadFile(String the_name){
	         try {
	        	Map<User, Integer> Usermap;
	            FileInputStream fileIn = new FileInputStream(the_name);
	            ObjectInputStream in = new ObjectInputStream(fileIn);
	            Usermap = (Map<User, Integer>) in.readObject();
	            in.close();
	            fileIn.close();
	        	System.out.println("Loading User map from file successfull.");
	            return Usermap;
	         }
	         catch (IOException i) {
		        	System.out.println("IO exception occured while trying to load User map file.");
	            i.printStackTrace();
	            return null;
	         }
	         catch (ClassNotFoundException c) {
		        	System.out.println("Class exception occured while trying to load User map file.");
	            c.printStackTrace();
	            return null;
	        }
	    }

	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	}

}
