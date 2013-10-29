/**
 * 
 */


/**
 * @author zhenchengwang
 *
 */
public class Cell {

	public COLOR color;
	public int weight;
	
	public Cell(int weight){
		color = COLOR.WHITE;
		this.weight = weight;
	}
	public Cell(Cell src){
		this.color = src.color;
		this.weight = src.weight;
	}
	
}
