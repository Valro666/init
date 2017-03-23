package model.options;

import java.util.Observable;

public class Bool extends Observable{
	private boolean bool;
	
	public Bool(){
		bool = false;
	}
	
	public Bool(boolean b){
		bool=b;
	}
	
	public boolean bool(){
		return bool;
	}
	
	public void setBool(boolean b){
		bool = b;
		setChanged();
		notifyObservers();
	}
}
