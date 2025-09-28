package ConstraintCost;

import java.util.ArrayList;

import ParserUTP.ConstraintUTP;
import ParserUTP.InstanceUTPArray;
import SimulatedAnneling.SolutionUTP;

public class Periodic extends ConstraintModel {
	ArrayList<Integer> variables;
	InstanceUTPArray utp;

	public Periodic(ConstraintUTP cutp,InstanceUTPArray utp) {
		super(cutp);
	}//FinMethod
	

	@Override
	public int evaluate(SolutionUTP ga) {
		int penalties = 0;
		if(this.cutp.getSessions().size() == 1) {
			for(int j = 0 ;j < this.cutp.getSessions().get(0).size()-1 ;j++) {
					if( ga.x_slot.get(this.cutp.getSessions().get(0).get(j)-1)+7200 != ga.x_slot.get(this.cutp.getSessions().get(0).get(j+1)-1) ) {
						penalties += 1;
					}
			}
		}
		return penalties;
	}
}//FinClass
