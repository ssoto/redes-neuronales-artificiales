package ia2.tests;

import ia2.classes.RNA;

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
				RNA mlp 
				= 
				new RNA(2, new int[]{2, 1});
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
					// imprimimos las salidas en los pasos multiplos de 100
					if ((e + 1) % 100 == 0)
					{
						System.out.println("=================================");
						System.out.printf("Paso del algoritmo %d\n", e + 1);
						for (int i = 0; i < res.length; i++) 
						{
							float[] t = train[i];
							System.out.printf("Ejemplo %d-ésimo: %.1f, %.1f --> %.3f\n", 
												i+1, t[0], t[1], mlp.run(t)[0]);
						}
					}
				}

	}

}
