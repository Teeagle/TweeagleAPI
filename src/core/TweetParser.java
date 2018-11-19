package core;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.*;

public class TweetParser {

	public static void main(String[] args) {
		File f = new File("0.txt");
		FileReader fileReader = null;
		
		String line;
		int count = 1;
		
		try {
			BufferedReader bufferedReader = new BufferedReader(new FileReader(f));
			JsonParser jsonParser = new JsonParser();
			JsonElement jsonTree = null;
			if ((line = bufferedReader.readLine()) != null) {
				jsonTree = jsonParser.parse(line);
			}
			JsonObject jsonObject = jsonTree.getAsJsonObject();
			JsonElement user = jsonObject.get("user");
			JsonElement name = null;
			if (user.isJsonObject()) {
				JsonObject f2Obj = user.getAsJsonObject();

				name = f2Obj.get("name");
			}
			JsonElement text = jsonObject.get("text");
			System.out.println(text + " " + name);
			bufferedReader.close();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}
}
