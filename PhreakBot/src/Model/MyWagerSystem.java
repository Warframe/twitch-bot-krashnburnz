package Model;

import java.util.ArrayList;
import java.util.List;

public class MyWagerSystem {
	List<WagerUser> my_betters;
	List<String> betters;
	List<String> my_choices;
	List<WagerUser> winners;
	int my_totalBets;
	boolean canWager;
	
	public MyWagerSystem(ArrayList<String> theOptions) {
		my_choices = theOptions;;
		my_betters = new ArrayList<WagerUser>();
		betters = new ArrayList<String>();
		my_totalBets = 0;
		canWager = true;
	}


	public boolean addBet(String the_name, int the_choice, int the_amount) {
		boolean answer = false;
		if(canWager) {
			if(!betters.contains(the_name.toLowerCase())) { //has not bet yet, so let them
				if(the_choice > 0 && the_choice <= my_choices.size()) {// Options 1 through n, where n is the max choice option
					if(the_amount > 0) { //must be a positive bet amount
						betters.add(the_name);
						my_betters.add(new WagerUser(the_name, the_choice, the_amount));
						my_totalBets++;
						answer = true;
					}				
				}		
			}
		}
		
		return answer;	
	}
	
	public List<WagerUser> chooseWinner(int the_winning_choice) {
		winners = new ArrayList<WagerUser>();
		for(int i = 0; i < my_betters.size(); i++) {
			if(my_betters.get(i).getChoice() == the_winning_choice) { //is a winner, add to winners list
				winners.add(my_betters.get(i));
			}
		}
		return winners;
		
	}
	
	public int getMyChosenOption(String sender) {
		int result = -1;
		if(betters.contains(sender.toLowerCase())) {
			for(int i = 0; i < my_betters.size(); i++) {
				if(my_betters.get(i).getBetterName().toLowerCase().contentEquals(sender.toLowerCase())) { 
					result = my_betters.get(i).getChoice();
				}
			}
			
		} else {
			System.out.println("USER KEY NOT FOUND");
		}	
		return result;
	}
	
	public int getMyBetAmount(String sender) {
		int result = -1;
		if(betters.contains(sender.toLowerCase())) {
			for(int i = 0; i < my_betters.size(); i++) {
				if(my_betters.get(i).getBetterName().toLowerCase().contentEquals(sender.toLowerCase())) { 
					result = my_betters.get(i).getBetAmount();
				}
			}
			
		} else {
			System.out.println("USER KEY NOT FOUND");
		}	
		return result;
	}
	
	public int getTotalCount() {
		return my_totalBets;
	}
	
	public void canWager(boolean result) {
		canWager = result;
	}
}
