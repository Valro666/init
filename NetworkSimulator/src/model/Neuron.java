package model;

import java.util.ArrayList;

/**
 * 
 * Describes each neuron in the world. 
 * @author valbert
 *
 */
public class Neuron {
	
	/**
	 * The weights of the neuron.
	 * Generally, the coordinates of the point. 
	 */
    private ArrayList<Float> weights;
    
    /**
     * The list of all neighbors of the neuron. 
     */
    private ArrayList<Neuron> neighbors;
    
    /**
     * List of the positions in the grid.
     * The number of positions depends on the number of dimensions of the world. 
     */
    private ArrayList<Integer> grid_positions;

    public Neuron(int cardinal, int[] g_p, boolean aleatoire){
		weights   = new ArrayList<Float>();
		grid_positions = new ArrayList<Integer>();
		
		for (int i = 0; i < g_p.length; i++) 
			grid_positions.add(g_p[i]);
		
		for (int i=0;i<cardinal;i++){
		    if (aleatoire) {
		    	weights.add(new Float(Math.random()));
		    } else {
		    	weights.add(new Float(.5));
		    }
		}
    }
    
    @Override
    public String toString(){
    	String s="(";
    	
    	for(Integer i : grid_positions)
    		s+=i+", ";
    	
    	return s;
    }

    /**
     * Return the manhattan distance between the neuron and the piece of data. 
     * @param data The piece of data. 
     * @return The distance between the neuron and the data. 
     */
    public double distance(DataPoint data) {
		double res = 0.0;
		for(int i=0;i<weights.size();i++){
		    res += Math.abs((weights.get(i) - data.getWeights().get(i)));
		}
		return res;
    }
   
	@Override
	public boolean equals(Object o){
		Neuron n = (Neuron) o;		
		boolean eq = true;
		
		for (int i = 0; i < n.getWeights().size(); i++) {
			if(weights.get(i) != n.getWeights().get(i))
				eq=false;
		}
		
		return eq;
	}
	
	//******************GETTERS AND SETTERS******************//
	
	/**
     * Get the position of the neuron in the dimension given in args
     * @param dimension The dimension in which we want to know the position of the neuron
     * @return The position of the neuron in this dimension of the grid
     */
    public Integer getPosition(int dimension){
    	return grid_positions.get(dimension);
    }
	
	public void set(Neuron n){
    	grid_positions = n.grid_positions;
    	weights = n.weights;
    	neighbors = n.neighbors;
    }
	
	public ArrayList<Neuron> getNeighbors(){
    	return neighbors;
    }
    
    public void setNeighbors(ArrayList<Neuron> n){
    	neighbors = n;
    }
	
	public ArrayList<Float> getWeights() {
    	return weights;
    }

    public void setWeights(ArrayList<Float> weights) {
    	this.weights = weights;
    }

    public void setWeights(double e, double h, DataPoint data) {
		double wi;
		for(int i=0;i<weights.size();i++){
		    wi = weights.get(i);
		    
		    //e-> learning, h->neighborhood
		    wi = wi + e*h*(data.getWeights().get(i) - wi);
		    weights.set(i, new Float(wi));
		}
    }
  
    public void setWeights(double taux, DataPoint data) {
    	setWeights(taux, 1, data);
    }
	
}
