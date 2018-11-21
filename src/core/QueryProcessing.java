package core;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.TreeMap;

public class QueryProcessing {

	// Find index from controller
	static InvertedIndex index = TweeagleController.index;

	/***
	 * 
	 * @param query
	 */
	public static void textSearch(String query) {
		StringTokenizer tokens = new StringTokenizer(query, " .,';?\\\"!$%^&*-–—+=_()<>|/\\\\|[]`~\n\t");

		Set<Integer> docIDs = new HashSet<Integer>();

		while (tokens.hasMoreTokens()) {
			String token = tokens.nextToken();
			token = token.toLowerCase();

			// Checks if token exists in dict
			TreeMap<String, IndexTermInfo> indexDict = index.getDictionary();
			if (indexDict.containsKey(token)) {
				ArrayList<Integer> tempDocIDs = indexDict.get(token).getTweetIds();
				for (Integer id : tempDocIDs) {
					docIDs.add(id);
				}
			}

		}

		// Loading Tweets from Files
		ArrayList<Tweet> tweets = new ArrayList<Tweet>();
		for (Integer id : docIDs) {
			tweets.add(MemoryManager.readTweetFromFile(id + ".txt"));
		}

		tweets = Ranking.rankResults(index, tweets, query, 1);

		for (Tweet tweet : tweets) {
			System.out.println(tweet.getDocID() + " score: " + tweet.getScore());
		}
	}

	public static void phraseSearch(String query) {
		// Tokenize to get terms
		StringTokenizer tokens = new StringTokenizer(query, " .,';?\\\"!$%^&*-–—+=_()<>|/\\\\|[]`~\n\t");
		Set<Integer> docIDs = new HashSet<>();

		boolean firstIteration = true;

		while (tokens.hasMoreTokens()) {
			String token = tokens.nextToken();
			token = token.toLowerCase();

			// Checks if token exists in dict
			TreeMap<String, IndexTermInfo> indexDict = index.getDictionary();
			if (indexDict.containsKey(token)) {
				ArrayList<Integer> tempDocIDs = indexDict.get(token).getTweetIds();
				if (firstIteration) {
					docIDs.addAll(tempDocIDs);
					firstIteration = false;
				} else {
					// retain
					docIDs.retainAll(tempDocIDs);
				}
			} else {
				System.out.println("Phrase not Found");
				return;
			}

		}

		// For each document, check terms depending on the positions in tweets'
		// Loading Tweets from Files
		ArrayList<Tweet> tweets = new ArrayList<Tweet>();
		for (Integer id : docIDs) {
			Tweet tweet = MemoryManager.readTweetFromFile(id + ".txt");
			TreeMap<String, TermInfo> tweetDict = tweet.getDictionary();
			while (tokens.hasMoreTokens()) {
				String token = tokens.nextToken();
				token = token.toLowerCase();
				
				ArrayList<Integer> positions = tweetDict.get(token).getPositions();
			}
			
		}

		tweets = Ranking.rankResults(index, tweets, query, 1);

		for (Tweet tweet : tweets) {
			System.out.println(tweet.getDocID() + " score: " + tweet.getScore());
		}
	}
}
