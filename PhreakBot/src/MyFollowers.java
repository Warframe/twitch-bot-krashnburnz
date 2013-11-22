
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;


public class MyFollowers  extends ArrayList<String>{

	/**
	 * 
	 */
	private static final long serialVersionUID = -430310740853253723L;

	private int my_count;
	public MyFollowers() {
		my_count = 0;
	}
	public void loadFile(String the_name){
        try {
           Scanner scanner = new Scanner(new File(the_name));
       	   String the_follower;
       	   while(scanner.hasNext()) {
       		the_follower = scanner.next();
       		add(the_follower);
       		System.out.println("Added " + the_follower + " to the array list. User number: " + my_count);
       		my_count++;
       	   }
       	   System.out.println("All followers loaded successfull.");
       	   scanner.close();
        }
        catch (IOException i) {
	        	System.out.println("IO exception occured while trying to load the followers map file.");
           i.printStackTrace();
        }
   }
	
	public String chooseRandomWinner() {
		Random rand = new Random();
		int min = 0;
		int max = my_count;

		// nextInt is normally exclusive of the top value,
		// so add 1 to make it inclusive
		int randomNum = rand.nextInt(max - min + 1) + min;
		System.out.println("The random number chosen is: " + randomNum);
		System.out.println("Count is : " + my_count);
		String winner = this.get(randomNum);
		return winner;
		
	}
}
