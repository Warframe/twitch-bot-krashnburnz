package Model;
import java.util.ArrayList;



public class MyBotMain {
    
	//Connection Settings
	private static String BOTNAME;
	private static String my_channel;
	private static String my_server_ip;
	private static int my_server_port;
	private static String my_login_password;
	private static String CHANNEL_OWNER;
	private static String POINTS_NAME;
	
	//General Settings
	private static final boolean LOTTERY_ENABLED_ON_STARTUP = false;
	private static final int LOTTERY_COST = 5;
	private static final int LOTTERY_TIMER_MILLISEC = 1800000;
	private static final boolean ACCUMULATE_POINTS_ON_STARTUP = true;
	private static final String[] CHANNEL_OP1 = {"krashnburnz"};
	private static final boolean enable_verbose_debug = true;
	
	//Container to hold Channel Mods
	private static ArrayList<String> the_ops = new ArrayList<String>();
	
	
    public static void main(String[] args) throws Exception {
    	BOTNAME = args[0];
    	my_channel = "#"+ args[1];
    	my_server_ip = args[2];
    	my_server_port = Integer.parseInt(args[3]);
    	my_login_password = args[4];
    	CHANNEL_OWNER = args[1];
    	POINTS_NAME = args[5];
    	
		System.out.println(System.getProperty("user.dir"));
		
		//create Channel operators list
		for(int i = 0; i < CHANNEL_OP1.length; i++) {
			the_ops.add(CHANNEL_OP1[i]);
		}
		
        // Now start our bot up.
        MyBot bot = new MyBot(BOTNAME, LOTTERY_ENABLED_ON_STARTUP, ACCUMULATE_POINTS_ON_STARTUP,the_ops, CHANNEL_OWNER, POINTS_NAME, LOTTERY_COST, LOTTERY_TIMER_MILLISEC, my_channel);
        
        // Enable debugging output.
        bot.setVerbose(enable_verbose_debug);
        
        // Connect to the IRC server.
        bot.connect(my_server_ip, my_server_port, my_login_password);

        // Join the #pircbot channel.
        bot.joinChannel(my_channel);

    }
    
}