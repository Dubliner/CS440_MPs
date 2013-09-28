/**
 * 
 */
package SearchAlgorithms;

import java.util.Comparator;

import Heuristics.PreferLeftDistance;
import Heuristics.PreferRightDistance;
import MazeReadIn.Pair;

/* In this case, Pair denotes current coordinates */
public class AstarComparator implements Comparator<Pair<Integer, Integer>>
{
	public int[][] cost;
 	public Pair<Integer, Integer> goal;
	public Pair<Integer, Integer> start;
	public AstarComparator(Pair<Integer, Integer> start, Pair<Integer,Integer> goal, int[][] pathCost){
		this.start = start;
		this.goal = goal;
		this.cost = pathCost;
	}
	
	public void setCost(int x, int y, int value){
		this.cost[x][y] = value; 
	}
	public int getCost(int x, int y){
		return this.cost[x][y];
	}
	
	/* Get manhattan distance between two points */
	public static double getDistance(Pair<Integer, Integer> a, Pair<Integer, Integer> b){
		return PreferLeftDistance.getDistance(a);
	}
	
	
    public int compare(Pair<Integer, Integer> x, Pair<Integer, Integer> y)
    {
//        
    	
    	 double xToGoal = getDistance(x, this.goal);
    	 double yToGoal = getDistance(y, this.goal);
    	 int xFromStart = this.cost[x.getFirst()][x.getSecond()];
    	 int yFromStart = this.cost[y.getFirst()][y.getSecond()];
    	 if (xToGoal+xFromStart < yToGoal+yFromStart)
    	 {
    		 return -1;
    	 }
    	 if (xToGoal+xFromStart > yToGoal+yFromStart)
    	 {
    		 return 1;
    	 }
    	 return 0;
    }
}