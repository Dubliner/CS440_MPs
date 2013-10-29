/**
 * 
 */


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author zhenchengwang
 *
 */
public class test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String s = "1 2 3 4 5\n";
		String[] result = s.split("\n")[0].split(" ");
		Arrays.asList(result);
		List<Integer> intResult = new ArrayList<Integer>();
		for(String curr:result) intResult.add(Integer.valueOf(curr)); 
		
	}

}
