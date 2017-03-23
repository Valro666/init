package view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JPanel;

/**
 * 
 * A slider with a color that deals with a position
 * @author valbert
 *
 */

@SuppressWarnings("serial")
public class JColorSlider extends JPanel{
	private Color color_slider;
	private Color color_background;
	private Double value;
	
	public JColorSlider(int width, int height, Double value_dep){
		color_slider = new Color(86, 168, 222);
		color_background = new Color(color_slider.getRed(), color_slider.getGreen(), color_slider.getBlue(), 120);
		value = value_dep;
		setPreferredSize(new Dimension(width, height));
	}
	
	@Override 
	public void setEnabled(boolean enabled){
		if(enabled)
			setColor(new Color(86, 168, 222));
		else
			setColor(Color.lightGray);
	}
	
	@Override
	public void paintComponent(Graphics g){
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, getWidth(), getHeight());
		
		g.setColor(color_background);
		g.fillRect(0, 0, getWidth(), getHeight());
		
		g.setColor(color_slider);
		g.fillRect(0, 0, value.intValue(), getHeight());
	}
	
	public Double getValue(){
		return value;
	}
	
	public void setValue(Double v){
		value = v;
	}
	
	public void setColor(Color c){
		color_slider = c;
		color_background = new Color(color_slider.getRed(), color_slider.getGreen(), color_slider.getBlue(), 120);
		repaint();
	}
	
	public Color getColor(){
		return color_slider;
	}
}
