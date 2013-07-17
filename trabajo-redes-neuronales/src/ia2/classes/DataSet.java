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
	
	private int numInputs, numOutputs;
	private Type typeInputs, typeOutputs;
	
	private int trainingExamplesSize, validationExamplesSize, testExamplesSize;
	private List<float[]> trainingExamples, validationExamples, testExamples;
	
	public DataSet(){
		this.f = null;
		this.trainingExamples = new LinkedList<float[]>();
		this.validationExamples = new LinkedList<float[]>();
		this.testExamples = new LinkedList<float[]>();
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
			try {
				int linesRead=0;
				String line = null;
				
				while ((line = reader.readLine()) != null) {
					if(linesRead >= 7 ){
						// ya hemos leido la cabecera
						if( linesRead>=7 && linesRead<this.trainingExamplesSize+7 ){
							this.process_training_line(line);
						}else if (linesRead>=7+this.trainingExamplesSize &&
								linesRead < 7+this.trainingExamplesSize+this.validationExamplesSize){
							this.process_validation_line(line);
						}else{
							this.process_test_line(line);
						}
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
							this.trainingExamplesSize = Integer.parseInt(line.split("=")[1]);
						}else if(line.contains("validation_examples")){
							this.validationExamplesSize = Integer.parseInt(line.split("=")[1]);
						}else if(line.contains("test_examples=")){
							this.testExamplesSize = Integer.parseInt(line.split("=")[1]);
						}
					}
					// siempre actualizamos el contador de líneas leidas
					linesRead++;
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
		
		int total = this.getTrainingExamples()+this.getValidationExamples()+this.getTestExamples();
		String msg = "Cargado fichero "+fileName+" en "+elapsedTime + " mS con "+total+" instancias de las cuales training: " + this.trainingExamples.size() + ", validation: "+
							this.validationExamples.size()+ " y test: "+this.testExamples.size();
		System.out.println(msg);
		assert this.trainingExamplesSize == this.trainingExamples.size();
		assert this.validationExamplesSize == this.validationExamples.size();
		assert this.testExamplesSize == this.testExamples.size();
	}
	
	private void process_test_line(String line) {
		this.testExamples.add(this.process_line(line));
	}


	private void process_validation_line(String line) {
		this.validationExamples.add(this.process_line(line));
	}


	private void process_training_line(String line) {
		this.trainingExamples.add(this.process_line(line));
	}


	public float[] process_line(String line){
		String[] splitedLine = line.split(" ");
		// nos aseguramos que la longitud de la linea es la misma que numInputs+numOutputs
		assert this.numInputs+this.numOutputs == splitedLine.length;

		float[] result = new float[this.numInputs+this.numOutputs];
		for (int i = 0; i < splitedLine.length; i++) {
			result[i] = new Float(splitedLine[i]);
		}
		return result;
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
		return trainingExamplesSize;
	}


	/**
	 * @param training_examples the training_examples to set
	 */
	public void setTrainingExamples(int training_examples) {
		this.trainingExamplesSize = training_examples;
	}


	/**
	 * @return the validation_examples
	 */
	public int getValidationExamples() {
		return validationExamplesSize;
	}


	/**
	 * @param validation_examples the validation_examples to set
	 */
	public void setValidationExamples(int validation_examples) {
		this.validationExamplesSize = validation_examples;
	}


	/**
	 * @return the test_examples
	 */
	public int getTestExamples() {
		return testExamplesSize;
	}


	/**
	 * @param test_examples the test_examples to set
	 */
	public void setTestExamples(int test_examples) {
		this.testExamplesSize = test_examples;
	}
	
	

	

}
