package Model;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Observable;
import java.util.Set;

import org.jibble.pircbot.PircBot;




public class MyBotUserPoints extends Observable implements Runnable, Serializable{
	
	/**
	 * 
	 */
	boolean stream_active = false;
	private static final long serialVersionUID = 2268218852535355614L;
	Map<User, Integer> UsersMap;
	String my_channel;
	User[] my_Users;
	int my_runCount;
	
	private boolean lottoOn=false;
	
	public MyBotUserPoints(String the_channel, User[] the_Users) {
		updateUserMap();
		setChannel(the_channel);
		setCurrentUsers(the_Users);
		my_runCount = 0; 
	}

	public synchronized void run() {
    	try {
			wait (20000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	        while (true) {
	        	UsersMap = loadFile("User_Map_File");
	        	if(UsersMap == null) {
	        		System.out.println("UserMap is NULL");
	        		Map<User, Integer> themap = new HashMap<User, Integer>();
	        		themap.put(new User("@", "user10204354"), 1);
	        		UsersMap = themap;
	        	} 
	        	int i;
	        	if(my_Users.length > 0 && stream_active) {
		        	for (i = 0; i < my_Users.length; i ++) {
		        		if(UsersMap.containsKey(my_Users[i])) {
		            		int num = (int) UsersMap.get(my_Users[i]);
		            		UsersMap.remove(my_Users[i]);
		            		num += 1;
		            		UsersMap.put(my_Users[i], num);
		            		//System.out.println("Added 1 point to " + my_Users[i]+" with a current total of " + UsersMap.get(my_Users[i]) + " points.");
		        		} else {
		            		UsersMap.put(my_Users[i], 1);
		            		//System.out.println("Added new User to map and added 1 point to " + my_Users[i] +" with a total of " + UsersMap.get(my_Users[i]) + " points.");
		        		}
		        	}
	        	} else {
		        	System.out.println("Not incrementing points. Either user length is to short, or stream not currently active.");	
	        	}
	        	my_runCount++;
	        	saveFile(UsersMap, "User_Map_File");
	        	setChanged();
	        	notifyObservers();
	        	try {
	    			wait (300000);
	    		} catch (InterruptedException e) {
	    			// TODO Auto-generated catch block
	    			e.printStackTrace();
	    		}
	        }
	}
	
	public void setUserMap(Map<User, Integer> the_map) {
		UsersMap = the_map;
	}
	
	public Map<User, Integer> getUserMap() {
		return UsersMap;
	}
	
	public void setChannel(String the_channel) {
		my_channel = the_channel;
	}
	
	public void setActivelyStreaming(boolean answer) {
		stream_active = answer;
	}
	public void setCurrentUsers(User[] the_Users) {
		my_Users = the_Users;
	}
	
	public void setLottoActive(boolean answer) {
		lottoOn = answer;
	}
	
	public int getMyCurrentPoints(String the_User) {
		User new_User = null;
		int i;
    	for (i = 0; i < my_Users.length; i ++) {
    		if  (my_Users[i].equals(the_User)) {
        		new_User = my_Users[i];	
    		}
    	}
		if(new_User != null) {
			if(UsersMap.get(new_User) != null) {
	    		return UsersMap.get(new_User);
			} else {
				System.out.println("USER DOES NOT EXIST IN User_map");	
		    	return 0;
			}

		} else {
			System.out.println("USER DOES NOT EXIST IN my_Users");	
	    	return -1;
		}

	
	}
	
	public void updateUserMap() {
    	UsersMap = loadFile("User_Map_File");
    	if(UsersMap == null) {
    		System.out.println("UserMap is NULL");
    		Map<User, Integer> themap = new HashMap<User, Integer>();
    		themap.put(new User("@", "user10204354"), 1);
    		UsersMap = themap;
    	} 
	}
	
	
	/**
	 * Method that will Serialize the User map to save to file.
	 * 
	 * @param Map<User, Integer> the_Usermap
	 * @param String The file name
	 */
	public void saveFile(Map<User, Integer> the_Usermap, String the_name) {   
	      try {
	         FileOutputStream fileOut = new FileOutputStream(the_name);
	         ObjectOutputStream out = new ObjectOutputStream(fileOut);
	         out.writeObject(the_Usermap);
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

	public void clearAllPoints() {
		UsersMap.clear();
    	saveFile(UsersMap, "User_Map_File");
		System.out.println("UserMap has been cleared.");
	}
	
	public synchronized void decrementTankerPoints(String user, int the_amount) {
		Set<Entry<User, Integer>> tempSet = UsersMap.entrySet();
		Entry<User, Integer> the_current_entry;
		User the_user;
		String nick;
		boolean exists = false;
		Iterator<Entry<User, Integer>> iterator = tempSet.iterator();
		while(iterator.hasNext()) {
			the_current_entry = iterator.next();
			the_user = the_current_entry.getKey();
			nick = the_user.getNick().toLowerCase();
			if(nick.toLowerCase().equalsIgnoreCase(user.toLowerCase())) {
				exists = true;
	        	int num = (int) UsersMap.get(the_user);
	        	int tempnum = num - the_amount;
	        		if(tempnum < 0) {
	        			System.out.println("ERROR, USER DOES NOT HAVE ENOUGH POINTS");
	        		} else {
	            		UsersMap.put(the_user, tempnum);
	                	saveFile(UsersMap, "User_Map_File");
	        		}
                	break;	
			}
			else {
				exists = false;
				//System.out.println("USER " +nick + " IS NOT EQUAL to " + user);
			}
			
		}
    	if (!exists) {
    		System.out.println("The user " + user + " does not exist.");
    	}
    }
	
	public synchronized void incrementTankerPoints(String user, int the_amount) {
		Set<Entry<User, Integer>> tempSet = UsersMap.entrySet();
		Entry<User, Integer> the_current_entry;
		User the_user;
		String nick;
		boolean exists = false;
		Iterator<Entry<User, Integer>> iterator = tempSet.iterator();
		while(iterator.hasNext()) {
			the_current_entry = iterator.next();
			the_user = the_current_entry.getKey();
			nick = the_user.getNick().toLowerCase();
			if(nick.toLowerCase().equalsIgnoreCase(user.toLowerCase())) {
				exists = true;
	        	int num = (int) UsersMap.get(the_user);
	        	int tempnum = num + the_amount;
	        		if(tempnum < 0) {
	        			System.out.println("Error, user has negative points");
	        		} else {
	            		UsersMap.put(the_user, tempnum);
	                	saveFile(UsersMap, "User_Map_File");
	        		}
                	break;	
			}
			else {
				exists = false;
			}
			
		}
    	if (!exists) {
    		System.out.println("The user " + user + " does not exist.");
    	}
    }

	public void saveBackupFile() {
        Calendar c = Calendar.getInstance();
        String timeStamp = "_" +c.get(Calendar.MONTH) + "_" + c.get(Calendar.DAY_OF_MONTH)+ "_" + c.get(Calendar.YEAR) + "_"
        		+ "_" +   c.get(Calendar.HOUR) + c.get(Calendar.MINUTE)+ c.get(Calendar.SECOND);
		System.out.println("Saving User Map to file name --> User_Map_File_backup_" + timeStamp);
		saveFile(UsersMap, "User_Map_File_backup_" + timeStamp);
	}
	
}
	
