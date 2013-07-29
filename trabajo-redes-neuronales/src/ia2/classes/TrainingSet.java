package ia2.classes;

import java.util.LinkedList;
import java.util.List;

public class TrainingSet {
	
	private float[][] array;
	private List<float[]> list;
	
	private int numExamples;
	
	private int numInputs, numOutputs;
	
	public TrainingSet(){
		this.list = new LinkedList<float[]>();
		this.numExamples = 0;
	}
	
	public TrainingSet(List<float[]> lista, int numInputs, int numOutputs){
		this.list = lista;
		this.numExamples = this.list.size();
	}
	
	public int getNumInputs(){
		return this.numInputs;
	}
	
	public int getNumOutputs(){
		return this.numOutputs;
	}

}
