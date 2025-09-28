package Local_search;

import java.util.Random;

public class SimulatedAnneling {
	private InstanceUTPArray utp;
	private int[][] solutions;
	private int SIZE_SOLUTION;
	private int nrSolutions;
	
	public SimulatedAnneling(InstanceUTPArray utp,int nrSolutions) {
		this.utp = utp;
		this.nrSolutions = nrSolutions; 
		this.SIZE_SOLUTION = utp.nr_sessions * 3;
		//this.SIZE_SOLUTION = this.utp.nr_sessions + this.utp.nr_classes*2;
	}//FinMethod
	
	public int heartConstraint() {
		int result = 0;
		
		return result;
	}//FinMethod;
	
	public int flattenConstraint() {
		int result = 0;
		
		return result;
	}//FinMethod;
	
	public int solutionCostFunction(int[] solution) {
		int resultat = 0;
		int k = 0;
		for(int i = 0; i < this.utp.nr_sessions ;i++) {
			
		}
		return resultat;
	}//FinMethod
	
	public int costFunction() {
		int result = 0;
		
		return result;
	}//FinMethod
	
	public void initSolutionsSlotSize() {
		Double seedDouble = new Random().nextDouble();
		int seed = seedDouble.intValue();
		System.out.println("Seed : "+seed);
		Random r = new Random(seed);
		this.solutions = new int[this.nrSolutions][this.SIZE_SOLUTION];
		
		for(int i = 0; i < nrSolutions ;i++) {
			int k = 0;
			for(int j = 0; j < utp.nr_sessions ;j++) {
				int part = this.utp.class_part[this.utp.session_class[j]-1];
				this.solutions[i][k] = this.utp.part_slots.get(part-1).get(r.nextInt(0,this.utp.part_slots.get(part-1).size()-1));
				k++;
			}
			for(int j = 0; j < utp.nr_sessions ;j++) {
				int part = this.utp.class_part[this.utp.session_class[j]-1];
				this.solutions[i][k] = this.utp.part_teachers.get(part-1).get(r.nextInt(0,this.utp.part_teachers.get(part-1).size()-1));
				k++;
			}
			for(int j = 0; j < utp.nr_sessions ;j++) {
				int part = this.utp.class_part[this.utp.session_class[j]-1];
				this.solutions[i][k] = this.utp.part_rooms.get(part-1).get(r.nextInt(0,this.utp.part_rooms.get(part-1).size()-1));
				k++;
			}
		}
	}//FinMethod

	public void initSolutionsClassSize() {
		Double seedDouble = new Random().nextDouble();
		int seed = seedDouble.intValue();
		System.out.println("Seed : "+seed);
		Random r = new Random(seed);
		this.solutions = new int[this.nrSolutions][this.SIZE_SOLUTION];

		
		for(int i = 0; i < nrSolutions ;i++) {
			int k = 0;
			for(int j = 0; j < utp.nr_sessions ;j++) {
				int part = this.utp.class_part[this.utp.session_class[j]-1];
				this.solutions[i][k] = this.utp.part_slots.get(part-1).get(r.nextInt(0,this.utp.part_slots.get(part-1).size()-1));
				k++;
			}
			for(int j = 0; j < utp.nr_classes ;j++) {
				int part = this.utp.class_part[j];
				this.solutions[i][k] = this.utp.part_teachers.get(part-1).get(r.nextInt(0,this.utp.part_teachers.get(part-1).size()-1));
				k++;
			}
			for(int j = 0; j < utp.nr_classes ;j++) {
				int part = this.utp.class_part[j];
				this.solutions[i][k] = this.utp.part_rooms.get(part-1).get(r.nextInt(0,this.utp.part_rooms.get(part-1).size()-1));
				k++;
			}
		}
	}//FinMethod
	
	public void solve() {
		
	}//FinMethod
	
	public String printSolution() {
		String out = "";
		return out;
	}//FinMethod
	
}//FinMehod
