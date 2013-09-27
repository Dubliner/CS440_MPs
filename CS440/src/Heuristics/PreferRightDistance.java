package Heuristics;

import MazeReadIn.Pair;

public class PreferRightDistance {

	public static double getDistance(Pair<Integer, Integer> curr)
	{
		return 1.0 / Math.exp(curr.getFirst());	
	}
}
