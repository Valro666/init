package src.control;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JRadioButton;

import src.model.*;

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
	LinkedEnvironment linked_world;
	
	public NetworkController(LinkedEnvironment w){
		linked_world = w;
	}

	@Override
	/**
	 * When we click on a radio button, we change the network
	 */
	public void actionPerformed(ActionEvent arg0) {
		//We get the network the world must holds now
		JRadioButton source=(JRadioButton)arg0.getSource();
		String net = source.getText();
				
		//Then we set it
		linked_world.setNetwork(net);	
	}
	
}
