package model;

import java.awt.geom.Ellipse2D;
import model.options.*;
import java.awt.geom.Point2D;
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
	 * An array describing all the shapes that data can have. 
	 */
	public static char[] SHAPES = {'S', 'M', 'R', 'C', '2', '3', 's', 'c', 'D'};
	
	/**
	 * The instance of Data. 
	 */
	private static Data DATA = new Data();
	
	/**
	 * The list of all data. 
	 */
	private ArrayList<DataPoint> data;
	
	/**
	 * The current shape of data. 
	 */
	private char shape;
	
	/**
	 * The number of dimensions of the data.
	 */
	private int nb_dimensions;
	
	private Data(){
		
		//We create a fake data object which is empty, for the beginning. 
		setData('C');
		data.clear();
		
	}
	
	/**
	 * Get data of the environment.
	 * @return The instance of data. 
	 */
	public static Data getInstance(){
		return DATA;
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
	
	/**
	 * Change a particular data. 
	 * @param index The index of the data to be modified.
	 * @param dp The new data. 
	 */
	public void set(int index, DataPoint dp){
		data.set(index, dp);
	}
	
	/**
	 * Change the shape of data.
	 * @param c The char representing data. 
	 */
	public void setData(char c){
		data = generateData(c);
		shape = c;
	}
	
	/**
	 * Regenerates data (in case of the shape does not change). 
	 */
	public void resetData(){
		data = generateData(shape);
	}
	
	/**
	 * Clear data (empties the list). 
	 */
	public void clear(){
		data.clear();
	}
	
	/**
	 * Generates data
	 * @param c The shape of data
	 * @param data.size() The number of data
	 * @return An arrayList containing data
	 */
	public ArrayList<DataPoint> generateData(char c){
		ArrayList<DataPoint> data = new ArrayList<DataPoint>();
		Options options = Options.getInstance();
		BasicOptions basic = options.getBasic();
		
		switch(c){
		case 'S': //Square
		    for (int nb = 0; nb < basic.getNbData() ; nb ++) {
				data.add(new DataPoint(nb_dimensions));
		    }
		    break;

		case 'M': //Moon	
		    int nb = 0;
		    Ellipse2D.Double n = new Ellipse2D.Double(0,0, 1, 1);
		    Ellipse2D.Double n2 = new Ellipse2D.Double(0,0, .6, .6);
		    
		    while(nb<basic.getNbData()){
				double x = Math.random();
				double y = Math.random();
				Point2D.Double p = new Point2D.Double(x,y);
				if(n.contains(p) && !n2.contains(p)){
				    ArrayList<Float> poids = new ArrayList<Float>();
				    poids.add(new Float(x));
				    poids.add(new Float(y));
				    data.add(new DataPoint(poids));
				    nb++;
				}
		    }
		    
		    break;
		case 'R': // Ring
		    int nb2 = 0;
		    Ellipse2D.Double n1 = new Ellipse2D.Double(   0,  0, 1, 1);
		    Ellipse2D.Double n22 = new Ellipse2D.Double(.2,.2, .6, .6);
		    while(nb2<basic.getNbData()){
				double x = Math.random();
				double y = Math.random();
				Point2D.Double p = new Point2D.Double(x,y);
				if(n1.contains(p) && !n22.contains(p)){
				    ArrayList<Float> poids = new ArrayList<Float>();
				    poids.add(new Float(x));
				    poids.add(new Float(y));
				    data.add(new DataPoint(poids));
				    nb2++;
				}
		    }
		    break;			
		case 'C': //Circle
		    int nb3 = 0;
		    Ellipse2D.Double n3 = new Ellipse2D.Double(0,0, 1, 1);
		    while(nb3<basic.getNbData()){
				double x = Math.random();
				double y = Math.random();
				Point2D.Double p = new Point2D.Double(x,y);
				if(n3.contains(p)){
				    ArrayList<Float> poids = new ArrayList<Float>();
				    poids.add(new Float(x));
				    poids.add(new Float(y));
				    data.add(new DataPoint(poids));
				    nb3++;
				}
		    }
		    break;
		case '2': // Two densities
		    int nb4;
		    for (nb4 = 0; nb4 < (int) (basic.getNbData()/3) ; nb4 ++) {
				double x = Math.random()/3;
				double y = Math.random()/3;
				ArrayList<Float> poids = new ArrayList<Float>();
				poids.add(new Float(x));
				poids.add(new Float(y));
				data.add(new DataPoint(poids));
		    }
		    while (nb4 < basic.getNbData()) {
				nb4 ++;
				double x = Math.random();
				double y = Math.random();
				ArrayList<Float> poids = new ArrayList<Float>();
				poids.add(new Float(x));
				poids.add(new Float(y));
				data.add(new DataPoint(poids));
		    }    	    	    
		    break;
		case '3': // Three densities
		    int nb5 = 0;
		    Ellipse2D.Double n5 = new Ellipse2D.Double(.1,.1, .4, .4);
		    while (nb5 < (int)(basic.getNbData()/3)) {
				double x = Math.random();
				double y = Math.random();
				Point2D.Double p = new Point2D.Double(x,y);
				if(n5.contains(p)){
				    ArrayList<Float> poids = new ArrayList<Float>();
				    poids.add(new Float(x));
				    poids.add(new Float(y));
				    data.add(new DataPoint(poids));
				    nb5++;
				}
		    }
		    n5 = new Ellipse2D.Double(.7,.4, .2, .2);
		    while (nb5 < (int)(basic.getNbData()/2)) {
			double x = Math.random();
			double y = Math.random();
				Point2D.Double p = new Point2D.Double(x,y);
				if(n5.contains(p)){
				    ArrayList<Float> poids = new ArrayList<Float>();
				    poids.add(new Float(x));
				    poids.add(new Float(y));
				    data.add(new DataPoint(poids));
				    nb5++;
				}
		    }
		    while (nb5 < basic.getNbData()) {
				nb5 ++;
				double x = Math.random();
				double y = Math.random();
				ArrayList<Float> poids = new ArrayList<Float>();
				poids.add(new Float(x));
				poids.add(new Float(y));
				data.add(new DataPoint(poids));
		    }	    	    	    
		    break;
		
		case 's': // Little square
		    int nb6;
		    for (nb6 = 0; nb6 <basic.getNbData() ; nb6 ++) {
				data.add(new DataPoint(nb_dimensions));
				data.get(data.size()-1).setWeight(0, data.get(data.size()-1).getWeight(0)/2);
				data.get(data.size()-1).setWeight(1, data.get(data.size()-1).getWeight(1)/2);
		    }	    	    	    
		    break;


		case 'c': // Little circle
		    int nb7= 0;
		    Ellipse2D.Double n7 = new Ellipse2D.Double(.6,.6, .4, .4);
		    while (nb7 < basic.getNbData()/2) {
				double x = Math.random();
				double y = Math.random();
				Point2D.Double p = new Point2D.Double(x,y);
				if(n7.contains(p)){
				    ArrayList<Float> poids = new ArrayList<Float>();
				    poids.add(new Float(x));
				    poids.add(new Float(y));
				    data.add(new DataPoint(poids));
				    nb7++;
				}
		    }	    	    	    
		    break;
		    
		case 'D': // Distinct densities
		    int nb8 = 0;
		    Ellipse2D.Double n8 = new Ellipse2D.Double(.5,.1, .45, .45);
		    while (nb8 < (int)(basic.getNbData()/3)) {
				double x = Math.random();
				double y = Math.random();
				Point2D.Double p = new Point2D.Double(x,y);
				if(n8.contains(p)){
				    ArrayList<Float> poids = new ArrayList<Float>();
				    poids.add(new Float(x));
				    poids.add(new Float(y));
				    data.add(new DataPoint(poids));
				    nb8++;
				}
		    }
		    n8 = new Ellipse2D.Double(.2,.7, .25, .25);
		    while (nb8 < (int)(basic.getNbData())) {
				double x = Math.random();
				double y = Math.random();
				Point2D.Double p = new Point2D.Double(x,y);
				if(n8.contains(p)){
				    ArrayList<Float> poids = new ArrayList<Float>();
				    poids.add(new Float(x));
				    poids.add(new Float(y));
				    data.add(new DataPoint(poids));
				    nb8++;
				}
		    }

		    break;
		}
		
		return data;
	}
	
	
}
