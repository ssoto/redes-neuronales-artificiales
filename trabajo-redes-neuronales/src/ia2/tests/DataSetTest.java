package ia2.tests;

import ia2.classes.DataSet;

public class DataSetTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		DataSet ds1, ds2;
		ds1 = new DataSet();
		ds2 = new DataSet();
		
		ds1.readFile("./resources/dataSets/cancer/cancer1.dt");
		ds1.readFile("./resources/dataSets/diabetes/diabetes1.dt");
	}
}
