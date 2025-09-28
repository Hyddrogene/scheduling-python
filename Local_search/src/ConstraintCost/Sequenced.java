
package ConstraintCost;

import java.util.ArrayList;

import ParserUTP.ConstraintUTP;
import SimulatedAnneling.SolutionUTP;

public class Sequenced extends ConstraintModel {
	ArrayList<Integer> variables;

	public Sequenced(ConstraintUTP cutp) {
		super(cutp);
	}//FinMethod
	

	

	@Override
	public int evaluate(SolutionUTP ga) {
		int penalties = 0;
		if(this.cutp.getSessions().size() == 1) {
			for(int j = 0 ;j < this.cutp.getSessions().get(0).size()-1 ;j++) {
					if( ga.x_slot.get(this.cutp.getSessions().get(0).get(j)-1) > ga.x_slot.get(this.cutp.getSessions().get(0).get(j+1)-1) ) {
						penalties += 1;
					}
			}
		}
		else if(this.cutp.getSessions().size() >= 2){
			for(int i = 0 ;i < this.cutp.getSessions().size()-1 ;i++) {
				for(int j = 0 ;j < this.cutp.getSessions().get(i).size() ;j++) {
					for(int k = 0 ;k < this.cutp.getSessions().get((i+1)).size() ;k++) {
						if( ga.x_slot.get(this.cutp.getSessions().get(i).get(j)-1) > ga.x_slot.get(this.cutp.getSessions().get((i+1)).get(k)-1) ) {
							penalties += 1;
						}
					}
				}
				
			}
			return penalties;
		}
		// TODO Auto-generated method stub
		return 0;
	}
}//FinClass
