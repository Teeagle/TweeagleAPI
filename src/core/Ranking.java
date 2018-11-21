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
		double score = 0;
		
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
		
		double qvLength = 0; // Length for Normalization
		double dvLength = 0; // Length for Normalization
	
		// Calculating Vectors
		for(String term : terms) {
			
			// Query Vector Creation
			double qTFWeight = 0;
			double idf = 0;
			double tf_idf = 0;
			int df = indexTerms.get(term).getDf();
			if (queryTerms.containsKey(term)) {
				qTFWeight = Math.log10(1 + queryTerms.get(term));
			}
			idf = Math.log10(index.getTotalDocuments()/df);
			tf_idf = idf * qTFWeight;
			queryVector.add(tf_idf); // Query Vector
			qvLength+=tf_idf;
			
			// Document Vector Creation
			double tfWeight = 0;
			if(tweetDictionary.containsKey(term)) {
				tfWeight = Math.log10(1 + tweetDictionary.get(term).getTf());
			}
			tweetVector.add(tfWeight); // Document Vector
			dvLength+=tfWeight;
		}
		
		// Normalize vectors and calculate score
		for(int i=0;i<queryVector.size();i++) {
			double q = queryVector.get(i) / qvLength;
			double d = tweetVector.get(i) / dvLength;
			score = q*d;
		}
		
		return score;
	}
	
	public static float calculateTweetBasedScore(Tweet tweet) {
		float score = 0;
		
		return score;
	}	

}
