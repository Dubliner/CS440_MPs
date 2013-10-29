/**
 * 
 */

import java.util.ArrayList;

/**
 * @author zhenchengwang
 *
 */
public class Tree {

	public Board root;
	private COLOR bottom=COLOR.WHITE;

	public Tree(int depth, COLOR myColor, Board mainBoard){
		this.root = new Board(mainBoard);
		/*
		if (root==null)
			System.out.println("root is null");
		else System.out.println("root is not null");
		*/
		//this.root.computeUtility();
		ArrayList<Board> parentList = new ArrayList<Board>(); 
		parentList.add(this.root);
		
		while(depth > 0){
			
			ArrayList<Board> childrenList=new ArrayList<Board>();
			//System.out.println("MYCLOOROR:" + myColor);
			
			for(int k = 0; k < parentList.size(); k ++){	// for each parent
				Board curr = parentList.get(k);
				for(int i = 0;i< curr.board.size(); i ++)	// for each white cell on this parent
				{					
					for(int j = 0; j < curr.board.size(); j++)
					{
						Cell c = curr.board.get(i).get(j);
						
						if(c.color == COLOR.WHITE)
						{							
							Board child = new Board(curr);//deep copy parent
							child.setBoard(myColor, i, j);
							//child.computeUtility();
							curr.children.add(child);
							child.parent = curr;
							childrenList.add(child);	
						}
					}					
				}

			}
			parentList=childrenList;
			depth--;
			if (myColor==COLOR.BLUE)
				myColor=COLOR.GREEN;
			else myColor=COLOR.BLUE;
		}
		
		
	}
	public void printTree(ArrayList<Board> parent)
	{
		
		ArrayList<Board> children=new ArrayList<Board>();
		for (int i=0;i<parent.size();i++)
		{
			Board currParent=parent.get(i);
			if(currParent.children.size()==0)
				return;
			System.out.print("Parent : ");
			currParent.printGameBoard();
			
			for (int j=0;j<currParent.children.size();j++)
			{
				Board currChild=currParent.children.get(j);
				currChild.printGameBoard();
				int result[]=currChild.bothUtility();
				
				System.out.println("blue is "+result[0]);
				System.out.println("green is "+result[1]);
				children.add(currChild);
			}
		}
		printTree(children);
	}
//	minimax(node) = 
//			utility(node) if node is terminal
//			max( minimax(succ(node, action))) if player = max
//			min( minimax(succ(node, action))) if player = min
//	
	public int[] getUtility( int [] utility,boolean isMax)//might be problem here
	{
		int [] result=new int [2];
		int curr=utility[0];
		if(isMax)//find the max value in utility
		{
			for(int i=1;i<utility.length;i++)
			{
				if(utility[i]>curr){
					curr=utility[i];
					result[1]=i;
				}
			}
		}
		else
		{	
			for(int i=1;i<utility.length;i++)
			{
				if(utility[i]<curr){
					curr=utility[i];
					result[1]=i;
				}
			}
		}
		result[0]=curr;
		
		return result;
	}
	
	public int[] minmax(Board curr)
	{
		
		// base case
		if(curr.children.size()==0||curr.isOver())//has no children
		{
			this.bottom=curr.player;
			int [] result=new int [2];
			result[0]=curr.computeUtility(curr.player);
			return result; 
			
		}
		// recursive case
		ArrayList<Board> myChildren=curr.children;
		int mySize=myChildren.size();
		int [] utility=new int [mySize];
		for (int i=0;i<mySize;i++)
		{
			
			int [] tmp=minmax(myChildren.get(i));
		
			utility[i]=tmp[0];
		}
		COLOR childColor=curr.children.get(0).player;
		if(bottom==childColor)
			return getUtility(utility,true);
		else 
			return getUtility(utility,false);
		
	}
	
	public Board getBoard()
	{
		int[] result= minmax(root);
		return this.root.children.get(result[1]);
	}

}
