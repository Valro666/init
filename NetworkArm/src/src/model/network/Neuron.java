package src.model.network;

import java.util.ArrayList;

/**
 * 
 * Describes each neuron in the world. 
 * @author valbert
 *
 */
public class Neuron {
	
	/**
	 * The weights of the neuron.
	 * Generally, the coordinates of the point. 
	 */
    private ArrayList<Float> weights;
    
    /**
     * The list of all neighbors of the neuron. 
     */
    private ArrayList<Neuron> neighbors;
    
    /**
     * List of the positions in the grid.
     * The number of positions depends on the number of dimensions of the world. 
     */
    private ArrayList<Integer> grid_positions;
    
    private int number;

    public Neuron(int cardinal, int[] grid_pos, boolean aleatoire){
		weights   = new ArrayList<Float>();
		grid_positions = new ArrayList<Integer>();
		
		for (int i = 0; i < grid_pos.length; i++) 
			grid_positions.add(grid_pos[i]);
		
		for (int i=0; i<cardinal; i++){
		    if (aleatoire) {
		    	weights.add(new Float(Math.random()));
		    } else {
		    	weights.add(new Float(.5));
		    }
		}		
    }
    
    public Neuron(double... weight){
    	weights = new ArrayList<Float>();
    	for (int i = 0; i < weight.length; i++) {
			weights.add(new Float(weight[i]));
		}
    	
    	grid_positions = new ArrayList<Integer>();
    }
    
    public Neuron(ArrayList<Float> w){
    	weights = w;
    	
    	grid_positions = new ArrayList<Integer>();
    }
    
    public void setNumber(int n)
    {
    	number = n;
    }
    
    public int getNumber()
    {
    	return number;
    }
    
    @Override
    public String toString(){
    	String s;
    	
    	if(grid_positions.size()>0){
    		s="Pos(";
    		
			for(int i=0; i<grid_positions.size()-1; i++){
				s+=grid_positions.get(i)+",";
			}
			
			s+=grid_positions.get(grid_positions.size()-1)+")";
			
			s+=" | Weigths(";
    	}else{
    		s="Weights(";
    	}
    	
    	for(int i=0; i<weights.size()-1; i++)
    		s+=weights.get(i)+", ";
    	
    	s+=weights.get(weights.size()-1)+")";
    	
    	return s;
    }

    /**
     * Return the manhattan distance between the neuron and the piece of data. 
     * @param data The piece of data. 
     * @return The distance between the neuron and the data. 
     */
    public double distance(DataPoint data) {
		double res = 0.0;
		for(int i=0;i<weights.size();i++){
		    res += Math.pow(weights.get(i) - data.getWeight(i), 2);
		}
		
		res = Math.sqrt(res);
		
		return res;
    }
    
    /**
     * Return the manhattan distance between the neuron and the piece of data. 
     * @param data The piece of data. 
     * @return The distance between the neuron and the data. 
     */
    public double distance(DataPoint data, int start, int end) {
		double res = 0.0;
		for(int i=start;i<=end;i++){
		    res += Math.pow(weights.get(i) - data.getWeight(i), 2);
		}
		
		res = Math.sqrt(res);
		
		return res;
    }
    
    /**
     * Return the manhattan distance between the neuron and all pieces of data.
     * @param dataset The set of data.
     * @return The distance.
     */
    public double distanceToAll(ArrayList<DataPoint> dataset, double[] data_priority){
    	
    	double dist = 0;
    	int current_weight = 0;
    	
    	for(int i=0; i<dataset.size(); i++){
    		
    		for(Float weight : dataset.get(i).getWeights()){
    			
    			dist += Math.pow(weights.get(current_weight) - weight, 2);
    			current_weight++;
    			
    		}
    		
    		double priority = data_priority[i];
    		dist*=priority;
    	}
    	
    	dist = Math.sqrt(dist);
    	
    	return dist;
    }
    /**
     * Return the manhattan distance between the neuron and all pieces of data for only weights whose index is between i and j.
     * @param dataset The set of data.
     * @return The distance.
     */
	public double distanceToAll(ArrayList<DataPoint> dataset, double[] data_priority, int xi, int xj) {
    	double dist = 0;
    	int current_weight = xi;
    	
    	for(int i=0; i<dataset.size(); i++){
    		for(int j = xi; j <= xj; j++){
    			Float weight = dataset.get(i).getWeight(j);
    			dist += Math.pow(weights.get(current_weight) - weight, 2);
    			current_weight++;
    		}
    		
    		double priority = data_priority[i];
    		dist*=priority;
    	}
    	
    	dist = Math.sqrt(dist);
    	
    	return dist;
	}
    
    /**
     * Compute the neuron equivalent to the barycenter of all neurons in parameters.
     * @param neurons The neurons we want to get the barycenter.
     * @return The barycenter of neurons.
     */
    public static Neuron getBarycenter(DataPoint dp, Neuron... neurons){
    	Neuron barycenter = new Neuron();
    	
    	//We compute the weighting of each neuron
    	ArrayList<Double> weightings = new ArrayList<Double>();
    	
    	for(Neuron n : neurons){
    		double dist = n.distance(dp);
    		weightings.add((dist > 0) ? 1.0/dist : Double.MAX_VALUE);
    	}
    	
    	//We compute the weights of the new neuron
    	ArrayList<Float> weights = new ArrayList<Float>();
    	for(int i=0; i<dp.getWeights().size(); i++){
    		double weight=0;
    		
    		int j=0;
    		for(Neuron n : neurons){
    			weight += weightings.get(j) * n.getWeight(i);
    			j++;
    		}
    		
    		weights.add(new Float(weight));
    	}
    	
    	barycenter = new Neuron(weights);
    	
    	return barycenter;
    }
    
    /**
     * Compute the neuron equivalent to the barycenter of all neurons in parameters.
     * @param neurons The neurons we want to get the barycenter.
     * @return The barycenter of neurons.
     */
    public static Neuron getBarycenter(DataPoint dp, int xi, int xj, Neuron... neurons){
    	Neuron barycenter;
    	
    	//We compute the weighting of each neuron
    	ArrayList<Double> weightings = new ArrayList<Double>();
    	double dist=0, global_dist = 0.0, test = 0.0;
    	int index = 0;
    	int size = neurons[0].getWeights().size();
    	
    	for(Neuron n : neurons){
    		dist = n.distance(dp, xi, xj);
    		weightings.add((dist > 0) ? 1.0/dist : Double.MAX_VALUE);
    		global_dist += weightings.get(index);
    		index++;
    	}
    	
    	for(int i = 0; i < weightings.size(); i++) {
    		Double d = new Double(weightings.get(i)/global_dist);
    		weightings.set(i, d);
    	}
    	
    	//We compute the weights of the new neuron
    	ArrayList<Float> weights = new ArrayList<Float>();
    	for(int i=0; i<2; i++){
    		double weight=0;
    		
    		int j=0;
    		for(Neuron n : neurons){
    			weight += weightings.get(j).doubleValue() * n.getWeight(i);
    			j++;
    		}
    		
    		weights.add(new Float((float)weight));
    	}
    	for(int i=2; i<size; i++){
    		double weightx=0, weighty=0;
    		
    		int j=0;
    		for(Neuron n : neurons){
    			weightx += weightings.get(j).doubleValue() * Math.cos(Math.toRadians((n.getWeight(i)*360)));
    			weighty += weightings.get(j).doubleValue() * Math.sin(Math.toRadians((n.getWeight(i)*360)));
    			j++;
    		}
    		double weight = Math.toDegrees(Math.atan2(weighty, weightx))/360.0;
    		weights.add(new Float((float)weight));
    	}
    	
    	barycenter = new Neuron(weights);
    	
    	return barycenter;
    }
    
    /**
     * Compute the neuron equivalent to the barycenter of all neurons in parameters.
     * @param neurons The neurons we want to get the barycenter.
     * @return The barycenter of neurons.
     */
    public static Neuron getBarycenter(DataPoint dp, ArrayList<Neuron> neurons){
    	Neuron barycenter = new Neuron();
    	
    	//We compute the weighting of each neuron
    	ArrayList<Double> weightings = new ArrayList<Double>();
    	
    	double weightings_sum = 0;
    	for(Neuron n : neurons){
    		double dist = n.distance(dp);
    		double new_val = (dist > 0) ? 1.0/dist : Double.MAX_VALUE;
    		weightings.add(new_val);
    		weightings_sum+=new_val;
    	}
    	
    	//We compute the weights of the new neuron
    	ArrayList<Float> weights = new ArrayList<Float>();
    	for(int i=0; i<dp.getWeights().size(); i++){
    		double weight=0;
    		
    		int j=0;
    		for(Neuron n : neurons){
    			weight += weightings.get(j) * n.getWeight(i);
    			j++;
    		}
    		
    		weights.add(new Float(weight/ weightings_sum));
    	}
    	
    	barycenter = new Neuron(weights);
    	
    	return barycenter;
    }
   
	@Override
	public boolean equals(Object o){
		Neuron n = (Neuron) o;		
		boolean eq = true;
		
		for (int i = 0; i < n.getWeights().size(); i++) {
			if(weights.get(i) != n.getWeights().get(i))
				eq=false;
		}
		
		return eq;
	}
	
	//******************GETTERS AND SETTERS******************//
	
	/**
     * Get the position of the neuron in the dimension given in args
     * @param dimension The dimension in which we want to know the position of the neuron
     * @return The position of the neuron in this dimension of the grid
     */
    public Integer getPosition(int dimension){
    	return grid_positions.get(dimension);
    }
	
    public void set(Neuron n){
    	grid_positions = n.grid_positions;
    	weights = n.weights;
    	neighbors = n.neighbors;
    }
	
    public ArrayList<Neuron> getNeighbors(){
    	return neighbors;
    }
    
    public void setNeighbors(ArrayList<Neuron> n){
    	neighbors = n;
    }
	
	public ArrayList<Float> getWeights() {
    	return weights;
    }
	
	public ArrayList<Float> getWeights(int begin, int end) {
    	ArrayList<Float> w = new ArrayList<Float>();
    	
    	for(int i=begin; i<=end; i++){
    		w.add(weights.get(i));
    	}
    	
    	return w;
    }
	
	public Float getWeight(int index){
		return weights.get(index);
	}

    public void setWeights(ArrayList<Float> weights) {
    	this.weights = weights;
    }

    public void setWeights(double e, double h, ArrayList<DataPoint> dataset) {
    	
    	int current_weight = 0;
    	double wi;
    	
    	for(DataPoint point : dataset){
    		for(Float weight : point.getWeights()){
    			
    			wi = weights.get(current_weight);
    			wi += e * h * (weight - wi);
    			weights.set(current_weight, new Float(wi));
    			
    			current_weight++;
    			
    		}
    	}
    
    }
  
    public void setWeights(double taux, ArrayList<DataPoint> dataset) {
    	setWeights(taux, 1, dataset);
    }
	
}
