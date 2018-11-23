package core;

import java.io.Serializable;
import java.util.ArrayList;

public class IndexTermInfo implements Serializable{
	private int df;
	private float idf;

	public ArrayList<Integer> tweetIds;

	public IndexTermInfo() {
		tweetIds = new ArrayList<>();
	}

	public IndexTermInfo(int df, float idf, ArrayList<Integer> tweetIds) {
		this.df = df;
		this.idf = idf;
		this.tweetIds = tweetIds;
	}
	
	public void addTweetID(int id) {
		tweetIds.add(id);
	}
	
	public void deleteTweetID(int id) {
		tweetIds.remove(id);
	}
	
	public int getDf() {
		return df;
	}

	public void setDf(int df) {
		this.df = df;
	}

	public float getIdf() {
		return idf;
	}

	public void setIdf(float idf) {
		this.idf = idf;
	}

	public ArrayList<Integer> getTweetIds() {
		return tweetIds;
	}

	public void setTweetIds(ArrayList<Integer> tweetIds) {
		this.tweetIds = tweetIds;
	}

	public void increaseDF() {
		this.df++;
	}

	public void decreaseDF() {
		this.df--;
	}

	@Override
	public String toString() {
		return "IndexTermInfo [df=" + df + ", idf=" + idf + ", tweetIds=" + tweetIds + "]";
	}
}
