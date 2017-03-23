package model.options;

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
	private int nb_data;
	
	/**
	 * The number of epochs the network is evolving.
	 */
	private int nb_epochs;
	
	/**
	 * The number of frames the video takes.
	 */
	private int nb_frames;
	
	/**
	 * If we record a video or not.
	 */
	private boolean video;
	
	/*
	 * Volatile for threads to change values
	 */
	
	/**
	 * If we stop the process.
	 */
    private volatile boolean stopped;
    
    /**
     * If we set the pause.
     */
    private volatile boolean paused;
    
    /**
     * If we step one time
     */
    private volatile boolean stepped;
    
    /**
     * The time we must wait at the end of each epoch (to slow the process down)
     */
    private volatile int time_to_wait;
    
    /**
     * The number of epochs we must refresh (if we don't want to refresh at each epoch)
     */
    private volatile int refresh;
    
    //We initialize options
    public BasicOptions(){
    	nb_data = 100000;
    	nb_epochs = 100000;
    	stopped = true;
    	paused = false;
    	stepped = false;
    	time_to_wait=0;
    	refresh = 2;
    	video = false;
    	nb_frames = 1;
    }
    
    
    //************************ GETTERS AND SETTERS ********************************//
    
    public int getNbFrames(){
    	return nb_frames;
    }
    
    public void setNbFrames(int nb){
    	nb_frames = nb;
    }
    
    public boolean getVideo() {
		return video;
	}

	public void setVideo(boolean video) {
		this.video = video;
	}

	public int getTimeToWait() {
		return time_to_wait;
	}

	public void setTimeToWait(int time_to_wait) {
		this.time_to_wait = time_to_wait;
	}
	
	 public boolean getStepped(){
    	return stepped;
    }
    
    public void setStep(boolean s){
    	stepped = s;
    }

    public int getNbData(){
    	return nb_data;
    }
    
    public void setNbData(int nb){
    	nb_data = nb;
    }
    
    public boolean getPaused(){
    	return paused;
    }
    
    public void setPause(boolean p){
    	paused = p;
    	synchronized (this) {
    		notify();
		}
    }
    
    public int getNbEpochs(){
    	return nb_epochs;
    }
    
    public void setNbEpochs(int nb){
    	nb_epochs = nb;
    }
    
    public boolean getStopped(){
    	return stopped;
    }
    
    public void setStopped(boolean b){
    	stopped = b;
    }

	public int getRefresh() {
		return refresh;
	}

	public void setRefresh(int refresh) {
		this.refresh = refresh;
	}
}
