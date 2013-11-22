import java.util.ArrayList;



public class MyBotMain {
    
	//GENERAL BOT SETTING

	private static final String BOTNAME = "NoroBot";
	private static final String my_channel = "#noroimusha";
	private static final String my_server_ip = "199.9.250.229";
	private static final int my_server_port = 6667;
	private static final String my_login_password = "151515";
	private static final boolean enable_verbose_debug = true;
	
	private static final boolean LOTTERY_ENABLED_ON_STARTUP = false;
	private static final int LOTTERY_COST = 5;
	private static final int LOTTERY_TIMER_MILLISEC = 1800000;
	private static final boolean ACCUMULATE_POINTS_ON_STARTUP = true;
	private static final String CHANNEL_OWNER = "noroimusha";
	private static final String[] CHANNEL_OP1 = {"krashnburnz"};
	private static final String POINTS_NAME = "piratepoints";

	private static ArrayList<String> the_ops = new ArrayList<String>();
	
	
    public static void main(String[] args) throws Exception {
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