package Model;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;
import java.util.Scanner;

import org.jibble.pircbot.IrcException;
import org.jibble.pircbot.NickAlreadyInUseException;
import org.jibble.pircbot.PircBot;
public class MyBot extends PircBot implements Observer{
	
	private boolean lottoOn;
	
	private boolean currentlyStreaming;
	
	private boolean giveawayOn = false;
	
	private boolean auctionOn = false;
	
	private int my_lottery_cost;
	
	private int lotto_time_counter;
	
	private String my_bot_name;
	
	private String my_keyword = "";
	
	private String channel_owner;
	
	private String my_points_name;
	
	private ArrayList<String> the_ops;
	
	private String my_channel = ""; 
	
	private String my_message;
	
	private int my_lottery_timer;
	
	private List<String> eventKeywordPeople = new LinkedList<String> ();

	private List<String> lottoPeople = new ArrayList<String> ();
	
	private List<String> tankerPointsQue = new LinkedList<String> ();
	
	private Map<User, Integer> my_usersMap = new HashMap<User, Integer>();;
	
	private User[] my_users;
	
	private MyBotUserPoints my_botUsers;
	
	private MyKeywordEntry my_keywordEntrys;
	
	private MyTankerPoints my_userTankerPoints;
	
	private MyLottoSystem my_lottoSystem;
	
	private MyLotteryWinner my_lottoWinner;
	
	private MyLotteryAdvert my_lottoAdvert;
	
	private MyFollowers my_followers;
	
	private MyConnected my_connected;
	
	private int auctionHighBidAmount = 0;
	
	private String auctionHighBidder = "";

    public MyBot(String name, boolean lotteryEnabled, boolean accumulateOnStartUp, ArrayList<String> ops, String the_owner, String the_pointsname, int the_lotto_cost, 
    		int the_lottery_timer, String channel) {
    	
    	this.my_bot_name = name.toLowerCase();
        this.setName(name);
        this.lottoOn = lotteryEnabled;
        this.currentlyStreaming = accumulateOnStartUp;
        this.the_ops = ops;
        this.channel_owner= the_owner;
        this.my_points_name = the_pointsname;
        this.my_lottery_cost = the_lotto_cost;
        this.my_lottery_timer = the_lottery_timer;
        this.my_channel = channel;
        
        lotto_time_counter = 0;
        
        my_botUsers = new MyBotUserPoints(my_usersMap, my_channel, my_users);
    	Thread pointAdder_t = new Thread(my_botUsers);
    	my_botUsers.addObserver(this);
    	pointAdder_t.start();
        checkIfStreamingByDefualt(accumulateOnStartUp);
        
        
    	my_keywordEntrys = new MyKeywordEntry();
    	Thread KeywordEntrys_t = new Thread(my_keywordEntrys);
    	my_keywordEntrys.addObserver(this);
    	KeywordEntrys_t.start();
    	
    	my_userTankerPoints = new MyTankerPoints();
    	Thread tankerPoints_t = new Thread(my_userTankerPoints);
    	my_userTankerPoints.addObserver(this);
    	tankerPoints_t.start();
    	
    	my_followers = new MyFollowers();
    	System.out.println("Loading followers.......");
    	my_followers.loadFile("followers.txt");
    	
    	
    	my_lottoSystem = new MyLottoSystem();
    	Thread lottoSystem_t = new Thread(my_lottoSystem);
    	my_lottoSystem.addObserver(this);
    	lottoSystem_t.start();
    	
    	
    	my_lottoWinner = new MyLotteryWinner(my_lottery_timer);
    	Thread lottoWinner_t = new Thread(my_lottoWinner);
    	my_lottoWinner.addObserver(this);
    	lottoWinner_t.start();
    	
    	my_lottoAdvert = new MyLotteryAdvert(my_lottery_timer, lottoOn);
    	Thread lottoAdvert_t = new Thread(my_lottoAdvert);
    	my_lottoAdvert.addObserver(this);
    	lottoAdvert_t.start();

    	my_connected = new MyConnected();
    	Thread connected_t = new Thread(my_connected);
    	my_connected.addObserver(this);
    	connected_t.start();
    	
    	
    }

    private void checkIfStreamingByDefualt(boolean streaming) {
    	if(streaming) {
        	my_botUsers.setActivelyStreaming(streaming);
    	}

		
	}

	public void onMessage(String channel, String sender,
                       String login, String hostname, String message) {
    	my_channel = channel; 
        my_message = message;
    	String command = "";
    	
    	@SuppressWarnings("resource")
		Scanner scanner = new Scanner(my_message);
    	if(scanner.hasNext()) {
        	command = scanner.next();	
    	}
    	
        if (message.toLowerCase().contains("!setkeyword") && sender.equals(channel_owner) ||
        		(message.toLowerCase().contains("!setkeyword") && the_ops.contains(sender))) {
        	if(scanner.hasNext()) {
        		my_keyword = scanner.next();
            	giveawayOn = true;
                sendMessage(channel, sender + ": The giveaway keyword " +my_keyword  +" has been set. The giveaway is now active.");
        	} else {
                sendMessage(channel, sender + ": Lack of arguments to set keyword and start event.");
        	}
        }
        
        if (message.toLowerCase().contains("!checkpoints") && sender.equals(channel_owner) ||
        		(message.toLowerCase().contains("!checkpoints") && the_ops.contains(sender))) {
        	String user;
        	if(scanner.hasNext()) {
        		user = scanner.next();
                sendMessage(channel, user +"'s current " + my_points_name + " are: " + my_botUsers.getMyCurrentPoints(user));
        	} else {
                sendMessage(channel, sender + ": Lack of arguments to get user's points.  Proper format: !checkpoints <username>");
        	}
        }
        
        if (message.equalsIgnoreCase("!removekeyword") && sender.equals(channel_owner) ||
        		(message.equalsIgnoreCase("!removekeyword") && the_ops.contains(sender))) {
        		my_keyword = "";
            	giveawayOn = false;
            	eventKeywordPeople.clear();
                sendMessage(channel, sender + ": The giveaway keyword has been removed and the giveaway is now complete.");        	
        }
        
        if (command.equalsIgnoreCase("!startstream") && sender.equals(channel_owner) ||
        		(command.equalsIgnoreCase("!startstream") && the_ops.contains(sender))) {
        		if(currentlyStreaming) {
        			sendMessage(channel, sender + ": Stream ALREADY ACTIVE and " + my_points_name + " have been accumulating! Do NOT forget to stop the Bot using the !stopstream command.  ><");

        		} else {
            		currentlyStreaming = true;
            		my_botUsers.setActivelyStreaming(currentlyStreaming);
                    sendMessage(channel, sender + ": Stream now Active--> "+ my_points_name + " are NOW being accumulated.");
        		}
        }
        
        if (command.equalsIgnoreCase("!stopstream") && sender.equals(channel_owner) ||
        		(command.equalsIgnoreCase("!stopstream") && the_ops.contains(sender))) {
        		if(currentlyStreaming) {
            		currentlyStreaming = false;
            		my_botUsers.setActivelyStreaming(currentlyStreaming);
                    sendMessage(channel, sender + ": Stream now Inactive--> " + my_points_name + " are NOT being accumulated.");
        		} else { 
                    sendMessage(channel, sender + ": Stream IS ALREADY INACTIVE and " + my_points_name + " have not been accumulating.");

        		}

        	
        }
        
        if (command.equalsIgnoreCase("!emptyAllPoints") && sender.equals(channel_owner) ||
        		(command.equalsIgnoreCase("!emptyAllPoints") && the_ops.contains(sender))) {
        		my_botUsers.clearAllPoints();
                sendMessage(channel, sender + ": All " + my_points_name + " have been reset.");
        	
        }
        
        if (command.equalsIgnoreCase("!chooseRandomFollower") && sender.equals(channel_owner) ||
        		(command.equalsIgnoreCase("!chooseRandomFollower") && the_ops.contains(sender))) {
        		String winner = my_followers.chooseRandomWinner();
                sendMessage(channel, "THE RANDOM WINNER (Follower) is : " + winner);
        	
        }
        
        
        if (message.toLowerCase().contains("!addpoints") && sender.equals(channel_owner) ||
        		(message.toLowerCase().contains("!addpoints") && the_ops.contains(sender))) {
        	String user;
        	int amount;
        	if(scanner.hasNext()) {
        		user = scanner.next().toLowerCase();
        		System.out.println("GOT THE USER TO ADD POINTS TO: " + user);
            	if(scanner.hasNextInt()) {
            		amount = scanner.nextInt();
            		System.out.println("GOT THE AMOUNT TO ADD: " + amount);
            		my_botUsers.incrementTankerPoints(user, amount);
                    sendMessage(channel, sender + ": " + amount + " " + my_points_name + " added to " + user);
                	
            	} else {
                    sendMessage(channel, sender + ": Missing <amount> argument. Proper Command:  !addpoints <name> <amount>");
            	}
        	} else {
                sendMessage(channel, sender + ": Missing <name> and <amount> arguments. Proper Command:  !addpoints <name> <amount>");
        	}
        }

        if (message.toLowerCase().contains("!subpoints") && sender.equals(channel_owner) ||
        		(message.toLowerCase().contains("!subpoints") && the_ops.contains(sender))) {
        	String user;
        	int amount;
        	if(scanner.hasNext()) {
        		user = scanner.next().toLowerCase();
        		System.out.println("GOT THE USER TO SUBTRACT POINTS FROM: " + user);
            	if(scanner.hasNextInt()) {
            		amount = scanner.nextInt();
            		System.out.println("GOT THE AMOUNT TO SUBTRACT: " + amount);
            		my_botUsers.decrementTankerPoints(user, amount);
                    sendMessage(channel, sender + ": " + amount + " " + my_points_name + " removed from " + user);
                	
            	} else {
                    sendMessage(channel, sender + ": Missing <amount> argument. Proper Command:  !subpoints <name> <amount>");
            	}
        	} else {
                sendMessage(channel, sender + ": Missing <name> and <amount> arguments. Proper Command:  !subpoints <name> <amount>");
        	}
        }
        
        if (message.toLowerCase().contains("!addpointall") && sender.equals(channel_owner) ||
        		(message.toLowerCase().contains("!addpointall") && the_ops.contains(sender))) {
        	int amount;
        	String current_users = "";
            	if(scanner.hasNextInt()) {
            		amount = scanner.nextInt();
            		System.out.println("GOT THE AMOUNT TO ADD: " + amount);
                	for(int i = 0; i < my_users.length; i++ ){
                		String this_user = my_users[i].getNick();
                		my_botUsers.incrementTankerPoints(this_user, amount);
                		if(current_users.length() < 2) {
                			current_users = this_user;
                		} else {
                			
                		}
                		current_users = current_users + ", " + this_user;
                	}
                    sendMessage(channel, sender + ": " + amount + " " + my_points_name + " added to" + current_users);
                	
            	} else {
                    sendMessage(channel, sender + ": Missing <amount> argument. Proper Command:  !subpoints <name> <amount>");
            	}

        }
        
        if (command.equalsIgnoreCase("!time")) {
            String time = new java.util.Date().toString();
            sendMessage(channel, sender + ": The time is now " + time);
        }
        
        if (command.equalsIgnoreCase("!startAuction") && sender.equals(channel_owner) ||
        		(command.equalsIgnoreCase("!startAuction") && the_ops.contains(sender))) {
        	if (!auctionOn) {
           		auctionOn = true;
                sendMessage(channel, "The Auction has started! Type !bid <amount>  to enter the auction. You must have greater then or equal to the bid amount or your bid will be ignored.");    	
        	} else {
        		sendMessage(channel, "The Auction is already running.");
        	}
        }
        
        
        if (command.equalsIgnoreCase("!endAuction") && sender.equals(channel_owner) ||
        		(command.equalsIgnoreCase("!endAuction") && the_ops.contains(sender))) {
        	if(auctionOn) {
        		auctionOn = false;
        		if(auctionHighBidAmount > 0 && !auctionHighBidder.equals("")){
                    sendMessage(channel, "The Auction has ended! The WINNER of the auction is " + auctionHighBidder + " with a high bid of " + auctionHighBidAmount); 
        			my_botUsers.decrementTankerPoints(auctionHighBidder, auctionHighBidAmount);
                    auctionHighBidAmount = 0;
                    auctionHighBidder = "";
        		}
        	} else {
                sendMessage(channel, "There is no auction currently active."); 
        	}
         }
        
        if (command.equalsIgnoreCase("!highBidder") && sender.equals(channel_owner) ||
        		(command.equalsIgnoreCase("!highBidder") && the_ops.contains(sender))) {
        	if(auctionOn) {
                sendMessage(channel, "CURRENT HIGH BIDDER : " + auctionHighBidder + "with a bid of " + auctionHighBidAmount + " " + my_points_name + "!"); 
        	}
         }

        if (message.toLowerCase().contains("!bid") && auctionOn) {
        	int bidAmount;
        	if(scanner.hasNextInt()) {
        		bidAmount = scanner.nextInt();
        		System.out.println("GOT THE AUCTION BIDDER: " + sender + " and bid amount of " + bidAmount);
        		int bidderTankerPoints = my_botUsers.getMyCurrentPoints(sender.toLowerCase());
            	if(bidderTankerPoints >= bidAmount && bidAmount > auctionHighBidAmount) {
            		auctionHighBidder = sender;
            		auctionHighBidAmount = bidAmount;
            		sendMessage(channel,"NEW HIGH BIDDER IS: " + sender + " with bid amount of " + bidAmount + " " + my_points_name + "!");

            	} else {
            		System.out.println("Invalid BID");
            	}
        	} else {
                sendMessage(channel, sender + ": Missing <amount> arguments. Proper Command:  !bid <amount>");
        	}
        }
        
        if (command.equalsIgnoreCase("!keyword") && giveawayOn) {
            sendMessage(channel, sender + ":  The keyword for the giveaway is: [  " + my_keyword + "  ]   Type this keyword into the chat to be entered into the giveaway.");
        }
        
        if (command.equalsIgnoreCase("!giveaway")) {
        	if(giveawayOn) {
                sendMessage(channel, sender + ": Its happening right now, type the keyword [   " + my_keyword + "   ] to be entered into the giveaway!");
        	} else {
                sendMessage(channel, sender + ": There is no event currently active.");
        	}

        }  
        
        if (command.equalsIgnoreCase("!" + my_bot_name) && sender.equals(channel_owner) || 
        		command.equalsIgnoreCase("!" + my_bot_name) && the_ops.contains(sender) ) {
            sendMessage(channel, "Commands : !stopstream, !startstream, !setkeyword < keyword> , !removekeyword, !time, !keyword, !giveaway, !" + my_bot_name + ", !" + my_points_name + ", " +
            		" !subpoints < user > < points >, !addpoints < user > < points >, !emptyAllPoints, !checkpoints <username>, !addpointall <amount>, !lottoOn, !lottoOff, !buyTicket");
        }

        
        if (command.equalsIgnoreCase(my_keyword) && giveawayOn) {
        	if(!eventKeywordPeople.contains(sender.toLowerCase())) { //Only adds new users.
        		eventKeywordPeople.add(sender.toLowerCase());
            	my_keywordEntrys.addUser(sender);
        	}
        }
        
        if (command.equalsIgnoreCase("!" + my_points_name)) {
        	if(!tankerPointsQue.contains(sender.toLowerCase())) { //Only adds new users.
        		tankerPointsQue.add(sender.toLowerCase());
        		int botUserPoints = my_botUsers.getMyCurrentPoints(sender.toLowerCase());
            	my_userTankerPoints.addUser(sender, botUserPoints);
            	if(botUserPoints == 0){
        			sendMessage(my_channel, "Notice : " + sender + ", you are either a new viewer, or the system has not updated your points yet. Please wait a few minutes and try again.");

            	}

        	}
        }
        
        if (message.toLowerCase().contains("what are " + my_points_name) || message.toLowerCase().contains("what is " + my_points_name)) {
            sendMessage(channel, sender + ": " + my_points_name.toUpperCase() + " are accumulated while you are on watching the active stream on this channel. You will receive 1 point every 5 minutes of watching. You can use these points for certain events we hold.");
        }
        if (command.equalsIgnoreCase("krashnburnz")) {
            sendMessage(channel, sender + ": Creater of this bot. email --> krashnburnz@yahoo.com");
        }
        
        // BEGIN LOTTERY SYSTEM
        if (command.equalsIgnoreCase("!lottoOn") && sender.equals(channel_owner) ||
        		(command.equalsIgnoreCase("!lottoOn") && the_ops.contains(sender))) {
        	if (!lottoOn) {
        		lottoOn = true;
        		my_lottoAdvert.setLottoOn(true);
                sendMessage(channel, "The Lottery System has been started! Type !buyticket to enter the lottery for " + my_lottery_cost + " " + my_points_name + ". You must have greater then or equal to the ticket price or your entry will be ignored.");    	
        	} else {
        		sendMessage(channel, "The LotterySystem is already active.");
        	}
        }
        
        if (command.equalsIgnoreCase("!lottooff") && sender.equals(channel_owner) ||
        		(command.equalsIgnoreCase("!lottooff") && the_ops.contains(sender))) {
        	if (lottoOn) {
        		lottoOn = false;
        		my_lottoAdvert.setLottoOn(false);
                sendMessage(channel, "The Lottery System has been shut down!");    	
        	} else {
        		sendMessage(channel, "The Lottery System is already de-activated.");
        	}
        
        }
        if (command.equalsIgnoreCase("!buyticket") && lottoOn) {
        	if(my_botUsers.getMyCurrentPoints(sender.toLowerCase()) >= my_lottery_cost && !lottoPeople.contains(sender.toLowerCase())) {
        		my_botUsers.decrementTankerPoints(sender, my_lottery_cost);
            	lottoPeople.add(sender);
            	my_lottoSystem.addUser(sender);
            	my_lottoWinner.addUser(sender);
        	}

        }
        // END OF LOTTERY SYSTEM
    }
    
    public void onUserList(String channel, User[] users) {
        my_users = users;
        my_botUsers.setCurrentUsers(my_users);
    }

	@Override
	public void update(Observable arg0, Object arg1) {
		if(arg0 instanceof MyBotUserPoints) {
            String time = new java.util.Date().toString();
			int i;
			org.jibble.pircbot.User[] temp_users = getUsers(my_channel);
			User[] newUserArray = new User[temp_users.length];
			for(i = 0; i < temp_users.length; i++) {
				newUserArray[i] = new User(temp_users[i].getPrefix(), temp_users[i].getNick());
			}
	        my_users = newUserArray;
	    	my_botUsers.setCurrentUsers(my_users);
	    	if(this.currentlyStreaming && this.isConnected()) {
	    		sendMessage(my_channel, "Currently accumulating points. to check your points type !" + my_points_name);
	    	} else {
	    		sendMessage(my_channel, "Not currently accumulating points. to check your points type !" + my_points_name);
	    	}

		}
		if(arg0 instanceof MyKeywordEntry) {
			sendMessage(my_channel, ("The following users have been entered into the giveaway: " + my_keywordEntrys.getUsers()));
			my_keywordEntrys.emptyUsers();
		}
		if(arg0 instanceof MyTankerPoints) {
            sendMessage(my_channel, "Current User Points: " + my_userTankerPoints.getUsers());
            my_userTankerPoints.emptyUsers();
            tankerPointsQue.clear();
		}
		
		if(arg0 instanceof MyLottoSystem) {
            sendMessage(my_channel, "Users entered into Lottery: " + my_lottoSystem.getUsers());
        	my_lottoSystem.emptyUsers();
		}
		
		if(arg0 instanceof MyLotteryAdvert) {
			lotto_time_counter += (my_lottery_timer / 5);
            sendMessage(my_channel, "Lottery is currently active. To purchase a ticket for " + my_lottery_cost + " points, type !buyticket. The lottery will end in " + ((my_lottery_timer - lotto_time_counter) / 60000) + " minutes.");
		}
		
		if(arg0 instanceof MyLotteryWinner) {
			String winner = my_lottoWinner.getWinner();
			int amount = lottoPeople.size() * my_lottery_cost;
            sendMessage(my_channel, "The Lottery winner is " + winner + " , winning a total of " + amount + " points!");
    		my_botUsers.incrementTankerPoints(winner, amount);
        	my_lottoSystem.emptyUsers();
            my_lottoWinner.emptyUsers();
        	lottoPeople.clear();
        	lotto_time_counter = 0;
        	winner = "reset";
		}
		
		if(arg0 instanceof MyConnected) {
			if(!this.isConnected()) {
				try {
					this.reconnect();
					try {
						wait(10000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
			        this.joinChannel(my_channel);
				} catch (NickAlreadyInUseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IrcException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	
	@Override
    protected synchronized void onDisconnect() {
    	try {
			this.reconnect();
			try {
				wait(10000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	        this.joinChannel(my_channel);
		} catch (NickAlreadyInUseException e) {
			e.printStackTrace();
			System.out.println("Tried to reconnect, but Nick already in use!");
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("Tried to reconnect, but The PircBot is already connected to an IRC server.  Disconnect first.");
	       
		} catch (IrcException e) {
			System.out.println("Tried to reconnect, but could not log into the IRC server");
			e.printStackTrace();
		}
	}
}
