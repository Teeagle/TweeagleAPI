# TweeagleCore

A search engine for retrieving relevant and credible tweets from Twitter

In the age of social media, issues like fake news and the presence of untrusted sources are flourishing. Having this in mind, we developed Tweeagle, a search engine for retrieving relevant and credible tweets from Twitter. Trough Tweeagle people can search for credible content, get informed and discover users with similar interests. Our engine communicates with the Twitter Stream API to collect tweets, it ranks their relevance and credibility and offers an intuitive interface to interact with. 

## Dependencies:
* TomCat 8.5

## How to Setup 

1. [Download Tweeagle](https://drive.google.com/open?id=1eFpFwb6Yb6AM9K7MU1ky3h7HfvIJPjPc)
2. Deploy *TweeagleCore.war* on a TomCat server
3. Deploy *TweeagleWeb* on the same TomCat server as TweeagleCore
4. Change the *SERVICE_PATH* in *conf.txt* to the running directory of your server. 
5. Run TweeagleCore.jar
6. Select Option 2: Create Index

Congratulations! You can now access Tweeagle at: http://localhost:8080/TweeagleWeb. Happy searching!

### What is TweeagleCorpus

This directory is necessary for the creation of the index. It contains the raw tweets downloaded using the Twitter Stream API. You can collect more tweets by running the Crawler.
