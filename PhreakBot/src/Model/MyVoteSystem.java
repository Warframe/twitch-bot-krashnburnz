package Model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class MyVoteSystem {
	Map<String, Integer> users;
	ArrayList<String> my_Options;
	Map<String, Integer> theVotes;
	private int max;
	
	public MyVoteSystem(ArrayList<String> theOptions) {
		my_Options = theOptions;
		theVotes = new HashMap<String, Integer>();
		users = new HashMap<String, Integer>();
		intilizeMap();
		max = -1;
	}

	private void intilizeMap() {
		for(int i = 0; i < my_Options.size(); i++) {
			theVotes.put( my_Options.get(i).toLowerCase(), 0);
		}
		
	}

	public void addVote(int input, String sender) {
		int count;
		if(input > 0 && input <= my_Options.size()) {
			if(!users.containsKey(sender.toLowerCase())) {
				if(theVotes.containsKey(my_Options.get(input-1))) {
					count = theVotes.get(my_Options.get(input-1));
					theVotes.put(my_Options.get(input-1), count + 1);
					users.put(sender.toLowerCase(), input);
				} else {
					System.out.println("This Key is not in the Vote Map!");
				}
			} else {
				System.out.println("User already exists, doing nothing!");
			}
		}
		
	}
	
	public ArrayList<String> getOptions() {
		return my_Options;
	}
	
	public int getMyChosenOption(String sender) {
		if(users.containsKey(sender.toLowerCase())) {
			return users.get(sender).intValue();
		} else {
			System.out.println("USER KEY NOT FOUND");
			return -1;
		}	
	}
	
	public String endVote() {
		String winningOption = "Error";
		ArrayList<String> voteOptions = new ArrayList<String>();
		
		Iterator<String> keyIt = theVotes.keySet().iterator();
		while(keyIt.hasNext()) {
			voteOptions.add((String) keyIt.next());
		}
		ArrayList<Integer> voteValues = new ArrayList<Integer>();
		Iterator<Integer> valueIt = theVotes.values().iterator();
		while(valueIt.hasNext()) {
			voteValues.add((Integer) valueIt.next());
		}
		for(int i = 0; i < voteValues.size(); i++) {
			int indexvalue = voteValues.get(i).intValue();
			if(indexvalue > max) {
				max = indexvalue;
				winningOption = voteOptions.get(i);
			}
		}
		return winningOption;		
	}
	
	public int getMax() {
		return max;
	}
	
	public void clearMax() {
		max = -1;
	}
}
