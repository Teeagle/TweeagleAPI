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
			if(indexDict.containsKey(token)) {
				ArrayList<Integer> tempDocIDs = indexDict.get(token).getTweetIds();
				for(Integer id : tempDocIDs) {
					docIDs.add(id);
				}
			}
			
		}
		
		// Loading Tweets from Files
		ArrayList<Tweet> tweets = new ArrayList<Tweet>();
		for(Integer id : docIDs) {
			tweets.add(MemoryManager.readTweetFromFile(id+".txt"));
		}		
		
		tweets = Ranking.rankResults(index, tweets, query, 1);
		
		for(Tweet tweet : tweets) {
			System.out.println(tweet.getDocID()+" score: "+tweet.getScore());
		}
	}
}
