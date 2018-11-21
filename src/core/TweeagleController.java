/***
 * The Controller of the Tweeagle Core
 */
package core;

import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Scanner;

public class TweeagleController {

	public static InvertedIndex index;

	public static void runCrawler() {
		System.out.println("Running Crawler");
		// APP:
		Crawler crawler = new Crawler("FRHoEIlNIAdGjNKKGzVyzcpER", "AKOZgkU3XmfQqEaUsHLCFyJzVlM8wPqsdFdY4NiqDkS3BMTYc3",
				"529412295-980juv52ABQEbi72Okc4edAvvYYEHUnC8nNP07an", "Xd61PK5e3WJYw0jaStYPe5zGjPlbvgiP1Noc9HxLnwXI8");
		crawler.setTweetBucketCapacity(1000); // Tweets per bucket-file
		crawler.crawl();
	}

	public static void createIndex() {
		System.out.println("Creating Index");

		// Parsing Tweets
		System.out.println("Runnign Parser");

		TweetParser parser = new TweetParser();
		parser.parseFiles();

		ArrayList<Tweet> tweets = parser.getTweets();

		System.out.println(tweets.size());

		// TODO: Loading tweets (ArrayList of tweets)
		index = new InvertedIndex();

		index.addDocuments(tweets);

//		System.out.println(index);

	}

	public static void main(String args[]) {

		// TODO: Load index if exists

		index = MemoryManager.loadIndexState();

		System.out.println("Welcome to the dark side of Tweeagle >:)");
		System.out.println("Available actions: ");
		System.out.println("-------------------");
		System.out.println("1. Run Crawler");
		System.out.println("2. Create Index");
		System.out.println("3. Delete Index");
		System.out.println("4. Delete a Tweet");
		System.out.println("5. Empty cached Tweets");
		System.out.print("\nType action number: ");

		Scanner scanner = new Scanner(System.in);
		int action = scanner.nextInt();

		switch (action) {
		case 1: {
			// Crawl for Tweets
			runCrawler();
			break;
		}
		case 2: {
			// Initialize index
			createIndex();
			break;
		}
		case 3: {
			// Delete Index from memory
			MemoryManager.deleteIndex();
			break;
		}
		case 4: {
			break;
			// TODO: Delete a Tweet (document)
		}
		case 5: {
			// TODO: Empty cached Tweets
			
			break;
		}
		default: {
			System.out.println("Action not available");

			// MemoryManager.readTweetFromFile("tweets/0.txt");
			// MemoryManager.loadIndex();
			// QueryProcessing.printIndex();
			//index.deleteDocument(MemoryManager.readTweetFromFile("0.txt"));
			index.printIndex();
			break;
		}
		}
	}

}
