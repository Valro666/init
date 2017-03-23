package model;

import java.util.ArrayList;

/**
 * These networks are specials : they contains basic network and
 * call their methods run() in their own methods run() after making a 
 * few changes
 * 
 * @author valbert
 */
public abstract class SpecialNetwork extends AbstractNetwork{
	
	protected BasicNetwork network;

	public SpecialNetwork(BasicNetwork n) {
		
		super(n.world);
		network=n;
		
		if(n instanceof SOM)
			((SOM)n).setContinu(1, 2);
		
	}
	
	public int size(){
    	return network.size();
    }
	
	public void clear(){
    	network.clear();
    }
	
	public ArrayList<ArrayList<Neuron>> getNeurons(){
    	return network.getNeurons();
    }
	
	@Override
	public String toString(){
		return network.toString();
	}
	
	@Override
	public void fill() {
		network.fill();
	}

}
