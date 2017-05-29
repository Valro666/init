package src.model.network;

import src.model.LinkedEnvironment;

/**
 * A Factory to create algos automatically
 * @author valbert
 */
public class NetworkFactory {
	
	/**
	 * The world of the factory. 
	 */
	private LinkedEnvironment world;
	
	public NetworkFactory(LinkedEnvironment world){
		this.world = world;
	}
	
	/**
	 * Create a new network from a describing string. 
	 * @param s_network The name of the network. 
	 * @return The new network. 
	 */
	public AbstractNetwork getNetwork(String s_network){
		AbstractNetwork network;

		switch(s_network){
			case "Self Organizing Map":
				network = new SOM(world.getNet().getNbDim());
			break;
			
			case "Dynamic Self Organizing Map":
				network = new DSOM(world.getNet().getNbDim());
			break;
			
			case "Growing Neural Gas":
				network = new GNG(world.getNet().getNbDim());
			break;

			default:
				network = new DSOM(world.getNet().getNbDim());
		}
		
		return network;
	}
	
	/**
	 * Create a new network from another network. 
	 * @param n The network we want to create a copy. 
	 * @return A new network with the same properties. 
	 */
	public AbstractNetwork getNetwork(AbstractNetwork n){
		
		AbstractNetwork network;

		if(n instanceof SOM)
			network = new SOM(world.getNet().getNbDim());
		
		else if(n instanceof DSOM)
			network = new DSOM(world.getNet().getNbDim());
		
		else if(n instanceof GNG)
			network = new GNG(world.getNet().getNbDim());
		
		else
			network = new DSOM(world.getNet().getNbDim());
		
		return network;
	}
}
