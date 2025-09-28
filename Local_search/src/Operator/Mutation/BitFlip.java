package Operator.Mutation;

import java.util.Random;

import GA.SolutionGA;
import ParserUTP.InstanceUTPArray;

public class BitFlip {
	protected Random random = new Random();
	InstanceUTPArray UTP;
	
	public BitFlip(InstanceUTPArray UTP) {
		this.UTP = UTP;
	}//FinMethod

    protected SolutionGA doMutation(SolutionGA X, double proba) {
     
        for (int i = 0; i < X.x_slot.size(); i++) {
        	if(random.nextDouble() < proba) {
        		int pos = random.nextInt(0,UTP.part_grid_extension.get(UTP.session_part[i]-1).size());
        		X.x_slot.set(i, UTP.part_grid_extension.get(UTP.session_part[i]-1).get(pos));
            }
        }
        return X;
    }//FinMethod

    public SolutionGA mutate(SolutionGA X, double proba) {
    	SolutionGA Y = new SolutionGA(X);//getProbVar(X.length);
        return doMutation(Y,proba);
    }//FinMethod
}//FinClass
