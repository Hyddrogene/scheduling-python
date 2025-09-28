package SimulatedAnneling;

import ParserUTP.InstanceUTPArray;

public class SolutionHelperPenalty {
// Helper class to handle solution and penalty as a pair (since Java does not support tuples natively)
    public SolutionUTP solution;
    public double penalty;
    public double penaltyHard;
    public double penaltySoft;
    
    

    public SolutionHelperPenalty(SolutionUTP solution, double penaltyHard, double penaltySoft) {
        this.solution = solution;
        this.penaltyHard = penaltyHard;
        this.penaltySoft = penaltySoft;
        this.penalty = (penaltyHard+penaltySoft);
    }//FinMethod

    public SolutionUTP getSolution() {
        return solution;
    }//FinMethod

    public double getPenalty() {
        return penalty;
    }//FinMethod
    
    
    public String toString(InstanceUTPArray utp) {
    	String res = "";
    	for(int i = 0; i < utp.nr_sessions ;i++) {
    		res += "session "+i+" : room="+ utp.room_name[this.solution.x_rooms.get(i)] +
    				", teacher="+utp.teacher_name[this.solution.x_teachers.get(i)-1]+
    				", slot="+this.solution.x_teachers.get(i)+"\n";
    						
    	}

    	
    	return res;
    }//FinMethod
}//FinClass


