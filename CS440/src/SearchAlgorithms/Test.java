package SearchAlgorithms;

// Test.java
import java.util.Comparator;
import java.util.PriorityQueue;

import MazeReadIn.Pair;


// Test priority queue with customed comparator
public class Test
{
    public static void main(String[] args)
    {
    	Pair<Integer, Integer> myGoal = new Pair<Integer, Integer>(2,2);
        GreedyComparator comparator = new GreedyComparator(myGoal);
        PriorityQueue<Pair<Integer, Integer>> queue = 
            new PriorityQueue<Pair<Integer, Integer>>(10, comparator);
        
        Pair<Integer, Integer> a = new Pair<Integer, Integer>(1,1);
        Pair<Integer, Integer> b = new Pair<Integer, Integer>(2,1);
        queue.add(a);
        queue.add(b);
        
        while (queue.size() != 0)
        {
            System.out.println(queue.remove().getFirst());
        }
        // It prints out point (2,1) first, closer to goal
    }
}

