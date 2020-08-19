//This contains all the functions for The SimpleBot

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import org.jibble.pircbot.*;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

	//Simple bot extends PircBot
	public class SimpleBot extends PircBot {

		//declaration of global variables.
		String ID;
		String block;
		static String type = "any";

		public SimpleBot() {

			this.setName("ShubhBot");
	
		}
		
	//This function is called every time a message is passed	
		public void onMessage(String channel, String sender,String login, String hostname, String message) {
	
			//changing the message to lowercase so I don't have to worry about case of entered message.
			message = message.toLowerCase();
			
			// checking if the message contains the keyword for API call, and the required parameter.
			if (message.contains("weather") && findID(message)) {
			
				String output = sendRequest1();
				String str = parser1(output);
				sendMessage(channel, str);
		
			}
			// checking if the message contains the keyword for API call, and the required parameter.			
			else if( message.contains ("joke")){
				
				
				//this api takes the category form the user and spits out a random joke. (by default I have set the category to any)
				findType(message);
				
				String output = sendRequest2();
				String str = parser2(output);
				sendMessage(channel, str);
				sendMessage(channel, block);
				
			}
			
			else {
				sendMessage(channel, "You have entered a wrong message that does not meet any criteria");
			}
			}
		
		//function to check if the message contains the ID 
		public boolean findID(String mes) {
			
			//splitting the message into individual words.
			String[] words = mes.split(" ");
			
			for(int i = 0; i < words.length; i++) {
				
				String temp = words[i];
				
				for(int j = 0;j<words[i].length(); j++) {
					
					char cr = temp.charAt(j);
				
					//if any of the characters is a digit, it is most likely the ID
					if(Character.isDigit(cr)) {
					//set the ID to the found ID in the message
						ID = words[i];
						return true;
					}
				}
			}
			
			return false;
			
		}

		//function to check if the message contians a type for the joke 
		public boolean findType(String mes) {
			
			//splitting the message into individual words.
			String[] words = mes.split(" ");
			
			String[] typelist = {"Programming","Miscellaneous","Dark","Any"};
			
			for(int i = 0; i < words.length; i++) {
				
				for(int j = 0; j<4; j++) {
				
					//if the any word in the message is same as the categories
				if(words[i].equalsIgnoreCase(typelist[j])) {
				
					//overwrite the type to the specified type
					type = typelist[j];
				
				return true;
					}
				}
			}
			return false;
				
		}
			
		//if the weather api is called.	
		public String sendRequest1() {
			
			String link = "http://api.openweathermap.org/data/2.5/weather?id="+ ID +"&APPID=94cd1f20fc459f6d47d9921afaa64952";
			
			//this contains the response from api hit
			StringBuilder response = new StringBuilder();

			try{

				//setting a HTTP object
			     URL url = new URL(link);

			     HttpURLConnection con = (HttpURLConnection) url.openConnection();

			     //request set to get
			     con.setRequestMethod("GET");

			     BufferedReader input = new BufferedReader(new InputStreamReader(con.getInputStream()));

			     String line;

			     //as long as there is more data to intake
			     while ((line = input.readLine()) != null){

			         response.append(line);

			     }

			     input.close();

			     return response.toString();

			}

			catch(Exception e){return "!!! Error encounterd !!!: " + e;}

			

		}

		//if the Jokes api is called
		public static String sendRequest2() {
				
			String link = "https://sv443.net/jokeapi/category/" + type;
			
			//this contains the response from api hit
			StringBuilder response = new StringBuilder();

			try{
				
			     URL url = new URL(link);

			     HttpURLConnection con = (HttpURLConnection) url.openConnection();
			     
			     //the api is designed to work primarily with IE. so I change that to chrome.
			     con.addRequestProperty("User-Agent", "chrome"); 

			     //request set to get
			     con.setRequestMethod("GET");

			     BufferedReader input = new BufferedReader(new InputStreamReader(con.getInputStream()));

			     String line;

			     //as long as there is more data to intake
			     while ((line = input.readLine()) != null){

			         response.append(line);

			     }
	

			     input.close();		     

			     return response.toString();

			}

			catch(Exception e){return "!!! Error encounterd !!!: " + e;}

			

			
		}
		
		
		//String Parser for Whether api
		public String parser1(String str) {
			
			//declare objects for parsing
			JsonObject obj = new JsonParser().parse(str).getAsJsonObject();
			JsonObject main = obj.getAsJsonObject("main");
		
			
			String CityName = obj.get("name").getAsString();
			String Temp = main.get("temp").getAsString();
			String TempL = main.get("temp_min").getAsString();
			String TempH = main.get("temp_max").getAsString();
			
			//returning teh final string
			return "The Current weather in " + CityName  + " is current temperature = " + Temp + "; MAX temperature " + TempH + "; MIN temperature " + TempL; 
		}

		//String parser for Jokes api
		public String parser2(String str) {
			
			//declare objects for parser
			JsonObject obj = new JsonParser().parse(str).getAsJsonObject();
		
			String set = obj.get("type").getAsString();
		
			//if its a one liner
			if(set.compareToIgnoreCase("single") == 0) {
			
				String joke = obj.get("joke").getAsString();
				String category = obj.get("category").getAsString();
				block = " Joke: " + joke; 
				return "Type: " + category ; 
				
			}
			
			//if its a two part joke
			else {
				String setup = obj.get("setup").getAsString();
				String category = obj.get("category").getAsString();
				String del = obj.get("delivery").getAsString();
				block = "Setup: " + setup + " Delivery: " + del;
				return "Type: " + category  ; 
			}
		}

		
	}
		


	
