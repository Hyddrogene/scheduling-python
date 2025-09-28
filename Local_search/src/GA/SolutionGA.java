package GA;

import java.util.ArrayList;

import ConstraintCost.ConstraintModel;

public class SolutionGA{
	//==== Attributes ====
	public ArrayList<Integer> x_slot;
	public ArrayList<SessionRooms> x_rooms;
	public ArrayList<SessionTeachers> x_teachers;
	
	//====== METHOD ======
	public SolutionGA(ArrayList<Integer> x_slot,ArrayList<SessionRooms> x_rooms, ArrayList<SessionTeachers> x_teachers) {
		this.x_slot = x_slot;
		this.x_rooms = x_rooms;
		this.x_teachers = x_teachers;
	}//FinMehod
	
	public SolutionGA(SolutionGA ga) {
		copySolution(ga);
	}//FinMehod
	
	
	public void copySolution(SolutionGA ga) {
		x_slot = new ArrayList<Integer>();
		for(int i = 0; i < ga.x_slot.size() ;i++) {
			x_slot.add(ga.x_slot.get(i).intValue());
		}
		x_rooms = new ArrayList<SessionRooms>();
		for(int i = 0; i < ga.x_rooms.size() ;i++) {
			x_rooms.add(new SessionRooms(ga.x_rooms.get(i)));
		}
		x_teachers = new ArrayList<SessionTeachers>();
		for(int i = 0; i < ga.x_teachers.size() ;i++) {
			x_teachers.add(new SessionTeachers(ga.x_teachers.get(i)));
		}
	}//FinMehod
	
	public int evaluate(ArrayList<ConstraintModel> constraints){
		int score = 0;
		for(ConstraintModel cons: constraints) {
			score += 1;//cons.evaluate(this);
		}
		return score;
	}//FinMethod;
	
	public String toString() {
		return this.x_slot.toString();
	}//FinMethod
}//FinClass
