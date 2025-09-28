package ConstraintCost;

import java.util.ArrayList;

import ParserUTP.ConstraintUTP;
import ParserUTP.InstanceUTPArray;
import SimulatedAnneling.SolutionUTP;

public class Implicite_Sequenced extends ConstraintModel {
	ArrayList<Integer> sessions;
	InstanceUTPArray utp;

	public Implicite_Sequenced(ConstraintUTP cutp,ArrayList<Integer> sessions,InstanceUTPArray utp) {
		super(cutp);
		this.sessions = sessions;
		this.utp = utp;
	}//FinMethod
	

	

	@Override
	public int evaluate(SolutionUTP ga) {
		int penalties = 0;
		if(sessions.size() == 1) {
			
			for(int j = 0 ;j < sessions.size()-1 ;j++) {
					if( ga.x_slot.get(sessions.get(j)-1) > ga.x_slot.get(sessions.get(j+1)-1) + utp.part_session_length[utp.class_part[utp.session_class[sessions.get(j+1)-1]-1]]  ) {
						penalties += 1;
					}
			}
		}
		return penalties;
	}
}//FinClass