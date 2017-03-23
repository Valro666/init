package model.options;


/**
 * 
 * This class contains options of DSOM.
 * @author valbert
 *
 */
public class DSOMOptions {
	
	/**
	 * The elasticity of the network (change the neighborhood)
	 */
	private volatile double elasticity;
	
	/**
	 * Epsilon : intervenes in the neighborhood too
	 */
    private volatile double epsilon;
    
    public DSOMOptions(){
    	elasticity = 0.65;
    	epsilon = 0.5;
    }
    
    //***************************** GETTERS AND SETTERS *****************************//
    
    public double getEpsilon(){
    	return epsilon;
    }
    
    public void setEpsilon(double e){
    	epsilon = e;
    }
    
    public double getElasticity(){
    	return this.elasticity;
    }
    
    public void setElasticity(double e){
    	elasticity = e;
    }
}
