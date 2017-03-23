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
	public static volatile boolean const_learning = false;
	
	/**
	 * If we have a constant neighborhood or a computed one.
	 */
	public static volatile boolean const_neighborhood = false;
    
    /**
     * The value of the learning if it is constant. 
     */
	public static volatile double learning_som = 0.1;
    
    /**
     * The value of the neighborhood if it is constant. 
     */
	public static volatile double neighborhood_som = 0.1;
	
}
