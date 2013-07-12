package ia2.tests;

import ia2.classes.MultiLayerNetwork;

import java.util.Random;

public class AndTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// creamos un conjunto de entrenamiento para operación lógica and
		
				float[][] train = new float[][]{new float[]{0, 0}, 
												new float[]{0, 1}, 
												new float[]{1, 0}, 
												new float[]{1, 1}};
				
				
				float[][] res = new float[][]{new float[]{0}, 
											  new float[]{1}, 
											  new float[]{1}, 
											  new float[]{0}};
				
				// se inicializa la red con dos entradas 
				MultiLayerNetwork mlp 
				= 
				new MultiLayerNetwork(2, new int[]{2, 1});
				// desactivamos la sigmoide para la primera capa
				mlp.getLayer(1).setIsSigmoid(false);
				Random r = new Random();
				int en = 500;
				for (int e = 0; e < en; e++) 
				{
					for (int i = 0; i < res.length; i++) 
					{
						int idx = r.nextInt(res.length);
						mlp.train(train[idx], res[idx], 0.3f, 0.6f);
					}

					if ((e + 1) % 100 == 0)
					{
						System.out.println();
						for (int i = 0; i < res.length; i++) 
						{
							float[] t = train[i];
							System.out.printf("%d epoch\n", e + 1);
							System.out.printf("%.1f, %.1f --> %.3f\n", 
												t[0], t[1], mlp.run(t)[0]);
						}
					}
				}

	}

}
