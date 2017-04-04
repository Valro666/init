package src.model.options;

/**
 * 
 * This class contains options of all networks extending BasicNetwork.
 * @author valbert
 *
 */
public class BasicOptions {
	
	/**
	 * The number of data in the environment.
	 */
	public static int nb_data = 100000;
	
	/**
	 * The number of epochs the network is evolving.
	 */
	public static int nb_epochs = 100000;
	
	/**
	 * The number of frames the video takes.
	 */
	public static int nb_frames = 1;
	
	/**
	 * If we record a video or not.
	 */
	public static boolean video = false;
	
	/*
	 * Volatile for threads to change values
	 */
	
	/**
	 * If we stop the process.
	 */
	public static volatile Bool stopped = new Bool(true);
    
    /**
     * If we set the pause.
     */
	public static volatile boolean paused = false;
    
    /**
     * If we step one time
     */
	public static volatile boolean stepped = false;
    
    /**
     * The time we must wait at the end of each epoch (to slow the process down)
     */
    public static volatile int time_to_wait = 0;
    
    /**
     * The number of epochs we must refresh (if we don't want to refresh at each epoch)
     */
    public static volatile int refresh = 2;
}
