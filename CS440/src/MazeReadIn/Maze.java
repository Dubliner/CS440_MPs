/**
 * 
 */
package MazeReadIn;

import java.util.ArrayList;

/**
 * @author zhenchengwang
 *
 */
public class Maze {
	public int [] start;
	public ArrayList<Pair<Integer, Integer>> goals;
	public int [][] maze;
	
	public Maze(int[] myStart, ArrayList<Pair<Integer, Integer>> myGoal, int[][] myMaze){
		this.start = myStart;
		this.goals = myGoal;
		this.maze = myMaze;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
