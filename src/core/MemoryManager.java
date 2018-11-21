package core;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class MemoryManager {
	private static String INDEX_DIR = "indexes/";

	/**
	 * Saves a serializable tweet to memory.
	 * 
	 * @param serObj
	 * @param filepath
	 */
	public static void writeTweetToFile(Tweet serObj, String filepath) {

		try {
			FileOutputStream fileOut = new FileOutputStream(filepath);
			ObjectOutputStream objectOut = new ObjectOutputStream(fileOut);
			objectOut.writeObject(serObj);
			objectOut.close();
//			System.out.println("The Object  was successfully written to a file");

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public static Tweet readTweetFromFile(String filepath) {

		try {
			FileInputStream fileIn = new FileInputStream(filepath);
			ObjectInputStream objectIn = new ObjectInputStream(fileIn);

			Tweet tweet = (Tweet) objectIn.readObject();

			// System.out.println("The Object has been read from the file. " + tweet.getDocID() + " " + tweet.getText() + " obj=" + tweet.getDictionary());
			
			objectIn.close();
			return tweet;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

	public static boolean storeIndex(InvertedIndex index) {
		// Check if the indexes directory does not exist and create it
		File directory = new File(INDEX_DIR);

		if (!directory.exists()) {
			directory.mkdir();
			// If you require it to make the entire directory path including parents,
			// use directory.mkdirs(); here instead.
		}

		try {
			FileOutputStream fileOut = new FileOutputStream(INDEX_DIR + "index");
			ObjectOutputStream objectOut = new ObjectOutputStream(fileOut);
			objectOut.writeObject(index);
			objectOut.close();
			System.out.println("The Object  was succesfully written to a file");
		} catch (Exception ex) {
			ex.printStackTrace();
			return false;
		}

		return true;
	}

	public static InvertedIndex loadIndex() {
		try {
			FileInputStream fileIn = new FileInputStream(INDEX_DIR + "index");
			ObjectInputStream objectIn = new ObjectInputStream(fileIn);

			InvertedIndex storedIndex = (InvertedIndex) objectIn.readObject();

			System.out.println("The Object has been read from the file.\n" + storedIndex.toString());
			objectIn.close();
			return storedIndex;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

}
