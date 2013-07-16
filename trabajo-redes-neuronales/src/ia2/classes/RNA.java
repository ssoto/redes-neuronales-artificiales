package ia2.classes;
import java.util.Random;



/**
 * @author soto
 *
 */
public class RNA {

	/*
	 * Clase que implementa una red neuronal artificial multicapa, RNA
	 * 
	 */
	
	Layer[] layers;

	/**
	 * @param inputSize: tamaño de la entrada
	 * @param layersSize: tamaño de las campas de entrada y de salida
	 */
	
	public RNA(int inputSize, int[] layersSize) {
		layers = new Layer[layersSize.length];
		Random r = new Random(1234);
		for (int i = 0; i < layersSize.length; i++) {
			int inSize = i == 0 ? inputSize : layersSize[i - 1];
			layers[i] = new Layer(inSize, layersSize[i], r);
		}
	}

	/**
	 * @param idx
	 * @return
	 */
	public Layer getLayer(int idx) {
		return layers[idx];
	}

	/**
	 * @param input
	 * @return
	 */
	public float[] run(float[] input) {
		float[] actIn = input;
		for (int i = 0; i < layers.length; i++) {
			actIn = layers[i].run(actIn);
		}
		return actIn;
	}

	/**
	 * @param input
	 * @param targetOutput
	 * @param learningRate
	 * @param momentum
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
}