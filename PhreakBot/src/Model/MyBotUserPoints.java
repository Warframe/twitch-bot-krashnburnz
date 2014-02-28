package Model;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.io.Serializable;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
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
	List<String> sortedPointsList;
	
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
	        	updateRankedList();
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
        String timeStamp = "_" + (c.get(Calendar.MONTH ) + 1) + "_" + c.get(Calendar.DAY_OF_MONTH)+ "_" + c.get(Calendar.YEAR) + "_"
        		+ "_" +   c.get(Calendar.HOUR) + c.get(Calendar.MINUTE)+ c.get(Calendar.SECOND);
		System.out.println("Saving User Map to file name --> User_Map_File_backup_" + timeStamp);
		saveFile(UsersMap, "User_Map_File_backup_" + timeStamp);
	}
	
	private Map sortByValue(Map map) {
		List list = new LinkedList(map.entrySet());
	     Collections.sort(list, new Comparator() {
	          public int compare(Object o1, Object o2) {
	               return ((Comparable) ((Map.Entry) (o1)).getValue())
	              .compareTo(((Map.Entry) (o2)).getValue());
	          }
	     });

	    Map result = new LinkedHashMap();
	    for (Iterator it = list.iterator(); it.hasNext();) {
	        Map.Entry entry = (Map.Entry)it.next();
	        result.put(entry.getKey(), entry.getValue());
	    }
	    return result;
	} 
	
	public void updateRankedList() {
		Set userList = sortByValue(UsersMap).keySet();
    	Iterator<User> it = userList.iterator();
    	sortedPointsList = new ArrayList<String>();
    	while(it.hasNext()) {
    		String userName = it.next().getNick().toLowerCase();
    		sortedPointsList.add(userName);
    		Collections.reverse(sortedPointsList);
    	}
	}
	public int getRank(String user) {
		int answer = -1;
		if(sortedPointsList != null) {
			if(sortedPointsList.contains(user)) {
				answer = sortedPointsList.indexOf(user)+1; //because of 0 indexing
			} else {
				answer = 0;
			}
		}
		return answer;	
	}
	
	public int getTotalUserCount() {
		int answer = -1;
		if(sortedPointsList != null) {
			answer = sortedPointsList.size();
		}
		return answer;
		
	}

	public boolean importUserMap(File file) {
		saveBackupFile(); //Auto Save of a backup
		Map<User, Integer> tempUserMap = new HashMap<User, Integer>();
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(file));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String line = null;
		String username;
		int points;
		try {
			while ((line = reader.readLine()) != null) {
				Scanner sc = new Scanner(line);
				if(sc.hasNext()) {
					username = sc.next();
					if(sc.hasNextInt()) {
						points = sc.nextInt();

						tempUserMap.put(new User("", username.toLowerCase()), points);
						
					}
				}
				sc.close();
			}
			saveFile(tempUserMap, "User_Map_File"); //Save new map to file overwriting original
			updateUserMap(); //Now update everything with new map
			updateRankedList(); //Update the ranked list with the new map

			reader.close();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(!tempUserMap.isEmpty()) {
			this.setUserMap(tempUserMap);
			//custom title, custom icon
			return true;
		} else {
			System.out.println("The temp User map was empty. Import of User Map was not successful!");
			return false;
		}
		
	}

	public boolean exportUserMap() {
		@SuppressWarnings("rawtypes")
		Iterator it = this.getUserMap().entrySet().iterator();
	    int count = 1;
	    ArrayList<String> list = new ArrayList<String>();
	    while (it.hasNext()) {
	        @SuppressWarnings("rawtypes")
			Map.Entry pairs = (Map.Entry)it.next();
	        list.add(pairs.getKey() + " " + pairs.getValue());
	        it.remove(); // avoids a ConcurrentModificationException
	        count++;
	    }
	    System.out.println("Size of List" + list.size());
	    
	    FileWriter file = null;
		try {
			Calendar c = Calendar.getInstance();
			String timeStamp = "_" + (c.get(Calendar.MONTH ) + 1) + "_" + c.get(Calendar.DAY_OF_MONTH)+ "_" + c.get(Calendar.YEAR) + "_"
	        		+ "_" +   c.get(Calendar.HOUR) + c.get(Calendar.MINUTE)+ c.get(Calendar.SECOND);
			file = new FileWriter("export_user_points_" + timeStamp + ".txt");
			BufferedWriter buff = new BufferedWriter(file);
		    PrintWriter out = new PrintWriter(buff);

		    for( int x = 0; x < list.size(); x++)
		    {
		    out.println(list.get(x));
		    }

		    out.close();
			return true;
		} catch (IOException e) {
			
			e.printStackTrace();
			return false;
		}
	    
	}
}
	
