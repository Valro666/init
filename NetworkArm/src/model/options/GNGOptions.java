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
	public static int nb_neurons_dep = 2;
	
	/**
	 * The age at which we kill a neuron.
	 */
	public static int max_age = 40;
    
    /**
     * The number of neurons we can hold in the environment. 
     * If we exceeds it, we stop the process.
     */
	public static int max_neurons = 100;
    
    /**
     * The number of epochs at which we create a new neuron.
     */
	public static int time_new = 3;
    
    /*
     * Volatile for threads
     */
    
    /**
     * The variation for the worst neuron.
     */
	public static volatile double variation_for_worst = 0.5;
    
    /**
     * The variation for all neurons.
     */
	public static volatile double variation_for_all = .0005;
    
    /**
     * The neighborhood in the network.
     */
	public static volatile double neighborhood_gng = 0.1;
    
    /**
     * The distance we approach the winner to the piece of data.
     */
	public static volatile double winner_gng = 0.05;

}
