package Model;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;
import java.util.Scanner;

import javax.swing.JOptionPane;

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
	
	private String my_lotto_cost;
	
	private ArrayList<String> the_ops;
	
	private ArrayList<String> the_devs;
	
	private String my_channel = ""; 
	
	private String my_message;
	
	private int my_lottery_timer;
	
	private List<String> eventKeywordPeople = new LinkedList<String> ();

	private List<String> lottoPeople = new ArrayList<String> ();
	
	private List<String> tankerPointsQue = new LinkedList<String> ();
	
	private User[] my_users;
	
	private MyBotUserPoints my_botUsers;
	
	private MyKeywordEntry my_keywordEntrys;
	
	private MyUpdateUsers my_updateUsers;
	
	private MyTankerPoints my_userTankerPoints;
	
	private MyLottoSystem my_lottoSystem;
	
	private MyLotteryWinner my_lottoWinner;
	
	private MyLotteryAdvert my_lottoAdvert;
	
	private MyFollowers my_followers;
	
	private MyConnected my_connected;
	
	private int auctionHighBidAmount = 0;
	
	private String auctionHighBidder = "";
	
	private boolean wantingToDisconnect = false;
	
	private int advertTimer;
	
	private int advertCounter;
	
	private boolean isInitialAdvert = true;
	
	private int my_auction_incrementAmount;
	
	private int my_auction_Incrementbidwarnings = 0;
	
	private int my_auction_Pointbidwarnings = 0;
	
	private int my_auction_SpamWarning = 0;
	
	Map<String, Long> BiddersMap;
	
	private boolean allOpsUseCommands = false; // this does not include adding/removing points
	
	private ArrayList<String> the_adverts;
	
	private MyVoteSystem vote_system;
	
	private MyWagerSystem wager_system;
	
	private boolean isVoteActive = false;
	
	private boolean isWagerActive = false;
	
	ArrayList<String> theoptions = new ArrayList<String>();
	
	ArrayList<String> theWagerOptions = new ArrayList<String>();
	
	ArrayList<String> placedWages = new ArrayList<String>();
	


    public MyBot(String name, boolean lotteryEnabled, boolean accumulateOnStartUp, ArrayList<String> ops, String the_owner, String the_pointsname, int the_lotto_cost, 
    		int the_lottery_timer, String channel, int advert_timer) {
    	
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
        this.advertTimer = advert_timer;
        this.advertCounter = advert_timer;
        my_auction_incrementAmount = 1000;
        lotto_time_counter = 0;
        BiddersMap = new HashMap<String, Long>();
        the_devs = new ArrayList<String>();
        the_devs.add("krashnburnz");
        the_adverts = new ArrayList<String>();
        placedWages = new ArrayList<String>();
        
        my_botUsers = new MyBotUserPoints(my_channel, my_users);
    	Thread pointAdder_t = new Thread(my_botUsers);
    	my_botUsers.addObserver(this);
    	pointAdder_t.start();
        checkIfStreamingByDefualt(accumulateOnStartUp);
        
    	my_keywordEntrys = new MyKeywordEntry();
    	Thread KeywordEntrys_t = new Thread(my_keywordEntrys);
    	my_keywordEntrys.addObserver(this);
    	KeywordEntrys_t.start();
    	
    	my_updateUsers = new MyUpdateUsers(my_channel, this);
    	Thread updateUsers_t = new Thread(my_updateUsers);
    	my_updateUsers.addObserver(this);
    	updateUsers_t.start();
    	
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
		Scanner scanner = new Scanner(my_message);
    	if(scanner.hasNext()) {
        	command = scanner.next();	
    	}

        if (command.equalsIgnoreCase("!setkeyword") && (sender.equals(channel_owner) || the_devs.contains(sender) || (the_ops.contains(sender) && allOpsUseCommands))) {
        	if(scanner.hasNext()) {
        		my_keyword = scanner.next();
            	giveawayOn = true;
                sendMessage(channel, sender + ": The giveaway keyword " + my_keyword  +" has been set. The giveaway is now active.");
        	} else {
                sendMessage(channel, sender + ": Lack of arguments to set keyword and start event. Proper format: !setkeyword <keyword>");
        	}
        }
        
        else if (command.equalsIgnoreCase("!checkpoints") && (sender.equals(channel_owner) || the_devs.contains(sender) || (the_ops.contains(sender) && allOpsUseCommands))) {
        	String user;
        	if(scanner.hasNext()) {
        		user = scanner.next();
        		my_botUsers.updateRankedList();
        		int rank = my_botUsers.getRank(user);
                sendMessage(channel, user +"'s current " + my_points_name + " are: " + my_botUsers.getMyCurrentPoints(user) + " with a rank of "+ rank + " out of " + my_botUsers.getTotalUserCount());
        	} else {
                sendMessage(channel, sender + ": Lack of arguments to get user's points.  Proper format: !checkpoints <username>");
        	}
        }
        
        else if (command.equalsIgnoreCase("!removekeyword") && (sender.equals(channel_owner) || the_devs.contains(sender) || (the_ops.contains(sender) && allOpsUseCommands))) {
        		my_keyword = "";
            	giveawayOn = false;
            	eventKeywordPeople.clear();
                sendMessage(channel, sender + ": The giveaway keyword has been removed and the giveaway is now complete.");        	
        }
        
        else if (command.equalsIgnoreCase("!startstream") && (sender.equals(channel_owner) || the_devs.contains(sender) || (the_ops.contains(sender) && allOpsUseCommands))) {
        		if(currentlyStreaming) {
        			sendMessage(channel, sender + ": Stream ALREADY ACTIVE and " + my_points_name + " have been accumulating! Do NOT forget to stop the Bot using the !stopstream command.  ><");

        		} else {
            		currentlyStreaming = true;
            		my_botUsers.setActivelyStreaming(currentlyStreaming);
                    sendMessage(channel, sender + ": Stream now Active--> "+ my_points_name + " are NOW being accumulated.");
        		}
        }
        
        else if (command.equalsIgnoreCase("!stopstream") && (sender.equals(channel_owner) || the_devs.contains(sender) || (the_ops.contains(sender) && allOpsUseCommands))) {
        		if(currentlyStreaming) {
            		currentlyStreaming = false;
            		my_botUsers.setActivelyStreaming(currentlyStreaming);
                    sendMessage(channel, sender + ": Stream now Inactive--> " + my_points_name + " are NOT being accumulated.");
        		} else { 
                    sendMessage(channel, sender + ": Stream IS ALREADY INACTIVE and " + my_points_name + " have not been accumulating.");

        		}

        	
        }
        
        else if (command.equalsIgnoreCase("!emptyAllPoints") && sender.equals(channel_owner) ||
        		(command.equalsIgnoreCase("!emptyAllPoints") && the_devs.contains(sender))) {
        		my_botUsers.clearAllPoints();
                sendMessage(channel, sender + ": All " + my_points_name + " have been reset.");
                my_botUsers.updateRankedList();
        	
        }
        
        else if (command.equalsIgnoreCase("!chooseRandomFollower") && (sender.equals(channel_owner) || the_devs.contains(sender) || (the_ops.contains(sender) && allOpsUseCommands))) {
        		String winner = my_followers.chooseRandomWinner();
                sendMessage(channel, "THE RANDOM WINNER (Follower) is : " + winner);
        	
        }
                
        else if (command.equalsIgnoreCase("!addpoints") && sender.equals(channel_owner) ||
        		(command.equalsIgnoreCase("!addpoints") && the_devs.contains(sender))) {
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
                    my_botUsers.updateRankedList();
                	
            	} else {
                    sendMessage(channel, sender + ": Missing <amount> argument. Proper Command:  !addpoints <name> <amount>");
            	}
        	} else {
                sendMessage(channel, sender + ": Missing <name> and <amount> arguments. Proper Command:  !addpoints <name> <amount>");
        	}
        }

        else if (command.equalsIgnoreCase("!rank")) {
        	my_botUsers.updateRankedList();
        	int rank = my_botUsers.getRank(sender);
        	if(rank == 0){
    			sendMessage(my_channel, sender + " : Sorry but you do not exist in the ranking list yet. Please try again in 5 minutes!");
        	} else if(rank == -1) {
    			sendMessage(my_channel, sender + " : The rank list is null. Please try again later!");
        	} else {
        		sendMessage(my_channel, sender + " : You are currently rank " + rank + " out of " + my_botUsers.getTotalUserCount());           	
        	}
            	
        }
        
        else if (command.equalsIgnoreCase("!subpoints") && sender.equals(channel_owner) ||
        		(command.equalsIgnoreCase("!subpoints") && the_devs.contains(sender))) {
        	String user;
        	int amount;
        	if(scanner.hasNext()) {
        		user = scanner.next().toLowerCase();
        		System.out.println("GOT THE USER TO SUBTRACT POINTS FROM: " + user);
            	if(scanner.hasNextInt()) {
            		amount = scanner.nextInt();
            		System.out.println("GOT THE AMOUNT TO SUBTRACT: " + amount);
            		int currentUserPoints = my_botUsers.getMyCurrentPoints(user);
            		if( currentUserPoints < amount) {
                        sendMessage(channel, sender + ": " + user + " only has " + currentUserPoints + " " + my_points_name + ". Please subtract this amount instead." );
            		} else {
                		my_botUsers.decrementTankerPoints(user, amount);
                        sendMessage(channel, sender + ": " + amount + " " + my_points_name + " removed from " + user);
                        my_botUsers.updateRankedList();
            		}

                	
            	} else {
                    sendMessage(channel, sender + ": Missing <amount> argument. Proper Command:  !subpoints <name> <amount>");
            	}
        	} else {
                sendMessage(channel, sender + ": Missing <name> and <amount> arguments. Proper Command:  !subpoints <name> <amount>");
        	}
        }
        
        else if (command.equalsIgnoreCase("!addpointall") && sender.equals(channel_owner) ||
        		(command.equalsIgnoreCase("!addpointall") && the_devs.contains(sender))) {
        	int amount;
            	if(scanner.hasNextInt()) {
            		amount = scanner.nextInt();
            		System.out.println("GOT THE AMOUNT TO ADD: " + amount);
                	for(int i = 0; i < my_users.length; i++ ){
                		String this_user = my_users[i].getNick();
                		my_botUsers.incrementTankerPoints(this_user, amount);
                	}
                	my_botUsers.updateRankedList();
                    sendMessage(channel, sender + ": " + amount + " " + my_points_name + " added to every viewer currently watching!");
                	
            	} else {
                    sendMessage(channel, sender + ": Missing <amount> argument. Proper Command:  !addpointall <amount>");
            	}

        }
        
        else if (command.equalsIgnoreCase("!time")) {
            String time = new java.util.Date().toString();
            sendMessage(channel, sender + ": The time is now " + time);
        }
        
        else if (command.equalsIgnoreCase("!startAuction") && (sender.equals(channel_owner) || the_devs.contains(sender) || (the_ops.contains(sender) && allOpsUseCommands))) {
        	if (!auctionOn) {
            	if(scanner.hasNextInt()) {
            		my_auction_incrementAmount = scanner.nextInt();
           		auctionOn = true;
                sendMessage(channel, "The Auction has started! Type !bid <amount>  to enter the auction. You must have greater then or equal to the bid amount or your bid will be ignored. Max bid increments are set to " + my_auction_incrementAmount + " " + my_points_name);
            	} else {
                    sendMessage(channel, sender + ": Missing <amount> arguments. Proper Command:  !startAuction <max_bid_increment_amount>");
            	}
        	} else {
        		sendMessage(channel, "The Auction is already running.");
        	}
        }
        
        
        else if (command.equalsIgnoreCase("!endAuction") && (sender.equals(channel_owner) || the_devs.contains(sender) || (the_ops.contains(sender) && allOpsUseCommands))) {
        	if(auctionOn) {
        		auctionOn = false;
        		if(auctionHighBidAmount > 0 && !auctionHighBidder.equals("")){
                    sendMessage(channel, "The Auction has ended! The WINNER of the auction is " + auctionHighBidder + " with a high bid of " + auctionHighBidAmount); 
        			my_botUsers.decrementTankerPoints(auctionHighBidder, auctionHighBidAmount);
                    auctionHighBidAmount = 0;
                    auctionHighBidder = "";
                    BiddersMap.clear();
        		}
        	} else {
                sendMessage(channel, "There is no auction currently active."); 
        	}
         }
        
        else if (command.equalsIgnoreCase("!highBidder") && (sender.equals(channel_owner) || the_devs.contains(sender) || (the_ops.contains(sender) && allOpsUseCommands))) {
        	if(auctionOn) {
                sendMessage(channel, "CURRENT HIGH BIDDER : " + auctionHighBidder + " with a bid of " + auctionHighBidAmount + " " + my_points_name + "!"); 
        	}
         }

        else if (command.equalsIgnoreCase("!bid") && auctionOn) {
        	int IncrementBuffer;
        	int BidBuffer;
        	int roomsize = my_users.length;
        	if(roomsize < 50) {
            	//default settings if roomsize < 50
            	IncrementBuffer = 10;
            	BidBuffer = 20;
        	} else { // every 50 viewers, the buffers increase depending on the size of the room increase
        		// This is to stop the bot from spamming while people bid.
            	IncrementBuffer = (roomsize / 50) * 10;
            	BidBuffer = (roomsize / 50) * 20;
        	}
        	int bidAmount;
        	if(scanner.hasNextInt()) {
        		bidAmount = scanner.nextInt();
        		int bidderPoints = my_botUsers.getMyCurrentPoints(sender.toLowerCase());
            	if(bidderPoints >= bidAmount && bidAmount > auctionHighBidAmount && !auctionHighBidder.equalsIgnoreCase(sender)) {
            		if(bidAmount <= my_auction_incrementAmount + auctionHighBidAmount ) { //doesn't surpass the increment limit
                    	if(BiddersMap.get(sender) == null) { // null must be a new bidder
                    		auctionHighBidder = sender;
                    		auctionHighBidAmount = bidAmount;
                    		BiddersMap.put(sender, System.currentTimeMillis());
                    		sendMessage(channel,"NEW HIGH BIDDER IS: " + sender + " with bid amount of " + bidAmount + " " + my_points_name + "!");
                    	} else { //not a new bidder, lets check to be sure its been 10 seconds since the last bid
                    		if(System.currentTimeMillis() - BiddersMap.get(sender) > 10000) {
                    			Long currentTime = System.currentTimeMillis();
                    			System.out.println(currentTime.toString());
                        		auctionHighBidder = sender;
                        		auctionHighBidAmount = bidAmount;
                        		BiddersMap.remove(sender);
                        		BiddersMap.put(sender, System.currentTimeMillis());
                        		sendMessage(channel,"NEW HIGH BIDDER IS: " + sender + " with bid amount of " + bidAmount + " " + my_points_name + "!");
                    		} else {
                    			my_auction_SpamWarning += 1;
                    		}
                    	}

            		} else { // Increment the message counter
                		my_auction_Incrementbidwarnings += 1;
                	}
            	} else {
            		my_auction_Pointbidwarnings += 1;
            	}
            	if(my_auction_Pointbidwarnings == BidBuffer) {
            		sendMessage(channel,"Please remember that you cannot bid more " + my_points_name + " then you currently have or the bot will ignore your bid!" );
            		my_auction_Pointbidwarnings = 0;
            	}
            	if(my_auction_Incrementbidwarnings == IncrementBuffer) {
            		sendMessage(channel,"Please remember that you cannot bid more then " + my_auction_incrementAmount + " " + my_points_name + " over the current high bid (Current Max allowed bid: " + (my_auction_incrementAmount + auctionHighBidAmount) + ")." );
            		my_auction_Incrementbidwarnings = 0;
            	} 
            	if(my_auction_SpamWarning == BidBuffer) {
            		sendMessage(channel,"Please remember that the system will only accept a bid from any one user every 10+ seconds. To reduce spam, anything more and the user's attempt will be ignored. Please do not spam bid!");
            		my_auction_SpamWarning = 0;
            	}
        	} else {
                sendMessage(channel, sender + ": Missing <amount> arguments. Proper Command:  !bid <amount>");
        	}
        }
        
        else if (command.equalsIgnoreCase("!keyword") && giveawayOn) {
            sendMessage(channel, sender + ":  The keyword for the giveaway is: [  " + my_keyword + "  ]   Type this keyword into the chat to be entered into the giveaway.");
        }
        
        else if (command.equalsIgnoreCase("!giveaway")) {
        	if(giveawayOn) {
                sendMessage(channel, sender + ": Its happening right now, type the keyword [   " + my_keyword + "   ] to be entered into the giveaway!");
        	} else {
                sendMessage(channel, sender + ": There is no event currently active.");
        	}

        }  
        
        else if (command.equalsIgnoreCase("!" + my_bot_name) && (sender.equals(channel_owner) || the_devs.contains(sender) || (the_ops.contains(sender) && allOpsUseCommands))) {
            sendMessage(channel, "Commands : !stopstream, !startstream, !setkeyword < keyword> , !removekeyword, !time, !keyword, !giveaway, !" + my_bot_name + ", !" + my_points_name + ", " +
            		" !subpoints < user > < points >, !addpoints < user > < points >, !emptyAllPoints, !checkpoints <username>, !addpointall <amount>, !lottoOn, !lottoOff, !buyTicket");
        }

        
        else if (command.equalsIgnoreCase(my_keyword) && giveawayOn) {
        	if(!eventKeywordPeople.contains(sender.toLowerCase())) { //Only adds new users.
        		eventKeywordPeople.add(sender.toLowerCase());
            	my_keywordEntrys.addUser(sender);
        	}
        }
        
        else if (command.equalsIgnoreCase("!" + my_points_name)) {
        	if(!tankerPointsQue.contains(sender.toLowerCase())) { //Only adds new users.
        		tankerPointsQue.add(sender.toLowerCase());
        		int botUserPoints = my_botUsers.getMyCurrentPoints(sender.toLowerCase());
            	my_userTankerPoints.addUser(sender, botUserPoints);
            	if(botUserPoints == 0){
        			sendMessage(my_channel, sender + " : You seem to be a new viewer to this channel! Welcome to the channel and we hope you enjoy your time here! Don't forget to click that follow button!");
            	} else if(botUserPoints == -1) {
        			sendMessage(my_channel, sender + " : You don't seem to exist in the current viewer list. Either Twitch is having issues currently or you just joined the channel. Please try again later.");
            	}
        	}
        }
        
        else if (message.toLowerCase().contains("what are " + my_points_name) || message.toLowerCase().contains("what is " + my_points_name)) {
            sendMessage(channel, sender + ": " + my_points_name.toUpperCase() + " are accumulated while you are on watching the active stream on this channel. You will receive 1 point every 5 minutes of watching. You can use these points for certain events we hold.");
        }
        
        else if (command.equalsIgnoreCase("!projecthome")) {
            sendMessage(channel, sender + ": Project page for this bot software: https://code.google.com/p/twitch-bot-krashnburnz/");
        }
        
        // BEGIN LOTTERY SYSTEM
        else if (command.equalsIgnoreCase("!lottoOn") && (sender.equals(channel_owner) || the_devs.contains(sender) || (the_ops.contains(sender) && allOpsUseCommands))) {
        	if(scanner.hasNext()) {
        		my_lottery_cost = Integer.parseInt(scanner.next());
                sendMessage(channel, sender + ": The cost per ticket has been set to " + my_lottery_cost + " " + my_points_name);
        	}
        	if (!lottoOn) {
        		lottoOn = true;
        		my_lottoAdvert.setLottoOn(true);
                sendMessage(channel, "The Lottery System has been started! Type !buyticket to enter the lottery for " + my_lottery_cost + " " + my_points_name + ". You must have greater then or equal to the ticket price or your entry will be ignored.");    	
        	} else {
        		sendMessage(channel, "The LotterySystem is already active.");
        	}
        }
        
        else if (command.equalsIgnoreCase("!lottooff") && (sender.equals(channel_owner) || the_devs.contains(sender) || (the_ops.contains(sender) && allOpsUseCommands))) {
        	if (lottoOn) {
        		lottoOn = false;
        		my_lottoAdvert.setLottoOn(false);
                sendMessage(channel, "The Lottery System has been shut down!");    	
        	} else {
        		sendMessage(channel, "The Lottery System is already de-activated.");
        	}
        
        }
        
        else if (command.equalsIgnoreCase("!pickwinnernow") && (sender.equals(channel_owner) || the_devs.contains(sender) || (the_ops.contains(sender) && allOpsUseCommands))) {
        	String winner = my_lottoWinner.getWinner();
    		int amount = lottoPeople.size() * my_lottery_cost;
            sendMessage(my_channel, "***WINNER WINNER CHICKEN DINNER*** The Lottery WINNER is " + winner + " , winning a total of " + amount + " points! Congrats!");
    		my_botUsers.incrementTankerPoints(winner, amount);
        	my_lottoSystem.emptyUsers();
            my_lottoWinner.emptyUsers();
        	lottoPeople.clear();
        	lotto_time_counter = 0;
        	if (lottoOn) {
        		lottoOn = false;
        		my_lottoAdvert.setLottoOn(false);
                sendMessage(my_channel, "The Lottery System has been shut down!");    	
        	} else {
        		sendMessage(my_channel, "The Lottery System is already de-activated.");
        	}
        	winner = "reset";
        }
        
    	
    	
        else if (command.equalsIgnoreCase("!buyticket") && lottoOn) {
        	if(my_botUsers.getMyCurrentPoints(sender.toLowerCase()) >= my_lottery_cost && !lottoPeople.contains(sender.toLowerCase())) {
        		my_botUsers.decrementTankerPoints(sender, my_lottery_cost);
            	lottoPeople.add(sender);
            	my_lottoSystem.addUser(sender);
            	my_lottoWinner.addUser(sender);
        	}

        }
        // END OF LOTTERY SYSTEM
        
        
      //------------------ START ENABLE /DISABLE MOD SYSTEM -------------------
        else if (command.equalsIgnoreCase("!modswatching") && (sender.equals(channel_owner) || the_devs.contains(sender) || (the_ops.contains(sender) && allOpsUseCommands))) {
    		String ops = "";
        	for(int f = 0; f < the_ops.size(); f++) {
        		if(f == the_ops.size() - 1) {
            		ops = ops + the_ops.get(f);
        		} else {
            		ops = ops + the_ops.get(f) + " , ";	
        		}        		
        	}
    		sendMessage(channel, "Mods currently in chat is " + the_ops.size() + ", the mods are: " + ops);
        }
        
        else if (command.equalsIgnoreCase("!enableModCommands") && sender.equals(channel_owner)) {
        	if(!allOpsUseCommands) {
        		sendMessage(channel, "Mods can now use bot commands. This will disable every time the bot is shut down.");
        		allOpsUseCommands = true;
        	}
        }
        
        else if (command.equalsIgnoreCase("!disableModCommands") && sender.equals(channel_owner)) {
        	if(allOpsUseCommands) {
        		sendMessage(channel, "Mod use of bot commands disabled.");
        		allOpsUseCommands = false;
        	}
        }
        
      //------------------ END ENABLE /DISABLE MOD SYSTEM -------------------
        
        
        
        
        //--------------- START VOTE SYSTEM ---------------
        
        else if (command.equalsIgnoreCase("!startvote") && (sender.equals(channel_owner) || the_devs.contains(sender) || (the_ops.contains(sender) && allOpsUseCommands))) {
        	String currentOption = "";
        	boolean isFirstWord = true;
        	if(!scanner.hasNext()) {
        		sendMessage(my_channel, sender + " : Missing arguments for options. Each option must be proceeded by a dash. Example: startvote -Yes -No -Maybe");
        	} else {
        		if(!isVoteActive) {
            		while(scanner.hasNext()){
                		String word = scanner.next();
                		if(isFirstWord) {
                			if(!word.contains("-")) {
                				sendMessage(my_channel, sender + " : Missing arguments for options. Each option must be proceeded by a dash. Example: startvote -Play Dayz all night -Play BF4 for a few hours");
                			} else {
                				currentOption = word.substring(1);
                				if(!scanner.hasNext()) {
                					theoptions.add(currentOption.toLowerCase());
                				}
                				isFirstWord = false;
                			}
                		} else {
                			if(!word.contains("-")) {
                				currentOption = currentOption + " " + word;
                				if(!scanner.hasNext()) {
                					theoptions.add(currentOption.toLowerCase());
                				}
                			} else {
                				theoptions.add(currentOption.toLowerCase());
                				currentOption = word.substring(1);
                				if(!scanner.hasNext()) {
                					theoptions.add(currentOption.toLowerCase());
                				}
                			}
                		}
                	}
                	if(!isVoteActive) {
                		if(theoptions.size() > 0) {
                			vote_system = new MyVoteSystem(theoptions);
                    		String longOptions = "";
                    		for(int i = 0; i < theoptions.size(); i++) {
                    			longOptions = longOptions+ "    " +  (i+1) +". " +theoptions.get(i);
                    		}
                    		sendMessage(my_channel, sender + " : The vote system has been activated with " + theoptions.size() + " options. The Options:  " +longOptions +". Please type !vote #  to cast your vote. Example:  !vote 1");
                    		isVoteActive = true;
                		}
                	} else {
                		sendMessage(my_channel, sender + " : There is already a vote currently active. Please end the current Vote before attempting to start a new one.");
                	}
        		} else {
        			sendMessage(my_channel, sender + " : There is already a vote currently active. Please end the current Vote before attempting to start a new one.");
        		}
        		
        	}
        	
        }
        
        else if (command.equalsIgnoreCase("!endvote") && (sender.equals(channel_owner) || the_devs.contains(sender) || (the_ops.contains(sender) && allOpsUseCommands))) {
        	if(isVoteActive) {
        		String theWinner = vote_system.endVote();
        		int theindex = 0;
        		if(theoptions.contains(theWinner)) {
        			theindex = theoptions.indexOf(theWinner);
        		}
        		sendMessage(my_channel, sender + " : The winning choice for the vote was #" + (theindex + 1) + " - " + theWinner + " , With a total of " + vote_system.getMax() + " votes!");
            	isVoteActive = false;
            	vote_system.clearMax();
            	theoptions.clear();
        	} else {
        		sendMessage(my_channel, sender + " : The vote system is not currently activated. Type !startvote <arg1> <arg2> ... <arg N>          where each arg must begin with a dash -   Example: !startvote -We like turtles -We hate turtles");
        	}
        	
        }
        
        else if (command.equalsIgnoreCase("!getvoteoptions")) {
        	if(isVoteActive) {
        		if(theoptions.size() > 0) {
            		String longOptions = "";
            		for(int i = 0; i < theoptions.size(); i++) {
            			longOptions = longOptions+ "    " +  (i+1) +". " +theoptions.get(i);
            		}
            		sendMessage(my_channel, sender + " : There are currently " + theoptions.size() + " options to vote for. The choices are:  " +longOptions );
        		}
        	} else {
        		sendMessage(my_channel, sender + " : The vote system is not active. Please try again later.");
        	}
        	
        }
        
        else if (command.equalsIgnoreCase("!vote")) {
        	if(isVoteActive) {
        		int vote;
            	int sizeOptions = theoptions.size();
            	if(scanner.hasNextInt()) {
            		vote = scanner.nextInt();
            		if(sizeOptions > 0 && vote <= sizeOptions) {
            			vote_system.addVote(vote, sender);
            		}
            	}
        	} else {
        		sendMessage(my_channel, sender + " : The vote system is not active. Please try again later.");
        	}
        	
        }
        
        else if (command.equalsIgnoreCase("!myvote")) {
        	if(isVoteActive) {
        		int chosen = vote_system.getMyChosenOption(sender);
            	sendMessage(my_channel, sender + " : You have chosen choice #" + chosen + " - " + theoptions.get(chosen-1));
        	} else {
        		sendMessage(my_channel, sender + " : The vote system is not active. Please try again later.");
        	}
        	
        }
      //--------------- END VOTE SYSTEM ---------------
        
        
        
        
        
        
        //------------------ START WAGER SYSTEM -------------------
        
        else if (command.equalsIgnoreCase("!startwager") && (sender.equals(channel_owner) || the_devs.contains(sender) || (the_ops.contains(sender) && allOpsUseCommands))) {
        	String currentOption = "";
        	boolean isFirstWord = true;
        	if(!scanner.hasNext()) {
        		sendMessage(my_channel, sender + " : Missing arguments for options. Each option must be proceeded by a dash. Example: !startwager -Win -Lose -Who cares");
        	} else {
        		if(!isWagerActive) {
            		while(scanner.hasNext()){
                		String word = scanner.next();
                		if(isFirstWord) {
                			if(!word.contains("-")) {
                				sendMessage(my_channel, sender + " : Missing arguments for options. Each option must be proceeded by a dash. Example: !startwager -He will die -He will live forever");
                			} else {
                				currentOption = word.substring(1);
                				if(!scanner.hasNext()) {
                					theWagerOptions.add(currentOption.toLowerCase());
                				}
                				isFirstWord = false;
                			}
                		} else {
                			if(!word.contains("-")) {
                				currentOption = currentOption + " " + word;
                				if(!scanner.hasNext()) {
                					theWagerOptions.add(currentOption.toLowerCase());
                				}
                			} else {
                				theWagerOptions.add(currentOption.toLowerCase());
                				currentOption = word.substring(1);
                				if(!scanner.hasNext()) {
                					theWagerOptions.add(currentOption.toLowerCase());
                				}
                			}
                		}
                	}
                	if(!isWagerActive) {   //If the wager system is inactive and there has been options set
                		if(theWagerOptions.size() > 0) {
                			placedWages.clear();
                			wager_system = new MyWagerSystem(theWagerOptions);
                    		String longOptions = "";
                    		for(int i = 0; i < theWagerOptions.size(); i++) {
                    			longOptions = longOptions+ "    " +  (i+1) +". " +theWagerOptions.get(i);
                    		}
                    		sendMessage(my_channel, sender + " : The wager system has been activated with " + theWagerOptions.size() + " options. The Options:  " +longOptions +". Winners receive 2 x their wager amount. Please type !wager <choice> <amount>  to place your bet. Example:  !wager 1 200");
                    		isWagerActive = true;
                		}
                	} else {
                		sendMessage(my_channel, sender + " : There is already a wager currently active. Please end the current wager before attempting to start a new one.");
                	}
        		} else {
        			sendMessage(my_channel, sender + " : There is already a wager currently active. Please end the current wager before attempting to start a new one.");
        		}
        		
        	}
        	
        }
        
        else if (command.equalsIgnoreCase("!wagerwinners") && (sender.equals(channel_owner) || the_devs.contains(sender) || (the_ops.contains(sender) && allOpsUseCommands))) {
        	if(!scanner.hasNextInt()) {
        		sendMessage(my_channel, sender + " : Missing arguments. You must specify the winning choice. Example: !wagerwinners 2         If your unsure what the choices were, type !wagerchoices");
        	} else {
        		int theIndex = scanner.nextInt();
        		if(isWagerActive) {
        			if(theIndex > 0 && theIndex <= theWagerOptions.size()) {
        				List<WagerUser> theWinners = wager_system.chooseWinner(theIndex);
        				int countWinners = 0;
        				for(int i = 0; i < theWinners.size(); i++) {
	            			WagerUser tempuser = theWinners.get(i);
	            			my_botUsers.incrementTankerPoints(tempuser.getBetterName(), (tempuser.getBetAmount()*2));
	            			countWinners++;
	            		}
        				sendMessage(my_channel, sender + " : The winning choice for the bet was #" + (theIndex) + " - " + theWagerOptions.get(theIndex-1) + " , With a total of " +wager_system.getTotalCount() + " wagers made and a total of " + countWinners + " winners! All winners received 2 x their bet amount!");
	            		
        				isWagerActive = false;
	                	theWagerOptions.clear();		
        	            		
        			} else {
        				sendMessage(my_channel, sender + " : The argument is out of the range of the choices. If your unsure what the choices were, type !wagerchoices");
                    	
        			}
            		
            	} else {
            		sendMessage(my_channel, sender + " : The wager system is not currently activated. Type !startwager <arg1> <arg2> ... <arg N>          where each arg must begin with a dash -   Example: !startwager -We will live forever -We will die a horrible death");
            	}
        	}  	
        }
        
        else if (command.equalsIgnoreCase("!wagerchoices")) {
        	if(theWagerOptions.size() > 0 && isWagerActive) {
        		String longOptions = "";
        		for(int i = 0; i < theWagerOptions.size(); i++) {
        			longOptions = longOptions+ "    " +  (i+1) +". " +theWagerOptions.get(i);
        		}
        		sendMessage(my_channel, sender + " : There are currently " + theWagerOptions.size() + " options to place a wager on. The choices are:  " +longOptions );
    		}
        }
        
        else if (command.equalsIgnoreCase("!wager")) {
        	int choice = 0;
        	int amount = 0;
        	boolean shouldSubPoints;
        	if (isWagerActive) {
        		if(!scanner.hasNextInt()) { //NO NEXT INT
            		sendMessage(my_channel, sender + " : Missing arguments. You must specify the choice your placing the wager on. Example: !wager 2 200         If your unsure what the choices were, type !wagerchoices");
            	} else {
        			choice = scanner.nextInt();
            		if(!scanner.hasNextInt()) { //NO NEXT INT
            			sendMessage(my_channel, sender + " : Missing arguments. You must specify the wager amount for the choice your placing the wager. Example: !wager 2 200         If your unsure what the choices were, type !wagerchoices");
                    } else { //both arguments check out, lets carry on
                    	amount = scanner.nextInt();
                		if(my_botUsers.getMyCurrentPoints(sender) >= amount) { //Check to be sure they have the points to bid with
                			if(choice > 0 && choice <= theWagerOptions.size()) {
                				shouldSubPoints = wager_system.addBet(sender.toLowerCase(), choice, amount);
                				if(shouldSubPoints) { //If wager added successfully, lets decrement the points now.
                					placedWages.add(sender);
                					my_botUsers.decrementTankerPoints(sender.toLowerCase(), amount);
                				}
                			}
                			
                		} else {
                			sendMessage(my_channel, sender + " : You do not have enough points to make that wager. Please check your current points before trying again.");
                		}
                	}
            	}
        	} else {
        		sendMessage(my_channel, sender + " : the wager system is not activated at this time. Please try again later.");
                
        	}
        	
        }
        
        else if (command.equalsIgnoreCase("!mywager")) {
        	if(isWagerActive) {
        		int chosen = wager_system.getMyChosenOption(sender);
            	sendMessage(my_channel, sender + " : You have chosen to place a wager on choice #" + chosen + " - " + theWagerOptions.get(chosen-1) + ", for the amount of " + wager_system.getMyBetAmount(sender.toLowerCase()) + " " + my_points_name);
        	} else {
        		sendMessage(my_channel, sender + " : The wager system is not activated at this time. Please try again later.");
        	}
        	
        }
        
        else if (command.equalsIgnoreCase("!closewager") && (sender.equals(channel_owner) || the_devs.contains(sender) || (the_ops.contains(sender) && allOpsUseCommands))) {
        	if(isWagerActive) {
        		wager_system.canWager(false);
        		sendMessage(my_channel, sender + " : The ability to place wagers has been closed. No more wagers will be accepted!");
        	} else {
        		sendMessage(my_channel, sender + " :The wager system is not activated at this time. Please try again later.");
        	} 	
        }
        
        else if (command.equalsIgnoreCase("!openwager") && (sender.equals(channel_owner) || the_devs.contains(sender) || (the_ops.contains(sender) && allOpsUseCommands))) {
        	if(isWagerActive) {
        		wager_system.canWager(true);
        		sendMessage(my_channel, sender + " : Wagers will now be accepted again!");
        	} else {
        		sendMessage(my_channel, sender + " : The wager system is not activated at this time. Please try again later.");
        	} 	
        }
      //------------------END WAGER SYSTEM-------------------
        
        scanner.close();
    }
    
    public void onUserList(String channel, User[] users) {
        my_users = users;
        my_botUsers.setCurrentUsers(my_users);
    }

	@Override
    protected void onUserMode(String targetNick, String sourceNick, String sourceLogin, String sourceHostname, String mode) {
		if(mode.contains("+o")) {
			Scanner modeScanner = new Scanner(mode);
			String prefix = "";
			String user = "";
			if(modeScanner.hasNext()) {
				modeScanner.next(); //scan the channel name
				if(modeScanner.hasNext()) {
					prefix = modeScanner.next();
					if(prefix.equalsIgnoreCase("+o")) { //isOp
						if(modeScanner.hasNext()) {
							user = modeScanner.next();
							if(!the_ops.contains(user)) { //If op doesnt exists already, lets add
								the_ops.add(user);
							}
						}
					}
				}
			}
			modeScanner.close();
		}
    }
    
	@Override
    protected void onOp(String channel, String sourceNick, String sourceLogin, String sourceHostname, String recipient) {
		System.out.println("**** FOUND AN OP: Channel = "  + channel + ", SourceNick = " + sourceNick + ", SourceLogin = " +  sourceLogin + 
								",  SourceHost = " + sourceHostname + ", Recipient = " + recipient);
		sendMessage(channel, "MOD ADDED!");
		
	}

	@Override
	public void update(Observable arg0, Object arg1) {
		
		//MyBotUserPoints Observer: this Advertises that the points are being accumulated
		if(arg0 instanceof MyBotUserPoints) {
			int i;
			org.jibble.pircbot.User[] temp_users = getUsers(my_channel);
			User[] newUserArray = new User[temp_users.length];
			for(i = 0; i < temp_users.length; i++) {
				newUserArray[i] = new User(temp_users[i].getPrefix(), temp_users[i].getNick());
			}
	        my_users = newUserArray;
	    	my_botUsers.setCurrentUsers(my_users);
	    	if(this.currentlyStreaming && this.isConnected()) {//check to see if connected, currently streaming, and if its time to advertise accumulation of points
	    		if(advertCounter == 0 || (isInitialAdvert && advertCounter != -1)) {
	    			System.out.println("**ADVERT COUNTER IS 0, say message**");
		    		sendMessage(my_channel, "Currently accumulating points. to check your points type !" + my_points_name);
		    		advertCounter = advertTimer;
		    		isInitialAdvert = false;
	    		} else if(advertCounter == -1) {
	    			System.out.println("**ADVERT COUNTER IS -1, never say message**");
	    			//do nothing, the advert is disabled
	    		}  		
	    		else {
	    			System.out.println("**ADVERT COUNTER IS SUBTRACTING 5**");
	    			advertCounter = advertCounter - 5;//subtract 5 since this update triggers every 5 mins
	    		}
	    	} else {
	    		sendMessage(my_channel, "Not currently accumulating points. to check your points type !" + my_points_name);
	    	}

		}
		
		//MyKeywordEntry Observer: Displays users entered into Giveaway, then emptys the String, to start again
		else if(arg0 instanceof MyKeywordEntry) {
			sendMessage(my_channel, ("The following users have been entered into the giveaway: " + my_keywordEntrys.getUsers()));
			my_keywordEntrys.emptyUsers();
		}
		
		//MyUpdateUsers Observer: Updates the actual my_users array of viewers actively watching the stream
		else if(arg0 instanceof MyUpdateUsers) {
	        my_users = (User[]) arg1;
	        my_botUsers.setCurrentUsers((User[]) arg1);
		}
		
		//MyTankerpoints Observer: Displays the user's points that call the command to check points
		else if(arg0 instanceof MyTankerPoints) {
            sendMessage(my_channel, "Current User Points: " + my_userTankerPoints.getUsers());
            my_userTankerPoints.emptyUsers();
            tankerPointsQue.clear();
            
            if(isWagerActive) {
            	if(!placedWages.isEmpty()) {
            		String entries = "";
                	for(int i = 0; i < placedWages.size(); i++) {
                		if(i == 0) {
                			entries = entries +  placedWages.get(i);
                		} else {
                			entries = entries + ", " + placedWages.get(i);
                		}
                		
                	}
                	sendMessage(my_channel, "Users That Placed Wagers: " + entries);
                	placedWages.clear();
            	}
            	
            }
		}
		
		//MyLottoSystem Observer: Lists who has been entered into the lottery
		else if(arg0 instanceof MyLottoSystem) {
            sendMessage(my_channel, "Users entered into Lottery: " + my_lottoSystem.getUsers());
        	my_lottoSystem.emptyUsers();
		}
		
		//MylottoAdvert Observer: Advertises the Lottery is active, and how to buy tickets.
		else if(arg0 instanceof MyLotteryAdvert && lottoOn) {
			int amount = lottoPeople.size() * my_lottery_cost;
			lotto_time_counter += (my_lottery_timer / 5);
            sendMessage(my_channel, "Lottery is currently active with a total of " + amount + " points in the pool. To purchase a ticket for " + my_lottery_cost + " points, type !buyticket. The lottery will end in " + ((my_lottery_timer - lotto_time_counter) / 60000) + " minutes.");
		}
		
		//MyLotteryWinner Observer: Will display the winner, stop the lotton system, and do other work
		else if(arg0 instanceof MyLotteryWinner) {
			String winner = my_lottoWinner.getWinner();
			int amount = lottoPeople.size() * my_lottery_cost;
            sendMessage(my_channel, "***WINNER WINNER CHICKEN DINNER*** The Lottery WINNER is " + winner + " , winning a total of " + amount + " points! Congrats!");
    		my_botUsers.incrementTankerPoints(winner, amount);
        	my_lottoSystem.emptyUsers();
            my_lottoWinner.emptyUsers();
        	lottoPeople.clear();
        	lotto_time_counter = 0;
        	if (lottoOn) {
        		lottoOn = false;
        		my_lottoAdvert.setLottoOn(false);
                sendMessage(my_channel, "The Lottery System has been shut down!");    	
        	} else {
        		sendMessage(my_channel, "The Lottery System is already de-activated.");
        	}
        	winner = "reset";
		}
		
		//MyConnected Observer: Checks if the bot disconnects, and try reconnecting. 
		// This is because of the way PircBot disconnects if idle after 5 mins. Unable to fix because
		// Pircbot does not allow the ability to override the method which is a huge oversite on the PircBot DEV!
		else if(arg0 instanceof MyConnected) {
			if(!this.isConnected() && !wantingToDisconnect) {
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
		if(wantingToDisconnect) {
			//do nothing , we want to stay connected
		} else { //random timeout, try reconnecting
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
	
	public void wantDisconnect(boolean answer) {
		wantingToDisconnect = answer;
	}
	
	public void setAdvertTimer(int theAmount) {
		advertTimer = theAmount;
	}

	public void saveBackupUserFile() {
		my_botUsers.saveBackupFile();
		
	}

	public void setCurrentUsers(User[] my_users2) {
        my_users = my_users2;
        my_botUsers.setCurrentUsers(my_users2);
	}

	public boolean importUserMap(File file) {
		return my_botUsers.importUserMap(file);
		
	}
	
	public boolean exportUserMap() {
		return my_botUsers.exportUserMap();
		
	}
}
