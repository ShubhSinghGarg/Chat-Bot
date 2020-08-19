
public class Config {

	
	
	public static void main(String[] args) throws Exception {
		
		String channelName = "#ShubhBot";
		
			//Define the bot
			SimpleBot bot = new SimpleBot();
			bot.setVerbose(true);
			
			//connecting to the channel
			try {
			bot.connect("irc.freenode.net");
			}
			
			catch (Exception e) {
			System.out.println("Can’t connect: " + e);
			
			return;
			}
			bot.joinChannel(channelName);  //tells the bot what channel on the server to join
			
			//gives the user instructions on what to do.
			bot.sendMessage(channelName, "Enter the key word Weather and the city ID to get the weather for that city");
			bot.sendMessage(channelName, "OR");
			bot.sendMessage(channelName, "Ask the bot to tell you a joke, you can also select a category : - Programming, Dark, Miscellaneous, Dark, Any ");
		}


	}

