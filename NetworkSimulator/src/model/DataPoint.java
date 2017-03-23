package model;

import java.util.ArrayList;

/**
 * A class that represents each piece of data
 * @author valbert
 *
 */
public class DataPoint {
	
	/**
	 * The weights of the piece of data (generally, the coordinates).
	 */
	private ArrayList<Float> weights;
	
	/**
	 * Creates a random piece of data.
	 * @param cardinal The cardinal of the data (number of dimensions)
	 */
	public DataPoint(int cardinal){

		weights = new ArrayList<Float>();
		for(int i=0;i<cardinal;i++){
			weights.add(new Float(Math.random()));
		}
		
	}
	
	/**
	 * Creates a piece of data from a particular list of weights. 
	 * @param weights2 The list of wheights
	 */
	public DataPoint(ArrayList<Float> weights2) {
		this.weights=weights2;
	}

	//******************** GETTERS AND SETTERS ********************// 
	public void setWeight(int index, Float w){
		weights.set(index, w);
	}
	
	public Float getWeight(int index){
		return weights.get(index);
	}
	
	public ArrayList<Float> getWeights() {
		return weights;
	}

	public void setWeights(ArrayList<Float> weights) {
		this.weights = weights;
	}
}
