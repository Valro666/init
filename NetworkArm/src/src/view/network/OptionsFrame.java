package src.view.network;

import java.awt.BorderLayout;

import src.model.options.*;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.NumberFormat;
import java.util.Hashtable;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import src.view.View;

@SuppressWarnings("serial")
public class OptionsFrame extends JFrame implements Observer{
	
	private View holder;
	private JPanel container;
	
	private JButton btn_stop, btn_pause, btn_step;
	
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
		
		
	public OptionsFrame(View view){
		super();
		
		holder=view;
		
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
		field_nb_data = new JFormattedTextField(new Integer(BasicOptions.nb_data));
		field_nb_epochs = new JFormattedTextField(new Integer(BasicOptions.nb_epochs));
		field_time_to_wait = new JFormattedTextField(new Integer(BasicOptions.time_to_wait));
		field_refresh = new JFormattedTextField(new Integer(BasicOptions.refresh));
	}
	
	public void initializeMap(){
		
		slider_width = getIntSlider(1, 10, 1, MapOptions.cols);
		slider_height = getIntSlider(1, 10, 1, MapOptions.rows);
	}
	
	public void initializeSOM(){
		
		//We initialize checkboxes
		choice_learning = new JCheckBox();
		choice_neighbor = new JCheckBox();
		
		choice_learning.setSelected(SOMOptions.const_learning);
		choice_neighbor.setSelected(SOMOptions.const_neighborhood);
		
		//The the wrappers
		Double neigh_min, neigh_max, neigh_step, neigh_val;
		neigh_min = new Double(0.05);
		neigh_max = new Double(0.2);
		neigh_val = new Double(SOMOptions.neighborhood_som);
		neigh_step = new Double(0.01);
		
		wrapper_neighborhood_som = new JSpinSliderWrapper(neigh_min, neigh_max, neigh_val, neigh_step);
		wrapper_neighborhood_som.setEnabled(false);
		
		Double learn_min, learn_max, learn_val, learn_step;
		
		learn_min = new Double(0.05);
		learn_max = new Double(2);
		learn_val = new Double(SOMOptions.learning_som);
		learn_step = new Double(0.01);
		
		wrapper_learning_som = new JSpinSliderWrapper(learn_min, learn_max, learn_val, learn_step);
		wrapper_learning_som.setEnabled(false);
	}
	
	public void initializeDSOM(){
		
		Double ela_min, ela_max, ela_step, ela_val;
		ela_min = new Double(0.05);
		ela_max = new Double(5);
		ela_step = new Double(0.01);
		ela_val = new Double(DSOMOptions.elasticity);
		
		wrapper_elasticity = new JSpinSliderWrapper(ela_min, ela_max, ela_val, ela_step);
		wrapper_elasticity.setNotNull(true);
		
		Double epsi_min, epsi_max, epsi_step, epsi_val;
		
		epsi_min = new Double(0);
		epsi_max = new Double(1);
		epsi_step = new Double(0.1);
		epsi_val = new Double(DSOMOptions.epsilon);
		
		wrapper_epsilon = new JSpinSliderWrapper(epsi_min, epsi_max, epsi_val, epsi_step);
	}
	
	public void initializeGNG(){
		
		slider_nb_dep = getIntSlider(2, 10, 1, GNGOptions.nb_neurons_dep);
		
		field_max_age = new JFormattedTextField(new Integer(GNGOptions.max_age));
		field_max_neurons = new JFormattedTextField(new Integer(GNGOptions.max_neurons));
		field_time_new = new JFormattedTextField(new Integer(GNGOptions.time_new));
		
		NumberFormat nf = NumberFormat.getInstance();
		nf.setMaximumFractionDigits(10);
		
		field_variation_all = new JFormattedTextField(nf);
		field_variation_worst = new JFormattedTextField(nf);
		
		field_variation_all.setValue(new Double(GNGOptions.variation_for_all));
		field_variation_worst.setValue(new Double(GNGOptions.variation_for_worst));
		
		Double neigh_min, neigh_max, neigh_step, neigh_val;
		
		neigh_min = new Double(GNGOptions.neighborhood_gng / 10);
		neigh_max = new Double(GNGOptions.neighborhood_gng * 2);
		neigh_val = new Double(GNGOptions.neighborhood_gng);
		neigh_step = new Double(GNGOptions.neighborhood_gng / 10);
		
		wrapper_neighborhood_gng = new JSpinSliderWrapper(neigh_min, neigh_max, neigh_val, neigh_step);
		
		Double win_min, win_max, win_val, win_step;
		
		win_min = new Double(0.01);
		win_max = new Double(1);
		win_val = new Double(GNGOptions.winner_gng);
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
	public void setDisabled(boolean occurring_process){
		
		btn_step.setEnabled(occurring_process);
		btn_pause.setEnabled(occurring_process);
		btn_stop.setEnabled(occurring_process);
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
		
		switch(s){
			case "SOM":
				setSOMFrame();
			break;
			
			case "DSOM":
				setDSOMFrame();
			break;
				
			case "GNG":
				setGNGFrame();
			break;
		}
		
		JPanel control_buttons = new JPanel();
		control_buttons.add(btn_stop);
		control_buttons.add(btn_pause);
		control_buttons.add(btn_step);
		
		container.add(control_buttons);
		
		setContentPane(container);
		pack();
		setDisabled(false);
	}
	
	public void setAbstractNetworkFrame(){
		
		field_nb_data.setValue(new Integer(BasicOptions.nb_data));
		field_nb_epochs.setValue(new Integer(BasicOptions.nb_epochs));
		field_refresh.setValue(BasicOptions.refresh);
		field_time_to_wait.setValue(new Integer(BasicOptions.time_to_wait));
		
		add("Number of data : ", field_nb_data);
		add("Number of epochs : ", field_nb_epochs);
		add("Refresh : ", field_refresh);
		add("Time to wait (for the speed) : ", field_time_to_wait);
	}
	
	public void setAbstractMapFrame(){
		
		slider_width.setValue(MapOptions.cols);
		slider_height.setValue(MapOptions.rows);
		
		add("Width of the grid : ", slider_width);
		add("Height of the grid : ", slider_height);
	}
	
	public void setSOMFrame(){
		setAbstractMapFrame();
		
		choice_learning.setSelected(SOMOptions.const_learning);
		choice_neighbor.setSelected(SOMOptions.const_neighborhood);
		
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
		
		slider_nb_dep.setValue(GNGOptions.nb_neurons_dep);
		field_variation_all.setValue(new Double(GNGOptions.variation_for_all));
		field_variation_worst.setValue(new Double(GNGOptions.variation_for_worst));
		
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
		
		btn_stop.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				synchronized (holder.getWorld()) {
					
					BasicOptions.stopped.setBool(true);
					BasicOptions.paused = false;
					
					holder.getDraw().getArm2D().show_neurons = false;
					
					btn_pause.setText("Pause");
					holder.getWorld().notifyAll();
				}
			}
		});
		
		btn_pause.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				
				synchronized (holder.getWorld()) {
					
					if(BasicOptions.paused){
						BasicOptions.paused =false;
						btn_pause.setText("Pause");
					}else{
						BasicOptions.paused  =true;
						btn_pause.setText("Resume");
					}
					
					holder.getWorld().notifyAll();		
					
				}
			}
		});
		
		btn_step.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				
				BasicOptions.stepped = true;
				synchronized (holder.getWorld()) {
					holder.getWorld().notifyAll();
				}
				
			}
		});
		
		field_nb_data.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				BasicOptions.nb_data = (int) field_nb_data.getValue();
			}
		});
		
		field_nb_epochs.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				BasicOptions.nb_epochs = (int) field_nb_epochs.getValue();
			}
		});
		
		field_refresh.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				BasicOptions.refresh = (int)field_refresh.getValue(); 
			}
		});
		
		field_time_to_wait.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				
				int time = (int) field_time_to_wait.getValue();
				
				if(time < 0) time=0;
					BasicOptions.time_to_wait = time; 
			}
		});
	}
	
	public void addMapListeners(){
		
		slider_width.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent arg0) {
				MapOptions.cols = slider_width.getValue();
			}
		});
		
		slider_height.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				MapOptions.rows = slider_height.getValue();
			}
		});
	}
	
	public void addSOMListeners(){
		
		choice_learning.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				SOMOptions.const_learning = choice_learning.isSelected();
				wrapper_learning_som.setEnabled(choice_learning.isSelected());
			}
		});
		
		choice_neighbor.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				SOMOptions.const_neighborhood = choice_neighbor.isSelected();
				wrapper_neighborhood_som.setEnabled(choice_neighbor.isSelected());
			}
		});
		
		wrapper_neighborhood_som.addChangeListener(new ChangeListener(){
			@Override
			public void stateChanged(ChangeEvent e) {
				SOMOptions.neighborhood_som = ((JSpinSlider)e.getSource()).getValue();
			}
		});
		
		wrapper_learning_som.addChangeListener(new ChangeListener(){
			@Override
			public void stateChanged(ChangeEvent e) {
				SOMOptions.learning_som = ((JSpinSlider)e.getSource()).getValue();
			}
		});
	}
	
	public void addDSOMListeners(){
		
		wrapper_elasticity.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				DSOMOptions.elasticity = ((JSpinSlider)e.getSource()).getValue();
			}
		});
		
		wrapper_epsilon.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				DSOMOptions.epsilon = ((JSpinSlider)e.getSource()).getValue();
			}
		});
	}
	
	public void addGNGListeners(){
		
		slider_nb_dep.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				GNGOptions.nb_neurons_dep = slider_nb_dep.getValue();
			}
		});
		
		field_max_age.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				GNGOptions.max_age = (int) field_max_age.getValue();
			}
		});
		
		field_max_neurons.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				GNGOptions.max_neurons = (int) field_max_neurons.getValue();
			}
		});
		
		field_time_new.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				GNGOptions.time_new = (int) field_time_new.getValue();
			}
		});
		
		field_variation_all.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				GNGOptions.variation_for_all = (double) field_variation_all.getValue();
			}
		});
		
		field_variation_worst.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				GNGOptions.variation_for_worst = (double) field_variation_worst.getValue();
			}
		});
		
		wrapper_neighborhood_gng.addChangeListener(new ChangeListener(){
			@Override
			public void stateChanged(ChangeEvent e) {
				GNGOptions.neighborhood_gng = ((JSpinSlider)e.getSource()).getValue();
			}
		});
		
		wrapper_winner_gng.addChangeListener(new ChangeListener(){
			@Override
			public void stateChanged(ChangeEvent e) {
				GNGOptions.winner_gng = ((JSpinSlider)e.getSource()).getValue();
			}
		});
	}

	@Override
	public void update(Observable o, Object arg) {
		switcher(holder.getWorld().getNet().toString());
		setDisabled(!BasicOptions.stopped.bool());
	}
}
