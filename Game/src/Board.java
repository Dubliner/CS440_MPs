
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class Board {

	
	public List<List<Cell>> board;	
	public Board parent;
	public ArrayList<Board> children;
	//public int greenUtil;
	//public int blueUtil;
	public COLOR player;
	public int boardSize;
	
	public Board(String filename) throws IOException{
		board = new ArrayList<List<Cell>>();
		BufferedReader br = new BufferedReader(new FileReader(filename));
		String line;
		while((line = br.readLine()) != null){
			String[] currLine = line.split("\n")[0].split("\t");
			Arrays.asList(currLine);
			List<Cell> boardRow = new ArrayList<Cell>();
			for(String curr:currLine){ 
				Cell currCell = new Cell(Integer.valueOf(curr));
				boardRow.add(currCell);				
			}		
			board.add(boardRow);
		}
		parent = null;
		children = new ArrayList<Board>();
		//greenUtil = 0;
	//	blueUtil = 0;
		player=COLOR.WHITE;
		boardSize=board.size();
	}
	
	
	/**
	 * Usage: 1, construct tree with root
	 * 	      2, make this node inherit from parent and distinguish from other children
	 * @param src
	 */
	public Board(Board src){
		
		List<List<Cell>> myboard = new ArrayList<List<Cell>>();
		for(int i=0; i<src.board.size(); i++){
			List<Cell> currRow = new ArrayList<Cell>();
			for(int j=0; j<src.board.size(); j++){
				Cell curr = new Cell(src.board.get(i).get(j));
				currRow.add(curr);
			}
			myboard.add(currRow);
		}
		this.board=myboard;
		this.parent = null;	// parent is null, for each level, parent is not grand parent
		this.children = new ArrayList<Board>();// children is empty, we will add children in buildTree
		//greenUtil = 0; // will be recompute for children
		//blueUtil = 0;  // will be recompute for children
		this.player=COLOR.WHITE;
		this.boardSize=src.boardSize;
		
	}
	
	public void tryDeathBlitz(COLOR myColor, int x, int y)
	{	COLOR enemy;
		if (myColor==COLOR.BLUE)
			enemy=COLOR.GREEN;
		else enemy=COLOR.BLUE;
		

		this.board.get(x).get(y).color = myColor;
		
		if (isConnected(myColor,x,y))
		{
			if(isInBound(x+1,y)){
				Cell curr=this.board.get(x+1).get(y);
				if(curr.color==enemy)
					curr.color=myColor;
			}
			if(isInBound(x-1,y)){
				Cell curr=this.board.get(x-1).get(y);
				if(curr.color==enemy)
					curr.color=myColor;
			}
			if(isInBound(x,y-1)){
				Cell curr=this.board.get(x).get(y-1);
				if(curr.color==enemy)
					curr.color=myColor;
			}
			if(isInBound(x,y+1)){
				Cell curr=this.board.get(x).get(y+1);
				if(curr.color==enemy)
					curr.color=myColor;
			}
		}
		
	}
	public boolean isConnected(COLOR myColor, int x, int y)
	{
		if(isInBound(x+1,y)&&myColor==this.board.get(x+1).get(y).color)
			return true;
		if(isInBound(x-1,y)&&myColor==this.board.get(x-1).get(y).color)
			return true;
		if(isInBound(x,y+1)&&myColor==this.board.get(x).get(y+1).color)
			return true;
		if(isInBound(x,y-1)&&myColor==this.board.get(x).get(y-1).color)
			return true;
		return false;
	}
	public boolean isInBound ( int x, int y)
	{
		if(x<0||x>=boardSize||y<0||y>=boardSize)
			return false;
		return true;
	}
	
	public boolean isOver(){
		
		for(int i=0; i<boardSize; i++){
			for(int j=0; j<boardSize; j++){
				Cell c = this.board.get(i).get(j);
				if(c.color == COLOR.WHITE)
					return false;
			}			
		}
		return true;
	}
	
	public void setBoard(COLOR color, int x, int y){
		player=color;
		Cell c = this.board.get(x).get(y); // check valid when play
		c.color = color;//set the color of current cell
		tryDeathBlitz(color, x, y);
		
	}
	
	public int computeUtility(boolean isBlue)
	{
		COLOR player = COLOR.GREEN;
		if(isBlue)
		{
			player = COLOR.BLUE;
		}
		
		return computeUtility(player);
	}
	
	public int computeUtility(COLOR player){
		int blueUtil = 0;
		int greenUtil = 0;
		for(int i = 0; i < boardSize; i++)
		{
			for(int j = 0; j <boardSize; j++)
			{
				COLOR myColor=this.board.get(i).get(j).color;
				if( myColor== COLOR.BLUE)
				{
					blueUtil += this.board.get(i).get(j).weight;
				}else if(myColor == COLOR.GREEN)
				{
					greenUtil += this.board.get(i).get(j).weight;
				}
			}
		}
		if(player==COLOR.BLUE)
			return blueUtil-greenUtil;
		else return greenUtil-blueUtil;
	}
	public int[] bothUtility(){
		int blueUtil = 0;
		int greenUtil = 0;
		for(int i = 0; i < boardSize; i++)
		{
			for(int j = 0; j <boardSize; j++)
			{
				COLOR myColor=this.board.get(i).get(j).color;
				if( myColor== COLOR.BLUE)
				{
					blueUtil += this.board.get(i).get(j).weight;
				}else if(myColor == COLOR.GREEN)
				{
					greenUtil += this.board.get(i).get(j).weight;
				}
			}
		}
		int[] result=new int[2];
		result[0]=blueUtil;result[1]=greenUtil;
		return result;
	}
	
	public void printGameBoard(){

		for(int i=0; i<boardSize; i++){
			System.out.println("\n");
			for(int j=0; j<board.get(0).size(); j++){
				System.out.print("(" + board.get(i).get(j).weight + ", " + board.get(i).get(j).color + ")" + " ");
			}
		}
		
		System.out.println("---------------------");
	}


	public ArrayList<Board> expandChildren(COLOR childColor) {
		Board root = this;
		ArrayList<Board> children = new ArrayList<Board>();
		
		//System.out.println("EXPANDING CHILDREN...");
		for(int i = 0;  i < root.boardSize; i++)
		{
			for(int j = 0; j < root.boardSize; j++)
			{
				if(root.board.get(i).get(j).color == COLOR.WHITE)
				{
					Board child = new Board(root);	
					child.setBoard(childColor, i, j);
					child.parent = root;
					child.player = childColor;
					children.add(child);
					//child.printGameBoard();
				}
			}
		}

		//System.out.println("EXPANDING CHILDREN FINISHED...");
		return children;
	}

}
