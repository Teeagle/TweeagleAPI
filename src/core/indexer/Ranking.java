package core.indexer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.TreeMap;
import java.util.Map.Entry;

import javax.management.Query;

import core.indexer.obj.IndexTermInfo;
import core.obj.TermInfo;
import core.obj.Tweet;

public class Ranking {

	/***
	 * Calculate the score of the query-document pair based on the Vector Space
	 * Model
	 * 
	 * @param tweet
	 * @param query
	 * @return
	 */
	public static double calculateVSMScore(InvertedIndex index, Tweet tweet, String query) {
		double score = 0;

		TreeMap<String, IndexTermInfo> indexTerms = index.getDictionary();
		Set<String> terms = new HashSet<String>(); // A set of unique terms in query-document pair
		HashMap<String, Integer> queryTerms = new HashMap<>(); // Query terms and their frequency
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
				queryTerms.put(token, queryTerms.get(token) + 1);
			} else {
				queryTerms.put(token, 1);
			}
		}

		double qvLength = 0; // Length for Normalization
		double dvLength = 0; // Length for Normalization

		// Calculating Vectors
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
		int retweets = tweet.getRetweetCnt();
		int favorites = tweet.getFavoriteCnt();
		int replies = tweet.getReplyCnt();
		int quotes = tweet.getQuoteCnt();
		int followers = tweet.getUserFollowersCnt();

		if (tweet.getIsVerified()) {
			verified = 1;
		}
		double total = retweets + favorites + replies + quotes + followers;
		double score = 0;
		// Empirical weights to the features
		if (total > 0) {
			score = 0.4 * verified + 0.1 * retweets / total + 0.1 * favorites / total + 0.1 * replies / total
					+ 0.1 * quotes / total + 0.2 * followers / total;
		}else {
			score = 0.4 * verified;
		}

		return score;
	}

	public static ArrayList<Tweet> rankResults(InvertedIndex index, ArrayList<Tweet> tweets, String query, int option) {

		switch (option) {
		case 0: {
			for (Tweet tweet : tweets) {
				double score = calculateVSMScore(index, tweet, query) + calculateTweetBasedScore(tweet);
				tweet.setScore(score);
			}
			// Both
			break;
		}
		case 1: {
			// VSM Ranking
			for (Tweet tweet : tweets) {
				tweet.setScore(calculateVSMScore(index, tweet, query));
			}

			break;
		}
		case 2: {
			// Tweet Ranking
			for (Tweet tweet : tweets) {
				double score = calculateTweetBasedScore(tweet);
				tweet.setScore(score);
			}
			break;
		}
		default: {
			break;
		}
		}

		Collections.sort(tweets);

		return tweets;
	}
}