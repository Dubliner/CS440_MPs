/**
 * 
 */
package SearchAlgorithms;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static java.lang.System.*;
import MazeReadIn.Maze;
import MazeReadIn.Pair;
import MazeReadIn.ReadMaze;


/**
 * @author zhenchengwang
 * Everything is the same with DFS & BFS, just change the data structure to priorityqueue
 * Data structure:
 * 			1, table to mark visited
 * 			2, dictionary to reconstruct the path: Map<String, String> myMap
 * 			3, path: List<String> myPath
 * 
 * Result:
 * 		1, solution
 * 		2, # of nodes expanded
 * 		3, maximum tree depth
 * 		4, maximum size of frontier
 *
 */
public class GBS2 {

	/* 
	 * Find best child, mark all children visited, append path
	 * return: best child
	 * */	
	public static Pair<Integer, Integer> findBestChild(Pair<Integer, Integer> curr, int[][] canTravel, List<String> myPath, int goalX, int goalY){
		int currX = curr.getFirst();
		int currY = curr.getSecond();
		
		List<Integer> childrenDistances = new ArrayList<Integer>();
		List<Pair<Integer, Integer>> children = new ArrayList<Pair<Integer, Integer>>();
		if(currX+1<canTravel.length && canTravel[currX+1][currY]==0){
			Pair<Integer, Integer> rightChild = new Pair<Integer, Integer>(currX+1, currY);
			children.add(rightChild);
			childrenDistances.add(getDistance(rightChild, goalX, goalY));
			//canTravel[currX+1][currY] = 1;
		}
		if(currY+1<canTravel.length && canTravel[currX][currY+1]==0){
			Pair<Integer, Integer> upChild = new Pair<Integer, Integer>(currX, currY+1);
			children.add(upChild);
			childrenDistances.add(getDistance(upChild, goalX, goalY));
			//canTravel[currX][currY+1] = 1;
		}
		if(currX-1>=0 && canTravel[currX-1][currY]==0){
			Pair<Integer, Integer> leftChild = new Pair<Integer, Integer>(currX-1, currY);
			children.add(leftChild);
			childrenDistances.add(getDistance(leftChild, goalX, goalY));
			//canTravel[currX-1][currY] = 1;
		}
		if(currY-1>=0 && canTravel[currX][currY-1]==0){
			Pair<Integer, Integer> downChild = new Pair<Integer, Integer>(currX, currY-1);
			children.add(downChild);
			childrenDistances.add(getDistance(downChild, goalX, goalY));
			//canTravel[currX][currY-1] = 1;
		}
		out.println(childrenDistances.size());
		int minValue = childrenDistances.get(0);
		int minIndex = 0;
		for(int i=0; i<childrenDistances.size(); i++){
			if(childrenDistances.get(i)<minValue){
				minIndex = i;
			}
		}
		
		return children.get(minIndex);
	}
	
	/*
	 * Get manhatten distance from current point to goal point 
	 * return a non-negative value */
	public static int getDistance(Pair<Integer, Integer> curr, int goalX, int goalY){	
		int currX = curr.getFirst();
		int currY = curr.getSecond();
		return Math.abs(goalX-currX) + Math.abs(goalY-currY);
	}
	
	
	/* Main function that returns a path */
	public static List<String> GBS(Maze myMaze){
		int mazeWidth = myMaze.maze.length;
		int mazeHeight = myMaze.maze[0].length;
		
		int[][] canTravel = new int[mazeWidth][mazeHeight];		
		List<String> myPath = new ArrayList<String>();
		
		// define current point
		Pair<Integer, Integer> curr = new Pair(myMaze.start[0], myMaze.start[1]);
		// get goal coordinates:
		int goalX = myMaze.goal[0];
		int goalY = myMaze.goal[1];		
		
		for(int i=0; i<mazeWidth; i++){
			for(int j=0; j<mazeHeight; j++){
				if(myMaze.maze[i][j] == 1)
					canTravel[i][j] = 1;
				else
					canTravel[i][j] = 0;
			}
		}		
		
		
		// build path
		while(/*have not found the end point */ getDistance(curr, goalX, goalY)>0){
			
			int currX = curr.getFirst();
			int currY = curr.getSecond();
			out.println(currX+":"+currY);
			myPath.add(""+currX+","+currY+"");
			// update current, mark current as visited
			canTravel[currX][currY] = 1;
			curr = findBestChild(curr, canTravel, myPath, goalX, goalY);
		}
		return myPath;
	}
	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		String input = "src/MazeReadIn/bigMaze";//console().readLine();
		Maze myMaze = ReadMaze.parseMaze(input); // className.methodName
		
		List<String> myPath = GBS(myMaze);
		out.println(myPath.size());
		for(int i=0; i<myPath.size(); i++){
			out.println(myPath.get(i));
		}
	}

}
