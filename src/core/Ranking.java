package core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.TreeMap;
import java.util.Map.Entry;

import javax.management.Query;

public class Ranking {
	

	private static double searchThreshold = 0.3;
	private static double searchThresholdTwitter = 0.3;
	private static int numResults = 20;
	
	// Query Info
	static class QueryInfo{
		private ArrayList<Double> queryVector;
		private double qvLength;
		private HashMap<String, Integer> queryTerms;
		public QueryInfo(ArrayList<Double> queryVector,double qvLength,HashMap<String, Integer> queryTerms) {
			this.queryVector = queryVector;
			this.qvLength = qvLength;
			this.queryTerms = queryTerms;
		}	
		
		public Set<String> appendTerms(Set<String> terms) {			
			for (Entry<String, Integer> entry : queryTerms.entrySet()) {
				terms.add(entry.getKey());
			}			
			return terms;
		}
	}
	
	
	public static QueryInfo calculateQueryVector(InvertedIndex index, String query) {
		TreeMap<String, IndexTermInfo> indexTerms = index.getDictionary();
		ArrayList<Double> queryVector = new ArrayList<>();
		HashMap<String, Integer> queryTerms = new HashMap<>(); // Query terms and their frequency
		double qvLength = 0; // Length for Normalization
		Set<String> terms = new HashSet<String>();
		
		// Adding Query Terms to terms set
		StringTokenizer tokens = new StringTokenizer(query, " \t");

		while (tokens.hasMoreTokens()) {
			String token = tokens.nextToken();
			token = token.toLowerCase();
			terms.add(token);
			if (queryTerms.containsKey(token)) {
				queryTerms.put(token, queryTerms.get(token) + 1);
			} else {
				queryTerms.put(token, 1);
			}			
		}
		
		for (String term : terms) {
			// Query Vector Creation
			double qTFWeight = 0;
			double idf = 0;
			double tf_idf = 0;
			if (indexTerms.containsKey(term)) {
				double df = indexTerms.get(term).getDf();
				if (queryTerms.containsKey(term)) {
					qTFWeight = Math.log10(1 + queryTerms.get(term));
				}
				idf = Math.log10(index.getTotalDocuments() / df);
				tf_idf = idf * qTFWeight;
			}
			queryVector.add(tf_idf); // Query Vector
			qvLength += tf_idf;
		}	
		
		return new QueryInfo(queryVector,qvLength,queryTerms);
	}

	/***
	 * Calculate the score of the query-document pair based on the Vector Space
	 * Model
	 * 
	 * @param tweet
	 * @param query
	 * @return
	 */
	public static double calculateVSMScore(InvertedIndex index, Tweet tweet, QueryInfo queryInfo) {
		double score = 0;

		TreeMap<String, IndexTermInfo> indexTerms = index.getDictionary();
		Set<String> terms = new HashSet<String>(); // A set of unique terms in query-document pair
		TreeMap<String, TermInfo> tweetDictionary = tweet.getDictionary();
		
		ArrayList<Double> queryVector = queryInfo.queryVector;
	//	System.out.println(queryVector);
		
		ArrayList<Double> tweetVector = new ArrayList<>();

		// Adding Tweet Dictionary Terms to terms set
		for (Entry<String, TermInfo> entry : tweetDictionary.entrySet()) {
			terms.add(entry.getKey());
		}
		terms = queryInfo.appendTerms(terms);

		double qvLength = queryInfo.qvLength; // Length for Normalization
		double dvLength = 0; // Length for Normalization

		// Calculating Vectors
		for (String term : terms) {
			// Document Vector Creation
			double tfWeight = 0;
			if (tweetDictionary.containsKey(term)) {
				tfWeight = Math.log10(1 + tweetDictionary.get(term).getTf());
			}
			tweetVector.add(tfWeight); // Document Vector
			dvLength += tfWeight;
		}

		// Normalize vectors and calculate score
		if (qvLength > 0 && dvLength > 0) {
			for (int i = 0; i < queryVector.size(); i++) {
				double q = (double) queryVector.get(i) / qvLength;
				double d = (double) tweetVector.get(i) / dvLength;
				score += q * d;
			}
		}

		return score;
	}

	/***
	 * Calculating Tweet score based on selected features with empirical weight
	 * 
	 * @param tweet
	 * @return score
	 */
	public static double calculateTweetBasedScore(Tweet tweet) {
		// Features that affect the Tweet Score
		int verified = 0;
		double retweets = Math.log10(tweet.getRetweetCnt() + 1);
		double favorites = Math.log10(tweet.getFavoriteCnt() + 1);
		double replies = Math.log10(tweet.getReplyCnt() + 1);
		double quotes = Math.log10(tweet.getQuoteCnt() + 1);
		double followers = Math.log10(tweet.getUserFollowersCnt() + 1);

		if (tweet.getIsVerified()) {
			verified = 1;
		}
		
		double total =0;
		double euc_sum = retweets*retweets + favorites*favorites + replies*replies
				+ quotes*quotes + followers*followers;
		if(euc_sum>0) {
			total = Math.sqrt(euc_sum);
		}
		
		double score = 0;
		// Empirical weights to the features
		if (total > 0) {
			score = 0.4 * verified + 0.1 * retweets / total + 0.1 * favorites / total + 0.1 * replies / total
					+ 0.1* quotes / total + 0.2 * followers / total;
		}else {
			score = 0.4 * verified;
		}

		return score;
	}

	public static ArrayList<Tweet> rankResults(InvertedIndex index, ArrayList<Tweet> tweets, String query, int option) {
		
		QueryInfo queryInfo = calculateQueryVector(index, query);
		
		ArrayList<Tweet> mostRelevant = new ArrayList<>();
		
		switch (option) {
		case 0: {
			for (Tweet tweet : tweets) {
				double scoreVsm= calculateVSMScore(index, tweet, queryInfo); 
				double scoreTweet= calculateTweetBasedScore(tweet); 
				double score =scoreVsm+scoreTweet;
				tweet.setScore(score);
				tweet.setScoreVSM(scoreVsm);
				
				// Keeping only most relevant tweets
				if (mostRelevant.size() < numResults) {
					mostRelevant.add(tweet);
					Collections.sort(mostRelevant);
				}else {
					if (scoreVsm < searchThreshold) {
						if (scoreVsm > mostRelevant.get(mostRelevant.size()-1).getScoreVSM()) {
							mostRelevant.remove(mostRelevant.size()-1);
							mostRelevant.add(tweet);
							Collections.sort(mostRelevant);
						}
					}
				}
			}
			// Both
			break;
		}
		case 1: {
			// VSM Ranking		
			
			for (Tweet tweet : tweets) {
				double score = calculateVSMScore(index, tweet, queryInfo); 
				tweet.setScore(score);
				tweet.setScoreVSM(score);
				
				// Keeping only most relevant tweets
				if (mostRelevant.size() < numResults) {
					mostRelevant.add(tweet);
					Collections.sort(mostRelevant);
				}else {
					if (score < searchThreshold) {
						if (score > mostRelevant.get(mostRelevant.size()-1).getScore()) {
							mostRelevant.remove(mostRelevant.size()-1);
							mostRelevant.add(tweet);
							Collections.sort(mostRelevant);
						}
					}
				}
			}
			break;
		}
		case 2: {
			// Tweet Ranking
			for (Tweet tweet : tweets) {
				double score = calculateTweetBasedScore(tweet);
				tweet.setScore(score);
				
				// Keeping only most relevant tweets
				if (mostRelevant.size() < numResults) {
					mostRelevant.add(tweet);
					Collections.sort(mostRelevant);
				}else {
					if (score < searchThresholdTwitter) {
						if (score > mostRelevant.get(mostRelevant.size()-1).getScore()) {
							mostRelevant.remove(mostRelevant.size()-1);
							mostRelevant.add(tweet);
							Collections.sort(mostRelevant);
						}
					}
				}
			}			
			break;
		}
		default: {
			break;
		}
		}

		return mostRelevant;
	}
}
