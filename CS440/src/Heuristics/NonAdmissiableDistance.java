package Heuristics;

import java.util.ArrayList;

import MazeReadIn.Pair;

public class NonAdmissiableDistance {
	public static int getDistance(Pair<Integer, Integer> a, ArrayList<Pair<Integer, Integer>> goalPairs){
		int dist = 0;
		for(Pair<Integer, Integer> g:goalPairs)
		{

			int d = Math.abs(a.getFirst() - g.getFirst()) + Math.abs(a.getSecond() - g.getSecond());
			dist += d;
		}
		return dist;
	}

}
