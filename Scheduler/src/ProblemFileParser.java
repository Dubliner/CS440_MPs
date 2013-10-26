import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/*
 * Parse the problem txt file into a SchedulingProblem class object
 * */
public class ProblemFileParser {
	
	private String filePath;
	
	public ProblemFileParser(String path)
	{
		this.filePath = path;
	}
	
	public SchedulingProblem GetProblem()
	{
		SchedulingProblem sp = new SchedulingProblem();
		
		try {
			BufferedReader br = new BufferedReader(new FileReader(this.filePath));
			String line = br.readLine();
			
			while (line != null)
			{
				if(line.contains("Number of meetings"))
				{
					String[] splitedLine = line.split(":");
					sp.NumMeetings = Integer.parseInt(splitedLine[1].substring(1));
				}else if(line.contains("Number of employees"))
				{
					String[] splitedLine = line.split(":");
					sp.NumEmployees = Integer.parseInt(splitedLine[1].substring(1));
				}
				else if(line.contains("Number of time slots"))
				{
					String[] splitedLine = line.split(":");
					sp.NumTimeSlots = Integer.parseInt(splitedLine[1].substring(1));
				}
				else if(line.contains("Meetings each employee must attend:"))
				{
					for(int i = 0; i < sp.NumEmployees; i++)
					{
						line = br.readLine();
						String[] splitedLine = line.split(":");
						int meetingNumber = Integer.parseInt(splitedLine[0]);
						String[] employeeNumbers = splitedLine[1].split(" ");
						ArrayList<Integer> eNums = new ArrayList<Integer>();
						for(int j = 1; j < employeeNumbers.length; j++)
						{
							if(!employeeNumbers[j].isEmpty()){
								eNums.add(Integer.parseInt(employeeNumbers[j]));
							}
						}
						sp.EmployeeMeetingMap.put(meetingNumber, eNums);
					}
				}
				else if(line.contains("Travel time between meetings:"))
				{
					line = br.readLine();
					for(int i = 0; i < sp.NumMeetings; i++)
					{
						line = br.readLine();
						String[] splitedLine = line.split(":");
						String[] employeeNumbers = splitedLine[1].split(" ");
						ArrayList<Integer> eNums = new ArrayList<Integer>();
						for(int j = 1; j < employeeNumbers.length; j++)
						{
							if(!employeeNumbers[j].isEmpty()){
								eNums.add(Integer.parseInt(employeeNumbers[j]));
							}
						}
						sp.TravelTimes.add(eNums);
					}
				}
				line = br.readLine();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		//Available slots for each meeting
		//Initially, each meeting can pick ALL slots
		for(int i = 0; i < sp.NumMeetings; i++)
		{
			ArrayList<Integer> slots = new ArrayList<Integer>();
			for(int j = 0; j < sp.NumTimeSlots; j ++)
			{
				slots.add(j + 1);
			}
			
			sp.SlotsForMeetings.put(i + 1, slots);
		}
		
		return sp;
	}	
}
