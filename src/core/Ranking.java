package core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.TreeMap;
import java.util.Map.Entry;

import javax.management.Query;

public class Ranking {
	
	
	/***
	 * Calculate the score of the query-document pair based on the Vector Space Model
	 * @param tweet
	 * @param query
	 * @return
	 */
	public static double calculateVSMScore(InvertedIndex index, Tweet tweet, String query) {
		float score = 0;
		
		TreeMap<String, IndexTermInfo> indexTerms = index.getDictionary();
		Set<String> terms = new HashSet<String>(); // A set of unique terms in query-document pair
		HashMap<String,Integer> queryTerms = new HashMap<>(); // Query terms and their frequency 
		TreeMap<String, TermInfo> tweetDictionary = tweet.getDictionary();
		ArrayList<Double> queryVector = new ArrayList<>();
		ArrayList<Double> tweetVector = new ArrayList<>();
		
		// Adding Tweet Dictionary Terms to terms set
		for (Entry<String, TermInfo> entry : tweetDictionary.entrySet()) {
			terms.add(entry.getKey());
		}
		
		// Adding Query Terms to terms set
		StringTokenizer tokens = new StringTokenizer(query, " \t");
		
		while (tokens.hasMoreTokens()) {
			String token = tokens.nextToken();
			token = token.toLowerCase();
			terms.add(token);
			if (queryTerms.containsKey(token)) {
				queryTerms.put(token,queryTerms.get(token)+1);
			}else {
				queryTerms.put(token,0);
			}
		}
		
		// Calculating Vectors
		for(String term : terms) {
			
			// Query Vector
			int qTF = 0;
			double idf = 0;
			double tf_idf = 0;
			int df = indexTerms.get(term).getDf();
			if (queryTerms.containsKey(term)) {
				qTF = queryTerms.get(term);
			}
			idf = Math.log10(index.getTotalDocuments()/df);
			
			// Document Vector
			double tfWeight = 0;
			if(tweetDictionary.containsKey(term)) {
				tfWeight = (Double) Math.log10(1 + tweetDictionary.get(term).getTf());
			}
			tweetVector.add(tfWeight);
		}
		
		return score;
	}
	
	public static float calculateTweetBasedScore(Tweet tweet) {
		float score = 0;
		
		return score;
	}	

}
