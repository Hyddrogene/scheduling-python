package problem;

import java.util.List;
import java.util.stream.IntStream;

import org.chocosolver.solver.Model;
import org.chocosolver.solver.Solution;
import org.chocosolver.solver.variables.IntVar;

public class Problem_poupee_bool {
	protected Model model;
	protected IntVar[] vars;
	protected IntVar varObj;
	protected int[] domaine_poupée;
	protected int nr_poupée_max;
	protected int sum_value;
	
	List<Solution> solutions;
	
	public Problem_poupee_bool(){
		this.model = new Model("Fifty-puzzle(Martin_Chlond)");
		this.domaine_poupée = new int[] {15, 9, 30, 21, 19, 3, 12, 6, 25, 27};
		this.nr_poupée_max = 10;
		this.sum_value = 50;
		
		this.variables();
		this.constraints();
		this.setObjectif();
	}//FinMethod
	
	public void variables() {
		this.vars = model.intVarArray(this.nr_poupée_max, new int[] {0,1});
		this.varObj = model.intVar(0, nr_poupée_max);
	}//FinMethod
	
	public void constraints() {
		IntVar[] produits = IntStream.range(0, nr_poupée_max)
		        .mapToObj(i -> vars[i].mul(domaine_poupée[i]).intVar())
		        .toArray(IntVar[]::new);
		model.sum(produits, "=", sum_value).post();
		model.globalCardinality(vars,new int[] {1}, new IntVar[] {varObj}, false).post();
	}//FinMethod
	
	 
	public void setObjectif() {
		model.setObjective(false, this.varObj);
	}//FinMethod
	
	
	public void solve() {
		
		   System.out.println("=== Statistiques ===");
		   model.getSolver().printStatistics();
		
		 solutions = model.getSolver().findAllSolutions();

		  System.out.println("=== Solutions trouvées ===");
		  int count = 1;
		  for (Solution sol : solutions) {
		        System.out.printf("> Solution %d score %d :%n", count++,sol.getIntVal(varObj));
		        int var_count = 0;
		        for (IntVar var : vars) {
		        	int val = sol.getIntVal(var)*this.domaine_poupée[var_count];
		        	if(val > 0) {
		        		 System.out.printf("   %s = %d%n", var.getName(), val);
		        	}
		            var_count++;
		        }
		        System.out.println();
		    }


	}//FinMethod
	
	
}//FinClass
