package Model;
import java.util.Observable;


public class MyTankerPoints extends Observable implements Runnable{

	private String my_users;
	//private static final long totalTime = 5000;	
	//private long startEntryTime;
	
	public MyTankerPoints() {
		my_users = "";
		//startEntryTime = System.currentTimeMillis();
	}
	@Override
	public synchronized void run() {
		 try {
			wait(12000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		while(true) {
			 if(!my_users.isEmpty()) {
			    	setChanged();
			    	notifyObservers();
			 }
			 try {
				wait(12000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public void addUser(String sender, int points) {
		if(my_users.length() < 3) {
			my_users = my_users + sender + " [" + points + "]";
		} else {
			my_users = my_users + ", " +sender + " [" + points + "]";	
		}

	}
	
	public void emptyUsers() {	
		my_users = "";
	}

	public String getUsers() {
		return my_users;
	}
}