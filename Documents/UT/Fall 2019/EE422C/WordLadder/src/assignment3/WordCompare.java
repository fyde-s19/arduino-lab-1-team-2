package assignment3;

import java.util.Comparator;

public class WordCompare implements Comparator<String>
{
	char[] target;
	public WordCompare(String s)
	{
		target = s.toCharArray();
	}
	
	@Override
	/**
	 * compares two words based on closeness to target word
	 */
	public int compare(String s1, String s2)
	{
		int count1 = 0, count2 = 0;
		for(int i = 0; i < target.length; i++)
		{
			if(target[i] == s1.charAt(i)) count1++;
			if(target[i] == s2.charAt(i)) count2++;
		}
		
		if(count1 > count2) return -1;
		else if(count1 < count2) return 1;
		else return 0;
	}
}