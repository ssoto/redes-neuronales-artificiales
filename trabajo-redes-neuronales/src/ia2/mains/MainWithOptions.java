package ia2.mains;

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
	
	private static final float _LEARNING_RATE = 0.3f;
	private static final float _MOMENTUM = 0.6f;
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
		int[] hydenLayer;
		String proben1File;
		String hydenLayerSizeString;
		
		// se configuran las opciones que queremos recoger por la línea de comandos		
		Options options = new Options();
		
		Option dataOption = new Option(fileOption, true, "fichero de datos sobre el que realizar las operaciones de entrenamiento, validación cruzada y calculo de error");
		dataOption.setRequired(true);
		options.addOption(dataOption);
		
		
		Option hydeOption = new Option(hyde, true, "Numero de neuronas en la(s) capas oculta.\nSi es una única capa oculta se indica valor 0.\nSi hay mas de una capa oculta se indican dos valores numéricos \nseparados por coma, en este caso no se permite valor 0");
		hydeOption.setRequired(true);
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
				if (!cmd.hasOption(hyde)){
					new HelpFormatter().printHelp("myapp", header, options, footer, true);
	                return;
				}
				else{
					String[] result;
					String hydeLayers = cmd.getOptionValue(hyde);
					if(hydeLayers.contains(",")){
						result = hydeLayers.split(","); 
						hydenLayer= new int[result.length];
						hydenLayerSizeString = "";
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
		
		System.out.printf( "Parámetros para el algoritmo:\n\tcapas ocultas:%s\n\tlearningRate: %.2f\n\tmomentum: %.2f\n",
				hydenLayerSizeString, momentumRatio, learningRateRatio);
		
		/************************************************************************
		 * 
		 * AQUÍ EMPIEZA EL TRABAJO CON LA RED NEURONAL REALMENTE
		 * 
		 */
		
		
		DataSet ds = new DataSet();
		ds.readFile(proben1File);
		long startTime = System.currentTimeMillis();
		int epoch;
		
		for (int iteration = 0; iteration < 20; iteration++) {
			
			double minErrorVa = Double.MAX_VALUE;
			double minErrorTest = Double.MAX_VALUE; 
			int minSqrErrorEpoch = -1;
			
			hydenLayer[hydenLayer.length-1] = ds.getNumOutputs();
			RNA redNeuronal = new RNA(ds.getNumInputs(), hydenLayer);
			redNeuronal.getLayer(1).setIsSigmoid(false);
			
			long init = System.currentTimeMillis();
			
			for(epoch=0; epoch<_MAX_EPOCHS; epoch++){
				
				traing_over_dataset(ds.getTrainingExamplesSize(), redNeuronal, 
						ds.getTrainingExamples(), learningRateRatio, momentumRatio);
				
				if(epoch % _VERIFY_EPOCHS==0 && epoch!=0){
					
					double currentErrorVa = redNeuronal.squaredError(ds.getValidationExamples());
					
					// comprobamos GL5
					double gl5 = 100 * (currentErrorVa / minErrorVa - (double)1 );
					minErrorTest = redNeuronal.squaredErrorPercentage(ds.getTestExamples(), ds.getNumOutputs());
					
					if (gl5 > 5){
						break;
					}
					else if (currentErrorVa < minErrorVa){
						minErrorVa = currentErrorVa;
						minSqrErrorEpoch = epoch;
					}
					
				}
			}
			
			long total = System.currentTimeMillis()-init;
			System.out.println("Iteracion "+(iteration+1)+":\ten época: "+epoch 
					+"\terror sobre test: "+minErrorTest+"\t("+total+"mS)");
		}
		long totalTime = (System.currentTimeMillis()-startTime)/1000;
		System.out.println("Tiempo empleado: "+totalTime+" segundos");
		
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

}
