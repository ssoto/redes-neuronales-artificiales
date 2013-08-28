package ia2.classes;



/**
 * @author Sergio Soto
 *
 */
public class RNA {

	/*
	 * Clase que implementa una red neuronal artificial multicapa, RNA
	 * 
	 */
	int numLayers, inputSize;
	Layer[] layers;

	/**
	 * @param inputSize: tamaño de la entrada
	 * @param layersSize: tamaño de las campas de entrada y de salida
	 */
	
	public RNA(int inputSize, int[] layersSize) {
		layers = new Layer[layersSize.length];
		for (int i = 0; i < layersSize.length; i++) {
			int inSize = i == 0 ? inputSize : layersSize[i - 1];
			// el constructor de Layer recibe su tamaño y el de la siguiente capa
			layers[i] = new Layer(inSize, layersSize[i]);
		}
	}
	

	/**
	 * @param idx: índice del layer a devolver
	 * @return layer que se corresponde con el índice
	 */
	public Layer getLayer(int idx) {
		return layers[idx];
	}

	/**
	 * @param input
	 * @return valores devueltos por la red para la entrada dada
	 */
	public float[] run(float[] input) {
		float[] actIn = input;
		for (int i = 0; i < layers.length; i++) {
			actIn = layers[i].run(actIn);
		}
		return actIn;
	}

	/**
	 * @param input: entrada a la red neuronal
	 * @param targetOutput: salida esperada para la entrada dada
	 * @param learningRate: coeficiente de aprendizaje
	 * @param momentum: momentum
	 */
	public void train(float[] input, float[] targetOutput, float learningRate, float momentum) 
	{
		float[] calcOut = run(input);
		float[] error = new float[calcOut.length];
		for (int i = 0; i < error.length; i++) 
		{
			error[i] = targetOutput[i] - calcOut[i]; // negative error
		}
		for (int i = layers.length - 1; i >= 0; i--) 
		{
			error = layers[i].train(error, learningRate, momentum);
		}
	}
	
	
	public double squaredErrorPercentage(DataExample[] examples, int outputSize){
		//supongo que el omax = 1 y el omin = 0
		return this.squaredError(examples)*100/(outputSize*examples.length);
	}
	
	public double squaredError(DataExample[] examples){
		double currentSquaredError, squaredErrorAcum=0;
		float[] currentExampleInput, currentExampleOutput, result;
		for (int i = 0; i < examples.length; i++) {
			currentExampleInput = examples[i].getInputs();
			currentExampleOutput = examples[i].getOutputs();
			
			result = this.run(currentExampleInput);
			currentSquaredError = 0.0;
			for (int j = 0; j < currentExampleOutput.length; j++) {
				currentSquaredError += Math.abs(currentExampleOutput[j]-result[j]);
			}
			squaredErrorAcum += currentSquaredError;
		}
		
		return squaredErrorAcum/examples.length;
	}
}