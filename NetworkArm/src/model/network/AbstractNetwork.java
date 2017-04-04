package src.model.network;

import java.util.ArrayList;
import java.util.Observable;

import src.model.options.*;

/**
 * 
 * A super-class that represents each network in the project.
 * It is a thread that can be paused, stopped and stepped. 
 * @author valbert
 *
 */

public abstract class AbstractNetwork extends Observable{
	
	/**
	 * Neurons held by the network.
	 */
	protected ArrayList<ArrayList<Neuron>> neurons;
	
	/**
	 * Number of neurons of the network. 
	 */
	protected int nb_neurons;
	
	/**
	 * Number of dimensions
	 */
	protected int nb_dimensions;
	
	/**
	 * Number of epochs during which the network will evolve. 
	 */
    protected int epochs;
	
  
    public AbstractNetwork(int nb_dimensions){
    	super();

    	this.nb_dimensions = nb_dimensions;
    	
    	epochs = BasicOptions.nb_epochs;
    	
    	nb_neurons = 0;
	neurons = new ArrayList<ArrayList<Neuron>>();
		
    }
    
    /**
     * Return the number of dimensions of the network.
     * @return The number of dimensions of the network.
     */
    public int getNbDim(){
    	return nb_dimensions;
    }
    
    @Override
    public abstract String toString();
    
    /**
     * Get the number of neurons we needs to compute the learning.
     * @return The number of winner neurons we need to make the network learn. 
     */
    public abstract int getNbNeuronForLearning();
    
    /**
     * A method that fills the network with neurons (because it's empty at the beginning)
     */
    public abstract void fill();
    
    /**
     * Make a step in the algorithm.
     * @param data_set The set of data in which the algo evolves.
     * @param winners The neurons used for the learning.
     */
    public abstract void step(ArrayList<Neuron> winners, ArrayList<DataPoint> data_set, double[] data_priority);
	
	/**
	 * This method is called at the end of each epochs and sleep
	 * the time that has to be slept. 
	 * This time can be 0. 
	 */
	public void sleep(){
		try {
			Thread.sleep(BasicOptions.time_to_wait);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Get the size of the network.
	 * @return Number of neurons in this network. 
	 */
	public int size(){
    	return nb_neurons;
    }
	
	/**
	 * Clears the network. It empties the array-list. 
	 */
	public void clear(){
    	neurons.clear();
    	nb_neurons = 0;
    }
	
	/**
	 * Get the neurons of the network.
	 * @return An array-list of array-list containing all neurons. 
	 */
	public ArrayList<ArrayList<Neuron>> getNeurons(){
    	return neurons;
    }
}