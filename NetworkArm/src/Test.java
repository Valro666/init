

import view.arm.ExperienceViewer;

import java.awt.BorderLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

import javax.swing.JFrame;

import Jama.Matrix;
import model.arm.CommandSequence;
import model.arm.CompleteArm;
import model.arm.Consignes;

public class Test {
	
	/** The complete arm simulated */
	CompleteArm _arm;
	
	/** Array of CommandSequence */
	Consignes _consignes;
	
	/** Simulation time */
	double _t = 0.0;
	
	/** Simulation DeltaT */
	double _dt = 0.05;
	
	/** Simulation _maxTime */
	double _maxTime = 100*_dt;
	
	/** Commands for Muscles */
	Matrix _u = new Matrix(1, 6, 0.0);
	
	/** Viewer for the Experience */
	ExperienceViewer _xpPanel;
	
	public Test(){

		_arm = new CompleteArm();
		int nbConsigne = _arm.getArrayNeuroControlers().length;
		_consignes = new Consignes(nbConsigne);
		
		buildGUI();
		
		_xpPanel.repaint();
		_dt = 0.05;
	}
	
	public void run() throws InterruptedException{
		Matrix previous_pos = new Matrix(0, 0);
		Matrix current_pos = _arm.getArm().getArmPos();
		
		while(!compareMatrix(current_pos, previous_pos) && _t < _maxTime){
			step(_dt);
			Thread.sleep(10);
			_xpPanel.repaint();
			
			previous_pos = current_pos;
			current_pos = _arm.getArm().getArmPos();

		}
	}
	
	/**
	 * Execute 
	 */
	public void exec(Matrix m, double dt){
		step(m, dt);
	}
	
	public static boolean compareMatrix(Matrix one, Matrix two){
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
	
	private void step(double dt) {
		
		for (int i = 0; i < _consignes.size(); i++) {
			
			CommandSequence cs = _consignes.get(i);
			// la valeur de la consigne est copiée dans le vecteur u
			_u.set(0,i, cs.getValAtTimeFocussed(_t));
			
		}
		
		step(_u, dt);
		
	}
	
	private void step(Matrix matrix, double dt){
		// Applique les consignes sur le bras
		_arm.applyCommand(_u, dt);
		
		_t += dt;
		
		_xpPanel.update();
	}
	
	private void buildGUI() {
		// Setup window
		JFrame frame = new JFrame("Human Arm");
		frame.setSize(500,500);
		frame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
		frame.setLayout(new BorderLayout());

		//_xpPanel = new JExperienceLess(_arm , _consignes);
		frame.add(_xpPanel, BorderLayout.CENTER);
		frame.setVisible(true);
	}


	/**
	 * Read the 6 CommandSequence from a file.
	 * @param fileName Name of the file
	 * @throws IOException
	 */
	public void readCommandSequences(String fileName) throws IOException{
		_consignes.read(fileName);
	}
	
	/**
	 * @param args
	 * @throws IOException 
	 * @throws InterruptedException 
	 */
	@SuppressWarnings("resource")
	public static void main(String[] args) throws IOException, InterruptedException {
		
		try{
			if(args.length == 0)
				throw new Exception("Il faut passer des fichiers en arguments ! ");
			
			if(args[0].equals("-d")){
				
				if(args.length < 2)
					throw new Exception("Usage : -d + Directory");
				
				Test app = new Test(); 
				
				for(int i=1; i<args.length; i++){
					
					File directory = new File(args[i]);
					
					if(!directory.isDirectory())
						throw new Exception("Usage : -d + Directory");
					
					for(int j=0; j<directory.listFiles().length; j++){
						
						File file = directory.listFiles()[j];
						
						System.out.println("Appuyez sur une touche pour la simulation de :\n"+file.getName());
						(new Scanner(System.in)).nextLine();
						app.reset(0, 0);

						app.readCommandSequences(file.getAbsolutePath());
						app.run();
						
					}
				}
			}else{
				for(int i=0; i<args.length; i++){
					Test app = new Test();
					app.readCommandSequences(args[i]);
					app.run();
					
					System.out.println("Appuyez sur une touche pour passer à la simulation suivante :");
					(new Scanner(System.in)).nextLine();
				}
			}
			
			System.out.println("Fin des simulations");
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void reset( double degAng0, double degAng1) {
		// Setup in resting position
		_arm.setup(Math.toRadians(degAng0), Math.toRadians(degAng1));
		
		// Un vecteur (Matrix 1x6) de consignes musculaires, initialisée à 0.0.
		for (int i = 0; i < 6; i++) {
			_u.set(0,i, 0.0);
		}
		
		_t = 0.0;
		
		_xpPanel.reset();
	}

	public void runLogged(String fileName, double degAng0, double degAng1) throws IOException {
		// open a file
		FileWriter file = new FileWriter( fileName );
		BufferedWriter bw = new BufferedWriter(file);

		bw.write( "# temps; Muscles: 6x(consigne,act,tension); Articulation: 2x(couple,angle,vitesse,x,y) ");
		bw.newLine();
		
		while( _t < _maxTime ) {
			step( _dt );

			// Ecrit dans fichier
			bw.write(Double.toString(_t));
			// Pour chaque muscle
			Matrix act = _arm.getMuscleActivation();
			Matrix tau = _arm.getMuscles().getTension();
			for (int i = 0; i < _consignes.size(); i++) {
				bw.write("\t"+Double.toString(_u.get(0, i))
						+"\t"+Double.toString(act.get(0, i))
						+"\t"+Double.toString(tau.get(0, i)));
			}
			// Pour chaque articulation
			Matrix cpl = _arm.getMuscles().getTorque();
			Matrix ang = _arm.getArm().getArmPos();
			Matrix spd = _arm.getArm().getArmSpeed();
			double[] x = _arm.getArm().getArmX();
			double[] y = _arm.getArm().getArmY();
			for (int i = 0; i < 2; i++) {
				bw.write("\t"+Double.toString(cpl.get(0, i))
						+"\t"+Double.toString(ang.get(0, i))
						+"\t"+Double.toString(spd.get(0, i))
						+"\t"+Double.toString(x[i])
						+"\t"+Double.toString(y[i]));
			}
			bw.newLine();
		}
		bw.close();
		file.close();
	}
}
