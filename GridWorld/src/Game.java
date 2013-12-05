import java.util.ArrayList;


public class Game {

	//States
	public State[][] States;
	//Actions
	//Transition Model
	//Reward Function
	//Policy
	public State StartState;

	public Game(int version)
	{
	//	testGameInit();
		init(version);
	}
	
	public void printQLearning()
	{
		for(int i = 0; i < this.States.length; i++)
		{
			for(int j = 0; j < this.States[0].length; j++)
			{
				if(this.States[i][j].IsWall)
				{
					System.out.print("( WALL )");
				}else if(this.States[i][j].IsTerminal)
				{
					System.out.print("( TERMI )") ;
				}else{
					System.out.print("( *" + this.States[i][j].OptimalPolicy + "*)" );
					System.out.print("("+ ACTION.UP + ":" + this.States[i][j].GetQValue(ACTION.UP) + "|"
							+ ACTION.DOWN + ":" + this.States[i][j].GetQValue(ACTION.DOWN) + "|" 
							+ ACTION.LEFT + ":" + this.States[i][j].GetQValue(ACTION.LEFT) + "|"
							+ ACTION.RIGHT + ":" + this.States[i][j].GetQValue(ACTION.RIGHT) + ")");
				}
			}
			System.out.println();
		}
	}
	
	public void printUtilities()
	{
		
		for(int i = 0; i < this.States.length; i++)
		{
			for(int j = 0; j < this.States[0].length; j++)
			{
				if(this.States[i][j].IsWall)
				{
					System.out.print("( WALL )");
				}else if(this.States[i][j].GetReward() == 1 || this.States[i][j].GetReward() == -1)
				{
					System.out.print("( TERMI " + this.States[i][j].GetUtility() + ")");
				}else{
				System.out.print("(" + this.States[i][j].GetUtility() + "," + this.States[i][j].OptimalPolicy + ") ");
				}
			}
			System.out.println();
		}
	}
	
	public void printRewards()
	{
		
		for(int i = 0; i < this.States.length; i++)
		{
			for(int j = 0; j < this.States[0].length; j++)
			{
				if(this.States[i][j].IsWall)
				{
					System.out.print("( WALL )");
				}else if(this.States[i][j].GetReward() == 1 || this.States[i][j].GetReward() == -1)
				{
					System.out.print("( TERMI " + this.States[i][j].GetReward() + ")");
				}else{
				System.out.print("(" + this.States[i][j].GetReward()  + ") ");
				}
			}
			System.out.println();
		}
	}

	private void testGameInit()
	{
		//Initializing Game States according to the given Grid in the Spec
		this.States = new State[4][3];
		//i is horizontal X value, j is vertical Y value
		for(int i = 0; i < this.States.length; i++)
		{
			for(int j = 0; j < this.States[0].length; j++)
			{
				this.States[i][j] = new State(i, j);
			}
		}
		
		this.States[3][2].SetReward(1);
		this.States[3][1].SetReward(-1);

		this.States[3][2].IsTerminal = true;
		this.States[3][1].IsTerminal = true;

		this.States[3][2].SetUtility(1);
		this.States[3][1].SetUtility(-1);

		this.States[1][1].IsWall = true;
		this.StartState = this.States[0][0];
	}
	private void init(int version)
	{
		//Initializing Game States according to the given Grid in the Spec
		this.States = new State[6][6];
		//i is horizontal X value, j is vertical Y value
		for(int i = 0; i < this.States.length; i++)
		{
			for(int j = 0; j < this.States[0].length; j++)
			{
				this.States[i][j] = new State(i, j);
			}
		}
		this.StartState = this.States[0][0];
		this.States[2][0].SetReward(-1);		
		this.States[3][0].SetReward(-1);
		this.States[3][1].SetReward(-1);
		this.States[5][1].SetReward(-1);
		this.States[5][0].SetReward(1);
		if(version == 1){
		this.States[5][5].SetReward(1);
		}else
		{
			this.States[5][5].SetReward(10);
		}
//
		this.States[2][0].SetUtility(-1);		
		this.States[3][0].SetUtility(-1);
		this.States[3][1].SetUtility(-1);
		this.States[5][1].SetUtility(-1);
		this.States[5][0].SetUtility(1);
		this.States[5][5].SetUtility(1);
		
		this.States[2][0].IsTerminal = true;		
		this.States[3][0].IsTerminal = true;
		this.States[3][1].IsTerminal = true;
		this.States[5][1].IsTerminal = true;
		this.States[5][0].IsTerminal = true;
		this.States[5][5].IsTerminal = true;
		
		this.States[1][2].IsWall = true;
		this.States[1][3].IsWall = true;
		this.States[1][4].IsWall = true;
		this.States[3][4].IsWall = true;
		this.States[3][5].IsWall = true;	
	}
	
	public boolean IsOutBound(State state)
	{
		return (state.XCoord < 0 || state.XCoord >= this.States.length || state.YCoord < 0 || state.YCoord >= this.States[0].length);
	}
	
	public State GetNextState(State currState, ACTION actionTaken)
	{
		int nextX;
		int nextY;
		
		if(actionTaken == ACTION.UP)
		{
			nextX = currState.XCoord;
			nextY = currState.YCoord + 1;
		}else if(actionTaken == ACTION.DOWN)
		{
			nextX = currState.XCoord;
			nextY = currState.YCoord - 1;
		}else if(actionTaken == ACTION.LEFT)
		{
			nextX = currState.XCoord - 1;
			nextY = currState.YCoord;
		}else
		{
			nextX = currState.XCoord + 1;
			nextY = currState.YCoord;
		}
		
		if(nextX < 0 || nextX >= this.States.length || nextY < 0 || nextY >= this.States[0].length)
		{
			return new State(nextX, nextY); 
		}else{
			return this.States[nextX][nextY];
		}
	}
	
	public ArrayList<State> GetNeighborStates(State currState)
	{
		ArrayList<State> states = new ArrayList<State>();
//		if(currState.XCoord - 1 >= 0)
//		{
//			states.add(this.States[currState.XCoord - 1][currState.YCoord]);
//			
//		}
//		if(currState.XCoord + 1 < this.States.length)
//		{
//			states.add(this.States[currState.XCoord + 1][currState.YCoord]);
//		}
//		if(currState.YCoord - 1 >= 0)
//		{
//			states.add(this.States[currState.XCoord][currState.YCoord - 1]);
//		}
//		
//		if(currState.YCoord + 1 < this.States[0].length)
//		{
//			states.add(this.States[currState.XCoord][currState.YCoord + 1]);
//		}
		for(ACTION action: ACTION.values()){
			states.add(this.GetNextState(currState, action));
		}
		return states;
	}
	
	public ArrayList<ACTION> GetPossibleActions(State state)
	{
		ArrayList<ACTION> ret = new ArrayList<ACTION>();
		for(ACTION action: ACTION.values())
		{
			if(action == ACTION.UP)
			{
				if(state.YCoord + 1 < this.States[0].length )//&& !this.States[state.XCoord][state.YCoord + 1].IsWall)
				{
					ret.add(action);
				}
			}else if(action == ACTION.DOWN)
			{
				if(state.YCoord - 1 >= 0)// && !this.States[state.XCoord][state.YCoord - 1].IsWall)
				{
					ret.add(action);
				}
			}else if(action == ACTION.LEFT)
			{
				if(state.XCoord - 1 >= 0)// && !this.States[state.XCoord - 1][state.YCoord].IsWall)
				{
					ret.add(action);
				}
			}else
			{
				if(state.XCoord + 1 < this.States.length)// && !this.States[state.XCoord + 1][state.YCoord].IsWall)
				{
					ret.add(action);
				}
			}
		}
		return ret;
	}
	public double GetProbability(State currState, ACTION action, State nextState)
	{
		if(action == ACTION.LEFT)
		{
			if(currState.XCoord - 1 == nextState.XCoord && currState.YCoord == nextState.YCoord)
			{
				//Intended outcome occurs
				return 0.8;
			}else if(currState.XCoord == nextState.XCoord 
						&& (currState.YCoord - 1 == nextState.YCoord || currState.YCoord + 1 == nextState.YCoord))
			{
				//agent moves at either right angle to the intended direction
				return 0.1;
			}else
			{
				return 0.0;
			}
		}else if(action == ACTION.RIGHT)
		{
			if(currState.XCoord + 1 == nextState.XCoord && currState.YCoord == nextState.YCoord)
			{
				//Intended outcome occurs
				return 0.8;
			}else if(currState.XCoord == nextState.XCoord 
						&& (currState.YCoord - 1 == nextState.YCoord || currState.YCoord + 1 == nextState.YCoord))
			{
				//agent moves at either right angle to the intended direction
				return 0.1;
			}else
			{
				return 0.0;
			}
		}else if(action == ACTION.UP)
		{
			if(currState.XCoord == nextState.XCoord && currState.YCoord + 1== nextState.YCoord)
			{
				//Intended outcome occurs
				return 0.8;
			}else if(currState.YCoord == nextState.YCoord 
						&& (currState.XCoord - 1 == nextState.XCoord || currState.XCoord + 1 == nextState.XCoord))
			{
				//agent moves at either right angle to the intended direction
				return 0.1;
			}else
			{
				return 0.0;
			}
		}else if(action == ACTION.DOWN)
		{
			if(currState.XCoord == nextState.XCoord && currState.YCoord - 1== nextState.YCoord)
			{
				//Intended outcome occurs
				return 0.8;
			}else if(currState.YCoord == nextState.YCoord 
						&& (currState.XCoord - 1 == nextState.XCoord || currState.XCoord + 1 == nextState.XCoord))
			{
				//agent moves at either right angle to the intended direction
				return 0.1;
			}else
			{
				return 0.0;
			}
		}else
		{
			System.out.println("bug");
			return -1.0;
		}
		
	}

}
