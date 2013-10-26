import java.util.ArrayList;
import java.util.HashMap;


public class Scheduler {

	/**
	 * This solves the scheduling CSP problem
	 */
	public static void main(String[] args) {
		
		ProblemFileParser pfp = new ProblemFileParser("E:\\Fall2013Workspace\\Scheduler\\src\\problem3.txt");
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
			System.out.println("Total Number of Assignments: " + counter.get(0));
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
		if(mostConstrainedVars.size() > 1)
		{
			selectedVar = csp.getMostConstrainingMeeting(mostConstrainedVars);
		}else
		{
			selectedVar = mostConstrainedVars.get(0);
		}
		
		for (Integer valueSlot : csp.OrderedTimeslots(selectedVar))
		{
			if(csp.isValid(selectedVar, valueSlot)){
				csp.addAssignment(selectedVar, valueSlot);
				assignmentCount.set(0, assignmentCount.get(0) + 1);
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
