package model.options;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

import com.thoughtworks.xstream.XStream;

/**
 * 
 * The class that holds all options of the software. 
 * It's a singleton to access it everywhere and to prevent multiple instances. 
 * @author valbert
 *
 */

public class Options{
	
	/**
	 * The options of all basic networks.
	 */
	private BasicOptions basic_options;
	
	/**
	 * The options of all map networks (SOM, DSOM, ...).
	 */
	private MapOptions map_options;
	
	/**
	 * The options of GNG.
	 */
	private GNGOptions gng_options;
	
	/**
	 * The options of DSOM.
	 */
	private DSOMOptions dsom_options;
	
	/**
	 * The options of SOM.
	 */
	private SOMOptions som_options;
	
	/**
	 * The instance of all options.
	 */
	private static Options OPTIONS = new Options();
    
	/**
	 * A private constructor to manage its use. 
	 */
    private Options(){
    	
    	String initial_conf = "./init_config.xml";
    	
    	//We verify if there is no initial configs
    	File file = new File(initial_conf);
    	
    	if(file.exists()){
    		
    		//If it exists, we load configs from this file.
    		Options options = fromXML(initial_conf);
    		
    		//And we change all sub-option of the object.
    		basic_options = options.basic_options;
        	map_options = options.map_options;
        	som_options = options.som_options;
        	dsom_options = options.dsom_options;
        	gng_options = options.gng_options;
    		
    	}else{
    		
    		//Otherwise we initialize all options. 
	    	basic_options = new BasicOptions();
	    	map_options = new MapOptions();
	    	gng_options = new GNGOptions();
	    	dsom_options = new DSOMOptions();
	    	som_options = new SOMOptions();
	    	
    	}
    }
    
    /*
     * A static method to access the singleton
     */
    public static Options getInstance(){
    	return OPTIONS;
    }
    
    /**
     * A method to serialize options into XML
     * @param path The path of the file in which we save configs.
     */
    public void toXML(String path){
    	
    	//We create a parser.
    	XStream writer = new XStream();
    	
    	try {
			
    		//We parse it in XML.
    		String xml = writer.toXML(this);
    		
    		//Then we write he xml string in the file.
    		PrintWriter file = new PrintWriter(new File(path));
			file.print(xml);
			file.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
    
    /**
     * Save current configuration as initial configuration for next times.  
     */
    public void saveAsInitConfig(){
    	
    	//We create a parser. 
    	XStream writer = new XStream();
    	
    	try {
    		//We parse into xml.
    		String xml = writer.toXML(this);
    		
    		//Then we write it in the file of the initial config.
    		PrintWriter file = new PrintWriter(new File("./init_config.xml"));
			file.print(xml);
			file.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
    
    /**
     * Load options from XML.
     * @param path The path of the config file to load. 
     * @return An options object that represents the XML. 
     */
    public static Options fromXML(String path){
    	
    	//We create a parser. 
		XStream writer = new XStream();
		
		//We parse the XML into the object. 
		Options op = (Options) writer.fromXML(new File(path));
		
		return op;
    }
    
    
    //*********************** GETTERS AND SETTERS ***********************//
    
    public BasicOptions getBasic(){
    	return basic_options;
    }
    
    public MapOptions getMap(){
    	return map_options;
    }
    
    public GNGOptions getGNG(){
    	return gng_options;
    }
    
    public SOMOptions getSOM(){
    	return som_options;
    }
    
    public DSOMOptions getDSOM(){
    	return dsom_options;
    }
}
