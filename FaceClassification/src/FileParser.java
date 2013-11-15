import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;


public class FileParser {

	public static ArrayList<Face> Parse(String dataFilePath, String labelFilePath)
	{
	    try {
	    
	      BufferedReader lbr = new BufferedReader(new FileReader(labelFilePath));
		  BufferedReader dbr = new BufferedReader(new FileReader(dataFilePath));

	      ArrayList<Face> ret = new ArrayList<Face>();
	      
	      String label;
	      while ((label = lbr.readLine()) != null) {
	    	
	    	Face face = new Face();
	        int lineCounter = 0;
	        
	        if(label.charAt(0)=='0') //non-face
	        {
	        	face.isFace = false;
	        }else if(label.charAt(0) == '1')
	        {
	        	face.isFace = true;
	        }
	        
	        while(lineCounter < 70)
	        {
	        	String lineInFace = dbr.readLine();
	        	for(int i = 0; i < lineInFace.length(); i++)
	        	{
	        		if(lineInFace.charAt(i) == ' ') //pixel is NOT set
	        		{
	        			face.faceImage[lineCounter][i] = 0;
	        		}else if(lineInFace.charAt(i) == '#') //pixel is set
	        		{
	        			face.faceImage[lineCounter][i] = 1;
	        		}
	        	}
	        	lineCounter++;
	        }
	        
	        ret.add(face);
	      }

	      // dispose all the resources after using them.
	      dbr.close();
	      lbr.close();
	      
	      //return all faces
	      return ret;

	    } catch (FileNotFoundException e) {
	      e.printStackTrace();
	    } catch (IOException e) {
	      e.printStackTrace();
	    }
	    return null; //exception happened
	}
}

