package ia2.classes;

import java.util.Arrays;
import java.util.Random;

/**
 * @author Sergio Soto
 *
 */

public class Layer {

	float[] output;
	float[] input;
	float[] weights;
	float[] dweights;
	boolean isSigmoid = true;
	Random random;

	/**
	 * @param inputSize
	 * @param outputSize
	 */
	public Layer(int inputSize, int outputSize) {
		output = new float[outputSize];
		input = new float[inputSize + 1];
		weights = new float[(1 + inputSize) * outputSize];
		dweights = new float[weights.length];
		random = new Random();
		initWeights();
	}

	/**
	 * @param isSigmoid
	 */
	public void setIsSigmoid(boolean isSigmoid) {
		this.isSigmoid = isSigmoid;
	}

	/**
	 */
	public void initWeights() {
		for (int i = 0; i < weights.length; i++) {
			weights[i] = (random.nextFloat() - 0.5f) * 4f;
		}
	}

	/**
	 * @param in
	 * @return
	 */
	public float[] run(float[] in) {
		System.arraycopy(in, 0, input, 0, in.length);
		input[input.length - 1] = 1;
		int offs = 0;
		// rellena todo el array con 0s
		Arrays.fill(output, 0);
		for (int i = 0; i < output.length; i++) {
			for (int j = 0; j < input.length; j++) {
				output[i] += weights[offs + j] * input[j];
			}
			if (isSigmoid) {
				output[i] = (float) (1 / (1 + Math.exp(-output[i])));
			}
			offs += input.length;
		}
		return Arrays.copyOf(output, output.length);
	}

	/**
	 * @param error
	 * @param learningRate
	 * @param momentum
	 * @return un array que contiene los nuevos pesos actualizados
	 */
	public float[] train(float[] error, float learningRate, float momentum) {
		int offs = 0;
		float[] nextError = new float[input.length];
		for (int i = 0; i < output.length; i++) {
			float d = error[i];
			// si tenemos la función sigmoide se aplica para la actualización
			// del peso del arco correspondiente
			if (isSigmoid) {
				d *= output[i] * (1 - output[i]);
			}
			for (int j = 0; j < input.length; j++) {
				int idx = offs + j;
				nextError[j] += weights[idx] * d;
				float dw = input[j] * d * learningRate;
				weights[idx] += dweights[idx] * momentum + dw;
				dweights[idx] = dw;
			}
			offs += input.length;
		}
		return nextError;
	}

}
