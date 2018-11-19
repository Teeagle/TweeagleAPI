/***
 * The Controller of the Tweeagle Core
 */
package core;

import java.util.Scanner;

public class TweeagleController {

    public static void runCrawler(){
        System.out.println("Running Crawler");
        // APP:
        Crawler crawler = new Crawler("FRHoEIlNIAdGjNKKGzVyzcpER",
                "AKOZgkU3XmfQqEaUsHLCFyJzVlM8wPqsdFdY4NiqDkS3BMTYc3",
                "529412295-980juv52ABQEbi72Okc4edAvvYYEHUnC8nNP07an",
                "Xd61PK5e3WJYw0jaStYPe5zGjPlbvgiP1Noc9HxLnwXI8");
        crawler.setTweetBucketCapacity(1000); // Tweets per bucket-file
        crawler.crawl();
    }

    public static void main(String args[]){
    	
        System.out.println("Welcome to the dark side of Tweeagle >:)");
        System.out.println("Available actions: ");
        System.out.println("-------------------");
        System.out.println("1. Run Crawler");
        System.out.println("2. ........\n");
        System.out.print("Type action number: ");
        Scanner scanner = new Scanner(System.in);
        int action = scanner.nextInt();
        if (action == 1){
            runCrawler();
        }else{
            System.out.println("Action not available");
        }
    }
}
