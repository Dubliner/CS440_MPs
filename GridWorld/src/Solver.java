import java.util.ArrayList;


/*
 * Different solutions for solving the Grid World
 * */
public class Solver {

	private Game game;
	
	
	public Solver(Game game)
	{
		this.game = game;
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		Game game = new Game();
		Solver gameSolver = new Solver(game);
		gameSolver.ValueIteration();
		game.printUtilities();
		gameSolver.ReinforcementLearning();
		//game.printQLearning();
		game.printUtilities();
	}
	
	
	private void SetNewUtilityWithBellman(State currState)
	{
		double discountFactor = 0.7;
		double maxExpectedUtility = Integer.MIN_VALUE;

		
		for(ACTION action : this.game.GetPossibleActions(currState))
		{
			double sumU = 0.0;
			for(State nextState: this.game.GetNeighborStates(currState))
			{
				if(!nextState.IsWall){
					sumU += this.game.GetProbability(currState, action, nextState) * nextState.GetUtility();
				}else
				{
					sumU += this.game.GetProbability(currState, action, nextState) * currState.GetUtility();
				}
			}
			
			if(sumU > maxExpectedUtility)
			{
				maxExpectedUtility = sumU;
				currState.OptimalPolicy = action;
			}
		}
		
		double newUtil = currState.GetReward() + discountFactor * maxExpectedUtility;
		currState.SetUtility(newUtil);
	}
	
	public void ValueIteration()
	{
		int numIteration = 50;
		int iter = 0;
		
		while(iter < numIteration)
		{
			//Update the utility of each state according to Bellman Equation:
			for(int i = 0; i < this.game.States.length; i++)
			{
				for(int j = 0; j < this.game.States[0].length; j++)
				{
					State s = this.game.States[i][j];
					if(!s.IsWall && !s.IsTerminal)
					{
						this.SetNewUtilityWithBellman(this.game.States[i][j]);
						
					}
				}	
			}
			iter++;
		}
	}

	
	public void ReinforcementLearning()
	{
		int step = 1;
		State currState = this.game.StartState;
		
		while(step < 400000)
		{	
			currState.TimeStep = currState.TimeStep + 1;
			ACTION selectedAction = this.selectAction(currState);
			currState.CountActionTaken(selectedAction);
			State nextState = this.getSuccessorState(currState, selectedAction);
			this.TDUpdate(currState, nextState, selectedAction);
		
			currState = nextState;
			if(currState.IsTerminal)
			{
				currState = this.game.StartState;
			}
			
			step++;
		}
	}
	
	private double explorationFuction(double expectedUtil, int numTimesTaken)
	{
		int Ne = 50; 
		if (numTimesTaken < Ne)
		{
			//return Integer.MAX_VALUE;
			return (double)Ne*1.5/(double)numTimesTaken;
		}else
		{
			return expectedUtil;
		}
	}
	
	private double getAlpha(int timeStep)
	{
		return 60.0/(59.0 + timeStep);//TODO: TRY DIFF
	}
	
	private void TDUpdate(State currState, State succState, ACTION selectedAction) 
	{
		double alpha = this.getAlpha(currState.TimeStep);
		double currQ = currState.GetQValue(selectedAction);
		double discountFactor = 0.7;
		double maxQForSucc;
		double newCurrQ = 0.0;
		maxQForSucc = succState.GetUtility();
		
		newCurrQ = currQ + alpha*(currState.GetReward() + discountFactor * maxQForSucc - currQ );
		currState.UpdateQValues(selectedAction, newCurrQ);
	}
	
	private State getSuccessorState(State currState, ACTION selectedAction) 
	{
		double rand = Math.random();
		int nextXCoord = 0;
		int nextYCoord = 0;
		
		if(rand < 0.1)
		{
			//System.out.println("0-0.1");
			if(selectedAction == ACTION.UP)
			{//GO LEFT
				nextXCoord = currState.XCoord - 1;
				nextYCoord = currState.YCoord;
			}else if(selectedAction == ACTION.DOWN)
			{//GO RIGHT
				nextXCoord = currState.XCoord + 1;
				nextYCoord = currState.YCoord;
			}else if(selectedAction == ACTION.LEFT)
			{//GO DOWN
				nextXCoord = currState.XCoord;
				nextYCoord = currState.YCoord - 1;
			}else
			{//GO UP
				nextXCoord = currState.XCoord;
				nextYCoord = currState.YCoord + 1;
			}
		}else if(rand < 0.2)
		{
			//System.out.println("0.1-0.2");
			if(selectedAction == ACTION.UP)
			{//GO right
				nextXCoord = currState.XCoord + 1;
				nextYCoord = currState.YCoord;
			}else if(selectedAction == ACTION.DOWN)
			{//GO left
				nextXCoord = currState.XCoord - 1;
				nextYCoord = currState.YCoord;
			}else if(selectedAction == ACTION.LEFT)
			{//GO up
				nextXCoord = currState.XCoord;
				nextYCoord = currState.YCoord + 1;
			}else
			{//GO down
				nextXCoord = currState.XCoord;
				nextYCoord = currState.YCoord - 1;
			}
		}else
		{ //go according to action
			//System.out.println("0.2-1");
			if(selectedAction == ACTION.UP)
			{
				nextXCoord = currState.XCoord;
				nextYCoord = currState.YCoord + 1;
			}else if(selectedAction == ACTION.DOWN)
			{
				nextXCoord = currState.XCoord;
				nextYCoord = currState.YCoord - 1;
			}else if(selectedAction == ACTION.LEFT)
			{
				nextXCoord = currState.XCoord - 1;
				nextYCoord = currState.YCoord;
			}else
			{
				nextXCoord = currState.XCoord + 1;
				nextYCoord = currState.YCoord;
			}
		}
		if (!(nextXCoord >= 0 && nextXCoord < this.game.States.length 
				&& nextYCoord >=0 && nextYCoord < this.game.States[0].length))
		{
			return currState;
		}
		
		if(!this.game.States[nextXCoord][nextYCoord].IsWall)
		{
			return this.game.States[nextXCoord][nextYCoord];
		}else
		{
			return currState;
		}
	}
	
	private ACTION selectAction(State currState) {
		double max = Integer.MIN_VALUE;
		ACTION ret = ACTION.UP;
		//this.game.GetPossibleActions(currState)
		for(ACTION action:ACTION.values())
		{
			double v = this.explorationFuction(currState.GetQValue(action), currState.GetActionTakenCount(action));
			if(v > max)
			{
				ret = action;
				max = v;
			}
		}
		return ret;
	}
	
	
}
