package ia2.mains;

import ia2.classes.DataExample;
import ia2.classes.DataSet;
import ia2.classes.RNA;

import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.Random;

public class SimpletTraining {

	private static final int _MAX_EPOCHS = 3000;
	private static final int _VERIFY_EPOCHS = 5;
	private static final int _HYDEN_SIZE = 8;
	
	/**
	 * @param args
	 */

	public static void start(String fileName, int hydenLayerSize){
		Random r = new Random();
		double minSqrError = Double.MAX_VALUE;
		int minSqrErrorEpoch = -1;
		
		DataSet ds = new DataSet();
		ds.readFile(fileName);
		
		RNA redNeuronal = new RNA(ds.getNumInputs(), 
								  new int[] {hydenLayerSize, ds.getNumOutputs()});
		redNeuronal.getLayer(1).setIsSigmoid(false);
		
		
		for (int e = 0; e < _MAX_EPOCHS; e++) {
			// entrenamos sobre trainingSet
			traing_over_dataset(ds.getTrainingExamplesSize(), redNeuronal, ds.getTrainingExamples());
			
			if ((e % _VERIFY_EPOCHS == 0)) {
				print_result(ds, redNeuronal, r, e);
				
				double currentSqrError = redNeuronal.squaredError( ds.getValidationExamples());
//				System.out.printf("\terror cuadrático: %f", currentSqrError);
				if (currentSqrError < minSqrError){
					minSqrErrorEpoch = e;
					minSqrError = currentSqrError;
				}
				
			}
		}
		System.out.printf("El menor error cuadrático fué %f en época %d", minSqrError, minSqrErrorEpoch);
		System.out.println("");

	}

	private static void traing_over_dataset(int size, RNA redNeuronal,
			DataExample[] de_array) {
		DataExample currentExample =null;
		for (int example = 0; example < size; example++) {
			currentExample = de_array[example];
			redNeuronal.train( currentExample.getInputs(), 
							   currentExample.getOutputs(), 
							   0.3f, 0.6f);
		}
	}

	private static void print_result(DataSet ds, RNA redNeuronal, Random r,
			int e) {
		
		String valoresEjemplo="", valoresReales="";
		
		int exmpIndex = r.nextInt(ds.getTrainingExamplesSize());
		
		DataExample exmpl = ds.getTrainingExamples()[exmpIndex];
		
				
		for (int i = 0; i < ds.getNumOutputs();i++) {
			valoresEjemplo += ""+exmpl.getOutputs()[i]+", ";
		}
		
		float[] result = redNeuronal.run(exmpl.getInputs());
		
		for (int i=0; i<ds.getNumOutputs(); i++)
			valoresReales += result[i]+", ";
//		System.out.printf("===================\nÉpoca %d=> esperado: %s --> RealOutput %s\n", 
//							e, valoresEjemplo, valoresReales );
	}
	
	public static void main(String[] args) throws Exception {
		
		PrintStream out = new PrintStream(new FileOutputStream("./salida_entrenamiento.txt"));
		System.setOut(out);
		
		for (int hydenSize = 0; hydenSize < 15; hydenSize++) {
			System.out.println("===================================================================");
			System.out.printf("Prueba con red neuronal con %d capas intermedias:", hydenSize);
			System.out.println();
			start("./resources/dataSets/cancer/cancer1.dt", hydenSize);
			start("./resources/dataSets/cancer/cancer2.dt", hydenSize);
			start("./resources/dataSets/cancer/cancer3.dt", hydenSize);
			System.out.println("===================================================================");
		}
			
	}

	public static void main1(String[] args) throws Exception {
		System.out.println(args.length);
		if (args.length != 1)
			System.err.println("Es necesario indicar un nombre de archivo");
		else
			start(args[0], _HYDEN_SIZE);
			
	}
}
