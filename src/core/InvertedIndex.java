package core;

import java.util.TreeMap;

public class InvertedIndex {

	// Dictionary Holder
	private TreeMap<String, IndexTermInfo> dictionary;

	public InvertedIndex() {
		dictionary = new TreeMap<String, IndexTermInfo>();
	}

	public boolean addTerm() {
		return false;
	}

	public boolean deleteTerm() {
		return false;
	}

	public boolean addDocument() {
		return false;

	}

	public boolean deleteDocument() {
		return false;
	}

	public boolean storeIndex() {
		return false;
	}

	public boolean loadIndex() {
		return false;
	}

	public void printIndex() {

	}

}
