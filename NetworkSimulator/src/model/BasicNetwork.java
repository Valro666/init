package model;

import java.util.ArrayList;
import model.options.*;

/**
 * 
 * This class represents all basic networks. 
 * A basic network is a network that is launch in a signle and defined environment.
 * Others networks extends of SpecialNetwork (MultipleNetwork, RandomNetwork, ...)
 * @author valbert
 *
 */
public abstract class BasicNetwork extends AbstractNetwork{
	
	/**
	 * Neurons held by the network.
	 */
	protected ArrayList<ArrayList<Neuron>> neurons;
	
	/**
	 * Number of neurons of the network. 
	 */
	protected int nb_neurons;
	
	/**
	 * Number of epochs during which the network will evolve. 
	 */
    protected int epochs;

	public BasicNetwork(Environment h) {
		
		super(h);
		
		nb_neurons = 0;
		neurons = new ArrayList<ArrayList<Neuron>>();
    	epochs = Options.getInstance().getBasic().getNbEpochs();
    	
	}
	
	/**
	 * A method that check the pause. 
	 * It is also this method that manage the step-by-step. 
	 */
	public void checkPause(){
		
		Options options = Options.getInstance();
		BasicOptions basic = options.getBasic();
		
		/*
		 * We synchronize in order to wake up the thread if it's waiting. 
		 */
		synchronized (world) {
			
			//We loop while there is the pause or we go forward for one step. 
			while(basic.getPaused() && !basic.getStopped() && !basic.getStepped()){
				
				try {
					
					//We wait for the pause to be unset. 
					world.wait();
					
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
				
			}
			
			//We set stepped to false, in case it has been changed. 
			basic.setStep(false);
		}
	}
	
	/**
	 * This method is called at the end of each epochs and sleep
	 * the time that has to be slept. 
	 * This time can be 0. 
	 */
	public void sleep(){
		Options options = Options.getInstance();
		BasicOptions basic = options.getBasic();
		
		try {
			Thread.sleep(basic.getTimeToWait());
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
