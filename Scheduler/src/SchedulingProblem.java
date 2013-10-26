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
			//Removing time slots that are limited by travel time
			Integer travelTime = this.TravelTimes.get(meeting - 1).get(m - 1);
			for(Integer k = 1 ; k <= travelTime; k++)
			{
				Integer o1 = timeSlot + k;
				mSlots.remove(o1);
				Integer o2 = timeSlot - k;
				mSlots.remove(o2);
			}
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
			Integer travelTime = this.TravelTimes.get(cm - 1).get(meeting - 1);
			if(this.Assignment.containsKey(cm)) // for meetings that already assigned
			{
				ArrayList<Integer> allSlots = this.SlotsForMeetings.get(meeting);
				Integer cmSlot = this.Assignment.get(cm);
				allSlots.remove(cmSlot);
				for(Integer k = 1 ; k <= travelTime; k++)
				{
					Integer o1 = cmSlot + k;
					allSlots.remove(o1);
					Integer o2 = cmSlot - k;
					allSlots.remove(o2);
				}
				this.SlotsForMeetings.put(meeting, allSlots);
			}else //not assigned, connected meetings
			{
				ArrayList<Integer> cmSlots = this.SlotsForMeetings.get(cm);
				if(!cmSlots.contains(timeslot))
					cmSlots.add(timeslot);
				for(Integer k = 1; k <= travelTime; k++)
				{
					Integer o1 = timeslot + k;
					if(o1 <= this.NumTimeSlots && !cmSlots.contains(o1))
						cmSlots.add(o1);
					Integer o2 = timeslot - k;
					if(o2 > 0 && !cmSlots.contains(o2))
						cmSlots.add(o2);
				}
				ArrayList<Integer> cmConnectedMeetings = this.getConnectedMeetings(cm);
				for(Integer cmcm: cmConnectedMeetings)
				{
					if(this.Assignment.containsKey(cmcm))
					{
						Integer rmSlot = this.Assignment.get(cmcm);
						cmSlots.remove(rmSlot);
						Integer tt = this.TravelTimes.get(cmcm - 1).get(cm - 1);
						for(Integer k = 1 ; k <= tt; k++)
						{
							Integer o1 = rmSlot + k;
							cmSlots.remove(o1);
							Integer o2 = rmSlot - k;
							cmSlots.remove(o2);
						}
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
		ArrayList<Integer> unassignedSlots = new ArrayList<Integer>();

		for(Integer i = 1; i <= this.NumTimeSlots; i ++)
		{
				unassignedSlots.add(i);			
		}
		
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
				//Checking this timeslot is not taken by any meetings 
				//which is connected with this meeting and is already assigned to this timeslot
				Integer cmTimeslot = this.Assignment.get(m);
				if( cmTimeslot == timeslot)
				{
					return false;
				}
	
				//Checking this timeslot is not violating travel time constrain
				Integer travelTime = this.TravelTimes.get(meeting - 1).get(m - 1);
				if(Math.abs(timeslot - cmTimeslot) < travelTime)
				{
					return false;
				}
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
}

/*
 * A comparator class for sorting the hash table
 * */
class ValueComparator implements Comparator<Integer> {

    Map<Integer, Integer> base;
    public ValueComparator(Map<Integer, Integer> base) {
        this.base = base;
    }
   
    public int compare(Integer a, Integer b) {
        if (base.get(a) >= base.get(b)) {
            return -1;
        } else {
            return 1;
        } 
    }
}
