package ia2.classes;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.LinkedList;
import java.util.List;

public class DataSet{
	
	public enum LineType{
		TRAINING,
		VALIDATION,
		TEST
	}
	
	private File f;
	
	private int numInputs, numOutputs;
	private Type typeInputs, typeOutputs;
	
	private int trainingExamplesSize, validationExamplesSize, testExamplesSize;
	private List<float[]> trainingExamplesList, validationExamplesList, testExamplesList;
	
	private DataExample[] trainingExamples, validationExamples, testExamples;
	
	
	
	public DataSet(){
		this.reset();
	}
	/*
	 * Este método inicializa a vacíos los atributos 
	 */
	private void reset() {
		this.f = null;
		this.trainingExamplesList = new LinkedList<float[]>();
		this.validationExamplesList = new LinkedList<float[]>();
		this.testExamplesList = new LinkedList<float[]>();
	}
	
	
	public void readFile(String fileName){
		// reiniciamos por si las listas contienen ejemplos de una ejecución anterior
		this.reset();
		// almacenamos el tiempo de inicio para luego mostrar lo tardado en cargar
		long startTime = System.currentTimeMillis();
		long stopTime = 0L;
		
		if (fileName==null)
			throw new NullPointerException("fileName can't be null");
		else
			this.f = new File(fileName);
		
		
		try {
			BufferedReader reader = new BufferedReader(new FileReader(fileName));
			try {
				int linesRead=0;
				String line = null;
				
				while ((line = reader.readLine()) != null) {
					if(linesRead >= 7 ){
						// ya hemos leido la cabecera
						if( linesRead>=7 && linesRead<this.trainingExamplesSize+7 ){
							this.process_line(LineType.TRAINING ,line);
						}else if (linesRead>=7+this.trainingExamplesSize &&
								linesRead < 7+this.trainingExamplesSize+this.validationExamplesSize){
							this.process_line(LineType.VALIDATION, line);
						}else{
							this.process_line(LineType.TEST, line);
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
//					System.out.println("Línea "+linesRead+": "+line);
				}
			} finally {
				reader.close();
				stopTime = System.currentTimeMillis();
			}
		} catch (IOException ioe) {
			System.err.println("imposible leer el archivo" + fileName);
			System.exit(1);
		} 
		long elapsedTime = stopTime - startTime; 
		
		// imprimimos los tamaños y tiempos de lectura de los datos
		int total = this.getTrainingExamplesSize()+this.getValidationExamplesSize()+this.getTestExamplesSize();
		String msg = "Cargado fichero "+fileName+" en "+elapsedTime + " mS con "+total+" instancias de las cuales training: " + this.trainingExamplesList.size() + ", validation: "+
							this.validationExamplesList.size()+ " y test: "+this.testExamplesList.size();
		System.out.println(msg);
		// algunos assert para comprobar que los datos cargados coinciden con 
		// la cabecera del archivo
		assert this.trainingExamplesSize == this.trainingExamplesList.size();
		assert this.validationExamplesSize == this.validationExamplesList.size();
		assert this.testExamplesSize == this.testExamplesList.size();
		
		// se transforman las listas en arrays
		this.trainingExamples = this.createDataFromList(this.trainingExamplesList);
		this.validationExamples = this.createDataFromList(this.validationExamplesList);
		this.testExamples = this.createDataFromList(this.testExamplesList);
	}
	
	private DataExample[] createDataFromList(List<float[]> listOfArrays) {
		
		float[] data;
		DataExample[] result = new DataExample[listOfArrays.size()];
		
		// iteramos sobre cada elemento del array de entrada que contiene
		// una lista de entradas y salidas concatenadas
		for (int i = 0; i < listOfArrays.size(); i++) {
			data = listOfArrays.get(i);
			float[] inputArray = new float[this.numInputs];
			float[] outputArray = new float[this.numOutputs];
			// según el índice de cada elemento sabremos si es entrada o salida
			for (int j = 0; j < data.length; j++) {
				if(j<this.numInputs){
					inputArray[j] = data[j];
				}
				else{
					outputArray[j-this.numInputs] = data[j];
				}
			}
			DataExample item = new DataExample(inputArray, outputArray);
			result[i] = item;
		}
		
		return result;
	}
	


	/**
	 * @param type
	 * @param line
	 * @return 
	 */
	public void process_line(LineType type, String line){
		String[] splitedLine = line.split(" ");
		// nos aseguramos que la longitud de la linea es la misma que numInputs+numOutputs
		assert this.numInputs+this.numOutputs == splitedLine.length;
		
		float[] floatAray = new float[this.numInputs+this.numOutputs];
		
		for (int i = 0; i < splitedLine.length; i++) {
			floatAray[i] = new Float(splitedLine[i]);
			
		}
		switch (type) {
		case TEST:
			this.testExamplesList.add(floatAray);
			break;
		case TRAINING:
			this.trainingExamplesList.add(floatAray);
			break;
		case VALIDATION:
			this.validationExamplesList.add(floatAray);
			break;
		default:
			break;
		}
		
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
	public int getTrainingExamplesSize() {
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
	public int getValidationExamplesSize() {
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
	public int getTestExamplesSize() {
		return testExamplesSize;
	}

	/**
	 * @param test_examples the test_examples to set
	 */
	public void setTestExamples(int test_examples) {
		this.testExamplesSize = test_examples;
	}
	
	public DataExample[] getTestExamples() {
		return this.testExamples;
	}
	
	public DataExample[] getTrainingExamples() {
		return trainingExamples;
	}
	public void setTrainingExamples(DataExample[] trainingExamples) {
		this.trainingExamples = trainingExamples;
	}
	
	public DataExample[] getValidationExamples(){
		return this.validationExamples;
	}
	public void setValidationExamples(DataExample[] validationExamples){
		this.validationExamples = validationExamples;
	}
	
	

}
