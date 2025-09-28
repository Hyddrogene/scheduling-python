package ConstraintCost;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;

import ParserUTP.ConstraintUTP;
import ParserUTP.InstanceUTPArray;
import SimulatedAnneling.SolutionUTP;

public class Forbidden_Slots extends ConstraintModel {
	ArrayList<Integer> variables;
	InstanceUTPArray utp;

	public Forbidden_Slots(ConstraintUTP cutp,InstanceUTPArray utp) {
		super(cutp);
		this.utp = utp;
	}//FinMethod
	
	public int search_value(ConstraintUTP cons,String value) {
		for(int i = 0; i < cons.getParameters().length ;i++) {
			if(this.utp.parameter_name[cons.getParameters()[i]-1].equals(value)) {
				return cons.getParameters()[i]-1;
			}
		}
		return -1;
		
	}//FinMethod
	public int unit_value(String unite) {
		//System.out.println(unite);
		if(unite.equals("week")) {
			 return this.utp.nr_days_per_week * this.utp.nr_slots_per_day;
		}
		
		if(unite.equals("day")) {return this.utp.nr_slots_per_day;}
		return -1;
	}//FinMethod
	
	public int unit_value_bound(String unite) {
		//System.out.println(unite);
		if(unite.equals("week")) {
			 return this.utp.nr_weeks;
		}
		
		if(unite.equals("day")) {return this.utp.nr_days_per_week*this.utp.nr_weeks;}
		return -1;
	}//FinMethod
	


	@Override
	public int evaluate(SolutionUTP ga) {
		
		int first_num = search_value(cutp,"first");
		int last_num = search_value(cutp,"last");
		//System.out.println("Contrainte "+constraint.getCpt()+" rule "+constraint.getRule());

		
		int first = Integer.parseInt(this.utp.parameter_value.get(first_num).get(0));
		int last = Integer.parseInt(this.utp.parameter_value.get(last_num).get(0));
		
		if(cutp.getParameters().length >=3) {
			int unite_num = search_value(cutp,"period");

			int unite = unit_value(this.utp.parameter_value.get(unite_num).get(0));
			int unite_bound = unit_value_bound(this.utp.parameter_value.get(unite_num).get(0));

			int[][] valuePeriod = new int[unite_bound][2];
			for(int i = 0; i < unite_bound ;i++) {
				valuePeriod[i][0] = first + (i*unite);
				valuePeriod[i][1] = last + (i*unite);
			}
		    ArrayList<Integer> room = Arrays.stream(valuePeriod).flatMapToInt(Arrays::stream).boxed().collect(Collectors.toCollection(ArrayList<Integer>::new));
			int penalties = 0;
			if(this.cutp.getSessions().size() == 1) {
				for(int j = 0 ;j < this.cutp.getSessions().get(0).size()-1 ;j++) {
						if( room.contains(ga.x_slot.get(this.cutp.getSessions().get(0).get(j)-1))  ) {
							penalties += 1;
						}
				}
			}
			// TODO Auto-generated method stub
			return penalties;
		}
		return 0;
		}
}//FinClass
