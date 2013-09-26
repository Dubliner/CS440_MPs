/**
 * 
 */
package MazeReadIn;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import static java.lang.System.*;
/**
 * @author zhenchengwang
 *
 */
public class ReadMaze {

	/* Parse maze: 
	 * Read in a txt file and return a 2D array
	 * */
	public static Maze parseMaze(String filename) throws IOException{
		/* Parse to lines of strings */
		List<String> mazeText = new ArrayList<String>();
		BufferedReader br = new BufferedReader(new FileReader(filename));
		String line;
		int mazeHeight = 0;
		int mazeWidth = 0;
		while ((line = br.readLine()) != null) {
		    // process the line.
			String noNewLine = line.split("\n")[0];
			mazeText.add(noNewLine);
			mazeHeight++;
		}
		br.close();
		mazeWidth = mazeText.get(0).length();
		out.println("mazeHeight is " + mazeHeight);
		out.println("mazeWidth is " + mazeWidth);
		/* Get the size of the maze, allocate int maze */
		int [][] maze = new int[mazeWidth][mazeHeight];
		/* Find entrance and exit of the maze, mark walls and paths */
		int [] start = new int[2]; // 0: width, 1: height 
		int [] goal = new int[2]; // 0: width, 1: height
		String a = "00";
		
		for(int i=0; i<mazeWidth; i++){
			for(int j=0; j<mazeHeight; j++){			
				char curr = mazeText.get(j).charAt(i);
				if(curr == '.'){ // entrance: s
					maze[i][j] = 2;
					start[0] = i;
					start[1] = j;
				}
				else if(curr == 'P'){ // exit: g
					maze[i][j] = 3;
					goal[0] = i;
					goal[1] = j;
				}
				else if(curr == '%'){ // walls: 1
					maze[i][j] = 1;
				}
				else{ // path
					maze[i][j] = 0;					
				}
			}
		}
		
		Maze myMaze = new Maze(start, goal, maze);
		return myMaze;
	}
	
	
	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		// It is the path from the java project 
		parseMaze("src/MazeReadIn/smallMaze");
	}

}
