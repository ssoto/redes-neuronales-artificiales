package ia2.tests;

import ia2.classes.DataSet;
import ia2.classes.RNA;

public class SimpletTraining {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		DataSet ds = new DataSet();
		ds.readFile("./resources/dataSets/cancer/cancer1.dt");
		RNA redNeuronal = new RNA(ds.getNumInputs(), new int[] {10, ds.getNumOutputs()});
		redNeuronal.getLayer(1).setIsSigmoid(false);
		float[][] testExamplesInput = ds.getTestExamplesInput();
		float[][] testExamplesOutput = ds.getTestExamplesOutput();
		int epochEnd = 500;
		for (int e = 0; e < epochEnd; e++) {
			
			for (int example = 0; example < ds.getTrainingExamplesSize(); example++) {
				redNeuronal.train(testExamplesInput[example], testExamplesOutput[example], 
									0.3f, 0.6f);
			}
		}

	}

}
