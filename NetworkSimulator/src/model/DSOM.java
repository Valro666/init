package model;

import model.options.*;

/**
 * 
 * The DSOM network.  
 * @author valbert
 *
 */
public class DSOM extends AbstractMap {

    public DSOM (Environment h) {
		super(h);
    }
    
    @Override
    public String toString(){
    	return "DSOM";
    }
    
    @Override
    public double distance_neighbor(Neuron currentBest,Neuron n) {
    	return Math.abs(currentBest.getPosition(1)-n.getPosition(1))+Math.abs(currentBest.getPosition(0)-n.getPosition(0));
    }
    
    @Override
    public boolean getAleatory(){
    	return false;
    }

    @Override
    public void learn(){
		
		nb_neurons = colNumber*rowNumber;
		int examples = 0;
		
		DSOMOptions dsom_options = Options.getInstance().getDSOM();
		BasicOptions basic_options = Options.getInstance().getBasic(); 
		
		while (examples < epochs && !basic_options.getStopped()) {
			
			//We verify the pause
			checkPause();
			 
			examples ++;
	
		    //We pick up a piece of data
		    int index_data = (int) (Math.random()*Data.getInstance().size());
		    
		    //We get the neuron which is the nearest to this piece of data
		    Neuron nearest = getNearest(index_data);
		    
		    //We compute the distance between the neuron and the piece of data
		    double distance_best = nearest.distance(Data.getInstance().get(index_data));
		    
		    //We compute the topological distance of the winner
		    int num_nearest_topo = nearest.getPosition(0) * colNumber + nearest.getPosition(1);
		    double db = Math.pow(distance_best, 2);
		    
		    /*
		     * We update weights of all neurons
		     */
		    
		    //We run through all neurons
		    for(int i = 0 ; i < neurons.size() ; i++) {
				for(int k=0;k<neurons.get(i).size();k++) {
					
					//We compute the topological distance of the target
				    Neuron target = neurons.get(i).get(k);
				    
				    //We update the weight of the neuron
				    int num_target   =  target.getPosition(0) * colNumber + target.getPosition(1);
				    double distance_topo = distancesTopo[num_target][num_nearest_topo]/(colNumber + rowNumber);
				    
				    //We compute the neighborhood
				    double neighb = Math.exp(-1* Math.pow(distance_topo, 2)/Math.pow(dsom_options.getElasticity(), 2)/db);
				    
				    //We run through all weights of the target
				    for(int l = 0; l < target.getWeights().size() ; l++) {
				    	
				    	//We compute the variation of the weight of the neuron
				    	double weight_neuron = target.getWeights().get(l);
    					double weight_data = Data.getInstance().get(index_data).getWeights().get(l);
    					double variation = Math.abs(weight_neuron - weight_data) * neighb * (weight_data - weight_neuron);
    					
    					weight_neuron += dsom_options.getEpsilon()*variation;
    					
    					//We update the weight of the neuron
    					target.getWeights().set(l, new Float(weight_neuron));
				    }
				}
			  
		    }	
		    
		    //We wait a little because it's too fast in other cases
		    sleep();
		    
		    if((basic_options.getRefresh() == 0) || (examples % basic_options.getRefresh() == 0))
		    	world.change();
		}
		
		world.change();
    }
}