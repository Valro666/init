package view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.NumberFormat;

import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

@SuppressWarnings("serial")
public class JSpinSliderWrapper extends JPanel{
	private JFormattedTextField field_min, field_max, field_step;
	private int width, height;
	private JSpinSlider spin_slider;
	private JButton go;
	private ChangeListener listener;
	private boolean not_null;
	
	public JSpinSliderWrapper(int w, int h, Double begin, Double end, Double val,Double step){
		width = w;
		height = h;

		spin_slider = new JSpinSlider(width, (int)(height*0.6), begin,end, val, step);
		
		NumberFormat nf = NumberFormat.getInstance();
		nf.setMaximumFractionDigits(10);
		nf.setMaximumIntegerDigits(2);
		nf.setMinimumFractionDigits(1);
		nf.setMinimumIntegerDigits(1);
		
		field_min = new JFormattedTextField(nf);
		field_max = new JFormattedTextField(nf);
		field_step = new JFormattedTextField(nf);
		
		field_min.setValue(begin);
		field_max.setValue(end);
		field_step.setValue(step);
		
		go = new JButton("Set");
		
		setPreferredSize(new Dimension(width, height));
		
		//We place all elements
		setElements();
		
		//We set a listener
		go.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//We verify that there are no errors
				if(verify()){
					//In that case, we create a new spin_slider with those values
					recreate();
				}
			}
		});
	}
	
	public JSpinSliderWrapper(Double begin, Double end, Double val,Double step){
		this(500, 40, begin, end, val, step);
	}
	
	public void addChangeListener(ChangeListener cl){
		listener = cl;
		spin_slider.addChangeListener(cl);
	}
	
	@Override
	public void setEnabled(boolean enabled){
		go.setEnabled(enabled);
		field_max.setEnabled(enabled);
		field_min.setEnabled(enabled);
		field_step.setEnabled(enabled);
		spin_slider.setEnabled(enabled);
	}
	
	/**
	 * Set all elements of the widget
	 */
	public void setElements(){
		
		JPanel fields = new JPanel();
		fields.setPreferredSize(new Dimension(width, (int)(height*0.4)));
		fields.setLayout(new GridLayout(1, 7));
		
		fields.add(new JLabel("Min: "));
		fields.add(field_min);
		fields.add(new JLabel("Max: "));
		fields.add(field_max);
		fields.add(new JLabel("Step: "));
		fields.add(field_step);
		fields.add(go);
		
		setLayout(new BorderLayout());
		add(fields, BorderLayout.NORTH);
		add(spin_slider, BorderLayout.SOUTH);
	}
	
	public void recreate(){
		
		//We get former values
		Double min = null;
		Double max = null;
		Double step = null;
		Double value = spin_slider.getValue();
		
		/*	We are obliged to verify the instance returned by each field: 
		 * 	If we put 2 or 3.0 in the field, it returns a Long and not a Double
		 */
		if(field_min.getValue() instanceof Double)
			min = (Double)field_min.getValue();
		else
			min = new Double((Long)field_min.getValue());
		
		if(field_max.getValue() instanceof Double)
			max = (Double)field_max.getValue();
		else
			max = new Double((Long)field_max.getValue());
		
		if(field_step.getValue() instanceof Double)
			step = (Double)field_step.getValue();
		else
			step = new Double((Long)field_step.getValue());
		

		remove(spin_slider);
		
		//We verify if we can reuse former value or if we have to set a new one
		if(verifyVal(value, min, max))
			spin_slider = new JSpinSlider(width, (int)(height*0.6), min, max, value, step);
		else{
			System.out.println((max-min)/(new Double(2)));
			spin_slider = new JSpinSlider(width, (int)(height*0.6), min, max, max - (max-min)/(new Double(2)), step);
		}
		
		spin_slider.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				spin_slider.addChangeListener(listener);
			}
		});
		
		add(spin_slider, BorderLayout.SOUTH);
		revalidate();
	}
	
	@Override
	public void paintComponent(Graphics g){
		super.repaint();
		
		spin_slider.repaint();
		field_max.repaint();
		field_min.repaint();
		field_step.repaint();
		
	}
	
	public boolean verifyVal(Double val, Double min, Double max){
		return  (val.compareTo(min)>=0) && (val.compareTo(max)<=0);
	}
	
	public boolean verify(){
		//We verify that no value are at 0
		String errors = "";
		
		Double min=null;
		Double max=null;
		Double step=null;
		
		/*	We are obliged to verify the instance returned by each field: 
		 * 	If we put 2 or 3.0 in the field, it returns a Long and not a Double
		 */
		if(field_min.getValue() instanceof Double)
			min = (Double)field_min.getValue();
		else
			min = new Double((Long)field_min.getValue());
		
		if(field_max.getValue() instanceof Double)
			max = (Double)field_max.getValue();
		else
			max = new Double((Long)field_max.getValue());
		
		if(field_step.getValue() instanceof Double)
			step = (Double)field_step.getValue();
		else
			step = new Double((Long)field_step.getValue());
		
		//Then we verify if values are correct
		
		if(not_null){
			if(min.compareTo(new Double(0)) == 0)
				errors = errors.concat("Min can't be 0 ! \n");
			
			if(max.compareTo(new Double(0)) == 0)
				errors = errors.concat("Max can't be 0 ! \n");
		}
		
		if(step.compareTo(new Double(0)) == 0)
			errors = errors.concat("Step can't be 0 ! \n");
			
		if(min.compareTo(max) >= 0)
			errors = errors.concat("Min can't be greater or equals to max !\n");
		
		if(step.compareTo(max)>=0)
			errors = errors.concat("Step can't be greater or equals to max !\n");
		
		if(!errors.equals(""))
			JOptionPane.showConfirmDialog(this, errors, "Erreur !", 0, JOptionPane.ERROR_MESSAGE);
		
		return errors.equals("");
	}
	
	public void setNotNull(boolean n_n){
		not_null = n_n;
	}
}
