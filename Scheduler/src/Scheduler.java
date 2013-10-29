import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;


public class Scheduler {

	/**
	 * This solves the scheduling CSP problem
	 */
	static BufferedWriter writer;
	public static void main(String[] args) {
		
		
		try {
			File file = new File("debug.txt");
	          writer = new BufferedWriter(new FileWriter(file));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		ProblemFileParser pfp = new ProblemFileParser("E:\\CS440Github\\Scheduler\\src\\simple.txt");
		SchedulingProblem sp = pfp.GetProblem();
		
		ArrayList<Integer> counter = new ArrayList<Integer>();
		counter.add(0);
		HashMap<Integer, Integer> sol = backTracking(sp, counter);
		
		if(sol == null)
		{
			System.out.println("No Schedule!");
		}else
		{
			for(Integer key: sol.keySet())
			{
				System.out.println("Meeting " + key + " is scheduled at time " + sol.get(key));
			}
			sp.checkAssignmentCorrect();
			System.out.println("Total Number of Assignments: " + counter.get(0));
		}
		try {
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/*CSP definition: 
	   Variables: Meetings
	   Domains: Time Slots
	   Constraints:
	   	1) Each Employee's meetings must be assigned with diff slots
	   	2) Between each meeting, the time slot must be separated by the required time units 
	*/
	public static HashMap<Integer, Integer> backTracking(SchedulingProblem csp, ArrayList<Integer> assignmentCount)
	{
		if (isComplete(csp))
		{
			return csp.Assignment;
		}
		
		Integer selectedVar;
		ArrayList<Integer> mostConstrainedVars = csp.getMostConstrainedMeeting();
		if(mostConstrainedVars.size() != 1)
		{
			selectedVar = csp.getMostConstrainingMeeting(mostConstrainedVars);
		}else
		{
			selectedVar = mostConstrainedVars.get(0);
		}
		
		ArrayList<Integer> slots  = new ArrayList<Integer>();
		slots.addAll(csp.OrderedTimeslots(selectedVar));
		for (Integer valueSlot : slots)
		{
			if(csp.isValid(selectedVar, valueSlot)){
				csp.addAssignment(selectedVar, valueSlot);
				assignmentCount.set(0, assignmentCount.get(0) + 1);
//				try {
//					writer.write("ASSIGNMENT: " + csp.Assignment.toString() + "\n");
//				} catch (IOException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
				if(csp.fowardChecking() == false)
				{
					csp.removeAssignment(selectedVar);
					return null;
				}
				
				HashMap<Integer, Integer> result = backTracking(csp, assignmentCount);
				if(result != null)
				{
					return result;
				}
				
				csp.removeAssignment(selectedVar);
			}
		}
		return null;
	}

	private static boolean isComplete(SchedulingProblem csp) {
		return csp.Assignment.size() == csp.NumMeetings;
	}

}
