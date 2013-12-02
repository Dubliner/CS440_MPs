import java.util.Hashtable;


public class State {

	private double utility;
	private double reward;
	public boolean IsWall;
	public int XCoord;
	public int YCoord;
	public int TimeStep;

	private int numUpTaken;
	private int numDownTaken;
	private int numLeftTaken;
	private int numRightTaken;
	private Hashtable<ACTION, Double> QValues;
	
	public ACTION OptimalPolicy;
	public boolean IsTerminal;
	
	private static final double DEFAULT_REWARD = -0.114;

	public State(int x, int y)
	{
		this.TimeStep = 1;
		this.XCoord = x;
		this.YCoord = y;
		this.IsWall = false;
		this.reward = DEFAULT_REWARD;
		this.utility = 0.0; //Start out with every U(s) = 0
		
		this.numDownTaken = 0;
		this.numUpTaken = 0;
		this.numLeftTaken = 0;
		this.numRightTaken = 0;
		
		this.QValues = new Hashtable<ACTION, Double>();
		QValues.put(ACTION.UP, 0.0);
		QValues.put(ACTION.DOWN, 0.0);
		QValues.put(ACTION.LEFT, 0.0);
		QValues.put(ACTION.RIGHT, 0.0);
	}
	
	public double GetQValue(ACTION action)
	{
		return this.QValues.get(action);
	}
	
	public void UpdateQValues(ACTION action, double newVal)
	{
		this.QValues.put(action, newVal);
		//UPDATE Policy too
		double max = Integer.MIN_VALUE;
		for(ACTION a: ACTION.values())
		{
			//this.QValues.get(a) != 0 && 
			if(this.QValues.get(a) > max)
			{
				max = this.QValues.get(a);
				this.OptimalPolicy = a;
				this.utility = max;
			}
		}
	}
	public void CountActionTaken(ACTION action)
	{
		if(action == ACTION.UP)
		{
			this.numUpTaken++;
		}
		else if(action == ACTION.DOWN)
		{
			this.numDownTaken++;
		}
		else if(action == ACTION.LEFT)
		{
			this.numLeftTaken++;
		}
		else if(action == ACTION.RIGHT)
		{
			this.numRightTaken++;
		}
	}
	
	public int GetActionTakenCount(ACTION action)
	{
		if(action == ACTION.UP)
		{
			return this.numUpTaken;
		}
		else if(action == ACTION.DOWN)
		{
			return this.numDownTaken;
		}
		else if(action == ACTION.LEFT)
		{
			return this.numLeftTaken;
		}
		else
		{
			return this.numRightTaken;
		}
	}
		
	public double GetUtility()
	{
		return this.utility;
	}

	public void SetUtility(double utility)
	{
		this.utility = utility;
	}
	
	public double GetReward()
	{
		return this.reward;
	}
	
	public void SetReward(double reward)
	{
		this.reward = reward;
	}
	
	
}
