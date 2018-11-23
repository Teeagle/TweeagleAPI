package core.indexer;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.TreeMap;

import core.indexer.obj.IndexTermInfo;
import core.obj.TermInfo;
import core.obj.Tweet;
import core.utils.MemoryManager;

public class QueryProcessing {
	
	private InvertedIndex index;
	
	public QueryProcessing() {
		this.index = MemoryManager.loadIndexState();
	}
	public QueryProcessing(InvertedIndex index) {
		this.index = index;
	}
	

	/***
	 * 
	 * @param query
	 * @param ranking 
	 * @return 
	 */
	public ArrayList<Tweet> textSearch(String query, Integer ranking) {
		StringTokenizer tokens = new StringTokenizer(query, " .,';?\\\"!$%^&*-–—+=_()<>|/\\\\|[]`~\n\t");
		Set<Integer> docIDs = new HashSet<Integer>();

		while (tokens.hasMoreTokens()) {
			String token = tokens.nextToken();
			token = token.toLowerCase();

			// Checks if token exists in dict
			if(index==null) {
				return new ArrayList<Tweet>();
			}
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

		tweets = Ranking.rankResults(index, tweets, query, ranking);

		for (Tweet tweet : tweets) {
			System.out.println(tweet.getDocID() + " score: " + tweet.getScore());
		}
		
		return tweets;
	}

	/***
	 * Searches index for an entire phrase
	 * using positional posting lists
	 * @param query
	 * @param ranking 
	 * @return 
	 */
	public ArrayList<Tweet> phraseSearch(String query, Integer ranking) {
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
				return null;
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
		tweets = Ranking.rankResults(index, tweets, query, ranking);
		for (Tweet tweet : tweets) {
			System.out.println(tweet.getDocID() + " score: " + tweet.getScore());
			System.out.println(tweet.getText());
			System.out.println("-------------------------------------------------------------------\n");
		}
		
		return tweets;
	}
}
