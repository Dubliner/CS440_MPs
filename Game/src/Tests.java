

import java.io.IOException;
import java.util.ArrayList;

public class Tests {
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			Board root = new Board("src/warBoard/Sevastopol.txt");
			
			Tree tree = new Tree(3, COLOR.BLUE,root);
			ArrayList<Board> parent=new ArrayList<Board>();
			parent.add(tree.root);
			tree.printTree(parent);
			int result[]=tree.minmax(tree.root);
			System.out.println(result[0]);
			System.out.println(result[1]);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
