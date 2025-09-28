package problem;

import java.util.List;

import org.chocosolver.solver.Model;
import org.chocosolver.solver.Solution;
import org.chocosolver.solver.variables.IntVar;

public class problem_poupee {
	
	protected Model model;
	protected IntVar[] vars;
	protected IntVar varObj;
	protected int[] domaine_poupée;
	protected int nr_poupée_max;
	protected int sum_value;
	
	List<Solution> solutions;
	
	public problem_poupee(){
		this.model = new Model("Fifty-puzzle(Martin_Chlond)");
		this.domaine_poupée = new int[] {0,15, 9, 30, 21, 19, 3, 12, 6, 25, 27};
		this.nr_poupée_max = 10;
		this.sum_value = 50;
		
		this.variables();
		this.constraints();
		this.setObjectif();
	}//FinMethod
	
	public void variables() {
		this.vars = model.intVarArray(nr_poupée_max, domaine_poupée);
		this.varObj = model.intVar(0, nr_poupée_max);
	}
	public void constraints() {
		model.sum(vars, "=",sum_value).post();;
		model.allDifferentExcept0(vars).post();;
		IntVar moins = model.intVar(0,this.nr_poupée_max);
		model.globalCardinality(vars,new int[] {0}, new IntVar[] {moins}, false).post();
		varObj.eq(model.intVar(nr_poupée_max).sub(moins).intVar()).post();;
		model.increasing(vars, 0).post();
		
	}//FinMethod
	
	 
	public void setObjectif() {
		model.setObjective(false, this.varObj);
	}//FinMethod
	
	
	public void solve() {
		 solutions = model.getSolver().findAllSolutions();
		   System.out.println("=== Statistiques ===");
		   model.getSolver().printStatistics();

		  System.out.println("=== Solutions trouvées ===");
		  int count = 1;
		  for (Solution sol : solutions) {
		        System.out.printf("> Solution %d score %d :%n", count++,sol.getIntVal(varObj));
		        for (IntVar var : vars) {
		        	int val = sol.getIntVal(var);
		        	if(val > 0) {
		        		 System.out.printf("   %s = %d%n", var.getName(), val);
		        	}
		        }
		        System.out.println();
		    }


	}//FinMethod
	
	
	
	
}
