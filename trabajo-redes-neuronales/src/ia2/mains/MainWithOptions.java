package ia2.mains;

import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;

/**
 * @author Sergio Soto
 * 
 * clase que parsea las opciones que se introducen por comando 
 *
 */

public class MainWithOptions {
	
	private static final float _LEARNING_RATE = 0.3f;
	private static final float _MOMENTUM = 0.6f;
	private static final String _MY_APP = "BORN_APP";
	
	

	/**
	 * @param args
	 */
	public static void main(String[] args) {
				
		// cadenas de nombres de opciones
		String 	hyde = "hydeLayer"; 
		String	momentum = "momentum";
		String	learningRate = "learningRate";
		String 	fileOption = "dataFile";
		
		String header = "Ejecutable para entrenar una red neuronal a partir \nde un archivo perteneciente a PROBEN1\n";
		String footer = "\nProyecto para la asignatura IA2\nAlojado en https://github.com/ssoto/redes-neuronales-artificiales\n\n";
		 
		
		// varialbes donde guardar parámetros de entrada
		float learningRateRatio, momentumRatio;
		int hydeLayerSize;
		
		Options options = new Options();
		
//		options.addOption(	OptionBuilder.withLongOpt(fileOption)
//										 .withDescription("The file to be processed")
//										 .hasArg()
//										 .withArgName("FILE")
//										 .isRequired()
//										 .create('f'));
		
		Option dataOption = new Option(fileOption, true, "fichero de datos sobre el que realizar las operaciones de entrenamiento, validación cruzada y calculo de error");
		dataOption.setRequired(true);
		options.addOption(dataOption);
		
		options.addOption( 	hyde,
							true, 
							"Numero de neuronas en la capa oculta" );
		
		String momentumHelp = "valor para momentum. Por defecto valdrá "+_MOMENTUM+". Recuerde que es un tipo double: 0.3 (pe)";
		options.addOption( 	momentum, 
							true, 
							momentumHelp);
		
		String learningRateHelp = "valor para factor de aprendizaje. Por defecto valdrá "+_LEARNING_RATE+"\n. Recuerde que es un tipo double: 0.3 (pe)";
		options.addOption( 	learningRate,
							true, 
							learningRateHelp);
		
		options.addOption(  "h", "help", false, "Muestra este mensaje" );
		
		CommandLineParser parser = new BasicParser();
		
		try {
			CommandLine cmd = parser.parse( options, args);
			if(cmd.hasOption("h")){
				new HelpFormatter().printHelp("myapp", header, options, footer, true);
                return;
			}
			else{
				// tratamos numero de elementos de la capa oculta
				hydeLayerSize = 0;
				if (cmd.hasOption(hyde)){
					hydeLayerSize = Integer.parseInt(cmd.getOptionValue(hyde));
					System.out.print("Capa oculta con "+hydeLayerSize+" elementos");
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
		
		/*
		 * TODO: añadir comportamiento!!
		 */
		System.out.printf( "Parámetros para el algoritmo:\n\tcapas ocultas:%d\n\tlearningRate: %.2f\n\tmomentum: %.2f\n",
							hydeLayerSize, momentumRatio, learningRateRatio);
		
	}

}
