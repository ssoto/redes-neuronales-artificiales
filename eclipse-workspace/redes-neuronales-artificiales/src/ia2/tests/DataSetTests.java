package ia2.tests;

import ia2.classes.DataSet;

public class DataSetTests {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		DataSet ds1 = new DataSet();
		ds1.readFile("./src/resources/dataSets/cancer/cancer1.dt");
		DataSet ds2 = new DataSet();
		ds2.readFile("./src/resources/dataSets/diabetes/diabetes1.dt");
		
	}

}
