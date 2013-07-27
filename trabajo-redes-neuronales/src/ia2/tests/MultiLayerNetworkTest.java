package ia2.tests;

import java.util.Random;

import ia2.classes.RNA;

public class MultiLayerNetworkTest {

	public static void main(String[] args) throws Exception {
		float[][] trainningSet = new float[][]{  new float[]{0, 0}, 
												 new float[]{0, 1}, 
												 new float[]{1, 0}, 
												 new float[]{1, 1}};
		
		float[][] ressult_set = new float[][]{	new float[]{0}, 
												new float[]{1}, 
												new float[]{1}, 
												new float[]{0}};
		// creamos la red con dos dos unidades de entrada, una de salida 
		// y dos unidades en la capa intermedia
		RNA mlp = new RNA (2, new int[]{10, 1});
		mlp.getLayer(1).setIsSigmoid(false);
		Random r = new Random();
		int en = 500;
		for (int e = 0; e < en; e++) {

			for (int i = 0; i < ressult_set.length; i++) {
				int idx = r.nextInt(ressult_set.length);
				mlp.train(trainningSet[idx], ressult_set[idx], 0.3f, 0.6f);
			}

			if ((e + 1) % 50 == 0) {
				System.out.printf("===================\nÉpoca %d \n", e + 1);
				for (int i = 0; i < ressult_set.length; i++) {
					float[] t = trainningSet[i];
					System.out.printf("Inputs: %.1f, %.1f --> Output %.3f\n", t[0], t[1], mlp.run(t)[0]);
				}
			}
		}

	}
}