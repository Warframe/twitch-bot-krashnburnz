package Model;
import java.util.Observable;


public class MyLottoSystem extends Observable implements Runnable{
	
	private String my_users;
	
	public MyLottoSystem() {
		my_users = "";
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
				wait(20000);
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
			my_users = my_users + ", " +sender;	
		}

	}
	
	public void emptyUsers() {	
		my_users = "";
	}

	public String getUsers() {
		return my_users;
	}

}
