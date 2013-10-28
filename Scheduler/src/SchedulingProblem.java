import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

/*
 * This is the CSP definition
 * */
public class SchedulingProblem {

	public int NumMeetings;
	public int NumEmployees;
	public int NumTimeSlots;
	
	//Key - Employee #; Value - Meeting #s
	public HashMap<Integer, ArrayList<Integer>> EmployeeMeetingMap;
	
	//Travel time between meetings
	public ArrayList<ArrayList<Integer>> TravelTimes;
	
	//Key - Meeting #; Value - Time slot #
	public HashMap<Integer, Integer> Assignment;
	
	public HashMap<Integer, ArrayList<Integer>> SlotsForMeetings;
	
	public SchedulingProblem()
	{
		this.EmployeeMeetingMap = new HashMap<Integer, ArrayList<Integer>>();
		this.TravelTimes = new ArrayList<ArrayList<Integer>>();
		this.Assignment = new HashMap<Integer, Integer>();
		this.SlotsForMeetings = new HashMap<Integer, ArrayList<Integer>>();
	}
	
	//Check if Assignment is Valid (for debugging purpose)
	public void checkAssignmentCorrect()
	{
		boolean checkAssign = true;
		for(Integer m: this.Assignment.keySet())
		{
			Integer t = this.Assignment.get(m);
			for(Integer cm: this.getConnectedMeetings(m))
			{
				if(t == this.Assignment.get(cm))
				{
					checkAssign = false;
					break;
				}
			}
		}
		if(checkAssign)
		{
			for (Integer e: this.EmployeeMeetingMap.keySet())
			{
				ArrayList<Integer> meetingsForE = this.EmployeeMeetingMap.get(e);
				for(Integer meeting: this.Assignment.keySet()){
					if(meetingsForE.contains(meeting))
					{
						if(!checkValidForOneEmployee(meeting, this.Assignment.get(meeting), meetingsForE))
						{
							checkAssign = false;
							break;
						}
					}
				}
			}
		}
		if(checkAssign)
			System.out.println("TESTING: Assignment is Valid");
		else
			System.out.println("TESTING: Assignment is NOT valid");
	}
	
	/*
	 * Add a meeting assigenement
	 * */
	public void addAssignment(Integer meeting, Integer timeSlot)
	{
		this.Assignment.put(meeting, timeSlot);
		//Updating SlotsForMeetings
		//Assigned meeting has 0 time slots
		this.SlotsForMeetings.put(meeting, new ArrayList<Integer>());
		//Removing this timeSlot from all connected meetings
		ArrayList<Integer> connectedMeetings = this.getConnectedMeetings(meeting);
		for(Integer m : connectedMeetings)
		{
			ArrayList<Integer> mSlots = this.SlotsForMeetings.get(m);
			mSlots.remove(timeSlot);
//			Integer i1 = timeSlot + 1;
//			mSlots.remove(i1);
//			Integer i2 = timeSlot - 1;
//			mSlots.remove(i2);
			this.SlotsForMeetings.put(m, mSlots);
		}
	}
	
	/*
	 * Remove a meeting's assignement
	 * */
	public void removeAssignment(int meeting)
	{
		Integer timeslot = this.Assignment.remove(meeting);
		
		if(timeslot == null)
			return;
		
		ArrayList<Integer> slots = new ArrayList<Integer>();
		for(int j = 0; j < this.NumTimeSlots; j ++)
		{
			slots.add(j + 1);
		}
		this.SlotsForMeetings.put(meeting, slots);
		
		ArrayList<Integer> connectedMeetings = this.getConnectedMeetings(meeting);
		for(Integer cm: connectedMeetings)
		{
			if(this.Assignment.containsKey(cm)) // for meetings that already assigned
			{
				ArrayList<Integer> allSlots = this.SlotsForMeetings.get(meeting);
				Integer cmSlot = this.Assignment.get(cm);
				allSlots.remove(cmSlot);
//				Integer i1 = cmSlot + 1;
//				allSlots.remove(i1);
//				Integer i2 = cmSlot - 1;
//				allSlots.remove(i2);
				
				this.SlotsForMeetings.put(meeting, allSlots);
			}
			else //not assigned, connected meetings
			{
				ArrayList<Integer> cmSlots = this.SlotsForMeetings.get(cm);
				if(!cmSlots.contains(timeslot))
				{
					cmSlots.add(timeslot);
//					Integer i1 = timeslot - 1;
//					if(i1 > 0 && !cmSlots.contains(i1))
//					{
//						cmSlots.add(i1);
//					}
//					
//					Integer i2 = timeslot + 1;
//					if(i2 <= this.NumTimeSlots && !cmSlots.contains(i2))
//					{
//						cmSlots.add(i2);
//					}
				}

				ArrayList<Integer> cmConnectedMeetings = this.getConnectedMeetings(cm);
				for(Integer cmcm: cmConnectedMeetings)
				{
					if(this.Assignment.containsKey(cmcm))
					{
						Integer rmSlot = this.Assignment.get(cmcm);
						cmSlots.remove(rmSlot);
//						Integer i1 = rmSlot + 1;
//						cmSlots.remove(i1);
//						Integer i2 = rmSlot - 1;
//						cmSlots.remove(i2);
					}
				}
				this.SlotsForMeetings.put(cm, cmSlots);
			}
		}
	}
	
	/*
	 * Get meetings that are connected (having constrains) with this meeting
	 * */
	public ArrayList<Integer> getConnectedMeetings(int meeting)
	{
		ArrayList<Integer> connectedMeetings = new ArrayList<Integer>();
		for(Integer i = 0; i < this.EmployeeMeetingMap.size(); i++)
		{
			ArrayList<Integer> ms = this.EmployeeMeetingMap.get(i + 1);
			if(ms.contains(meeting))
			{
				for(Integer m: ms)
				{
					if((!connectedMeetings.contains(m)) && (m != meeting))
					{
						connectedMeetings.add(m);
					}
				}
			}
		}
		
		return connectedMeetings;
	}
	
	/*
	 * Most Constrained Variable:
	 * Choose the variable with the fewest legal values
	 * */
	public ArrayList<Integer> getMostConstrainedMeeting()
	{
		HashMap<Integer, ArrayList<Integer>> slotsForMeetings = this.SlotsForMeetings;
		int minNumSlots = Integer.MAX_VALUE;
		ArrayList<Integer> meeting = new ArrayList<Integer>();
		for(Integer key: slotsForMeetings.keySet())
		{
			int numSlots = slotsForMeetings.get(key).size();
			if( numSlots <= minNumSlots && !this.Assignment.containsKey(key))
			{
				if(numSlots < minNumSlots)
				{
					meeting = new ArrayList<Integer>();
				}
				minNumSlots = numSlots;
				meeting.add(key);
			}
		}
		
		return meeting;
	}
	
	/*
	 * Find the meeting which has most number of connections with other meetings
	 * */
	public Integer getMostConstrainingMeeting(ArrayList<Integer> meetings)
	{
		Integer retMeeting = meetings.get(0);
		int maxNumCMs = this.getConnectedMeetings(retMeeting).size();
		for(int i = 1; i < meetings.size(); i++)
		{
			int numConnectedMeetings = this.getConnectedMeetings(meetings.get(i)).size();
			if (numConnectedMeetings > maxNumCMs)
			{
				maxNumCMs = numConnectedMeetings;
				retMeeting = meetings.get(i);
			}
		}
		
		return retMeeting;
	}
	
	/* LeastConstrainingValue:
	 * Get the value that rules out the fewest values in the remaining variables
	 * 
	 * All domain values ordered from least constraining to most constraining
	 * */
	public Collection<Integer> OrderedTimeslots(Integer meeting)
	{
		ArrayList<Integer> unassignedSlots = this.SlotsForMeetings.get(meeting);
		
		HashMap<Integer, Integer> map = new HashMap<Integer, Integer>();
		for(Integer us: unassignedSlots)
		{
			this.addAssignment(meeting, us);
			Integer count = 0;
			for(Integer slotKey: this.SlotsForMeetings.keySet()){
				count += this.SlotsForMeetings.get(slotKey).size();
			}
			map.put(us, count);
			this.removeAssignment(meeting);
		}
		
		TreeMap<Integer, Integer> sortedMap = new TreeMap<Integer, Integer>(new ValueComparator(map));
		sortedMap.putAll(map);
		return sortedMap.keySet();
	}
	
	/*
	 * Checking if an assignment is valid - satisfying all the constrains
	 * */
	public boolean isValid(Integer meeting, Integer timeslot)
	{
		ArrayList<Integer> connectedMeetings = this.getConnectedMeetings(meeting);
		for(Integer m: connectedMeetings)
		{
			if(this.Assignment.containsKey(m)){
				Integer cmTimeslot = this.Assignment.get(m);
				if( cmTimeslot == timeslot)
				{
					return false;
				}
			}
		}

		for (Integer e: this.EmployeeMeetingMap.keySet())
		{
			ArrayList<Integer> meetingsForE = this.EmployeeMeetingMap.get(e);
			if(meetingsForE.contains(meeting))
			{
				if(!checkValidForOneEmployee(meeting, timeslot, meetingsForE))
				{
					return false;
				}
			}
		}
		return true;
	}
	private boolean lastCheckValid(ArrayList<Integer> meetings, Integer m1, Integer tt1, Integer m2, Integer tt2)
	{
		for(Integer m: meetings)
		{
			if(!this.Assignment.containsKey(m))
			{
				ArrayList<Integer> availableSlots = this.SlotsForMeetings.get(m);
				for(Integer s: availableSlots)
				{
					if(tt1 < s && s < tt2)
					{
						if(this.TravelTimes.get(m - 1).get(m1 - 1)< s - tt1 && this.TravelTimes.get(m - 1).get(m2 - 1)< tt2 - s)
						{
							return true;
						}
					}
					if(tt2 < s && s < tt1)
					{
						if(this.TravelTimes.get(m - 1).get(m2 - 1)< s - tt2 && this.TravelTimes.get(m - 1).get(m1 - 1) < tt1 - s)
						{
							return true;
						}
					}
				}
			}
		}
		return false;
	}
	//TODO: bug - need to consider when later unassigned meeting in meetings gets assigned, (meeting,timeslot) might become valid
	private boolean checkValidForOneEmployee(Integer meeting, Integer timeslot, ArrayList<Integer> meetings)
	{
		Integer leftNeighbor = -1;
		Integer rightNeighbor = -1;
		Integer leftTime = Integer.MIN_VALUE;
		Integer rightTime = Integer.MAX_VALUE;
		for(Integer m: meetings)
		{
			if(this.Assignment.containsKey(m))
			{
				Integer thisTime = this.Assignment.get(m);
				if(thisTime > leftTime && thisTime < timeslot)
				{
					leftNeighbor = m;
					leftTime = thisTime;
				}
				if(thisTime < rightTime && thisTime > timeslot)
				{
					rightNeighbor = m;
					rightTime = thisTime;
				}
			}
		}
		if(leftNeighbor != -1)
		{
			Integer tt = this.TravelTimes.get(leftNeighbor - 1).get(meeting - 1);
			if(Math.abs(timeslot - leftTime) <= tt && !lastCheckValid(meetings, leftNeighbor, leftTime, meeting, timeslot))
			{
				return false;
			}
		}
		if(rightNeighbor != -1)
		{
			Integer tt = this.TravelTimes.get(rightNeighbor - 1).get(meeting - 1);
			if(Math.abs(rightTime - timeslot) <= tt && !lastCheckValid(meetings, rightNeighbor, rightTime, meeting, timeslot))
			{
				return false;
			}
		}
		return true;
	}
	
	/*
	 * Check if there is non-assigned meetings which have no pickable time slots
	 * */
	public boolean fowardChecking() {
		ArrayList<Integer> unassignedMeetings = new ArrayList<Integer>();
		Collection<Integer> assignedMeetings = this.Assignment.keySet();
		for(Integer i = 1; i <= this.NumMeetings; i ++)
		{
			if(!assignedMeetings.contains(i))
			{
				unassignedMeetings.add(i);
			}
		}
		
		for(Integer um: unassignedMeetings)
		{
			if(this.SlotsForMeetings.get(um).size() <= 0)
			{
				return false;
			}
		}
		
		return true;
	}
	
	//Help Debugging
	public void printSlotsForMeetings()
	{
		HashMap<Integer, ArrayList<Integer>> sfm = this.SlotsForMeetings;
		for(Integer key :sfm.keySet())
		{
			String line = key + ":";
			ArrayList<Integer> vals = sfm.get(key);
			for (Integer v: vals)
			{
				line  = line + " " + v;
			}
			System.out.println(line);
		}
	}

	public void printEmployeeMeetingsMap()
	{
		HashMap<Integer, ArrayList<Integer>> emm = this.EmployeeMeetingMap;
		for(Integer key: emm.keySet())
		{
			String line = key + ":";
			ArrayList<Integer> vals = emm.get(key);
			for (Integer v: vals)
			{
				line  = line + " " + v;
			}
			System.out.println(line);
		}
	}
	
	//For debugging ProblemGenerator
	public void printProblem()
	{
		System.out.println("Number of meetings: " + this.NumMeetings);
		System.out.println("Number of employees: " + this.NumEmployees);
		System.out.println("Number of time slots: " + this.NumTimeSlots);
		
		System.out.println("Meetings each employee must attend:");
		for(Integer e : this.EmployeeMeetingMap.keySet())
		{
			ArrayList<Integer> meetings = this.EmployeeMeetingMap.get(e);
			System.out.print(e + ":");
			for(Integer m: meetings)
			{
				System.out.print(" " + m);
			}
			System.out.println();
		}
		
		System.out.println("Travel time between meetings: ");
		System.out.print("    ");
		for(Integer i = 1; i <= this.NumMeetings; i++)
		{
			System.out.print(i + " ");
		}
		System.out.println();
		for(Integer i = 0; i < this.NumMeetings; i++)
		{
			System.out.print((i + 1) + ":   ");
			for(Integer j = 0; j < this.NumMeetings; j++)
			{
				System.out.print(this.TravelTimes.get(i).get(j) + " ");
			}
			System.out.println();
		}
	}

	/*For ProblemGenerator*/
	public ArrayList<Integer> sortMeetingsForEByTime(ArrayList<Integer> meetings)
	{
		HashMap<Integer, Integer> meetingTimeMap = new HashMap<Integer, Integer>();
		for(Integer m: meetings)
		{
			meetingTimeMap.put(m, this.Assignment.get(m));
		}
		
		TreeMap<Integer, Integer> sortedMap = new TreeMap<Integer, Integer>(new ValueComparator(meetingTimeMap));
		sortedMap.putAll(meetingTimeMap);
		
		ArrayList<Integer> ret = new ArrayList<Integer>();
		ret.addAll(sortedMap.keySet());
		return ret;
	}
}
