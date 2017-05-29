package src.model.arm;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Observable;
import java.util.Random;

import Jama.Matrix;
import src.model.LinkedEnvironment;

public class FreeArm extends Observable {

	public double epaule, coude;
	public float arm1, arm2;

	double x, y;

	Arm arm;

	NeuroControl[] _nc = new NeuroControl[6];

	private String last_command;

        
    public double getPosX() {
            return x;
	}

	public double getPosY() {
            return y;
	}
        
        
	public int midX() {

		int rep = 0;
		rep = (int) (Math.cos(Math.toRadians(epaule)) * arm1);
		return rep;
	}

	public int midY() {

		int rep = 0;
		rep = (int) (Math.sin(Math.toRadians(epaule)) * arm1);
		return rep;
	}

	public int endX() {

		int rep = 0;
		rep = (int) (Math.cos(Math.toRadians(epaule + coude)) * arm2);
		rep = rep + midX();
		return rep;
	}
	
	public double normMidX() {
		double l = arm1+arm2;
		return (midX()+l)/(2*l);
	}
	
	public double normMidY() {
		double l = arm1+arm2;
		return (midY()+l)/(2*l);
	}
	
	public double normEndX() {
		double l = arm1+arm2;
		return (endX()+l)/(2*l);
	}
	
	public double normEndY() {
		double l = arm1+arm2;
		return (endY()+l)/(2*l);
	}

	public int endY() {

		int rep = 0;
		rep = (int) (Math.sin(Math.toRadians(epaule + coude)) * arm2);
		rep = rep + midY();
		return rep;
	}

	public FreeArm() {

		epaule = 0;
		coude = 0;
		arm1 = 150;
		arm2 = 150;
        updatePosition();

	}

	public FreeArm(int ep, int cou, int l1, int l2) {

		epaule = ep;
		coude = cou;
		arm1 = l1;
		arm2 = l2;
        updatePosition();
    }

	public FreeArm(Float float1, Float float2) {
		arm1 = 100;
		arm2 = 100;
		float l = (float)(arm1+arm2);
		x = float1;// * (2*l))-l;
		y = float2;// * (2*l))-l;
	}

	public void setup(double ang0, double ang1) {
		// setup arm with no angular speed
		// double[] ang = { ang0, ang1 };
		// _arm.setArmPos(ang);
		// _arm.setArmSpeed(new Matrix(1, 2)); // Matrix at zero by default
		//
		// // setup muscles length
		// // _muscles.computeTorque(new Matrix(1,6), _arm.getArmPos(),
		// // _arm.getArmSpeed());
		//
		// // No neural activity
		// for (int i = 0; i < _nc.length; i++) {
		// _nc[i].setAct(0.0);
		// _nc[i].setU(0.0);
		// }

		last_command = "RESET";

		// Observable
		setChanged();
		notifyObservers();

	}

	public boolean apply(ArrayList<Float> goal, int wait_time) {

		// Matrix previous_pos = new Matrix(0, 0);
		// Matrix current_pos = _arm.getArmPos();

		// last_command = matrixToString(comm);

		double elapsed_time = 0.0;
		
		epaule = (double) (goal.get(2))*360;
		coude = (double) (goal.get(3))*360;
		/*
		boolean arret = false;
		
		while (!arret) {

			applyCommand(goal.get(0), goal.get(1));
			// elapsed_time += dt;
                        
                        updatePosition();
                        
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			// previous_pos = current_pos;
			// current_pos = _arm.getArmPos();

			if (goal.endX() == endX()) {
				if (goal.endY() == endY()) {
					arret = true;
				}
			}

		}*/
		updatePosition();
		setChanged();
		notifyObservers();
		return true;
	}

	public void applyCommand2(int ep, int cou) {

		if (ep != epaule) {
			if (ep >= epaule) {
				epaule++;
			} else {
				epaule--;
			}
		}

		if (cou != coude) {
			if (cou >= coude) {
				coude++;
			} else {
				coude--;
			}
		}


		// for (int i = 0; i < _nc.length; i++) {
		// _nc[i].applyCommand(comm.get(0, i), dt);
		// _act.set(0, i, _nc[i].getAct());
		// }
		//
		// // Observable
		setChanged();
		notifyObservers();
		// return applyActivation(_act, dt);
	}

	public void applyCommand(int ep, int cou) {

                if (cou > coude)
                    coude++;
		else if(cou != coude)
                    coude--;
            
                if (ep > epaule) 
                    epaule++;
                else if(ep != epaule)
                    epaule--;
              
                
		// for (int i = 0; i < _nc.length; i++) {
		// _nc[i].applyCommand(comm.get(0, i), dt);
		// _act.set(0, i, _nc[i].getAct());
		// }
		//
		// // Observable
		setChanged();
		notifyObservers();
		// return applyActivation(_act, dt);
	}

	public void change() {
		// TODO Auto-generated method stub

	}

	@Override
	public String toString() {
		return "FreeArm [epaule=" + epaule + ", coude=" + coude + ", arm1=" + arm1 + ", arm2=" + arm2 + ", x=" + x
				+ ", y=" + y + ", _arm=" + arm + ", _nc=" + Arrays.toString(_nc) + ", last_command=" + last_command
				+ "]";
	}

    private void updatePosition() {

		double l =  (arm1 + arm2);
        x = (endX()+l)/(2*l);
        y = (endY()+l)/(2*l);
        
    }

	public ArrayList<Float> getRandomReachablePoint() {
		float[] res = new float[4];
		float l =  (float) (arm1 + arm2);
		boolean reachable = false;
		Random r = new Random();
		do {
			res[0] = (r.nextFloat() * l * 2) - l;
			res[1] = (r.nextFloat() * l * 2) - l;
			double hypo = Math.sqrt(res[0]*res[0] + res[1]*res[1]);
			if (hypo <= l)
				reachable = true;
		} while (!reachable);
		res[2] = r.nextFloat();
		res[3] = r.nextFloat();
		ArrayList<Float> lr = new ArrayList<Float>();
		lr.add((res[0]+l)/(2*l));
		lr.add((res[1]+l)/(2*l));
		lr.add((res[2]));
		lr.add((res[3]));
		return lr;
	}

	public ArrayList<Float> getPosition() {
		ArrayList<Float> res = new ArrayList<Float>();
		float l = (float)(arm1+arm2);
		res.add((float)getPosX());
		res.add((float)getPosY());
		res.add((float)epaule/360.f);
		res.add((float)coude/360.f);
		return res;
	}
}
