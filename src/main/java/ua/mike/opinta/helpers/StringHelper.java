package ua.mike.opinta.helpers;

import java.util.ArrayList;
import java.util.List;

public class StringHelper {
	public static List<String> getListOfStringBySplitter(String string, String splitter) {
		List<String> commandWords = new ArrayList<String>();
		
		String[] splittedWords = string.trim().split(splitter);
		
		for (int i = 0; i < splittedWords.length; i++) {
			String currentValue = splittedWords[i];
			if (currentValue.isEmpty()) {
				continue;
			}
			
			commandWords.add(currentValue.trim());
		}
		
		return commandWords;
	}
	
	public static String getStringFromListBySplitter(List<String> list, String splitter) {
		String result = "";
		
		if (list.size() == 0) {
			return result;
		}
		
		for (String string : list) {
			result += splitter + string;
		}
		
		result = result.substring(splitter.length());
		
		return result;
	}
}
