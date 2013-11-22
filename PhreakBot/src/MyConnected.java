import java.util.Observable;


public class MyConnected extends Observable implements Runnable{

	@Override
	public synchronized void run() {
		 try {
			wait(60000);
	    	setChanged();
	    	notifyObservers();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		while(true) {
			 try {
				wait(60000);
		    	setChanged();
		    	notifyObservers();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
