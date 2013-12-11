package View;

public class AnimateConnect implements Runnable {

	private MyBotLogin myLoginWindow;
	public AnimateConnect(MyBotLogin the_LoginWindow)  {
		myLoginWindow = the_LoginWindow;
	}

	@Override
	public synchronized void run() {
		int pass = 1;
		System.out.println("Trying to connect...please wait...");
		while(true) {
			if(pass == 1) {
				pass++;
				myLoginWindow.setConnectBtnName("Please Wait. Connecting.     ");
				try {
					wait(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			else if(pass == 2) {
				pass++;
				myLoginWindow.setConnectBtnName("Please Wait. Connecting..    ");
				try {
					wait(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			else if(pass == 3) {
				pass++;
				myLoginWindow.setConnectBtnName("Please Wait. Connecting...   ");
				try {
					wait(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			else if(pass == 4) {
				pass++;
				myLoginWindow.setConnectBtnName("Please Wait. Connecting....  ");
				try {
					wait(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			else if(pass == 5) {
				pass++;
				myLoginWindow.setConnectBtnName("Please Wait. Connecting..... ");
				try {
					wait(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			else { //pass is 6, reset to one
				pass = 1;
				myLoginWindow.setConnectBtnName("Please Wait. Connecting......");
				try {
					wait(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
		}

		
	}
}
