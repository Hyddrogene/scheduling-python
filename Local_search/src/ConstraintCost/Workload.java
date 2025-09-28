package ConstraintCost;

import java.util.ArrayList;

import ParserUTP.ConstraintUTP;
import SimulatedAnneling.SolutionUTP;

public class Workload extends ConstraintModel {
	ArrayList<Integer> variables;

	public Workload(ConstraintUTP cutp) {
		super(cutp);
	}//FinMethod
	

	

	@Override
	public int evaluate(SolutionUTP ga) {
		int penalties = 0;
		if(this.cutp.getSessions().size() == 1) {
			int  minVal = this.createSubArrayList(ga.x_slot,cutp.getSessions().get(0)).stream().mapToInt(u->u).min().getAsInt();
			int  maxVal = this.createSubArrayList(ga.x_slot,cutp.getSessions().get(0)).stream().mapToInt(u->u).max().getAsInt();
			if( 540 < (maxVal -minVal)) {
				penalties++;
			}
			
		}
		// TODO Auto-generated method stub
		return penalties;
	}
}//FinClass
