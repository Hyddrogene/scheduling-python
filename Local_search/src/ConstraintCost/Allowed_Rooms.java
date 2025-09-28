package ConstraintCost;

import java.util.ArrayList;

import ParserUTP.ConstraintUTP;
import ParserUTP.InstanceUTPArray;
import SimulatedAnneling.SolutionUTP;

public class Allowed_Rooms extends ConstraintModel {
	ArrayList<Integer> variables;
	InstanceUTPArray utp;

	public Allowed_Rooms(ConstraintUTP cutp,InstanceUTPArray utp) {
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
		return 0;
	}
}//FinClass
