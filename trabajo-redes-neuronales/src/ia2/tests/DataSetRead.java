package ia2.tests;

import ia2.classes.DataSet;

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
