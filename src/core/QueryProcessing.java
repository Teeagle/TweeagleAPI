package core;

public class QueryProcessing {
	
	// Find index from controller
	static InvertedIndex index = TweeagleController.index;
	
	public static void printIndex() {
		System.out.println("index= " + index.toString());
	}
}
