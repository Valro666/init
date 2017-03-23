package view;

import java.awt.BorderLayout;

import model.options.*;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Hashtable;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileNameExtensionFilter;

import model.Data;

@SuppressWarnings("serial")
public class OptionsFrame extends JFrame{
	
	private WorldInterface holder;
	private JPanel container;
	
	private JButton btn_save,  btn_load,  btn_keep_subsequent;
	private JButton btn_clear, btn_stop, btn_pause, btn_step;
	
	/*--- Options for AbstractNetwork ---*/
		private JFormattedTextField field_nb_data;
		private JFormattedTextField field_nb_epochs;
		private JFormattedTextField field_time_to_wait;
		private JFormattedTextField field_refresh;
		
	/*--- Options for AbstractMap (SOM, DSOM) ---*/
		private JSlider slider_width;
		private JSlider slider_height;
		
	/*--- Options for SOM ---*/
		private JCheckBox choice_learning;
		private JCheckBox choice_neighbor;
		private JSpinSliderWrapper wrapper_neighborhood_som;
		private JSpinSliderWrapper wrapper_learning_som;
		
	/*--- Options for DSOM ---*/
		private JSpinSliderWrapper wrapper_elasticity;
		private JSpinSliderWrapper wrapper_epsilon;
		
	/*--- Options for GNG ---*/
		private JSlider slider_nb_dep;
		private JFormattedTextField field_max_age;
		private JFormattedTextField field_max_neurons;
		private JFormattedTextField field_time_new;
		private JFormattedTextField field_variation_all;
		private JFormattedTextField field_variation_worst;
		private JSpinSliderWrapper wrapper_neighborhood_gng;
		private JSpinSliderWrapper wrapper_winner_gng;
		
		
	public OptionsFrame(WorldInterface h){
		super();
		
		holder=h;
		
		btn_save = new JButton("Save");
		btn_load = new JButton("Load");
		btn_keep_subsequent = new JButton("Keep for subsequent simulations");
		
		btn_clear = new JButton("Clear");
		btn_stop = new JButton("Stop");
		btn_pause = new JButton("Pause");
		btn_step = new JButton("Step");
		
		//We initialize all attributes
		
		initializeBasic();
		
		initializeMap();
		
		initializeSOM();
		
		initializeDSOM();
		
		initializeGNG();

		//We add them listeners
		addListeners();
		
		//We will add components later with switcher()
		container = new JPanel();
		
		setDefaultCloseOperation(HIDE_ON_CLOSE);
	}
	
	public void initializeBasic(){
		BasicOptions options_basic = Options.getInstance().getBasic();
		
		field_nb_data = new JFormattedTextField(new Integer(options_basic.getNbData()));
		field_nb_epochs = new JFormattedTextField(new Integer(options_basic.getNbEpochs()));
		field_time_to_wait = new JFormattedTextField(new Integer(options_basic.getTimeToWait()));
		field_refresh = new JFormattedTextField(new Integer(options_basic.getRefresh()));
	}
	
	public void initializeMap(){
		MapOptions options_map = Options.getInstance().getMap();
		
		slider_width = getIntSlider(1, 10, 1, options_map.getNbColumns());
		slider_height = getIntSlider(1, 10, 1, options_map.getNbRows());
	}
	
	public void initializeSOM(){
		SOMOptions options_som = Options.getInstance().getSOM();
		
		//We initialize checkboxes
		choice_learning = new JCheckBox();
		choice_neighbor = new JCheckBox();
		
		choice_learning.setSelected(options_som.getLearningCst());
		choice_neighbor.setSelected(options_som.getNeighborhoodCst());
		
		//The the wrappers
		Double neigh_min, neigh_max, neigh_step, neigh_val;
		neigh_min = new Double(0.05);
		neigh_max = new Double(0.2);
		neigh_val = new Double(options_som.getNeighborhoodSom());
		neigh_step = new Double(0.01);
		
		wrapper_neighborhood_som = new JSpinSliderWrapper(neigh_min, neigh_max, neigh_val, neigh_step);
		wrapper_neighborhood_som.setEnabled(false);
		
		Double learn_min, learn_max, learn_val, learn_step;
		
		learn_min = new Double(0.05);
		learn_max = new Double(2);
		learn_val = new Double(options_som.getLearningSom());
		learn_step = new Double(0.01);
		
		wrapper_learning_som = new JSpinSliderWrapper(learn_min, learn_max, learn_val, learn_step);
		wrapper_learning_som.setEnabled(false);
	}
	
	public void initializeDSOM(){
		
		DSOMOptions options_dsom = Options.getInstance().getDSOM();
		
		Double ela_min, ela_max, ela_step, ela_val;
		ela_min = new Double(0.05);
		ela_max = new Double(5);
		ela_step = new Double(0.01);
		ela_val = new Double(options_dsom.getElasticity());
		
		wrapper_elasticity = new JSpinSliderWrapper(ela_min, ela_max, ela_val, ela_step);
		wrapper_elasticity.setNotNull(true);
		
		Double epsi_min, epsi_max, epsi_step, epsi_val;
		
		epsi_min = new Double(0);
		epsi_max = new Double(1);
		epsi_step = new Double(0.1);
		epsi_val = new Double(options_dsom.getEpsilon());
		
		wrapper_epsilon = new JSpinSliderWrapper(epsi_min, epsi_max, epsi_val, epsi_step);
	}
	
	public void initializeGNG(){
		GNGOptions options_gng = Options.getInstance().getGNG();
		
		slider_nb_dep = getIntSlider(2, 10, 1, options_gng.getNbDep());
		
		field_max_age = new JFormattedTextField(new Integer(options_gng.getMaxAge()));
		field_max_neurons = new JFormattedTextField(new Integer(options_gng.getMaxNeurons()));
		field_time_new = new JFormattedTextField(new Integer(options_gng.getTimeNew()));
		
		NumberFormat nf = NumberFormat.getInstance();
		nf.setMaximumFractionDigits(10);
		
		field_variation_all = new JFormattedTextField(nf);
		field_variation_worst = new JFormattedTextField(nf);
		
		field_variation_all.setValue(new Double(options_gng.getVariationForAll()));
		field_variation_worst.setValue(new Double(options_gng.getVariationForWorst()));
		
		Double neigh_min, neigh_max, neigh_step, neigh_val;
		
		neigh_min = new Double(0.00001);
		neigh_max = new Double(0.0001);
		neigh_val = new Double(options_gng.getNeighborhoodGng());
		neigh_step = new Double(0.00001);
		
		wrapper_neighborhood_gng = new JSpinSliderWrapper(neigh_min, neigh_max, neigh_val, neigh_step);
		
		Double win_min, win_max, win_val, win_step;
		
		win_min = new Double(0.01);
		win_max = new Double(1);
		win_val = new Double(options_gng.getWinnerDistGng());
		win_step = new Double(0.01);
		
		wrapper_winner_gng = new JSpinSliderWrapper(win_min, win_max, win_val, win_step);
	}
	
	public JSlider getIntSlider(int begin, int end, int step, int value){
		JSlider slider = new JSlider(begin, end);
		
		Hashtable<Integer, JLabel> table = new Hashtable<Integer, JLabel>();
		for(int i=begin; i<=end; i++) table.put(i, new JLabel(i+""));
		
		slider.setLabelTable(table);
		slider.setMinorTickSpacing(1);
		slider.setPaintTicks(true);
		slider.setPaintLabels(true);
		
		slider.setValue(value);
		
		return slider;
	}
	
	/**
	 * This method disable elements that can't be used during a process but don't change
	 * those which must be enabled during processes (elasticity, ...)
	 * @param occurring_process True if there is a process
	 */
	public void disable(boolean occurring_process){
		
		btn_step.setEnabled(occurring_process);
		btn_pause.setEnabled(occurring_process);
		btn_stop.setEnabled(occurring_process);
		btn_clear.setEnabled(!occurring_process);
		field_nb_data.setEnabled(!occurring_process);
		field_nb_epochs.setEnabled(!occurring_process);
		slider_width.setEnabled(!occurring_process);
		slider_height.setEnabled(!occurring_process);
		slider_nb_dep.setEnabled(!occurring_process);
		field_max_age.setEnabled(!occurring_process);
		field_max_neurons.setEnabled(!occurring_process);
		field_time_new.setEnabled(!occurring_process);
	}
	
	/**
	 * This method switch the interface depending on the network held by the world
	 * @param s Name of the network (get with its toString())
	 */
	public void switcher(String s){
		container = new JPanel();
		//We set 0 to have an infinite possibility of rows
		container.setLayout(new GridLayout(0, 1, 30, 30)); 
		setAbstractNetworkFrame();
		
		HashMap<String, Integer> network_names = new HashMap<String, Integer>();
		network_names.put("SOM", 0);
		network_names.put("DSOM", 1);
		network_names.put("GNG", 2);
		
		switch(network_names.get(s)){
			case 0:
				setSOMFrame();
			break;
			
			case 1:
				setDSOMFrame();
			break;
				
			case 2:
				setGNGFrame();
			break;
		}
		
		JPanel control_buttons = new JPanel();
		control_buttons.add(btn_clear);
		control_buttons.add(btn_stop);
		control_buttons.add(btn_pause);
		control_buttons.add(btn_step);
		
		container.add(control_buttons);
		
		JPanel buttons = new JPanel();
		buttons.add(btn_save);
		buttons.add(btn_load);
		buttons.add(btn_keep_subsequent);
		
		container.add(buttons);
		setContentPane(container);
		pack();
		disable(false);
	}
	
	public void setAbstractNetworkFrame(){
		BasicOptions basic = Options.getInstance().getBasic();
		
		field_nb_data.setValue(new Integer(basic.getNbData()));
		field_nb_epochs.setValue(new Integer(basic.getNbEpochs()));
		field_refresh.setValue(basic.getRefresh());
		field_time_to_wait.setValue(new Integer(basic.getTimeToWait()));
		
		add("Number of data : ", field_nb_data);
		add("Number of epochs : ", field_nb_epochs);
		add("Refresh : ", field_refresh);
		add("Time to wait (for the speed) : ", field_time_to_wait);
	}
	
	public void setAbstractMapFrame(){
		MapOptions options_map = Options.getInstance().getMap();
		
		slider_width.setValue(options_map.getNbColumns());
		slider_height.setValue(options_map.getNbRows());
		
		add("Width of the grid : ", slider_width);
		add("Height of the grid : ", slider_height);
	}
	
	public void setSOMFrame(){
		setAbstractMapFrame();
		
		SOMOptions options_som = Options.getInstance().getSOM();
		
		choice_learning.setSelected(options_som.getLearningCst());
		choice_neighbor.setSelected(options_som.getNeighborhoodCst());
		
		//We add the two checkboxes on the same line
		String[] labels = {"Constant learning : ", "Constant neighborhood : "};
		JCheckBox[] boxes = {choice_learning, choice_neighbor};
		add(labels, boxes);

		add("Learning :           ", wrapper_learning_som);
		add("Neighborhood : ", wrapper_neighborhood_som);;
		
		setTitle("Options - SOM");
	}
	
	public void setDSOMFrame(){
		setAbstractMapFrame();
		
		setTitle("Options - DSOM");
		
		add("Elasticity : ", wrapper_elasticity);
		add("Epsilon : ", wrapper_epsilon);
	}
	
	public void setGNGFrame(){
		setTitle("Options - GNG");
		
		GNGOptions options_gng = Options.getInstance().getGNG();
		
		slider_nb_dep.setValue(options_gng.getNbDep());
		field_variation_all.setValue(new Double(options_gng.getVariationForAll()));
		field_variation_worst.setValue(new Double(options_gng.getVariationForWorst()));
		
		add("Number of neurons : ", slider_nb_dep);
		add("Nb max of neurons : ", field_max_neurons);
		add("Age max of connexions : ", field_max_age);
		add("Time of new neuron : ", field_time_new);
		add("Variation for all : ", field_variation_all);
		add("Variation for worst : ", field_variation_worst);
		add("Neighborhood : ", wrapper_neighborhood_gng);
		add("Winner distance", wrapper_winner_gng);
	}
	
	/**
	 * A method to add a new row with a label and a component
	 * @param label
	 * @param component
	 */
	public void add(String label, JComponent component){
		JPanel new_row = new JPanel();
		new_row.setLayout(new BorderLayout());
		
		new_row.add(new JLabel(label), BorderLayout.WEST);
		new_row.add(component, BorderLayout.CENTER);
		
		container.add(new_row);
	}
	
	/**
	 * A method to add a new row with a few labels and components
	 * @param labels
	 * @param components
	 */
	public void add(String[] labels, JComponent[] components){
		JPanel new_row = new JPanel();
		new_row.setLayout(new GridLayout(1, 0));
		
		for (int i = 0; i < labels.length && i < components.length; i++) {
			JPanel sub_row = new JPanel();
			sub_row.setLayout(new BorderLayout());
			
			sub_row.add(new JLabel(labels[i]), BorderLayout.WEST);
			sub_row.add(components[i],BorderLayout.CENTER);
			
			new_row.add(sub_row);
		}
		
		container.add(new_row);
	}
	
	public void addListeners(){
		
		addBasicListeners();
		
		addMapListeners();
		
		addSOMListeners();
		
		addDSOMListeners();
		
		addGNGListeners();

	}
	
	public void addBasicListeners(){
		
		final BasicOptions o_basic = Options.getInstance().getBasic();
		
		btn_clear.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				holder.getWorld().clear();
			}
		});
		
		btn_stop.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				synchronized (holder.getWorld()) {
					
					o_basic.setStopped(true);
					o_basic.setPause(false);
					
					btn_pause.setText("Pause");
					holder.getWorld().notifyAll();
				}
			}
		});
		
		btn_pause.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				synchronized (holder.getWorld()) {
					
					if(o_basic.getPaused()){
						o_basic.setPause(false);
						btn_pause.setText("Pause");
					}else{
						o_basic.setPause(true);
						btn_pause.setText("Resume");
					}
					holder.getWorld().notifyAll();
					
				}
			}
		});
		
		btn_step.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				
				o_basic.setStep(true);
				synchronized (holder.getWorld()) {
					holder.getWorld().notifyAll();
				}
				
			}
		});
		
		btn_save.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				
				JFileChooser jfc = new JFileChooser();
				FileNameExtensionFilter filter = new FileNameExtensionFilter("XML", "xml");
				jfc.setFileFilter(filter);
				
				int returnVal = jfc.showOpenDialog(null);
				
				if(returnVal == JFileChooser.FILES_ONLY) {
				     String name = jfc.getSelectedFile().getAbsolutePath();
				     Options.getInstance().toXML(name);
				     JOptionPane.showConfirmDialog(null, "Your configuration is now saved !", "OK", 0, JOptionPane.INFORMATION_MESSAGE);
				}else{
				     JOptionPane.showConfirmDialog(null, "Your configuration is not saved !", "Not saved", 0, JOptionPane.WARNING_MESSAGE);
				}
			}
		});
		
		btn_load.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				
				JFileChooser jfc = new JFileChooser();
				FileNameExtensionFilter filter = new FileNameExtensionFilter("XML", "xml");
				jfc.setFileFilter(filter);
				
				int returnVal = jfc.showOpenDialog(null);
				
				if(returnVal == JFileChooser.FILES_ONLY) {
				     String path = jfc.getSelectedFile().getAbsolutePath();
				     Options.fromXML(path);
				     holder.getWorld().change();
				     
				     JOptionPane.showConfirmDialog(null, "Your configuration is now loaded !", "OK", 0, JOptionPane.INFORMATION_MESSAGE);
				}else{
				     JOptionPane.showConfirmDialog(null, "Your configuration is not loaded !", "Not saved", 0, JOptionPane.WARNING_MESSAGE);
				}
			}
		});
		
		btn_keep_subsequent.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Options.getInstance().saveAsInitConfig();
			}
		});
		
		field_nb_data.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				o_basic.setNbData((Integer) field_nb_data.getValue());
				Data.getInstance().resetData();
				holder.getWorld().change();
			}
		});
		
		field_nb_epochs.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				o_basic.setNbEpochs((Integer) field_nb_epochs.getValue());
			}
		});
		
		field_refresh.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				o_basic.setRefresh((Integer)field_refresh.getValue()); 
			}
		});
		
		field_time_to_wait.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				
				int time = (Integer) field_time_to_wait.getValue();
				
				if(time < 0) time=0;
				o_basic.setTimeToWait(time); 
			}
		});
	}
	
	public void addMapListeners(){
		final MapOptions o_map = Options.getInstance().getMap();
		
		slider_width.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent arg0) {
				o_map.setNbColumns(slider_width.getValue());
			}
		});
		
		slider_height.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				o_map.setNbRows(slider_height.getValue());
			}
		});
	}
	
	public void addSOMListeners(){
		final SOMOptions o_som = Options.getInstance().getSOM();
		
		choice_learning.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				o_som.setLearningCst(choice_learning.isSelected());
				wrapper_learning_som.setEnabled(choice_learning.isSelected());
			}
		});
		
		choice_neighbor.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				o_som.setNeighborhoodCst(choice_neighbor.isSelected());
				wrapper_neighborhood_som.setEnabled(choice_neighbor.isSelected());
			}
		});
		
		wrapper_neighborhood_som.addChangeListener(new ChangeListener(){
			@Override
			public void stateChanged(ChangeEvent e) {
				o_som.setNeighborhoodSom(((JSpinSlider)e.getSource()).getValue());
			}
		});
		
		wrapper_learning_som.addChangeListener(new ChangeListener(){
			@Override
			public void stateChanged(ChangeEvent e) {
				o_som.setLearningSom(((JSpinSlider)e.getSource()).getValue());
			}
		});
	}
	
	public void addDSOMListeners(){
		final DSOMOptions options_dsom = Options.getInstance().getDSOM();
		
		wrapper_elasticity.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				options_dsom.setElasticity(((JSpinSlider)e.getSource()).getValue());
			}
		});
		
		wrapper_epsilon.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				options_dsom.setEpsilon(((JSpinSlider)e.getSource()).getValue());
			}
		});
	}
	
	public void addGNGListeners(){
		final GNGOptions options_gng = Options.getInstance().getGNG();
		
		slider_nb_dep.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				options_gng.setNbDep(slider_nb_dep.getValue());
			}
		});
		
		field_max_age.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				options_gng.setMaxAge((Integer) field_max_age.getValue());
			}
		});
		
		field_max_neurons.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				options_gng.setMaxNeurons((Integer) field_max_neurons.getValue());
			}
		});
		
		field_time_new.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				options_gng.setTimeNew((Integer) field_time_new.getValue());
			}
		});
		
		field_variation_all.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				options_gng.setVariationForAll((Double) field_variation_all.getValue());
			}
		});
		
		field_variation_worst.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				options_gng.setVariationForWorst((Double) field_variation_worst.getValue());
			}
		});
		
		wrapper_neighborhood_gng.addChangeListener(new ChangeListener(){
			@Override
			public void stateChanged(ChangeEvent e) {
				options_gng.setNeighborhoodGng(((JSpinSlider)e.getSource()).getValue());
			}
		});
		
		wrapper_winner_gng.addChangeListener(new ChangeListener(){
			@Override
			public void stateChanged(ChangeEvent e) {
				options_gng.setWinnerDistGng(((JSpinSlider)e.getSource()).getValue());
			}
		});
	}
}
