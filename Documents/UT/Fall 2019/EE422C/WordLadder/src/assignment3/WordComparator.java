package assignment3;

import java.util.Comparator;

public class WordComparator implements Comparator<String>{
	
	char[] end;
	
	public WordComparator(String s) {
		end = s.toCharArray();
	}

	@Override
	public int compare(String s1, String s2) {
		// TODO Auto-generated method stub
		int len = end.length;
		
		int countS1 = 0, countS2 = 0;
		
		for (int i = 0; i < len; i++) {
			if (end[i] == s1.charAt(i)) 
				countS1++;
			if (end[i] == s2.charAt(i)) 
				countS2++;
		}
		
		if (countS1 == countS2)
			return 0;
		else if (countS1 > countS2) 
			return -1;
		else 
			return 1;
	}
	
}
