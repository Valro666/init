package model.options;


/**
 * 
 * This class contains options of GNG.
 * @author valbert
 *
 */
public class GNGOptions {
	
	/**
	 * The number of neurons we have at the beginning of the process
	 */
	private int nb_neurons_dep;
	
	/**
	 * The age at which we kill a neuron.
	 */
    private int max_age;
    
    /**
     * The number of neurons we can hold in the environment. 
     * If we exceeds it, we stop the process.
     */
    private int max_neurons;
    
    /**
     * The number of epochs at which we create a new neuron.
     */
    private int time_new;
    
    /*
     * Volatile for threads
     */
    
    /**
     * The variation for the worst neuron.
     */
    private volatile double variation_for_worst;
    
    /**
     * The variation for all neurons.
     */
    private volatile double variation_for_all;
    
    /**
     * The neighborhood in the network.
     */
    private volatile double neighborhood_gng;
    
    /**
     * The distance we approach the winner to the piece of data.
     */
    private volatile double winner_gng;
    
    public GNGOptions(){
    	variation_for_worst = .5;
        variation_for_all = .0005;
        nb_neurons_dep = 2;
        max_age = 40;
        max_neurons = 100;
        time_new = 300;
        neighborhood_gng = 0.00006;
        winner_gng = 0.05;
    }
    
    //*********************** GETTERS AND SETTERS ***********************//
    
    public double getWinnerDistGng(){
    	return winner_gng;
    }
    
    public void setWinnerDistGng(double dist){
    	winner_gng = dist;
    }
    
    public int getMaxAge() {
		return max_age;
	}

	public void setMaxAge(int max_age) {
		this.max_age = max_age;
	}

	public int getMaxNeurons() {
		return max_neurons;
	}

	public void setMaxNeurons(int max_neurons) {
		this.max_neurons = max_neurons;
	}

	public int getTimeNew() {
		return time_new ;
	}

	public void setTimeNew(int time_nouveau) {
		this.time_new = time_nouveau;
	}
	
	public double getVariationForWorst() {
		return variation_for_worst;
	}

	public void setVariationForWorst(double variation_for_worst) {
		this.variation_for_worst = variation_for_worst;
	}

	public double getVariationForAll() {
		return variation_for_all;
	}

	public void setVariationForAll(double variation_for_all) {
		this.variation_for_all = variation_for_all;
	}
	
	public double getNeighborhoodGng() {
		return neighborhood_gng;
	}

	public void setNeighborhoodGng(double neighborhood_gng) {
		this.neighborhood_gng = neighborhood_gng;
	}
	
	public int getNbDep(){
    	return nb_neurons_dep;
    }
    
    public void setNbDep(int nb_neur){
    	nb_neurons_dep = nb_neur;
    }
}
