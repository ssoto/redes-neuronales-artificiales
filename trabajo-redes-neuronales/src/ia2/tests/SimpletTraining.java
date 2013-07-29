package ia2.tests;

import ia2.classes.DataExample;
import ia2.classes.DataSet;
import ia2.classes.RNA;

public class SimpletTraining {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		DataSet ds = new DataSet();
		ds.readFile("./resources/dataSets/cancer/cancer1.dt");
		
		RNA redNeuronal = new RNA(ds.getNumInputs(), 
								  new int[] {3, ds.getNumOutputs()});
		redNeuronal.getLayer(1).setIsSigmoid(false);
		DataExample[] de_array = ds.getTestExamples();
		int epochEnd = 100;
		
		for (int e = 0; e < epochEnd; e++) {
			DataExample currentExample =null;
			for (int example = 0; example < ds.getTestExamplesSize(); example++) {
				currentExample = de_array[example];
				redNeuronal.train( currentExample.getInputs(), 
								   currentExample.getOutputs(), 
								   0.3f, 0.6f);
			}
			if ((e + 1) % 5 == 0) {
				System.out.printf("===================\nÉpoca %d \n", e + 1);
				String valoresEntrada="", valoresReales="";
				
				for (int i = 0; i < ds.getNumOutputs();i++) {
					valoresEntrada += ""+currentExample.getOutputs()[i]+", ";
				}
				
				float[] result = redNeuronal.run(currentExample.getInputs());
				valoresReales += result[0]+", "+result[1];
				
				System.out.printf("Target: %s --> RealOutput %s\n", 
									valoresEntrada, valoresReales );
				
			}
		}

	}

}
