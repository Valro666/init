package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
/**
 * A customized component that holds a spinner and a slider.
 * It deals with a min, a max and a step.
 * 
 * @author valbert
 *
 */

@SuppressWarnings("serial")
public class JSpinSlider extends JPanel{
	private JDoubleSpin spin;
	private JColorSlider slider;
	private Double minimum, maximum, value;
	private ChangeListener listener;
	
	public JSpinSlider(int width, int height, Double min, Double max, Double v, Double step){
		super();
		
		minimum = min;
		maximum = max;
		value = v;
		
		//We begin by creating a customized JSpinner that accept just integers.
		spin = new JDoubleSpin((int)(width*0.3), height,min, max, value, step);
		slider = new JColorSlider((int)(width*0.7), height, value/max);
		
		setLayout(new BorderLayout());
		
		this.add(slider, BorderLayout.WEST);
		this.add(spin, BorderLayout.EAST);
		
		activate();
		
		setPreferredSize(new Dimension(width, height));
	}
	
	public JSpinSlider(Double min, Double max, Double value, Double step){
		this(300, 50, min, max, value, step);
	}
	
	@Override
	public void setEnabled(boolean enabled){
		spin.setEnabled(enabled);
		slider.setEnabled(enabled);
		
		if(!enabled){
			slider.removeMouseListener(slider.getMouseListeners()[0]);
			slider.removeMouseMotionListener(slider.getMouseMotionListeners()[0]);
		}else{
			activateSlider();
		}
	}
	
	public Double getValue(){
		return value;
	}
	
	public void activateSpin(){
		spin.getModel().addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {

				setValue((Double)spin.getModel().getValue());
			}
		});
	}
	
	public void activateSlider(){
		slider.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				//We convert the graphical value into the real value
				/*
				 * We use this formula : value = position * gap / width
				 * for normalization
				 */
				Double gap = maximum - minimum; //The interval between min and max
				Double conversion=new Double(arg0.getX())*gap/new Double(slider.getWidth());
				
				setValue(conversion);
			}
		});
		
		slider.addMouseMotionListener(new MouseMotionAdapter() {
			@Override
			public void mouseDragged(MouseEvent arg0) {
				//We convert the graphical value into the real value
				/*
				 * We use this formula : value = position * gap / width
				 * for normalization
				 */
				Double gap = maximum-minimum; //The interval between min and max
				Double conversion=new Double(arg0.getX())*gap/(new Double(slider.getWidth()));
				
				if(arg0.getX() >= 0 && arg0.getX() <= slider.getWidth())
					setValue(conversion);
			}
		});
	}
	
	public void activate(){
		//We add listeners
		activateSlider();
		activateSpin();
	}
	
	public void addChangeListener(ChangeListener cl){
		listener = cl;
	}
	
	/**
	 * Set the new value, or ignore it if it doesn't respect bounds
	 * @param v The value
	 */
	public void setValue(Double v){
		if(v.compareTo(minimum)>=0 && v.compareTo(maximum)<=0)
			value = v;
		else if(v.compareTo(minimum)<=0)
			value = minimum;
		else if(v.compareTo(maximum)>=0)
			value = maximum;
		repaint();
		
		listener.stateChanged(new ChangeEvent(this));
	}
	
	@Override
	public void paintComponent(Graphics g){
		
		//We convert real the value into graphical coordinates
		/*
		 * We use this formula : position = value / gap * width
		 * for normalization
		 */
		Double gap = maximum-minimum; //The interval between min and max
		Double conversion = value/gap*new Double(slider.getWidth());
		slider.setValue(conversion);

		spin.setValue(value);
		
		spin.repaint();
		slider.repaint();
	}
	
	public void setColor(Color c){
		slider.setColor(c);
		repaint();
	}
	
	public Color getColor(){
		return slider.getColor();
	}
}

