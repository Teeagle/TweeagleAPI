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

	/***
	 * Searches index for an entire phrase
	 * using positional posting lists
	 * @param query
	 */
	public static void phraseSearch(String query) {
		// Tokenize to get terms
		StringTokenizer tokens = new StringTokenizer(query, " .,';?\\\"!$%^&*-–—+=_()<>|/\\\\|[]`~\n\t");
		Set<Integer> docIDs = new HashSet<>();
		ArrayList<String> words = new ArrayList<>();

		boolean firstIteration = true;
		
		// AND of Terms
		while (tokens.hasMoreTokens()) {
			String token = tokens.nextToken();
			token = token.toLowerCase();
			words.add(token);
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
			
			
			boolean found = false;
			int cur = -1;
			int total = 1;
			
			ArrayList<Integer> firstWordList = tweetDict.get(words.get(0)).getPositions();			
			while(!found && cur < firstWordList.size()-1) {
				cur++;
				int firstPos = firstWordList.get(cur);
				boolean cont = false;
				for(int i=1;i< words.size();i++) {
					cont = false;
					ArrayList<Integer> nextList = tweetDict.get(words.get(i)).getPositions();
					for(int j=0;j<nextList.size();j++) {
						int num = nextList.get(j);
						if(num == firstPos + i) {
							cont = true;
							total++;
							break;
						}
					}
					if(!cont) {
						total = 1;
						break; // Not found
					}
				}
				if(total == words.size()) {
					found = true;
					//System.out.println(tweet.getText());
					tweets.add(tweet);
				}
			}		
		}

		// Rank the results
		tweets = Ranking.rankResults(index, tweets, query, 1);
		for (Tweet tweet : tweets) {
			System.out.println(tweet.getDocID() + " score: " + tweet.getScore());
			System.out.println(tweet.getText());
			System.out.println("-------------------------------------------------------------------\n");
		}
	}
}
