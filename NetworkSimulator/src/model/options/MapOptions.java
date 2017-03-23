package model.options;

/**
 * 
 * This class contains options of networks extending AbstractMap
 * @author valbert
 *
 */
public class MapOptions {
	
	/**
	 * The number of rows of the map network.
	 */
	private int rows;
	
	/**
	 * The number of cols of the map network.
	 */
	private int cols;
	
	public MapOptions(){
		rows = 10;
    	cols = 10;
	}
	
	//*********************** GETTERS AND SETTERS ***********************//
	
	
	
	public void setNbColumns(int nb){
    	cols = nb;
    }
    
    public void setNbRows(int nb){
    	rows = nb;
    }
    
    public int getNbColumns(){
    	return cols;
    }
    
    public int getNbRows(){
    	return rows;
    }
}
