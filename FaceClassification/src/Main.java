import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;


public class Main {

	private static double[][] isFaceProbilities; //P(Fij = 1 | isFaceClass)
	private static double[][] nonFaceProbilities; //P(Fij = 1 | nonFaceClass)
	
	private static ArrayList<Face> trainingFaces;
	private static ArrayList<Face> testingFaces;
	
	private static int smoothingK = 1;
	private static final int FACE_HEIGHT = 70;
	private static final int FACE_WIDTH = 60;
	
	
	
	/*
	 * odds(Fij=1, c1, c2) = P(Fij=1 | c1) / P(Fij=1 | c2). c1 is Face class, c2 is Non-face class
	 * for the odds ratio map, you can use '+' to denote features with positive log odds,
	 * ' ' for features with log odds close to 1, and '-' for features with negative log odds
	 */
	public static void DrawOddsRatioImage()
	{
		System.out.println("face vs non-face odds ratio image:");
		for(int i = 0; i < FACE_HEIGHT; i++)
		{
			for(int j = 0; j < FACE_WIDTH; j++)
			{
				double odds = Math.log(isFaceProbilities[i][j]/nonFaceProbilities[i][j]);
				String denote = "#";
				if(odds >= -0.05 && odds <= 0.05)
				{
					denote = " ";
				}else if(odds > 0)
				{
					denote = "+";
				}else if(odds < 0)
				{
					denote = "-";
				}
				System.out.print(denote);
			}
			System.out.println();
		}
	}
	
	public static double[][] GetConfusionMatrix()
	{
		//row r and column c is the percentage of test images from class r that are classified as class c
		//matrix[0][0] -> percentage of test images from class IsFace that are classified as class IsFace
		//matrix[0][1] -> percentage of test images from class IsFace that are classified as class NonFace
		//matrix[1][0] -> percentage of test images from class NonFace that are classified as class IsFace
		//matrix[1][1] -> percentage of test images from class NonFace that are classified as class NonFace
		double[][] matrix = new double[2][2];
		
		//Calc matrix[0][0]
		double numIsFace = 0;
		double numClassifiedIsFaceInFace = 0;
		double numClassifiedNonFaceInFace = 0;
		double numNonFace = 0;
		double numClassifiedIsFaceInNonFace = 0;
		double numClassifiedNonFaceInNonFace = 0;
		for(Face f: testingFaces)
		{
			if(f.isFace)
			{
				numIsFace++;
				if(TestingSingleFace(f))
				{
					numClassifiedIsFaceInFace++;
				}else
				{
					numClassifiedNonFaceInFace++;
				}
			}else
			{
				numNonFace++;
				if(TestingSingleFace(f))
				{
					numClassifiedIsFaceInNonFace++;
				}else{
					numClassifiedNonFaceInNonFace++;
				}
			}
		}
		matrix[0][0] = numClassifiedIsFaceInFace/numIsFace;
		System.out.println("percentage of test images from class IsFace that are classified as class IsFace: " + matrix[0][0]);
		matrix[0][1] = numClassifiedIsFaceInNonFace/numNonFace;
		System.out.println("percentage of test images from class IsFace that are classified as class NonFace: " + matrix[0][1]);
		matrix[1][0] = numClassifiedNonFaceInFace/numIsFace;
		System.out.println("percentage of test images from class NonFace that are classified as class IsFace: " + matrix[1][0]);
		matrix[1][1] = numClassifiedNonFaceInNonFace/numNonFace;
		System.out.println("percentage of test images from class NonFace that are classified as class NonFace: " + matrix[1][1]);
		
		return matrix;
		
	}
	
	//returns true if classifier thinks testingFace is a face
	public static boolean TestingSingleFace(Face testingFace)
	{
		int[][] testingData = testingFace.faceImage;
		double isFacePosteriorVal = Math.log(0.5);
		double nonFacePosteriorVal = Math.log(0.5);
		for(int i = 0; i < FACE_HEIGHT; i++)
		{
			for(int j = 0; j < FACE_WIDTH; j++)
			{
				isFacePosteriorVal += Math.log(GetTrainingLikelyhood(i, j, testingData[i][j], true));
				nonFacePosteriorVal += Math.log(GetTrainingLikelyhood(i, j, testingData[i][j], false));
			}
		}
		
		return isFacePosteriorVal > nonFacePosteriorVal;
	}
	
	//true if classifier made the correct judgement
	public static boolean TestingSingleFaceCorrect(Face testingFace)
	{
		boolean isFace = TestingSingleFace(testingFace); 
		boolean isFaceActual = testingFace.isFace;
		return isFace == isFaceActual;
	}
	
	public static double Testing()
	{
		try {
			BufferedWriter out = new BufferedWriter(new FileWriter("WrongTestingFaces.txt"));
			
			int correctTests = 0;
			for(Face f: testingFaces)
			{
				if(TestingSingleFaceCorrect(f))
				{
					correctTests++;
				}else
				{
					String faceStr = f.Print();//printing wrong face
					out.write(faceStr);
					out.write("Above face is supposed to be " + f.isFace);
					out.newLine();
				}
			}
			//System.out.println((double)correctTests/(double)testingFaces.size());
			out.close();
			return (double)correctTests/(double)testingFaces.size();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return -1;
	}
	public static void Training()
	{
		//get all training data
		trainingFaces = FileParser.Parse("E:\\CS440Github\\FaceClassification\\src\\facedatatrain", 
				"E:\\CS440Github\\FaceClassification\\src\\facedatatrainlabels");
		

		//get all testing data
		testingFaces = FileParser.Parse("E:\\CS440Github\\FaceClassification\\src\\facedatatest", 
				"E:\\CS440Github\\FaceClassification\\src\\facedatatestlabels");

		isFaceProbilities = new double[FACE_HEIGHT][FACE_WIDTH];
		nonFaceProbilities = new double[FACE_HEIGHT][FACE_WIDTH];
		
		double isFaceClassSize = 0;
		double nonFaceClassSize = 0;
		for(Face tf: trainingFaces)
		{
			if(tf.isFace)
			{
				isFaceClassSize++;
			}else
			{
				nonFaceClassSize++;
			}
		}
		
		System.out.println("is face class size:" + isFaceClassSize);
		System.out.println("non face class size:" + nonFaceClassSize);
		
		for(int i = 0; i < FACE_HEIGHT; i++)
		{
			for(int j = 0; j < FACE_WIDTH; j++)
			{
				//checking pixel(i,j) for all faces
				double numHasValPixelsIsFace = 0;
				double numHasValPixelsNonFace = 0;
				double numNoValPixelsIsFace = 0;
				double numNoValPixelsNonFace = 0;
				
				for(Face tf: trainingFaces)
				{
					if(tf.isFace && tf.faceImage[i][j] == 1)
					{
						//pixel (i,j) has value # in training examples from IS-FACE class
						numHasValPixelsIsFace++;
					}
					if(tf.isFace && tf.faceImage[i][j] == 0)
					{
						//pixel (i,j) has value # in training examples from IS-FACE class
						numNoValPixelsIsFace++;
					}
					if(!tf.isFace && tf.faceImage[i][j] == 1)
					{
						numHasValPixelsNonFace++;
					}
					if(!tf.isFace && tf.faceImage[i][j] == 0)
					{
						numNoValPixelsNonFace++;
					}
				}
				
				isFaceProbilities[i][j] = (numHasValPixelsIsFace + smoothingK)/(isFaceClassSize + 2*smoothingK);
				//System.out.println("P(F_"+i + "," + j + "=1|IsFace)" + isFaceProbilities[i][j]);
				//System.out.println("P(F_"+i + "," + j + "=0|IsFace)" + numNoValPixelsIsFace/isFaceClassSize);
				nonFaceProbilities[i][j] = (numHasValPixelsNonFace + smoothingK)/(nonFaceClassSize + 2*smoothingK);
				//System.out.println("P(F_"+i + "," + j + "=1|NonFace)" + nonFaceProbilities[i][j]);
				//System.out.println("P(F_"+i + "," + j + "=0|NonFace)" + numNoValPixelsNonFace/nonFaceClassSize);
			}
		}
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		//init everything
		Training();		
		System.out.println("training over");
		//Testing();
		TestingWithDiffSmoothing();
		//System.out.println(Math.log(4));
		GetConfusionMatrix();
		DrawOddsRatioImage();
	}
	
	public static void TestingWithDiffSmoothing()
	{
		double maxCorrectRate = Integer.MIN_VALUE;
		int bestK = smoothingK;
		while(smoothingK <= 50)
		{
			double rate = Testing();
			if( rate > maxCorrectRate)
			{
				maxCorrectRate = rate;
				bestK = smoothingK;
			}
			smoothingK++;
		}
		
		System.out.println("Rate: " + maxCorrectRate + "; K:" + bestK);
	}
	//Get P(Fij = f | class) 
	public static double GetTrainingLikelyhood(int i, int j, int f, boolean isFaceClass)
	{
		if(f == 1 && isFaceClass)
		{
			return isFaceProbilities[i][j];
		}
		else if(f == 0 && isFaceClass)
		{
			return 1 - isFaceProbilities[i][j];
		}
		else if(f == 1 && !isFaceClass)
		{
			return nonFaceProbilities[i][j];
		}
		else if(f == 0 && !isFaceClass)
		{
			return 1 - nonFaceProbilities[i][j];
		}
		
		return -1.0;
	}
}
