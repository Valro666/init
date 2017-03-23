package view;

import java.awt.*;
import java.awt.event.*;
import java.io.*;

import javax.swing.*;
import javax.swing.filechooser.*;

import video.*;
import model.*;
import model.options.*;

@SuppressWarnings("serial")
public class ButtonsPanel extends JPanel{
	
	/**
	 * Buttons to control the network.
	 */
	private JButton btn_options, btn_launch, btn_launch_video;
	
	/**
	 * The environment where the network evolves. 
	 */
	private Environment world;
	
	/**
	 * The frame that contains the panel. 
	 */
	private WorldInterface holder;
	
	/**
	 * A boolean to keep the previous state of the world (occurring -> true, else -> false)
	 */
	private boolean previous_state;
	
	/**
	 * Creates a panel with control buttons for the network. 
	 * @param parent The main frame of the GUI.
	 * @param w The world where it takes place. 
	 */
	public ButtonsPanel(WorldInterface parent, Environment w){
		
		holder = parent;
		world = w;
		
		//We create buttons
		btn_options = new JButton("Options");
		btn_launch = new JButton("Launch");
		btn_launch_video = new JButton("Launch and rec");
		
		//We add them listeners
		addListeners();
		
		//We add them to the object
		setLayout(new GridLayout(1, getClass().getDeclaredFields().length));
		add(btn_options);
		add(btn_launch);
		add(btn_launch_video);
	}
	
	/**
	 * Change the state of the enability of buttons. 
	 * @param current_state
	 */
	public void change(boolean current_state){
		
		//If there is a process, we can't play another. 
		btn_launch.setEnabled(!current_state);
		btn_launch_video.setEnabled(!current_state);
		
		previous_state = !previous_state;
	}
	
	/**
	 * Add the listeners to the buttons of the panel. 
	 */
	public void addListeners(){
		
		btn_options.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				holder.launchOptions();
			}
		});
		
		btn_launch.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				start();
			}
		});
		
		btn_launch_video.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				
				BasicOptions basic = Options.getInstance().getBasic();
				basic.setVideo(true);
				
				int nb_frames=-1;
				String s="";
				
				//We get the number of frames the users want to use. 
				while(nb_frames<=0 && s!=null){
					s = JOptionPane.showInputDialog("We take 1 image on : ");
					
					if(s!=null){
						try{
							nb_frames = Integer.parseInt(s);
							
							if(nb_frames <=0)
								throw new Exception();
							
						}catch(Exception e){
							JOptionPane.showConfirmDialog(null, "This is not a valid number (must be over than 0)", "Error", 0, JOptionPane.ERROR_MESSAGE);
						}
					}
				}
				
				//If the number selected is correct, we launch the algorithm
				if(nb_frames>0){
					basic.setNbFrames(nb_frames);
					
					//We delete all files in the directory to have an empty one
					File f = new File("./Images");
					File[] files = f.listFiles();
					for (int i = 0; i < files.length; i++) {
						files[i].delete();
					}
					
					start();
					
					Thread th = new Thread(){
						public void run(){
							try {
								world.getNet().join();
								
								//We select the video file
								JFileChooser jfc = new JFileChooser();
								FileNameExtensionFilter filter = new FileNameExtensionFilter("MOV", ".mov");
								jfc.setFileFilter(filter);
								
								int returnVal = jfc.showOpenDialog(null);
								
								if(returnVal == JFileChooser.FILES_ONLY) {
								     String name = jfc.getSelectedFile().getAbsolutePath();
								     File f = new File("./Images");
								     ConvertVideo.fromImagesToVideo(f.getAbsolutePath(), name);
								     JOptionPane.showConfirmDialog(null, "Your video is now created !", "OK", 0, JOptionPane.INFORMATION_MESSAGE);
								}else{
								     JOptionPane.showConfirmDialog(null, "Your video was not created !", "Not created", 0, JOptionPane.WARNING_MESSAGE);
								}
								
								Options.getInstance().getBasic().setVideo(false);
								
								//We delete all files in the directory to have an empty one
								File f = new File("./Images");
								File[] files = f.listFiles();
								for (int i = 0; i < files.length; i++) {
									files[i].delete();
								}
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
						}
					};
					
					th.start();
				}
			}
		});

	}
	
	/**
	 * Prepare the world for the advent of the network and launch it. 
	 */
	public void start(){
		//We clear the world...
		world.clear();
		
		//...then we create it again, just like god.
		world.getData().resetData();
		
		/*We create a new network based on the previous network because if we want to 
		  launch the same network without clicking on it in the list_network, there is 
		  a Thread error*/
		NetworkFactory nf = new NetworkFactory(world);
		AbstractNetwork n = nf.getNetwork(world.getNet());
		
		//We fill the network
		n.fill();
		world.setNet(n);
		
		world.getNet().start();
	}
}
