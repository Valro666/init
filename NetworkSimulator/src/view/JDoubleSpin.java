package view;

import java.awt.Dimension;
import javax.swing.JFormattedTextField;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.text.NumberFormatter;

/**
 * This class represents a JSpinner that holds values between two bounds with a defined step 
 * and that prevents wrong values in the editor.
 * 
 * @author valbert
 *
 */

@SuppressWarnings("serial")
class JDoubleSpin extends JSpinner{
	/**
	 * @param width The width of the spin
	 * @param height The height of the spin
	 * @param min The min bound of the spin
	 * @param max The max bound of the spin
	 * @param value The first value of the spin
	 * @param step The step of the spin
	 */
	public JDoubleSpin(int width, int height, Double min, Double max, Double value, Double step){
		super();
		
		setPreferredSize(new Dimension(width, height));
		
		//We change the model of the spin (for arrows)
		setModel(new SpinnerNumberModel(value,min,max,step));
		
		//We change the model of the text editor 
		setEditor(new JSpinner.NumberEditor(this, getStringModel(min, max, step)));
		
		//We forbid wrong values (you don't say ^^')
		JFormattedTextField txt = ((JSpinner.NumberEditor) getEditor()).getTextField();
		((NumberFormatter) txt.getFormatter()).setAllowsInvalid(false);
	}
	
	/**
	 * A spin constructor. Heigth and width are predefined :
	 * 		w -> 50px
	 * 		h -> 50px
	 * 
	 * @param min The min bound of the spin
	 * @param max The max bound of the spin
	 * @param value The first value of the spin
	 * @param step The step of the spin
	 */
	public JDoubleSpin(Double min, Double max, Double value, Double step){
		this(50, 50, min, max, value, step);
	}
	
	public void setValue(Double v){
		getModel().setValue(v);
		((JSpinner.NumberEditor)getEditor()).getTextField().setText(new String(v+""));
	}
	
	/**
	 * A method that compute the string corresponding to the model we need for the editor
	 * @param max The maximal bound of the spin
	 * @param step The step of the spin 
	 * @return The string corresponding to the model
	 */
	public String getStringModel(Double min, Double max, Double step){
		//We create the model of the spinner (values that he can take)
		String model = "";
		
		/*We compute the max of values that there can be in the editor.
		  We just verify the integer part, the decimal part will be
		  compute with the step*/
		String s_max = max.toString();
		String[] split_max = s_max.split("\\.");
		for(int i=0; i<split_max[0].length(); i++)
			model = model.concat("#");
		
		//Then we compute the number of decimals that there can be
		int p_min = getPrecision(min);
		int p_max = getPrecision(max);
		int p_step = getPrecision(step);
		
		//We watch which has the more decimals
		int nb_dec=0;

		if(p_min>nb_dec)
			nb_dec = p_min;
		
		if(p_max>nb_dec)
			nb_dec = p_max;
		
		if(p_step>nb_dec)
			nb_dec = p_step;
		
		//We verify that there are decimals
		if(nb_dec>0){
			//Then we compute the number of decimals
			model = model.concat(".");
			for(int i=0; i<nb_dec; i++)
				model = model.concat("#");
		}
		
		return model;
	}
	
	private int getPrecision(Double v){
		int p=0;
		
		String s_v = v.toString();
		
		/*We check the representation of the double
		 * (0.005 or 1.0E-5)
		 */
		if(s_v.contains("E")){
			
			String[] split_v = s_v.split("E");
			p = -(new Integer(split_v[1]));
			
		}else{
			
			String[] split_v = s_v.split("\\.");
			
			if(split_v.length > 1)
				p = split_v[1].length();
			
		}
		
		return p;
	}
}
