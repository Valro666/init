package src.model.arm;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Observable;

import javax.vecmath.Point3d;

import src.utils.JamaU;
import Jama.Matrix;

/**
// * Bras complet composé de 2 articulations et de 6 muscles. Ce modèle est utilisé
// * dans [Li, 06], réutilisé dans [Kaladjian, 98] mais aussi [Marin, 11].
// * Les 2 angle sont :
// * 0 : angle d'épaule
// * 1 : angle du coude
// * 
// * Les 6 muscles sont, dans l'ordre :
// * 0 : biceps court => flexion du coude. 
// * 1 : Triceps latéral => extension du coude.
// * 2 : Deltoid anterieur => flexion de l'épaule
// * 3 : Deltoid posterieur => extension de l'épaule
// * 4 : biceps long => flexion (épaule+coude)
// * 5 : triceps long => extension (épaule+coude)
// * 
// * L'utilisation typique est :
// * 1) création
// * 2) initialisation 'setup(ang0, ang1)'
// * 3) appliquer une consigne neuronale (dans [0,1]) pour chaque muscle
// * pendant dt : 'applyCommand( Matrix comm, double dt )'
// * 4) (afficher ou autre)
// * 5) goto 3)
// * 
// * TODO Les données numériques sont codées en dur dans les différentes Classes (c'est mal).
// * 
// * @author Alain.Dutech@loria.fr
// */

public class CompleteArm extends Observable {
	
	private String last_command;
	
	/** Commande neuronale => activation du muscle */
	NeuroControl [] _nc = new NeuroControl[6];
	
	/** 1 x nb_muscles : Matrix of muscles activation */
	Matrix _act = new Matrix(1, 6);
	
	/** Activation + muscle => couple de moments sur bras */
	SimpleMuscle _muscles = new SimpleMuscle();
	
	/** couple sur bras => nouvel état bras */
	Arm _arm;
	
	/** Decimal formating */
	DecimalFormat df5_3 = new DecimalFormat( "00.000" );
	
	/** 
	 * Creation : pas d'initialisation particulière.
	 */
	public CompleteArm() {
		for (int i = 0; i < 6; i++) {
			_nc[i] = new NeuroControl();
		}
		
		_arm = new Arm();
		
		setup(0,0);
	}
	
	/**
	 * Met le bras dans une position donnée, au repos (pas de vitesse,
	 * de consigne neuronale ou d'activation des muscles).
	 * 
	 * TODO Faudrait vérifer que les angles sont valides.
	 * 
	 * @param ang0 angle (radians) de la première articulation
	 * @param ang1 angle (radians) de la seconde articulation
	 */
	public void setup(double ang0, double ang1){
		// setup arm with no angular speed
		double [] ang = {ang0,ang1};
		_arm.setArmPos( ang );
		_arm.setArmSpeed(new Matrix(1,2)); // Matrix at zero by default
		
		// setup muscles length
		_muscles.computeTorque(new Matrix(1,6), _arm.getArmPos(), _arm.getArmSpeed());
		
		// No neural activity
		for (int i = 0; i < _nc.length; i++) {
			_nc[i].setAct(0.0);
			_nc[i].setU(0.0);
		}
		
		last_command = "RESET";
		
		// Observable
		setChanged();
		notifyObservers();
		
	}
	
	/**
	 * Applique un couple (en N.m) à chaque articulation pendant dt.
	 * ATTENTION : les muscles et les commandes neuronales ne sont plus valides après.
	 * 
	 * @param torque Matrix(1xnb_articulation) dans [-inf,+inf]
	 * @param dt intervalle de temps en secondes.
	 * @return Positions des articulations et de l'extrémité du bras après dt.
	 */
	public ArrayList<Point3d> applyTorque( Matrix torque, double dt ) {
		_arm.applyTension( torque, dt );
		// Observable

		return _arm.getArmPoints();
	}
	/**
	 * Applique une activation (dans [0,1]) sur les muscles pendant dt.
	 * ATTENTION : les commandes neuronales ne seront plus valides.
	 *  
	 * @param act Matrix(1xnb_muscles) dans [0,1]
	 * @param dt temps en secondes
	 * @return Positions des articulations et de l'extrémité du bras après dt.
	 */
	public ArrayList<Point3d> applyActivation( Matrix act, double dt ) {
		assert checkBounds(act) : "activation out of bounds [0:1]";
		_muscles.computeTorque(act, _arm.getArmPos(), _arm.getArmSpeed());
		
		// Observable

		return applyTorque(_muscles.getTorque(), dt);
	}

	/**
	 * Verify if the values of a Matrix are in the bounds [0:1].
	 *
	 * @param act the Matrix to check
	 * @return true if the values Matrix in [0:1], false otherwise.
	 */
	private boolean checkBounds(Matrix act) {
		for (int i=0; i < act.getRowDimension(); i++) {
			for (int j = 0; j < act.getColumnDimension(); j++) {
				double val = act.get(i, j);
				if (val < 0.0 || val > 1.0) {
					return false;
				}
			}
		}
		return true;
	}

	/**
	 * Applique une nouvelle consigne neuronale (dans [0,1]), pour chaque muscle.
	 * Cette consigne est appliquée pendant dt.
	 * 
	 * @param comm Matrix(1xnb_muscles) dans [0,1]
	 * @param dt intervalle de temps en secondes
	 * @return Positions des articulations et de l'extrémité du bras après dt.
	 */
	public ArrayList<Point3d> applyCommand(Matrix comm, double dt) {
		
		for (int i = 0; i < _nc.length; i++) {
			_nc[i].applyCommand(comm.get(0,i), dt);
			_act.set(0, i, _nc[i].getAct());
		}
		
		//Observable
		setChanged();
		notifyObservers();
		return applyActivation(_act, dt);
	}
	
	/**
	 * Apply a command on the arm until it arrives to its destination or if the max_time is outdated.
	 * @param comm The command we want to apply.
	 * @param max_time The maximum time the command is applied.
	 * @param dt The duration the command occured in one loop.
	 */
	public void apply(Matrix comm, double max_time, double dt){
		
		Matrix previous_pos = new Matrix(0, 0);
		Matrix current_pos = _arm.getArmPos();
		
		last_command = matrixToString(comm);
		
		double elapsed_time = 0.0;
		
		while(!compareMatrix(current_pos, previous_pos) && elapsed_time < max_time){
			
			applyCommand(comm, dt);
			elapsed_time+=dt;
			
			try {
				Thread.sleep(10);
			} catch (InterruptedException e){
				e.printStackTrace();
			}
			
			previous_pos = current_pos;
			current_pos = _arm.getArmPos();
			
		}
		
	}
	
	public static String matrixToString(Matrix command){

		String s_command="(";
		
		for(int i=0; i<command.getRowDimension(); i++){
			
			for(int j=0; j<command.getColumnDimension(); j++){
				
				s_command+=String.format("%.5f", command.get(i, j));
				
				if(j<command.getColumnDimension() - 1)
					s_command+=", ";
			}
			
			if(i<command.getRowDimension() - 1)
				s_command+="\n";

		}
		
		s_command+=")";
		
		return s_command;
	}
	
	/**
	 * Compare two matrix.
	 * @param one The first matrix.
	 * @param two The second matrix;
	 * @return True if they are the same, false otherwise. 
	 */
	private static boolean compareMatrix(Matrix one, Matrix two){
		
		boolean eq = true;
		double [][] array_one = one.getArray();
		double [][] array_two = two.getArray();
		
		if(array_one.length != array_two.length)
			eq = false;
		
		for (int i = 0; i < array_one.length && eq; i++) {
			
			if(array_one[i].length != array_two[i].length)
				eq = false;
			
			for (int j = 0; j < array_one[i].length && eq; j++) {
				if(array_one[i][j] != array_two[i][j])
					eq = false;
			}
		}
		
		return eq;
	}
	
	
	/** Les infos importantes sur le bras.
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		String str = "** CompleteArm ************************************\n";
		// activation
		str += "  act= ";
		for (int i = 0; i < _nc.length; i++) {
			str += df5_3.format(_nc[i].getAct())+", ";
		}
		// tension des muscles
		str += "\n  tau= "+JamaU.vecToString(_muscles.getTension());
		// Couples sur articulations
		str += "\n  cpl= "+JamaU.vecToString(_muscles.getTorque());
		// Bras
		str += "\n  ang= "+JamaU.vecToString(_arm.getArmPos());
		str += "\n  spd= "+JamaU.vecToString(_arm.getArmSpeed());
		str += "\n";
		
		return str;
	}

	/**
	 * Get the array of Neurocontrolers
	 * @return _nc
	 */
	public NeuroControl[] getArrayNeuroControlers() {
		return _nc;
	}

	/**
	 * Get the Matrix of activation.
	 * @return _act (synchronized with _nc.getAct().
	 */
	public Matrix getMuscleActivation() {
		for (int i = 0; i < _nc.length; i++) {
			_act.set(0, i, _nc[i].getAct());
		}
		return _act;
	}
	/**
	 * Get the Muscles.
	 * @return _muscles
	 */
	public SimpleMuscle getMuscles() {
		return _muscles;
	}
	/**
	 * Get the Arm
	 * @return _arm
	 */
	public Arm getArm() {
		return _arm;
	}	
	
	public String getLastComm(){
		return last_command;
	}
	
	public String getActivationString(){
		return matrixToString(_act);
	}
	
	public Matrix getActivation(){
		return _act;
	}
}
