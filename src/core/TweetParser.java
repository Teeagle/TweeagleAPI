package core;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;

public class TweetParser {

	private String CORPUS_PATH;
	private String OFFLINE_DATA = "tweets/";

	private ArrayList<Tweet> tweets;

	/**
	 * Default Constructor
	 */
	public TweetParser() {
		tweets = new ArrayList<Tweet>();
		this.CORPUS_PATH = "TweeagleCorpus/";

		// Initialize saved tweets directory
		initializeTweetsDirectory();
	}

	/**
	 * Default Constructor
	 */
	public TweetParser(String corpus) {
		this.CORPUS_PATH = corpus;

		// Initialize saved tweets directory
		initializeTweetsDirectory();
	}

	/**
	 * Lists every file in a given collection.
	 */
	public void parseFiles() {
		// List all files of the corpus directory
		File file = new File(CORPUS_PATH);
		String[] files = file.list(new FilenameFilter() {
			@Override
			public boolean accept(File current, String name) {
				return new File(current, name).isFile();
			}
		});

		int docIdCount = 0;

		// Parse all files
		for (int i = 0; i < files.length; i++) {
			// Load File to memory
			File f = new File(CORPUS_PATH + files[i]);

			String line;
			try {
				BufferedReader bufferedReader = new BufferedReader(new FileReader(f));

				// Initialize a JSON Parser
				JsonParser jsonParser = new JsonParser();

				// Get global JSON object
				JsonElement jsonTree = null;

				while ((line = bufferedReader.readLine()) != null) {
					jsonTree = jsonParser.parse(line);
					JsonObject jsonObject = jsonTree.getAsJsonObject();

					// Initialize Tweet
					Tweet tweet = new Tweet();

					// Set a unique docID for the Tweeagle system
					tweet.setDocID(docIdCount++);

					// Parse JSON response
					tweet.setId(jsonObject.get("id").getAsString());
					tweet.setCreated_at(jsonObject.get("created_at").getAsString());
					tweet.setText(jsonObject.get("text").getAsString());
					tweet.setQuoteCnt(jsonObject.get("quote_count").getAsInt());
					tweet.setReplyCnt(jsonObject.get("reply_count").getAsInt());
					tweet.setRetweetCnt(jsonObject.get("retweet_count").getAsInt());
					tweet.setFavoriteCnt(jsonObject.get("favorite_count").getAsInt());

					// Parse User Meta Information
					tweet.setUsertag(jsonObject.get("user").getAsJsonObject().get("screen_name").getAsString());
					tweet.setIsVerified(jsonObject.get("user").getAsJsonObject().get("verified").getAsBoolean());
					tweet.setUserFriendsCnt(jsonObject.get("user").getAsJsonObject().get("friends_count").getAsInt());
					tweet.setUserFollowersCnt(
							jsonObject.get("user").getAsJsonObject().get("followers_count").getAsInt());
					tweet.setUserFavoritesCnt(
							jsonObject.get("user").getAsJsonObject().get("favourites_count").getAsInt());
					tweet.setUserStatusesCnt(jsonObject.get("user").getAsJsonObject().get("statuses_count").getAsInt());
					tweet.setUserProfileImgURL(
							jsonObject.get("user").getAsJsonObject().get("profile_image_url").getAsString());

					// Debug
					// System.out.println(tweet);
					tweet.createDictionary();

					// Store tweet object
					tweets.add(tweet);

					// Save tweet to memory
					MemoryManager.writeTweetToFile(tweet, OFFLINE_DATA + tweet.getDocID() + ".txt");
				}

				bufferedReader.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}

	public void initializeTweetsDirectory() {
		// Check if the indexes directory does not exist and create it
		File directory = new File(OFFLINE_DATA);

		if (!directory.exists()) {
			directory.mkdir();
			// If you require it to make the entire directory path including parents,
			// use directory.mkdirs(); here instead.
		}
	}

	/**
	 * Returns a list of tweet objects.
	 * 
	 * @return
	 */
	public ArrayList<Tweet> getTweets() {
		return tweets;
	}

	

	/**
	 * Testing Method
	 * 
	 * @param args
	 */
	public static void main(String[] args) {

		TweetParser tp = new TweetParser();
		tp.parseFiles();
	}
}
