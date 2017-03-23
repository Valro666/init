package model;

import java.util.HashMap;

/**
 * A Factory to create algos automatically
 * @author valbert
 */
public class NetworkFactory {
	
	/**
	 * The world where networks are created. 
	 */
	private Environment world;
	
	public NetworkFactory(Environment w){
		world = w;
	}
	
	/**
	 * Create a new network from a describing string. 
	 * @param s_network The name of the network. 
	 * @return The new network. 
	 */
	public AbstractNetwork getNetwork(String s_network){
		AbstractNetwork network;
		
		HashMap<String, Integer> network_names = new HashMap<String, Integer>();
		network_names.put("Self Organizing Map", 0);
		network_names.put("Dynamic Self Organizing Map", 1);
		network_names.put("Growing Neural Gas", 2);
		
		network_names.put("Multiple Self Organizing Map", 3);
		network_names.put("Multiple Growing Neural Gas", 4);
		network_names.put("Multiple Dynamic Self Organizing Map", 5);
		
		network_names.put("Random Self Organizing Map", 6);
		network_names.put("Random Dynamic Self Organizing Map", 7);
		network_names.put("Random Growing Neural Gas", 8);
		
		switch(network_names.get(s_network)){
			case 0:
				network = new SOM(world);
			break;
			
			case 1:
				network = new DSOM(world);
			break;
			
			case 2:
				network = new GNG(world);
			break;
			
			case 3:
				network = new MultipleNetwork(new SOM(world));
			break;
			
			case 4:
				network = new MultipleNetwork(new GNG(world));
			break;
			
			case 5:
				network = new MultipleNetwork(new DSOM(world));
			break;
			
			case 6:
				network = new RandomNetwork(new SOM(world));
			break;
				
			case 7:
				network = new RandomNetwork(new DSOM(world));
			break;
			
			case 8:
				network = new RandomNetwork(new GNG(world));
			break;

			default:
				network = new DSOM(world);
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
			network = new SOM(world);
		
		else if(n instanceof DSOM)
			network = new DSOM(world);
		
		else if(n instanceof GNG)
			network = new GNG(world);
		
		else if(n instanceof MultipleNetwork)
			network = new MultipleNetwork(((MultipleNetwork) n).network);
		
		else if(n instanceof RandomNetwork)
			network = new RandomNetwork(((RandomNetwork) n).network);
		
		else
			network = new DSOM(world);
		
		return network;
	}
}
