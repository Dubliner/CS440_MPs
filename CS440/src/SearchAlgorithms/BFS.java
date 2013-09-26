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
import java.util.PriorityQueue;
import java.util.Queue;

import MazeReadIn.Maze;
import MazeReadIn.Pair;
import MazeReadIn.ReadMaze;
import static java.lang.System.*;



/**
 * @author zhenchengwang
 * Data structure: 1, queue -> Queue<Pair<Integer, Integer>> myQueue
 * 				   2, table to mark visited ->  int[][] canTravel 
 * 				   3, dictionary to remember the path -> Map<Pair<Integer, Integer>, Pair<Integer, Integer>> myMap
 * 				   4, path -> List<Pair<Integer, Integer>> myPath
 * Result:
 * 	1, solution
 *  2, number of nodes expanded: # nodes on path - # marked visited
 *  3, maximum tree depth: BFS: path length, DFS: stackSizeMax
 *  4, maximum size of frontier BFS: queueSizeMax, DFS: 1
 *
 */
public class BFS {

	/* 
	 * canTravel: push adjacent node into queue, mark them as visited */
	public static void pushAdjacent(Queue<Pair<Integer, Integer>> myQueue, int[][] canTravel, Pair<Integer, Integer> curr, Map<String, String> myMap){
		
			int currX = curr.getFirst();
			int currY = curr.getSecond();
			 
			if(currX+1<canTravel.length && canTravel[currX+1][currY]==0){
				Pair<Integer, Integer> rightChild = new Pair(currX+1, currY);
				String parent = ""+currX+","+currY+"";
				String child = 	""+(currX+1)+","+currY+"";	
				myMap.put(child, parent);
				myQueue.offer(rightChild);
			}
			if(currY+1<canTravel[0].length && canTravel[currX][currY+1]==0){
				Pair<Integer, Integer> upChild = new Pair(currX, currY+1);
				String parent = ""+currX+","+currY+"";
				String child = 	""+(currX)+","+(currY+1)+"";	
				myMap.put(child, parent);
				myQueue.offer(upChild);
			}
			if(currX-1>=0 && canTravel[currX-1][currY]==0){
				Pair<Integer, Integer> leftChild = new Pair(currX-1, currY);
				String parent = ""+currX+","+currY+"";
				String child = 	""+(currX-1)+","+currY+"";	
				myMap.put(child, parent);
				myQueue.offer(leftChild);
			}
			if(currY-1>=0 && canTravel[currX][currY-1]==0){
				Pair<Integer, Integer> downChild = new Pair(currX, currY-1);
				String parent = ""+currX+","+currY+"";
				String child = 	""+(currX)+","+(currY-1)+"";	
				myMap.put(child, parent);
				myQueue.offer(downChild);
			}
				
				
		
	}
	
	/* findGoal: check if we find the goal */
	
	/* BFS Main function: */
	public static List<String> BFS(Maze myMaze){
		int mazeWidth = myMaze.maze.length;
		int mazeHeight = myMaze.maze[0].length;	
		
		Queue<Pair<Integer, Integer>> myQueue = new LinkedList<Pair<Integer, Integer>>(); 
		int[][] canTravel = new int[mazeWidth][mazeHeight];
		Map<String, String> myMap = new HashMap<String, String>();
		List<String> myPath = new ArrayList<String>();
		
		// initialization queue, push the start point into queue, initial canTravel:
		Pair<Integer, Integer> curr = new Pair(myMaze.start[0], myMaze.start[1]);
		for(int i=0; i<mazeWidth; i++){
			for(int j=0; j<mazeHeight; j++){
				if(myMaze.maze[i][j] == 1)
					canTravel[i][j] = 1;
				else
					canTravel[i][j] = 0;
			}
		}
		myQueue.offer( curr );
		
		while(myQueue.size() != 0){
			// pop one element from queue, mark it visited
			Pair checkCurr = myQueue.poll();
			int currX = (int) checkCurr.getFirst();
			int currY = (int) checkCurr.getSecond();			
			canTravel[currX][currY] = 1;
			
			// check if we reach the goal
			int goalX = myMaze.goal[0];
			int goalY = myMaze.goal[1];		
			
			// if so terminate
			if(currX == goalX && currY == goalY )
				break;			
			// if not push unvisited adjacents into queue, update dictionary, so we can find our path when we reach the goal:
			else{
				pushAdjacent(myQueue, canTravel, checkCurr, myMap);				
			}
		}
		
		// we are out of the loop, now report the path we found
			// initialize initial key: the 
		String currKey = ""+myMaze.goal[0]+","+myMaze.goal[1]+"";
		myPath.add(currKey);
		int count = 0;
		while(myMap.containsKey(currKey)){
			currKey = myMap.get(currKey);
			myPath.add(currKey);
			count++;
		}		
		return myPath;
	}
	
	
	
	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		out.println("Please enter the maze you would like to run:");
		String input = "src/MazeReadIn/smallMaze";//console().readLine();
		Maze myMaze = ReadMaze.parseMaze(input); // className.methodName
		List<String> result = BFS(myMaze);
		
		for(int i=0; i<result.size(); i++){			
			out.println(result.get(i));
		}
		out.println(result.size());
	}

}
