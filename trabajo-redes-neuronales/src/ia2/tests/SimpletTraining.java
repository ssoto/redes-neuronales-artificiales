package ia2.tests;

import ia2.classes.DataExample;
import ia2.classes.DataSet;
import ia2.classes.RNA;

import java.util.Random;

public class SimpletTraining {

	/**
	 * @param args
	 */

	public static void start(String fileName){
		DataSet ds = new DataSet();
		ds.readFile(fileName);
		
		RNA redNeuronal = new RNA(ds.getNumInputs(), 
								  new int[] {6, ds.getNumOutputs()});
		redNeuronal.getLayer(1).setIsSigmoid(false);
		DataExample[] de_array = ds.getTestExamples();
		int epochEnd = 1000;
		Random r = new Random();
		
		for (int e = 0; e < epochEnd; e++) {
			DataExample currentExample =null;
			for (int example = 0; example < ds.getTestExamplesSize(); example++) {
				currentExample = de_array[example];
				redNeuronal.train( currentExample.getInputs(), 
								   currentExample.getOutputs(), 
								   0.3f, 0.6f);
			}
			if ((e + 1) % 20 == 0) {
				System.out.printf("===================\nÉpoca %d \n", e + 1);
				String valoresEjemplo="", valoresReales="", valoresEntrada="";
				
				int exmpIndex = r.nextInt(ds.getTestExamplesSize());
				
				System.out.println("Elegimos el ejemplo "+exmpIndex);
				DataExample exmpl = ds.getTestExamples()[exmpIndex];
				
				
				for (int i = 0; i < ds.getNumInputs();i++) {
					valoresEntrada += ""+exmpl.getInputs()[i]+", ";
				}
				System.out.println("con valores de entrada: "+valoresEntrada);
				
				for (int i = 0; i < ds.getNumOutputs();i++) {
					valoresEjemplo += ""+exmpl.getOutputs()[i]+", ";
				}
				
				float[] result = redNeuronal.run(exmpl.getInputs());
				
				for (int i=0; i<ds.getNumOutputs(); i++)
					valoresReales += result[i]+", ";
				
				System.out.printf("Target: %s --> RealOutput %s\n", 
									valoresEjemplo, valoresReales );
				
			}
		}

	}

	public static void main(String[] args) throws Exception {
		System.out.println(args.length);
		if (args.length != 1)
			System.err.println("Es necesario indicar un nombre de archivo");
		else
			start(args[0]);
			
	}
}
