package ia2.tests.cancer;

import java.util.Iterator;

import ia2.classes.cancerDataHandler.DataHandler;

public class ManagingDataFile {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String file_name = "./src/ia2/resources/proben1/cancer/cancer1.dt";
		DataHandler cancer_dt = new DataHandler();
		cancer_dt.read_examples(file_name);
		for (Iterator iterator = cancer_dt.iterator(); iterator.hasNext();) {
			String linea = (String) iterator.next();
			
		}
	}

}
