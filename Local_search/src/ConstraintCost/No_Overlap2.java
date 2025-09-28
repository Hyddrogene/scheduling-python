package ConstraintCost;

import java.util.ArrayList;

import ParserUTP.ConstraintUTP;
import ParserUTP.InstanceUTPArray;
import SimulatedAnneling.SolutionUTP;

public class No_Overlap2 extends ConstraintModel {
	ArrayList<Integer> variables;
	InstanceUTPArray utp;
	ArrayList<Integer> sessions;
	int element;

	public No_Overlap2(ConstraintUTP cutp,InstanceUTPArray utp,ArrayList<Integer> sessions,int element) {
		super(cutp);
	}//FinMethod
	

	public int search_value(ConstraintUTP cons,String value) {
		for(int i = 0; i < cons.getParameters().length ;i++) {
			if(this.utp.parameter_name[cons.getParameters()[i]-1].equals(value)) {
				return cons.getParameters()[i]-1;
			}
		}
		return -1;
	}//FinMethod
	

	@Override
	public int evaluate(SolutionUTP ga) {
		int penalties = 0;
		if(this.cutp.getSessions().size() == 1) {
			for(int i = 0 ;i < sessions.size()-1 ;i++) {
				for(int j = 0 ;j < sessions.size()-1 ;j++) {
					if( sessions.get(i) != sessions.get(j) ) {
						if((ga.x_rooms.get(sessions.get(i)) == element && ga.x_rooms.get(sessions.get(i)) == element)) {
							if((ga.x_slot.get(sessions.get(i)-1)+ utp.part_session_length[utp.class_part[utp.session_class[sessions.get(i)-1]-1]] <=  ga.x_slot.get(sessions.get(j)))
									||  
								(ga.x_slot.get(sessions.get(j)-1)+ utp.part_session_length[utp.class_part[utp.session_class[sessions.get(j)-1]-1]] <=  ga.x_slot.get(sessions.get(i))))
							{
								penalties += 1;
							}
						}
					}
				}
			}
		}
		return penalties;
	}//FinMethod
}//FinClass
