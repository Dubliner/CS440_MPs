package Heuristics;

import java.util.ArrayList;
import java.util.Arrays;

import MazeReadIn.Pair;

public class MultiGoalDistance {

	public static int getDistance(Pair<Integer, Integer> a, Pair<Integer, Integer>[] goalPairs){
		
		ArrayList<Pair<Integer, Integer>> goals = new ArrayList<Pair<Integer, Integer>>(Arrays.asList(goalPairs));//making a deepcopy. arraylist pass by ref in java
		return findShortest(a, goals);
	}
	
	private static int findShortest(Pair<Integer, Integer> curr, ArrayList<Pair<Integer, Integer>> others)
	{
		int totalHeurist = 0;
		int index = -1;
		int shortest = Integer.MAX_VALUE;
		Pair<Integer, Integer> start = curr;
		
		while(others.size() > 0){
			
			shortest = Integer.MAX_VALUE;
			
			for(int i = 0; i < others.size(); i++)
			{
				int mahaDist = Math.abs(start.getFirst() - others.get(i).getFirst()) + Math.abs(start.getSecond() - others.get(i).getSecond()); 
				if (mahaDist < shortest)
				{
					shortest = mahaDist;
					index = i;
				}
			}
			
			start = others.get(index);
			others.remove(index);
			totalHeurist += shortest;
		}
		
		return totalHeurist;
	}
}
