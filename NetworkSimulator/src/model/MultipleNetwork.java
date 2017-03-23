package model;

import model.options.*;

/**
 * 
 * A special network that frequently changes the density of data. 
 * @author valbert
 *
 */
public class MultipleNetwork extends SpecialNetwork{
	
	/**
	 * Number of times we have to launch the algo. 
	 */
	private final int MULTIDATA = 10;

	public MultipleNetwork(BasicNetwork n) {
		super(n);
	}

	@Override
	public void learn() {
		
		int i=0;
		BasicOptions basic = Options.getInstance().getBasic();
		
		while(i<MULTIDATA && !basic.getStopped()){
			
			//We choose a shape for data
			int shape = (int)(Math.random() * Data.SHAPES.length);
			world.setData(Data.SHAPES[shape]);

			network.learn();
			
			i++;
		}
		
	}
}
