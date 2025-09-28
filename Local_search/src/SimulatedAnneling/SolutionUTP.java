package SimulatedAnneling;

import java.util.ArrayList;

import ConstraintCost.ConstraintModel;


import ParserUTP.InstanceUTPArray;

public class SolutionUTP {
	public int nr_sessions;
	//==== Attributes ====
	public ArrayList<Integer> x_slot;
	public ArrayList<Integer> x_rooms;
	public ArrayList<Integer> x_teachers;
	public ArrayList<VariableUTP> variables;

	public InstanceUTPArray utp;
	public int soft_penalty;
	public int hard_penalty;
	public int class_overflow;
	//public ArrayList<SessionRooms> x_rooms;
	//public ArrayList<SessionTeachers> x_teachers;
	
	//====== METHOD ======
	
	/*public SolutionUTP(ArrayList<Integer> x_slot,ArrayList<SessionRooms> x_rooms, ArrayList<SessionTeachers> x_teachers) {
		this.x_slot = x_slot;
		this.x_rooms = x_rooms;
		this.x_teachers = x_teachers;
	}//FinMehod*/
	
	public SolutionUTP(InstanceUTPArray utp) {
		this.utp = utp;
	}//FinMehod
	
	public SolutionUTP(SolutionUTP ga) {
		copySolution(ga);
	}//FinMehod
	
	
	public void copySolution(SolutionUTP ga) {
		x_slot = new ArrayList<Integer>();
		for(int i = 0; i < ga.x_slot.size() ;i++) {
			x_slot.add(ga.x_slot.get(i).intValue());
		}
		
		x_rooms = new ArrayList<Integer>();
		for(int i = 0; i < ga.x_rooms.size() ;i++) {
			x_rooms.add( ga.x_rooms.get(i) );
		}
		x_teachers = new ArrayList<Integer>();
		for(int i = 0; i < ga.x_teachers.size() ;i++) {
			x_teachers.add(ga.x_teachers.get(i));
		}
		/*
		x_rooms = new ArrayList<SessionRooms>();
		for(int i = 0; i < ga.x_rooms.size() ;i++) {
			x_rooms.add(new SessionRooms(ga.x_rooms.get(i)));
		}
		x_teachers = new ArrayList<SessionTeachers>();
		for(int i = 0; i < ga.x_teachers.size() ;i++) {
			x_teachers.add(new SessionTeachers(ga.x_teachers.get(i)));
		}*/
	}//FinMehod
	
	public int evaluate(ArrayList<ConstraintModel> constraints){
		int score = 0;
		//String res = "score =";
		for(ConstraintModel cons: constraints) {
			if(cons != null) {
				int ttt = cons.evaluate(this);
				score += ttt;
				//res += cons.getUTP().getConstraint()+"-"+cons.evaluate(this)+"  ";
			}
		}
		//System.out.println(res);
		return score;
	}//FinMethod;
	
	public int evaluate(ArrayList<ConstraintModel> constraints,ArrayList<Integer> weitg){
		int score = 0;
		//String res = "score =";
		int u = 0;
		for(ConstraintModel cons: constraints) {
			if(cons != null) {
				int ttt = cons.evaluate(this);
				
				if(ttt > 0) {
					score += ttt*weitg.get(u);
					weitg.set(u,(weitg.get(u)+10));

				}
				else {
					score += ttt;
				}
				u++;
				//res += cons.getUTP().getConstraint()+"-"+cons.evaluate(this)+"  ";
			}
		}
		//System.out.println(res);
		return score;
	}//FinMethod;
	
	
	public String toString() {
		return this.x_slot.toString();
	}//FinMethod

	public int getNr_sessions() {
		return nr_sessions;
	}//FinMethod



	public void setNr_sessions(int nr_sessions) {
		this.nr_sessions = nr_sessions;
	}//FinMethod



	public int getSoft_penalty() {
		return soft_penalty;
	}//FinMethod



	public void setSoft_penalty(int soft_penalty) {
		this.soft_penalty = soft_penalty;
	}//FinMethod



	public int getHard_penalty() {
		return hard_penalty;
	}//FinMethod



	public void setHard_penalty(int hard_penalty) {
		this.hard_penalty = hard_penalty;
	}//FinMethod
	
	
	
}//FinClass
