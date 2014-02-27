package Model;

public class WagerUser {
	private String user;
	private int choice;
	private int amount;

	public WagerUser(String the_name, int the_choice, int the_amount) {
		user = the_name;
		choice = the_choice;
		amount = the_amount;
	}

	public String getBetterName() {
		return user;
	}
	
	public int getBetAmount() {
		return amount;
	}
	
	public int getChoice() {
		return choice;
	}
}
