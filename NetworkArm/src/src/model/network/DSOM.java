package src.model.network;

import java.util.ArrayList;

import src.model.options.*;

/**
 * 
 * The DSOM network.  
 * @author valbert
 *
 */
public class DSOM extends AbstractMap {

    public DSOM (int nb_dimensions) {
		super(nb_dimensions);
		
		nb_neurons = colNumber*rowNumber;
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
    	return true;
    }
    
    @Override
    public void step(ArrayList<Neuron> winners, ArrayList<DataPoint> data_set, double[] data_priority){
	    
	    //We get the neuron which is the nearest to this piece of data
	    Neuron nearest = winners.get(0);
	    
            
            
	    //We compute the distance between the neuron and the piece of data
	    double distance_best = nearest.distanceToAll(data_set, data_priority);
	    
            //System.out.println("distance best : "+distance_best);
            
	    //We compute the topological distance of the winner
	    
            int num_nearest_topo = nearest.getPosition(0) * colNumber + nearest.getPosition(1);
	    //int num_nearest_topo = Math.round(nearest.getWeight(0)) * colNumber + Math.round(nearest.getWeight(1));
            double db = Math.pow(distance_best, 2);
	    
           //System.out.println("nearest position : "+nearest.getWeight(0)+" , "+nearest.getWeight(1));
            
	    /*
	     * We update weights of all neurons
	     */
	    
	    //We run through all neurons
	    for(ArrayList<Neuron> list : neurons) {
			for(Neuron target : list){
				
                            //We compute the topological distance of the target
			    int num_target = target.getPosition(0) * colNumber + target.getPosition(1);
                            
                            //int num_target = Math.round(nearest.getWeight(0)) * colNumber + Math.round(nearest.getWeight(1));
			    double distance_topo = distancesTopo[num_target][num_nearest_topo]/(colNumber + rowNumber);
			    
			    //We compute the neighborhood
			    double neighb = Math.exp(-1* Math.pow(distance_topo, 2)/Math.pow(DSOMOptions.elasticity, 2)/db);
			    
			    //We run through all weights of data
			    int current_weight = 0;
			    int set = 0;
			    for(DataPoint point : data_set){
			    	for(Float weight_data : point.getWeights()){
			    		
                                    double weight_neuron = target.getWeights().get(current_weight);

                                    //We compute the variation of the weight of the neuron.
                                    double variation = Math.abs(weight_neuron - weight_data) * neighb * (weight_data - weight_neuron);

                                    //And finally we compute the new value of the weight of the neuron.
                                    weight_neuron += DSOMOptions.epsilon*variation*data_priority[set];

                                    //We update the weight of the neuron
                                    target.getWeights().set(current_weight, new Float(weight_neuron));

                                    current_weight++;
			    	}
			    	
			    	set++;
			    }
			}
	    }	
	    
	    //We manage the speed.
	    sleep();
    }
}