package SearchAlgorithms;

import java.util.ArrayList;
import java.util.List;

public class ArrayListHelper {

	public static ArrayList<String> add(List<String> a, List<String> b)
	{
		ArrayList<String> ret = new ArrayList<String>();
		for(String s:a)
		{
			ret.add(s);
		}
		
		for(String s:b)
		{
			ret.add(s);
		}
		return ret;
	}
}
