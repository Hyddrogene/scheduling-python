package ConstraintCost;

import java.util.ArrayList;
import java.util.stream.Collectors;

import ParserUTP.ConstraintUTP;
import SimulatedAnneling.SolutionUTP;

public class Same_WeeklySlot extends ConstraintModel {
	ArrayList<Integer> variables;

	public Same_WeeklySlot(ConstraintUTP cutp) {
		super(cutp);
	}//FinMethod
	

	

	@Override
	public int evaluate(SolutionUTP ga) {
		if(this.cutp.getSessions().size() == 1) {
			return this.countDifferenceToMaxv2(this.createSubArrayList(ga.x_slot.stream().map(u->u).collect(Collectors.toCollection(ArrayList<Integer>::new)) ,cutp.getSessions().get(0)));
		}
		else if(this.cutp.getSessions().size() >= 2){
			return this.countDifferenceToMaxv2(this.createSubArrayList(ga.x_slot.stream().map(u->u).collect(Collectors.toCollection(ArrayList<Integer>::new)),fusionAllArray(cutp.getSessions())));
		}
		// TODO Auto-generated method stub
		return 0;
	}
}//FinClass
