import java.util.Observable;


public class MyLotteryAdvert extends Observable implements Runnable{

	private int timer;
	
	private boolean lottoOn;
	
	public MyLotteryAdvert (int the_timer, boolean lottoActive) {
		timer = the_timer /5;
		lottoOn = lottoActive;
	}
	@Override
	public synchronized void run() {
		 try {
			wait(timer);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		while(true) {
			 if(lottoOn) {
			    	setChanged();
			    	notifyObservers();
			 }
			 try {
				wait(timer);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
	
	public void setLottoOn (boolean isOn) {
		lottoOn = isOn;
	}

}
