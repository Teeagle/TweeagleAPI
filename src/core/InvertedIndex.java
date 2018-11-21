package core;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Map;
import java.util.Map.Entry;
import java.util.StringTokenizer;
import java.util.TreeMap;

public class InvertedIndex implements Serializable {

	// Dictionary Holder
	private TreeMap<String, IndexTermInfo> dictionary;
	private int totalDocuments;

	public InvertedIndex() {
		dictionary = new TreeMap<String, IndexTermInfo>();
		totalDocuments = 0;
	}

	public TreeMap<String, IndexTermInfo> getDictionary() {
		return dictionary;
	}

<<<<<<< HEAD
	private void addTerm(String token, Tweet tweet, int pos) {
=======
	private boolean addTerm(String token, Tweet tweet, int pos) {
		
>>>>>>> e1bd11e4fca4e43844e3294425f6d570fd91b7d0
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
	}

	private boolean deleteTerm() {
		return false;
	}

	private void addDocument(Tweet tweet) {
		StringTokenizer tokens = new StringTokenizer(tweet.getText(), " .,';?\\\"!$%^&*-–—+=_()<>|/\\\\|[]`~\n\t");

		int pos = 0;
		while (tokens.hasMoreTokens()) {
			String token = tokens.nextToken();

			// Convert character to lower case
			token = token.toLowerCase();

			addTerm(token, tweet, pos++);
		}
	}

	public boolean addDocuments(ArrayList<Tweet> tweets) {

		for (int i = 0; i < tweets.size(); i++) {
			addDocument(tweets.get(i));
			totalDocuments++;
		}

		if (!MemoryManager.storeIndex(this)) {
			System.err.println("[!] Failed to store index to memory.");
			return false;
		}

		return true;

	}

	public boolean deleteDocument(Tweet tweet) {
		// Deletes from index

		for (String term : tweet.getDictionary().keySet()) {
			this.dictionary.get(term).tweetIds.remove((Integer) tweet.getDocID());
			totalDocuments--;
		}
		// TODO: Deletes from memory

		return false;
	}

	public void printIndex() {
		System.out.println(this.toString());
	}

	@Override
	public String toString() {

		String lol = "";

		for (Entry<String, IndexTermInfo> entry : this.dictionary.entrySet()) {
			lol = lol.concat(entry.getKey() + " " + entry.getValue() + " \n");
		}

		return lol;
	}

	public int getTotalDocuments() {
		return totalDocuments;
	}

}
