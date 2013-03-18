

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.LinkedList;
import java.util.Scanner;
import org.json.JSONArray;
import org.json.JSONObject;

public class UniqueTweets {
	
	LinkedList<String> displayUrls = new LinkedList<String>();
	LinkedList<Long> ids = new LinkedList<Long>();
	static int count = 0;
	public void getUrls(String hashtag){
		
		try {
			
			int noOfUniqueUrls = 100;
			
			// Referance - https://dev.twitter.com/docs/api/1/get/search
			String url = "http://search.twitter.com/search.json?q=%23"+hashtag+"&rpp=150";
			
			InputStream in = new URL(url).openStream();
			BufferedReader br = new BufferedReader(new InputStreamReader(in, Charset.forName("UTF-8")));
			String response = br.readLine();

			// Tokenize the response and read the response as an array	
			JSONObject json = new JSONObject(response);
			
			JSONArray tweetArray = json.getJSONArray("results");
			JSONObject parse = new JSONObject();
			for(int i=0;i<tweetArray.length();i++){
				if(count == noOfUniqueUrls){
					return;
				}
				else {
					
					parse = tweetArray.getJSONObject(i);
					String res = "https://twitter.com/" + parse.get("from_user") + "/status/" + parse.get("id");
					if(ids.contains(parse.get("id")) == false){
						// the id is not present its a unique url add it to ids and displayUrls
						ids.add((Long) parse.get("id"));
						displayUrls.add(res);
						count ++;
					}	
				}
			}
			if(displayUrls.size()<noOfUniqueUrls){
				System.out.println("The unique results returned filtered on the user id were less than 100");
			}
			in.close();
			
		} catch (IOException e) {
			
			e.printStackTrace();
		}
				
		
	}
	
	public static void main(String[] args){
		try {
			// Read the hashtag from user
			System.out.println("Please enter hashtag: ");
			Scanner input = new Scanner(System.in);
			String hashtag = input.nextLine();
			hashtag = hashtag.trim();
			input.close();
			
			// Validating the user input
			// Reference - http://www.hashtags.org/platforms/twitter/what-characters-can-a-hashtag-include/
			
			if(hashtag.startsWith("#")){
				hashtag = hashtag.substring(1);
			}
			
			// No Special Characters
			if(hashtag.matches("\\s") || hashtag.contains(" ")){
				System.out.println("Error : The hashtag should not contain spaces");
				return;
			}
			
			// Don’t Start With or Use Only Numbers
			if(hashtag.matches("[0-9]+\\w*") || hashtag.matches("^\\d+$")){
				System.out.println("Error : The hashtag should not start with or contain only numbers");
				return;
				
			}
			// No Special Characters like  “!, $, %, ^, &, *, +, .” 
			if (!hashtag.matches("[\\w]*")){
				System.out.println("Error : The hashtag should not contain special characters");
				return;
			}
			
			if(hashtag.equalsIgnoreCase("") || hashtag == null){
				System.out.println("Error : Please enter a valid hashtag (Do not hit enter)");
				return;
			}
			System.out.println("The hashtag entered is #"+hashtag);
			
			UniqueTweets tweets = new UniqueTweets();
			System.out.println("Displaying the unique urls found in the 100 most recent tweets matching the hashtag : #"+hashtag);
			tweets.getUrls(hashtag);
			for(int i =0; i<tweets.displayUrls.size();i++){
				System.out.println(tweets.displayUrls.get(i));
			}
			System.out.println("The number of unique urls found are "+tweets.displayUrls.size());		
		}
		catch (Exception e){
			System.out.println("Error occured : Run the program again");
			e.printStackTrace();
		}
		
	}

}
