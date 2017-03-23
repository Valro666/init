package model;

import java.util.ArrayList;
import model.options.*;

/**
 * 
 * The GNG network.
 * algo : http://www.neuroinformatik.ruhr-uni-bochum.de/VDM/research/gsn/JavaPaper/node19.html
 * @author valbert
 *
 */
public class GNG extends BasicNetwork {
	
	/**
	 * The differences between data and new position of the neuron after moving.
	 * (errors.get(i) -> the error of the i neuron)
	 */
    private ArrayList<Double> errors;
    
    /**
     * Age of connections per neuron
     */
    private ArrayList<ArrayList<Integer>> ages;
    

    public GNG (Environment h) {
		super(h);

		neurons.add(new ArrayList<Neuron>());
		
		this.errors = new ArrayList<Double>();
		this.ages    = new ArrayList<ArrayList<Integer>>();
    }
    
    @Override
    public String toString(){
    	return "GNG";
    }
    
    @Override
    public void fill(){
    	GNGOptions gng_options = Options.getInstance().getGNG();
    	
    	nb_neurons = gng_options.getNbDep();
    	neurons.clear();
    	neurons.add(new ArrayList<Neuron>());
    	
    	for(int i = 0 ; i < nb_neurons ; i++) {
		    neurons.get(0).add(new Neuron(world.getDim(), new int[]{i, 0}, true));	    
		    errors.add(i,0.0);
		}
		
		// connexion entre les neurons de depart et age des connexions a 0
		for(int i = 0 ; i < nb_neurons ; i++) {
			ArrayList<Neuron> neighbors = new ArrayList<Neuron>();
		    ages.add(new ArrayList<Integer>()); 
		    for(int j = 0 ; j < nb_neurons; j ++) {
				if (i!= j) {
				    neighbors.add(neurons.get(0).get(j));
				    ages.get(i).add(0);
				}
		    }
		    
		    neurons.get(0).get(i).setNeighbors(neighbors);
		}	
    }
    
    @Override
    public void learn(){
    	
    	BasicOptions basic_options = Options.getInstance().getBasic();
    	GNGOptions gng_options = Options.getInstance().getGNG();
		
		int examples = 0;
		int max_neuron = gng_options.getMaxNeurons();
	
		while (( neurons.get(0).size() < max_neuron) && examples < epochs && !basic_options.getStopped())  {
			
			//We check the pause
			checkPause();
		    
		    //We choose a new piece of data for the example
		    int index_data = (int) (Math.random()*Data.getInstance().size());
		    examples ++;
		    
		    //We search for the two neurons which are the nearest of the piece of data
		     Neuron[] nearest = getNearest(index_data);
		     Neuron winner1  = nearest[0];
			 Neuron winner2  = nearest[1];
		     double distance_to_the_data = winner1.distance(Data.getInstance().get(index_data));
		    
		    //We increase the error of the winner
		    int index_1 = neurons.get(0).indexOf(winner1);
		    errors.set(index_1, errors.get(index_1) + distance_to_the_data);
	
		    //We update the state of links
		    updateConnection(winner1, winner2);
		    
		    //We approach the winner to the piece of data
		    winner1.setWeights(gng_options.getWinnerDistGng(), Data.getInstance().get(index_data));
	
		    //We also approach its direct neighbors
		    for (Neuron neighbor : winner1.getNeighbors()) {
		    	neighbor.setWeights(gng_options.getNeighborhoodGng(), Data.getInstance().get(index_data));
		    }
		    
		    //We delete wrong connections and neurons
		    deleteWrong();
	
		    //If it's time to, we add a new neuron
		    if ((examples % gng_options.getTimeNew()) == 0)
		    	addNewNeuron();
		    
		    //We manage the speed
		    sleep();
	
		    if((basic_options.getRefresh() == 0) || (examples % basic_options.getRefresh() == 0))
		    	world.change();
		}
		
		if(neurons.get(0).size() >= max_neuron){
			basic_options.setStopped(true);
		}
		
		world.change();
	}
    
    /**
     * This method modify neurons to get the two best (in winner1 and winner2)
     * and return the distance between them and the piece of data 
     * @param winner1 The nearest neuron
     * @param winner2 The second nearest neuron
     */
    private Neuron[] getNearest(int index_data){
    	
    	/*
    	 * We initialize distances in order to have best_dist1 < best_dist2.
    	 * Thanks to that, we'll always have the best distance in best_dist1
    	 * and the second best in best_dist2.
    	 */
    	double best_dist1 = 10;
	    double best_dist2 = 20;
	    double distance_to_the_data = 0;
	    Neuron[] nearest = new Neuron[2];
	    
	    //We run through all neurons
	    for(int i = 0 ; i < neurons.get(0).size() ; i ++) {
	    	
	    	//We get the distance between the piece of data and the current neuron
	    	distance_to_the_data = neurons.get(0).get(i).distance(Data.getInstance().get(index_data));
			
	    	//We check this distance
			if(distance_to_the_data < best_dist1) {
				
				//The previous best distance become the second best dist
				best_dist2  = best_dist1;
				
				//The previous nearest become the second nearest
			    nearest[1] = nearest[0];
			    
			    //The best distance changes
			    best_dist1  = distance_to_the_data;
			    
			    //The best neuron changes
			    nearest[0] = neurons.get(0).get(i);
			    
			} else if (distance_to_the_data < best_dist2) {
				
				//The second best distance changes
				best_dist2  = distance_to_the_data;
				
				//The second best neuron changes 
			    nearest[1] = neurons.get(0).get(i);
			    
			}
	    }
	    
	    return nearest;
    }
    
    /**
     * Update connections between the winner, its neighbors and the second winner
     * @param winner1 The nearest neuron of the piece of data
     * @param winner2 The second nearest neuron of the piece of data
     */
    private void updateConnection(Neuron winner1, Neuron winner2){
    	
	    int index_1 = neurons.get(0).indexOf(winner1);
	    
	    //We increase ages of winner's links
	    for(int i=0; i < winner1.getNeighbors().size(); i++) {
	    	
	    	//We get the current neighbor
			Neuron neighbor = winner1.getNeighbors().get(i);
			
			//We get the age of the connection between the neuron and its neighbor
			int age_neuron_neigh = ages.get(index_1).get(i);
			
			//We increment this age 
			ages.get(index_1).set(i, ++age_neuron_neigh);
			
			/*
			 * Now we'll do the same thing for the reverse link
			 */
	
			int index_winner = neighbor.getNeighbors().indexOf(winner1);
			int index_neighbor = neurons.get(0).indexOf(neighbor);
			
			//We get the age of the reverse connection
			int age_neigh_neurone = ages.get(index_neighbor).get(index_winner);
			
			//We increment this age
			ages.get(index_neighbor).set(index_winner, ++age_neigh_neurone);   
		    
	    }

	    /*
	     * We create or update the link between the two winners
	     */
	    
	    //We get indexes of winner2 in the list of neurons and the list of winner1's neighbors
	    int index_2 = neurons.get(0).indexOf(winner2);
	    int neigh1 = winner1.getNeighbors().indexOf(winner2);
	    
	    if (neigh1 == -1) {
	    	
	    	/*
	    	 * If there is no connection between those two, we create it.
	    	 */
	    	
	    	//We add the link in the winner1
	    	winner1.getNeighbors().add(winner2);
			ages.get(index_1).add(0);
			
			//We add the link in the winner2
			neurons.get(0).get(index_2).getNeighbors().add(winner1);
			ages.get(index_2).add(0);
			
	    } else { 
	    	
	    	/*
	    	 * In the other case, we set the age of the connection to 0
	    	 */
	    	
	    	//We refresh the age in winner1
			ages.get(index_1).set(neigh1, 0);
			
			//We refresh the age in winner2
			int neigh2 = winner2.getNeighbors().indexOf(winner1);
			ages.get(index_2).set(neigh2, 0);

	    }
    }
    
    /**
     * Create a new neuron and add it to the network
     */
    private void addNewNeuron(){
    	
    	Neuron worst_neuron1=null, worst_neuron2=null;
		
		/*
		 * We search for the worst neuron
		 */
		double worst_dist1  =  errors.get(0);
		worst_neuron1 = neurons.get(0).get(0);
		
		for (int i = 1 ; i < neurons.get(0).size() ; i++) {
			
		    if (errors.get(i) > worst_dist1) {
		    	worst_dist1 = errors.get(i);
				worst_neuron1 = neurons.get(0).get(i);
		    }
		}
		
		/*
		 * We search for the worst neighbor of this neuron
		 */
		double worst_dist2  = 0;
		worst_neuron2 = neurons.get(0).get(1);
		
		for (Neuron neighbor : worst_neuron1.getNeighbors()) {
			
			int index_neighbor = neurons.get(0).indexOf(neighbor);
		    
		    if (errors.get(index_neighbor) > worst_dist2) {
				worst_dist2  = errors.get(index_neighbor);
				worst_neuron2 = neurons.get(0).get(index_neighbor);
		    }
		}
	    
		/*
		 * We add a new neuron between these two worst neurons
		 */
		
		//We create a new neuron
		int neurons_number = neurons.get(0).size();
		Neuron new_neuron = new Neuron(world.getDim(), new int[]{neurons_number, 0}, true);
		neurons.get(0).add(new_neuron);
		
		ArrayList<Float> weight_worse1 = worst_neuron1.getWeights();
		ArrayList<Float> weight_worse2 = worst_neuron2.getWeights();
		
		ArrayList<Float> weight_new = new ArrayList<Float>();

		//We set the weight of the new neuron to the average of the two worst 
		for (int i = 0 ; i < weight_worse1.size(); i++)
			weight_new.add((weight_worse1.get(i)+weight_worse2.get(i))/2);

		new_neuron.setWeights(weight_new);
		
		//We add the error of the new neuron
		errors.add((worst_dist1+worst_dist2)/2);
		
		//We change errors of the two worst neurons
		GNGOptions gng_options = Options.getInstance().getGNG();
		
		int index_worst1 = neurons.get(0).indexOf(worst_neuron1);
		int index_worst2 = neurons.get(0).indexOf(worst_neuron2);
		errors.set(index_worst1, worst_dist1  - gng_options.getVariationForWorst() * worst_dist1);
		errors.set(index_worst2, worst_dist2  - gng_options.getVariationForWorst() * worst_dist2);
		
		//We change errors of all neurons
		for (int i = 0 ; i < neurons.get(0).size() ; i++){
		    double erreur = errors.get(i);
		    errors.set(i, erreur-gng_options.getVariationForAll()*erreur);
		}
		
		/*
		 * We connect the new neuron to the two winners
		 */
		
		//We create new neighbors' list and add the 2 worst to them
		new_neuron.setNeighbors(new ArrayList<Neuron>());
		new_neuron.getNeighbors().add(worst_neuron1);
		new_neuron.getNeighbors().add(worst_neuron2);
		
		//We create and set ages of these new connections
		ages.add(new ArrayList<Integer>()); 
		ages.get(neurons_number).add(0);
		ages.get(neurons_number).add(0);

		/*
		 * We change neighborhoods between the two worst neurons
		 */
		
		//We change the direct link worst1 -> worst2 to worst1 -> new
		int index_neighbor1 = worst_neuron1.getNeighbors().indexOf(worst_neuron2);
		worst_neuron1.getNeighbors().set(index_neighbor1, new_neuron);
		
		//We change the direct link worst2 -> worst1 to worst2 -> new
		int index_neighbor2 = worst_neuron2.getNeighbors().indexOf(worst_neuron1);
		worst_neuron2.getNeighbors().set(index_neighbor2, new_neuron);

		//We reset the age of these connections
		ages.get(index_worst1).set(index_neighbor1, 0);
		ages.get(index_worst2).set(index_neighbor2, 0);
		
    }
    
    /**
     * Delete too old connections and neurons without connections
     */
    private void deleteWrong(){
    	
    	/*
    	 * We run through all neurons
    	 */
    	int i=0;
    	while(i < neurons.get(0).size()) {
    		
    		//We get the current neuron
			Neuron current = neurons.get(0).get(i);
			
			//We run through all its neighbors
			int k=0;
			while(k < current.getNeighbors().size()) {
				
			   //We get the age between the neuron and its current neighbor
			   Integer age = ages.get(i).get(k);
				
			   //If the connection is too old, we delete it
			   GNGOptions gng_options = Options.getInstance().getGNG();
			   
			   if (age > gng_options.getMaxAge()) {			
			   		current.getNeighbors().remove(k);
			   		ages.get(i).remove(k);
			   } else {
			    	k++;
			   }
			    
			}
			
			//If the neuron don't have connection anymore, we delete it. 
			if (current.getNeighbors().size() == 0){
			    neurons.get(0).remove(i);
			    ages.remove(i);
			    errors.remove(i);	    
			} else i++;
	    }
    }
}