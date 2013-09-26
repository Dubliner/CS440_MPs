/**
 * 
 */
package SearchAlgorithms;

import java.util.Comparator;

import MazeReadIn.Pair;

/* In this case, Pair denotes current coordinates */
public class AstarComparator implements Comparator<Pair<Integer, Integer>>
{
	
	public Pair<Integer, Integer> goal;
	public Pair<Integer, Integer> start;
	public AstarComparator(Pair<Integer, Integer> start, Pair<Integer,Integer> goal){
		this.start = start;
		this.goal = goal;
	}
	
	/* Get manhattan distance between two points */
	public static int getDistance(Pair<Integer, Integer> a, Pair<Integer, Integer> b){
		return Math.abs(a.getFirst()-b.getFirst())+Math.abs(a.getSecond()-b.getSecond());
	}
	
	
    public int compare(Pair<Integer, Integer> x, Pair<Integer, Integer> y)
    {
        int xToGoal = getDistance(x, this.goal);
        int xFromStart = getDistance(x, this.start);
        int yToGoal = getDistance(y, this.goal);
        int yFromStart = getDistance(y, this.start);
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