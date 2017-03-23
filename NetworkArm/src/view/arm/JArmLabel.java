/**
 * 
 */
package view.arm;

import java.util.Observable;
import java.util.Observer;

import javax.swing.JLabel;

import model.arm.Arm;
import utils.JamaU;

/**
 * Un JLabel qui affiche la position et la vitesse du bras.
 * 
 * @author alain.dutech@loria.fr
 *
 */
@SuppressWarnings("serial")
public class JArmLabel extends JLabel implements Observer {

	/** The model to display */
	Arm _arm;
	
	/**
	 * Construction avec un model (Arm).
	 */
	public JArmLabel(Arm model) {
		super();
		_arm = model;
		setText(getDisplayString());
	}

	/** 
	 * Pos et Speed.
	 */
	private String getDisplayString() {
		String str = "";
		// state
		str += "Pos="+JamaU.vecToString(_arm.getArmPos());
		str +="  Spd="+JamaU.vecToString(_arm.getArmSpeed())+"\n";
		str +="pos cart. : ("+_arm.getArmEndPointX()+", "+_arm.getArmEndPointY()+")";
		return str;
	}

	public void update(Observable model, Object o) {
		setText(getDisplayString());
	}
}
