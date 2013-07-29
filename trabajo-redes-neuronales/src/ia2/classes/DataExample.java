package ia2.classes;

public class DataExample {
	
	private float[] inputs;
	private float[] outputs;
			
	public DataExample(){
		
	}
	
	public DataExample(float[] inputs, float[] outputs){
		this.inputs = inputs;
		this.outputs = outputs;
	}

	/**
	 * @return the inputs
	 */
	public float[] getInputs() {
		return inputs;
	}

	/**
	 * @param inputs the inputs to set
	 */
	public void setInputs(float[] inputs) {
		this.inputs = inputs;
	}

	/**
	 * @return the outputs
	 */
	public float[] getOutputs() {
		return outputs;
	}

	/**
	 * @param outputs the outputs to set
	 */
	public void setOutputs(float[] outputs) {
		this.outputs = outputs;
	}
}
