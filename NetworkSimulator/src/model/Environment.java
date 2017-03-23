package model;

import java.util.Observable;

/**
 * 
 * Represents the environment where the algo is evolving. 
 * @author valbert
 *
 */
public class Environment extends Observable{
	
	/**
	 * The network of the world.
	 */
	private AbstractNetwork network;
	
	/**
	 * The data of the world.
	 */
	private Data data;
	
	/**
	 * The number of dimensions of the world. 
	 */
	private final int nb_dimensions;
	
	/**
	 * Creates an environment from a number of dimensions. 
	 * @param nb_dim The number of dimensions. 
	 */
	public Environment(int nb_dim){
		
		nb_dimensions = nb_dim;
		
		//We create the object that holds all options
		data = Data.getInstance();
		data.setDimensions(nb_dimensions);
		
		//We create a void network
		network = new SOM(this);
		network.clear();
	}
	
	/**
	 * Get the dimensions of the world. 
	 * @return the dimensions of the world
	 */
	public int getDim(){
		return nb_dimensions;
	}
	
	/**
	 * Clears the world (network and data)
	 */
	public void clear(){
    	data.clear();
    	network.clear();
    	
    	setChanged();
    	notifyObservers();
    }
	
	/**
	 * Changes shape of data.
	 * @param c The letter describing the shape. 
	 */
	public void setData(char c){
    	data.setData(c);
    	
    	change();
    }
	
	/**
	 * Indicates that the world changed. 
	 */
	public void change(){
    	setChanged();
    	notifyObservers();
    }
	
	//************************** GETTERS AND SETTERS **************************//
    public Data getData() {
    	return data;
    }
    
    public AbstractNetwork getNet(){
    	return network;
    }
    
    public void setNet(AbstractNetwork net){
    	network = net;
    	
    	setChanged();
    	notifyObservers();
    }
}
