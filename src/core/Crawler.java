package core;

import twitter4j.conf.ConfigurationBuilder;
import twitter4j.*;
import twitter4j.json.DataObjectFactory;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import com.google.gson.JsonObject;

/**
 * This is a crawler baby
 */
public class Crawler {

    private TwitterStream twitterStream;
    private int tweetBucketCapacity; // The number of tweets to store per file
    private int tweetBucketFilled; // The number of tweets in the current bucket
    private ArrayList<String> tweetBucket; // The bucket of tweets
    private int tweetBucketsCnt; // The amount of Tweet buckets filled

    public Crawler(String consumerKey,String consumerSecret,String accessToken, String accessTokenSecret){
        ConfigurationBuilder cb = new ConfigurationBuilder();
        cb.setDebugEnabled(true)
                .setOAuthConsumerKey(consumerKey)
                .setOAuthConsumerSecret(consumerSecret)
                .setOAuthAccessToken(accessToken)
                .setOAuthAccessTokenSecret(accessTokenSecret)
                .setJSONStoreEnabled(true);

        TwitterStreamFactory tsf = new TwitterStreamFactory(cb.build());
        this.twitterStream = tsf.getInstance();
        this.tweetBucketCapacity = 100; //default capacity
        this.tweetBucketsCnt=733;
        this.tweetBucket = new ArrayList<>();
    }

    public void crawl(){
        twitterStream.addListener(new StatusListener() {
            @Override
            public void onStatus(Status status) {

                // Avoiding dublicates due to retweets
                if(!status.isRetweet()){
                    //Status To JSON String
                    String statusJson = TwitterObjectFactory.getRawJSON(status);
                    tweetBucket.add(statusJson);
                    tweetBucketFilled++;
                    // Tweet bucket is full
                    if(tweetBucketFilled == tweetBucketCapacity){
                        storeTweetBucket();
                        tweetBucketFilled = 0;
                        tweetBucket.clear();
                        tweetBucketsCnt++;
                    }
                    System.out.println("Current Bucket Count: "+tweetBucketFilled+" Buckets Filled: "+tweetBucketsCnt);
                }
            }

            @Override
            public void onDeletionNotice(StatusDeletionNotice statusDeletionNotice) {

            }

            @Override
            public void onTrackLimitationNotice(int numberOfLimitedStatuses) {

            }

            @Override
            public void onScrubGeo(long userId, long upToStatusId) {

            }

            @Override
            public void onStallWarning(StallWarning warning) {

            }

            @Override
            public void onException(Exception ex) {
                ex.printStackTrace();
                //System.out.println(ex.getStackTrace());
            }
        });

        FilterQuery tweetFilterQuery = new FilterQuery();
        tweetFilterQuery.language(new String[]{"en"});

        // Set location to USA bounding box
        tweetFilterQuery.locations(new double[][]{new double[]{-171.791110603,18.91619},
                new double[]{-66.96466,71.3577635769
                }});

        twitterStream.filter(tweetFilterQuery);
    }

    /**
     * Stores Tweet Bucket in a file
     */
    public void storeTweetBucket(){
        try {
            PrintWriter pw = new PrintWriter(new FileWriter(("Data/"+tweetBucketsCnt+".txt")));
            for (String tweet: tweetBucket ) {
                pw.println(tweet);
            }
            pw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /***
     * Changes default Tweet bucket capacity
     * @param tweetBucketCapacity
     */
    public void setTweetBucketCapacity(int tweetBucketCapacity) {
        this.tweetBucketCapacity = tweetBucketCapacity;
    }
}
