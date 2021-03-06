package src.model;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Random;

import Jama.Matrix;
import src.model.arm.CompleteArm;
import src.model.arm.FreeArm;
import src.model.network.AbstractNetwork;
import src.model.network.DSOM;
import src.model.network.Data;
import src.model.network.DataPoint;
import src.model.network.NetworkFactory;
import src.model.network.Neuron;
import src.model.network.SOM;
import src.model.options.BasicOptions;
import src.model.options.GraphOptions;

/**
 * 
 * This class will linked the environment for commands and the environment for
 * the arm positions.
 * 
 * @author valbert
 *
 */
public class LinkedEnvironment extends Observable implements Runnable {

	Random r = new Random();

	/**
	 * All data of the world. We admit that : - first env. is for position. -
	 * second env. is for commands. - third env. (if exists) is for move.
	 */
	private Data[] data;

	/**
	 * The priority for each set of data when we choose a neuron.
	 */
	private double[] data_priority;

	/**
	 * The network that evolves in this world.
	 */
	private AbstractNetwork network;

	/**
	 * The arm that moves depending on the network.
	 */
	private CompleteArm arm;

	/**
	 * Dimensions of environments.
	 */
	private int size;

	/**
	 * The errors between the position of the arm and the position of the
	 * neuron.
	 */
	private double error;
	
	private double lambda = 0.90;
	private double sumoferrors = 0;
	private double previous_errors[];
	private int next_index_error = 0;
	
	private static int speed = 1;

	/**
	 * The errors between the position of the arm and the barycenter the winner
	 * neurons.
	 */
	private double error_approx;

	/**
	 * The number of the current example
	 */
	private int examples;

	public FreeArm free;
	
	public static FreeArm goal;
	
	public static ArrayList<Neuron> vainqueurs;
	
	public static Neuron barycenter;
	
	public static boolean modedefonctionnement = true;

	/**
	 * Link different environments with the same network. (Example :
	 * LinkedEnvironment(2, 1, 5) )
	 * 
	 * @param width
	 *            The width of environments.
	 * @param length
	 *            The length of environments.
	 * @param nb_environments
	 *            The number of environments we want to link.
	 * @param dimensions_each_enrir
	 *            The dimension of each environment.
	 */

	public static LinkedEnvironment env;

	public LinkedEnvironment(int... dimensions_each_data) throws Exception {

		size = 600;

		data = new Data[dimensions_each_data.length];
		data_priority = new double[dimensions_each_data.length];

		// We set a priority to set all data-set at the same state.
		if (dimensions_each_data.length == 2) {
			data_priority[0] = 6 / 8.0;
			data_priority[1] = 2 / 8.0;
		} else if (dimensions_each_data.length == 3) {
			data_priority[0] = 3 / 7.0;
			data_priority[1] = 1 / 7.0;
			data_priority[2] = 3 / 7.0;
		} else
			throw new Exception("Not a good number of data-set !");

		int nb_dim = 0;

		for (int i = 0; i < dimensions_each_data.length; i++) {

			nb_dim += dimensions_each_data[i];
			data[i] = new Data(dimensions_each_data[i]);

		}
                /*
		// We decrease the value of commands
		for (DataPoint dp : data[1].getDataPoints()) {
			for (int i = 0; i < dp.getWeights().size(); i++) {
				dp.setWeight(i, new Float(dp.getWeight(i) * 0.1));
			}
		}
                */
		env = this;
		// We create the common neural network
		network = new DSOM(nb_dim);

		// We create the arm we will move
		// arm = new CompleteArm();
		free = new FreeArm();
		
		previous_errors = new double[100];

	}

	@Override
	public void run() {

		examples = 0;

		BasicOptions.stopped.setBool(false);
		// change();
		for (ArrayList<Neuron> ln : network.getNeurons()){
			for (Neuron n : ln) {
				ArrayList<Float> weights = n.getWeights();
				//FreeArm test = new FreeArm((int)(weights.get(2)*360),(int)(weights.get(3)*360),150,150);
				weights.set(0, 0.5f);
				weights.set(1, 0.5f);
				n.setWeights(weights);
			}
		}

		while (!BasicOptions.stopped.bool()) {
			// checkPause();

			step();
			// change();
			// examples++;

		}

		// We reset the position of the arm
		// arm.setup(0, 0);
		// free.setup(90, 90);

		BasicOptions.stopped.setBool(true);
		change();

	}

	public void step() {
                
                /*
                for(int i=0; i<network.getNeurons().size(); i++){
                    System.out.println ("neuron : "+network.getNeurons().get(i).get(i).getWeight(0)+" , "+network.getNeurons().get(i).get(i).getWeight(1));
                }
                */
		/*
		 * We choose a command in data.
		 */
		
                /*
                int hazard_index = (int) (Math.random() * data[1].size());
		DataPoint command = data[1].get(hazard_index);

		// We transform the datapoint into a matrix.
		Matrix matrix_comm = new Matrix(1, 6);

		for (int i = 0; i < command.getWeights().size(); i++) {
			matrix_comm.set(0, i, command.getWeight(i));
		}
                */
		/*
		 * We move the arm with this command.
		 */
		// arm.apply(matrix_comm, ArmOptions.max_time, ArmOptions.move_time);

		/*int ep = (int) (Math.random() * 361), cou = (int) (Math.random() * 361), l1 = (int) (Math.random() * 151),
				l2 = (int) (Math.random() * 151);
		

		*/
                //System.out.println(goal.endX() + " , " + goal.endY()+" ==> "+goal.getPosX() + " , " + goal.getPosY());
		ArrayList<Float> pC = null;
		//Premier mode de fonctionnement : Algorithme principale avec parcours al�atoire des X
		if (modedefonctionnement) {
			pC = free.getRandomReachablePoint();
			DataPoint positionC = new DataPoint(pC);
			ArrayList<DataPoint> datasetC = new ArrayList<DataPoint>();
			datasetC.add(positionC);
			
			ArrayList<Neuron> winnersC = getNearestInEyeNetwork(GraphOptions.nb_neurons_approx,datasetC);
			vainqueurs = winnersC;
			barycenter = Neuron.getBarycenter(positionC, 0, 1, winnersC.toArray(new Neuron[1]));
			ArrayList<Float> goal = barycenter.getWeights();
			this.goal = new FreeArm(pC.get(0), pC.get(1));
			free.apply(goal,50);
		//Second mode de fonctionnement : parcours al�atoire des angles pour que l'algo ne se retrouve pas bloqu�
		} else {

			ArrayList<Float> pER = free.getRandomReachablePoint();
			this.goal = new FreeArm((int)((double)pER.get(2)*360), (int)((double)pER.get(3)*360),150,150);
			free.apply(pER,50);
		}
		
		
		ArrayList<Float> pR = free.getPosition();
		DataPoint positionR = new DataPoint(pR);
		ArrayList<Float> cR = new ArrayList<Float>();
		cR.add(pR.get(2));
		cR.add(pR.get(3));
		DataPoint commandR = new DataPoint(cR);
		ArrayList<DataPoint> datasetR = new ArrayList<DataPoint>();
		datasetR.add(positionR);
		datasetR.add(commandR);
		
		ArrayList<Neuron> winnersP = getNearestInAllData(GraphOptions.nb_neurons_approx,datasetR);
		Neuron winnerP = null;
		if (!modedefonctionnement) {
			vainqueurs = winnersP;
			barycenter = Neuron.getBarycenter(positionR,0,1, winnersP.toArray(new Neuron[1]));
			winnerP = barycenter;
		} else 
			winnerP = winnersP.get(0);
		network.step(winnersP, datasetR, data_priority);   
	
                
		examples++;
		if (modedefonctionnement && pC != null) {
			error = lambda * error + distance(pC,pR,0,1);
			error_approx = lambda * error_approx + distance(pR,barycenter.getWeights(),0,1);}
		else{
			error = 0;//distance(pR,barycenter.getWeights(),0,1);
			error_approx = 0;}//arycenter.distance(choosenPosition);}
			sumoferrors = sumoferrors - previous_errors[next_index_error] + error;
			previous_errors[next_index_error] = error;
			next_index_error++;
			if (next_index_error >= 100)
				next_index_error = 0;
			setChanged();
			notifyObservers();
			Thread t = Thread.currentThread();
			try {
				t.sleep(speed);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		
		// BasicOptions.stopped.setBool(free.apply(goal));
		/*
		 * We get the new position of the arm.
		 */
		// We get normalized positions.

		/*
		 * We change the normalization to have the same normalization as the
		 * network.
		 */

		// We set the same dimensions 

		/*ArrayList<Float> pos = new ArrayList<>();
                
                
                
                pos.add(new Float((float)pos_x_norm));
                pos.add(new Float((float)pos_y_norm));


                ArrayList<Float> freecommand = new ArrayList<>();

                freecommand.add(goal.arm1/150);
                freecommand.add(goal.arm2/150);
                freecommand.add(new Float(goal.coude/360));
                freecommand.add(new Float(goal.epaule/360));*/
                 
                 //pos.add(new Float(goal.endX()));
		 //pos.add(new Float(goal.endY()));
                 
		//DataPoint position = new DataPoint(pos);

         //       DataPoint command_data = new DataPoint(freecommand);
		/*
		 * We get the winner neuron in those two data.
		 */
		// We create an array of all datapoints we want to approach.
	/*	ArrayList<DataPoint> dataset = new ArrayList<DataPoint>();
		dataset.add(position);
		dataset.add(command_data);

		ArrayList<Neuron> winners = getNearestInAllData(network.getNbNeuronForLearning(), dataset);*/

                
                //System.out.println("winner end : "+winners.get(0).get);
                
		/*
		 * We compute the error between the position in the neuron and the real
		 * position.
		 */
                
       /* System.out.println(winners.get(0));
		// The first neuron is the really best.
		Neuron best = new Neuron(winners.get(0).getWeights(0, 1));
                
                
                
                
                
                error = best.distance(position);

		/*
		 * We compute the error between the position of the arm and the
		 * barycenter winners.
		 */

		//ArrayList<Neuron> winners_for_approx = getNearestInAllData(GraphOptions.nb_neurons_approx, dataset);

		// We transform it into (x, y) neurons
		//for (int i = 0; i < winners_for_approx.size(); i++)
		//	winners_for_approx.set(i, new Neuron(winners_for_approx.get(i).getWeights(0, 1)));

		// We get their barycenter
		//Neuron barycenter = Neuron.getBarycenter(position, winners_for_approx);

		// Now we compute the error
		//error_approx = barycenter.distance(position);
		/*
		 * We make a step of the learning with these winners.
		 */
		//network.step(winners, dataset, data_priority);

	}
	
	public double distance(ArrayList<Float> f1, ArrayList<Float> f2, int x1,int x2) {
		double res = 0.0;
		for(int i=x1;i<=x2;i++){
		    res += Math.pow(f1.get(i) - f2.get(i), 2);
		}
		
		res = Math.sqrt(res);
		return res;
	}

	/**
	 * A method to find the neurons that wins in each set of data.
	 * 
	 * @param nb_neurons_to_find
	 *            The number of neurons to find in data.
	 * @param arraylist
	 *            of all points we want to approach
	 * @return The best neurons in all sets.
	 */
        
	public ArrayList<Neuron> getNearestInAllData(int nb_neurons_to_find, ArrayList<DataPoint> dataset) {

		ArrayList<Neuron> neuron_list = new ArrayList<Neuron>();

		for (int i = 0; i < nb_neurons_to_find; i++) {

			Neuron winner = null;
			double best_distance = Double.MAX_VALUE;

			// We run through all neurons
			for (ArrayList<Neuron> list : network.getNeurons()) {
				for (Neuron current_neuron : list) {

					// We compute the distance between all data and the current
					// neuron.
					double dist = current_neuron.distanceToAll(dataset, data_priority);

					if (dist < best_distance && !neuron_list.contains(current_neuron)) {
						best_distance = dist;
						winner = current_neuron;
					}
				}
			}

			neuron_list.add(winner);
		}

		return neuron_list;
	}
	
	public ArrayList<Neuron> getNearestInEyeNetwork(int nb_neurons_to_find, ArrayList<DataPoint> dataset) {

		ArrayList<Neuron> neuron_list = new ArrayList<Neuron>();

		for (int i = 0; i < nb_neurons_to_find; i++) {

			Neuron winner = null;
			double best_distance = Double.MAX_VALUE;

			// We run through all neurons
			for (ArrayList<Neuron> list : network.getNeurons()) {
				for (Neuron current_neuron : list) {

					// We compute the distance between all data and the current
					// neuron.
					double dist = current_neuron.distanceToAll(dataset, data_priority,0 ,1);
					if (dist < best_distance && !neuron_list.contains(current_neuron)) {
						best_distance = dist;
						winner = current_neuron;
					}
				}
			}

			neuron_list.add(winner);
		}

		return neuron_list;
	}	
	
	public ArrayList<Neuron> getNearestInMotorNetwork(int nb_neurons_to_find, ArrayList<DataPoint> dataset) {

		ArrayList<Neuron> neuron_list = new ArrayList<Neuron>();

		for (int i = 0; i < nb_neurons_to_find; i++) {

			Neuron winner = null;
			double best_distance = Double.MAX_VALUE;

			// We run through all neurons
			for (ArrayList<Neuron> list : network.getNeurons()) {
				for (Neuron current_neuron : list) {

					// We compute the distance between all data and the current
					// neuron.
					double dist = current_neuron.distanceToAll(dataset, data_priority,0 ,1);

					if (dist < best_distance && !neuron_list.contains(current_neuron)) {
						best_distance = dist;
						winner = current_neuron;
					}
				}
			}
			neuron_list.add(winner);
		}

		return neuron_list;
	}

	/**
	 * A method that check the pause. It is also this method that manage the
	 * step-by-step.
	 */
	public void checkPause() {

		/*
		 * We synchronize in order to wake up the thread if it's waiting.
		 */
		synchronized (this) {

			// We loop while there is the pause or we go forward for one step.
			while (BasicOptions.paused && !BasicOptions.stopped.bool() && !BasicOptions.stepped) {

				try {

					// We wait for the pause to be unset.
					wait();
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}

			}

			// We set stepped to false, in case it has been changed.
			BasicOptions.stepped = false;
		}
	}

	public void reset() {
		resetNetwork();
		error = 0;
		error_approx = 0;
		examples = 0;
	}

	/**
	 * A method to reset all networks.
	 */
	public void resetNetwork() {

		NetworkFactory factory = new NetworkFactory(this);
		AbstractNetwork net = factory.getNetwork(network);
		net.fill();
		network = net;

		change();
	}

	public void change() {
		setChanged();
		notifyObservers();
	}

	// *********************** GETTERS AND SETTERS ***********************//

	public int getSize() {
		return size;
	}

	public CompleteArm getArm() {
		return arm;
	}

	public AbstractNetwork getNet() {
		return network;
	}

	/**
	 * Set the new network to all environments
	 * 
	 * @param network
	 *            The name of the new network.
	 */
	public void setNetwork(String s_net) {
		NetworkFactory factory = new NetworkFactory(this);
		AbstractNetwork net = factory.getNetwork(s_net);
		net.fill();
		network = net;

		change();
	}

	/**
	 * Set the new network to all environments
	 * 
	 * @param network
	 *            The new network.
	 */
	public void setNetwork(AbstractNetwork network) {
		NetworkFactory factory = new NetworkFactory(this);
		AbstractNetwork net = factory.getNetwork(network);
		net.fill();
		network = net;

		change();
	}

	/**
	 * Get the data selected.
	 * 
	 * @param i
	 *            The index of the environment we want.
	 * @return The environment we want.
	 */
	public Data getData(int i) {
		return data[i];
	}

	/**
	 * Get all data.
	 * 
	 * @return
	 */
	public Data[] getData() {
		return data;
	}

	public double getError() {
		return error;
	}

	public double getErrorApprox() {
		return error_approx;
	}

	public int getExamples() {
		return examples;
	}

	public double[] getData_priority() {
		return data_priority;
	}

	public void setData_priority(double[] data_priority) {
		this.data_priority = data_priority;
	}

	public double getError_approx() {
		return error_approx/ 100;
	}

	public void setError_approx(double error_approx) {
		this.error_approx = error_approx;
	}
	
	public FreeArm getFree() {
		return free;
	}

	public void setFree(FreeArm free) {
		this.free = free;
	}

	public AbstractNetwork getNetwork() {
		return network;
	}

	public void setData(Data[] data) {
		this.data = data;
	}

	public void setArm(CompleteArm arm) {
		this.arm = arm;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public void setError(double error) {
		this.error = error;
	}

	public void setExamples(int examples) {
		this.examples = examples;
	}

	public static void changeMode() {
		modedefonctionnement = !modedefonctionnement;		
	}

	public static void setSpeed(double s) {
		speed = (int) (1000/s);		
	}

}
