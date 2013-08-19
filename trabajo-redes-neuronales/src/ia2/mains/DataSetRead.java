package ia2.mains;

import ia2.classes.DataSet;

/**
 * @author Sergio Soto
 * 
 *	Esta clase main sirve como ejemplo para comprobar la lectura de algunos
 *	archivos .dt 
 *
 */

public class DataSetRead {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		DataSet ds1;
		ds1 = new DataSet();

		ds1.readFile("./resources/dataSets/cancer/cancer1.dt");
		ds1.readFile("./resources/dataSets/diabetes/diabetes1.dt");
		ds1.readFile("./resources/dataSets/diabetes/diabetes2.dt");
		ds1.readFile("./resources/dataSets/diabetes/diabetes3.dt");
	}
}
