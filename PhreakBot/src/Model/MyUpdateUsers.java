package Model;

import java.util.Observable;

public class MyUpdateUsers extends Observable implements Runnable{
	
	private User[] my_users;
	
	private MyBot my_bot;
	
	private int updateTimerInSeconds = 10;
	
	private int my_timer;
	
	String my_channel;
	public MyUpdateUsers(String the_channel, MyBot the_bot) {
		my_channel = the_channel;
		my_bot = the_bot;
		my_timer = (updateTimerInSeconds * 1000);
	}
	
	public synchronized void run() {
		//this will run a thread to update the current Users in the channel watching
		//this will minimize returning 0 userpoints for people that just joined, yet have more points then 0!
    	try {
			wait (1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	while(true) {
        	int i;
        	org.jibble.pircbot.User[] temp_users = my_bot.getUsers(my_channel);
        	User[] newUserArray = new User[temp_users.length];
        	for(i = 0; i < temp_users.length; i++) {
        		newUserArray[i] = new User(temp_users[i].getPrefix(), temp_users[i].getNick());
        	}
            my_users = newUserArray;
        	setChanged();
        	notifyObservers(my_users);
        	try {
    			wait (my_timer);
    		} catch (InterruptedException e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		}
    	}

	}

}
