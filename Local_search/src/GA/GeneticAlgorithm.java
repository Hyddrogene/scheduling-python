package GA;

import java.util.ArrayList;
import java.util.Random;

import ConstraintCost.ConstraintModel;
import ConstraintCost.Same_Slot;
import Operator.Mutation.BitFlip;
import ParserUTP.ConstraintUTP;
import ParserUTP.InstanceUTPArray;

public class GeneticAlgorithm {
	//==== Attributes ====
	
	public int nr_sessions;
	public int seed;
	public ParametersGA parameters;
	public ArrayList<SolutionGA> solutions;
	public SolutionGA bestSolution;
	public SolutionGA currentSolution;
	public InstanceUTPArray utp;
	public ArrayList<ConstraintModel> constraints;
	public int bestScore;
	
	//====== METHOD ======
	
	public GeneticAlgorithm(ParametersGA parameters,InstanceUTPArray utp){
		this.parameters = parameters;
		Random rand = new Random();
		this.utp = utp;
		seed = rand.nextInt();
		ConstraintGenerate();
	}//FinMethod
	
	public void ConstraintGenerate() {
		constraints = new ArrayList<ConstraintModel>();
		for(ConstraintUTP cons: utp.constraints) {
			//cons.getSessions()
			if(cons.getConstraint().equals("adjacentRooms")) {
				
			}
			else if(cons.getConstraint().equals("allowedGrids")){
				
			}
			
			else if(cons.getConstraint().equals("allowedRooms")){
				
			}
			else if(cons.getConstraint().equals("allowedSlots") || cons.getConstraint().equals("allowedSlot")){
				
			}
			else if(cons.getConstraint().equals("allowedTeachers")){
				
			}
			else if(cons.getConstraint().equals("assignRooms")){
				
			}
			else if(cons.getConstraint().equals("assignSlot")){
				
			}
			else if(cons.getConstraint().equals("assignTeachers")){
				
			}
			else if(cons.getConstraint().equals("compactness")){
				
			}
			else if(cons.getConstraint().equals("differentDailySlot")){
				
			}
			else if(cons.getConstraint().equals("differentDay")){
				
			}
			else if(cons.getConstraint().equals("differentRooms")){
				
			}
			else if(cons.getConstraint().equals("differentSlot")){
				
			}
			else if(cons.getConstraint().equals("differentTeachers")){
				
			}
			else if(cons.getConstraint().equals("differentWeek")){
				
			}
			else if(cons.getConstraint().equals("differentWeekDay")){
				
			}
			else if(cons.getConstraint().equals("differentWeeklySlot")){
				
			}
			else if(cons.getConstraint().equals("forbiddenRooms")){
				
			}
			else if(cons.getConstraint().equals("forbiddenSlots")||cons.getConstraint().equals("forbiddenSlot")){
				
			}
			else if(cons.getConstraint().equals("forbiddenTeachers")){
				
			}
			else if(cons.getConstraint().equals("gap")){
				
			}
			else if(cons.getConstraint().equals("noOverlap")){
				
			}
			else if(cons.getConstraint().equals("pairwiseNoOverlap")){
				
			}
			else if(cons.getConstraint().equals("periodic")){
				
			}
			else if(cons.getConstraint().equals("requiredRooms")){
				
			}
			else if(cons.getConstraint().equals("requiredTeachers")){
				
			}
			else if(cons.getConstraint().equals("roomSize")){
				
			}
			else if(cons.getConstraint().equals("sameDailySlot")){
				
			}
			else if(cons.getConstraint().equals("sameDay")){
				
			}
			else if(cons.getConstraint().equals("sameRooms")){
				
			}
			else if(cons.getConstraint().equals("sameSlot")){
				Same_Slot s = new Same_Slot(cons);
				constraints.add(s);
			}
			else if(cons.getConstraint().equals("sameTeachers")){
				
			}
			else if(cons.getConstraint().equals("sameWeek")){
				
			}
			else if(cons.getConstraint().equals("sameWeekDay")){
				
			}
			else if(cons.getConstraint().equals("sameWeeklySlot")){
				
			}
			else if(cons.getConstraint().equals("sequenced")){
				
			}
			else if(cons.getConstraint().equals("serviceTeachers")){
				
			}
			else if(cons.getConstraint().equals("workload")){
				
			}
			else if(cons.getConstraint().equals("differentWeek")){
				
			}
			else {
				
			}
		}
	}//FinMethod
	
	public void init() {
		Random rand = new Random(seed);
		solutions = new ArrayList<SolutionGA>();
		Random rand3 = new Random(seed);
		for(int pop = 0 ; pop < parameters.population_size ;pop++) {
			
			
			ArrayList<Integer> x_slot_tmp = new ArrayList<Integer>();
			for(int i = 0; i < utp.nr_sessions ;i++) {
				
				x_slot_tmp.add(utp.part_grid_extension.get(utp.session_part[i]-1).get(rand3.nextInt(0, utp.part_grid_extension.get(utp.session_part[i]-1).size())));
			}
			
			ArrayList<SessionRooms> x_rooms_tmp = new ArrayList<SessionRooms>();
			for(int i = 0; i < utp.nr_sessions ;i++) {
				int rand2 = new Random(seed).nextInt(0, utp.part_rooms.get(utp.session_part[i]-1).size());
				SessionRooms sr =  new SessionRooms(1);
				sr.room = utp.part_rooms.get(utp.session_part[i]-1).get(rand2);
				x_rooms_tmp.add(sr);
			}
			ArrayList<SessionTeachers> x_teachers_tmp = new ArrayList<SessionTeachers>();
			
			for(int i = 0; i < utp.nr_sessions ;i++) {
				int rand2 = new Random(seed).nextInt(0, utp.part_teachers.get(utp.session_part[i]-1).size());
				SessionTeachers sr =  new SessionTeachers(1);
				sr.teacher = utp.part_rooms.get(utp.session_part[i]-1).get(rand2);
				x_teachers_tmp.add(sr);
			}
			
			SolutionGA ga_pop = new SolutionGA(x_slot_tmp,x_rooms_tmp,x_teachers_tmp);
			solutions.add(ga_pop);
		}
		this.bestSolution = this.solutions.get(0);
		bestScore = 10000000;
	}//FinMethod
	
	public SolutionGA mutate(SolutionGA X) {
		BitFlip b = new BitFlip(utp);
		Random rand = new Random();
		if(rand.nextDouble()>this.parameters.taux_mutations) {
			return b.mutate(X, this.parameters.bitflip_taux);
		}
		else {
			return X;
		}
	}//FinMethod
	
	public SolutionGA getBest() {
		
		for(int i=1; i < this.solutions.size() ;i++) {
			int score = this.solutions.get(i).evaluate(constraints);
			if(score<bestScore ) {
				this.bestSolution = new SolutionGA(this.solutions.get(i));
				this.bestScore = score;
			}
		}
		
		return this.bestSolution;
	}//FinMethod
	
	public SolutionGA solve() {
		init();
		bestSolution = getBest();
		for(int i = 0; i < parameters.nr_iterations ;i++ ) {
			ArrayList<SolutionGA> childPool = new ArrayList<>();
			for(int sga = 0; sga < this.solutions.size() ;sga++) {
				childPool.add(mutate(this.solutions.get(sga)));
			}
			solutions = childPool;
			
			bestSolution = getBest();
			System.out.println("Score "+bestSolution.evaluate(constraints));
			System.out.println(bestSolution);
		}
		System.out.println("Score "+bestSolution.evaluate(constraints));
		return bestSolution;
	}//FinMethod

}//FinClass
