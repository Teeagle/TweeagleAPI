package core;

import java.util.ArrayList;
import java.util.TreeMap;

public class Tweet {

	// DocumentID
	private int docID;

	private String created_at;
	private String id;
	private String text;

	private int quoteCnt;
	private int replyCnt;
	private int retweetCnt;
	private int favoriteCnt;

	private String usertag;
	private int verified;
	private int friendsCnt; // Following
	private int followersCnt;
	private int favoritesCnt;
	private int statusesCnt;
	private String profileImgURL;

	// Dictionary Holder
	private TreeMap<String, TermInfo> dictionary;

	public Tweet() {
		dictionary = new TreeMap<>();
	}

	public Tweet(int docID, String created_at, String id, String text, int quoteCnt, int replyCnt, int retweetCnt,
			int favoriteCnt, String usertag, int verified, int friendsCnt, int followersCnt, int favoritesCnt,
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
		this.verified = verified;
		this.friendsCnt = friendsCnt;
		this.followersCnt = followersCnt;
		this.favoritesCnt = favoritesCnt;
		this.statusesCnt = statusesCnt;
		this.profileImgURL = profileImgURL;
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

	public int getVerified() {
		return verified;
	}

	public void setVerified(int verified) {
		this.verified = verified;
	}

	public int getFriendsCnt() {
		return friendsCnt;
	}

	public void setFriendsCnt(int friendsCnt) {
		this.friendsCnt = friendsCnt;
	}

	public int getFollowersCnt() {
		return followersCnt;
	}

	public void setFollowersCnt(int followersCnt) {
		this.followersCnt = followersCnt;
	}

	public int getFavoritesCnt() {
		return favoritesCnt;
	}

	public void setFavoritesCnt(int favoritesCnt) {
		this.favoritesCnt = favoritesCnt;
	}

	public int getStatusesCnt() {
		return statusesCnt;
	}

	public void setStatusesCnt(int statusesCnt) {
		this.statusesCnt = statusesCnt;
	}

	public String getProfileImgURL() {
		return profileImgURL;
	}

	public void setProfileImgURL(String profileImgURL) {
		this.profileImgURL = profileImgURL;
	}

	public int getDocID() {
		return docID;
	}

	public void setDocID(int docID) {
		this.docID = docID;
	}

	@Override
	public String toString() {
		return "Tweet [docID=" + docID + ", created_at=" + created_at + ", id=" + id + ", text=" + text + ", quoteCnt="
				+ quoteCnt + ", replyCnt=" + replyCnt + ", retweetCnt=" + retweetCnt + ", favoriteCnt=" + favoriteCnt
				+ ", usertag=" + usertag + ", verified=" + verified + ", friendsCnt=" + friendsCnt + ", followersCnt="
				+ followersCnt + ", favoritesCnt=" + favoritesCnt + ", statusesCnt=" + statusesCnt + ", profileImgURL="
				+ profileImgURL + "]";
	}

}
