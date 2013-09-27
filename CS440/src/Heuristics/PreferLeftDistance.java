package Heuristics;

import MazeReadIn.Pair;

public class PreferLeftDistance {

	public static double getDistance(Pair<Integer, Integer> curr)
	{
		return Math.exp(curr.getFirst());	
	}
}
