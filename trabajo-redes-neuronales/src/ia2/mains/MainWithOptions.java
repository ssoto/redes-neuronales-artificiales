package ia2.mains;

import java.util.LinkedList;
import java.util.List;

import javax.lang.model.type.NullType;

import ia2.classes.DataExample;
import ia2.classes.DataSet;
import ia2.classes.RNA;

import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;

/**
 * @author Sergio Soto
 * 
 * clase que parsea las opciones que se introducen por comando y
 * entrena la red enuronal. Es posible especificar las siguientes opciones:
 * 
 * @param hydeLayer: dimensión de la capa oculta
 * @param momentum: valor para el momentum
 * @param learningRate: coeficiente de aprendizaje del algoritmo de retropropagación
 * @param dataFile: archivo con ejemplos de PROBEN1
 *
 */

public class MainWithOptions {
	
	private static final int _MAX_EPOCHS = 3000;
	private static final int _VERIFY_EPOCHS = 5;
	private static final int _RUNS = 10;
	
	private static final double _PROGRESS_SANK = 0.1;
	private static final float _LEARNING_RATE = 0.1f;
	private static final float _MOMENTUM = 0.5f;
	private static final String _MY_APP = "red-neuronal";
	
	
	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
				
		// cadenas de nombres de opciones
		String 	hyde = "hydeLayers"; 
		String	momentum = "momentum";
		String	learningRate = "learningRate";
		String 	fileOption = "dataFile";
		
		String header = "Ejecutable para entrenar una red neuronal a partir \nde un archivo perteneciente a PROBEN1\n";
		String footer = "\nProyecto para la asignatura IA2\nAlojado en https://github.com/ssoto/redes-neuronales-artificiales\n\n";
		 
		
		// varialbes donde guardar parámetros de entrada
		float learningRateRatio, momentumRatio;
		int[] hydenLayer = null;
		String proben1File;
		String hydenLayerSizeString;
		
		// se configuran las opciones que queremos recoger por la línea de comandos		
		Options options = new Options();
		
		Option dataOption = new Option(fileOption, true, "fichero de datos sobre el que realizar las operaciones de entrenamiento, validación cruzada y calculo de error");
		dataOption.setRequired(true);
		options.addOption(dataOption);
		
		
		Option hydeOption = new Option(hyde, true, "Numero de neuronas en la(s) capas oculta.\nSi es una única capa oculta se indica valor 0.\nSi hay mas de una capa oculta se indican dos valores numéricos \nseparados por coma, en este caso no se permite valor 0");
		options.addOption(hydeOption);

		
		String momentumHelp = "valor para momentum. Por defecto valdrá "+_MOMENTUM+". Recuerde que es un tipo double: 0.3 (pe)";
		options.addOption( 	momentum, 
							true, 
							momentumHelp);
		
		String learningRateHelp = "valor para factor de aprendizaje. Por defecto valdrá "+_LEARNING_RATE+"\n. Recuerde que es un tipo double: 0.3 (pe)";
		options.addOption( 	learningRate,
							true, 
							learningRateHelp);
		
		options.addOption(  "h", "help", false, "Muestra este mensaje" );
		
		
		// ahora parseamos las opciones
		CommandLineParser parser = new BasicParser();
		
		try {
			CommandLine cmd = parser.parse( options, args);
			if(cmd.hasOption("h")){
				new HelpFormatter().printHelp("myapp", header, options, footer, true);
                return;
			}
			else{
				if (!cmd.hasOption(fileOption)){
					new HelpFormatter().printHelp("myapp", header, options, footer, true);
	                return;
				}
				else{
					proben1File = cmd.getOptionValue(fileOption);
				}
				// tratamos numero de elementos de la capa oculta
				hydenLayerSizeString="";
				if (cmd.hasOption(hyde)){
					String[] result;
					String hydeLayers = cmd.getOptionValue(hyde);
					if(hydeLayers.contains(",")){
						result = hydeLayers.split(","); 
						hydenLayer= new int[result.length];
						for (int i=0 ; i<result.length; i++) {
							hydenLayer[i] = Integer.parseInt(result[i]);
							hydenLayerSizeString += result[i]+"+";
						}
						// le quitamos el ultimo + añadido
						hydenLayerSizeString = hydenLayerSizeString.substring(0, hydenLayerSizeString.length()-1);
					}
					
					else{
						// tiene dimension 2 porque es la oculta y 
						// una mas para la salida que se inicializa después
						hydenLayer = new int[2];
						hydenLayer[0] = Integer.parseInt(cmd.getOptionValue(hyde));
						hydenLayerSizeString = (new Integer(hydenLayer[0])).toString(); 
					}
				}

				// opcion de momentum
				momentumRatio = _MOMENTUM;
				if (cmd.hasOption(momentum)){
					momentumRatio = Float.parseFloat(cmd.getOptionValue(momentum));
				}
				
				// opcion para factor de aprendizaje
				learningRateRatio = _LEARNING_RATE;
				if (cmd.hasOption(learningRate)){
					learningRateRatio = Float.parseFloat(cmd.getOptionValue(learningRate));
				}
			}
		}  catch (org.apache.commons.cli.ParseException ex){  
            System.out.println(ex.getMessage());  
            new HelpFormatter().printHelp(_MY_APP, header, options, footer, true);
			return;
		}  catch (java.lang.NumberFormatException ex){  
			new HelpFormatter().printHelp(_MY_APP, header, options, footer, true);
            return;
        } 
		
		System.out.printf( "Parámetros para el algoritmo:\n\tlearningRate: %.2f\n\tmomentum: %.2f\n",
				momentumRatio, learningRateRatio);
		
		/************************************************************************
		 * 
		 * AQUÍ EMPIEZA EL TRABAJO CON LA RED NEURONAL REALMENTE
		 * 
		 */
		
		System.out.print("\"Problem\", \"Media Training\", \"Desviación Training\", \"Media Val\", \"Desviación Val\", \"Media Test\", \"Desviación Test\", ");
		System.out.println("\"Test set classif media\",\"Test set classif desviacion\", \"Overfit media\", \"Overfit desviacion\", \"Total Epochs media\", \"Total Epochs desviacion\", \"RelevantEpochs media\", \"RelevantEpochs desviacion\" ");
		
		
		DataSet ds = new DataSet();
		ds.readFile(proben1File);
		
		if(hydenLayer != null){
			// se ejecuta con la opción por teclado
			runWith(ds, learningRateRatio, momentumRatio, hydenLayer);
		}
		else{
			int outputs = ds.getNumOutputs();
			// se ejecuta con estructuras para medir rendimiento
			int[][] hydenTests = {{0,outputs},{2,outputs},{4,outputs},{8,outputs},
					{16,outputs}, {2,4,outputs}, {2,6,outputs}, {2,8,outputs}, 
					{4,2,outputs}, {4,6,outputs},{4,8,outputs}, {8,4,outputs}, 
					{8,6,outputs}, {8,8,outputs}, {12,2,outputs}, {12,6,outputs},
					{12,8,outputs}};
			
			for (int j = 0; j < hydenTests.length; j++) {
				runWith(ds, learningRateRatio, momentumRatio, hydenTests[j]);
			}
		}
	}
	
		
	public static void runWith(DataSet ds, float learningRateRatio, float momentumRatio,
		int[] hydenLayer){
		long startTime = System.currentTimeMillis();
		String hydeStr = "";
		for (int i = 0; i < hydenLayer.length-1; i++) {
			hydeStr+=String.valueOf(hydenLayer[i])+"+";
		}
		System.out.print(String.format("\""+ds.getFileName()+" ("+hydeStr.substring(0, hydeStr.length()-1))+")\", ");
		int epoch;
		// vectores para posteriormente calcular media y desviación
		double[] minErrorValidationAcum = new double[_RUNS];
		double[] minErrorTrainingAcum = new double[_RUNS];
		double[] minErrorTestAcum = new double[_RUNS];
		double[] minTestClassification = new double[_RUNS];
		double[] accuracyError = new double[_RUNS];
		double[] minGl5 = new double [_RUNS];
		int[] relevantEpoch = new int[_RUNS];
		int[] totalEpoch = new int[_RUNS];
		
		for (int itr = 0; itr < _RUNS; itr++) {
			
			double minSqrErrorPercentValidation = Double.MAX_VALUE;
			double minSqrErrorPercentTestClassification = Double.MAX_VALUE;
			double gl5=0;
			
			RNA redNeuronal = new RNA(ds.getNumInputs(), hydenLayer);
			redNeuronal.getLayer(1).setIsSigmoid(false);

			int finalEpoch=0;
			
			for(epoch=0; epoch<_MAX_EPOCHS; epoch++){
				
				double minSqrErrorTraining = Double.MAX_VALUE;
				double sumatoryTrainingError = 0;
				finalEpoch = epoch;
				traing_over_dataset(ds.getTrainingExamplesSize(), redNeuronal, 
						ds.getTrainingExamples(), learningRateRatio, momentumRatio);
				
				if(epoch % _VERIFY_EPOCHS==0 && epoch!=0){
					
					double currentErrorVa = redNeuronal.squaredErrorPercentage(ds.getValidationExamples(),
							ds.getNumOutputs());
					
					double currSqrErrorTraining = redNeuronal.squaredErrorPercentage(ds.getTrainingExamples(), ds.getNumOutputs());
					sumatoryTrainingError += currSqrErrorTraining;
					
					// actualizamos el mínimo error sobre training
					if(currentErrorVa<minSqrErrorTraining){
						minSqrErrorTraining = currentErrorVa;
					}
					
					// comprobamos GL5
					gl5 = 100 * (currentErrorVa / minSqrErrorPercentValidation - (double)1 );
					
					if (currentErrorVa < minSqrErrorPercentValidation){
						minSqrErrorPercentValidation = currentErrorVa;
						relevantEpoch[itr] = epoch;
						minSqrErrorPercentTestClassification = redNeuronal.squaredErrorPercentage(ds.getTestExamples(), ds.getNumOutputs());
					}
					
					// comprobamos si aprende
					double trainingProgressSank = ((sumatoryTrainingError/(5*minSqrErrorTraining))-1)*1000; 
//					if( trainingProgressSank < _PROGRESS_SANK ){
//						break;
//					}
					
					if (gl5 > 5){
						break;
					}
				}
			}
			
			minGl5[itr] = gl5;
			totalEpoch[itr] = finalEpoch;
			
			minTestClassification[itr] = minSqrErrorPercentTestClassification;
			
			// se almacenan los mínimos errores
			minErrorValidationAcum[itr] = minSqrErrorPercentValidation;
			minErrorTrainingAcum[itr] = redNeuronal.squaredErrorPercentage(ds.getTrainingExamples(), 
					ds.getNumOutputs());
			minErrorTestAcum[itr] = redNeuronal.squaredErrorPercentage(ds.getValidationExamples(), 
					ds.getNumOutputs());
			accuracyError[itr] = accuracyOverDataSet(redNeuronal,ds.getTestExamples());
			
		}
		// media y desviación sobre training
		double meanTr = mean(minErrorTrainingAcum);
		double stdTr = standardDeviationMean(minErrorTrainingAcum);
		System.out.print(String.format("\"%.3f\", \"%.3f\", ", meanTr, stdTr));
		
		// media y desviación sobre validation
		double meanVa = mean(minErrorValidationAcum);
		double stdVa = standardDeviationMean(minErrorValidationAcum);
		System.out.print(String.format("\"%.3f\", \"%.3f\", ", meanVa, stdVa));
		
		// media y desviación sobre test
		double meanTest = mean(minErrorTestAcum);
		double stdTest = standardDeviationMean(minErrorTestAcum);
		System.out.print(String.format("\"%.3f\", \"%.3f\", ",meanTest,stdTest));
		
		// media y desviación sobre test classification
		double meanTestClassification = mean(minTestClassification);
		double stdTestClassification = standardDeviationMean(minTestClassification);
		System.out.print(String.format("\"%.3f\", \"%.3f\", ",meanTestClassification,stdTestClassification));
		
		// overfit, el gl5 al final del entrenamiento
		double meanOverfit = mean(minGl5);
		double stdOverfit = standardDeviationMean(minGl5);
		System.out.print(String.format("\"%.3f\", \"%.3f\", ",meanOverfit,stdOverfit));
		
		// media y desviación sobre relevant epoch
		double meanTotalEpoch = mean(totalEpoch);
		double stdTotalEpoch = standardDeviationMean(totalEpoch);
		System.out.print(String.format("\"%.0f\", \"%.0f\", ", meanTotalEpoch, stdTotalEpoch));
		
		// media y desviación sobre relevant epoch
		double meanEpoch = mean(relevantEpoch);
		double stdEpoch = standardDeviationMean(relevantEpoch);
		System.out.println(String.format("\"%.0f\", \"%.0f\" ", meanEpoch, stdEpoch));
		
		//System.out.println(String.format("Porcentaje de error en clasificación medio en las iteraciones es %.4f", mean(accuracyError)));
		
		
		long totalTime = (System.currentTimeMillis()-startTime)/1000;
		//System.out.println("Tiempo empleado: "+totalTime+" segundos\n\n");
		
	}
	
	private static void traing_over_dataset(int size, RNA redNeuronal,
					DataExample[] de_array, float learningRate, float momentum) {
		DataExample currentExample =null;
		for (int example = 0; example < size; example++) {
			currentExample = de_array[example];
			redNeuronal.train( currentExample.getInputs(), 
							   currentExample.getOutputs(), 
							   learningRate, momentum);
		}
	}
	
	private static float  accuracyOverDataSet(RNA redNeuronal, DataExample[] de_array) {
		float result = 0;
		for (DataExample dataExample : de_array) {
			result += redNeuronal.classificationAccuracy(dataExample.getInputs(), dataExample.getOutputs());
		}
		return result/de_array.length;
	}
	
	public static double mean(double[] data){
		double mean = 0; 
		final int n = data.length; 
		if ( n < 2 ){ 
			return Double.NaN; 
		}
		else{
			for ( int i=0; i<n; i++ ){ 
				mean += data[i];
			}
		}
		mean /= n; 
		return mean;
	}
	
	public static double mean(int[] data){
		double mean = 0; 
		final int n = data.length; 
		if ( n < 2 ){ 
			return Double.NaN; 
		}
		else{
			for ( int i=0; i<n; i++ ){ 
				mean += data[i];
			}
		}
		mean /= n; 
		return mean;
	}
	
	public static double standardDeviationMean ( double[] data ){ 
		double mean = mean(data);
		double sum = 0; 
		for ( int i=0; i<data.length; i++ ){ 
			final double v = data[i] - mean; 
			sum += v * v; 
		}
		return Math.sqrt( sum / ( data.length - 1 ) ); 
	}
	
	public static double standardDeviationMean ( int[] data){ 
		double mean = mean(data);
		double sum = 0; 
		for ( int i=0; i<data.length; i++ ){ 
			final double v = data[i] - mean; 
			sum += v * v; 
		}
		return Math.sqrt( sum / ( data.length - 1 ) ); 
	}

}
