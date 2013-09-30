package Heuristics;

import MazeReadIn.Pair;

public class ManhattanDistance {

	public static int getDistance(Pair<Integer, Integer> start, Pair<Integer, Integer> goal)
	{
		return Math.abs(start.getFirst() - goal.getFirst()) + Math.abs(start.getSecond() - goal.getSecond()); 
	}
}
