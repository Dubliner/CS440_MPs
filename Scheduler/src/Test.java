
public class Test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		ProblemFileParser pfp = new ProblemFileParser("E:\\Fall2013Workspace\\Scheduler\\src\\simple.txt");
		SchedulingProblem sp = pfp.GetProblem();
		
		System.out.println("NumberOfEmployees: " + sp.NumEmployees);
		System.out.println("NumberOfMeetings: " + sp.NumMeetings);
		System.out.println("NumberOfTimeSlots: " + sp.NumTimeSlots);
		
		sp.addAssignment(1, 1);
		System.out.println("PRINTING SLOTS FOR MEETINGS");
		sp.printSlotsForMeetings();
		System.out.println("PRINTING EMPLOYEE MEETING MAP");
		sp.printEmployeeMeetingsMap();
		System.out.println("CONNECTED MEETINGS: " + sp.getConnectedMeetings(1).toString());
		System.out.println("MOST CONSTRAINED MEETING: " + sp.getMostConstrainedMeeting());
		
		//System.out.println("ISVALID:" + sp.isValid(5, 1));
//		
//		sp.removeAssignment(1);
//		System.out.println("PRINTING SLOTS FOR MEETINGS");
//		sp.printSlotsForMeetings();
//		System.out.println("PRINTING EMPLOYEE MEETING MAP");
//		sp.printEmployeeMeetingsMap();
//		System.out.println("CONNECTED MEETINGS: " + sp.getConnectedMeetings(1).toString());
		
//		System.out.println(sp.OrderedTimeslots(3).toString());	
//
//		System.out.println("PRINTING SLOTS FOR MEETINGS");
//		sp.printSlotsForMeetings();
//		System.out.println("PRINTING EMPLOYEE MEETING MAP");
//		sp.printEmployeeMeetingsMap();
//		System.out.println("CONNECTED MEETINGS: " + sp.getConnectedMeetings(1).toString());
//		System.out.println("MOST CONSTRAINED MEETING: " + sp.getMostConstrainedMeeting());

	}

}
