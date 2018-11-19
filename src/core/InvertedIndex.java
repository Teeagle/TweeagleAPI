package core;

import java.util.ArrayList;
import java.util.Map;
import java.util.Map.Entry;
import java.util.StringTokenizer;
import java.util.TreeMap;

public class InvertedIndex {

	// Dictionary Holder
	private TreeMap<String, IndexTermInfo> dictionary;

	public InvertedIndex() {
		dictionary = new TreeMap<String, IndexTermInfo>();
	}

	private boolean addTerm(String token, Tweet tweet, int pos) {

		// Index Dictionary

		if (dictionary.containsKey(token)) {
			// If the key exists, update it

			dictionary.get(token).increaseDF();
			dictionary.get(token).addTweetID(tweet.getDocID());
		} else {
			// If the key does not exist, add it to dictionary

			IndexTermInfo info = new IndexTermInfo();

			info.setDf(1);
			info.addTweetID(tweet.getDocID());

			dictionary.put(token, info);
		}

		// Tweet Dictionary
		tweet.addTerm(token, pos);

		return true;
	}

	private boolean deleteTerm() {
		return false;
	}

	private boolean addDocument(Tweet tweet) {
		StringTokenizer tokens = new StringTokenizer(tweet.getText(), " .,';?\\\"!$%^&*-–—+=_()<>|/\\\\|[]`~\n\t");

		int pos = 0;
		while (tokens.hasMoreTokens()) {
			String token = tokens.nextToken();

			// Convert character to lower case
			token = token.toLowerCase();

			addTerm(token, tweet, pos++);
		}

		return false;

	}

	public boolean addDocuments(ArrayList<Tweet> tweets) {

		for (int i = 0; i < tweets.size(); i++) {
			addDocument(tweets.get(i));
		}

		return true;

	}

	public boolean deleteDocument() {
		return false;
	}

	public boolean storeIndex() {
		return false;
	}

	public boolean loadIndex() {
		return false;
	}

	public void printIndex() {

	}

	@Override
	public String toString() {
		
		String lol = "";
		
		for (Entry<String, IndexTermInfo> entry : dictionary.entrySet()) {
			lol = lol.concat(entry.getKey()+" "+entry.getValue()+" \n");
		}
		
		return lol;
	}
}
