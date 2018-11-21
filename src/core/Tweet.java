package core;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.StringTokenizer;
import java.util.TreeMap;

public class Tweet implements Serializable {

	// default serialVersion id
	private static long serialVersionUID;

	// DocumentID
	private int docID;

	// Tweet Attributes
	private String created_at;
	private String id;
	private String text;

	// Tweet Statistics
	private int quoteCnt;
	private int replyCnt;
	private int retweetCnt;
	private int favoriteCnt;

	// User Meta Information
	private String usertag;
	private boolean isVerified;
	private int userFriendsCnt; // Following
	private int userFollowersCnt;
	private int userFavoritesCnt;
	private int userStatusesCnt;
	private String userProfileImgURL;

	// Dictionary Holder
	private TreeMap<String, TermInfo> dictionary;

	public Tweet() {
		dictionary = new TreeMap<>();
	}

	public Tweet(int docID, String created_at, String id, String text, int quoteCnt, int replyCnt, int retweetCnt,
			int favoriteCnt, String usertag, boolean verified, int friendsCnt, int followersCnt, int favoritesCnt,
			int statusesCnt, String profileImgURL) {
		dictionary = new TreeMap<>();

		this.docID = docID;
		this.created_at = created_at;
		this.id = id;
		this.text = text;
		this.quoteCnt = quoteCnt;
		this.replyCnt = replyCnt;
		this.retweetCnt = retweetCnt;
		this.favoriteCnt = favoriteCnt;
		this.usertag = usertag;
		this.isVerified = verified;
		this.userFriendsCnt = friendsCnt;
		this.userFollowersCnt = followersCnt;
		this.userFavoritesCnt = favoritesCnt;
		this.userStatusesCnt = statusesCnt;
		this.userProfileImgURL = profileImgURL;
	}

	public String getCreated_at() {
		return created_at;
	}

	public void setCreated_at(String created_at) {
		this.created_at = created_at;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public int getQuoteCnt() {
		return quoteCnt;
	}

	public void setQuoteCnt(int quoteCnt) {
		this.quoteCnt = quoteCnt;
	}

	public int getReplyCnt() {
		return replyCnt;
	}

	public void setReplyCnt(int replyCnt) {
		this.replyCnt = replyCnt;
	}

	public int getRetweetCnt() {
		return retweetCnt;
	}

	public void setRetweetCnt(int retweetCnt) {
		this.retweetCnt = retweetCnt;
	}

	public int getFavoriteCnt() {
		return favoriteCnt;
	}

	public void setFavoriteCnt(int favoriteCnt) {
		this.favoriteCnt = favoriteCnt;
	}

	public String getUsertag() {
		return usertag;
	}

	public void setUsertag(String usertag) {
		this.usertag = usertag;
	}

	public boolean getIsVerified() {
		return isVerified;
	}

	public void setIsVerified(boolean verified) {
		this.isVerified = verified;
	}

	public int getUserFriendsCnt() {
		return userFriendsCnt;
	}

	public void setUserFriendsCnt(int friendsCnt) {
		this.userFriendsCnt = friendsCnt;
	}

	public int getUserFollowersCnt() {
		return userFollowersCnt;
	}

	public void setUserFollowersCnt(int followersCnt) {
		this.userFollowersCnt = followersCnt;
	}

	public int getUserFavoritesCnt() {
		return userFavoritesCnt;
	}

	public void setUserFavoritesCnt(int favoritesCnt) {
		this.userFavoritesCnt = favoritesCnt;
	}

	public int getUserStatusesCnt() {
		return userStatusesCnt;
	}

	public void setUserStatusesCnt(int statusesCnt) {
		this.userStatusesCnt = statusesCnt;
	}

	public String getUserProfileImgURL() {
		return userProfileImgURL;
	}

	public void setUserProfileImgURL(String profileImgURL) {
		this.userProfileImgURL = profileImgURL;
	}

	public int getDocID() {
		return docID;
	}

	public void setDocID(int docID) {
		this.docID = docID;
		this.serialVersionUID = docID;
	}

	public TreeMap<String, TermInfo> getDictionary() {
		return dictionary;
	}

	@Override
	public String toString() {
		return "Tweet [docID=" + docID + ", created_at=" + created_at + ", id=" + id + ", text=" + text + ", quoteCnt="
				+ quoteCnt + ", replyCnt=" + replyCnt + ", retweetCnt=" + retweetCnt + ", favoriteCnt=" + favoriteCnt
				+ ", usertag=" + usertag + ", verified=" + isVerified + ", friendsCnt=" + userFriendsCnt
				+ ", followersCnt=" + userFollowersCnt + ", favoritesCnt=" + userFavoritesCnt + ", statusesCnt="
				+ userStatusesCnt + ", profileImgURL=" + userProfileImgURL + "]";
	}

	/**
	 * Add term to tweet local dictionary
	 * 
	 * @param token
	 * @param pos
	 */
	public void addTerm(String token, int pos) {
		if (dictionary.containsKey(token)) {
			// if the key exists, update it
			dictionary.get(token).increaseTF();
			dictionary.get(token).addPosition(pos);
		} else {
			// if the key does not exist, add it
			TermInfo info = new TermInfo();

			info.setTf(1);
			info.addPosition(pos);

			dictionary.put(token, info);
		}
	}
	
	public void createDictionary(){
		StringTokenizer tokens = new StringTokenizer(this.text, " .,';?\\\"!$%^&*-–—+=_()<>|/\\\\|[]`~\n\t");

		int pos = 0;
		while (tokens.hasMoreTokens()) {
			String token = tokens.nextToken();

			// Convert character to lower case
			token = token.toLowerCase();

			addTerm(token, pos++);
		}
	}

}
