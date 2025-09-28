package SimulatedAnneling;
import java.util.ArrayList;
import java.util.Random;

import ConstraintCost.ConstraintModel;
import ParserUTP.InstanceUTPArray;


public class Mutation {
	private Constants constants = new Constants();
		private int seed;
	    private Random random;
	    private InstanceUTPArray utp;
	    private int nrSessions;
	    ArrayList<ConstraintModel> hards;
	    ArrayList<ConstraintModel> softs;

	    // Utility method to get a random index from an array
	    private <T> T randomIndex(T[] array) {
	        return array[random.nextInt(array.length)];
	    }
	    
	    public Mutation(int seed, InstanceUTPArray utp,ArrayList<ConstraintModel> hards, ArrayList<ConstraintModel> softs) {
	    	this.seed = seed;
	    	this.utp = utp;
	    	random = new Random(seed);
	    	nrSessions = utp.nr_sessions;
	    	this.softs = softs;
	    	this.hards = hards;
	    }//FinMethod
	    
	    public void typePositionMutateIterative(int type,double penalties, int position, SolutionUTP s) {
	    	if(type == 0) {
	    		//Teacher
	    		for(int i = 0 ; i < utp.part_teachers.get(utp.class_part[utp.session_class[position]-1]-1).size();i++) {
	    			s.x_teachers.set(position, utp.part_teachers.get(utp.class_part[utp.session_class[position]-1]-1).get(i));
	    			if(s.evaluate(softs) + s.evaluate(hards) < penalties ) {
	    				
	    			}
	    		}
	    		  
	    	}
	    	else if (type == 1) {
	    		//Room
	    		int possibleRoom = random.nextInt(utp.part_rooms.get(utp.class_part[utp.session_class[position]-1]-1).size());
	    		s.x_rooms.set(position, utp.part_teachers.get(utp.class_part[utp.session_class[position]-1]-1).get(possibleRoom)); 
	    	}
	    	else if(type == 2 ) {
	    		//Slot
	    		int possibleSlot = random.nextInt(utp.part_slots.get(utp.class_part[utp.session_class[position]-1]-1).size());
	    		s.x_slot.set(position, utp.part_slots.get(utp.class_part[utp.session_class[position]-1]-1).get(possibleSlot)); 
	    	}
	    	else {
	    		System.out.println("Type not recognize "+type);
	    	}
	    }//FinMethod
	    
	    
	    public void typePositionMutateLegal(int type, int position, SolutionUTP s) {
	    	if(type == 0) {
	    		//Teacher
	    		int possibleTeacher = random.nextInt(utp.part_teachers.get(utp.class_part[utp.session_class[position]-1]-1).size());
	    		s.x_teachers.set(position, utp.part_teachers.get(utp.class_part[utp.session_class[position]-1]-1).get(possibleTeacher));  
	    	}
	    	else if (type == 1) {
	    		//Room
	    		int possibleRoom = random.nextInt(utp.part_rooms.get(utp.class_part[utp.session_class[position]-1]-1).size());
	    		s.x_rooms.set(position, utp.part_teachers.get(utp.class_part[utp.session_class[position]-1]-1).get(possibleRoom)); 
	    	}
	    	else if(type == 2 ) {
	    		//Slot
	    		int possibleSlot = random.nextInt(utp.part_slots.get(utp.class_part[utp.session_class[position]-1]-1).size());
	    		s.x_slot.set(position, utp.part_slots.get(utp.class_part[utp.session_class[position]-1]-1).get(possibleSlot)); 
	    	}
	    	else {
	    		System.out.println("Type not recognize "+type);
	    	}
	    }//FinMethod
	    
	    public void typePositionMutateIllegal(int type, int position, SolutionUTP s) {
	    	if(type == 0) {
	    		//Teacher
	    		int possibleTeacher = random.nextInt(utp.nr_teachers);
	    		s.x_teachers.set(position, possibleTeacher);  
	    	}
	    	else if (type == 1) {
	    		//Room
	    		int possibleRoom = random.nextInt(utp.nr_rooms);
	    		s.x_rooms.set(position,possibleRoom); 
	    	}
	    	else if(type == 2 ) {
	    		//Slot
	    		int possibleSlot = random.nextInt(utp.part_slots.get(utp.class_part[utp.session_class[position]-1]-1).size());
	    		s.x_slot.set(position, utp.part_slots.get(utp.class_part[utp.session_class[position]-1]-1).get(possibleSlot)); 
	    	}
	    	else {
	    		System.out.println("Type not recognize "+type);
	    	}
	    }//FinMethod    
	    
	    
	    public SolutionUTP feasibleMutateIterative(SolutionHelperPenalty s){
	    	double choice = random.nextDouble(1.0);
	    	if(choice > 0.49) {
	    		int ns = random.nextInt(constants.MAX_MUTATION);
	    		for(int i = 0 ; i < ns ; i++) {
	    			int position = random.nextInt(this.nrSessions);
	    			int type = random.nextInt(3);
	    			typePositionMutateIterative(type,s.penalty, position, s.getSolution());
	    		}
	    	}
	    	else {
    			int position = random.nextInt(this.nrSessions);
    			int type = random.nextInt(3);
    			typePositionMutateIterative(type,s.penalty, position, s.getSolution());
	    	}
	    	
	    	return new SolutionUTP(s.getSolution());
	    }//FinMethod
	    
	    public SolutionUTP feasibleMutate(SolutionUTP s){
	    	double choice = random.nextDouble(1.0);
	    	if(choice > 0.49) {
	    		int ns = random.nextInt(constants.MAX_MUTATION);
	    		for(int i = 0 ; i < ns ; i++) {
	    			int position = random.nextInt(this.nrSessions);
	    			int type = random.nextInt(3);
	    			typePositionMutateLegal(type, position, s);
	    		}
	    	}
	    	else {
    			int position = random.nextInt(this.nrSessions);
    			int type = random.nextInt(3);
    			typePositionMutateLegal(type, position, s);
	    	}
	    	
	    	return new SolutionUTP(s);
	    }//FinMethod
	    
	    public SolutionUTP infeasibleMutate(SolutionUTP s){
	    	double choice = random.nextDouble(1.0);
	    	if(choice > 0.49) {
	    		int ns = random.nextInt(constants.MAX_MUTATION);
	    		for(int i = 0 ; i < ns ; i++) {
	    			int position = random.nextInt(this.nrSessions);
	    			int type = random.nextInt(3);
	    			typePositionMutateIllegal(type, position, s);
	    		}
	    	}
	    	else {
    			int position = random.nextInt(this.nrSessions);
    			int type = random.nextInt(3);
    			typePositionMutateIllegal(type, position, s);
	    	}
	    	
	    	return new SolutionUTP(s);
	    }//FinMethod
	    
	    public SolutionUTP mutate(SolutionHelperPenalty sol) {
	    	if(sol.penaltyHard == 0) {
	    		return feasibleMutateIterative(sol);
	    	}
	    	else {
	    		return feasibleMutate(sol.getSolution());
	    		//return infeasibleMutate(sol.getSolution());
	    	}
	    }//FinMethod
	    
	    
	    // Calculates the time difference for penalties
	    /*
	    private double timeDiff(ClassPenalties[] penalties, SolutionUTP solution, int cls, int time) {
	        double[] penaltyTimes = penalties[cls].getTimes();
	        int oldTime = solution.getTimeIndex(cls);
	        return penaltyTimes[time] - penaltyTimes[oldTime];
	    }*/

	    // Calculates the room difference for penalties
	    /*
	     * private double roomDiff(ClassPenalties[] penalties, SolutionUTP solution, int cls, int room) {
	        double[] penaltyRooms = penalties[cls].getRooms();
	        int oldRoom = solution.getRoomIndex(cls);
	        return penaltyRooms[room] - penaltyRooms[oldRoom];
	    }
	    */

	    // Updates solution with a new time or room based on non-penalized variable
	    /*
	        Variable variable = randomIndex(variables);
	        int cls = variable.getClassIndex();
	        int value = random.nextInt(variable.getMaxValue());

	        if (variable.getType() == VariableType.TIME) {
	            return solution.withTime(value, cls);
	        } else {
	            return solution.withRoom(value, cls);
	        }
	    }
	    */

	    // Updates solution with a new time and calculates the penalty difference
	    /*public SolutionAndPenalty timePenalized(ClassPenalties[] penalties, Variable[] timeVariables, Solution solution) {
	        Variable variable = randomIndex(timeVariables);
	        int cls = variable.getClassIndex();
	        int time = random.nextInt(variable.getMaxValue());

	        Solution updatedSolution = solution.withTime(time, cls);
	        double penaltyDifference = timeDiff(penalties, solution, cls, time);

	        return new SolutionAndPenalty(updatedSolution, penaltyDifference);
	    }*/

	    // Similar methods for roomPenalized, etc.

	    // Additional methods based on provided F# code
	}


