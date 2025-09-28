package problem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.chocosolver.solver.Model;
import org.chocosolver.solver.Solution;
import org.chocosolver.solver.Solver;
import org.chocosolver.solver.variables.IntVar;

public class Problem_Triangle extends Problem_abstract{

/*
                 A
	          B    C
	        D    E    F
	      G    H     I   J
	
	
	 Replace the letters A, B, C, D, E, F, G, H, I, J with 
	 with a different number from 0 to 9, such that
	 
	  A + C + F + J   =   A + B + D + G   =   G + H + I + J
	 and
	   A + B + C   =   D + G + H   =   F + I + J
*/	
	protected String[] array_vars = {"A","BC","DEF","GHIJ"};
	protected String[] all_eq_vars = new String[] { "A,C,F,J","A,B,D,G" ,"G,H,I,J"};
	protected String[] all_eq_vars2 = new String[] { "A,B,C" , "D,G,H" ,"F,I,J"};
	
	public Problem_Triangle() {
		model = new Model("Triangle problems");
		this.variables();
		this.constraints();
		this.objectives();
		
	}//FinMethod

	@Override
	public void variables() {
		List<IntVar> tmp = new ArrayList<>();
		varMap = new HashMap<>();
		for(String str_vars:array_vars) {
			for(int i = 0 ; i < str_vars.toCharArray().length;i++) {
				IntVar var = model.intVar(""+str_vars.toCharArray()[i],0,9);
				tmp.add(var);
				varMap.put(""+str_vars.toCharArray()[i], var);
			}
		}
		vars = tmp.toArray(IntVar[]::new);
	}//FinMethod
	
	public void build_constraints(String[] string_tab) {

		IntVar[] sumVars = Arrays.stream(string_tab)
			    .map(t_vars -> {
			        IntVar[] vars = Arrays.stream(t_vars.split(","))
			                              .map(s -> this.varMap.get(s))
			                              .toArray(IntVar[]::new);
			        IntVar sumVar = model.intVar("sum_" + Arrays.toString(t_vars.split(",")), 0, 55);
			        model.sum(vars, "=", sumVar).post();
			        return sumVar;
			    })
			    .toArray(IntVar[]::new);
		model.allEqual(sumVars).post();
		
	}//FinMethod

	@Override
	public void constraints() {
		build_constraints(this.all_eq_vars);
		build_constraints(this.all_eq_vars2);
		model.allDifferent(this.vars).post();
	}//FinMethod

	@Override
	public void objectives() {
		
	}//FinMethod

	@Override
	public void strategies() {
		
	}//FinMethod

	@Override
	public void solve() {
		ArrayList<Solution> solutions = new ArrayList<>();
		Solver solver = model.getSolver();
		/*
		        A
		     B    C
		   D    E    F
		 G    H     I   J
		*/

		while (solver.solve()) {
		    solutions.add(new Solution(model, vars).record());
		}
		int count = 1;
		for(Solution sol: solutions) {
			System.out.printf("Solution %d \n",count);
			int indent = 0;
		    for (String str_vars : array_vars) {
		        char[] chars = str_vars.toCharArray();
		        System.out.print(" ".repeat(array_vars.length-indent));  // Indentation
		        for (char c : chars) {
		            int val = sol.getIntVal(varMap.get(String.valueOf(c)));
		            System.out.print(c + "=" + val + " ");
		        }
		        System.out.println();
		        indent++;
		    }
		    count++;
		}
		
		
		System.out.println("========= Statistics ===========");
		solver.printStatistics();;
		
	}//FinMethod
	
	
	
	
}//FinMethod
