package GA;

import java.util.ArrayList;
import java.util.stream.IntStream;

import ConstraintCost.ConstraintModel;
import ConstraintCost.Same_Slot;
import ConstraintCost.Variable;
import ParserUTP.InstanceUTPArray;

public class Solution {
	public int nrSessions;
	public ArrayList<Variable> xSlot;
	public ArrayList<ConstraintModel> constraints;
	
    public int[] range(int min, int max) {
        if (min > max) {
            throw new IllegalArgumentException("min should be less than or equal to max");
        }
        return IntStream.rangeClosed(min, max).toArray();
    }//FinMethod
	
	
	public Solution(InstanceUTPArray utp) {
		this.nrSessions = utp.nr_sessions;
		xSlot = new ArrayList<Variable> ();
		for (int i= 0 ; i  < this.nrSessions ;i++) {
			Variable varTmp = new Variable("xSlot_"+i,range(1,600));
			xSlot.add(varTmp);
 		}
		
		
		for(int c = 0; c < utp.constraints.size();c++) {
			if(utp.constraints.get(c).getConstraint().equals("sameTeacher")) {
				ConstraintModel cons = new Same_Slot(utp.constraints.get(c));
				constraints.add(cons);
			}
		}
		
		
		
	}//FinMethod
}//FinClass
