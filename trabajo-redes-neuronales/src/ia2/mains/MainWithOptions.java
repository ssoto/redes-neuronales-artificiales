package ia2.mains;

import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

public class MainWithOptions {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		String hyde="hydeLayer";
		
		Options options = new Options();
		options.addOption(hyde, true, "Cadena de doc" );
		options.addOption( "help",false, "print this message" );
		
		CommandLineParser parser = new BasicParser();
		try {
			CommandLine cmd = parser.parse( options, args);
			if(cmd.hasOption("help")){
				System.out.println(options.getOptions());
			}
			else if (cmd.hasOption(hyde)){
				System.out.println("Capa oculta con %s elementos");
			}
			else{
				System.out.println("Sin capa oculta");
			}
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		
	}

}
