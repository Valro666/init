package src.model.network;

import java.util.ArrayList;
import src.model.options.*;

/**
 * 
 * The SOM network. 
 * @author valbert
 *
 */
public class SOM extends AbstractMap{

    private double sigma_i =  .25;
    private double sigma_f =  .03;

    private Boolean decrease_lrate = true;
    private Boolean decrease_sigma = true;
    
    private double lrate_i =   0.5;
    private double lrate_f = 0.005;
    
    /**
     * If the learning is continuous or not. 
     */
    int continuous;
    
    private int totalEpochs;
    
    /**
     * Number of examples we get. 
     */
    private int examples;
    
    private double lrate, sigma;

    public SOM (int nb_dimensions) {
    	
		super(nb_dimensions);
		
		this.totalEpochs = 1;
		
		if (decrease_lrate) 
		    lrate = lrate_i;
		else 
		    lrate = (lrate_f + lrate_i)/4;
		
		if (decrease_sigma) 
		    sigma = sigma_i;
		else
		    sigma = (sigma_f + sigma_i)/3;
		
		nb_neurons = colNumber*rowNumber;
		continuous = 0;
    }
    
    @Override
    public String toString(){
    	return "SOM";
    }
    
    @Override
    public boolean getAleatory(){
    	return true;
    }
    
    @Override
    public void step(ArrayList<Neuron> winners, ArrayList<DataPoint> data_set, double[] data_priority){
    	
		
	    //We update learning rate and neighborhood
	    double t = ((continuous * epochs)+examples) / (float)(totalEpochs * epochs);

	    if (decrease_lrate)
	    	lrate = lrate_i*Math.pow((lrate_f/lrate_i), Math.pow(t, 1.2));
	    if (decrease_sigma){
	    	sigma = sigma_i*Math.pow((sigma_f/sigma_i), Math.pow(t, 1.2));
	    }
	    
	    //We  get the nearest neuron
	    Neuron nearest = winners.get(0);
	    
	    

            //We compute the topological distance between the datum and the winner
	    int num_nearest_topo = nearest.getPosition(0) * colNumber + nearest.getPosition(1);
	    
	    /*
	     * We update all weights
	     */
	    
	    //We run through all neurons
	    for(ArrayList<Neuron> neurons_list : neurons){
                    
			for(Neuron target : neurons_list){
                            
                            
                            
				//We get the topological position of the current neuron
			    int num_target_topo =  target.getPosition(0) * colNumber + target.getPosition(1);
			    
			    //There is 2 cases : if learning is continuous or not.
			    double learning_rate = 0;
			    double neighborhood = 0;
			    
			    if(SOMOptions.const_neighborhood){
			    	neighborhood = SOMOptions.neighborhood_som;
			    }else{
			    	double distance_w = distancesTopo[num_target_topo][num_nearest_topo];
			    	neighborhood = Math.exp(-1.0*distance_w/sigma);				   
			    }
			    
			    if(SOMOptions.const_learning){
			    	learning_rate = SOMOptions.learning_som;
			    }else{
			    	learning_rate = lrate;
			    }
			    
			    //We update the weight of the neuron 
			    target.setWeights(learning_rate, neighborhood, data_set);
			}
	    }	
	    
	    //We wait a little because it's too fast in other cases
	    sleep();
	    examples++;
    }


    @Override
    //Normalized topological distance between two neurons
    public double distance_neighbor(Neuron current_best, Neuron n2) {
		double dCol;
		double dRow;
		
		dCol = Math.pow(((double) current_best.getPosition(1) - (double) n2.getPosition(1))/colNumber, 2);
		dRow = Math.pow(((double) current_best.getPosition(0) - (double) n2.getPosition(0))/rowNumber, 2);
	
		return Math.sqrt(dCol + dRow);
    }

    public void setContinu(int cont, int totalEpochs) {
    	this.continuous= cont; 
		this.totalEpochs = totalEpochs;
    }
    
    public void setDecrease_lrate (Boolean decrease_lrate) {
    	this.decrease_lrate = decrease_lrate;
    }

    public Boolean getDecrease_lrate () {
    	return this.decrease_lrate;
    }
	
    public void setDecrease_sigma (Boolean decrease_sigma) {
    	this.decrease_sigma = decrease_sigma;
    }

    public Boolean getDecrease_sigma () {
    	return this.decrease_sigma;
    }
  
    public void onLineSom() {
    	this.setDecrease_sigma(false);
    	this.setDecrease_lrate(false);
    }
}