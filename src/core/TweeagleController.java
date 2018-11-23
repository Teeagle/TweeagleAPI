/***
 * The Controller of the Tweeagle Core
 */
package core;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Scanner;

public class TweeagleController {

	public static InvertedIndex index;

	public static void initialize() {
		// Load Index from memory
		index = MemoryManager.loadIndexState();
	}

	public static void runCrawler() {
		System.out.println("Running Crawler");
		// APP:
		Crawler crawler = new Crawler("FRHoEIlNIAdGjNKKGzVyzcpER", "AKOZgkU3XmfQqEaUsHLCFyJzVlM8wPqsdFdY4NiqDkS3BMTYc3",
				"529412295-980juv52ABQEbi72Okc4edAvvYYEHUnC8nNP07an", "Xd61PK5e3WJYw0jaStYPe5zGjPlbvgiP1Noc9HxLnwXI8");
		crawler.setTweetBucketCapacity(1000); // Tweets per bucket-file
		crawler.crawl();
	}
public static void printLogo() {

    // The name of the file to open.
    String fileName = "eaglelogo";

    // This will reference one line at a time
    String line = null;

    try {
        // FileReader reads text files in the default encoding.
        FileReader fileReader = 
            new FileReader(fileName);

        // Always wrap FileReader in BufferedReader.
        BufferedReader bufferedReader = 
            new BufferedReader(fileReader);

        while((line = bufferedReader.readLine()) != null) {
            System.out.println(line);
        }   

        // Always close files.
        bufferedReader.close();         
    }
    catch(FileNotFoundException ex) {             
    }
    catch(IOException ex) {
    }

}
	public static void createIndex() {
		System.out.println("Creating Index");

		// Parsing Tweets
		System.out.println("Running Parser");

		TweetParser parser = new TweetParser();
		parser.parseFiles();

		ArrayList<Tweet> tweets = parser.getTweets();

		System.out.println(tweets.size() + " tweets were parsed");

		index = new InvertedIndex();
		
		index.addDocuments(tweets);
		System.out.println(index.getTotalDocuments() + " tweets inside index");

	}

	public static void main(String args[]) {

		// TODO: Load index if exists

		index = MemoryManager.loadIndexState();

		boolean exitflag = false;
		while (!exitflag) {
			printLogo();
			System.out.println("------------------------------------------------0");
			System.out.println("Welcome to the dark side of Tweeagle >:)\t*");
			System.out.println("------------------------------------------------*");
			System.out.println("Available actions: \t\t\t\t*");
			System.out.println("1. Run Crawler\t\t\t\t\t*");
			System.out.println("2. Create Index\t\t\t\t\t*");
			System.out.println("3. Delete Index\t\t\t\t\t*");
			System.out.println("4. Delete a Tweet\t\t\t\t*");
			System.out.println("5. Empty cached Tweets\t\t\t\t*");
			System.out.println("6. Text Search\t\t\t\t\t*");
			System.out.println("7. Phrase Search\t\t\t\t*");
			System.out.println("8. Print Dictionary\t\t\t\t*");
			System.out.println("9. Exit\t\t\t\t\t\t*");
			System.out.println("------------------------------------------------0");
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
				MemoryManager.deleteIndex();
				break;
			}
			case 4: {
				System.out.print("Tweet to delete:");
				scanner.nextLine();
				String tweetname = scanner.nextLine();
				index.deleteDocument(tweetname);
				System.out.println(index.getTotalDocuments() + " tweets inside index");
				break;
			}
			case 5: {
				MemoryManager.eraseCashe();
				index = new InvertedIndex();
				break;
			}
			case 6: {
				System.out.print("Text to search:");
				scanner.nextLine();
				String query = scanner.nextLine();
				QueryProcessing qp = new QueryProcessing(index);
				qp.textSearch(query, 0);
				break;
			}

			case 7: {
				System.out.print("Phrase to search:");
				scanner.nextLine();
				String query = scanner.nextLine();
				QueryProcessing qp = new QueryProcessing(index);
				qp.phraseSearch(query, 0);
				break;
			}
			case 8: {
				index.printIndex();
				break;
			}
			case 9: {
				exitflag = true;
			}
			default: {
				System.out.println("Action not available");

				break;
			}
			}
		}
	}

}
