package ia2.tests;

import java.util.Random;

import ia2.classes.RNA;

public class MultiLayerNetworkTest {

	public static void main(String[] args) throws Exception {
		float[][] train = new float[][]{new float[]{0, 0}, new float[]{0, 1}, new float[]{1, 0}, new float[]{1, 1}};
		float[][] res = new float[][]{new float[]{0}, new float[]{1}, new float[]{1}, new float[]{0}};
		RNA mlp = new RNA (2, new int[]{2, 1});
		mlp.getLayer(1).setIsSigmoid(false);
		Random r = new Random();
		int en = 500;
		for (int e = 0; e < en; e++) {

			for (int i = 0; i < res.length; i++) {
				int idx = r.nextInt(res.length);
				mlp.train(train[idx], res[idx], 0.3f, 0.6f);
			}

			if ((e + 1) % 50 == 0) {
				System.out.printf("===================\nIteración %d \n", e + 1);
				for (int i = 0; i < res.length; i++) {
					float[] t = train[i];
					System.out.printf("Inputs: %.1f, %.1f --> Output %.3f\n", t[0], t[1], mlp.run(t)[0]);
				}
			}
		}

	}
}