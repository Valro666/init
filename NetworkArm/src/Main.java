package src;

import src.view.View;
import src.model.LinkedEnvironment;

public class Main {
    
    @SuppressWarnings("unused")
	public static void main(String[] args){	
		
		LinkedEnvironment world=null;
		try {
			world = new LinkedEnvironment(4,2);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		View v = new View(world);

    }
    
}
