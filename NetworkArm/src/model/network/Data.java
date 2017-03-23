package model.network;

import model.options.*;
import java.util.ArrayList;

/**
 * 
 * A class that represents all the map of data
 * It's a singleton to access it everywhere and to prevent multiple instances. 
 * @author valbert
 *
 */
public class Data {
	
	/**
	 * The list of all data. 
	 */
	private ArrayList<DataPoint> data;
	
	/**
	 * The number of dimensions of the data.
	 */
	private int nb_dimensions;
	
	public Data(int nb_dim){
		
		//We create a fake data object which is empty, for the beginning.
		nb_dimensions = nb_dim;
		data = generateData();
		
	}
	
	public int getNbDimensions(){
		return nb_dimensions;
	}
	
	/**
	 * Set the number of dimensions there is in the world. 
	 * @param dim The number of dimensions of the world. 
	 */
	public void setDimensions(int dim){
		nb_dimensions = dim;
	}
	
	/**
	 * Get the size of data. 
	 * @return Number of data held by the world. 
	 */
	public int size(){
		return data.size();
	}
	
	/**
	 * Get a particular data in the world. 
	 * @param i The index of the data. 
	 * @return The data corresponding to the index. 
	 */
	public DataPoint get(int i){
		return data.get(i);
	}
	
	public ArrayList<DataPoint> getDataPoints(){
		return data;
	}

	/**
	 * Generates data
	 * @param c The shape of data
	 * @param data.size() The number of data
	 * @return An arrayList containing data
	 */
	public ArrayList<DataPoint> generateData(){
		
		ArrayList<DataPoint> data = new ArrayList<DataPoint>();
		
	    for (int nb = 0; nb < BasicOptions.nb_data; nb ++) {
			data.add(new DataPoint(nb_dimensions));
	    }

		return data;
	}
	
	public String toString(){
		String ret="";
		
		for(DataPoint p : data){
			ret+=p+"\n";
		}
		
		return ret;
	}
}
