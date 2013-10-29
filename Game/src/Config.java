/**
 * 
 */


import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;

import static java.lang.System.*;
/**
 * @author zhenchengwang
 *
 */
public class Config {

	
	public Board mainBoard;
	
	public Config(String boardName) throws IOException{
		this.mainBoard = new Board(boardName);
	}

	public void XuPlay()
	{
		
		Board step = this.mainBoard;
		int alpha = Integer.MIN_VALUE;
		int beta = Integer.MAX_VALUE;
		boolean isBlue = true;
		DataPackage dp = new DataPackage(-1, step);
		while (step.isOver() == false){
			Play game;
			if(isBlue)
			{
				game = new Play(COLOR.BLUE, true, step);
				step = game.minimax(step, 3, true);
				//dp = game.alphaBeta(step, 5, alpha, beta, true);
			}else
			{
				game = new Play(COLOR.GREEN, true, step);
				step = game.minimax(step, 3, true);
				//step = game.alphaBeta(step, 5, alpha, beta, true).board;
			}
			//step = new Board(dp.board);
			step.printGameBoard();
			int []result=step.bothUtility();
			System.out.println("blue is "+result[0]);
			System.out.println("green is "+result[1]);
			isBlue = !isBlue;
		}
	}
	
	public void play(){
		
		int count = 0;
		System.out.println("start");
		mainBoard.printGameBoard();
		while(!this.mainBoard.isOver()){
			if(count%2 == 0) //blue
			{
				Tree minmaxTree=new Tree(3, COLOR.BLUE, mainBoard);
				ArrayList<Board> parent=new ArrayList<Board>();
				parent.add(minmaxTree.root);
				minmaxTree.printTree(parent);
				Board newBoard=minmaxTree.getBoard();
				mainBoard=new Board(newBoard);
				System.out.println("blue goes");
				mainBoard.printGameBoard();
			}
			else{ // green
				Tree minmaxTree=new Tree(3, COLOR.GREEN, mainBoard);
				ArrayList<Board> parent=new ArrayList<Board>();
				parent.add(minmaxTree.root);
				minmaxTree.printTree(parent);
				Board newBoard=minmaxTree.getBoard();
				mainBoard=new Board(newBoard);
				System.out.println("green goes");
				mainBoard.printGameBoard();
			}
				
			count++;
		}
		int []result=mainBoard.bothUtility();
		System.out.println("blue is "+result[0]);
		System.out.println("green is "+result[1]);
		
	}
	public static void main(String[] args) throws IOException{
		Config game=new Config("E:\\CS440Github\\Game\\src\\Smolensk.txt");
		game.XuPlay();
		
	}
	
}
