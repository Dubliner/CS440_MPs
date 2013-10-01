/**
 * 
 */
package SearchAlgorithms;

import java.util.ArrayList;
import java.util.Comparator;

import Heuristics.ManhattanDistance;
import Heuristics.MultiGoalDistance;
import Heuristics.NonAdmissiableDistance;
import Heuristics.PreferLeftDistance;
import Heuristics.PreferRightDistance;
import MazeReadIn.Pair;

/* In this case, Pair denotes current coordinates */
public class AstarComparator implements Comparator<Pair<Integer, Integer>>
{
	public double[][] cost;
 	public Pair<Integer, Integer> goal;
	public Pair<Integer, Integer> start;
	public ArrayList<Pair<Integer, Integer>> goals; 
	
	/*Constructor for single goal maze*/
	public AstarComparator(Pair<Integer, Integer> start, Pair<Integer,Integer> goal, double[][] pathCost){
		this.start = start;
		this.goal = goal;
		this.cost = pathCost;
		this.goals = null;
	}
	
	/*Constructor for multi-goal maze*/
	public AstarComparator(Pair<Integer, Integer> start, ArrayList<Pair<Integer, Integer>> goals, double[][] pathCost){
		this.start = start;
		this.goal = null;
		this.cost = pathCost;
		this.goals = goals;
	}
	
	public void setCost(int x, int y, double value){
		this.cost[x][y] = value; 
	}
	public double getCost(int x, int y){
		return this.cost[x][y];
	}
	
	/* Get heuristic for a point when there is only one goal */
	public static double getDistance(Pair<Integer, Integer> start, Pair<Integer, Integer> goal){
		//return PreferLeftDistance.getDistance(start);  
		//return PreferRightDistance.getDistance(start); 
		//return ManhattanDistance.getDistance(start, goal);
		return 0;
	}

	/* Get heuristic for a point when there are multiple goals */
	public static double getDistance(Pair<Integer, Integer> start, ArrayList<Pair<Integer, Integer>> goals)
	{
		return MultiGoalDistance.getDistance(start, goals);
		//return NonAdmissiableDistance.getDistance(start,goals);
	}
	
	
    public int compare(Pair<Integer, Integer> x, Pair<Integer, Integer> y)
    {
    	//Single goal    	 
    	  //double xToGoal = getDistance(x, this.goals.get(0));
   	 	  //double yToGoal = getDistance(y, this.goals.get(0));
    	 //Multi goals
    	 double xToGoal = getDistance(x, this.goals);
    	 double yToGoal = getDistance(y, this.goals);
    	 
    	 double xFromStart = this.cost[x.getFirst()][x.getSecond()];
    	 double yFromStart = this.cost[y.getFirst()][y.getSecond()];
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