package model.network;

import java.util.ArrayList;
import model.options.*;

/**
 * 
 * This class represents all map networks. 
 * They are grids (SOM, DSOM, ...).
 * @author valbert
 *
 */
public abstract class AbstractMap extends AbstractNetwork{
	
	/**
	 * Topological distances that are between two neurons (neuron i & j for distancesTopo[i][j])
	 */
	protected double [][] distancesTopo;
	
	/**
	 * The number of columns and rows there are. 
	 */
	protected int colNumber,rowNumber;

	public AbstractMap(int nb_dimensions) {
		super(nb_dimensions);
		
		colNumber = MapOptions.cols;
		rowNumber = MapOptions.rows;
		
		//We create an array to compute and memorize topological distances. 
		distancesTopo = new double [colNumber*rowNumber][colNumber*rowNumber];
	}
    
	/**
	 * The distance there is between two neighbors. 
	 * @param currentBest The best neuron of the moment.
	 * @param n The current neuron we want to compare. 
	 * @return The distance there is between the two int he grid. 
	 */
    public abstract double distance_neighbor(Neuron currentBest,Neuron n);
    
    /**
     * Return true if the initial position of neurons is random. 
     * @return A boolean that describes the position of neurons. 
     */
    public abstract boolean getAleatory();
    
    @Override
    public int getNbNeuronForLearning(){
    	return 1;
    }
    
    @Override
    /**
     * Fills the network with neurons. 
     */
	public void fill() {
    	
    	nb_neurons = rowNumber * colNumber;
    	
    	//We create the number of neurons we need. 
		for(int k=0;k<rowNumber;k++) {
    		neurons.add(new ArrayList<Neuron>());
    		for(int k1=0;k1<colNumber;k1++){
    			neurons.get(k).add(new Neuron(nb_dimensions, new int[]{k, neurons.get(k).size()}, getAleatory()));
    		}
    	}
	
		// We create the topology.
		setNeighbors();
	
		//Finally, we compute topological distances. 
		for (int i=0;i<neurons.size();i++){
		    for (int j=0 ;j<neurons.get(i).size();j++) {
				for(int k = 0 ; k < neurons.size() ; k ++) {
				    for(int l = 0 ; l <neurons.get(k).size(); l++) {
				    	distancesTopo[i*colNumber +j][k*colNumber +l] = distance_neighbor(neurons.get(i).get(j), neurons.get(k).get(l));
				    }
				}
		    }
		}
	}
	
	public void setNeighbors(){
		
		//An array to set neighbors for each neurons. 
		ArrayList<Neuron> voisins;
	
		for (int col = 0 ; col < colNumber ; col ++) {
		    for (int row = 0 ; row < rowNumber ; row ++) {
		    	
				voisins = new ArrayList<Neuron>();
				
				if(col < colNumber -1){
				    voisins.add(neurons.get(row).get(col + 1));
				}
				if(col > 0){
				    voisins.add(neurons.get(row).get(col - 1));
				}
				
				if(row == 0 && neurons.size() > 1){
				    voisins.add(neurons.get(row + 1).get(col));
				}else if(row == neurons.size() -1 && neurons.size()> 1){
				    voisins.add(neurons.get(row - 1).get(col));
				}else if(neurons.size()> 1){
				    voisins.add(neurons.get(row + 1).get(col));
				    voisins.add(neurons.get(row - 1).get(col));
				}
				
				neurons.get(row).get(col).setNeighbors(voisins);		
		    }
		}		
    }
}
