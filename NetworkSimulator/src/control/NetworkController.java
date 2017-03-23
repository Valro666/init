package control;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JRadioButton;

import model.Environment;
import model.NetworkFactory;

/**
 * 
 * A controller that links radio buttons to networks
 * @author valbert
 *
 */
public class NetworkController implements ActionListener{
	
	/**
	 * The world where the network is evolving
	 */
	Environment world;
	
	public NetworkController(Environment w){
		world = w;
	}

	@Override
	/**
	 * When we click on a radio button, we change the network
	 */
	public void actionPerformed(ActionEvent arg0) {
		//We get the network teh world must holds now
		JRadioButton source=(JRadioButton)arg0.getSource();
		String net = source.getText();
				
		//Then we set it
		NetworkFactory nf = new NetworkFactory(world);
		world.setNet(nf.getNetwork(net));	
	}
	
}
