package ia2.classes;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.LinkedList;
import java.util.List;

public class DataSet{
	
	private File f;
	private List<String[]> linesList;
	
	private int numInputs, numOutputs;
	private Type typeInputs, typeOutputs;
	private int trainingExamples, validationExamples, testExamples;
	
	public DataSet(){
		this.f = null;
		this.linesList = null;
	}
	
	
	public void readFile(String fileName){
		long startTime = System.currentTimeMillis();
		long stopTime = 0L;
		try{
			this.f = new File(fileName);
		}catch (Exception e){
			System.err.println("Imposible abrir archivo de datos: "+fileName);
		}
		
		
		try {
			BufferedReader reader = new BufferedReader(new FileReader(fileName));
			linesList = new LinkedList<String[]>();	
			try {
				int linesReaded=0;
				List<Long> newLine = new LinkedList<Long>();
				String line = null;
				String [] lineSplitted;
				while ((line = reader.readLine()) != null) {
					if(linesReaded >= 7 ){
						// ya hemos leido la cabecera
						lineSplitted = line.split(" ");
						linesList.add(lineSplitted);
					}
					else{
						// es cabecera!
						// linea de input
						if(line.contains("_in=")){
							String inType;
							int inArgs;

							inArgs = Integer.parseInt(line.split("=")[1]);
							inType = line.split("_")[0];
							
							if (inType.contains("bool") &&  inArgs>0){
								this.setNumInputs(inArgs);
								this.setTypeInputs(Boolean.class);
							}
							else if(inType.contains("real") && inArgs>0){
								this.setNumInputs(inArgs);
								this.setTypeInputs(Float.class);
							}
						}else if (line.contains("_out=")) {
							//linea de output
							String outType;
							int outArgs;
							
							outArgs = Integer.parseInt(line.split("=")[1]);
							outType = line.split("_")[0];
							
							//es tipo bool
							if (outType.contains("bool") &&  outArgs>0){
								this.setNumOutputs(outArgs);
								this.setTypeOutputs(Boolean.class);
							}
							//es tipo real
							else if(outType.contains("real") && outArgs>0){
								this.setNumOutputs(outArgs);
								this.setTypeOutputs(Float.class);
							}
						}else if(line.contains("training_examples=")){
							this.trainingExamples = Integer.parseInt(line.split("=")[1]);
						}else if(line.contains("validation_examples")){
							this.validationExamples = Integer.parseInt(line.split("=")[1]);
						}else if(line.contains("test_examples=")){
							this.testExamples = Integer.parseInt(line.split("=")[1]);
						}
					}
					linesReaded++;
					//System.out.println("Línea "+linesReaded+": "+line);
				}
			} finally {
				reader.close();
				stopTime = System.currentTimeMillis();
			}
		} catch (IOException ioe) {
			System.err.println("oops " + ioe.getMessage());
		} 
		long elapsedTime = stopTime - startTime; 
		System.out.println("Cargado fichero "+fileName+" en "+elapsedTime + " mS, se han leido "+this.linesList.size()+" instancias, de la scuales:\n"
							+this.trainingExamples+" training, "+this.validationExamples+" validation y "+this.testExamples+" test");
	}


	public int getNumInputs() {
		return numInputs;
	}


	public void setNumInputs(int numInputs) {
		this.numInputs = numInputs;
	}


	public int getNumOutputs() {
		return numOutputs;
	}


	public void setNumOutputs(int numOutputs) {
		this.numOutputs = numOutputs;
	}


	/**
	 * @return the typeInputs
	 */
	public Type getTypeInputs() {
		return typeInputs;
	}


	/**
	 * @param typeInputs the typeInputs to set
	 */
	public void setTypeInputs(Type typeInputs) {
		this.typeInputs = typeInputs;
	}


	/**
	 * @return the typeOutputs
	 */
	public Type getTypeOutputs() {
		return typeOutputs;
	}


	/**
	 * @param typeOutputs the typeOutputs to set
	 */
	public void setTypeOutputs(Type typeOutputs) {
		this.typeOutputs = typeOutputs;
	}


	/**
	 * @return the training_examples
	 */
	public int getTrainingExamples() {
		return trainingExamples;
	}


	/**
	 * @param training_examples the training_examples to set
	 */
	public void setTrainingExamples(int training_examples) {
		this.trainingExamples = training_examples;
	}


	/**
	 * @return the validation_examples
	 */
	public int getValidationExamples() {
		return validationExamples;
	}


	/**
	 * @param validation_examples the validation_examples to set
	 */
	public void setValidationExamples(int validation_examples) {
		this.validationExamples = validation_examples;
	}


	/**
	 * @return the test_examples
	 */
	public int getTestExamples() {
		return testExamples;
	}


	/**
	 * @param test_examples the test_examples to set
	 */
	public void setTestExamples(int test_examples) {
		this.testExamples = test_examples;
	}
	
	

	

}