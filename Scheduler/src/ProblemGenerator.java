import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;


/*Extra Credit:
 * Write code to generate instances of the scheduling problem for a specified size (M, N, T)
 * Make sure that each meeting has at least two employees assigned to it
 * Apply your backtracking search code to ever-larger instances and report some statistics 
 * (average number of variable assignments attempted and average time to find a solution 
 * as a function of problem size, however you choose to define that)
 * */
public class ProblemGenerator {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		//SchedulingProblem sp = Generate(30,20,9);
		SchedulingProblem sp = Generate(50,40,20);
		//SchedulingProblem sp = Generate(40,30,20);
		
		sp.printProblem();
		
		ArrayList<Integer> counter = new ArrayList<Integer>();
		counter.add(0);
		HashMap<Integer, Integer> sol = Scheduler.backTracking(sp, counter);
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

	}
	
	/*
	 * There is a set of M employees.
	 * There is a set of N meetings, each of which has a duration of one time unit.
	 * The schedule has a maximum number T of time slots.
	 * */
	public static SchedulingProblem Generate(int numEmploy, int numMeet, int numSlots)
	{
		SchedulingProblem csp = new SchedulingProblem();
		csp.NumEmployees = numEmploy;
		csp.NumMeetings = numMeet;
		csp.NumTimeSlots = numSlots;
		
		ArrayList<Integer> slots = new ArrayList<Integer>();
		ArrayList<Integer> allMeetings = new ArrayList<Integer>();
		for(Integer i = 1; i <= csp.NumTimeSlots; i++)
		{
			slots.add(i);
		}
		
		//Randomly generate meeting -> time slot assignment
		for(Integer i = 1; i <= csp.NumMeetings; i++)
		{
			Integer randomSlot = getRandom(slots);
			csp.Assignment.put(i, randomSlot);
			allMeetings.add(i);
			ArrayList<Integer> myslots = new ArrayList<Integer>();
			
			//Initialize SlotsForMeetings with all slots
			for(int j = 1; j <= csp.NumTimeSlots; j ++)
			{
				myslots.add(j);
			}
			csp.SlotsForMeetings.put(i, myslots);
		}
		
		//Assign meetings that each employee must attend
		for(Integer employee = 1; employee <= csp.NumEmployees; employee++)
		{
			ArrayList<Integer> meetings = new ArrayList<Integer>();
			for(int i = 0; i < csp.NumMeetings/2; i++)
			{
				if(i == 0)
				{
					meetings.add(getRandom(allMeetings));
				}else
				{
					boolean addIt = true;
					Integer m = getRandom(allMeetings);
					if(!meetings.contains(m))
					{
						for(Integer cm : csp.getConnectedMeetings(m))
						{
							if(csp.Assignment.get(cm) == csp.Assignment.get(m))
							{
								addIt = false;
								break;
							}
						}
					
						if(addIt)
						{
							meetings.add(m);
							ArrayList<Integer> sortedMS = csp.sortMeetingsForEByTime(meetings);
							for(int k = 0; k < sortedMS.size(); k++)
							{
								if (k - 1 >= 0)
								{
									if(Math.abs(csp.Assignment.get(sortedMS.get(k)) - csp.Assignment.get(sortedMS.get(k - 1))) <= 1)
									{
										meetings.remove(m);
									}
								}
								if (k + 1 < sortedMS.size())
								{
									if(Math.abs(csp.Assignment.get(sortedMS.get(k)) - csp.Assignment.get(sortedMS.get(k + 1))) <= 1)
									{
										meetings.remove(m);
									}
								}
							}
						}
					}
				}
			}
			csp.EmployeeMeetingMap.put(employee, meetings);
		}
		
		//Set travel time between meetings according to current assignments
		//First, setting all travel times to be 0
		for(int i = 0; i < csp.NumMeetings; i++)
		{
			ArrayList<Integer> row = new ArrayList<Integer>();
			for(int j = 0; j < csp.NumMeetings; j++)
			{
				row.add(-1);
			}
			csp.TravelTimes.add(row);
		}
		for(int i = 0; i < csp.NumMeetings; i++)
		{
			for(int j = 0; j < csp.NumMeetings; j++)
			{
				if(i == j)
				{
					csp.TravelTimes.get(i).set(j,0);
				}else
				{
					Integer rnd = new Random().nextInt(csp.NumTimeSlots/2) + 1;
					csp.TravelTimes.get(i).set(j, rnd);
					csp.TravelTimes.get(j).set(i, rnd);
				}
			}
		}
		
		//Then, setting actual travel times
		for(ArrayList<Integer> ms: csp.EmployeeMeetingMap.values())
		{
			ArrayList<Integer> sortedMs = csp.sortMeetingsForEByTime(ms);
			for(int i = 0; i < sortedMs.size() - 1; i++)
			{
				Integer m1 = sortedMs.get(i);
				Integer m2 = sortedMs.get(i+1);
				Integer timeDiff = Math.abs(csp.Assignment.get(m1) - csp.Assignment.get(m2));
				if(timeDiff <= 1)
				{
					System.out.println("BUGBUGBUG----WRONG!!");
				}
				Integer currTT = csp.TravelTimes.get(m1 - 1).get(m2 - 1);
				Integer newTT = timeDiff - 1;
				if(currTT != 0)
				{
					csp.TravelTimes.get(m1 - 1).set(m2 - 1, newTT);
					csp.TravelTimes.get(m2 - 1).set(m1 - 1, newTT);
				}
				else
				{
					if(currTT < newTT){
						csp.TravelTimes.get(m1 - 1).set(m2 - 1, newTT);
						csp.TravelTimes.get(m2 - 1).set(m1 - 1, newTT);
					}
				}
			}
		}
		
		//printing Assignment(solution)
		System.out.println("Assignment Solution: ");
		for(Integer k : csp.Assignment.keySet())
		{
			System.out.println("Meeting " + k + "is at time " + csp.Assignment.get(k));
		}
		
//		boolean checkAssign = true;
//		for(Integer m: csp.Assignment.keySet())
//		{
//			Integer t = csp.Assignment.get(m);
//			for(Integer cm: csp.getConnectedMeetings(m))
//			{
//				//Checking this timeslot is not violating travel time constrain
//				Integer travelTime = csp.TravelTimes.get(cm - 1).get(m - 1);
//				if(Math.abs(t - csp.Assignment.get(cm)) <= travelTime)
//				{
//					checkAssign = false;
//				}
//			}
//		}
//		if(checkAssign)
//			System.out.println("TESTING: Assignment is Valid");
//		else
//			System.out.println("TESTING: Assignment is NOT valid");
		
		csp.checkAssignmentCorrect();
		//Clear Assignment
		csp.Assignment = new HashMap<Integer, Integer>();
		return csp;
	}
	
	private static int getRandom(ArrayList<Integer> array) {
	    int rnd = new Random().nextInt(array.size());
	    return array.get(rnd);
	}

}
