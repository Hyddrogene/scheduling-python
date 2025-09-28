package ConstraintCost;

import java.util.ArrayList;

import ParserUTP.ConstraintUTP;
import ParserUTP.InstanceUTPArray;
import SimulatedAnneling.SolutionUTP;

public class Assign_Teachers extends ConstraintModel {
	ArrayList<Integer> variables;
	InstanceUTPArray utp;

	public Assign_Teachers(ConstraintUTP cutp,InstanceUTPArray utp) {
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
		
		int last_num = search_value(cutp,"value");
		//System.out.println("Contrainte "+constraint.getCpt()+" rule "+constraint.getRule());

		
		int value = Integer.parseInt(this.utp.parameter_value.get(last_num).get(0));
		int penalties = 0;
		if(this.cutp.getSessions().size() == 1) {
			for(int i = 0; i < cutp.getSessions().get(0).size() ;i++) {
				if(ga.x_teachers.get(cutp.getSessions().get(0).get(i)-1) != value  ) {
					penalties++;
				}
			}
		}
		// TODO Auto-generated method stub
		return penalties;
	}
}//FinClass
