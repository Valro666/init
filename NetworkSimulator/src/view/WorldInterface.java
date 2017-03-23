package view;

import java.util.ArrayList;
import model.options.*;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;

import model.Environment;
import control.DataController;
import control.NetworkController;

@SuppressWarnings("serial")
public class WorldInterface extends JFrame implements Observer{
	
	private JRadioList list_data;
	private JRadioList list_networks;
	private ButtonsPanel buttons;
	private NetworkDraw draw;
	private OptionsFrame options;
	private int nb_updates;
	
	public WorldInterface(){
		
		nb_updates=0;
		
		//the frame for the options
		options = new OptionsFrame(this);
		
		//We create all necessary objects
		draw = new NetworkDraw(550);
		draw.getWorld().addObserver(this);
		
		ArrayList<String> names_data = new ArrayList<String>();
			names_data.add("Square");
			names_data.add("Circle");
			names_data.add("Ring");
			names_data.add("Moon");
			names_data.add("Two densities");
			names_data.add("Three densities");
			names_data.add("Little square");
			names_data.add("Little circle");
			names_data.add("Two distinct densities");
			
		ArrayList<String> names_network=new ArrayList<String>();
			names_network.add("Self Organizing Map");
			names_network.add("Dynamic Self Organizing Map");
			names_network.add("Growing Neural Gas");
			names_network.add("Multiple Self Organizing Map");
			names_network.add("Multiple Dynamic Self Organizing Map");
			names_network.add("Multiple Growing Neural Gas");
			names_network.add("Random Self Organizing Map");
			names_network.add("Random Dynamic Self Organizing Map");
			names_network.add("Random Growing Neural Gas");
		
		list_data = new JRadioList(names_data);
		list_networks = new JRadioList(names_network);
		
		buttons = new ButtonsPanel(this, draw.getWorld());
		
		//We add listeners
		list_data.addActionListenerOnAll(new DataController(draw.getWorld()));
		list_networks.addActionListenerOnAll(new NetworkController(draw.getWorld()));
		
		//We add them to the frame
		JMenuBar menu_bar = new JMenuBar();
			JMenu menu_data = new JMenu("Data");
			JMenu menu_net = new JMenu("Networks");
			menu_data.add(list_data);
			menu_net.add(list_networks);
			
		menu_bar.add(menu_data);
		menu_bar.add(menu_net);
		menu_bar.add(buttons);
			
		setJMenuBar(menu_bar);
		add(draw);
		
		//We initialize data and network
		list_data.setSelected(0);
		list_networks.setSelected(0);
		
		//We show the frame
		pack();
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setVisible(true);
	}
	
	public void launchOptions(){
		options.setVisible(true);
		options.switcher(draw.getWorld().getNet().toString());
	}
	
	public Environment getWorld(){
		return draw.getWorld();
	}
	
	@Override
	public void update(Observable arg0, Object arg1) {
		
		nb_updates++;
		
		draw.repaint();
		
		BasicOptions basic = Options.getInstance().getBasic();
		//We save images (to create a video later)
		if(basic.getVideo() && (nb_updates%basic.getNbFrames() == 0))
			draw.saveImage("./Images/", "jpg");
        
		options.repaint();
		
		//We verify if there is a process is occurring 
		Options opt = Options.getInstance();
		
		if(opt.getBasic().getStopped()){
			buttons.change(false);
			options.disable(false);
			getJMenuBar().getMenu(0).setEnabled(true);
			getJMenuBar().getMenu(1).setEnabled(true);
			
			//We don't switch options when a process occurs, because it can't change and it switches too much times
			if(options.isVisible())
				options.switcher(draw.getWorld().getNet().toString());
		}else{
			buttons.change(true);
			options.disable(true);
			getJMenuBar().getMenu(0).setEnabled(false);
			getJMenuBar().getMenu(1).setEnabled(false);
		}
	}
}
