package SimulatedAnneling;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.Vector;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import ConstraintCost.Allowed_Slots;
import ConstraintCost.Assign_Rooms;
import ConstraintCost.ConstraintModel;
import ConstraintCost.Different_Slot;
import ConstraintCost.Different_Week;
import ConstraintCost.Different_WeekDay;
import ConstraintCost.Forbidden_Rooms;
import ConstraintCost.Forbidden_Slots;
import ConstraintCost.Implicite_Sequenced;
import ConstraintCost.No_Overlap;
import ConstraintCost.No_Overlap2;
import ConstraintCost.No_Overlap3;
import ConstraintCost.No_Overlap4;
import ConstraintCost.Periodic;
import ConstraintCost.Same_Rooms;
import ConstraintCost.Same_Slot;
import ConstraintCost.Same_Teachers;
import ConstraintCost.Same_Week;
import ConstraintCost.Same_WeekDay;
import ConstraintCost.Same_WeeklySlot;
import ConstraintCost.Sequenced;
import ParserUTP.InstanceUTPArray;
import ParserUTP.ConstraintUTP;

public class Solver {
	//public
	public SolutionUTP solution;
	public SolutionUTP best;
	public InstanceUTPArray utp;
	public int limit;
	public int seed;
	public Random rand;
	public ArrayList<ConstraintModel> hards;
	public ArrayList<ConstraintModel> softs;;
	public Constants constants = new Constants();
	//private
	
	
	public SolutionUTP initSolution(SolutionUTP solution) {
		int p= -1;
		int t = -1;
		int r = -1;
		int s = -1;
		int ns = utp.nr_sessions;
		
		int[] slots = new int[ns];
		int[] rooms = new int[ns];
		int[] teachers = new int[ns];
		
		for(int i =0 ; i < utp.nr_sessions ;i++){
			p = utp.class_part[utp.session_class[i]-1]-1;
			r = rand.nextInt(utp.part_rooms.get(p).size());
			t = 0;//rand.nextInt(utp.part_teachers.get(p).size());
			s = 0;//rand.nextInt(utp.part_slots.get(p).size());
			
			int room = utp.part_rooms.get(p).get(r);
			int teacher = utp.part_teachers.get(p).get(t);
			int slot = utp.part_slots.get(p).get(s);
			slots[i] = slot;
			rooms[i] = room;
			teachers[i] = teacher;
		}
		solution.x_rooms = Arrays.stream(rooms).map(u->u).boxed().collect(Collectors.toCollection(ArrayList<Integer>::new));
		solution.x_teachers = Arrays.stream(teachers).map(u->u).boxed().collect(Collectors.toCollection(ArrayList<Integer>::new));
		solution.x_slot = Arrays.stream(slots).map(u->u).boxed().collect(Collectors.toCollection(ArrayList<Integer>::new));
		
		ArrayList<VariableUTP> variables = new ArrayList<VariableUTP>();
		for(int i = 0; i < ns ;i++) {
			variables.add(new VariableUTP(solution.x_rooms.get(i),solution.x_teachers.get(i),solution.x_slot.get(i)));
		}
		solution.variables = variables;
		return solution;
	}//FinMethod
	
	
	
	public void convertConstraint (InstanceUTPArray utp) {
		ArrayList<ConstraintModel> hards = new ArrayList<ConstraintModel>();
		ArrayList<ConstraintModel> softs = new ArrayList<ConstraintModel>();
		
		createHards(hards);
		
		for(int i = 0; i < utp.constraints.size() ;i++) {
			ConstraintUTP constraint =  utp.constraints.get(i);
			ConstraintModel tmp;
			if(constraint.getConstraint().equals("sameTeachers")) {
				tmp = new Same_Teachers(constraint);
				//sof_periodic_v2(this.instanceUTP.constraints.get(i));
				}
			else if(constraint.getConstraint().equals("forbiddenSlots") || constraint.getConstraint().equals("forbiddenPeriod")) {tmp = new Forbidden_Slots(constraint,utp);}
			else if(constraint.getConstraint().equals("sameRooms")) {tmp = new Same_Rooms(constraint);}
			else if(constraint.getConstraint().equals("sameSlot")) {tmp = new Same_Slot(constraint);}
			else if(constraint.getConstraint().equals("sameSlots")) {tmp = new  Same_Slot(constraint);}
			else if(constraint.getConstraint().equals("allowedPeriod")) {tmp = new Allowed_Slots(constraint,utp);}
			else if(constraint.getConstraint().equals("sequenced")) {tmp = new Sequenced(constraint);}
			else if(constraint.getConstraint().equals("sameWeek")) {tmp = new Same_Week(constraint);}
			else if(constraint.getConstraint().equals("assignRoom")) {tmp = new Assign_Rooms(constraint,utp);}
			else if(constraint.getConstraint().equals("sameWeekDay")) {tmp = new Same_WeekDay(constraint);}
			else if(constraint.getConstraint().equals("sameWeeklySlot")) {tmp = new Same_WeeklySlot(constraint);}
			else if(constraint.getConstraint().equals("disjunct")) {tmp = new No_Overlap(constraint,utp);}
			else if(constraint.getConstraint().equals("differentWeekDay")) {tmp = new Different_WeekDay(constraint);}
			else if(constraint.getConstraint().equals("periodic")) {tmp = new Periodic(constraint,utp);}
			//else if(constraint.getConstraint().equals("weekly")) {weekly(constraint);}
			else if(constraint.getConstraint().equals("differentWeek")) {tmp = new Different_Week(constraint);}
			else if(constraint.getConstraint().equals("differentSlots")) {tmp = new Different_Slot(constraint);}
			else if(constraint.getConstraint().equals("forbiddenRooms")) {tmp = new Forbidden_Rooms(constraint,utp);}
			else {
				tmp = null;
				System.out.println("Constraint "+constraint.getConstraint()+" is not implemented"+" provide from rule : "+constraint.getRule());}
			
			if(utp.constraints.get(i).getHardness().equals("soft")) {
				softs.add(tmp);
			}
			else {
				hards.add(tmp);
			}
		}
		this.hards =  hards;
		this.softs =  softs;
	}//FinMethod
	
	public void writeSolution() {
		
	}//FinMethod
	
	public Solver(InstanceUTPArray utp) {
		this.utp = utp;
		Random sedd = new Random();
		int seed = sedd.nextInt();
		System.out.println("Seed "+seed);
		rand = new Random(seed);
	}//FinMethod
	
	public void createHards(ArrayList<ConstraintModel> conss) {
		for(int i = 0; i < utp.nr_rooms ;i++) {
			ArrayList<Integer> sessions = utp.room_sessions.get(i).stream().map(u->u-1).collect(Collectors.toCollection(ArrayList<Integer>::new));
			ConstraintUTP cu = new ConstraintUTP(0,0,"NoOverlapRoom","hard",1,new String[] {"room"},new int[] {(i+1)},new Vector<Vector<Integer>>(),new int[] {});
			No_Overlap2 cons = new No_Overlap2(cu,utp,sessions,(i+1));
			conss.add(cons);
		}
		for(int i = 0; i < utp.nr_teachers ;i++) {
			ArrayList<Integer> sessions = utp.teacher_sessions.get(i).stream().map(u->u-1).collect(Collectors.toCollection(ArrayList<Integer>::new));
			ConstraintUTP cu = new ConstraintUTP(0,0,"NoOverlapTeacher","hard",1,new String[] {"teacher"},new int[] {(i+1)},new Vector<Vector<Integer>>(),new int[] {});
			No_Overlap3 cons = new No_Overlap3(cu,utp,sessions,(i+1));
			conss.add(cons);
		}
		for(int i = 0; i < utp.nr_groups ;i++) {
			ArrayList<Integer> sessions = utp.group_sessions.get(i).stream().map(u->u-1).collect(Collectors.toCollection(ArrayList<Integer>::new));
			ConstraintUTP cu = new ConstraintUTP(0,0,"NoOverlapGroup","hard",1,new String[] {"group"},new int[] {(i+1)},new Vector<Vector<Integer>>(),new int[] {});
			No_Overlap4 cons = new No_Overlap4(cu,utp,sessions,(i+1));
			conss.add(cons);
		}
		//implicite sequenced 
		for(int i = 0; i < utp.nr_classes ;i++) {
			ArrayList<Integer> sessions = utp.class_sessions.get(i).stream().map(u->u-1).collect(Collectors.toCollection(ArrayList<Integer>::new));
			ConstraintUTP cu = new ConstraintUTP(0,0,"ImpliciteSequenced","hard",1,new String[] {"class"},new int[] {(i+1)},new Vector<Vector<Integer>>(),new int[] {});
			Implicite_Sequenced cons = new Implicite_Sequenced(cu,sessions,utp);
			conss.add(cons);
		}
		
	}//FinMethod
	
	
	public double cool(double t,int hardPenalty, int softPenalty) {
		double t2 = 0;
	      if (hardPenalty == 0 ) {
	    	  t2 = t / (1.0 + constants.BETA * t);
	      }        
	      else {
	    	  t2 = t / (1.0 + constants.BETA_UNFEASIBLE * t);
	      }
	       
	     return t2;
	}

	    /*
	    public SolutionUTP mutate(SolutionUTP s, double sPenalty) {
	        int randomCount = Math.max(1, random.nextInt(maxMutations)) - 1;
	        SolutionUTP best = s;
	        double bestPenalty = sPenalty;
	        SolutionUTP current = s;

	        for (int i = 0; i <= randomCount; i++) {
	        	SolutionUTP candidate = getNextCandidate(current, bestPenalty >= 1.0);
	            double candidatePenalty1 = candidate.evaluate(hards);
	            double candidatePenalty2 = candidate.evaluate(softs);

	            double candidatePenalty = candidatePenalty1 + candidatePenalty2;

	            if (candidatePenalty <= bestPenalty) {
	                best = candidate;
	                bestPenalty = candidatePenalty;
	            }
	            // Continue with the current candidate regardless of whether it was better
	            current = candidate;
	        }

	        return best;
	    }//FinMethod
	    */
	    
	    public double gamma(int localTimeout) {
	        int localTimeoutPeriod = 3_000_000;
	        double trigCoefficient = 2.0 * Math.PI / (double) localTimeoutPeriod;
	        double gammaBase = 0.95;
	        double gammaAmplitude = 0.025;
	        double gamma = gammaBase + gammaAmplitude * (1.0 + Math.cos(trigCoefficient * (double) localTimeout));
	        return gamma;
	    }//FinMethod
	   

	
	
	
	public SolutionHelperPenalty solve() {
		
		limit = 100000;
		
		//init Temperature 
		double t = constants.TEMPERATURE_INITIAL;
		convertConstraint(utp);
		int seedMutation = rand.nextInt();
		System.out.println("seedMutation "+seedMutation);
		Mutation mutation = new Mutation(seedMutation,utp,hards,softs);
		SearchPenalty searchPenalty = new SearchPenalty();
		
		solution = new SolutionUTP(utp);
		//initSolution
		solution = initSolution(solution);
		
		//weithed
		ArrayList<Integer> weitg = IntStream.range(0, softs.size()).map(u->1).boxed().collect(Collectors.toCollection(ArrayList<Integer>::new));
		
		//initPenalty
		int softPenalties = solution.evaluate(softs);
		int hardPenalties = solution.evaluate(hards);
		

		//best<- initSolution
		best = new SolutionUTP(solution);
		SolutionHelperPenalty bestHelper = new SolutionHelperPenalty(best,hardPenalties,softPenalties);
		double localBest = 10000000;
		
		int localTimeOut = 0;
		
		SolutionUTP current = new SolutionUTP(solution);
		SolutionHelperPenalty currentHelper = new SolutionHelperPenalty(current,hardPenalties,softPenalties);
		
		System.out.println("penalty "+bestHelper.penalty);
		
		for(int i = 0 ;i <limit ;i++) {
			t = cool(t,hardPenalties,softPenalties);
			//SolutionHelperPenalty solutionPenaltyHelper = new SolutionHelperPenalty(Current,(softPenalties+hardPenalties));
			SolutionUTP candidate = mutation.mutate(currentHelper);
			int hardCandidate = candidate.evaluate(hards);
			//int softCandidate = candidate.evaluate(softs);
			int softCandidate = candidate.evaluate(softs);
			SolutionHelperPenalty candidateHelper = new SolutionHelperPenalty(candidate,hardCandidate,softCandidate);
			
			if (candidateHelper.penalty < bestHelper.penalty){
				best = candidate;
				bestHelper = new SolutionHelperPenalty(best,candidateHelper.penaltyHard,candidateHelper.penaltySoft);
			}
			if(searchPenalty.search(candidateHelper)< localBest) {
				localBest = searchPenalty.search(candidateHelper);
				localTimeOut = 0;
			}
			else {
				localTimeOut += 1;
			}
			
			
			double f0 = bestHelper.penalty;
			double nextSearch = searchPenalty.fstun(candidateHelper.penalty,f0,gamma(localTimeOut));
			double currentSearch = searchPenalty.fstun(currentHelper.penalty,f0,gamma(localTimeOut));
			
			if(searchPenalty.modifiedPenalty(candidateHelper) < searchPenalty.modifiedPenalty(currentHelper)) {
			    current = candidate;  
			    currentHelper = new SolutionHelperPenalty(current,candidate.hard_penalty,candidate.soft_penalty);
			}
			else if ( (nextSearch <= 0.3) && Math.exp((double) (currentSearch - nextSearch) / t) > rand.nextFloat()) {
			    current = candidate;  // Accepter un mauvais mouvement avec une certaine probabilité
			    currentHelper = new SolutionHelperPenalty(current,candidate.hard_penalty,candidate.soft_penalty);
			    //weitg = IntStream.range(0, softs.size()).map(u->1).boxed().collect(Collectors.toCollection(ArrayList<Integer>::new));
			}
			
			/*if(localTimeOut > limit ) {
				localBest = 100000;
				localTimeOut = 0;
				t = constants.TEMPERATURE_RESTART;
				//persistent_constraint = (constraint with age > age limit)
				if(infeasible(current) & (persistent constraint != 0)) {
					fosued constraint = oldest 3 persistent constraint
					current = constraintSearch(current,focused constraint)
				}
				else {
					penalties = scale(penalties)
				}
			}*/
			//System.out.println("penalty candidate "+ candidateHelper.penalty);
		}
		//System.out.println(bestHelper.toString(utp));
		System.out.println("penalty "+bestHelper.penalty);
		return bestHelper;
	}//FinMethod
	
	public SolutionUTP constraintSearch(SolutionUTP solution,ConstraintUTP  focusedconstraint) {
		/*timeOut = 0
			for(int i = 0;i < timeLimit;i++) {
				candidate = randomWalk(solution,distance)
				if(focusedPenalty(candidate)< focusedPenalty(solution) ) {
					solution = candidate
					timeOut = 0
				}
				else{
					timeOut++
				}
			}
		return solution;
	}*/
	return null;
	}//FinMethod
}//FinClass
