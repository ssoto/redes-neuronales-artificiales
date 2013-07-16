package ia2.test;

import ia2.classes.DataSet;

public class dataSetTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		DataSet dt1, dt2, dt3;
		dt1 = new DataSet();
		dt2 = new DataSet();
		dt3 = new DataSet();
		
		dt1.readFile("./resources/dataSets/cancer/cancer1.dt");
//		dt1.getTrainingExamples();
//		dt1.getValidationExamples();
//		dt1.getTestExamples();
		dt2.readFile("./resources/dataSets/diabetes/diabetes1.dt");
	}

}
