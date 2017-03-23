package model;

import model.options.*;

/**
 * 
 * A random network is a special network that frequently changes the shape of data. 
 * @author valbert
 *
 */
public class RandomNetwork extends SpecialNetwork{

	public RandomNetwork(BasicNetwork n) {
		super(n);
	}

	@Override
	public void learn() {

		world.setData(Data.SHAPES[6]);
		network.learn();

		int i=0;
		BasicOptions basic = Options.getInstance().getBasic();
		while (i < 100 && !basic.getStopped()) {
			
		    //We get the number of data. 
		    int nbNew = (int)(Math.random() * world.getData().size());
		    double lx = Math.random();
		    double ly = Math.random();
		    
		    for (int j = 0 ; j < nbNew ; j++) {
		    	
				int index = (int)(Math.random() * world.getData().size());
				DataPoint data = world.getData().get(index);
				double nbdim = Math.random();
				
				if (nbdim < .333) {			
					
				    //We modify the first dimension
				    data.setWeight(0, new Float(Math.random() * lx));
				    
				} else if (nbdim < .666) {
					
					data.setWeight(1, new Float(Math.random() * ly));
	
				} else {
					
				    //We modify the two dimensions
					data.setWeight(0, new Float(Math.random() * lx));
					data.setWeight(1, new Float(Math.random() * ly));
				    
				}
		    }
		    
		    i++;
		    network.learn();
		}

	}
	
}
