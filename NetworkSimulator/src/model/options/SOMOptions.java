package model.options;

/**
 * 
 * This class contains options of SOM.
 * @author valbert
 *
 */
public class SOMOptions {
	
	/**
	 * If we have a constant learning or a computed one.
	 */
	private volatile boolean const_learning;
	
	/**
	 * If we have a constant neighborhood or a computed one.
	 */
    private volatile boolean const_neighborhood;
    
    /**
     * The value of the learning if it is constant. 
     */
    private volatile double learning_som;
    
    /**
     * The value of the neighborhood if it is constant. 
     */
    private volatile double neighborhood_som;
    
    public SOMOptions(){
    	const_neighborhood = false;
    	const_learning = false;
    	learning_som = 0.1;
    	neighborhood_som = 0.1;
    }
    
    //*********************** GETTERS AND SETTERS ***********************//
    
	
	public double getNeighborhoodSom() {
		return neighborhood_som;
	}

	public void setNeighborhoodSom(double neighborhood_som) {
		this.neighborhood_som = neighborhood_som;
	}

	public double getLearningSom() {
		return learning_som;
	}

	public void setLearningSom(double learing_som) {
		this.learning_som = learing_som;
	}
	
	public boolean getLearningCst(){
    	return const_learning;
    }
    
    public void setLearningCst(boolean b){
    	const_learning = b;
    }
    
    public boolean getNeighborhoodCst(){
    	return const_neighborhood;
    }
    
    public void setNeighborhoodCst(boolean c_n){
    	const_neighborhood = c_n;
    }
}
