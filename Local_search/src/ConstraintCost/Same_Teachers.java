package ConstraintCost;

import java.util.ArrayList;

import ParserUTP.ConstraintUTP;
import SimulatedAnneling.SolutionUTP;

public class Same_Teachers extends ConstraintModel {
	ArrayList<Integer> variables;

	public Same_Teachers(ConstraintUTP cutp) {
		super(cutp);
	}//FinMethod
	

	

	@Override
	public int evaluate(SolutionUTP ga) {
		if(this.cutp.getSessions().size() == 1) {
			return this.countDifferenceToMaxv2(this.createSubArrayList(ga.x_teachers,cutp.getSessions().get(0)));
		}
		else if(this.cutp.getSessions().size() >= 2){
			return this.countDifferenceToMaxv2(this.createSubArrayList(ga.x_teachers,fusionAllArray(cutp.getSessions())));
		}
		// TODO Auto-generated method stub
		return 0;
	}

}//FinClass
