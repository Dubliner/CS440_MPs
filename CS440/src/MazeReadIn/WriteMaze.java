/**
 * 
 */
package MazeReadIn;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * @author zhenchengwang
 * Put original txt file into a string, modified the string and print out the string to a txt file
 */
public class WriteMaze {


	public static char [] orderCount = {'0', '1', '2', '3', '4', '5',
		'6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j',
		'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x',
		'y', 'z', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J',
		'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X',
		'Y', 'Z'};
	
	/* Write solution with maze to a txt file */
	public static void writeSolution(String mazeName, List<String> solution, String output) throws IOException{
		List<String> mazeText = new ArrayList<String>();
		BufferedReader br = new BufferedReader(new FileReader(mazeName));
		String line;
		int mazeHeight = 0;
		int mazeWidth = 0;
		
		/* Go through file read in mazeText */
		while((line = br.readLine()) != null){
			// process the line
			String noNewLine = line.split("\n")[0];
			mazeText.add(noNewLine);
			mazeHeight++;
		}
		br.close();
		mazeWidth = mazeText.get(0).length();
		
		/* Read solution, modify mazeText */
		for(int i=1; i<solution.size()-1; i++){
			// x coord
			int currFirst = Integer.parseInt(solution.get(i).split(",")[0]);
			// y coord
			int currSecond = Integer.parseInt(solution.get(i).split(",")[1]);
			StringBuilder currLine = new StringBuilder(mazeText.get(currSecond));
			currLine.setCharAt(currFirst, '*');
			mazeText.set(currSecond, currLine.toString());			
		}
		String result = new String("");
		for(int i=0; i<mazeText.size(); i++){
			result += mazeText.get(i)+"\n";
		}
		
		/* mazeText is ready to output */
		PrintWriter writer = new PrintWriter(output);
		writer.println(result);
		writer.close();
		
		
	}

	public static void writeSolution(String mazeName, List<String> solution, String output, ArrayList<Pair<Integer, Integer>> reachedGoals) throws IOException{
		List<String> mazeText = new ArrayList<String>();
		BufferedReader br = new BufferedReader(new FileReader(mazeName));
		String line;
		int mazeHeight = 0;
		int mazeWidth = 0;
		
		/* Go through file read in mazeText */
		while((line = br.readLine()) != null){
			// process the line
			String noNewLine = line.split("\n")[0];
			mazeText.add(noNewLine);
			mazeHeight++;
		}
		br.close();
		mazeWidth = mazeText.get(0).length();
		
		/* Read solution, modify mazeText */
		for(int i=1; i<solution.size()-1; i++){
			// x coord
			int currFirst = Integer.parseInt(solution.get(i).split(",")[0]);
			// y coord
			int currSecond = Integer.parseInt(solution.get(i).split(",")[1]);
			StringBuilder currLine = new StringBuilder(mazeText.get(currSecond));
			currLine.setCharAt(currFirst, '.');
			mazeText.set(currSecond, currLine.toString());			
		}
		
		//set reached goals ordering
		for(int i=0; i<reachedGoals.size(); i++){
			// x coord
			int currFirst = reachedGoals.get(i).getFirst();
			// y coord
			int currSecond = reachedGoals.get(i).getSecond();
			StringBuilder currLine = new StringBuilder(mazeText.get(currSecond));
			currLine.setCharAt(currFirst, orderCount[i]);
			mazeText.set(currSecond, currLine.toString());			
		}
		
		String result = new String("");
		for(int i=0; i<mazeText.size(); i++){
			result += mazeText.get(i)+"\n";
		}
		
		/* mazeText is ready to output */
		PrintWriter writer = new PrintWriter(output);
		writer.println(result);
		writer.close();
		
		
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
