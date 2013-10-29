import java.util.ArrayList;


/*Main Class for playing the Game*/
public class Play {
	
	boolean MaxPlayer = true;
	boolean isMinimax = true;
	COLOR StartColor;
	
	static final int MINIMAX_DEPTH = 3;
	static final int ALPHA_BETA_DEPTH = 5;
	
	Tree myTree;
	private boolean firstRound;
	
	public Play(COLOR startColor, boolean isMinimax, Board board)
	{
		
		this.isMinimax = isMinimax;
		this.StartColor = startColor;
		this.firstRound = true;
	}
	
	/*
	 * Alpha-beta pruning
	 * depth should be 5
	 * player is always initially TRUE
	 * */
	public DataPackage alphaBeta(Board board, int depth, int alpha, int beta, boolean player)
	{
		if(depth == 0 || board.isOver())
		{
			return new DataPackage(board.computeUtility(this.StartColor), board);//this is eval func
		}
		
		COLOR childreanColor = this.StartColor;
		
		if(player != this.MaxPlayer)
		{
			if(this.StartColor == COLOR.BLUE)
			{
				childreanColor = COLOR.GREEN;
			}else
			{
				childreanColor = COLOR.BLUE;
			}
		}
		if(this.firstRound)
		{
			this.firstRound = false;
			childreanColor = this.StartColor;
		}
		ArrayList<Board> children = board.expandChildren(childreanColor);
		
		if(player == this.MaxPlayer)
		{
			Board b = null;
			for(Board childConfig: children)//all children of next level
			{
				int newUtil = alphaBeta(childConfig, depth - 1, alpha, beta, !player).Utility;
				if(alpha < newUtil){
					alpha = newUtil;
					b = childConfig;
				}
				if (beta < alpha)
				{
					break;
				}
			}
			
			return new DataPackage(alpha, b);
		}else
		{
			Board b = null;
			for(Board childConfig: children)//all children of next level
			{
				int newUtil = alphaBeta(childConfig, depth - 1, alpha, beta, !player).Utility;
				if(beta > newUtil)
				{
					beta = newUtil;
					b = childConfig;
				}
				if(beta < alpha)
				{
					break;
				}
			}
			
			return new DataPackage(beta, b);
		}
		
	}

	/*Minimax
	 * depth should be 3
	 * player is always initialized to be TRUE
	 * Evaluation function is equal to the score of the current board config for the player
	 * */
	public Board minimax(Board board, int depth, boolean player)
	{
		if(depth == 0 || board==null || board.isOver())
		{
			return board;
		}
		
		COLOR childreanColor = this.StartColor;
		
		if(player != this.MaxPlayer)
		{
			if(this.StartColor == COLOR.BLUE)
			{
				childreanColor = COLOR.GREEN;
			}else
			{
				childreanColor = COLOR.BLUE;
			}
		}
		if(this.firstRound)
		{
			this.firstRound = false;
			childreanColor = this.StartColor;
		}
		ArrayList<Board> children = board.expandChildren(childreanColor);
		
		Board retBoard = null;
		if(player == this.MaxPlayer)
		{
			int minimaxVal = Integer.MIN_VALUE;
			for(Board childBoard: children)
			{
				int maxVal = minimax(childBoard, depth - 1, !player).computeUtility(this.StartColor);
				if(minimaxVal < maxVal)
				{
					minimaxVal = maxVal;
					retBoard = childBoard;
				}
			}
		}else
		{
			int minimaxVal = Integer.MAX_VALUE;
			for(Board childBoard: children)
			{
				int minVal = minimax(childBoard, depth - 1, !player).computeUtility(this.StartColor);
				if(minimaxVal > minVal)
				{
					minimaxVal = minVal;
					retBoard = childBoard;
				}
			}
		}
		return new Board(retBoard);
	}
}