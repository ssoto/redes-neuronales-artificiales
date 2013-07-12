package ia2.classes;

import java.util.Arrays;
import java.util.Random;

public class Layer {

	float[] output;
	float[] input;
	float[] weights;
	float[] dweights;
	boolean isSigmoid = true;

	/*
	 * constructor de la clase, toma como parámetros 
	 * numero de entradas, numero de slaidas y un Random para
	 * inicializar a aleatorios los pesos iniciales
	 */
	public Layer(int inputSize, int outputSize, Random r) {
		output = new float[outputSize];
		input = new float[inputSize + 1];
		weights = new float[(1 + inputSize) * outputSize];
		dweights = new float[weights.length];
		initWeights(r);
	}

	public void setIsSigmoid(boolean isSigmoid) {
		this.isSigmoid = isSigmoid;
	}

	public void initWeights(Random r) {
		for (int i = 0; i < weights.length; i++) {
			weights[i] = (r.nextFloat() - 0.5f) * 4f;
		}
	}

	public float[] run(float[] in) {
		System.arraycopy(in, 0, input, 0, in.length);
		input[input.length - 1] = 1;
		int offs = 0;
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

	public float[] train(float[] error, float learningRate, float momentum) {
		int offs = 0;
		float[] nextError = new float[input.length];
		for (int i = 0; i < output.length; i++) {
			float d = error[i];
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
