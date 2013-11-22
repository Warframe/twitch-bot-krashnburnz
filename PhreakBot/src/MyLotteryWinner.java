import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Observable;
import java.util.Random;


public class MyLotteryWinner extends Observable implements Runnable {
	
	private int my_lotto_timer;
	
	private List<String> lottoPeople = new ArrayList<String> ();
	
	public MyLotteryWinner(int the_lotto_timer) {
		my_lotto_timer = the_lotto_timer;
	}

	@Override
	public synchronized void run() {
		 try {
			wait(my_lotto_timer);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		while(true) {
			 if(!lottoPeople.isEmpty()) {
			    	setChanged();
			    	notifyObservers();
			 }
			 try {
				wait(my_lotto_timer);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
	public String getWinner() {
		return randomSelectWinner();
	}

	public void addUser(String sender) {
		if(!lottoPeople.contains(sender)) {
			lottoPeople.add(sender);
		}

	}
	
	public void emptyUsers() {	
		lottoPeople.clear();
	}

	private String randomSelectWinner() {
		Random rand = new Random();
		long seed = new Date().getTime();
		rand.setSeed(seed);
		int max = lottoPeople.size();

		// nextInt is normally exclusive of the top value,
		// so add 1 to make it inclusive
		int randomNum = rand.nextInt(max);
		System.out.println("The random number chosen is: " + randomNum);
		System.out.println("Count is : " + max);
		String winner = lottoPeople.get(randomNum);
		return winner;
	}
}
