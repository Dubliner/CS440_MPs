
public class Face {

	public int [][] faceImage;
	public boolean isFace;
	
	public Face()
	{
		this.faceImage = new int [70][60];
		this.isFace = false;
	}
	
	public String Print()
	{
		String face = "";
		for(int i = 0; i < 70; i ++)
		{
			for(int j = 0; j < 60; j++)
			{
				if(this.faceImage[i][j]==0){
					face += " ";
				}else if(this.faceImage[i][j]==1)
				{
					face += "#";
				}
			}
			face += "\n";
		}
		return face;
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
