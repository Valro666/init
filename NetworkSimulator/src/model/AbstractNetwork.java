package model;

import java.util.ArrayList;
import model.options.*;

/**
 * 
 * A super-class that represents each network in the project.
 * It is a thread that can be paused, stopped and stepped. 
 * @author valbert
 *
 */

public abstract class AbstractNetwork extends Thread{
	
	/**
	 * The environment where the network is evolving.
	 */
	protected Environment world;
  
    public AbstractNetwork(Environment h){
    	super();
    	world = h;
    }
    
    @Override
    public abstract String toString();
    
    /**
     * Execute the algorithm of the network. 
     */
    public abstract void learn();
    
    /**
     * Get the size of the network (number of neurons).
     * @return Number of neurons of the network. 
     */
    public abstract int size();
    
    /**
     * Clear the network. 
     */
    public abstract void clear();
    
    /**
     * A method that fills the network with neurons (because it's empty at the beginning)
     */
    public abstract void fill();

    /**
     * Get all neurons of the network.
     * @return A double array-list that contains all neurons of the network. 
     */
    public abstract ArrayList<ArrayList<Neuron>> getNeurons();
    
    /**
     * Launch the thread. It will execute the algo if it can be. 
     */
    public void run(){
    	
    	//We verify that there is no other process occuring
    	Options options = Options.getInstance();
    	BasicOptions basic = options.getBasic();
    	
    	if(basic.getStopped()){ 	
    		
    		//In that case, we indicate that a process is now acting.
    		basic.setStopped(false);
    		
    		//And we execute the algo. 
    		learn();
    		
    		//At the end, we indicate that there is no running process anymore.
    		basic.setStopped(true);
    		world.change();
    		
    	}
    }
    
}