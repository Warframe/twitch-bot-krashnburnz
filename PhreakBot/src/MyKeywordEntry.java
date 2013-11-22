import java.util.Observable;


public class MyKeywordEntry extends Observable implements Runnable{

	private String my_users;
	//private static final long totalTime = 5000;	
	//private long startEntryTime;
	
	public MyKeywordEntry() {
		my_users = "";
		//startEntryTime = System.currentTimeMillis();
	}
	@Override
	public synchronized void run() {
		 try {
			wait(10000);
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
				wait(10000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public void addUser(String sender) {
		if(my_users.length() < 3) {
			my_users = my_users + sender;
		} else {
			my_users = my_users + ", " + sender;	
		}

	}
	
	public void emptyUsers() {	
		my_users = "";
	}

	public String getUsers() {
		return my_users;
	}
}
