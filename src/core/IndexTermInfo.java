package core;

import java.util.ArrayList;

public class IndexTermInfo {
	private int tf;
	private int df;

	private float tf_idf;
	private float idf;

	private ArrayList<Integer> tweetIds;

	public IndexTermInfo() {

	}

	public IndexTermInfo(int tf, int df, float tf_idf, float idf, ArrayList<Integer> tweetIds) {
		this.tf = tf;
		this.df = df;
		this.tf_idf = tf_idf;
		this.idf = idf;
		this.tweetIds = tweetIds;
	}

	public int getTf() {
		return tf;
	}

	public void setTf(int tf) {
		this.tf = tf;
	}

	public int getDf() {
		return df;
	}

	public void setDf(int df) {
		this.df = df;
	}

	public float getTf_idf() {
		return tf_idf;
	}

	public void setTf_idf(float tf_idf) {
		this.tf_idf = tf_idf;
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

	@Override
	public String toString() {
		return "IndexTermInfo [tf=" + tf + ", df=" + df + ", tf_idf=" + tf_idf + ", idf=" + idf + ", tweetIds="
				+ tweetIds + "]";
	}
}
